export interface DatasetSimplified {
    
	id: number;
	draft: boolean;
	creationDateTime: Date;
	lastUpdateDateTime: Date; 
    title: string;
    datasetType: string;
    producer: string;
    periodicity: string;
    tmpExtentFrom: Date;
    tmpExtentTo: Date;
    formats: string[];
}