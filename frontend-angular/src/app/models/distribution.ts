import { VocabularyValue } from './vocabulary-value';
import { DataService } from './data-service';

export interface Distribution {
    id: number;
    title: string;
    issued: string;
    modified: string;
    description: string;
    license: string;
    accessRights: VocabularyValue;
    accessProcedures: string;
    accessUrl: string;
    downloadUrl: string;
    byteSize: number;
    format: VocabularyValue;
    compressFormat: VocabularyValue;
    packagingFormat: VocabularyValue;
    accessService: DataService;
}