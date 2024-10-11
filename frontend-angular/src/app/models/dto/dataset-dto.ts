import { AnalysisUnitDTO } from './analysis-unit-dto';
import { DatasetVariableDTO } from './dataset-variable-dto';
import { DistributionDTO } from './distribution-dto';

export interface DatasetDTO {
    
    id: number;

    draft: boolean;

    // General category
    title: string;
    description: string;
    datasetType: string;
    wpTask: string;
    creationDateTime: Date;
    lastUpdateDateTime: Date;
    issued: string;
    modified: string;
    producer: string;
    link: string;
    languages: string[];
    periodicity: string;
    catalogueId: number;
    catalogueTitle: string
    spatialResolutionInMeters: number;
    temporalResolution: string;
	wasGeneratedBy: string[];
    isReferencedBy: string[];
	resourceType: string;

    // Purpose category
    tmpExtentFrom: Date;
    tmpExtentTo: Date;
    subjects: string[];
    purposes: string[];
    themes: string[];

    // Distribution category
    distributions: DistributionDTO[];

    // Analysis category
    analysisUnits: AnalysisUnitDTO[];

    // Geocoverage category
    continentalCoverage: string[];
    countryCoverage: string[];
    nuts1: string[];
    nuts2: string[];
    nuts3: string[];
    adm1: string[];
    adm2: string[];

    // Variables category
    variables: DatasetVariableDTO[];

    // Keywords category
    keywords: String[];

    // Dataset owner
    owner: string;
    // Total views
    view: number;
    
}