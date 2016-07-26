DROP DATABASE IF EXISTS diagram;
CREATE DATABASE diagram;
USE diagram;

DROP TABLE IF EXISTS robots;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS properties;
DROP TABLE IF EXISTS links;
DROP TABLE IF EXISTS nodes;
DROP TABLE IF EXISTS diagrams;
DROP TABLE IF EXISTS folders;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  username VARCHAR(45) NOT NULL,
  password VARCHAR(60) NOT NULL,
  enabled  TINYINT     NOT NULL DEFAULT 1,
  PRIMARY KEY (username)
);

CREATE TABLE folders (
  folder_id        BIGINT      NOT NULL AUTO_INCREMENT,
  folder_name      VARCHAR(255) NOT NULL,
  username         VARCHAR(45) NOT NULL,
  folder_parent_id BIGINT,
  PRIMARY KEY (folder_id),
  FOREIGN KEY (folder_parent_id) REFERENCES folders (folder_id)
);

CREATE TABLE diagrams (
  diagram_id BIGINT      NOT NULL AUTO_INCREMENT,
  name       VARCHAR(255) NOT NULL,
  folder_id  BIGINT      DEFAULT NULL,
  PRIMARY KEY (diagram_id),
  FOREIGN KEY (folder_id) REFERENCES folders (folder_id)
);

CREATE TABLE nodes (
  id                  VARCHAR(255)  NOT NULL,
  logical_id          VARCHAR(255)  NOT NULL,
  graphical_id        VARCHAR(50)  NOT NULL,
  type                VARCHAR(50)  NOT NULL,
  diagram_id          BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (diagram_id) REFERENCES diagrams (diagram_id)
);

CREATE TABLE links (
  id                  VARCHAR(255)  NOT NULL,
  logical_id          VARCHAR(255)  NOT NULL,
  graphical_id        VARCHAR(50)  NOT NULL,
  diagram_id          BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (diagram_id) REFERENCES diagrams (diagram_id)
);

CREATE TABLE node_properties (
  property_id VARCHAR(255) NOT NULL,
  name        VARCHAR(50) NOT NULL,
  value       VARCHAR(255) NOT NULL,
  type        VARCHAR(50) NOT NULL,
  node_id     VARCHAR(255),
  PRIMARY KEY (property_id),
  FOREIGN KEY (node_id) REFERENCES nodes (id)
);

CREATE TABLE link_properties (
  property_id VARCHAR(50) NOT NULL,
  name        VARCHAR(50) NOT NULL,
  value       VARCHAR(255) NOT NULL,
  type        VARCHAR(50) NOT NULL,
  link_id     VARCHAR(255),
  PRIMARY KEY (property_id),
  FOREIGN KEY (link_id) REFERENCES links (id)
);

CREATE TABLE user_roles (
  user_role_id INT(11)     NOT NULL AUTO_INCREMENT,
  username     VARCHAR(45) NOT NULL,
  ROLE         VARCHAR(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (ROLE, username),
  KEY fk_username_idx (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username)
);


CREATE TABLE robots (
  id         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name       VARCHAR(45)     NOT NULL,
  ssid VARCHAR(45) NOT NULL,
  username   VARCHAR(45)     NOT NULL,
  status VARCHAR(45),
  UNIQUE KEY uni_robotName_username (name, username),
  FOREIGN KEY (username) REFERENCES users (username)
);

INSERT INTO users (username, password, enabled) VALUES ('user', '$2a$04$eWY0Czs5avEcJTDVRcB66.8n5mQmMnRCCTi6P/9oNAncfzyb8.D7e', TRUE);
INSERT INTO user_roles (username, ROLE) VALUES ('user', 'ROLE_USER');
INSERT INTO folders (folder_id, folder_name, username) VALUES (1, 'root', 'user');
