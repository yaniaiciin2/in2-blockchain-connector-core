CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS transactions (
    id uuid PRIMARY KEY UNIQUE DEFAULT uuid_generate_v4(),
    created_at TIMESTAMP NOT NULL,
    data_location VARCHAR(256),
    entity_id VARCHAR(256),
    entity_hash VARCHAR(256),
    status VARCHAR(256),
    trader VARCHAR(256),
    hash VARCHAR(256)
);
