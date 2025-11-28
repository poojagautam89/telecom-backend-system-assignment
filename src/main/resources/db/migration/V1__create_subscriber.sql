CREATE TABLE IF NOT EXISTS subscribers (
    subscriber_id UUID PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(50),
    address TEXT,
    activation_date DATE,
    status VARCHAR(50)
);
