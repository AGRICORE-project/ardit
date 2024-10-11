package eu.agricore.indexer.model.distribution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@Entity
@Table(name = "data_service")
public class DataService {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotEmpty()
	@Column(name = "title")
	private String title;
	
	@Size(max = 1000)
	@Column(name = "description", length = 1000)
	private String description;
	
	@Column(name = "issued")
	@Temporal(TemporalType.DATE)
    private Date issued;
	
	@Column(name = "modified")
	@Temporal(TemporalType.DATE)
    private Date modified;
	
	@Column(name = "creator")
	private String creator;
	
	@Column(name = "publisher")
	private String publisher;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="access_rights", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue accessRights;
	
	@Column(name = "endpoint_url")
	private String endpointUrl;
	
	@Column(name = "endpoint_description")
	private String endpointDescription;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "data_service_id", referencedColumnName = "id")
	private List<DataServiceServedDataset> servedDatasets;

	public DataService() {}
	
	public DataService(@NotEmpty String title, @Size(max = 1000) String description, Date issued, Date modified,
			String creator, String publisher, VocabularyValue accessRights, String endpointUrl, String endpointDescription) {
		
		this.title = title;
		this.description = description;
		this.issued = issued;
		this.modified = modified;
		this.creator = creator;
		this.publisher = publisher;
		this.accessRights = accessRights;
		this.endpointUrl = endpointUrl;
		this.endpointDescription = endpointDescription;
		this.servedDatasets = new ArrayList<DataServiceServedDataset>();
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getIssued() {
		return issued;
	}

	public void setIssued(Date issued) {
		this.issued = issued;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public VocabularyValue getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(VocabularyValue accessRights) {
		this.accessRights = accessRights;
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public String getEndpointDescription() {
		return endpointDescription;
	}

	public void setEndpointDescription(String endpointDescription) {
		this.endpointDescription = endpointDescription;
	}

	public List<DataServiceServedDataset> getServedDatasets() {
		return servedDatasets;
	}
	
	public void setServedDatasets(List<DataServiceServedDataset> servedDatasets) {
		this.servedDatasets = servedDatasets;
	}

	public void addServedDataset(DataServiceServedDataset servedDataset) {
		this.servedDatasets.add(servedDataset);
	}
}
