--
-- Add SQL scripts for migrations
--
--
alter table dataset add column description varchar(1000);
alter table dataset add column spatial_resolution_in_meters float4;
alter table dataset add column temporal_resolution varchar(255);
alter table dataset add column resource_type int8;
alter table dataset add constraint FK5i5w35yhk5h9rkb4k54bdupu9 foreign key (resource_type) references vocabulary_value;

create table dataset_generation_activity (id  bigserial not null, label varchar(255), dataset_id int8, primary key (id));
alter table dataset_generation_activity add constraint FKkvoqx10e0ltdq3pr7wiwswuvu foreign key (dataset_id) references dataset;

create table dataset_referenced_resource (id  bigserial not null, label varchar(255), dataset_id int8, primary key (id));
alter table dataset_referenced_resource add constraint FKcc9yohqmwb420sojgt5rum13s foreign key (dataset_id) references dataset;

-- New descriptions
INSERT INTO dataset_description VALUES (51, 'A brief description of the dataset, its purpose or the data collected', 'DESCRIPTION');
INSERT INTO dataset_description VALUES (52, 'Minimum spatial separation resolvable in a dataset, measured in meters', 'SPATIAL_RESOLUTION');
INSERT INTO dataset_description VALUES (53, 'Minimum time period resolvable in the dataset', 'TEMPORAL_RESOLUTION');
INSERT INTO dataset_description VALUES (54, 'Activities that generated, or provide the business context for, the creation of the dataset', 'WAS_GENERATED_BY');
INSERT INTO dataset_description VALUES (55, 'Some related resources, such as publications, that reference, cite, or otherwise point to the cataloged resource', 'IS_REFERENCED_BY');
INSERT INTO dataset_description VALUES (56, 'The nature or genre of the resource', 'RESOURCE_TYPE');

-- Resource type vocabulary
INSERT INTO public.vocabulary (id,description,name,topic,url) VALUES (24, 'Vocabulary to describe the genre or nature of a resource', 'DCMI resource type vocabulary', 'RESOURCE_TYPE', 'https://dublincore.org/specifications/dublin-core/dcmi-terms/#section-7');

INSERT INTO public.vocabulary_value (code,extra_data,label,url,vocabulary_id) VALUES 
('COL', NULL, 'Collection', 'http://purl.org/dc/dcmitype/Collection', 24)
,('DAT', NULL, 'Dataset', 'http://purl.org/dc/dcmitype/Dataset', 24)
,('EVE', NULL, 'Event', 'http://purl.org/dc/dcmitype/Event', 24)
,('IMG', NULL, 'Image', 'http://purl.org/dc/dcmitype/Image', 24)
,('INR', NULL, 'Interactive resource', 'http://purl.org/dc/dcmitype/InteractiveResource', 24)
,('MIM', NULL, 'Moving image', 'http://purl.org/dc/dcmitype/MovingImage', 24)
,('SRV', NULL, 'Service', 'http://purl.org/dc/dcmitype/Service', 24)
,('SOF', NULL, 'Software', 'http://purl.org/dc/dcmitype/Software', 24)
,('SOU', NULL, 'Sound', 'http://purl.org/dc/dcmitype/Sound', 24)
,('STI', NULL, 'Still image', 'http://purl.org/dc/dcmitype/StillImage', 24)
,('TEX', NULL, 'Text', 'http://purl.org/dc/dcmitype/Text', 24);

SELECT setval('vocabulary_id_seq', max(id)) FROM vocabulary;