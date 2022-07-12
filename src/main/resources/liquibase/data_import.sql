INSERT INTO permission (id, name) VALUES (1, 'WRITE');
INSERT INTO permission (id, name) VALUES (2, 'READ');

INSERT INTO role (id, ROLE_TYPE) VALUES (1, 'ADMIN');
INSERT INTO role (id, ROLE_TYPE) VALUES (2, 'USER');

INSERT INTO ROLE_PERMISSION (role_id, PERMISSION_ID) VALUES (1, 1);
INSERT INTO ROLE_PERMISSION (role_id, PERMISSION_ID) VALUES (1, 2);
INSERT INTO ROLE_PERMISSION (role_id, PERMISSION_ID) VALUES (2, 2);

INSERT INTO users (id, email, first_name, last_name, password, PHONE_NUMBER, USER_NAME, role_id) VALUES (1, 'admin@mail.com', 'admin', 'admin', 'admin', null, 'admin', 1);
INSERT INTO users (id, email, first_name, last_name, password, PHONE_NUMBER, USER_NAME, role_id) VALUES (2, 'user@mail.com', 'user', 'user', 'user', null, 'user', 2);
