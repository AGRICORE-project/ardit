package eu.agricore.indexer.ldap.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty()
    @Column(name = "username")
    private String username;

    @NotEmpty()
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "disabled")
    private Boolean disabled;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "delete_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Column(name = "dataset_subscription")
    private Boolean datasetSubscription;

    public AppUser() {
    }

    public AppUser(@NotEmpty String username, @NotEmpty String email, Date lastLogin, Boolean verified,
                   Boolean disabled, Boolean deleted, Date deleteAt,Boolean datasetSubscription) {

        this.username = username;
        this.email = email;
        this.lastLogin = lastLogin;
        this.verified = verified;
        this.disabled = disabled;
        this.deleted = deleted;
        this.deleteAt = deleteAt;
        this.datasetSubscription=datasetSubscription;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }

    public Boolean getDatasetSubscription() {
        return datasetSubscription;
    }

    public void setDatasetSubscription(Boolean datasetSubscription) {
        this.datasetSubscription = datasetSubscription;
    }
}
