--
-- Add SQL scripts for migrations
--
--
ALTER TABLE dataset ADD COLUMN access_procedures varchar(1000);

UPDATE dataset SET "access_procedures" = 'None';

INSERT INTO dataset_description VALUES (39, 'Guidelines for accessing private datasets', 'ACCESS_PROCEDURES');
