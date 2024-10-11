package eu.agricore.indexer.model.analysisunit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@Entity
@DiscriminatorValue(value="GEOREFERENCED")
public class GeoreferencedAnalysisUnit extends AnalysisUnit {
	
	@Column(name = "area_size_value")
	private Integer areaSizeValue;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="area_size_unit", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue areaSizeUnit;

	public GeoreferencedAnalysisUnit() {
		
	}

	public GeoreferencedAnalysisUnit(String unitReference, Integer unitAnalysisNumber, Date tmpExtentFrom, Boolean census, Integer populationCoverage,
			Date tmpExtentTo, Integer aggregationLevel, VocabularyValue aggregationUnit, Integer aggregationScale, 
			String statsRepresentativeness, String downscalingMethodology, Integer areaSizeValue, VocabularyValue areaSizeUnit) {
		
		super(unitReference, unitAnalysisNumber, tmpExtentFrom, tmpExtentTo, aggregationLevel, aggregationUnit, aggregationScale, census, populationCoverage, statsRepresentativeness, downscalingMethodology);
		this.areaSizeValue = areaSizeValue;
		this.areaSizeUnit = areaSizeUnit;
	}

	public Integer getAreaSizeValue() {
		return areaSizeValue;
	}

	public void setAreaSizeValue(Integer areaSizeValue) {
		this.areaSizeValue = areaSizeValue;
	}

	public VocabularyValue getAreaSizeUnit() {
		return areaSizeUnit;
	}

	public void setAreaSizeUnit(VocabularyValue areaSizeUnit) {
		this.areaSizeUnit = areaSizeUnit;
	}
}
