package eu.agricore.indexer.model.analysisunit;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@Entity
@DiscriminatorValue(value="SOCIOECONOMIC")
public class SocioeconomicAnalysisUnit extends AnalysisUnit {
	
	public SocioeconomicAnalysisUnit() {}

	public SocioeconomicAnalysisUnit(String unitReference, Integer unitAnalysisNumber, Date tmpExtentFrom,
			Date tmpExtentTo, Integer aggregationLevel, VocabularyValue aggregationUnit, Integer aggregationScale, Boolean census, Integer populationCoverage,
			String statsRepresentativeness, String downscalingMethodology) {
		
		super(unitReference, unitAnalysisNumber, tmpExtentFrom, tmpExtentTo, aggregationLevel, aggregationUnit, aggregationScale, census, populationCoverage, statsRepresentativeness, downscalingMethodology);
	}
}
