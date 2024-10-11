package eu.agricore.indexer.ldap.service;

import eu.agricore.indexer.dto.ContactDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateSmtpEmail;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {


    @Value("${ardit.mail-token}")
    private String mailToken;

    @Value("${ardit.mail-name}")
    private String email;


    public CreateSmtpEmail sendBlueMail(ContactDTO contactDTO) {

        CreateSmtpEmail result = null;
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(mailToken);
        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail(); // SendSmtpEmail | Values to send a transactional email

        try {

            List<SendSmtpEmailTo> recipientList = new ArrayList<>();
            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(email);
            recipientList.add(recipient);
            SendSmtpEmailSender sendSmtpEmailSender = new SendSmtpEmailSender();
            sendSmtpEmailSender.setEmail(email);
            sendSmtpEmailSender.setName("ARDIT");
            sendSmtpEmail.setSubject(contactDTO.getSubject());
            sendSmtpEmail.setTo(recipientList);
            sendSmtpEmail.setSender(sendSmtpEmailSender);
            sendSmtpEmail.setHtmlContent(contactDTO.getBody());
            CreateSmtpEmail email = apiInstance.sendTransacEmail(sendSmtpEmail);
            result = email;

        } catch (ApiException e) {
            e.printStackTrace();
        }

        return result;

    }


    public CreateSmtpEmail sendBlueMailExplicit(String mailRecipient, String mailSubject, String mailBody) {

        CreateSmtpEmail result = null;
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(mailToken);
        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail(); // SendSmtpEmail | Values to send a transactional email

        try {

            List<SendSmtpEmailTo> recipientList = new ArrayList<>();
            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(mailRecipient);
            recipientList.add(recipient);

            SendSmtpEmailSender sendSmtpEmailSender = new SendSmtpEmailSender();
            sendSmtpEmailSender.setEmail(email);
            sendSmtpEmailSender.setName("ARDIT");

            sendSmtpEmail.setSubject(mailSubject);
            sendSmtpEmail.setTo(recipientList);
            sendSmtpEmail.setSender(sendSmtpEmailSender);
            sendSmtpEmail.setHtmlContent(mailBody);

            CreateSmtpEmail email = apiInstance.sendTransacEmail(sendSmtpEmail);
            result = email;

        } catch (ApiException e) {
            e.printStackTrace();
        }

        return result;

    }


//
//
//    @Async
//    public void sendSimpleMessage(ContactDTO contactDTO) throws MessagingException, IOException {
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
//
//        helper.setSubject(contactDTO.getSubject());
//        helper.setText(contactDTO.getBody(), contactDTO.getHtml());
//        helper.setTo(EMAIL_RECEPTORS);
//
//        mailSender.send(helper.getMimeMessage());
//    }
//
//
//
//    @Async
//    public void send(String recipientName, String recipientEmail, String link, String emailBody) {
//
////    	System.out.println("From: " + emitter);
//        System.out.println("To: " + recipientName);
//        System.out.println("To email: " + recipientEmail);
//        System.out.println("Link: " + link);
//
//        String email = emailBody;
//
//
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//            helper.setText(email, true);
//            helper.setTo(recipientEmail);
//            helper.setSubject("Confirm your email");
////            helper.setFrom(emitter);
//            mailSender.send(mimeMessage);
//        } catch (MessagingException e) {
//            throw new IllegalStateException("Failed to send email");
//        }
//    }


}
