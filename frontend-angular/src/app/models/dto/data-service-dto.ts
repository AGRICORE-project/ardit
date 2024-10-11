export interface DataServiceDTO {

    title: string;
	description: string;
	issued: Date;
    modified: Date;
    creator: string;
    publisher: string;
    accessRights: string;
    endpointUrl: string;
    endpointDescription: string;
	servedDatasets: string[];
}