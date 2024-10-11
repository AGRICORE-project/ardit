--
-- Add SQL scripts for migrations
--
--
-- Aggregation level properties added to the units of analysis
alter table analysis_unit add column aggregation_level int4;
alter table analysis_unit add column aggregation_scale int4;
alter table analysis_unit add column aggregation_unit int8;
alter table analysis_unit add constraint FK98m2svqwfmhg1jtacnxobwlo3 foreign key (aggregation_unit) references vocabulary_value;

-- Unit of analysis new properties
alter table analysis_unit add column census boolean default false;
alter table analysis_unit add column population_coverage int4;

-- Statistically representativeness for unit of analysis and variables
alter table analysis_unit add column statistical_representativeness varchar(1000);
alter table dataset_variable add column statistical_representativeness varchar(1000);

-- Support column for initisalisation (to be undone when inisialisation proccess is completed)
alter table analysis_unit add column to_delete boolean default false;
update analysis_unit set to_delete=true where dataset_id is null;
update analysis_unit au set dataset_id = (select v.dataset_id from dataset_variable v where v.id=(select auv.dataset_variable_id from analysisunits_variables auv where auv.analysis_unit_id=au.id limit 1)) where dataset_id is null;

-- Census initialisation
update analysis_unit set census=true where stats_representative=0 or stats_representative=1;
update analysis_unit set census=true where (select d.stats_representative from dataset d where d.id=dataset_id)=0 or (select d.stats_representative from dataset d where d.id=dataset_id)=1 and census is false;

-- Population coverage initialisation
update analysis_unit set population_coverage=stats_representative where stats_representative > 1;
update analysis_unit set population_coverage=(select d.stats_representative from dataset d where d.stats_representative > 1 and d.id=dataset_id) where population_coverage is null;

-- Aggregation level inisialisation
update analysis_unit set aggregation_level=(select d.aggregation_level from dataset d where d.id=dataset_id);
update analysis_unit set aggregation_unit=(select d.aggregation_unit from dataset d where d.id=dataset_id);
update analysis_unit set aggregation_scale=(select d.aggregation_scale from dataset d where d.id=dataset_id);

-- Undo proccess
alter table dataset alter column stats_representative type varchar(1000);
alter table dataset rename column stats_representative to statistical_representativeness;
alter table analysis_unit drop column stats_representative;

update analysis_unit set dataset_id=null where to_delete is true;
alter table analysis_unit drop column to_delete;

-- Set hidden properties to null
update dataset set aggregation_level=null;
update dataset set aggregation_unit=null;
update dataset set aggregation_scale=null;
update dataset set statistical_representativeness=null;

-- New properties descriptions
INSERT INTO dataset_description VALUES (84, 'Indicates whether the dataset is a census', 'ANALYSISUNIT_CENSUS');
INSERT INTO dataset_description VALUES (85, 'When the dataset is not a census, this property indicates the proportion of Units of Analysis sampled with the size of the total population of Units of Analysis. Must be expressed as a percentage from 0 to 100', 'ANALYSISUNIT_POPULATION_COVERAGE');
INSERT INTO dataset_description VALUES (86, 'A free text property to indicate, when known, information on how the sample was built or which variables are included', 'VARIABLE_STATS_REPRESENTATIVE');
INSERT INTO dataset_description VALUES (87, 'Select a owner for the dataset', 'DATASET_OWNER');
update dataset_description set description='A free text property to indicate, when known, information on how the sample was built or which units of analysis are included' WHERE id=30;

-- Descriptions of discarded properties (dataset formats, access rights, data frequency and statistical representativeness)
DELETE FROM dataset_description WHERE id=10;
DELETE FROM dataset_description WHERE id=11;
DELETE FROM dataset_description WHERE id=12;
DELETE FROM dataset_description WHERE id=15;

-- Max size constraints (distribution description, dataset link and variable name)
alter table distribution alter column description type varchar(1000);
alter table dataset alter column link type varchar(1000);
alter table dataset_variable alter column name type varchar(500);
