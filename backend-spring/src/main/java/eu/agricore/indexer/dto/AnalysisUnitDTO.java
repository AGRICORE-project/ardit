package eu.agricore.indexer.dto;

import java.util.Date;

public class AnalysisUnitDTO {
	
	private String unitReference;
	
	private Integer unitAnalysisNumber;
	
	private Date tmpExtentFrom;
	
	private Date tmpExtentTo;
	
    private Integer aggregationLevel;
    
    private String aggregationUnit;
    
    private Integer aggregationScale;
    
    private Boolean census;
    
    private Integer populationCoverage;
    
    private String statsRepresentativeness;
	
	private String downscalingMethodology;
	
	private Integer areaSizeValue;
	
    private String areaSizeUnit;

    public AnalysisUnitDTO() {}
    
	public AnalysisUnitDTO(String unitReference, Integer unitAnalysisNumber, Date tmpExtentFrom, Date tmpExtentTo,
			Integer aggregationLevel, String aggregationUnit, Integer aggregationScale, Boolean census,
			Integer populationCoverage, String statsRepresentativeness, String downscalingMethodology,
			Integer areaSizeValue, String areaSizeUnit) {
		
		this.unitReference = unitReference;
		this.unitAnalysisNumber = unitAnalysisNumber;
		this.tmpExtentFrom = tmpExtentFrom;
		this.tmpExtentTo = tmpExtentTo;
		this.aggregationLevel = aggregationLevel;
		this.aggregationUnit = aggregationUnit;
		this.aggregationScale = aggregationScale;
		this.census = census;
		this.populationCoverage = populationCoverage;
		this.statsRepresentativeness = statsRepresentativeness;
		this.downscalingMethodology = downscalingMethodology;
		this.areaSizeValue = areaSizeValue;
		this.areaSizeUnit = areaSizeUnit;
	}

	public String getUnitReference() {
		return unitReference;
	}

	public void setUnitReference(String unitReference) {
		this.unitReference = unitReference;
	}

	public Integer getUnitAnalysisNumber() {
		return unitAnalysisNumber;
	}

	public void setUnitAnalysisNumber(Integer unitAnalysisNumber) {
		this.unitAnalysisNumber = unitAnalysisNumber;
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

	public Boolean getCensus() {
		return census;
	}

	public void setCensus(Boolean census) {
		this.census = census;
	}

	public Integer getPopulationCoverage() {
		return populationCoverage;
	}

	public void setPopulationCoverage(Integer populationCoverage) {
		this.populationCoverage = populationCoverage;
	}

	public String getStatsRepresentativeness() {
		return statsRepresentativeness;
	}

	public void setStatsRepresentativeness(String statsRepresentativeness) {
		this.statsRepresentativeness = statsRepresentativeness;
	}

	public String getDownscalingMethodology() {
		return downscalingMethodology;
	}

	public void setDownscalingMethodology(String downscalingMethodology) {
		this.downscalingMethodology = downscalingMethodology;
	}

	public Integer getAreaSizeValue() {
		return areaSizeValue;
	}

	public void setAreaSizeValue(Integer areaSizeValue) {
		this.areaSizeValue = areaSizeValue;
	}

	public String getAreaSizeUnit() {
		return areaSizeUnit;
	}

	public void setAreaSizeUnit(String areaSizeUnit) {
		this.areaSizeUnit = areaSizeUnit;
	}
}
