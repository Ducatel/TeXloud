-- phpMyAdmin SQL Dump
-- version 3.3.10deb1
-- http://www.phpmyadmin.net
--
-- Serveur: localhost
-- Généré le : Dim 08 Janvier 2012 à 20:08
-- Version du serveur: 5.1.54
-- Version de PHP: 5.3.5-1ubuntu7.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `TeXloud`
--

-- --------------------------------------------------------

--
-- Structure de la table `file`
--

CREATE TABLE IF NOT EXISTS `file` (
  `id` int(11) NOT NULL,
  `path` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `filename` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  `project_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_file_1` (`type_id`),
  KEY `fk_file_2` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `file`
--


-- --------------------------------------------------------

--
-- Structure de la table `group`
--

CREATE TABLE IF NOT EXISTS `group` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `group`
--


-- --------------------------------------------------------

--
-- Structure de la table `group_user`
--

CREATE TABLE IF NOT EXISTS `group_user` (
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `fk_group_user_1` (`group_id`),
  KEY `fk_group_user_2` (`user_id`),
  KEY `fk_group_user_3` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `group_user`
--


-- --------------------------------------------------------

--
-- Structure de la table `permission`
--

CREATE TABLE IF NOT EXISTS `permission` (
  `id` int(11) NOT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `permission`
--


-- --------------------------------------------------------

--
-- Structure de la table `project`
--

CREATE TABLE IF NOT EXISTS `project` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `server_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_project_1` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `project`
--


-- --------------------------------------------------------

--
-- Structure de la table `type`
--

CREATE TABLE IF NOT EXISTS `type` (
  `id` int(11) NOT NULL,
  `extension` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `type`
--


-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lastname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `address` varchar(511) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zip` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `country` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `salt` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Contenu de la table `user`
--


--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `file`
--
ALTER TABLE `file`
  ADD CONSTRAINT `fk_file_1` FOREIGN KEY (`type_id`) REFERENCES `type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_file_2` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `group_user`
--
ALTER TABLE `group_user`
  ADD CONSTRAINT `fk_group_user_1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_group_user_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_group_user_3` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `fk_project_1` FOREIGN KEY (`id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
