package eu.agricore.indexer.model.datasetvariable;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@Entity
@DiscriminatorValue(value="GEOREFERENCED")
public class GeoreferencedDatasetVariable extends DatasetVariable {
	
	public GeoreferencedDatasetVariable() {}

	public GeoreferencedDatasetVariable(String name, String measurementUnit, Date tmpExtentFrom, Date tmpExtentTo,
			VocabularyValue dataOrigin, VocabularyValue frequency, VocabularyValue frequencyMathRep,
			VocabularyValue mathRepresentation, Integer aggregationLevel, VocabularyValue aggregationUnit,
			Integer aggregationScale, String statsRepresentativeness, String downscalingMethodology) {
		
		super(name, measurementUnit, tmpExtentFrom, tmpExtentTo, dataOrigin, frequency, frequencyMathRep, mathRepresentation,
				aggregationLevel, aggregationUnit, aggregationScale, statsRepresentativeness, downscalingMethodology);
	}
}
