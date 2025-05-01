INSERT INTO accounts (keycloak_id, email, name, last_name, is_active, phone_number, created_at, created_by)
VALUES
    ('1f36bead-4d1e-4ad1-97c1-6c5270cc58a1', 'john.doe@example.com', 'John', 'Doe', true, 77071234567, NOW(), 'system'),

    ('2a81e8b4-6e58-4f97-a779-9c135cfa395b', 'alice.smith@example.com', 'Alice', 'Smith', true, 77076543210, NOW(), 'system'),

    ('3e27a7c9-b9c0-4f7a-937b-84e0e83f1111', 'bob.jones@example.com', 'Bob', 'Jones', false, 77075558888, NOW(), 'system'),

    ('4a19fc01-7d6f-4474-9930-daf97efab321', 'eva.miller@example.com', 'Eva', 'Miller', true, 77079997777, NOW(), 'system'),

    ('5c2c59e3-5a2a-4bb0-bb88-3cfd7c00cc89', 'mike.brown@example.com', 'Mike', 'Brown', false, 77074442211, NOW(), 'system');
