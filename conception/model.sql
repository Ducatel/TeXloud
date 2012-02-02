-- MySQL dump 10.13  Distrib 5.1.58, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: entrespecialistes
-- ------------------------------------------------------
-- Server version	5.1.58-1ubuntu1

SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT;
SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS;
SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION;
SET NAMES utf8;
SET @OLD_TIME_ZONE=@@TIME_ZONE;
SET TIME_ZONE='+00:00';
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';
SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0;

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user(
	id INT PRIMARY KEY AUTO_INCREMENT,
	firstname VARCHAR(255),
	lastname VARCHAR(255),
	username VARCHAR(255) NOT NULL UNIQUE,
	gender TINYINT(1),
	date_of_birth DATE,
	address VARCHAR(511),
	zip VARCHAR(15),
	city VARCHAR(255),
	country VARCHAR(255),
	email VARCHAR(255) NOT NULL UNIQUE,
	salt VARCHAR(32) NOT NULL,
	password VARCHAR(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO user VALUES(1, 'Meva', 'Rakotondratsima', 'meva', 0, '1989-11-17', '76 rue des Mouettes', '76600', 'Le Havre', 'France', 'plip@plop.com', 'b7b7b7e7e9a4e639bcb0e9f6b2d5567a', '65932ea15759a926b02a7e99f17df2275aa45983');

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE permission(
	id INT PRIMARY KEY AUTO_INCREMENT,
	type VARCHAR(255) NOT NULL
)	ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
	/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO permission (id, type) VALUES (1, 'admin'), (2, 'work'), (3, 'read');

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ugroup`(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL
)	ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
	/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `ugroup` VALUES(1, 'groupe de Meva 1');
INSERT INTO `ugroup` VALUES(2, 'groupe de Meva 2');

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE group_user(
	user_id INT NOT NULL references user(id),
	ugroup_id INT NOT NULL references `ugroup` (`id`),
	status VARCHAR(255),
	permission_id INT NOT NULL DEFAULT 3 references permission(id),
	PRIMARY KEY (user_id, ugroup_id)
)	ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
	/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO group_user VALUES(1, 1, 'valide', 1);
INSERT INTO group_user VALUES(1, 2, 'valide', 1);

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE project(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	server_url VARCHAR(255),
	ugroup_id INT NOT NULL references `ugroup` (`id`)
)	ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
	/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO project VALUES(1, 'Projet de demo 1', '127.0.0.1', 1);
INSERT INTO project VALUES(2, 'Projet de demo 2', '127.0.0.1', 2);

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE file(
	id INT PRIMARY KEY AUTO_INCREMENT,
	path VARCHAR(255) NOT NULL,
	project_id INT NOT NULL references project(id),
	parent INT DEFAULT NULL,
	is_dir TINYINT(1) DEFAULT 0
)	ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
	/*!40101 SET character_set_client = @saved_cs_client */;
	
SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT;
SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS;
SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION;
SET NAMES utf8;
SET @OLD_TIME_ZONE=@@TIME_ZONE;
SET TIME_ZONE='+00:00';
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';
SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0;