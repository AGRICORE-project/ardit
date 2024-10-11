--
-- Add SQL scripts for migrations
--
--
ALTER TABLE dataset DROP CONSTRAINT fkevki24pwxly3ojnoia34prb17;
ALTER TABLE dataset DROP COLUMN math_representation;
DELETE FROM dataset_description WHERE id=13;

---

ALTER TABLE dataset_variable ADD COLUMN data_origin int8;
ALTER TABLE dataset_variable ADD CONSTRAINT FK790guyxolfo2y59rgc9g87cm FOREIGN KEY (data_origin) REFERENCES vocabulary_value;

---

ALTER TABLE datasets_values DROP CONSTRAINT fk5c303r39osbwg498yt4dvreeg;
ALTER TABLE datasets_values DROP CONSTRAINT fktg63uyyd7pt5j5ncle1w75kba;
DROP TABLE datasets_values;
DELETE FROM dataset_description WHERE id=14;

---

CREATE TABLE variable_reference_value (id  bigserial not null, label varchar(255), dataset_variable_id int8, primary key (id));
ALTER TABLE variable_reference_value ADD CONSTRAINT FKr43l6aurd1os1m5xe1ccc1hbo FOREIGN KEY (dataset_variable_id) REFERENCES dataset_variable;

---

INSERT INTO dataset_description VALUES (43, 'Variable data origin', 'VARIABLE_DATA_ORIGIN');
INSERT INTO dataset_description VALUES (44, 'Variable reference values', 'VARIABLE_REFERENCE_VALUES');