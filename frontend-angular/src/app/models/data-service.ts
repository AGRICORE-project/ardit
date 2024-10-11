import { VocabularyValue } from './vocabulary-value';
import { DataServiceServedDataset } from './data-service-served-dataset';

export interface DataService {
    id: number;
    title: string;
    description: string;
    issued: string;
    modified: string;
    creator: string;
    publisher: string;
    accessRights: VocabularyValue;
    endpointUrl: string;
    endpointDescription: string;
    servedDatasets: DataServiceServedDataset[];
}