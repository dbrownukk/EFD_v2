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
-- Table structure for table `oxusers_oxroles`
--

DROP TABLE IF EXISTS `oxusers_oxroles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oxusers_oxroles` (
  `OXUSERS_name` varchar(30) NOT NULL,
  `roles_name` varchar(30) NOT NULL,
  KEY `FK_brc1txk9tjjkd59ohpgdprlhw` (`roles_name`),
  KEY `FK_m8dvdguj1ip1mfm29xwqj9coi` (`OXUSERS_name`),
  CONSTRAINT `FK_brc1txk9tjjkd59ohpgdprlhw` FOREIGN KEY (`roles_name`) REFERENCES `oxroles` (`name`),
  CONSTRAINT `FK_m8dvdguj1ip1mfm29xwqj9coi` FOREIGN KEY (`OXUSERS_name`) REFERENCES `oxusers` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oxusers_oxroles`
--

LOCK TABLES `oxusers_oxroles` WRITE;
/*!40000 ALTER TABLE `oxusers_oxroles` DISABLE KEYS */;
INSERT INTO `oxusers_oxroles` VALUES ('admin','admin'),('admin','user'),('efd','efd_user'),('efd','user');
/*!40000 ALTER TABLE `oxusers_oxroles` ENABLE KEYS */;
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
