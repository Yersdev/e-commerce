INSERT INTO accounts (user_id, email, name, last_name, is_active, phone_number, created_at, created_by)
VALUES
    (1, 'alice@example.com', 'Alice', 'Wonderland', true, 77001112233, NOW(), 'admin'),
    (2, 'bob@example.com', 'Bob', 'Builder', true, 77002223344, NOW(), 'admin'),
    (3, 'charlie@example.com', 'Charlie', 'Brown', false, 77003334455, NOW(), 'admin'),
    (4, 'david@example.com', 'David', 'Smith', true, 77004445566, NOW(), 'admin'),
    (5, 'eve@example.com', 'Eve', 'Johnson', true, 77005556677, NOW(), 'admin'),
    (6, 'frank@example.com', 'Frank', 'Miller', false, 77006667788, NOW(), 'admin'),
    (7, 'grace@example.com', 'Grace', 'Hopper', true, 77007778899, NOW(), 'admin'),
    (8, 'heidi@example.com', 'Heidi', 'Klum', true, 77008889900, NOW(), 'admin'),
    (9, 'ivan@example.com', 'Ivan', 'Ivanov', false, 77009990011, NOW(), 'admin'),
    (10, 'judy@example.com', 'Judy', 'Garland', true, 77001001122, NOW(), 'admin');
