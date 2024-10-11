package eu.agricore.indexer.model.analysisunit;

import java.util.Date;

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
import javax.persistence.ManyToOne;
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

import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "analysisUnitType")
@JsonSubTypes({
    @JsonSubTypes.Type(value=GeoreferencedAnalysisUnit.class, name = "GEOREFERENCED"),
    @JsonSubTypes.Type(value=SocioeconomicAnalysisUnit.class, name = "SOCIOECONOMIC")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "analysis_unit_type")
@Table(name = "analysis_unit")
public class AnalysisUnit {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotEmpty()
	@Column(name = "unit_reference")
	private String unitReference;
	
	@Column(name = "unit_analysis_number")
	private Integer unitAnalysisNumber;
	
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
	
	@Column(name = "aggregation_level")
    private Integer aggregationLevel;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="aggregation_unit", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue aggregationUnit;
    
    @Column(name = "aggregation_scale")
    private Integer aggregationScale;
    
    @Column(name = "census")
    private Boolean census;
    
    @Column(name = "population_coverage")
    private Integer populationCoverage;
    
    @Size(max = 1000)
    @Column(name = "statistical_representativeness", length = 1000)
    private String statsRepresentativeness;
	
	@Size(max = 1000)
	@Column(name = "downscaling_methodology", length = 1000)
	private String downscalingMethodology;
	
	public AnalysisUnit() {}

	public AnalysisUnit(String unitReference, Integer unitAnalysisNumber, Date tmpExtentFrom,
			Date tmpExtentTo, Integer aggregationLevel, VocabularyValue aggregationUnit, Integer aggregationScale, Boolean census, Integer populationCoverage, 
			String statsRepresentativeness, String downscalingMethodology) {
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
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
