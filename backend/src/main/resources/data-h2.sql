-- fill avatars table
insert into AVATAR (id, file_name) values (1, 'avatar0.jpg');
insert into AVATAR (id, file_name) values (2, 'avatar1.jpg');
insert into AVATAR (id, file_name) values (3, 'avatar2.jpg');
insert into AVATAR (id, file_name) values (4, 'avatar3.jpg');
insert into AVATAR (id, file_name) values (5, 'avatar4.jpg');
insert into AVATAR (id, file_name) values (6, 'avatar5.jpg');
insert into AVATAR (id, file_name) values (7, 'avatar6.jpg');
insert into AVATAR (id, file_name) values (8, 'avatar7.jpg');

-- users
insert into USER (ID, EMAIL, ENABLED, ENCRYPTED_PASSWORD, FIRST_NAME, LAST_NAME, USER_NAME, USER_PREFS_ID )
  VALUES (1,'johndoe@gmail.com',true, '{bcrypt}sdf', 'John', 'Doe','johndoe',1);

-- user prefs
insert into USER_PREFS (ID, USER_ID, AVATAR_ID) values (1, 1, 1);