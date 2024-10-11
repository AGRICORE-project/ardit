--
-- Add SQL scripts for migrations
--
--
-- Help system
create table help (id  bigserial not null, content varchar(500), created_at timestamp,  last_update timestamp, owner varchar(255), primary key (id));
