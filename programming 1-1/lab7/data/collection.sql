DROP TABLE IF EXISTS persons CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash CHAR(64) NOT NULL
);

CREATE TABLE persons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    coordinates_x REAL NOT NULL CHECK (coordinates_x > -690),
    coordinates_y DOUBLE PRECISION NOT NULL CHECK (coordinates_y <= 28),
    creation_date TIMESTAMP NOT NULL,
    height INTEGER NOT NULL CHECK (height > 0),
    passport_id VARCHAR(255) CHECK (passport_id IS NULL OR LENGTH(passport_id) >= 9),
    eye_color VARCHAR(50),
    nationality VARCHAR(50) NOT NULL,
    location_x REAL NOT NULL,
    location_y BIGINT NOT NULL,
    location_name VARCHAR(811) NOT NULL,
    owner_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE
);
