package eu.agricore.indexer.dto;

import java.util.Date;
import java.util.List;

public class DataServiceDTO {
	
	private String title;
	
	private String description;
	
    private Date issued;

    private Date modified;
	
	private String creator;
	
	private String publisher;
	
	private String accessRights;
	
	private String endpointUrl;
	
	private String endpointDescription;
	
	private List<String> servedDatasets;
	
	public DataServiceDTO() {}
	
	public DataServiceDTO(String title, String description, Date issued, Date modified, String creator,
			String publisher, String accessRights, String endpointUrl, String endpointDescription,
			List<String> servedDatasets) {
		
		this.title = title;
		this.description = description;
		this.issued = issued;
		this.modified = modified;
		this.creator = creator;
		this.publisher = publisher;
		this.accessRights = accessRights;
		this.endpointUrl = endpointUrl;
		this.endpointDescription = endpointDescription;
		this.servedDatasets = servedDatasets;
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

	public String getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(String accessRights) {
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

	public List<String> getServedDatasets() {
		return servedDatasets;
	}

	public void setServedDatasets(List<String> servedDatasets) {
		this.servedDatasets = servedDatasets;
	}
}
