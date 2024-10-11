import { VocabularyValue } from './vocabulary-value';
import { DatasetVariable } from './dataset-variable';
import { AnalysisUnit } from './analysis-unit';
import { Catalogue } from './catalogue';
import { DatasetGenerationActivity } from './dataset-generation-activity';
import { DatasetReferencedResource } from './dataset-refereced-resource';
import { Distribution } from './distribution';
import { Keyword } from './keyword';

export interface Dataset {
    id: number;

    draft: boolean;

    // General category
    title: string;
    description: string;
    datasetType: string;
    wpTask: VocabularyValue;
    creationDateTime: Date;
    lastUpdateDateTime: Date;
    issued: string;
    modified: string;
    producer: string;
    link: string;
    languages: VocabularyValue[];
    periodicity: VocabularyValue;
    catalogue: Catalogue;
    spatialResolutionInMeters: number;
    temporalResolution: string;
	wasGeneratedBy: DatasetGenerationActivity[];
    isReferencedBy: DatasetReferencedResource[];
	resourceType: VocabularyValue;

    // Purpose category
    tmpExtentFrom: Date;
    tmpExtentTo: Date;
    subjects: VocabularyValue[];
    purposes: VocabularyValue[];
    themes: VocabularyValue[];

    // Distribution category
    distributions: Distribution[];

    // Analysis category
    analysisUnits: AnalysisUnit[];

    // Geocoverage category
    continentalCoverage: VocabularyValue[];
    countryCoverage: VocabularyValue[];
    nuts1: VocabularyValue[];
    nuts2: VocabularyValue[];
    nuts3: VocabularyValue[];
    adm1: VocabularyValue[];
    adm2: VocabularyValue[];

    // Variables category
    variables: DatasetVariable[];

    // Keywords category
    keywords: Keyword[];

    // Dataset owner
    owner: string;
}
