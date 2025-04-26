CREATE TABLE accounts (
                          user_id BIGINT PRIMARY KEY,
                          email VARCHAR(255) UNIQUE,
                          name VARCHAR(255),
                          last_name VARCHAR(255),
                          is_active BOOLEAN NOT NULL,
                          phone_number BIGINT,
                          created_at TIMESTAMP,
                          created_by VARCHAR(255),
                          updated_at TIMESTAMP,
                          updated_by VARCHAR(255)
);
