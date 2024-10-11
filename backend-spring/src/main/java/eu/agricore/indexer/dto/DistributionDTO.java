package eu.agricore.indexer.dto;

import java.util.Date;

public class DistributionDTO {
    
	private String title;
	
	private String description;
	
    private Date issued;
	
    private Date modified;
	
	private String license;
	
	private String accessRights;
	
	private String accessProcedures;
	
	private String accessUrl;
	
    private DataServiceDTO accessService;
	
	private String downloadUrl;
	
	private Float byteSize;
	
	private String format;
	
	private String compressFormat;
	
	private String packagingFormat;
	
	public DistributionDTO() {}
	
	public DistributionDTO(String title, String description, Date issued, Date modified, String license,
			String accessRights, String accessProcedures, String accessUrl, DataServiceDTO accessService,
			String downloadUrl, Float byteSize, String format, String compressFormat, String packagingFormat) {
		
		this.title = title;
		this.description = description;
		this.issued = issued;
		this.modified = modified;
		this.license = license;
		this.accessRights = accessRights;
		this.accessProcedures = accessProcedures;
		this.accessUrl = accessUrl;
		this.accessService = accessService;
		this.downloadUrl = downloadUrl;
		this.byteSize = byteSize;
		this.format = format;
		this.compressFormat = compressFormat;
		this.packagingFormat = packagingFormat;
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

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(String accessRights) {
		this.accessRights = accessRights;
	}

	public String getAccessProcedures() {
		return accessProcedures;
	}

	public void setAccessProcedures(String accessProcedures) {
		this.accessProcedures = accessProcedures;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public DataServiceDTO getAccessService() {
		return accessService;
	}

	public void setAccessService(DataServiceDTO accessService) {
		this.accessService = accessService;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public Float getByteSize() {
		return byteSize;
	}

	public void setByteSize(Float byteSize) {
		this.byteSize = byteSize;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCompressFormat() {
		return compressFormat;
	}

	public void setCompressFormat(String compressFormat) {
		this.compressFormat = compressFormat;
	}

	public String getPackagingFormat() {
		return packagingFormat;
	}

	public void setPackagingFormat(String packagingFormat) {
		this.packagingFormat = packagingFormat;
	}
}
