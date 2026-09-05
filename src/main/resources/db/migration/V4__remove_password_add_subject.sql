
ALTER TABLE users.users
    ADD COLUMN auth0_subject VARCHAR(255);

ALTER TABLE users.users
    DROP COLUMN password_hash