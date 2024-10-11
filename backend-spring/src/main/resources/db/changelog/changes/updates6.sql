--
-- Add SQL scripts for migrations
--
--
alter table catalogue add column creator varchar(255);
alter table catalogue add column description varchar(500);
alter table catalogue add column issued timestamp;
alter table catalogue add column link varchar(255);
alter table catalogue add column modified timestamp;
alter table catalogue add column publisher varchar(255);
alter table catalogue add column spatial_resolution_in_meters float4;
alter table catalogue add column temporal_resolution varchar(255);
alter table catalogue add column tmp_extent_from date;
alter table catalogue add column tmp_extent_to date;
alter table catalogue add column periodicity int8;

create table catalogues_languages (catalogue_id int8 not null, vocabulary_value_id int8 not null);
create table catalogues_themes (catalogue_id int8 not null, vocabulary_value_id int8 not null);

alter table catalogue add constraint FKauclmk32nr3kvyu3bs24soaum foreign key (periodicity) references vocabulary_value;

alter table catalogues_languages add constraint FK7veee0kd9siiq5lw7cgvwplvi foreign key (vocabulary_value_id) references vocabulary_value;
alter table catalogues_languages add constraint FKobaeqv5h1fjreyy4o9u7b04bd foreign key (catalogue_id) references catalogue;
alter table catalogues_themes add constraint FK1jvqlx24l8ifoj0mdfln6e9ik foreign key (vocabulary_value_id) references vocabulary_value;
alter table catalogues_themes add constraint FK9foki04e62r8uoe2758e35pfg foreign key (catalogue_id) references catalogue;