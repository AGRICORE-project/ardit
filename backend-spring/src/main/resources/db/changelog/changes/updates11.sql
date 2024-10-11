--
-- Add SQL scripts for migrations
--
--
-- Data service property
create table data_service (id  bigserial not null, creator varchar(255), description varchar(1000), endpoint_description varchar(255), endpoint_url varchar(255), issued date, modified date, publisher varchar(255), title varchar(255), access_rights int8, primary key (id));
create table data_service_served_dataset (id  bigserial not null, label varchar(255), data_service_id int8, primary key (id));
alter table data_service add constraint FK74e9olje1d6nmwrk0l1jo9dlp foreign key (access_rights) references vocabulary_value;
alter table data_service_served_dataset add constraint FKsh92h9tyftb4jyxtea9oppsxp foreign key (data_service_id) references data_service;

-- Distribution property
create table distribution (id  bigserial not null, access_url varchar(255), byte_size float4, description varchar(255), download_url varchar(255), issued date, license varchar(255), modified date, title varchar(255), access_rights int8, data_service_id int8, compress_format int8, format int8, packaging_format int8, dataset_id int8, primary key (id));
alter table distribution add constraint FKo8p65857e3gsrkhu9g7yye0ji foreign key (access_rights) references vocabulary_value;
alter table distribution add constraint FKkxnbafldonujwpfqo6pcc4r2i foreign key (data_service_id) references data_service;
alter table distribution add constraint FKr268stosvxeamsg2vlg9ych3c foreign key (compress_format) references vocabulary_value;
alter table distribution add constraint FKri9e35vqtby587iex9sjhbnh7 foreign key (format) references vocabulary_value;
alter table distribution add constraint FK76juvjcqythrh0mioal8j3sf5 foreign key (packaging_format) references vocabulary_value;
alter table distribution add constraint FKpnq2adfxfb1q8pvlp61hr6wc6 foreign key (dataset_id) references dataset;
alter table distribution add column access_procedures varchar(1000);

-- Distribution properties descriptions
INSERT INTO dataset_description VALUES (57, 'A specific representation of a dataset. A dataset might be available in multiple serializations that may differ in various ways, including natural language, media-type or format, schematic organization, temporal and spatial resolution, level of detail or profiles', 'DISTRIBUTION');
INSERT INTO dataset_description VALUES (58, 'A name given to the distribution', 'DISTRIBUTION_TITLE');
INSERT INTO dataset_description VALUES (59, 'Date of formal issuance or publication of the distribution', 'DISTRIBUTION_ISSUED');
INSERT INTO dataset_description VALUES (60, 'Most recent date on which the distribution was changed, updated or modified', 'DISTRIBUTION_MODIFIED');
INSERT INTO dataset_description VALUES (61, 'A free-text account of the distribution', 'DISTRIBUTION_DESCRIPTION');
INSERT INTO dataset_description VALUES (62, 'A legal document under which the distribution is made available', 'DISTRIBUTION_LICENSE');
INSERT INTO dataset_description VALUES (63, 'A rights statement that concerns how the distribution is accessed', 'DISTRIBUTION_ACCESS_RIGHTS');
INSERT INTO dataset_description VALUES (64, 'A URL of the resource that gives access to a distribution of the dataset', 'DISTRIBUTION_ACCESS_URL');
INSERT INTO dataset_description VALUES (65, 'The URL of the downloadable file in a given format', 'DISTRIBUTION_DOWNLOAD_URL');
INSERT INTO dataset_description VALUES (66, 'The size of a distribution in bytes', 'DISTRIBUTION_BYTE_SIZE');
INSERT INTO dataset_description VALUES (67, 'The file format of the distribution', 'DISTRIBUTION_FORMAT');
INSERT INTO dataset_description VALUES (68, 'The compression format of the distribution in which the data is contained, if it is a compressed form', 'DISTRIBUTION_COMPRESS_FORMAT');
INSERT INTO dataset_description VALUES (69, 'The package format of the distribution in which one or more data files are grouped together, if applicable', 'DISTRIBUTION_PACKAGING_FORMAT');
INSERT INTO dataset_description VALUES (70, 'A data service that gives access to the distribution of the dataset', 'DISTRIBUTION_DATA_SERVICE');

-- Data service properties descriptions
INSERT INTO dataset_description VALUES (71, 'A name given to the data service', 'DATA_SERVICE_TITLE');
INSERT INTO dataset_description VALUES (72, 'A free-text account of the data service', 'DATA_SERVICE_DESCRIPTION');
INSERT INTO dataset_description VALUES (73, 'Date of formal issuance or publication of the data service', 'DATA_SERVICE_ISSUED');
INSERT INTO dataset_description VALUES (74, 'Most recent date on which the data service was changed, updated or modified', 'DATA_SERVICE_MODIFIED');
INSERT INTO dataset_description VALUES (75, 'The entity responsible for producing the data service', 'DATA_SERVICE_CREATOR');
INSERT INTO dataset_description VALUES (76, 'The entity responsible for making the data service available', 'DATA_SERVICE_PUBLISHER');
INSERT INTO dataset_description VALUES (77, 'A rights statement that concerns how the data service is accessed', 'DATA_SERVICE_ACCESS_RIGHTS');
INSERT INTO dataset_description VALUES (78, 'A link for the root location or primary endpoint of the service. It is a Web-resolvable IRI', 'DATA_SERVICE_ENDPOINT_URL');
INSERT INTO dataset_description VALUES (79, 'A link to the description of the services available via the end-points, including their operations or parameters', 'DATA_SERVICE_ENDPOINT_DESCRIPTION');
INSERT INTO dataset_description VALUES (80, 'A collection of datasets that this data service can distribute. Could be filled in with links to characterised datasets in ARDIT ', 'DATA_SERVICE_SERVED_DATASETS');

-- Dataset issued and modified
alter table dataset add column issued date;
alter table dataset add column modified date;
INSERT INTO dataset_description VALUES (81, 'Date of formal issuance or publication of the dataset', 'ISSUED');
INSERT INTO dataset_description VALUES (82, 'Most recent date on which the dataset was changed, updated or modified', 'MODIFIED');

-- Distribution migrations
insert into distribution (title, format, dataset_id) select vv.label, df.vocabulary_value_id, df.dataset_id from datasets_formats df, vocabulary_value vv where df.vocabulary_value_id=vv.id;
update distribution set access_rights=dat.access_right, access_procedures=dat.access_procedures from dataset dat where dataset_id=dat.id;

-- Keywords property
create table datasets_keywords (dataset_id int8 not null, keyword_id int8 not null);
create table keyword (id  bigserial not null, label varchar(255), primary key (id));
alter table datasets_keywords add constraint FKeinj8fi4wolhw7bp1aphk22b4 foreign key (keyword_id) references keyword;
alter table datasets_keywords add constraint FK3wrt3bo32kmyvsn2i71nrref8 foreign key (dataset_id) references dataset;

INSERT INTO dataset_description VALUES (83, 'List of keywords or tags describing or representing the dataset. It can be filled with existing keywords, inserted in other datasets, or with new ones', 'KEYWORDS');