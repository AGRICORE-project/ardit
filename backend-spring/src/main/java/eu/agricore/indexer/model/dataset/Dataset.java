package eu.agricore.indexer.model.dataset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.agricore.indexer.model.Catalogue;
import eu.agricore.indexer.model.analysisunit.AnalysisUnit;
import eu.agricore.indexer.model.datasetvariable.DatasetVariable;
import eu.agricore.indexer.model.distribution.Distribution;
import eu.agricore.indexer.model.vocabulary.VocabularyTopic;
import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@Entity
@Table(name = "dataset")
public class Dataset {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


	@Column(name = "views")
	private Long view;

	@NotNull()
	@Column(name = "draft", columnDefinition="boolean default false")
	private Boolean draft;
	
	@Column(name = "creation_date_time", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;
	
	@Column(name = "last_update_date")
	@Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDateTime;
	
	@Column(name = "issued")
	@Temporal(TemporalType.DATE)
    private Date issued;
	
	@Column(name = "modified")
	@Temporal(TemporalType.DATE)
    private Date modified;
	
	/*************************GENERAL CATEGORY ATTRIBUTES*************************/
	@NotEmpty()
	@Column(name = "title")
	private String title;
	
	@Size(max = 1000)
	@Column(name = "description", length = 1000)
	private String description;
	
	public enum DatasetType {
		GEOREFERENCED,
		SOCIOECONOMIC
	}

	@Column(name = "dataset_type")
	@Enumerated(EnumType.STRING)
	private DatasetType datasetType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="wp_task", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue wpTask;
    
	@NotEmpty()
	@Column(name = "producer")
    private String producer;
    
	@Size(max = 1000)
	@Column(name = "link", length = 1000)
    private String link;


	@Column(name = "owner", length = 1000)
	private String owner;

	@ManyToMany
	@JoinTable(name = "datasets_languages",
		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> languages;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="periodicity", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue periodicity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="catalogue", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Catalogue catalogue;
	
	@Column(name = "spatial_resolution_in_meters")
    private Float spatialResolutionInMeters;
	
	@Column(name = "temporal_resolution")
    private String temporalResolution;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "dataset_id", referencedColumnName = "id")
	private List<DatasetGenerationActivity> wasGeneratedBy;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "dataset_id", referencedColumnName = "id")
	private List<DatasetReferencedResource> isReferencedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="resource_type", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue resourceType;
	
	/*************************PURPOSE CATEGORY ATTRIBUTES*************************/
	@NotNull()
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tmpExtentFrom;
	
	@NotNull()
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tmpExtentTo;
	
	@ManyToMany
	@JoinTable(name = "datasets_subjects",
		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> subjects;
	
	@ManyToMany
	@JoinTable(name = "datasets_purposes",
		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> purposes;
	
	@ManyToMany
	@JoinTable(name = "datasets_themes",
		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> themes;
	
	/*************************DISTRIBUTION CATEGORY ATTRIBUTES*************************/
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="access_right", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue accessRight;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "datasets_formats",
		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> formats;
	
	@JsonIgnore
	@Size(max = 1000)
	@Column(name = "access_procedures", length = 1000)
	private String accessProcedures;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "dataset_id", referencedColumnName = "id")
	private List<Distribution> distributions;
	
	/*************************RESOLUTION AND REPRESENTATIVENESS CATEGORY ATTRIBUTES*************************/
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="data_frequency", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue dataFrequency;
	
    @Size(max = 1000)
    @Column(name = "statistical_representativeness", length = 1000)
	private String statsRepresentative;
	
	@Column(name = "aggregation_level")
    private Integer aggregationLevel;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="aggregation_unit", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue aggregationUnit;
    
    @Column(name = "aggregation_scale")
    private Integer aggregationScale;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "dataset_id", referencedColumnName = "id")
	private List<AnalysisUnit> analysisUnits;
	
    /*************************VARIABLES CATEGORY ATTRIBUTES*************************/
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "dataset_id", referencedColumnName = "id")
	private List<DatasetVariable> variables;
    
