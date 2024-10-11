package eu.agricore.indexer.controller.api;

import eu.agricore.indexer.dto.ContactDTO;
import eu.agricore.indexer.ldap.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

@ApiIgnore
@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {

    @Autowired
    private EmailService emailService;


    @PostMapping("")
    public ResponseEntity<Object> contactEmail(@RequestBody @Valid ContactDTO contactDTO) {

        emailService.sendBlueMail(contactDTO);
        return ResponseEntity.ok()
                .body(contactDTO);
    }

}
