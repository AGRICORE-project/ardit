--
-- Add SQL scripts for migrations
--
--
-- Environmental type changed to geo-referenced
update dataset set dataset_type='GEOREFERENCED' where dataset_type='ENVIRONMENTAL';
update dataset_variable set variable_type ='GEOREFERENCED' where variable_type='ENVIRONMENTAL';
update analysis_unit set analysis_unit_type ='GEOREFERENCED' where analysis_unit_type ='ENVIRONMENTAL';