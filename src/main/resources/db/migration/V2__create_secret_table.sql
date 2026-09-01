CREATE SCHEMA secrets;

CREATE TABLE secrets.secret(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    public_token VARCHAR(255) NOT NULL UNIQUE,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE,
    consumed_at TIMESTAMP WITH TIME ZONE,
    revoked_at TIMESTAMP WITH TIME ZONE
);