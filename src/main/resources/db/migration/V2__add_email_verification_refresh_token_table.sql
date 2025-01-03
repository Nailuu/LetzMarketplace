CREATE TABLE jwt_refresh_token_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES users(id) UNIQUE NOT NULL,
    token VARCHAR(500) NOT NULL
);

ALTER TABLE users ADD email_verified BOOLEAN DEFAULT false NOT NULL;
ALTER TABLE users ADD email_verify_token VARCHAR(250);