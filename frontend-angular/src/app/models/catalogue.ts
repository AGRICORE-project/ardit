import { VocabularyValue } from './vocabulary-value';
export interface Catalogue {
    id: number;
    issued: Date;
    modified: Date;
    title: string;
    description: string;
    creator: string;
    publisher: string;
    link: string;
    tmpExtentFrom: Date;
    tmpExtentTo: Date;
    spatialResolutionInMeters: number;
    temporalResolution: string;
    periodicity: VocabularyValue;
    languages: VocabularyValue[];
    themes: VocabularyValue[];
}
