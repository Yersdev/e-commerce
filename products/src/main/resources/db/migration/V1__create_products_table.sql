CREATE TABLE products (
                          product_id BIGSERIAL PRIMARY KEY,

                          name VARCHAR(50) NOT NULL UNIQUE,
                          description VARCHAR(255) NOT NULL UNIQUE,
                          price DOUBLE PRECISION NOT NULL,
                          stock_quantity BIGINT,
                          category VARCHAR(50), -- Enum как строка

                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          created_by VARCHAR(255),

                          updated_at TIMESTAMP,
                          updated_by VARCHAR(255)
);
INSERT INTO products (name, description, price, stock_quantity, category, created_at, created_by) VALUES
