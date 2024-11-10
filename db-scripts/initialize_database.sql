-- Create the database
CREATE DATABASE IF NOT EXISTS styleup_db;

-- Use the new database
USE styleup_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fname VARCHAR(255) NOT NULL,
    lname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create rooms table
CREATE TABLE IF NOT EXISTS rooms (
    room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    roomName VARCHAR(255) NOT NULL,
    fku BIGINT
);
-- Create decorations table
CREATE TABLE IF NOT EXISTS decorations (
    decoration_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    searchLink VARCHAR(255) NOT NULL,
    fkr BIGINT
);
