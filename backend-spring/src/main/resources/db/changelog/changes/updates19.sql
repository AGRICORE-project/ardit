
-- Add unique constraint to email in db
ALTER TABLE app_user ADD CONSTRAINT unique_email_co UNIQUE (email);