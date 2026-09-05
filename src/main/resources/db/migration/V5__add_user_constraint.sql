ALTER TABLE users.users
    ALTER COLUMN auth0_subject SET NOT NULL;

ALTER TABLE users.users
    ADD CONSTRAINT uk_users_auth0_subject UNIQUE (auth0_subject);