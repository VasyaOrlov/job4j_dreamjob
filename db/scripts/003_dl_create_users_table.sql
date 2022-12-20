CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email varchar UNIQUE,
  password TEXT,
  name TEXT
);