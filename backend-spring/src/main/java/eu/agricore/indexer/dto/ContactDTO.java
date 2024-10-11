package eu.agricore.indexer.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ContactDTO {

    @NotEmpty
    @NotNull
    private String body;
    @NotEmpty
    @NotNull
    private String subject;
    @NotNull
    private Boolean html;


    public ContactDTO() {
    }


    public ContactDTO(String body, String subject, Boolean html) {
        this.body = body;
        this.subject = subject;
        this.html = html;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getHtml() {
        return html;
    }

    public void setHtml(Boolean html) {
        this.html = html;
    }
}
