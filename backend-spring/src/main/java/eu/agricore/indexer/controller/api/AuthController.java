package eu.agricore.indexer.controller.api;

import eu.agricore.indexer.dto.NewPasswordDTO;
import eu.agricore.indexer.dto.ResetPasswordDTO;
import eu.agricore.indexer.ldap.dto.AuthRequest;
import eu.agricore.indexer.ldap.dto.AuthResponse;
import eu.agricore.indexer.ldap.dto.CurrentUserCredentials;
import eu.agricore.indexer.ldap.dto.LdapUserDTO;
import eu.agricore.indexer.ldap.model.AppUser;
import eu.agricore.indexer.ldap.model.ConfirmationToken;
import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.ldap.service.AppUserService;
import eu.agricore.indexer.ldap.service.ConfirmationTokenService;
import eu.agricore.indexer.ldap.service.LdapUserService;
import eu.agricore.indexer.ldap.util.Utils;
import eu.agricore.indexer.service.UserManagementService;
import eu.agricore.indexer.util.JWTService;
import eu.agricore.indexer.util.ResponseCookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private LdapUserService ldapService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private ResponseCookieUtil responseCookieUtil;

    @Autowired
    private UserManagementService userManagementService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest, HttpServletResponse httpResponse) throws URISyntaxException {

        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        // Authenticate by LDAP
        Boolean authenticated = ldapService.authenticate(username, password);
        if (!authenticated) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }

        // Check if user is verified
        if (!appUserService.checkIfUserIsVerified(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your email address is not verified");
        }

        // Check if the user account is disabled
        if (!appUserService.checkIfUserIsDisabled(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your user account has been blocked");
        }

        // LDAP roles recovery
        Set<String> roles = ldapService.getRolesByUser(username);

        // Token generation
        String token = jwtService.generateToken(username, roles);

        // AuthResponse
        AuthResponse res = new AuthResponse(token);

        // Cookie to store jwt token value on the client browser
        ResponseCookie cookie = responseCookieUtil.createTokenCookie(token);
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        appUserService.setAppUserLastLogin(username);

        return ResponseEntity.ok()
                .body(res);
    }

    @GetMapping("/me")
    public ResponseEntity<LdapUserDTO> getCurrentUser() {

        LdapUser user = getCurrentLdapUser();
        LdapUserDTO userDTO = new LdapUserDTO(user);

        if (user != null) {
            AppUser appUser = appUserService.findAppUserByUsername(user.getUsername());
            userDTO.setSubscribed(appUser.getDatasetSubscription());
        }

        return ResponseEntity.ok()
                .body(userDTO);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe() {

        LdapUser user = getCurrentLdapUser();
        String username = user.getUsername();
        AppUser appUser = appUserService.findAppUserByEmail(user.getEmail());

        // Check if user is verified
        if (!appUserService.checkIfUserIsVerified(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your email address is not verified");
        }

        // Check if the user account is disabled
        if (!appUserService.checkIfUserIsDisabled(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your user account has been blocked");
        }

        Boolean subscription = appUserService.enableDatasetSubscriptionAppUser(appUser);
        String requestBody = "Successfully Subscribed";

        return ResponseEntity.ok().body("\"Subscribed.\"");

    }


    @DeleteMapping("/me")
    public ResponseEntity<?> deleteCurrentUser() {

        String username = null;
        LdapUser user = getCurrentLdapUser();

        if (user != null) {
            username = user.getUsername();
        }

        // Check if user is verified
        if (!appUserService.checkIfUserIsVerified(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your email address is not verified");
        }

        // Check if the user account is disabled
        if (!appUserService.checkIfUserIsDisabled(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your user account has been blocked");
        }

        userManagementService.delete(user);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/recoverPassword")
    public ResponseEntity<?> recoverPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO /* @RequestParam("email") String email */) {

        String email = resetPasswordDTO.getEmail();

        AppUser appUser = appUserService.findAppUserByEmail(email);

        if (appUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email can not be found in the database");
        }

        // Check if user is verified
        if (!appUserService.checkIfUserIsVerified(appUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your email address is not verified");
        }

        // Check if the user account is disabled
        if (!appUserService.checkIfUserIsDisabled(appUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your user account has been blocked");
        }

        // if token already exist delete the old ones
        List<ConfirmationToken> confirmationTokenList = confirmationTokenService.getTokenPasswordByUser(appUser.getId());
        if (confirmationTokenList != null && !confirmationTokenList.isEmpty()) {
            for (ConfirmationToken confirmationToken : confirmationTokenList) {
                confirmationTokenService.deleteToken(confirmationToken);
            }
        }

        appUserService.generateUrlResetPassword(appUser);
        return ResponseEntity.ok().body("\"We have sent an email to reset your password, if you can't find it, check your spam folder.\"");

    }


    @PostMapping("/newPassword")
    public ResponseEntity<?> newPassword(@RequestBody @Valid NewPasswordDTO newPasswordDTO) {

        String token = newPasswordDTO.getResetPasswordToken();
        String newPassword = newPasswordDTO.getNewPassword();

        Optional<ConfirmationToken> confirmationToken = confirmationTokenService.getToken(token);

        if (confirmationToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token does not exists");
        }

        // Check if token has been confirmed
        if (confirmationToken.get().getConfirmedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password already changed");
        }

        // Check if token has expired
        Date currentDate = new Date();
        if (currentDate.after(confirmationToken.get().getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired");

        }

        if (!(confirmationToken.get().getType().equals(Utils.ConfirmationTokenType.PASSWORD))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token type does not match");
        }

        AppUser appUser = confirmationToken.get().getAppUser();
        String username = appUser.getUsername();

        // Check if user is verified
        if (!appUserService.checkIfUserIsVerified(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your email address is not verified");
        }

        // Check if the user account is disabled
        if (!appUserService.checkIfUserIsDisabled(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your user account has been blocked");
        }

        appUserService.updatePassword(username, newPassword, confirmationToken.get());

        return ResponseEntity.ok().body("\"Your password has been changed, you can sign in now.\"");

    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid LdapUser user, HttpServletResponse httpResponse) {

        AppUser appUser = appUserService.findAppUserByEmail(user.getEmail());

        if (appUser != null) {
            Date now = new Date();
            List<ConfirmationToken> registerTokenUser = confirmationTokenService.getTokenRegisterByUser(appUser.getId());
            if (registerTokenUser != null && !registerTokenUser.isEmpty()) {
                for (ConfirmationToken confirmationToken : registerTokenUser) {
                    if (now.getTime() > confirmationToken.getExpiresAt().getTime()) {
                        // if token is expired and the email i already in BBDD, we delete the BBDD user and start with the normal register process
                        appUserService.deleteUserFull(appUser, confirmationToken);
                    } else if (!appUser.getUsername().equals(user.getUsername())) {
                        // if there is a token associated with the email and the are different, we throw email already in use error
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email is already in use");
                    } else {
                        // if there is a token which is not expired associated to that email, until the token expired we throw an error of pending token
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email is already in use and has a pending confirmation token");
                    }
                }
            } else {
                // if there is a token confirmed to the email, we throw email already in use exception
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email is already in use");

            }

        }

        // Auth by LDAP
        user = ldapService.create(user);

        // Create app user
        appUserService.registerAppUser(user);

        return ResponseEntity.ok().body("\"Thank you " + user.getUsername() + " for signing in. A verification email has been sent to your email address.\"");
    }


    @GetMapping("/register/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {

        Optional<ConfirmationToken> confirmationToken = confirmationTokenService.getToken(token);

        // Check if token exists
        if (confirmationToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token does not exists");
        }

        // Check if token has been confirmed
        if (confirmationToken.get().getConfirmedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address already confirmed");
        }

        // Check if token has expired
        Date currentDate = new Date();
        if (currentDate.after(confirmationToken.get().getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired");
        }


        if (!(confirmationToken.get().getType().equals(Utils.ConfirmationTokenType.REGISTRATION))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token type does not match");
        }

        // Confirm token and validate user account
        ConfirmationToken confirmedToken = confirmationTokenService.confirmToken(confirmationToken.get());
        appUserService.enableAppUser(confirmedToken.getAppUser());

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("\"Your email address has been confirmed succesfully. You can log in now.\"");
    }


    @PutMapping("/me")
    public ResponseEntity<?> modifyCurrentUser(@RequestBody @Valid LdapUserDTO user) {

        if (!isCurrentUser(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }

        // Update current user data
        Optional<LdapUser> res = ldapService.update(user);
        if (res.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user not found");
        }

        LdapUserDTO userDTO = new LdapUserDTO(res.get()); // Return a DTO to hide the password
        // update db user
        appUserService.updateAppUser(user);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> modifyCurrentUserPassword(@RequestBody @Valid CurrentUserCredentials user) {

        String username = user.getUsername();
        String oldPassword = user.getCurrentPassword();
        String newPassword = user.getNewPassword();
        String newPasswordConfirm = user.getNewPasswordConfirm();

        // Check that the user makes the change on his own account
        if (!isCurrentUser(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }

        // Check that the old password is correct to verify the user
        if (!ldapService.authenticate(username, oldPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad credentials");
        }

        // Check that new password matches
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password does not match");
        }

        // Update the password
        Optional<LdapUser> res = ldapService.updatePassword(username, newPassword);

        LdapUserDTO userDTO = new LdapUserDTO(res.get()); // Return a DTO to hide the password

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse httpResponse) throws URISyntaxException {

        //Remove Agricore token cookie from the user's browser
        ResponseCookie cookie = responseCookieUtil.deleteTokenCookie();
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }

    private Boolean isCurrentUser(final String username) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getPrincipal().toString();
        return currentUsername.equals(username);
    }

    private String getCurrentUsername() {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getPrincipal().toString();
        return currentUsername;
    }

    private LdapUser getCurrentLdapUser() throws ResponseStatusException {
        Optional<LdapUser> optionalUser = getOptionalCurrentLdapUser();
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found");
        }
        return optionalUser.get();
    }

    private Optional<LdapUser> getOptionalCurrentLdapUser() throws ResponseStatusException {
        String username = getCurrentUsername();
        Optional<LdapUser> optionalUser = ldapService.find(username);
        return optionalUser;
    }
}
