CREATE TABLE IF NOT EXISTS bot_users (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL UNIQUE,
    username VARCHAR(255),
    gender VARCHAR(50),
    preferred_gender VARCHAR(50),
    count_uses INTEGER NOT NULL DEFAULT 0,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_activity_date TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_bot_users_chat_id ON bot_users(chat_id);

CREATE INDEX IF NOT EXISTS idx_bot_users_gender ON bot_users(gender);