create database gdse69;

use gdse69;

create table customer(
    id varchar(5) primary key,
    name varchar(255),
    address varchar(255),
    contact varchar(255),
    nic varchar(255),
    email varchar(255),
    salary varchar(255),
    age varchar(255)
);

INSERT INTO customer VALUES('C003', 'John', 'Colombo', '0771234567', '123456789V', 'N0XKg@example.com', '10000', '30'),
('C004', 'Jane', 'Colombo', '0771234567', '123456789V', 'N0XKg@example.com', '10000', '30'),
('C005', 'Jim', 'Colombo', '0771234567', '123456789V', 'N0XKg@example.com', '10000', '30'),
('C006', 'Jill', 'Colombo', '0771234567', '123456789V', 'N0XKg@example.com', '10000', '30');