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
-- Table structure for table `wealthgroup_wgcharacteristicsresource`
--

DROP TABLE IF EXISTS `wealthgroup_wgcharacteristicsresource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroup_wgcharacteristicsresource` (
  `WealthGroup_WealthGroupID` varchar(32) NOT NULL,
  `wgcharacteristicsresource_IDWGResource` varchar(32) NOT NULL,
  UNIQUE KEY `UK_am2ubiqk9wn9g7g6ei9gydysw` (`wgcharacteristicsresource_IDWGResource`),
  KEY `FK_8sdlgu2pywd6ayc5iu1pq7241` (`WealthGroup_WealthGroupID`),
  CONSTRAINT `FK_8sdlgu2pywd6ayc5iu1pq7241` FOREIGN KEY (`WealthGroup_WealthGroupID`) REFERENCES `wealthgroup` (`WealthGroupID`),
  CONSTRAINT `FK_am2ubiqk9wn9g7g6ei9gydysw` FOREIGN KEY (`wgcharacteristicsresource_IDWGResource`) REFERENCES `wgcharacteristicsresource` (`IDWGResource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroup_wgcharacteristicsresource`
--

LOCK TABLES `wealthgroup_wgcharacteristicsresource` WRITE;
/*!40000 ALTER TABLE `wealthgroup_wgcharacteristicsresource` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroup_wgcharacteristicsresource` ENABLE KEYS */;
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
