-- temporarily disable FK constraints
SET REFERENTIAL_INTEGRITY FALSE;
-- fill avatars table
insert into AVATAR (id, file_name) values (1, 'avatar0.jpg');
insert into AVATAR (id, file_name) values (2, 'avatar1.jpg');
insert into AVATAR (id, file_name) values (3, 'avatar2.jpg');
insert into AVATAR (id, file_name) values (4, 'avatar3.jpg');
insert into AVATAR (id, file_name) values (5, 'avatar4.jpg');
insert into AVATAR (id, file_name) values (6, 'avatar5.jpg');
insert into AVATAR (id, file_name) values (7, 'avatar6.jpg');
insert into AVATAR (id, file_name) values (8, 'avatar7.jpg');

-- Roles
insert into ROLE (ID, ROLE) values (1, 'ADMIN');
insert into ROLE (ID, ROLE) values (2, 'USER');

-- categories
insert into CATEGORY (ID, NAME, CREATED) values (1, 'Business', current_timestamp );
insert into CATEGORY (ID, NAME, CREATED) values (2, 'Heath & Fitness', current_timestamp );
insert into CATEGORY (ID, NAME, CREATED) values (3, 'Tech Gadgets', current_timestamp );
insert into CATEGORY (ID, NAME, CREATED) values (4, 'Web Development', current_timestamp );

-- users
insert into USER (ID, EMAIL, ENABLED, ENCRYPTED_PASSWORD, FIRST_NAME, LAST_NAME, USER_NAME, USER_PREFS_ID )
  VALUES (1,'admin@blogen.com',true, '{bcrypt}$2a$10$KFGHLT74AQ6QsEa42m2p9e7SaiLGcrjiYYue8KjSZJZTGicKUog.m', 'Carl', 'Sagan','admin',1);
insert into USER (ID, EMAIL, ENABLED, ENCRYPTED_PASSWORD, FIRST_NAME, LAST_NAME, USER_NAME, USER_PREFS_ID )
  VALUES (2,'johndoe@gmail.com',true, '{bcrypt}$2a$10$OND.oWEP5h/MW9XtkLgus.L2trjets5OoqSQvp6W6oo0BiVdgzW3a', 'John', 'Doe','johndoe',2);
insert into USER (ID, EMAIL, ENABLED, ENCRYPTED_PASSWORD, FIRST_NAME, LAST_NAME, USER_NAME, USER_PREFS_ID )
  VALUES (3,'gilly@gmail.com',true, '{bcrypt}$2a$10$lfhS3fX1ICGXdt3aE0C/TOlQ03dkFbaYQfjmcNGm6qxIs0Of71g2C', 'Maggie', 'McGill','mcgill',3);
insert into USER (ID, EMAIL, ENABLED, ENCRYPTED_PASSWORD, FIRST_NAME, LAST_NAME, USER_NAME, USER_PREFS_ID )
  VALUES (4,'scotsman@gmail.com',true, '{bcrypt}$2a$10$OND.oWEP5h/MW9XtkLgus.L2trjets5OoqSQvp6W6oo0BiVdgzW3a', 'William', 'Wallace','scotsman',4);
insert into USER (ID, EMAIL, ENABLED, ENCRYPTED_PASSWORD, FIRST_NAME, LAST_NAME, USER_NAME, USER_PREFS_ID )
  VALUES (5,'lizreed@gmail.com',true, '{bcrypt}$2a$10$OND.oWEP5h/MW9XtkLgus.L2trjets5OoqSQvp6W6oo0BiVdgzW3a', 'Elizabeth', 'Reed','lizreed',5);

-- user prefs
insert into USER_PREFS (ID, USER_ID, AVATAR_ID) values (1, 1, 1);
insert into USER_PREFS (ID, USER_ID, AVATAR_ID) values (2, 2, 3);
insert into USER_PREFS (ID, USER_ID, AVATAR_ID) values (3, 3, 2);
insert into USER_PREFS (ID, USER_ID, AVATAR_ID) values (4, 4, 4);
insert into USER_PREFS (ID, USER_ID, AVATAR_ID) values (5, 5, 5);