    /*************************GEOCOVERAGE CATEGORY ATTRIBUTES*************************/
    @ManyToMany
	@JoinTable(name = "datasets_continents",
		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> continentalCoverage;

    @ManyToMany
	@JoinTable(name = "datasets_countries",
		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> countryCoverage;

    @ManyToMany
	@JoinTable(name = "datasets_nuts1",
		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> nuts1;
    
    @ManyToMany
   	@JoinTable(name = "datasets_nuts2",
   		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
   		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
   	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
       private List<VocabularyValue> nuts2;
    
    @ManyToMany
   	@JoinTable(name = "datasets_nuts3",
   		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
   		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
   	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
       private List<VocabularyValue> nuts3;
    
    @ManyToMany
   	@JoinTable(name = "datasets_adm1",
   		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
   		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
   	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
       private List<VocabularyValue> adm1;
    
    @ManyToMany
   	@JoinTable(name = "datasets_adm2",
   		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
   		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
   	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
       private List<VocabularyValue> adm2;
    
    /*************************KEYWORDS CATEGORY ATTRIBUTES*************************/
    @ManyToMany
	@JoinTable(name = "datasets_keywords",
		joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "keyword_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Keyword> keywords;
    	
	@PrePersist
	protected void prePersist() {
		this.creationDateTime = new Date();
		this.lastUpdateDateTime = new Date();
	}
	
	@PreUpdate
	protected void preUpdate() {
		this.lastUpdateDateTime = new Date();
	}
    
	public Dataset() {}

	public Dataset(String title, String description, DatasetType datasetType, VocabularyValue wpTask, Boolean draft, Date creationDateTime, Date lastUpdateDateTime, Date issued, Date modified, String producer, String link, VocabularyValue periodicity, 
			Catalogue catalogue, Float spatialResolution, String temporalResolution, VocabularyValue resourceType, Date tmpExtentFrom, Date tmpExtentTo, VocabularyValue accessRight, String accessProcedures,
			VocabularyValue dataFrequency, String statsRepresentative, Integer aggregationLevel, VocabularyValue aggregationUnit, Integer aggregationScale) {
		
		this.title = title;
		this.description = description;
		this.datasetType = datasetType;
		this.wpTask = wpTask;
		this.draft = draft;
		this.creationDateTime = creationDateTime;
		this.lastUpdateDateTime = lastUpdateDateTime;
		this.issued = issued;
		this.modified = modified;
		this.producer = producer;
		this.link = link;
		this.languages = new ArrayList<VocabularyValue>();
		this.periodicity = periodicity;
		this.catalogue = catalogue;
		this.spatialResolutionInMeters = spatialResolution;
		this.temporalResolution = temporalResolution;
		this.resourceType = resourceType;
		this.tmpExtentFrom = tmpExtentFrom;
		this.wasGeneratedBy = new ArrayList<DatasetGenerationActivity>();
		this.isReferencedBy = new ArrayList<DatasetReferencedResource>();
		this.tmpExtentTo = tmpExtentTo;
		this.subjects = new ArrayList<VocabularyValue>();
		this.purposes = new ArrayList<VocabularyValue>();
		this.themes = new ArrayList<VocabularyValue>();
		this.formats = new ArrayList<VocabularyValue>();
		this.distributions = new ArrayList<Distribution>();
		this.continentalCoverage = new ArrayList<VocabularyValue>();
		this.countryCoverage = new ArrayList<VocabularyValue>();
		this.nuts1 = new ArrayList<VocabularyValue>();
		this.nuts2 = new ArrayList<VocabularyValue>();
		this.nuts3 = new ArrayList<VocabularyValue>();
		this.adm1 = new ArrayList<VocabularyValue>();
		this.adm2 = new ArrayList<VocabularyValue>();
		this.variables = new ArrayList<DatasetVariable>();
		this.analysisUnits = new ArrayList<AnalysisUnit>();
		this.accessRight = accessRight;
		this.accessProcedures = accessProcedures;
		this.dataFrequency = dataFrequency;
		this.statsRepresentative = statsRepresentative;
		this.aggregationLevel = aggregationLevel;
		this.aggregationUnit = aggregationUnit;
		this.aggregationScale = aggregationScale;
		this.keywords = new ArrayList<Keyword>();
	}
	
	/*************************GETTERS & SETTERS*************************/
	public Long getId() {
		return id;
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

	public DatasetType getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(DatasetType datasetType) {
		this.datasetType = datasetType;
	}
	
	public VocabularyValue getWpTask() {
		return wpTask;
	}

	public void setWpTask(VocabularyValue wpTask) {
		this.wpTask = wpTask;
	}

	public Boolean getDraft() {
		return draft;
	}

	public void setDraft(Boolean draft) {
		this.draft = draft;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public List<VocabularyValue> getLanguages() {
		return languages;
	}

	public void setLanguages(List<VocabularyValue> languages) {
		this.languages = languages;
	}

	public VocabularyValue getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(VocabularyValue periodicity) {
		this.periodicity = periodicity;
	}

	public Catalogue getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(Catalogue catalogue) {
		this.catalogue = catalogue;
	}
	
	public Float getSpatialResolutionInMeters() {
		return spatialResolutionInMeters;
	}

	public void setSpatialResolutionInMeters(Float spatialResolutionInMeters) {
		this.spatialResolutionInMeters = spatialResolutionInMeters;
	}

	public String getTemporalResolution() {
		return temporalResolution;
	}

	public void setTemporalResolution(String temporalResolution) {
		this.temporalResolution = temporalResolution;
	}

	public List<DatasetGenerationActivity> getWasGeneratedBy() {
		return wasGeneratedBy;
	}

	public void setWasGeneratedBy(List<DatasetGenerationActivity> wasGeneratedBy) {
		this.wasGeneratedBy = wasGeneratedBy;
	}
	
	public void addGenerationActivity(DatasetGenerationActivity generationActivity) {
		this.wasGeneratedBy.add(generationActivity);
	}

	public List<DatasetReferencedResource> getIsReferencedBy() {
		return isReferencedBy;
	}

	public void setIsReferencedBy(List<DatasetReferencedResource> isReferencedBy) {
		this.isReferencedBy = isReferencedBy;
	}
	
	public void addReferencedResource(DatasetReferencedResource referencedResource) {
		this.isReferencedBy.add(referencedResource);
	}

	public VocabularyValue getResourceType() {
		return resourceType;
	}

	public void setResourceType(VocabularyValue resourceType) {
		this.resourceType = resourceType;
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

	public List<VocabularyValue> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<VocabularyValue> subjects) {
		this.subjects = subjects;
	}

	public List<VocabularyValue> getPurposes() {
		return purposes;
	}

	public void setPurposes(List<VocabularyValue> purposes) {
		this.purposes = purposes;
	}

	public List<VocabularyValue> getThemes() {
		return themes;
	}

	public void setThemes(List<VocabularyValue> themes) {
		this.themes = themes;
	}

	public VocabularyValue getAccessRight() {
		return accessRight;
	}

	public void setAccessRight(VocabularyValue accessRight) {
		this.accessRight = accessRight;
	}

	public List<VocabularyValue> getFormats() {
		return formats;
	}

	public void setFormats(List<VocabularyValue> formats) {
		this.formats = formats;
	}

	public String getAccessProcedures() {
		return accessProcedures;
	}

	public void setAccessProcedures(String accessProcedures) {
		this.accessProcedures = accessProcedures;
	}

	public List<Distribution> getDistributions() {
		return distributions;
	}

	public void setDistributions(List<Distribution> distributions) {
		this.distributions = distributions;
	}

	public VocabularyValue getDataFrequency() {
		return dataFrequency;
	}

	public void setDataFrequency(VocabularyValue dataFrequency) {
		this.dataFrequency = dataFrequency;
	}

	public String getStatsRepresentative() {
		return statsRepresentative;
	}

	public void setStatsRepresentative(String statsRepresentative) {
		this.statsRepresentative = statsRepresentative;
	}

	public Integer getAggregationLevel() {
		return aggregationLevel;
	}

	public void setAggregationLevel(Integer aggregationLevel) {
		this.aggregationLevel = aggregationLevel;
	}

	public VocabularyValue getAggregationUnit() {
		return aggregationUnit;
	}

	public void setAggregationUnit(VocabularyValue aggregationUnit) {
		this.aggregationUnit = aggregationUnit;
	}

	public Integer getAggregationScale() {
		return aggregationScale;
	}

	public void setAggregationScale(Integer aggregationScale) {
		this.aggregationScale = aggregationScale;
	}

	public List<AnalysisUnit> getAnalysisUnits() {
		return analysisUnits;
	}

	public void setAnalysisUnits(List<AnalysisUnit> analysisUnits) {
		this.analysisUnits = analysisUnits;
	}

	public List<DatasetVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<DatasetVariable> variables) {
		this.variables = variables;
	}

	public List<VocabularyValue> getContinentalCoverage() {
		return continentalCoverage;
	}

	public void setContinentalCoverage(List<VocabularyValue> continentalCoverage) {
		this.continentalCoverage = continentalCoverage;
	}

	public List<VocabularyValue> getCountryCoverage() {
		return countryCoverage;
	}

	public void setCountryCoverage(List<VocabularyValue> countryCoverage) {
		this.countryCoverage = countryCoverage;
	}

	public List<VocabularyValue> getNuts1() {
		return nuts1;
	}

	public void setNuts1(List<VocabularyValue> nuts1) {
		this.nuts1 = nuts1;
	}

	public List<VocabularyValue> getNuts2() {
		return nuts2;
	}

	public void setNuts2(List<VocabularyValue> nuts2) {
		this.nuts2 = nuts2;
	}

	public List<VocabularyValue> getNuts3() {
		return nuts3;
	}

	public void setNuts3(List<VocabularyValue> nuts3) {
		this.nuts3 = nuts3;
	}

	public List<VocabularyValue> getAdm1() {
		return adm1;
	}

	public void setAdm1(List<VocabularyValue> adm1) {
		this.adm1 = adm1;
	}

	public List<VocabularyValue> getAdm2() {
		return adm2;
	}

	public void setAdm2(List<VocabularyValue> adm2) {
		this.adm2 = adm2;
	}
	
	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	/*************************METHODS TO ADD ITEMS*************************/
	public void addDistribution(Distribution distribution) {
		this.distributions.add(distribution);
	}
	
	public void addAnalysisUnit(AnalysisUnit analysisUnit) {
		this.analysisUnits.add(analysisUnit);
	}

	public void addDatasetVariable(DatasetVariable datasetVariable) {
		this.variables.add(datasetVariable);
	}
	
	public void addKeyword(Keyword keyword) {
		this.keywords.add(keyword);
	}

	public void removeKeyword(Keyword keyword) {
		this.keywords.remove(keyword);
	}

	public void addVocabularyValue(VocabularyValue value, VocabularyTopic topic) {
		switch(topic){
			case LANGUAGE:
				this.languages.add(value);
				break;
			case THEME: 
				this.themes.add(value);
				break;
			case PURPOSE:
				this.purposes.add(value);
				break;
			case SUBJECT:
				this.subjects.add(value);
				break;
			case FORMAT:
				this.formats.add(value);
				break;
			case CONTINENT:
				this.continentalCoverage.add(value);
				break;
			case COUNTRY:
				this.countryCoverage.add(value);
				break;
			case NUTS1:
				this.nuts1.add(value);
				break;
			case NUTS2:
				this.nuts2.add(value);
				break;
			case NUTS3:
				this.nuts3.add(value);
				break;
			case ADM1:
				this.adm1.add(value);
				break;
			case ADM2:
				this.adm2.add(value);
				break;
			default:
				break;
		}
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Long getView() {
		return view;
	}

	public void setView(Long view) {
		this.view = view;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dataset other = (Dataset) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Dataset [title=" + title + ", creationDateTime=" + creationDateTime + ", producer=" + producer
				+ ", link=" + link + ", periodicity=" + periodicity + ", tmpExtentFromDate="
				+ tmpExtentFrom + ", tmpExtentToDate=" + tmpExtentTo + "]";
	}
}
