--
-- Add SQL scripts for migrations
--
--
CREATE TABLE datasets_languages (dataset_id int8 not null, vocabulary_value_id int8 not null);
ALTER TABLE datasets_languages ADD CONSTRAINT FKjw7y8bhsgnl8nr9lyg457qw1f FOREIGN KEY (vocabulary_value_id) REFERENCES vocabulary_value;
alter table datasets_languages ADD CONSTRAINT FK8bx0ghpve4wrwwaw1f6jrgu98 FOREIGN KEY (dataset_id) REFERENCES dataset;

INSERT INTO datasets_languages (dataset_id, vocabulary_value_id) SELECT id, language FROM dataset;

ALTER TABLE dataset DROP CONSTRAINT fkh6m8n675np16cre08s84sjgjl;
ALTER TABLE dataset DROP COLUMN language;