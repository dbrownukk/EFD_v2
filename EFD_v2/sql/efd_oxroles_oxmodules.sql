-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: efd
-- ------------------------------------------------------
-- Server version	5.7.18-log

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
-- Table structure for table `oxroles_oxmodules`
--

DROP TABLE IF EXISTS `oxroles_oxmodules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oxroles_oxmodules` (
  `excludedActions` longtext,
  `excludedMembers` longtext,
  `readOnlyMembers` longtext,
  `roles_name` varchar(30) NOT NULL,
  `modules_name` varchar(80) NOT NULL,
  `modules_application` varchar(50) NOT NULL,
  PRIMARY KEY (`roles_name`,`modules_name`,`modules_application`),
  KEY `FK_fy576omq6gi0fxbelv0r1gc63` (`modules_name`,`modules_application`),
  CONSTRAINT `FK_fy576omq6gi0fxbelv0r1gc63` FOREIGN KEY (`modules_name`, `modules_application`) REFERENCES `oxmodules` (`name`, `application`),
  CONSTRAINT `FK_hob07vk3yempwywcgk78ksmq1` FOREIGN KEY (`roles_name`) REFERENCES `oxroles` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oxroles_oxmodules`
--

LOCK TABLES `oxroles_oxmodules` WRITE;
/*!40000 ALTER TABLE `oxroles_oxmodules` DISABLE KEYS */;
INSERT INTO `oxroles_oxmodules` VALUES (NULL,NULL,NULL,'admin','Configuration','EFD_v2'),(NULL,NULL,NULL,'admin','ConfigurationRecord','EFD_v2'),(NULL,NULL,NULL,'admin','Folder','EFD_v2'),(NULL,NULL,NULL,'admin','Module','EFD_v2'),(NULL,NULL,NULL,'admin','ModuleRights','EFD_v2'),(NULL,NULL,NULL,'admin','Organization','EFD_v2'),(NULL,NULL,NULL,'admin','Role','EFD_v2'),(NULL,NULL,NULL,'admin','SessionRecord','EFD_v2'),(NULL,NULL,NULL,'admin','User','EFD_v2'),(NULL,NULL,NULL,'efd_user','Community','EFD_v2'),(NULL,NULL,NULL,'efd_user','CommunityYearNotes','EFD_v2'),(NULL,NULL,NULL,'efd_user','Country','EFD_v2'),(NULL,NULL,NULL,'efd_user','LivelihoodZone','EFD_v2'),(NULL,NULL,NULL,'efd_user','Project','EFD_v2'),(NULL,NULL,NULL,'efd_user','ResourceSubType','EFD_v2'),(NULL,NULL,NULL,'efd_user','ResourceType','EFD_v2'),(NULL,NULL,NULL,'efd_user','Site','EFD_v2'),(NULL,NULL,NULL,'efd_user','WealthGroup','EFD_v2'),(NULL,NULL,NULL,'efd_user','WGCharacteristicsResource','EFD_v2'),(NULL,NULL,NULL,'self sign up','ChangePassword','EFD_v2'),(NULL,NULL,NULL,'self sign up','MyPersonalData','EFD_v2'),(NULL,NULL,NULL,'user','ChangePassword','EFD_v2'),(NULL,NULL,NULL,'user','Community','EFD_v2'),(NULL,NULL,NULL,'user','CommunityYearNotes','EFD_v2'),(NULL,NULL,NULL,'user','Country','EFD_v2'),(NULL,NULL,NULL,'user','LivelihoodZone','EFD_v2'),(NULL,NULL,NULL,'user','MyPersonalData','EFD_v2'),(NULL,NULL,NULL,'user','Project','EFD_v2'),(NULL,NULL,NULL,'user','ResourceSubType','EFD_v2'),(NULL,NULL,NULL,'user','ResourceType','EFD_v2'),(NULL,NULL,NULL,'user','Site','EFD_v2'),(NULL,NULL,NULL,'user','WealthGroup','EFD_v2'),(NULL,NULL,NULL,'user','WGCharacteristicsResource','EFD_v2');
/*!40000 ALTER TABLE `oxroles_oxmodules` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-26 13:11:58
