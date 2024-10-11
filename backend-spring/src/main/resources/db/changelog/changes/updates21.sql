--
-- Add SQL scripts for migrations
--
--
-- Insert data into app_user table and confirmation token

-- INSERTION OF DEV USERS

INSERT INTO public.app_user (delete_at, deleted, disabled, email, last_login, username, verified) VALUES(NULL, false, false, 'admin1@agricore.eu', now(), 'admin1', true);

INSERT INTO public.app_user (delete_at, deleted, disabled, email, last_login, username, verified) VALUES(NULL, false, false, 'mantainer1@agricore.eu', now(), 'mantainer1', true);

INSERT INTO public.app_user (delete_at, deleted, disabled, email, last_login, username, verified) VALUES(NULL, false, false, 'editor1@agricore.eu', now(), 'editor1', true);

INSERT INTO public.app_user (delete_at, deleted, disabled, email, last_login, username, verified) VALUES(NULL, false, false, 'user1@gmail.com', now(), 'user1', true);

INSERT INTO public.app_user (delete_at, deleted, disabled, email, last_login, username, verified) VALUES(NULL, false, false, 'user2@gmail.com', now(), 'user2', true);