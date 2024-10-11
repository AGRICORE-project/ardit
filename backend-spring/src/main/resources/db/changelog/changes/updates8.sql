--
-- Add SQL scripts for migrations
--
--

-- Analysis units changes
-- Set default type to ENVIRONMENTAL
alter table analysis_unit add column analysis_unit_type varchar(31) not null default 'ENVIRONMENTAL';
-- Update analysis units type according to the type of the dataset they belong to
update analysis_unit au set analysis_unit_type = (select d.dataset_type from dataset d where d.id=au.dataset_id) where au.dataset_id is not null;
-- Update type for analysis units with dataset_id set to null
update analysis_unit au1 set analysis_unit_type = au2.analysis_unit_type from analysis_unit au2 where au1.unit_reference=au2.unit_reference and au1.tmp_extent_from=au2.tmp_extent_from and au1.tmp_extent_to=au2.tmp_extent_to and au2.dataset_id is not null;
-- Drop not null constraint for stats_representative
alter table analysis_unit alter column stats_representative drop not null;
-- Set stats_representative to null for environmental analysis units
update analysis_unit set stats_representative = null where analysis_unit_type = 'ENVIRONMENTAL';
-- Socio-economic properties
alter table analysis_unit add column area_size_value int4;
alter table analysis_unit add column area_size_unit int8;
alter table analysis_unit add constraint FKri03dghlbplbg0ngcxwjv87cy foreign key (area_size_unit) references vocabulary_value;

-- Dataset variables changes
alter table dataset_variable add column variable_type varchar(31) not null default 'SOCIOECONOMIC';
update dataset_variable v set variable_type = (select d.dataset_type from dataset d where d.id=v.dataset_id);
alter table dataset_variable add column size_unit_amount int4;
alter table dataset_variable add column currency int8;
alter table dataset_variable add column price_type int8;
alter table dataset_variable add column area_size_unit int8;
alter table dataset_variable add constraint FKenx7qttars7eevpf46ukmf5ja foreign key (currency) references vocabulary_value;
alter table dataset_variable add constraint FK1v07y90uxd2keoxnni6w3im44 foreign key (price_type) references vocabulary_value;
alter table dataset_variable add constraint FKth1scr5j6tafu4gg221jb81r1 foreign key (area_size_unit) references vocabulary_value;

-- New descriptions
INSERT INTO dataset_description VALUES (47, 'Area size for environmental analysis units', 'ANALYSISUNIT_AREA_SIZE');
INSERT INTO dataset_description VALUES (48, 'Price variable currency property', 'VARIABLE_CURRENCY');
INSERT INTO dataset_description VALUES (49, 'Price variable price type property', 'VARIABLE_PRICE_TYPE');
INSERT INTO dataset_description VALUES (50, 'Price variable size unit property', 'VARIABLE_SIZE_UNIT');