--
-- Add SQL scripts for migrations
--
--
ALTER TABLE dataset ADD COLUMN dataset_type varchar(255);
INSERT INTO dataset_description VALUES (46, 'Type of dataset. ENVIRONMENTAL to indicate a geo-referenced dataset and SOCIOECONOMIC for statistic or economic datasets', 'DATASET_TYPE');

ALTER TABLE dataset ADD COLUMN wp_task int8;
ALTER TABLE dataset ADD CONSTRAINT FK99o7drpokrnnk4unm4h6cbi04 FOREIGN KEY (wp_task) REFERENCES vocabulary_value;