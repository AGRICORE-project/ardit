
-- Add type to confirmation token table to differentiate between register and reset password token
ALTER TABLE dataset ADD COLUMN views int4 DEFAULT 0;