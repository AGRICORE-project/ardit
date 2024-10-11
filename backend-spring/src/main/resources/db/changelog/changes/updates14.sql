--
-- Add SQL scripts for migrations
--
--
-- Comment system
create table comment (id  bigserial not null, content varchar(500), created_at timestamp, delete_at timestamp, deleted boolean, last_update timestamp, level int4 check (level>=0 AND level<=2), parent_id int8, root_id int8, user_id varchar(255), dataset_id int8, primary key (id));
alter table comment add constraint FKox3so6ydjh4tsra6jp7vm3tc9 foreign key (dataset_id) references dataset;