package eu.agricore.indexer.dto;

import java.util.Date;
import java.util.List;

public class DatasetDTO {
	
	private Long id;

	private Boolean draft;
	
    private Date creationDateTime;
	
    private Date lastUpdateDateTime;
	
    private Date issued;
    
    private Date modified;
	
	/*************************GENERAL CATEGORY ATTRIBUTES*************************/
	private String title;
	
	private String description;
	
	private String datasetType;
	
	private String wpTask;
    
    private String producer;
    
    private String link;
	
    private List<String> languages;
    
    private String periodicity;
    
    private Long catalogueId;
    
	private String catalogueTitle;
	
    private Float spatialResolutionInMeters;
	
    private String temporalResolution;
	
	private List<String> wasGeneratedBy;
	
	private List<String> isReferencedBy;
	
    private String resourceType;
	
	/*************************PURPOSE CATEGORY ATTRIBUTES*************************/
	private Date tmpExtentFrom;
	
	private Date tmpExtentTo;
	
    private List<String> subjects;
	
    private List<String> purposes;
	
    private List<String> themes;
	
	/*************************DISTRIBUTION CATEGORY ATTRIBUTES*************************/
	private List<DistributionDTO> distributions;
	
	/*************************RESOLUTION AND REPRESENTATIVENESS CATEGORY ATTRIBUTES*************************/
	private String statsRepresentative;
	
    private Integer aggregationLevel;
    
    private String aggregationUnit;
    
    private Integer aggregationScale;
    
	private List<AnalysisUnitDTO> analysisUnits;
	
    /*************************VARIABLES CATEGORY ATTRIBUTES*************************/
	private List<DatasetVariableDTO> variables;
    
    /*************************GEOCOVERAGE CATEGORY ATTRIBUTES*************************/
    private List<String> continentalCoverage;

    private List<String> countryCoverage;

    private List<String> nuts1;
    
    private List<String> nuts2;
    
    private List<String> nuts3;
    
    private List<String> adm1;
    
    private List<String> adm2;
    
    /*************************KEYWORDS CATEGORY ATTRIBUTES*************************/
    private List<String> keywords;

	private String owner;

	private Long view;
    
	public DatasetDTO() {}

	public DatasetDTO(Long id, Boolean draft, Date creationDateTime, Date lastUpdateDateTime, Date issued, Date modified,
			String title, String description, String datasetType, String wpTask, String producer, String link,
			List<String> languages, String periodicity, Long catalogueId, String catalogueTitle,
			Float spatialResolutionInMeters, String temporalResolution, List<String> wasGeneratedBy,
			List<String> isReferencedBy, String resourceType, Date tmpExtentFrom, Date tmpExtentTo,
			List<String> subjects, List<String> purposes, List<String> themes, List<DistributionDTO> distributions,
			String statsRepresentative, Integer aggregationLevel, String aggregationUnit, Integer aggregationScale,
			List<AnalysisUnitDTO> analysisUnits, List<DatasetVariableDTO> variables, List<String> continentalCoverage,
			List<String> countryCoverage, List<String> nuts1, List<String> nuts2, List<String> nuts3, List<String> adm1,
			List<String> adm2, List<String> keywords, String owner) {

		this.id = id;
		this.draft = draft;
		this.creationDateTime = creationDateTime;
		this.lastUpdateDateTime = lastUpdateDateTime;
		this.issued = issued;
		this.modified = modified;
		this.title = title;
		this.description = description;
		this.datasetType = datasetType;
		this.wpTask = wpTask;
		this.producer = producer;
		this.link = link;
		this.languages = languages;
		this.periodicity = periodicity;
		this.catalogueId = catalogueId;
		this.catalogueTitle = catalogueTitle;
		this.spatialResolutionInMeters = spatialResolutionInMeters;
		this.temporalResolution = temporalResolution;
		this.wasGeneratedBy = wasGeneratedBy;
		this.isReferencedBy = isReferencedBy;
		this.resourceType = resourceType;
		this.tmpExtentFrom = tmpExtentFrom;
		this.tmpExtentTo = tmpExtentTo;
		this.subjects = subjects;
		this.purposes = purposes;
		this.themes = themes;
		this.distributions = distributions;
		this.statsRepresentative = statsRepresentative;
		this.aggregationLevel = aggregationLevel;
		this.aggregationUnit = aggregationUnit;
		this.aggregationScale = aggregationScale;
		this.analysisUnits = analysisUnits;
		this.variables = variables;
		this.continentalCoverage = continentalCoverage;
		this.countryCoverage = countryCoverage;
		this.nuts1 = nuts1;
		this.nuts2 = nuts2;
		this.nuts3 = nuts3;
		this.adm1 = adm1;
		this.adm2 = adm2;
		this.keywords = keywords;
		this.owner = owner;
	}
	
