CREATE DATABASE  IF NOT EXISTS `dbcrawlermerlot` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `dbcrawlermerlot`;
-- MySQL dump 10.13  Distrib 5.1.40, for Win32 (ia32)
--
-- Host: localhost    Database: dbcrawlermerlot
-- ------------------------------------------------------
-- Server version	5.5.8

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
-- Table structure for table `loauthors`
--

DROP TABLE IF EXISTS `loauthors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loauthors` (
  `ID_Author` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(300) DEFAULT NULL,
  `ID_Organization` int(10) unsigned DEFAULT NULL,
  `Email` varchar(150) DEFAULT NULL,
  `ID_Author_Merlot` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID_Author`),
  KEY `FK_loauthors_1` (`ID_Organization`),
  CONSTRAINT `FK_loauthors_1` FOREIGN KEY (`ID_Organization`) REFERENCES `loorganizations` (`ID_Organization`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28388 DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 32768 kB';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `locomments`
--

DROP TABLE IF EXISTS `locomments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locomments` (
  `ID_Comment` int(10) unsigned NOT NULL,
  `Material` varchar(300) DEFAULT NULL,
  `Rating` int(10) unsigned DEFAULT NULL,
  `Classroom_Use` varchar(100) DEFAULT NULL,
  `users_ID_User` int(11) NOT NULL DEFAULT '0',
  `Remarks` text,
  `Technical_Remarks` text,
  `Date_Added` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_Comment`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lodata_has_primaryaud`
--

DROP TABLE IF EXISTS `lodata_has_primaryaud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lodata_has_primaryaud` (
  `lodata_ID_LO` int(11) NOT NULL,
  `PrimaryAud_idPrimary_Audience` int(11) NOT NULL,
  PRIMARY KEY (`lodata_ID_LO`,`PrimaryAud_idPrimary_Audience`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `ID_User` int(11) NOT NULL,
  `Name` varchar(60) NOT NULL,
  `Ribbon` varchar(20) NOT NULL,
  `Author` tinyint(1) NOT NULL,
  `Vsb` tinyint(1) NOT NULL,
  `Peer_Reviewer` tinyint(1) NOT NULL,
  `Merlot_Award` varchar(50) NOT NULL,
  `locategories_ID_Cat` int(10) unsigned NOT NULL,
  `memberType` varchar(45) DEFAULT NULL,
  `lastLogin` date DEFAULT NULL,
  `memberSince` date DEFAULT NULL,
  `userscol` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID_User`),
  KEY `fk_users_locategories1` (`locategories_ID_Cat`),
  CONSTRAINT `fk_users_locategories1` FOREIGN KEY (`locategories_ID_Cat`) REFERENCES `locategories` (`ID_Cat`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `locategories`
--

DROP TABLE IF EXISTS `locategories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locategories` (
  `ID_Cat` int(10) unsigned NOT NULL,
  `Categorie` varchar(150) NOT NULL,
  PRIMARY KEY (`ID_Cat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `loorganizations`
--

DROP TABLE IF EXISTS `loorganizations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loorganizations` (
  `ID_Organization` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(400) NOT NULL,
  PRIMARY KEY (`ID_Organization`)
) ENGINE=InnoDB AUTO_INCREMENT=11210 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `loval_rev`
--

DROP TABLE IF EXISTS `loval_rev`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loval_rev` (
  `id_val_rev` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_valoration` int(10) unsigned NOT NULL,
  `id_review` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_val_rev`),
  KEY `FK_loval_rev_1` (`id_valoration`),
  KEY `FK_loval_rev_2` (`id_review`),
  CONSTRAINT `FK_loval_rev_1` FOREIGN KEY (`id_valoration`) REFERENCES `lovaloration` (`ID_Rating`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_loval_rev_2` FOREIGN KEY (`id_review`) REFERENCES `loreviews` (`id_review`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9131 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `locat_dat`
--

DROP TABLE IF EXISTS `locat_dat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locat_dat` (
  `ID_CatDat` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_LO` int(10) unsigned NOT NULL,
  `ID_Cat` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_CatDat`),
  KEY `FK_locat_dat_2` (`ID_Cat`),
  KEY `FK_locat_dat_1` (`ID_LO`),
  CONSTRAINT `FK_locat_dat_1` FOREIGN KEY (`ID_LO`) REFERENCES `lodata` (`ID_LO`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_locat_dat_2` FOREIGN KEY (`ID_Cat`) REFERENCES `locategories` (`ID_Cat`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=98024 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `metrics`
--

DROP TABLE IF EXISTS `metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metrics` (
  `lodata_ID_LO` int(10) unsigned NOT NULL,
  `Links_number` int(11) DEFAULT '0',
  `Links_unique_number` int(11) DEFAULT '0',
  `Internal_links` int(11) DEFAULT '0',
  `Unique_internal_links` int(11) DEFAULT '0',
  `External_links` int(11) DEFAULT '0',
  `Unique_external_links` int(11) DEFAULT '0',
  `Image_Number` int(11) DEFAULT '0',
  `Html_bytes` int(11) DEFAULT '0',
  `Images_Size` int(11) DEFAULT '0',
  `Script_number` int(11) DEFAULT '0',
  `Applets_Number` int(11) DEFAULT '0',
  `Display_word_count` int(11) DEFAULT '0',
  `Link_word_count` int(11) DEFAULT '0',
  `Number_of_pages` int(11) DEFAULT '0',
  `download_files` int(11) DEFAULT '0',
  `audio_files` int(11) DEFAULT '0',
  `video_files` int(11) DEFAULT '0',
  `multimedia_files` int(11) DEFAULT '0',
  `average_number_unique_internal_links` float DEFAULT '0',
  `average_number_internal_links` float DEFAULT '0',
  `average_number_unique_external_links` float DEFAULT '0',
  `average_number_external_links` float DEFAULT '0',
  `average_number_unique_links` float DEFAULT '0',
  `average_number_links` float DEFAULT '0',
  `average_number_words` float DEFAULT '0',
  `average_files_download` float DEFAULT '0',
  `average_audio_files` float DEFAULT '0',
  `average_video_files` float DEFAULT '0',
  `average_multimedia_files` float DEFAULT '0',
  `average_applets_number` float DEFAULT '0',
  `average_image_number` float DEFAULT '0',
  `average_html_bytes` float DEFAULT '0',
  `average_images_size` float DEFAULT '0',
  `average_scripts_number` float DEFAULT '0',
  `Page_Error` int(11) DEFAULT '0',
  `gunningFogIndex` float DEFAULT NULL,
  `weblinkErrors` int(11) DEFAULT NULL,
  `redundantLinks` int(11) DEFAULT NULL,
  `popUps` int(11) DEFAULT NULL,
  `colorCount` int(11) DEFAULT NULL,
  `bodyWordCount` int(11) DEFAULT NULL,
  `italicWordCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`lodata_ID_LO`),
  KEY `fk_Metrics_lodata` (`lodata_ID_LO`),
  CONSTRAINT `fk_Metrics_lodata` FOREIGN KEY (`lodata_ID_LO`) REFERENCES `lodata` (`ID_LO`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_lo`
--

DROP TABLE IF EXISTS `users_lo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_lo` (
  `lodata_ID_LO` int(10) unsigned NOT NULL,
  `users_ID_User` int(11) NOT NULL,
  `ID_Coll` int(11) DEFAULT NULL,
  PRIMARY KEY (`users_ID_User`,`lodata_ID_LO`),
  KEY `fk_users_has_lodata_users` (`users_ID_User`),
  KEY `fk_users_has_lodata_lodata` (`lodata_ID_LO`),
  CONSTRAINT `fk_users_has_lodata_lodata` FOREIGN KEY (`lodata_ID_LO`) REFERENCES `lodata` (`ID_LO`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_lodata_users` FOREIGN KEY (`users_ID_User`) REFERENCES `users` (`ID_User`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `primaryaud`
--

DROP TABLE IF EXISTS `primaryaud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `primaryaud` (
  `idPrimary_Audience` int(11) NOT NULL AUTO_INCREMENT,
  `Audience` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`idPrimary_Audience`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `loreviews`
--

DROP TABLE IF EXISTS `loreviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loreviews` (
  `id_review` int(10) unsigned NOT NULL,
  `material` varchar(300) CHARACTER SET utf8 DEFAULT NULL,
  `overview` text CHARACTER SET utf8,
  `learning_goals` text CHARACTER SET utf8,
  `author` varchar(300) CHARACTER SET utf8 DEFAULT NULL,
  `date_added` datetime DEFAULT NULL,
  `target_student_population` text CHARACTER SET utf8,
  `prerequisite_knowledge_or_skills` text CHARACTER SET utf8,
  `type_of_material` text CHARACTER SET utf8,
  `recommended_use` text CHARACTER SET utf8,
  `technical_requirements` text CHARACTER SET utf8,
  `content_quality_rating` double DEFAULT NULL,
  `content_quality_strengths` text CHARACTER SET utf8,
  `content_quality_concerns` text CHARACTER SET utf8,
  `efectiveness_rating` double DEFAULT NULL,
  `efectiveness_strengths` text CHARACTER SET utf8,
  `efectiveness_concerns` text CHARACTER SET utf8,
  `ease_of_use_rating` double DEFAULT NULL,
  `ease_of_use_strengths` text CHARACTER SET utf8,
  `ease_of_use_concerns` text CHARACTER SET utf8,
  `other_issues_and_comments` text CHARACTER SET utf8,
  `comments_from_the_author` text CHARACTER SET utf8,
  `id_peerReviewer` int(10) DEFAULT NULL,
  PRIMARY KEY (`id_review`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `loval_com`
--

DROP TABLE IF EXISTS `loval_com`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loval_com` (
  `ID_Val_Com` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_Valoration` int(10) unsigned NOT NULL,
  `ID_Comment` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_Val_Com`),
  KEY `FK_loval_com_1` (`ID_Valoration`),
  KEY `FK_loval_com_2` (`ID_Comment`),
  CONSTRAINT `FK_loval_com_1` FOREIGN KEY (`ID_Valoration`) REFERENCES `lovaloration` (`ID_Rating`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_loval_com_2` FOREIGN KEY (`ID_Comment`) REFERENCES `locomments` (`ID_Comment`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22560 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lodata`
--

DROP TABLE IF EXISTS `lodata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lodata` (
  `ID_LO` int(10) unsigned NOT NULL,
  `Title` varchar(300) DEFAULT NULL,
  `Material_Type` varchar(45) DEFAULT NULL,
  `Technical_Format` varchar(100) DEFAULT NULL,
  `Location` text,
  `Date_Added` date DEFAULT NULL,
  `Date_Modified` date DEFAULT NULL,
  `ID_Author` int(10) unsigned DEFAULT NULL,
  `Submitter_ID_User` int(11) DEFAULT '0',
  `Description` text,
  `Technical_Requirements` text,
  `Language` varchar(200) DEFAULT NULL,
  `Material_Version` varchar(200) DEFAULT NULL,
  `Copyright` tinyint(1) DEFAULT NULL,
  `Source_Code_Available` tinyint(1) DEFAULT NULL,
  `Accessibility_Information_Available` tinyint(1) DEFAULT NULL,
  `Cost_Involved` tinyint(1) DEFAULT NULL,
  `CreativeCommons` varchar(200) DEFAULT NULL,
  `MobileCompatibility` varchar(200) DEFAULT NULL,
  `Editors_Choice` tinyint(1) DEFAULT NULL,
  `Merlot_Classic` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_LO`),
  KEY `FK_lodata_1` (`ID_Author`),
  CONSTRAINT `FK_lodata_1` FOREIGN KEY (`ID_Author`) REFERENCES `loauthors` (`ID_Author`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='This table contais information about learning objects';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lovaloration`
--

DROP TABLE IF EXISTS `lovaloration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lovaloration` (
  `ID_LO` int(10) unsigned NOT NULL,
  `Peer_Reviews` varchar(45) DEFAULT NULL,
  `Comments` varchar(45) DEFAULT NULL,
  `Stars_Reviews` double DEFAULT NULL,
  `Stars_Comments` double DEFAULT NULL,
  `ID_Rating` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Personal_Collections` varchar(45) DEFAULT NULL,
  `Learning_Exercises` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID_Rating`),
  KEY `FK_lovaloration_1` (`ID_LO`),
  CONSTRAINT `FK_lovaloration_1` FOREIGN KEY (`ID_LO`) REFERENCES `lodata` (`ID_LO`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=74241 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-06-03 20:33:19
