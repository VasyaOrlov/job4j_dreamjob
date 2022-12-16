CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   created TIMESTAMP,
   description TEXT,
   city_id INTEGER,
   photo bytea
);