	/*************************GETTERS & SETTERS*************************/

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

	public String getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}

	public String getWpTask() {
		return wpTask;
	}

	public void setWpTask(String wpTask) {
		this.wpTask = wpTask;
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

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public Long getCatalogueId() {
		return catalogueId;
	}

	public void setCatalogueId(Long catalogueId) {
		this.catalogueId = catalogueId;
	}

	public String getCatalogueTitle() {
		return catalogueTitle;
	}

	public void setCatalogueTitle(String catalogueTitle) {
		this.catalogueTitle = catalogueTitle;
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

	public List<String> getWasGeneratedBy() {
		return wasGeneratedBy;
	}

	public void setWasGeneratedBy(List<String> wasGeneratedBy) {
		this.wasGeneratedBy = wasGeneratedBy;
	}

	public List<String> getIsReferencedBy() {
		return isReferencedBy;
	}

	public void setIsReferencedBy(List<String> isReferencedBy) {
		this.isReferencedBy = isReferencedBy;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
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

	public List<String> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	public List<String> getPurposes() {
		return purposes;
	}

	public void setPurposes(List<String> purposes) {
		this.purposes = purposes;
	}

	public List<String> getThemes() {
		return themes;
	}

	public void setThemes(List<String> themes) {
		this.themes = themes;
	}

	public List<DistributionDTO> getDistributions() {
		return distributions;
	}

	public void setDistributions(List<DistributionDTO> distributions) {
		this.distributions = distributions;
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

	public String getAggregationUnit() {
		return aggregationUnit;
	}

	public void setAggregationUnit(String aggregationUnit) {
		this.aggregationUnit = aggregationUnit;
	}

	public Integer getAggregationScale() {
		return aggregationScale;
	}

	public void setAggregationScale(Integer aggregationScale) {
		this.aggregationScale = aggregationScale;
	}

	public List<AnalysisUnitDTO> getAnalysisUnits() {
		return analysisUnits;
	}

	public void setAnalysisUnits(List<AnalysisUnitDTO> analysisUnits) {
		this.analysisUnits = analysisUnits;
	}

	public List<DatasetVariableDTO> getVariables() {
		return variables;
	}

	public void setVariables(List<DatasetVariableDTO> variables) {
		this.variables = variables;
	}

	public List<String> getContinentalCoverage() {
		return continentalCoverage;
	}

	public void setContinentalCoverage(List<String> continentalCoverage) {
		this.continentalCoverage = continentalCoverage;
	}

	public List<String> getCountryCoverage() {
		return countryCoverage;
	}

	public void setCountryCoverage(List<String> countryCoverage) {
		this.countryCoverage = countryCoverage;
	}

	public List<String> getNuts1() {
		return nuts1;
	}

	public void setNuts1(List<String> nuts1) {
		this.nuts1 = nuts1;
	}

	public List<String> getNuts2() {
		return nuts2;
	}

	public void setNuts2(List<String> nuts2) {
		this.nuts2 = nuts2;
	}

	public List<String> getNuts3() {
		return nuts3;
	}

	public void setNuts3(List<String> nuts3) {
		this.nuts3 = nuts3;
	}

	public List<String> getAdm1() {
		return adm1;
	}

	public void setAdm1(List<String> adm1) {
		this.adm1 = adm1;
	}

	public List<String> getAdm2() {
		return adm2;
	}

	public void setAdm2(List<String> adm2) {
		this.adm2 = adm2;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
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
}