-- map users to roles
insert into USER_ROLES (USERS_ID, ROLES_ID) values (1, 1);
insert into USER_ROLES (USERS_ID, ROLES_ID) values (1, 2);
insert into USER_ROLES (USERS_ID, ROLES_ID) values (2, 2);
insert into USER_ROLES (USERS_ID, ROLES_ID) values (3, 2);
insert into USER_ROLES (USERS_ID, ROLES_ID) values (4, 2);
insert into USER_ROLES (USERS_ID, ROLES_ID) values (5, 2);

-- create some posts
INSERT INTO POST(ID, CREATED, IMAGE_URL, TEXT, TITLE, UUID, CATEGORY_ID, PARENT_ID, USER_ID) VALUES
(1, TIMESTAMP '2017-01-01 10:11:12', 'https://picsum.photos/300/200/?image=1058', 'Smart-phones are the greatest invention in the history of mankind', 'Love this tech', '957322f4-3016-4099-b81a-eb871da2e7a6', 3, NULL, 2),
(2, TIMESTAMP '2017-01-01 10:12:12', 'https://picsum.photos/300/200/?image=1059', 'I wish I could embed the phone into my head', 'Love it too', 'f5b8fac2-b8ee-4457-830c-cbf7fa2a88d8', 3, 1, 2),
(3, TIMESTAMP '2017-01-01 10:13:12', 'https://picsum.photos/300/200/?image=1060', 'Are they even greater than the Internet?', 'Not so fast', '8b3bd26f-c65a-40fd-81fc-e6c2300d17c5', 3, 1, 3),
(4, TIMESTAMP '2017-01-01 10:14:12', 'https://picsum.photos/300/200/?image=1061', 'They''re the greatest for now, but something better will come along', 'Here today gone tomorrow', '15cf5c5c-db11-4102-92c4-a066ae9d8281', 3, 1, 4),
(5, TIMESTAMP '2017-01-01 10:15:12', 'https://picsum.photos/300/200/?image=1062', 'the greatest invention is velcro :)', 'No No No', '04ace78c-dc7d-41be-87f9-5410b1774181', 3, 1, 4),
(6, TIMESTAMP '2017-02-01 10:11:12', 'https://picsum.photos/300/200/?image=1063', 'Trying to burn off these holiday calories. I hear resistence training is better than running', 'Started lifting today', '15f5fcde-cbe6-4476-a622-7e184bb762de', 4, NULL, 4),
(7, TIMESTAMP '2017-03-01 10:11:12', 'https://picsum.photos/300/200/?image=1064', 'They stock markets won''t stop running higher. When will the bubble burst?', 'Bulls are on parade', '46bc486d-4a6a-4699-93e3-8951ffb8bd8b', 1, NULL, 4),
(8, TIMESTAMP '2017-04-01 10:11:12', 'https://picsum.photos/300/200/?image=1065', 'Forget about gold, I''m all in on bitcoin', 'Bitcoin or bust', '748ab15f-460c-4f4c-8e4f-21b120c9976c', 1, NULL, 3),
(9, TIMESTAMP '2017-04-02 10:11:12', 'https://picsum.photos/300/200/?image=1066', 'I''m game too. I just don''t know where to but it from', 'probably buying it', '205c2f87-5018-4a2d-8718-804338d2b67f', 1, 8, 2),
(10, TIMESTAMP '2017-04-03 10:11:12', 'https://picsum.photos/300/200/?image=1067', 'If we''ve waited this long, I fear it''s already too late', 'beware the bubble', '6034bb6d-7625-4adf-ad77-7dfd27cca5f7', 1, 8, 3),
(11, TIMESTAMP '2017-05-01 10:11:12', 'https://picsum.photos/300/200/?image=1068', 'It used to be all the rage, now I can''t find a single gym that offers it', 'What ever happened to Jazzercise?', '24f9ba42-6354-4240-a057-86a9beaf7b8e', 4, NULL, 3),
(12, TIMESTAMP '2017-05-02 10:11:12', 'https://picsum.photos/300/200/?image=1069', 'Jazzercise? Really? What do you do, listen to jazz until you pass out?', 'sounds gross', '9ba45df1-c439-4f37-9e0f-2b55dfa2a1de', 4, 11, 4),
(13, TIMESTAMP '2017-05-03 10:11:12', 'https://picsum.photos/300/200/?image=1070', 'I changed my mind. It looks kinda fun. Someone get me leg warmers', 'on second thought...', '01f841d6-1c8b-4b0c-9a4b-96442ad4735c', 4, 11, 4),
(14, TIMESTAMP '2017-06-01 10:11:12', 'https://picsum.photos/300/200/?image=1071', 'Does anyone have stats on PHP usage in the wild?', 'Is PHP dead?', '937f5678-bad7-40d8-b5ef-a943391e516f', 2, NULL, 3),
(15, TIMESTAMP '2017-06-02 10:11:12', 'https://picsum.photos/300/200/?image=1072', 'PHP is everywhere. I''m pretty sure it still powers the internet!', 'I doubt it', 'b4882542-07b7-427b-bfa6-61b43dcb74e8', 2, 14, 4),
(16, TIMESTAMP '2017-06-03 10:11:12', 'https://picsum.photos/300/200/?image=1073', '...anymore. We switched to Kotlin/React, but a lot of companies are still powered by PHP', 'We don''t use it here', '591c5ba1-9d0d-4047-86ac-7880096a605e', 2, 14, 1),
(17, TIMESTAMP '2017-01-01 10:11:12', 'https://picsum.photos/300/200/?image=1074', 'Market returns are crazy, there is still time to jump on in', 'Invest now', '9e1dab5f-2faa-4d41-9a47-64453157daae', 1, NULL, 5),
(18, TIMESTAMP '2017-01-02 10:11:12', 'https://picsum.photos/300/200/?image=1075', 'No matter what excercise you do, just remember you can never out-train a poor diet', 'Proper Diet trumps all', 'c7fd886f-36db-4623-8bd1-6fd315a41e2c', 4, NULL, 5),
(19, TIMESTAMP '2017-01-03 10:11:12', 'https://picsum.photos/300/200/?image=1076', 'Does anyone own one of these? Is it any good?', 'About Alexa', '98dd0017-6dcd-4247-959d-f3a3804e43d9', 3, NULL, 5),
(20, TIMESTAMP '2017-01-04 10:11:12', 'https://picsum.photos/300/200/?image=1077', 'Hey you all. Would it be worth my time to learn Bootstrap 4?', 'Bootstrap 4', 'faae9658-5c58-44c8-a396-9ae060177318', 2, NULL, 5),
(21, TIMESTAMP '2017-01-05 10:11:12', 'https://picsum.photos/300/200/?image=1078', 'I wanna buy some gold. Can someone point me in the right direction', 'Buying gold', 'ee1202ce-dea9-486c-a35a-696e46cab280', 1, NULL, 5),
(22, TIMESTAMP '2017-01-06 10:11:12', 'https://picsum.photos/300/200/?image=1079', 'Forget about running for hours on end. High Intensity Interval Training can give you all the benefits in half the time', 'HIIT Training', '9d1bff0e-4282-497d-bf2c-05cf392fabc0', 4, NULL, 5),
(23, TIMESTAMP '2017-01-07 10:11:12', 'https://picsum.photos/300/200/?image=1080', 'My nephew is showing an interest in programming. Can anyone recommend something for a ten year old?', 'Toys that teach Programming', 'aebd11e7-cc99-47bc-bd54-e96a23a2e37f', 3, NULL, 5),
(24, TIMESTAMP '2017-01-08 10:11:12', 'https://picsum.photos/300/200/?image=1081', 'You guys need to try this http://clojure.org, It saved me hours of web dev work', 'Clojure Script', '628e32f1-d1cc-4f62-9f49-e01b8c66f60b', 2, NULL, 5),
(25, TIMESTAMP '2017-01-09 10:11:12', 'https://picsum.photos/300/200/?image=1082', 'This phone is the greatest. Nice screen, good battery life, and tons of apps!', 'Samsung Galaxy 8', 'ccf0f7c2-5459-4c3a-ad77-7c79dc1c4dae', 3, NULL, 5),
(26, TIMESTAMP '2017-01-10 10:11:12', 'https://picsum.photos/300/200/?image=1083', 'I hear webFlux is all the rage in Spring Framework. Does anyone have first hand experience?', 'Spring Framework 5', 'fda52e65-c9ed-47b0-8cbd-0175851c40cb', 2, NULL, 5);

SET REFERENTIAL_INTEGRITY TRUE;