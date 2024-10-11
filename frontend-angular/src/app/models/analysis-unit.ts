import { VocabularyValue } from './vocabulary-value';

export interface AnalysisUnit {
    id: number;
    analysisUnitType: string;
    unitReference: string;
    unitAnalysisNumber: number;
    tmpExtentFrom: Date;
    tmpExtentTo: Date;
    aggregationLevel: number;
    aggregationUnit: VocabularyValue;
    aggregationScale: number;
    census: boolean;
    populationCoverage: number;
    statsRepresentativeness: string;
    areaSizeValue: number;
    areaSizeUnit: VocabularyValue;
    downscalingMethodology: string;
}