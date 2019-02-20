-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: localhost    Database: blogen
-- ------------------------------------------------------
-- Server version	5.7.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `avatar`
--

DROP TABLE IF EXISTS `avatar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `avatar` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `file_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `UK_1e56l1ui8p8eoxa9e1xsrkak` (`file_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `created` datetime DEFAULT NULL,
                          `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `UK_46ccwnsi9409t36lurvtyljak` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                      `created` datetime DEFAULT NULL,
                      `image_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                      `text` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                      `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                      `uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                      `category_id` bigint(20) DEFAULT NULL,
                      `parent_id` bigint(20) DEFAULT NULL,
                      `user_id` bigint(20) DEFAULT NULL,
                      PRIMARY KEY (`id`),
                      KEY `FKg6l1ydp1pwkmyj166teiuov1b` (`category_id`),
                      KEY `FK5yhuvatu7cubfxyltetys1c3n` (`parent_id`),
                      KEY `FK72mt33dhhs48hf9gcqrq4fxte` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                      `role` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                      PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                      `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                      `enabled` bit(1) DEFAULT NULL,
                      `encrypted_password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                      `first_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                      `last_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                      `user_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                      `user_prefs_id` bigint(20) DEFAULT NULL,
                      PRIMARY KEY (`id`),
                      KEY `FKhsb0xqvo1jo6p7q90w5yd0rwp` (`user_prefs_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_prefs`
--

DROP TABLE IF EXISTS `user_prefs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_prefs` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `avatar_id` bigint(20) DEFAULT NULL,
                            `user_id` bigint(20) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKnx1542pdp3mbq55mkoc4wfffn` (`avatar_id`),
                            KEY `FKiyryuw6fswahn38272xddn326` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
                            `users_id` bigint(20) NOT NULL,
                            `roles_id` bigint(20) NOT NULL,
                            KEY `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
                            KEY `FK7ecyobaa59vxkxckg6t355l86` (`users_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;