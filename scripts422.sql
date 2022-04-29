CREATE TABLE users(
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(30) NOT NULL,
                      age SMALLINT CHECK (age > 0),
                      drivers_license boolean DEFAULT false,
                      car_id INTEGER REFERENCES cars(id) DEFAULT Null
);

CREATE TABLE cars(
                     id SERIAL PRIMARY KEY,
                     car_name VARCHAR(30) NOT NULL,
                     car_model VARCHAR(30) NOT NULL,
                     price MONEY
);