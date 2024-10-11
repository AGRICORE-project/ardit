package eu.agricore.indexer.model.distribution;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@Entity
@Table(name = "distribution")
public class Distribution {
	
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
	
	@Column(name = "license")
	private String license;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="access_rights", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue accessRights;
	
	@Size(max = 1000)
	@Column(name = "access_procedures", length = 1000)
	private String accessProcedures;
	
	@Column(name = "access_url")
	private String accessUrl;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="data_Service_id", referencedColumnName = "id")
    private DataService accessService;
	
	@Column(name = "download_url")
	private String downloadUrl;
	
	@Column(name = "byte_size")
	private Float byteSize;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="format", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue format;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="compress_format", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue compressFormat;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="packaging_format", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue packagingFormat;

	public Distribution() {}

	public Distribution(@NotEmpty String title, String description, Date issued, Date modified, String license,
			VocabularyValue accessRights, String accessProcedures, String accessUrl, DataService accessService, String downloadUrl, Float byteSize, 
			VocabularyValue format, VocabularyValue compressFormat, VocabularyValue packagingFormat) {
		
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

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public VocabularyValue getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(VocabularyValue accessRights) {
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

	public VocabularyValue getFormat() {
		return format;
	}

	public void setFormat(VocabularyValue format) {
		this.format = format;
	}

	public VocabularyValue getCompressFormat() {
		return compressFormat;
	}

	public void setCompressFormat(VocabularyValue compressFormat) {
		this.compressFormat = compressFormat;
	}

	public VocabularyValue getPackagingFormat() {
		return packagingFormat;
	}

	public void setPackagingFormat(VocabularyValue packagingFormat) {
		this.packagingFormat = packagingFormat;
	}

	public DataService getAccessService() {
		return accessService;
	}

	public void setAccessService(DataService accessService) {
		this.accessService = accessService;
	}
}
