-- Sample table creation
CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(50),
    age INT
);

-- Sample data
INSERT INTO users (id, name, email, age) VALUES (1, 'Alice', 'alice@example.com', 25);
INSERT INTO users (id, name, email, age) VALUES (2, 'Bob', 'bob@example.com', 30);
INSERT INTO users (id, name, email, age) VALUES (3, 'Charlie', 'charlie@example.com', 28);
