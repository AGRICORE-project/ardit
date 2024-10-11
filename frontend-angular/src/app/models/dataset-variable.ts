import { VocabularyValue } from './vocabulary-value';
import { AnalysisUnit } from './analysis-unit';
import { VariableReferenceValue } from './variable-reference-value';

export interface DatasetVariable {
    id: number;
    variableType: string
    name: string;
    measurementUnit: string;
    tmpExtentFrom: Date;
    tmpExtentTo: Date;
    mathRepresentation: VocabularyValue;
    frequency: VocabularyValue;
    frequencyMathRep: VocabularyValue;
    analysisUnits: AnalysisUnit[];
    aggregationLevel: number;
    aggregationUnit: VocabularyValue;
    aggregationScale: number;
    statsRepresentativeness: string;
    downscalingMethodology: string;
    dataOrigin: VocabularyValue;
    referenceValues: VariableReferenceValue[];
    currency: VocabularyValue;
    priceType: VocabularyValue;
    sizeUnitAmount: number;
    sizeUnit: VocabularyValue;
}