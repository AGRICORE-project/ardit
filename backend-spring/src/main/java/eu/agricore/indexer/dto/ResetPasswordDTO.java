package eu.agricore.indexer.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ResetPasswordDTO {

    @NotEmpty
    @NotNull
    private String email;


    public ResetPasswordDTO() {
    }

    public ResetPasswordDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}