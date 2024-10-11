--
-- Add SQL scripts for migrations
--
--
-- Add owner colunm to dataset to identify who is the dataset creator
ALTER TABLE dataset ADD COLUMN owner varchar(10000);