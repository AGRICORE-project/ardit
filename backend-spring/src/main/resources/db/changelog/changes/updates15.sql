--
-- Add SQL scripts for migrations
--
--
-- App user (table to replicate the users registered on AGRICORE without any password or role)
create table app_user (id  bigserial not null, delete_at timestamp, deleted boolean, disabled boolean, email varchar(255), last_login timestamp, username varchar(255), verified boolean, primary key (id));

-- Confirmation token table
create table confirmation_token (id  bigserial not null, confirmed_at timestamp, create_at timestamp, expires_at timestamp, token varchar(255), app_user_id int8 not null, primary key (id));
alter table confirmation_token add constraint FKo9fl25wqyh7w7mnfkdqen1rcm foreign key (app_user_id) references app_user;

