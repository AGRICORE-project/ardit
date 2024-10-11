--
-- Add SQL scripts for migrations
--
--
alter table dataset_variable add column aggregation_level int NULL;

alter table dataset_variable add column aggregation_scale int NULL;

alter table dataset_variable add column aggregation_unit bigint NULL;

alter table dataset_variable add constraint FKf8l52x2a94jqtuiu0rokcwy4d foreign key (aggregation_unit) references vocabulary_value;

INSERT INTO dataset_description VALUES (40, 'Variable aggregation level', 'VARIABLE_AGGREGATION_LEVEL');
INSERT INTO dataset_description VALUES (41, 'Variable aggregation level unit of measurement', 'VARIABLE_AGGREGATION_UNIT');
INSERT INTO dataset_description VALUES (42, 'Variable aggregation scale', 'VARIABLE_AGGREGATION_SCALE');