
-- Add type to confirmation token table to differentiate between register and reset password token
ALTER TABLE confirmation_token ADD COLUMN type varchar(10000);