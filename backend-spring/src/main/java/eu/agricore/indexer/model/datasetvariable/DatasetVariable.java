package eu.agricore.indexer.model.datasetvariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import eu.agricore.indexer.model.analysisunit.AnalysisUnit;
import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "variableType")
@JsonSubTypes({
    @JsonSubTypes.Type(value=GeoreferencedDatasetVariable.class, name = "GEOREFERENCED"),
    @JsonSubTypes.Type(value=SocioeconomicDatasetVariable.class, name = "SOCIOECONOMIC"),
    @JsonSubTypes.Type(value=PriceDatasetVariable.class, name = "PRICE")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "variable_type")
@Table(name = "dataset_variable")
public class DatasetVariable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotEmpty()
	@Size(max = 500)
	@Column(name = "name", length = 500)
	private String name;
	
	@Column(name = "measurement_unit")
	private String measurementUnit;
	
	@NotNull
	@Column(name = "tmp_extent_from")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tmpExtentFrom;
	
	@NotNull
	@Column(name = "tmp_extent_to")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tmpExtentTo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="data_origin", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue dataOrigin;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "dataset_variable_id", referencedColumnName = "id")
	private List<VariableReferenceValue> referenceValues;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="frequency", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue frequency;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="frequency_math_rep", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue frequencyMathRep;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="math_representation", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private VocabularyValue mathRepresentation;
	
	@ManyToMany
	@JoinTable(name = "analysisunits_variables",
		joinColumns = @JoinColumn(name = "dataset_variable_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "analysis_unit_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<AnalysisUnit> analysisUnits;
	
	@Column(name = "aggregation_level")
    private Integer aggregationLevel;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="aggregation_unit", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue aggregationUnit;
    
    @Column(name = "aggregation_scale")
    private Integer aggregationScale;
    
    @Size(max = 1000)
    @Column(name = "statistical_representativeness", length = 1000)
    private String statsRepresentativeness;
	
	@Size(max = 1000)
	@Column(name = "downscaling_methodology", length = 1000)
	private String downscalingMethodology;
	
	public DatasetVariable() {}

	public DatasetVariable(String name, String measurementUnit, Date tmpExtentFrom, Date tmpExtentTo, VocabularyValue dataOrigin,
			VocabularyValue frequency, VocabularyValue frequencyMathRep, VocabularyValue mathRepresentation, Integer aggregationLevel, 
			VocabularyValue aggregationUnit, Integer aggregationScale, String statsRepresentativeness, String downscalingMethodology) {
		
		this.name = name;
		this.measurementUnit = measurementUnit;
		this.tmpExtentFrom = tmpExtentFrom;
		this.tmpExtentTo = tmpExtentTo;
		this.dataOrigin = dataOrigin;
		this.referenceValues = new ArrayList<VariableReferenceValue>();
		this.frequency = frequency;
		this.frequencyMathRep = frequencyMathRep;
		this.mathRepresentation = mathRepresentation;
		this.analysisUnits = new ArrayList<AnalysisUnit>();
		this.aggregationLevel = aggregationLevel;
		this.aggregationUnit = aggregationUnit;
		this.aggregationScale = aggregationScale;
		this.statsRepresentativeness = statsRepresentativeness;
		this.downscalingMethodology = downscalingMethodology;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public VocabularyValue getDataOrigin() {
		return dataOrigin;
	}

	public void setDataOrigin(VocabularyValue dataOrigin) {
		this.dataOrigin = dataOrigin;
	}

	public List<VariableReferenceValue> getReferenceValues() {
		return referenceValues;
	}

	public void setReferenceValues(List<VariableReferenceValue> referenceValues) {
		this.referenceValues = referenceValues;
	}
	
	public void addReferenceValue(VariableReferenceValue referenceValue) {
		this.referenceValues.add(referenceValue);
	}

	public VocabularyValue getFrequency() {
		return frequency;
	}

	public void setFrequency(VocabularyValue frequency) {
		this.frequency = frequency;
	}

	public VocabularyValue getFrequencyMathRep() {
		return frequencyMathRep;
	}

	public void setFrequencyMathRep(VocabularyValue frequencyMathRep) {
		this.frequencyMathRep = frequencyMathRep;
	}

	public VocabularyValue getMathRepresentation() {
		return mathRepresentation;
	}

	public void setMathRepresentation(VocabularyValue mathRepresentation) {
		this.mathRepresentation = mathRepresentation;
	}

	public List<AnalysisUnit> getAnalysisUnits() {
		return analysisUnits;
	}

	public void setAnalysisUnits(List<AnalysisUnit> analysisUnits) {
		this.analysisUnits = analysisUnits;
	}
	
	public void addAnalysisUnit(AnalysisUnit analysisUnit) {
		this.analysisUnits.add(analysisUnit);
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
}
