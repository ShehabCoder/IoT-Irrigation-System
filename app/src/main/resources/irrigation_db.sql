-- Create the database
CREATE DATABASE irrigation_db;

-- Connect to the newly created database
\c irrigation_db

-- Create the `plots` table
CREATE TABLE plots (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    area DOUBLE PRECISION NOT NULL,
    crop_type VARCHAR(255) NOT NULL
);

-- Create the `time_slots` table
CREATE TABLE time_slots (
    id SERIAL PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    water_amount DOUBLE PRECISION NOT NULL,
    status BOOLEAN DEFAULT FALSE,
    plot_id INT REFERENCES plots(id) ON DELETE CASCADE
);

-- Insert data into tables
BEGIN;

-- Insert data into `plots`
INSERT INTO plots (name, area, crop_type) VALUES ('Plot A', 10.5, 'Wheat');
INSERT INTO plots (name, area, crop_type) VALUES ('Plot B', 15.0, 'Corn');

-- Insert data into `time_slots`
INSERT INTO time_slots (start_time, end_time, water_amount, status, plot_id)
VALUES ('06:00:00', '06:30:00', 5.0, FALSE, 1);

INSERT INTO time_slots (start_time, end_time, water_amount, status, plot_id)
VALUES ('07:00:00', '07:30:00', 7.0, FALSE, 2);

COMMIT;
