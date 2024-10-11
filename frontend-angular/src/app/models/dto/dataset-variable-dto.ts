import { AnalysisUnitDTO } from './analysis-unit-dto';

export interface DatasetVariableDTO {

    name: string;
    variableType: string;
    measurementUnit: string;
	tmpExtentFrom: Date;
	tmpExtentTo:Date;
	dataOrigin: string;
	referenceValues: string[];
    frequency: string;
    frequencyMathRep: string;
    mathRepresentation: string;
    analysisUnits: AnalysisUnitDTO[];
    aggregationLevel: number;
    aggregationUnit: string;
    aggregationScale: number;
    statsRepresentativeness: string;
    downscalingMethodology: string;
    currency: string;
    priceType: string;
    sizeUnitAmount: number;
    sizeUnit: string;
}