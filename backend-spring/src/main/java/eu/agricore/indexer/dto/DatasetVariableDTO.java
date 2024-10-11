package eu.agricore.indexer.dto;

import java.util.Date;
import java.util.List;

public class DatasetVariableDTO {
	
	private String name;
	
	private String variableType;
	
	private String measurementUnit;
	
	private Date tmpExtentFrom;
	
	private Date tmpExtentTo;
	
	private String dataOrigin;
	
	private List<String> referenceValues;
	
	private String frequency;
	
	private String frequencyMathRep;
	
	private String mathRepresentation;
	
	private List<AnalysisUnitDTO> analysisUnits;
	
    private Integer aggregationLevel;
    
    private String aggregationUnit;
    
    private Integer aggregationScale;
    
    private String statsRepresentativeness;
	
	private String downscalingMethodology;
	
    private String currency;
	
    private String priceType;
	
	private Integer sizeUnitAmount;
	
    private String sizeUnit;
	
	public DatasetVariableDTO() {}

	public DatasetVariableDTO(String name, String variableType, String measurementUnit, Date tmpExtentFrom, Date tmpExtentTo,
			String dataOrigin, List<String> referenceValues, String frequency, String frequencyMathRep,
			String mathRepresentation, List<AnalysisUnitDTO> analysisUnits, Integer aggregationLevel,
			String aggregationUnit, Integer aggregationScale, String statsRepresentativeness,
			String downscalingMethodology, String currency, String priceType, Integer sizeUnitAmount,
			String sizeUnit) {
		
		this.name = name;
		this.variableType = variableType;
		this.measurementUnit = measurementUnit;
		this.tmpExtentFrom = tmpExtentFrom;
		this.tmpExtentTo = tmpExtentTo;
		this.dataOrigin = dataOrigin;
		this.referenceValues = referenceValues;
		this.frequency = frequency;
		this.frequencyMathRep = frequencyMathRep;
		this.mathRepresentation = mathRepresentation;
		this.analysisUnits = analysisUnits;
		this.aggregationLevel = aggregationLevel;
		this.aggregationUnit = aggregationUnit;
		this.aggregationScale = aggregationScale;
		this.statsRepresentativeness = statsRepresentativeness;
		this.downscalingMethodology = downscalingMethodology;
		this.currency = currency;
		this.priceType = priceType;
		this.sizeUnitAmount = sizeUnitAmount;
		this.sizeUnit = sizeUnit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
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

	public String getDataOrigin() {
		return dataOrigin;
	}

	public void setDataOrigin(String dataOrigin) {
		this.dataOrigin = dataOrigin;
	}

	public List<String> getReferenceValues() {
		return referenceValues;
	}

	public void setReferenceValues(List<String> referenceValues) {
		this.referenceValues = referenceValues;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getFrequencyMathRep() {
		return frequencyMathRep;
	}

	public void setFrequencyMathRep(String frequencyMathRep) {
		this.frequencyMathRep = frequencyMathRep;
	}

	public String getMathRepresentation() {
		return mathRepresentation;
	}

	public void setMathRepresentation(String mathRepresentation) {
		this.mathRepresentation = mathRepresentation;
	}

	public List<AnalysisUnitDTO> getAnalysisUnits() {
		return analysisUnits;
	}

	public void setAnalysisUnits(List<AnalysisUnitDTO> analysisUnits) {
		this.analysisUnits = analysisUnits;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public Integer getSizeUnitAmount() {
		return sizeUnitAmount;
	}

	public void setSizeUnitAmount(Integer sizeUnitAmount) {
		this.sizeUnitAmount = sizeUnitAmount;
	}

	public String getSizeUnit() {
		return sizeUnit;
	}

	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}
}
