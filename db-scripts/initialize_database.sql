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
    room_name VARCHAR(255) NOT NULL,
    fku BIGINT,
    FOREIGN KEY(fku) references users(user_id)
);
-- Create decorations table
CREATE TABLE IF NOT EXISTS decorations (
    decoration_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    search_link VARCHAR(255) NOT NULL,
    fkr BIGINT,
    wish_id BIGINT,
    description VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    image VARCHAR(225) NOT NULL,
    FOREIGN KEY(fkr) references rooms(room_id),
    FOREIGN KEY(wish_id) references users(user_id)
);

-- Create invitations table
CREATE TABLE IF NOT EXISTS invitations (
    invitation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    owner_id BIGINT,
    room_id BIGINT,
    room_name VARCHAR(255) NOT NULL,
    sender_name VARCHAR(255) NOT NULL
);

-- Create join table for collaborations on rooms
CREATE TABLE IF NOT EXISTS collaborations(
    collaboration_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    FOREIGN KEY(user_id) references users(user_id),
    FOREIGN KEY(room_id) references rooms(room_id)
);
