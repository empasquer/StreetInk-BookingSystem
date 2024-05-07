
CREATE DATABASE IF NOT EXISTS street_ink_booking_system;
USE street_ink_booking_system;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin123';
GRANT USAGE ON street_ink_booking_system.* TO 'admin'@'localhost';

DROP TABLE IF EXISTS tattoo_artist;
CREATE TABLE tattoo_artist(
	username VARCHAR(255) PRIMARY KEY NOT NULL,
	password VARCHAR(255) NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	phone_number VARCHAR(255) NOT NULL,
	profile_picture LONGBLOB,
	avg_work_hours INT NOT NULL,
    isAdmin BOOLEAN DEFAULT 0
);

DROP TABLE IF EXISTS client;
CREATE TABLE client (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255),
	email VARCHAR(255) NOT NULL,
	phone_number VARCHAR(255),
	description TEXT
);

DROP TABLE IF EXISTS booking;
CREATE TABLE booking (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	start_time_slot INT NOT NULL,
	end_time_slot INT NOT NULL,
	date DATE NOT NULL,
	client_id INT NOT NULL,
    username VARCHAR(255) NOT NULL,
	project_title TEXT NOT NULL,
	project_desc TEXT,
	personal_note TEXT,
	is_deposit_payed BOOLEAN DEFAULT 0,
	FOREIGN KEY (client_id) REFERENCES client(id),
	FOREIGN KEY (username) REFERENCES tattoo_artist(username) ON DELETE CASCADE 
);



DROP TABLE IF EXISTS project_picture;
CREATE TABLE project_picture (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	booking_id INT NOT NULL,
	picture_data LONGBLOB NOT NULL,
	FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE
);


INSERT INTO client (first_name, last_name, email, phone_number, description) 
VALUES 
("Hella", "Nice", "hellanice@dummy.com", "+45 11 11 11 11", "They're hella nice"),
("Seeu", "Later", "aligator@dummy.com", "+45 22 22 22 22", "Always f*cking late");

INSERT INTO tattoo_artist (username, password, first_name, last_name, email, phone_number, avg_work_hours, isAdmin) 
VALUES 
("smallDummy", 123, "Small", "Dummy", "smalldummy@dummy.com", "+45 12 34 56 78", 8,0),
("bigDummy", 321, "Big", "Dummy", "bigdummy@dummy.com", "+45 87 65 43 21", 4,1);


INSERT INTO booking (start_time_slot, end_time_slot, date, client_id, username, project_title, project_desc, personal_note, is_deposit_payed) 
VALUES (10, 13, '2024-05-16', 1, "smallDummy", "butterfly on forhead", "coloured butterfly, in memory of mother", "", 1),
(14, 15, '2024-05-16', 2, "smallDummy", "star on ass", "specific star at coordinate: RA (Right Ascension): 12h 34m 56s Dec (Declination): +45° 67' 89", "He's gonna be late", 0),
(10, 15, '2024-05-17', 1, "bigDummy", "earworm", "specific worm behind ear", "idk why anyone would want that", 0),
(14, 18, '2024-05-20', 2, "bigDummy", "illuminati on bellybutton", " ", "late", 0);

SELECT * FROM booking;