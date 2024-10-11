package eu.agricore.indexer.dto;

import java.util.Date;
import java.util.List;

public class DatasetSimplifiedDTO {
	
	private Long id;
	
	private Boolean draft;
	
	private Date creationDateTime;
		
	private Date lastUpdateDateTime; 
	
	private String title;
	
	private String datasetType;
    
    private String producer;
    
    private String periodicity;
    
    private Date tmpExtentFrom;
	
	private Date tmpExtentTo;
	
	private List<String> formats;

	private String owner;
	
	public DatasetSimplifiedDTO() {}

	public DatasetSimplifiedDTO(Long id, Boolean draft, Date creationDateTime, Date lastUpdateDateTime,
			String title, String datasetType, String producer, String periodicity, Date tmpExtentFrom, Date tmpExtentTo,
			List<String> formats, String owner) {
		
		this.id = id;
		this.draft = draft;
		this.creationDateTime = creationDateTime;
		this.lastUpdateDateTime = lastUpdateDateTime;
		this.title = title;
		this.datasetType = datasetType;
		this.producer = producer;
		this.periodicity = periodicity;
		this.tmpExtentFrom = tmpExtentFrom;
		this.tmpExtentTo = tmpExtentTo;
		this.formats = formats;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getDraft() {
		return draft;
	}

	public void setDraft(Boolean draft) {
		this.draft = draft;
	}

	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public Date getLastUpdateDateTime() {
		return lastUpdateDateTime;
	}

	public void setLastUpdateDateTime(Date lastUpdateDateTime) {
		this.lastUpdateDateTime = lastUpdateDateTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public Date getTmpExtentFrom() {
		return tmpExtentFrom;
	}

	public void setTmpExtentFrom(Date tmpExtentFrom) {
		this.tmpExtentFrom = tmpExtentFrom;
	}

	public Date getTmpExtentTo() {
		return tmpExtentTo;
	}

	public void setTmpExtentTo(Date tmpExtentTo) {
		this.tmpExtentTo = tmpExtentTo;
	}

	public List<String> getFormats() {
		return formats;
	}

	public void setFormats(List<String> formats) {
		this.formats = formats;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
