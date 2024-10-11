import { DataServiceDTO } from './data-service-dto';

export interface DistributionDTO {

    title: string;
	description: string;
	issued: Date;
    modified: Date;
	license: string
	accessRights: string;
	accessProcedures: string;
	accessUrl: string;
	accessService: DataServiceDTO;
    downloadUrl: string;
	byteSize: number;
    format: string;
    compressFormat: string;
    packagingFormat: string;
}