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
-- Table structure for table `oxusers`
--

DROP TABLE IF EXISTS `oxusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oxusers` (
  `name` varchar(30) NOT NULL,
  `active` char(1) NOT NULL,
  `authenticateWithLDAP` char(1) NOT NULL,
  `birthDate` datetime DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  `failedLoginAttempts` int(11) NOT NULL,
  `familyName` varchar(30) DEFAULT NULL,
  `forceChangePassword` char(1) NOT NULL,
  `givenName` varchar(30) DEFAULT NULL,
  `jobTitle` varchar(30) DEFAULT NULL,
  `lastLoginDate` datetime DEFAULT NULL,
  `lastPasswordChangeDate` datetime DEFAULT NULL,
  `middleName` varchar(30) DEFAULT NULL,
  `nickName` varchar(30) DEFAULT NULL,
  `password` varchar(41) DEFAULT NULL,
  `passwordRecoveringCode` varchar(32) DEFAULT NULL,
  `passwordRecoveringDate` datetime DEFAULT NULL,
  `recentPassword1` varchar(41) DEFAULT NULL,
  `recentPassword2` varchar(41) DEFAULT NULL,
  `recentPassword3` varchar(41) DEFAULT NULL,
  `recentPassword4` varchar(41) DEFAULT NULL,
  PRIMARY KEY (`name`),
  KEY `UK_1kr6bnx5w22vlyob1y4p8qqft` (`email`),
  KEY `UK_4scpsyerabcdjqu5hj2yq3el7` (`passwordRecoveringCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oxusers`
--

LOCK TABLES `oxusers` WRITE;
/*!40000 ALTER TABLE `oxusers` DISABLE KEYS */;
INSERT INTO `oxusers` VALUES ('admin','Y','N',NULL,'2017-11-25 16:27:57','a@b.com',0,'','N','','','2017-11-25 17:07:51','2017-11-25 16:27:57','','','-2fcc1dd51cb7514a99f03debf513ca7af3b25669',NULL,NULL,NULL,NULL,NULL,NULL),('efd','Y','N',NULL,'2017-11-25 00:00:00','efd@efd.com',0,'','N','','','2017-11-25 17:19:28','2017-11-25 16:41:12','','','701c4714e78900071c1e3db9ee378c0585df9a0a',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `oxusers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-26 13:11:59
