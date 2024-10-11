package eu.agricore.indexer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import eu.agricore.indexer.model.dataset.Dataset;

@Entity
@Table(name = "comment")
public class Comment {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "created_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
	
	@Column(name = "last_update")
	@Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
	
	@JsonIgnore
	@Column(name = "delete_at")
	@Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;
	
	@Column(name = "deleted")
	private Boolean deleted;
	
	@NotEmpty
	@Column(name = "user_id")
	private String userId;
	
	@NotEmpty
	@Size(max = 500)
	@Column(name = "content")
	private String content;
	
	@Column(name = "parent_id")
	private Long parentId;
	
	@Column(name = "root_id")
	private Long rootId;
	
	@Column(name = "level")
	@Min(value = 0)
	@Max(value = 2)
	private Integer level;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="dataset_id", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Dataset dataset;
	
	@JsonInclude()
	@Transient
	private List<Comment> replies;
	
	@PrePersist
	protected void prePersist() {
		this.createdAt = new Date();
	}

	public Comment() {}

	public Comment(Long id, Boolean deleted, @NotEmpty String userId, @NotEmpty String content, Long parentId, Long rootId, Integer level, Dataset dataset) {
		
		this.id = id;
		this.deleted = deleted;
		this.userId = userId;
		this.content = content;
		this.parentId = parentId;
		this.rootId = rootId;
		this.dataset = dataset;
		this.level = level;
		this.replies = new ArrayList<Comment>();
	}

	public Long getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getRootId() {
		return rootId;
	}

	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public void setRootId() {
		this.setRootId(this.id);
	}

	public List<Comment> getReplies() {
		return replies;
	}

	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}
	
	public void addReply(Comment reply) {
		this.replies.add(reply);
	}
	
	public void deleteReply(Comment reply) {
		this.replies.remove(reply);
	}
}
