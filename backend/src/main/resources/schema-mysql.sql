DROP DATABASE IF EXISTS blogen;
CREATE DATABASE blogen DEFAULT CHARACTER SET UTF8MB4 DEFAULT COLLATE utf8mb4_general_ci;

USE blogen;

create table IF NOT EXISTS avatar (
  id BIGINT auto_increment,
  file_name VARCHAR(255),
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS category (
  id BIGINT AUTO_INCREMENT,
  created TIMESTAMP,
  name VARCHAR(255),
  PRIMARY KEY (id));

CREATE TABLE post (
  id BIGINT AUTO_INCREMENT,
  created timestamp,
  image_url VARCHAR(255),
  text VARCHAR(255),
  title VARCHAR(255),
  uuid VARCHAR(255),
  category_id BIGINT,
  parent_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (id));

create table role (
  id bigint AUTO_INCREMENT,
  role varchar(255),
  primary key (id));

create table user (
  id bigint AUTO_INCREMENT,
  email varchar(255),
  enabled boolean,
  encrypted_password varchar(255),
  first_name varchar(255),
  last_name varchar(255),
  user_name varchar(255) NOT NULL ,
  user_prefs_id bigint,
  primary key (id));

create table user_roles (
  users_id bigint not null,
  roles_id bigint not null);

create table user_prefs (
  id bigint AUTO_INCREMENT,
  avatar_id bigint,
  user_id bigint,
  primary key (id));

alter table avatar add constraint UK_AVATAR_FILE_NAME unique (file_name);
alter table category add constraint UK_CATEGORY_NAME unique (name);
alter table post add constraint FK_POST_CATEGORY__CATEGORY_ID foreign key (category_id) references category(id);
alter table post add constraint FK_POST_POST__PARENT_ID foreign key (parent_id) references post(id);
alter table post add constraint FK_POST_USER__USER_ID foreign key (user_id) references user(id);
alter table user add constraint FK_USER_USER_PREFS__USER_PREFS_ID foreign key (user_prefs_id) references user_prefs(id);
alter table user_roles add constraint FK_USER_ROLES_ROLE__ROLES_ID foreign key (roles_id) references role(id);
alter table user_roles add constraint FK_USER_ROLES_USER__USERS_ID foreign key (users_id) references user(id);
alter table user_prefs add constraint FK_USER_PREFS_AVATAR__AVATAR_ID foreign key (avatar_id) references avatar(id);
alter table user_prefs add constraint FK_USER_PREFS_USER__USER_ID foreign key (user_id) references user(id);