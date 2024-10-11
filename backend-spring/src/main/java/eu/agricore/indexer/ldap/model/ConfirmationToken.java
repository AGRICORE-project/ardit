package eu.agricore.indexer.ldap.model;

import eu.agricore.indexer.ldap.util.Utils;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createAt;
	
	@Column(name = "expires_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiresAt;
	
	@Column(name = "confirmed_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date confirmedAt;
	
	@ManyToOne
	@JoinColumn(nullable = false, name="app_user_id")
	private AppUser appUser;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private Utils.ConfirmationTokenType type;
		
	public ConfirmationToken() {}

	public ConfirmationToken(String token, Date createAt, Date expiresAt, Date confirmedAt, AppUser appUser, Utils.ConfirmationTokenType confirmationTokenType) {
		
		this.token = token;
		this.createAt = createAt;
		this.expiresAt = expiresAt;
		this.confirmedAt = confirmedAt;
		this.appUser = appUser;
		this.type= confirmationTokenType;
	}

	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Date getConfirmedAt() {
		return confirmedAt;
	}

	public void setConfirmedAt(Date confirmedAt) {
		this.confirmedAt = confirmedAt;
	}
	
	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Utils.ConfirmationTokenType getType() {
		return type;
	}

	public void setType(Utils.ConfirmationTokenType type) {
		this.type = type;
	}
}
