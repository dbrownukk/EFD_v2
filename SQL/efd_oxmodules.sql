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
-- Table structure for table `oxmodules`
--

DROP TABLE IF EXISTS `oxmodules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oxmodules` (
  `name` varchar(80) NOT NULL,
  `application` varchar(50) NOT NULL,
  `hidden` char(1) NOT NULL,
  `orderInFolder` int(11) DEFAULT NULL,
  `unrestricted` char(1) NOT NULL,
  `folder_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`name`,`application`),
  KEY `FK_b46jvjl7ok0n1nasyuptgjc3d` (`folder_id`),
  CONSTRAINT `FK_b46jvjl7ok0n1nasyuptgjc3d` FOREIGN KEY (`folder_id`) REFERENCES `oxfolders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oxmodules`
--

LOCK TABLES `oxmodules` WRITE;
/*!40000 ALTER TABLE `oxmodules` DISABLE KEYS */;
INSERT INTO `oxmodules` VALUES ('ChangePassword','EFD_v2','N',9999,'N','402880895ff401b9015ff401c1ed0000'),('Community','EFD_v2','N',9999,'N','402880895ff4076b015ff40bea340003'),('CommunityYearNotes','EFD_v2','Y',9999,'N',NULL),('Configuration','EFD_v2','N',9999,'N','402880895ff401b9015ff401c1ed0000'),('ConfigurationRecord','EFD_v2','Y',9999,'N','402880895ff401b9015ff401c1ed0000'),('Country','EFD_v2','N',9999,'N','402880895ff4076b015ff40b06d30002'),('Folder','EFD_v2','N',9999,'N','402880895ff401b9015ff401c1ed0000'),('LivelihoodZone','EFD_v2','N',9999,'N','402880895ff4076b015ff40bea340003'),('Module','EFD_v2','N',9999,'N','402880895ff401b9015ff401c1ed0000'),('ModuleRights','EFD_v2','Y',9999,'N','402880895ff401b9015ff401c1ed0000'),('MyPersonalData','EFD_v2','N',9999,'N','402880895ff401b9015ff401c1ed0000'),('Organization','EFD_v2','N',9999,'N','402880895ff401b9015ff401c1ed0000'),('Project','EFD_v2','N',9999,'N','402880895ff4076b015ff40bea340003'),('ResourceSubType','EFD_v2','N',9999,'N','402880895ff4076b015ff40b06d30002'),('ResourceType','EFD_v2','N',9999,'N','402880895ff4076b015ff40b06d30002'),('Role','EFD_v2','N',9999,'N','402880895ff401b9015ff401c1ed0000'),('SessionRecord','EFD_v2','Y',9999,'N','402880895ff401b9015ff401c1ed0000'),('Site','EFD_v2','N',9999,'N','402880895ff4076b015ff40bea340003'),('User','EFD_v2','N',9999,'N','402880895ff401b9015ff401c1ed0000'),('WealthGroup','EFD_v2','N',9999,'N','402880895ff4076b015ff40bea340003'),('WGCharacteristicsResource','EFD_v2','N',9999,'N','402880895ff4076b015ff40bea340003');
/*!40000 ALTER TABLE `oxmodules` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-26 13:11:57
