export interface AnalysisUnitDTO {

    unitReference: string;
	unitAnalysisNumber: number;
	tmpExtentFrom: Date;
	tmpExtentTo: Date;
    aggregationLevel: number;
    aggregationUnit: string;
    aggregationScale: number;
    census: boolean;
    populationCoverage: number;
    statsRepresentativeness: string;
    downscalingMethodology: string;
    areaSizeValue: number;
    areaSizeUnit: string;
}