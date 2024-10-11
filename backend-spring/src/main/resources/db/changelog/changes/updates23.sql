
-- Add type to confirmation token table to differentiate between register and reset password token
ALTER TABLE app_user ADD COLUMN dataset_subscription BOOLEAN DEFAULT FALSE;