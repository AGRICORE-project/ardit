--
-- Add SQL scripts for migrations
--
--
CREATE TABLE catalogue (id  bigserial not null, title varchar(255), primary key (id));
ALTER TABLE dataset ADD COLUMN catalogue int8;
ALTER TABLE dataset ADD CONSTRAINT FKek01mqmqtsg49m6wo4hb9tdxg FOREIGN KEY (catalogue) REFERENCES catalogue;

INSERT INTO dataset_description VALUES (45, 'Catalogue to which the dataset belongs', 'CATALOGUE');