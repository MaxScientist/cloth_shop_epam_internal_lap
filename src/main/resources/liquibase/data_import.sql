INSERT INTO permission ( name) VALUES ('WRITE');
INSERT INTO permission ( name) VALUES ('READ');

INSERT INTO role ( ROLE_TYPE) VALUES ('ADMIN');
INSERT INTO role ( ROLE_TYPE) VALUES ('USER');

INSERT INTO ROLE_PERMISSION (ROLE_ID, PERMISSION_ID) VALUES (1, 1);
INSERT INTO ROLE_PERMISSION (role_id, PERMISSION_ID) VALUES (1, 2);
INSERT INTO ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) VALUES (2, 2);

INSERT INTO users (email, first_name, last_name, PASSWORD, PHONE_NUMBER, USER_NAME, role_id) VALUES ('admin@mail.com', 'admin', 'admin', '$2a$12$gwsStJT0rloNni4ZWUKiaeGEvrUQeAKTWHeRwvIPwzMeYkpeYDAaO', null, 'admin', 1);
INSERT INTO users (email, first_name, last_name, PASSWORD, PHONE_NUMBER, USER_NAME, role_id) VALUES ('user@mail.com', 'user', 'user', 'user', null, 'user', 2);
