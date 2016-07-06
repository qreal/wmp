INSERT INTO users (username, password, enabled) VALUES ('user', '$2a$04$eWY0Czs5avEcJTDVRcB66.8n5mQmMnRCCTi6P/9oNAncfzyb8.D7e', TRUE);
INSERT INTO folders (folder_id, folder_name, username) VALUES (1, 'root', 'user');
INSERT INTO user_roles (username, ROLE) VALUES ('user', 'ROLE_USER');