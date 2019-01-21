-- MySQL dump 10.13  Distrib 5.7.23, for Linux (x86_64)
--
-- Host: localhost    Database: efd
-- ------------------------------------------------------
-- Server version	5.7.23-0ubuntu0.16.04.1

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
-- Table structure for table `community`
--

DROP TABLE IF EXISTS `community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community` (
  `CID` varchar(32) NOT NULL,
  `CInterviewDate` datetime DEFAULT NULL,
  `CInterviewSequence` int(11) DEFAULT NULL,
  `CIVF` int(11) DEFAULT NULL,
  `CIVM` int(11) DEFAULT NULL,
  `CIVparticipants` int(11) DEFAULT NULL,
  `Interviewers` varchar(255) DEFAULT NULL,
  `Notes` varchar(45) DEFAULT NULL,
  `CProject` varchar(32) NOT NULL,
  `CLocation` varchar(32) NOT NULL,
  PRIMARY KEY (`CID`),
  KEY `FK_f9qw7cegyv78nid6ltj9xa0ld` (`CProject`),
  KEY `FK_dadkdhpgglfex4r4rt523ri8o` (`CLocation`),
  CONSTRAINT `FK_dadkdhpgglfex4r4rt523ri8o` FOREIGN KEY (`CLocation`) REFERENCES `site` (`LocationID`),
  CONSTRAINT `FK_f9qw7cegyv78nid6ltj9xa0ld` FOREIGN KEY (`CProject`) REFERENCES `project` (`ProjectID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community`
--

LOCK TABLES `community` WRITE;
/*!40000 ALTER TABLE `community` DISABLE KEYS */;
/*!40000 ALTER TABLE `community` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `communityyearnotes`
--

DROP TABLE IF EXISTS `communityyearnotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `communityyearnotes` (
  `IDCommunityYearNotes` varchar(32) NOT NULL,
  `Notes` longtext NOT NULL,
  `Year` int(4) NOT NULL,
  `CommunityID` varchar(32) NOT NULL,
  PRIMARY KEY (`IDCommunityYearNotes`),
  KEY `FK_d5weodps7e24tx3hu702p5t0y` (`CommunityID`),
  CONSTRAINT `FK_d5weodps7e24tx3hu702p5t0y` FOREIGN KEY (`CommunityID`) REFERENCES `community` (`CID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `communityyearnotes`
--

LOCK TABLES `communityyearnotes` WRITE;
/*!40000 ALTER TABLE `communityyearnotes` DISABLE KEYS */;
/*!40000 ALTER TABLE `communityyearnotes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `IDCountry` varchar(32) NOT NULL,
  `Currency` varchar(3) NOT NULL,
  `CurrencySymbol` varchar(1) DEFAULT NULL,
  `CountryName` varchar(45) NOT NULL,
  `ISOCountryCode` varchar(3) NOT NULL,
  PRIMARY KEY (`IDCountry`),
  UNIQUE KEY `UK_bvsla6ehv2ududek6w1j2fhh4` (`CountryName`),
  UNIQUE KEY `UK_1rhinx110uame916g2hcfuopt` (`ISOCountryCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES ('4028808860bbdf630160bbe5f21a0002','MWK',NULL,'Malawi','MAL'),('402880895ff401b9015ff403e4150002','GBP','£','United Kingdom','UK'),('8a80848460c2ee580160c73af80f000b','USH','','Uganda','UGA'),('8a80848460d98aad0160dbc35b50000d','KSH','K','Kenya','KEA'),('8a80848461a48f910161a543f02c0005','CFA','','Burkina Faso','BFA'),('8a80848461fbb5780162062bad4c005f','EU','€','EuroZone','EUZ');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `images` (
  `ID` varchar(255) NOT NULL,
  `GALLERY` varchar(255) DEFAULT NULL,
  `IMAGE` longblob,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `livelihoodzone`
--

DROP TABLE IF EXISTS `livelihoodzone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `livelihoodzone` (
  `LZID` varchar(32) NOT NULL,
  `LZName` varchar(255) DEFAULT NULL,
  `LZZoneMap` longblob,
  `LZCountry` varchar(32) NOT NULL,
  PRIMARY KEY (`LZID`),
  UNIQUE KEY `UK_dkucpdt3bfx14coxuujn553dn` (`LZName`,`LZCountry`),
  KEY `FK_t87hbra2eipecikn7ghjqgx23` (`LZCountry`),
  CONSTRAINT `FK_t87hbra2eipecikn7ghjqgx23` FOREIGN KEY (`LZCountry`) REFERENCES `country` (`IDCountry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `livelihoodzone`
--

LOCK TABLES `livelihoodzone` WRITE;
/*!40000 ALTER TABLE `livelihoodzone` DISABLE KEYS */;
/*!40000 ALTER TABLE `livelihoodzone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oxfiles`
--

DROP TABLE IF EXISTS `oxfiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oxfiles` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `data` longblob,
  `LIBRARYID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `UK_o7k20w7792nm0o0x4hasi1yk5` (`LIBRARYID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oxfiles`
--

LOCK TABLES `oxfiles` WRITE;
/*!40000 ALTER TABLE `oxfiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `oxfiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `ProjectID` varchar(32) NOT NULL,
  `altExchangeRate` decimal(10,5) DEFAULT NULL,
  `Notes` varchar(32) DEFAULT NULL,
  `PDate` datetime DEFAULT NULL,
  `ProjectTitle` varchar(255) DEFAULT NULL,
  `altcurrency_IDCountry` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ProjectID`),
  UNIQUE KEY `UK_jqk4h2lmyetg49p33dnr7abcw` (`ProjectTitle`),
  UNIQUE KEY `UK_ndbbqvhic4oin4y8dpy9mrxpm` (`ProjectTitle`,`PDate`),
  KEY `FK_country` (`altcurrency_IDCountry`),
  CONSTRAINT `FK_country` FOREIGN KEY (`altcurrency_IDCountry`) REFERENCES `country` (`IDCountry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES ('4028808860bbdf630160bbe57d4f0001',NULL,'','2016-07-25 00:00:00','Change Project Name',NULL);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `project_details`
--

DROP TABLE IF EXISTS `project_details`;
/*!50001 DROP VIEW IF EXISTS `project_details`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `project_details` AS SELECT 
 1 AS `ProjectID`,
 1 AS `Project`,
 1 AS `ProjectDate`,
 1 AS `ExchangeRate`,
 1 AS `LZName`,
 1 AS `Country`,
 1 AS `District`,
 1 AS `SubDistrict`,
 1 AS `CInterviewDate`,
 1 AS `CIVF`,
 1 AS `CIVM`,
 1 AS `CIVParticipants`,
 1 AS `Interviewers`,
 1 AS `WGNameEng`,
 1 AS `WGHHSize`,
 1 AS `WGNameLoc`,
 1 AS `WGOrder`,
 1 AS `WGPercent`,
 1 AS `WGWives`,
 1 AS `CommunityID`,
 1 AS `WGID`,
 1 AS `WGIIID`,
 1 AS `WGInterviewDate`,
 1 AS `WGIntervieweesCount`,
 1 AS `WGInterviewers`,
 1 AS `WGInterviewNumber`,
 1 AS `WGISpreadsheet`,
 1 AS `WGIStatus`,
 1 AS `WGMaleIVees`,
 1 AS `WGFemaleIVees`,
 1 AS `WGYearType`,
 1 AS `ResourceType`,
 1 AS `ResourceSubTypeName`,
 1 AS `ResourceUnit`,
 1 AS `ResourceKCal`,
 1 AS `unit`,
 1 AS `status`,
 1 AS `cashamount`,
 1 AS `cashcurrency`,
 1 AS `TypeEntered`,
 1 AS `Quantity`,
 1 AS `PricePerUnit`,
 1 AS `Market1`,
 1 AS `PercentTradeMarket1`,
 1 AS `Market2`,
 1 AS `PercentTradeMarket2`,
 1 AS `Market3`,
 1 AS `PercentTradeMarket3`,
 1 AS `UnitsConsumed`,
 1 AS `UnitsOtherUse`,
 1 AS `UnitsProduced`,
 1 AS `UnitsSold`,
 1 AS `FoodPaymentFoodType`,
 1 AS `FoodPaymentUnit`,
 1 AS `FoodPaymentUnitsPaidWork`,
 1 AS `PeopleCount`,
 1 AS `WorkLocation1`,
 1 AS `PercentWorkLocation1`,
 1 AS `WorkLocation2`,
 1 AS `PercentWorkLocation2`,
 1 AS `WorkLocation3`,
 1 AS `PercentWorkLocation3`,
 1 AS `LivestockProduct`,
 1 AS `Official`,
 1 AS `PeopleReceiving`,
 1 AS `Source`,
 1 AS `TimesReceived`,
 1 AS `TransferFoodOtherType`,
 1 AS `unitestransferred`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `projectlz`
--

DROP TABLE IF EXISTS `projectlz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectlz` (
  `Project` varchar(32) NOT NULL,
  `LZ` varchar(32) NOT NULL,
  UNIQUE KEY `UK_projlz` (`Project`,`LZ`),
  UNIQUE KEY `UK_8klrcj7lb4abh5etbxb6853x3` (`Project`,`LZ`),
  KEY `FK_b59ds4g1k10uham8m0dqqtv1r` (`LZ`),
  KEY `FK_k1dqilub21ny98pbqh5dypl3m` (`Project`),
  CONSTRAINT `FK_b59ds4g1k10uham8m0dqqtv1r` FOREIGN KEY (`LZ`) REFERENCES `livelihoodzone` (`LZID`),
  CONSTRAINT `FK_k1dqilub21ny98pbqh5dypl3m` FOREIGN KEY (`Project`) REFERENCES `project` (`ProjectID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectlz`
--

LOCK TABLES `projectlz` WRITE;
/*!40000 ALTER TABLE `projectlz` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectlz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `referencecode`
--

DROP TABLE IF EXISTS `referencecode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `referencecode` (
  `id` varchar(32) NOT NULL,
  `referenceName` varchar(255) NOT NULL,
  `referenceType` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `referencecode`
--

LOCK TABLES `referencecode` WRITE;
/*!40000 ALTER TABLE `referencecode` DISABLE KEYS */;
INSERT INTO `referencecode` VALUES ('8a80848463fa751f0163fa7f4710000d','Hectare','Area'),('8a80848463fa80590163fa81868b0000','Acre','Area'),('8a80848463fa80590163fa81b3120001','Kg','Unit'),('8a80848463fa80590163fa81da310002','Item','Unit'),('8a80848463fa80590163fa8209d10003','Sack','Unit'),('8a80848463fa80590163fa82418a0004','Official','Transfer Style'),('8a80848463fa80590163fa826dbf0005','Unofficial','Transfer Style');
/*!40000 ALTER TABLE `referencecode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `resources`
--

DROP TABLE IF EXISTS `resources`;
/*!50001 DROP VIEW IF EXISTS `resources`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `resources` AS SELECT 
 1 AS `rtype`,
 1 AS `rsubtype`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `resourcesubtype`
--

DROP TABLE IF EXISTS `resourcesubtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcesubtype` (
  `IDResourceSubType` varchar(32) NOT NULL,
  `ResourceSubTypeKCal` int(11) DEFAULT NULL,
  `ResourceSubTypeUnit` varchar(20) DEFAULT NULL,
  `ResourceTypeName` varchar(255) DEFAULT NULL,
  `resourcesubtypesynonym_IDResourceSubType` varchar(32) DEFAULT NULL,
  `ReourceType` varchar(32) NOT NULL,
  PRIMARY KEY (`IDResourceSubType`),
  UNIQUE KEY `UK_g8gw06cehsmw8lnwheufwqcf5` (`ReourceType`,`ResourceTypeName`),
  UNIQUE KEY `UK_4gyixekr36ftipta9p2ueumix` (`ReourceType`,`ResourceTypeName`),
  KEY `FK_5tu8jlniaidwghtms0i5clwty` (`resourcesubtypesynonym_IDResourceSubType`),
  CONSTRAINT `FK_5tu8jlniaidwghtms0i5clwty` FOREIGN KEY (`resourcesubtypesynonym_IDResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`),
  CONSTRAINT `FK_k1s0qoc90ov7pdbt25u1lurou` FOREIGN KEY (`ReourceType`) REFERENCES `resourcetype` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcesubtype`
--

LOCK TABLES `resourcesubtype` WRITE;
/*!40000 ALTER TABLE `resourcesubtype` DISABLE KEYS */;
INSERT INTO `resourcesubtype` VALUES ('4028808860bbecfe0160bc5c9e920006',0,'Acres','Upland',NULL,'4028808860bbecfe0160bc5c9e830005'),('4028808860bbecfe0160bc5d06810007',0,'Acres','Lowland',NULL,'4028808860bbecfe0160bc5c9e830005'),('4028808860bbecfe0160bc5dae330009',0,'Item','Local Cattle',NULL,'4028808860bbecfe0160bc5dae230008'),('4028808860bbecfe0160bc5df41e000a',0,'Item','Improved Cattle (Dairy)','8a80848461652eb6016166852ae60055','4028808860bbecfe0160bc5dae230008'),('4028808860bbecfe0160bc5e22af000b',0,'Item','Goats',NULL,'4028808860bbecfe0160bc5dae230008'),('4028808860bbecfe0160bc5e5995000c',0,'Item ','Local Chickens',NULL,'4028808860bbecfe0160bc5dae230008'),('4028808860bbecfe0160bc5e81ab000d',0,'Item','Pigs ',NULL,'4028808860bbecfe0160bc5dae230008'),('4028808860bbecfe0160bc5f519f000f',0,'Item','Hoes',NULL,'8a80848461f72ca50161f7472f040007'),('4028808860bbecfe0160bc5f79a70010',0,'Item','Panga',NULL,'8a80848461f72ca50161f7472f040007'),('4028808860bbecfe0160bc5fae380011',0,'Item','Brewing Utensil','8a80848463fa751f0163fa79e2410004','8a80848461f72ca50161f7472f040007'),('4028808860bbecfe0160bc5fd1060012',0,'Items','Slasher',NULL,'8a80848461f72ca50161f7472f040007'),('4028808860bbecfe0160bc6033d20014',0,'Item','Motorcycle',NULL,'8a80848461f72ca50161f7472f040007'),('4028808860bc887e0160bc8c911c0004',0,'day','Brewing',NULL,'4028808860bc887e0160bc8c910d0003'),('4028808860bc887e0160bc8cb2630005',0,'day','Digging',NULL,'4028808860bc887e0160bc8c910d0003'),('4028808860bc887e0160bc8cde260006',0,'day','Brick Making',NULL,'4028808860bc887e0160bc8c910d0003'),('4028808860bc887e0160bc8cf9950007',0,'day','Petty Trade',NULL,'4028808860bc887e0160bc8c910d0003'),('4028808860bc887e0160bc8d230e0008',0,'Item','Transportation',NULL,'4028808860bc887e0160bc8c910d0003'),('4028808860bc887e0160bc8d42140009',0,'Item','Salaried Jobs',NULL,'4028808860bc887e0160bc8c910d0003'),('4028808860bc887e0160bc8d5b37000a',0,'Item','Trading',NULL,'4028808860bc887e0160bc8c910d0003'),('4028808860bc887e0160bc8d9d8e000c',0,'Item','Rental Houses',NULL,'4028808860bc887e0160bc8c910d0003'),('8a808484615621fa01615be06a0f0030',2000,'item','Ducks',NULL,'4028808860bbecfe0160bc5dae230008'),('8a808484615621fa01615be50aea0038',0,'day','Fishing',NULL,'4028808860bc887e0160bc8c910d0003'),('8a808484615c078f01615c6279fb0006',2000,'Kg','Banana',NULL,'8a80848461f72ca50161f73936fa0003'),('8a808484615c078f01615c77177a000a',0,'Kg','Coffee',NULL,'8a80848461f72ca50161f73936fa0003'),('8a808484615c078f01615c7918e2000e',2000,'Kg','Sweet Potatoes','8a808484615c078f01615c7abbbc0012','8a80848461f72ca50161f73936fa0003'),('8a808484615c078f01615c798486000f',2000,'Kg','Maize',NULL,'8a80848461f72ca50161f73936fa0003'),('8a808484615c078f01615c79f1970010',2000,'Kg','Beans',NULL,'8a80848461f72ca50161f73936fa0003'),('8a808484615c078f01615c7a544c0011',2000,'Kg','Cassava',NULL,'8a80848461f72ca50161f73936fa0003'),('8a808484615c078f01615c7abbbc0012',2000,'Kg','Sweet Potato',NULL,'8a80848461f72ca50161f73936fa0003'),('8a808484615c078f01615c7b2b690013',2000,'Kg','Ground Nuts',NULL,'8a80848461f72ca50161f73936fa0003'),('8a808484615c078f01615c7bf0a60014',0,'day','Boda Boda',NULL,'4028808860bc887e0160bc8c910d0003'),('8a808484615c078f01615c7c93f10016',0,'month','Government Worker',NULL,'4028808860bc887e0160bc8c910d0003'),('8a80848461652eb60161658e5cfa0027',0,'Item','Local Cow','4028808860bbecfe0160bc5dae330009','4028808860bbecfe0160bc5dae230008'),('8a80848461652eb6016165b4a55e003c',0,'Item','Shop Keeper',NULL,'4028808860bc887e0160bc8c910d0003'),('8a80848461652eb6016166852ae60055',0,'Item','Dairy Cow',NULL,'4028808860bbecfe0160bc5dae230008'),('8a80848461652eb601616686d2db0057',0,'Item','Chicken','4028808860bbecfe0160bc5e5995000c','4028808860bbecfe0160bc5dae230008'),('8a80848461f587740161f58f3f000003',75,'unit','Chicken Eggs',NULL,'8a80848461f587740161f58ed6d10002'),('8a80848461f587740161f58f936c0004',2000,'litre','Goat milk',NULL,'8a80848461f587740161f58ed6d10002'),('8a8084846225b5540162299444530008',3666,'unit','Maize - Fresh',NULL,'8a80848461f72ca50161f73936fa0003'),('8a808484628bce590162a5039c1a0005',0,'1','Goat','4028808860bbecfe0160bc5e22af000b','4028808860bbecfe0160bc5dae230008'),('8a808484628bce590162a5043caf0006',0,'1','Cattle','4028808860bbecfe0160bc5dae330009','4028808860bbecfe0160bc5dae230008'),('8a808484628bce590162a507eb190008',0,'day','Planting',NULL,'4028808860bc887e0160bc8c910d0003'),('8a808484628bce590162a50b4544000a',2000,'KG','Matoke','8a808484615c078f01615c6279fb0006','8a80848461f72ca50161f73936fa0003'),('8a8084846373a2c40163742c46710003',435,'kg','Meat',NULL,'8a80848461f72ca50161f74639c30005'),('8a80848463fa751f0163fa78ad180001',0,'Item','Impala',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848463fa751f0163fa78cbd90002',0,'Item','Zebra',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848463fa751f0163fa79b7140003',0,'Item','Bicycle',NULL,'8a80848461f72ca50161f7472f040007'),('8a80848463fa751f0163fa79e2410004',0,'Item','Brewing Equipment',NULL,'8a80848461f72ca50161f7472f040007'),('8a80848463fa751f0163fa7adc900005',0,'Item','Food',NULL,'8a80848461652eb6016166e505ed005d'),('8a80848463fa751f0163fa7c573c0006',0,'Item','Axe',NULL,'8a80848461f72ca50161f7472f040007'),('8a80848463fa751f0163fa7d322c0008',0,'Item','Oak',NULL,'8a80848461f72ca50161f745eb7d0004'),('8a80848463fa751f0163fa7d67340009',0,'Item','Ash',NULL,'8a80848461f72ca50161f745eb7d0004'),('8a80848463fa751f0163fa7d8be2000a',0,'Item','Sycamore',NULL,'8a80848461f72ca50161f745eb7d0004'),('8a80848463fa80590163fa903cdf0009',1500,'Kg','Tomatoes',NULL,'8a80848461f72ca50161f73936fa0003'),('8a80848463fa80590163fa9061eb000a',2000,'Kg','Rice',NULL,'8a80848461f72ca50161f73936fa0003'),('8a80848464c6e6f80164c817ae16001e',0,'Day','Weeding',NULL,'4028808860bc887e0160bc8c910d0003'),('8a80848464c6e6f80164c818716f001f',0,'day','Harvesting',NULL,'4028808860bc887e0160bc8c910d0003'),('8a80848464c6e6f80164c819e3660020',2000,'Kg','Jack Fruits',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848464c6e6f80164c81a0fd90021',1500,'Kg','Dodo',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848464c6e6f80164c81b35bd0022',1200,'Kg','Egg Plant',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848464c6e6f80164c81cd5820023',1500,'Kg','Silver Fish',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848464c6e6f80164c81e99950025',3000,'Litre','Cooking Oil',NULL,'8a80848461652eb6016166e9d0bd005f'),('8a80848464c6e6f80164c81f8ff60026',0,'Kg','Tea',NULL,'8a80848461652eb6016166e9d0bd005f'),('8a80848464c6e6f80164c81fed7c0027',0,'Kg','Soap',NULL,'8a80848461652eb6016166ea01440060'),('8a80848464c6e6f80164c82034b00028',0,'Litre','Paraffin',NULL,'8a80848461652eb6016166ea01440060'),('8a80848464c6e6f80164c820f54a0029',0,'Gm','Drugs',NULL,'8a80848461652eb6016166ea01440060'),('8a80848464c6e6f80164c8214247002a',0,'Unit','Telephone Credit',NULL,'8a80848461652eb6016166ea01440060'),('8a80848464c6e6f80164c8218c99002b',0,'Litre','Beer',NULL,'8a80848461652eb6016166ea01440060'),('8a80848464c6e6f80164c8228fde002c',0,'Unit','Clothing',NULL,'8a80848461652eb6016166ea01440060'),('8a80848464cbc5140164cc552b290008',0,'unit','GBP',NULL,'8a80848461f72ca50161f746af910006'),('8a80848464dac8b80164db3ac07e0003',0,'item','Piglets',NULL,'4028808860bbecfe0160bc5dae230008'),('8a80848464dc39a00164dc5dd4050003',2000,'Litre','Cows Milk',NULL,'8a80848461f587740161f58ed6d10002'),('8a80848464dc39a00164dc5f47140004',2000,'Kg','Mangoes',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848464dc39a00164dc5f8b9f0005',1500,'Kg','Paw Paw',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848464dc39a00164dc6023ee0006',0,'1000','Amaranthus',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848464dc39a00164dc6097f10007',2500,'Kg','Avocado',NULL,'8a80848461652eb6016166e6b5be005e'),('8a80848464dc39a00164dc62965a0008',1500,'Kg','Meat',NULL,'8a80848461652eb6016166e9d0bd005f'),('8a80848464dc39a00164dc62a1f10009',1500,'Kg','Goat meat','8a80848464dc39a00164dc62965a0008','8a80848461f587740161f58ed6d10002'),('8a80848464dc39a00164dc633bb9000a',500,'Kg','Onions',NULL,'8a80848461652eb6016166e9d0bd005f'),('8a80848464dc39a00164dc638ada000b',3000,'Kg','Sugar',NULL,'8a80848461652eb6016166e9d0bd005f'),('8a80848464dc39a00164dc6d50fe000d',0,'day','Brick Laying',NULL,'4028808860bc887e0160bc8c910d0003'),('8a80848464eaaacf0164eaca8f7a0004',0,'each','Chicken',NULL,'8a80848463fa80590163fa84e1380006'),('8a80848464eb7d5b0164efd24ffe002e',0,'Kg','Matooke','8a808484628bce590162a50b4544000a','8a80848461f72ca50161f73936fa0003'),('8a8084846513d34e016520938d950022',0,'item','Sheep',NULL,'4028808860bbecfe0160bc5dae230008'),('8a8084846513d34e01652095c71c0024',0,'item','Hand hoe','4028808860bbecfe0160bc5f519f000f','8a80848461f72ca50161f7472f040007'),('8a8084846513d34e0165209644bb0025',0,'item','Hand axe','8a80848463fa751f0163fa7c573c0006','8a80848461f72ca50161f7472f040007'),('8a8084846513d34e01652098f8f50026',0,'item','Wheel barrow',NULL,'8a80848461f72ca50161f7472f040007'),('8a8084846513d34e0165209971570027',0,'item','Ox plough',NULL,'8a80848461f72ca50161f7472f040007'),('8a8084846513d34e01652099feab0028',0,'item','Ox-Cart',NULL,'8a80848461f72ca50161f7472f040007'),('8a8084846513d34e0165209cfe200029',0,'item','Ox',NULL,'4028808860bbecfe0160bc5dae230008'),('8a8084846513d34e0165209e6aca002a',0,'item','Motor vehicle',NULL,'8a80848461f72ca50161f7472f040007'),('8a8084846513d34e0165209edbeb002b',0,'item','Sugar cane crasher',NULL,'8a80848461f72ca50161f7472f040007'),('8a8084846513d34e0165209f4259002c',0,'item','Grinding mill',NULL,'8a80848461f72ca50161f7472f040007'),('8a8084846513d34e0165209fce18002d',0,'item','Tractor',NULL,'8a80848461f72ca50161f7472f040007'),('8a8084846513d34e016520a02844002e',0,'item','Water pump',NULL,'8a80848461f72ca50161f7472f040007'),('8a8084846513d34e016520a0a8cf002f',0,'item','Brick making box',NULL,'8a80848461f72ca50161f7472f040007'),('8a8084846513d34e016520a4f1ed0038',0,'Kg','Tobacco',NULL,'8a80848461f72ca50161f73936fa0003'),('8a8084846513d34e016520a5810c0039',1500,'Kg','Kale',NULL,'8a80848461f72ca50161f73936fa0003'),('8a8084846513d34e016520a72f53003a',0,'day','Agricultural Labour',NULL,'4028808860bc887e0160bc8c910d0003'),('8a8084846513d34e016520a7ace6003b',0,'day','Construction labour',NULL,'4028808860bc887e0160bc8c910d0003'),('8a8084846513d34e016520a83d40003c',0,'day','Motorcycle riding',NULL,'4028808860bc887e0160bc8c910d0003'),('8a8084846513d34e016520a8b3c9003d',0,'day','Crushing sugar cane',NULL,'4028808860bc887e0160bc8c910d0003'),('8a8084846513d34e016520a98546003e',0,'month','Teaching',NULL,'4028808860bc887e0160bc8c910d0003'),('8a8084846513d34e016520a9f906003f',0,'day','Construction work','8a8084846513d34e016520a7ace6003b','4028808860bc887e0160bc8c910d0003'),('8a8084846513d34e016520b052450044',2000,'Kg','Sugar cane',NULL,'8a80848461f72ca50161f73936fa0003'),('8a808484652373da01652399ec96000a',0,'unit','Cash',NULL,'8a80848461652eb6016166e505ed005d'),('8a808484652373da0165239a3b5d000b',0,'unit','Pension',NULL,'8a80848461652eb6016166e505ed005d'),('8a808484652373da0165239aa5cf000c',0,'Kg','Fertilizer',NULL,'8a80848461652eb6016166e505ed005d'),('8a8084846548a6e8016556ae092b001c',1500,'Kg','Guava',NULL,'8a80848461652eb6016166e6b5be005e'),('8a8084846548a6e8016556aef1d5001d',500,'Kg','Green Leaves',NULL,'8a80848461652eb6016166e6b5be005e'),('8a8084846548a6e8016556b064c3001e',1500,'Kg','Cow Peas',NULL,'8a80848461f72ca50161f73936fa0003'),('8a8084846548a6e8016557e4a5ea0034',0,'Kg','Mukene','8a80848464c6e6f80164c81cd5820023','8a80848461652eb6016166e6b5be005e'),('8a8084846558de0301655e6613d40084',3000,'unit','Chicken - Local',NULL,'4028808860bbecfe0160bc5dae230008'),('8a8084846558de0301655e6921b60085',0,'litre','Water',NULL,'8a80848461652eb6016166e9d0bd005f'),('8a8084846558de0301655e6962130086',0,'Kg','Salt',NULL,'8a80848461652eb6016166e9d0bd005f'),('8a8084846558de0301655e6a03940087',2500,'Kg','Potatoes',NULL,'8a80848461652eb6016166e9d0bd005f'),('8a8084846558de0301655e6a79330088',2500,'Kg','Irish Potatoes','8a8084846558de0301655e6a03940087','8a80848461652eb6016166e9d0bd005f'),('8a8084846558de0301655e6bc8040089',1500,'Kg','Tilapia',NULL,'8a80848461652eb6016166e6b5be005e'),('8a8084846558de0301655e83aca5008c',100,'Kg','Ginger',NULL,'8a80848461f72ca50161f73936fa0003'),('8a8084846558de0301655e841564008d',500,'Kg','Tangawizi','8a8084846558de0301655e83aca5008c','8a80848461f72ca50161f73936fa0003'),('8a8084846558de0301655e8e5873008e',1200,'Kg','Yams',NULL,'8a80848461f72ca50161f73936fa0003'),('8a8084846558de030165619fa3d8009e',0,'day','Domestic Work',NULL,'4028808860bc887e0160bc8c910d0003'),('8a8084846558de03016561a021c0009f',0,'day','Maid','8a8084846558de030165619fa3d8009e','4028808860bc887e0160bc8c910d0003'),('8a80848465753ce3016576041f4e0008',1000,'Kg','Green Vegetables ',NULL,'8a80848461f72ca50161f73936fa0003'),('8a80848465753ce30165760c551f000a',250,'Kg','Cocoa',NULL,'8a80848461f72ca50161f73936fa0003'),('8a80848465753ce30165760f7a60000c',0,'unit','Rental House',NULL,'8a80848461f72ca50161f7472f040007'),('8a80848465753ce30165760fcb1e000d',0,'unit','Spray Pump',NULL,'8a80848461f72ca50161f7472f040007'),('8a80848465753ce3016576141aac000e',350,'kg','Coconut',NULL,'8a80848461f72ca50161f73936fa0003'),('8a80848465753ce3016576155a02000f',0,'unit','House Rental',NULL,'4028808860bc887e0160bc8c910d0003'),('8a80848465753ce30165761748d00010',3500,'kg','Macaroni',NULL,'8a80848461652eb6016166e9d0bd005f'),('8a80848465753ce3016576194e6e0012',1000,'Kg','Nile Perch',NULL,'8a80848461652eb6016166e6b5be005e');
/*!40000 ALTER TABLE `resourcesubtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourcetype`
--

DROP TABLE IF EXISTS `resourcetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcetype` (
  `IDResourceType` varchar(32) NOT NULL,
  `ResourceTypeName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcetype`
--

LOCK TABLES `resourcetype` WRITE;
/*!40000 ALTER TABLE `resourcetype` DISABLE KEYS */;
INSERT INTO `resourcetype` VALUES ('4028808860bbecfe0160bc5c9e830005','Land'),('4028808860bbecfe0160bc5dae230008','Livestock'),('4028808860bc887e0160bc8c910d0003','Employment'),('8a80848461652eb6016166e505ed005d','Transfers'),('8a80848461652eb6016166e6b5be005e','Wild Foods'),('8a80848461652eb6016166e9d0bd005f','Food Purchase'),('8a80848461652eb6016166ea01440060','Non Food Purchase'),('8a80848461f587740161f58ed6d10002','Livestock Products'),('8a80848461f72ca50161f73936fa0003','Crops'),('8a80848461f72ca50161f745eb7d0004','Trees'),('8a80848461f72ca50161f74639c30005','Food Stocks'),('8a80848461f72ca50161f746af910006','Cash'),('8a80848461f72ca50161f7472f040007','Other Tradeable Goods'),('8a80848463fa80590163fa84e1380006','Livestock Sales');
/*!40000 ALTER TABLE `resourcetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site`
--

DROP TABLE IF EXISTS `site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site` (
  `LocationID` varchar(32) NOT NULL,
  `GPSLocation` varchar(25) DEFAULT NULL,
  `LocationDistrict` varchar(25) DEFAULT NULL,
  `SubDistrict` varchar(25) DEFAULT NULL,
  `LZ` varchar(32) NOT NULL,
  PRIMARY KEY (`LocationID`),
  UNIQUE KEY `uk_lz_district_sub` (`LZ`,`LocationDistrict`,`SubDistrict`) USING BTREE,
  KEY `FK_krxguwiy58opb3227k6jy7jay` (`LZ`),
  CONSTRAINT `FK_krxguwiy58opb3227k6jy7jay` FOREIGN KEY (`LZ`) REFERENCES `livelihoodzone` (`LZID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site`
--

LOCK TABLES `site` WRITE;
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
/*!40000 ALTER TABLE `site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroup`
--

DROP TABLE IF EXISTS `wealthgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroup` (
  `WealthGroupID` varchar(32) NOT NULL,
  `WGHHSize` decimal(19,2) DEFAULT NULL,
  `WGName_Eng` varchar(255) DEFAULT NULL,
  `WGName_Local` varchar(255) DEFAULT NULL,
  `WGOrder` int(11) DEFAULT NULL,
  `WGPercent` int(11) DEFAULT NULL,
  `WGWives` decimal(19,2) DEFAULT NULL,
  `CommunityID` varchar(32) NOT NULL,
  PRIMARY KEY (`WealthGroupID`),
  KEY `FK_nv0rv8odje44q8vc2464g05wk` (`CommunityID`),
  CONSTRAINT `FK_nv0rv8odje44q8vc2464g05wk` FOREIGN KEY (`CommunityID`) REFERENCES `community` (`CID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroup`
--

LOCK TABLES `wealthgroup` WRITE;
/*!40000 ALTER TABLE `wealthgroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview`
--

DROP TABLE IF EXISTS `wealthgroupinterview`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview` (
  `WGIID` varchar(32) NOT NULL,
  `Notes` varchar(32) DEFAULT NULL,
  `WGISpreadsheet` varchar(32) DEFAULT NULL,
  `WGIStatus` int(11) DEFAULT NULL,
  `WGAverageNumberInHH` int(11) DEFAULT NULL,
  `WGFemaleIVees` int(11) DEFAULT NULL,
  `WGInterviewDate` datetime DEFAULT NULL,
  `WGInterviewNumber` int(11) NOT NULL,
  `WGIntervieweesCount` int(11) NOT NULL,
  `WGInterviewers` varchar(255) NOT NULL,
  `WGMaleIVees` int(11) DEFAULT NULL,
  `WGYearType` varchar(255) DEFAULT NULL,
  `WGID` varchar(32) NOT NULL,
  PRIMARY KEY (`WGIID`),
  KEY `FK_jh8uxqbt3bi76knbc0gthr9sb` (`WGID`),
  CONSTRAINT `FK_jh8uxqbt3bi76knbc0gthr9sb` FOREIGN KEY (`WGID`) REFERENCES `wealthgroup` (`WealthGroupID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview`
--

LOCK TABLES `wealthgroupinterview` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_assetcash`
--

DROP TABLE IF EXISTS `wealthgroupinterview_assetcash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_assetcash` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `Amount` double DEFAULT NULL,
  `Currency` varchar(3) NOT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) DEFAULT NULL,
  KEY `FK_2nr8ydsi4mnuw0a0ykwf0ruof` (`ResourceSubType`),
  KEY `FK_thux4cm1oqoi97xb87h7nx7pr` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_2nr8ydsi4mnuw0a0ykwf0ruof` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`),
  CONSTRAINT `FK_thux4cm1oqoi97xb87h7nx7pr` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_assetcash`
--

LOCK TABLES `wealthgroupinterview_assetcash` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_assetcash` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_assetcash` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_assetfoodstock`
--

DROP TABLE IF EXISTS `wealthgroupinterview_assetfoodstock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_assetfoodstock` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `FoodTypeEnteredName` varchar(50) DEFAULT NULL,
  `Quantity` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_hnq32h0i13y5is5a8axl1fs4r` (`ResourceSubType`),
  KEY `FK_8u6y59hnvb5bi5g3y1cp6dh7x` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_8u6y59hnvb5bi5g3y1cp6dh7x` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`),
  CONSTRAINT `FK_hnq32h0i13y5is5a8axl1fs4r` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_assetfoodstock`
--

LOCK TABLES `wealthgroupinterview_assetfoodstock` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_assetfoodstock` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_assetfoodstock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_assetland`
--

DROP TABLE IF EXISTS `wealthgroupinterview_assetland`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_assetland` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `LandTypeEnteredName` varchar(50) DEFAULT NULL,
  `NumberofUnits` double NOT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_q74xk3tvw0couwq06m5phhs7j` (`ResourceSubType`),
  KEY `FK_8au1des2s77ncnoj0uxmi259i` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_8au1des2s77ncnoj0uxmi259i` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`),
  CONSTRAINT `FK_q74xk3tvw0couwq06m5phhs7j` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_assetland`
--

LOCK TABLES `wealthgroupinterview_assetland` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_assetland` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_assetland` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_assetlivestock`
--

DROP TABLE IF EXISTS `wealthgroupinterview_assetlivestock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_assetlivestock` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `LiveStockTypeEnteredName` varchar(50) NOT NULL,
  `NumberOwnedAtStart` double NOT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_mjloor1kfbhr5gwbc8il8cnkn` (`ResourceSubType`),
  KEY `FK_jpg0rgp412kmfd1p19edn8f0b` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_jpg0rgp412kmfd1p19edn8f0b` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`),
  CONSTRAINT `FK_mjloor1kfbhr5gwbc8il8cnkn` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_assetlivestock`
--

LOCK TABLES `wealthgroupinterview_assetlivestock` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_assetlivestock` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_assetlivestock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_assettradeable`
--

DROP TABLE IF EXISTS `wealthgroupinterview_assettradeable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_assettradeable` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `NumberOwned` double NOT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `TradeableTypeEnteredName` varchar(50) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_d5416skgj0ruc3slrpluhfqd3` (`ResourceSubType`),
  KEY `FK_mlvhkwmmqdx0t3c1wxrk41kj` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_d5416skgj0ruc3slrpluhfqd3` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`),
  CONSTRAINT `FK_mlvhkwmmqdx0t3c1wxrk41kj` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_assettradeable`
--

LOCK TABLES `wealthgroupinterview_assettradeable` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_assettradeable` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_assettradeable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_assettree`
--

DROP TABLE IF EXISTS `wealthgroupinterview_assettree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_assettree` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `NumberOwned` double NOT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `TreeTypeEnteredName` varchar(50) NOT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_lbdt8prhgj1qdnqxmqf6g2agm` (`ResourceSubType`),
  KEY `FK_s7v8lt248hwywi98naat77ood` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_lbdt8prhgj1qdnqxmqf6g2agm` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`),
  CONSTRAINT `FK_s7v8lt248hwywi98naat77ood` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_assettree`
--

LOCK TABLES `wealthgroupinterview_assettree` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_assettree` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_assettree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_crop`
--

DROP TABLE IF EXISTS `wealthgroupinterview_crop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_crop` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `CropType` varchar(50) DEFAULT NULL,
  `Market1` varchar(50) DEFAULT NULL,
  `Market2` varchar(50) DEFAULT NULL,
  `Market3` varchar(50) DEFAULT NULL,
  `PercentTradeMarket1` double DEFAULT NULL,
  `PercentTradeMarket2` double DEFAULT NULL,
  `PercentTradeMarket3` double DEFAULT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `UnitsConsumed` double DEFAULT NULL,
  `UnitsOtherUse` double DEFAULT NULL,
  `UnitsProduced` double NOT NULL,
  `UnitsSold` double DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_9y7kgkrlj88kwh8468l103n2g` (`ResourceSubType`),
  KEY `FK_iqaewx6ugdf4vxyc8h4en15fv` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_9y7kgkrlj88kwh8468l103n2g` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`),
  CONSTRAINT `FK_iqaewx6ugdf4vxyc8h4en15fv` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_crop`
--

LOCK TABLES `wealthgroupinterview_crop` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_crop` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_crop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_employment`
--

DROP TABLE IF EXISTS `wealthgroupinterview_employment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_employment` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `CashPaymentAmount` double DEFAULT NULL,
  `EmploymentName` varchar(50) DEFAULT NULL,
  `FoodPaymentFoodType` varchar(50) DEFAULT NULL,
  `FoodPaymentUnit` varchar(50) DEFAULT NULL,
  `FoodPaymentUnitsPaidWork` varchar(50) DEFAULT NULL,
  `FoodResourceSubType` varchar(32) DEFAULT NULL,
  `PeopleCount` double NOT NULL,
  `PercentWorkLocation1` double DEFAULT NULL,
  `PercentWorkLocation2` double DEFAULT NULL,
  `PercentWorkLocation3` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `UnitsWorked` double DEFAULT NULL,
  `WorkLocation1` varchar(50) DEFAULT NULL,
  `WorkLocation2` varchar(50) DEFAULT NULL,
  `WorkLocation3` varchar(50) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_qqnjxqask5cs75i2j8vqn6tx8` (`ResourceSubType`),
  KEY `FK_2hxfvpov5xbln6n0qlc0hjh3q` (`WealthGroupInterview_WGIID`),
  KEY `fk_wealthgroupinterview_employment_1_idx` (`FoodResourceSubType`),
  CONSTRAINT `FK_2hxfvpov5xbln6n0qlc0hjh3q` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`),
  CONSTRAINT `FK_qqnjxqask5cs75i2j8vqn6tx8` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`),
  CONSTRAINT `fk_wealthgroupinterview_employment_1` FOREIGN KEY (`FoodResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_employment`
--

LOCK TABLES `wealthgroupinterview_employment` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_employment` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_employment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_foodpurchase`
--

DROP TABLE IF EXISTS `wealthgroupinterview_foodpurchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_foodpurchase` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `FoodTypeEnteredName` varchar(50) NOT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `UnitsPurchased` double NOT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_kfh77hd53dij9ll68nt000rf3` (`ResourceSubType`),
  KEY `FK_4w9saroqpjhd988bcg7ho4l7t` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_4w9saroqpjhd988bcg7ho4l7t` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`),
  CONSTRAINT `FK_kfh77hd53dij9ll68nt000rf3` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_foodpurchase`
--

LOCK TABLES `wealthgroupinterview_foodpurchase` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_foodpurchase` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_foodpurchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_livestockproducts`
--

DROP TABLE IF EXISTS `wealthgroupinterview_livestockproducts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_livestockproducts` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `LivestockProduct` varchar(50) DEFAULT NULL,
  `LivestockType` varchar(50) DEFAULT NULL,
  `Market1` varchar(50) DEFAULT NULL,
  `Market2` varchar(50) DEFAULT NULL,
  `Market3` varchar(50) DEFAULT NULL,
  `PercentTradeMarket1` double DEFAULT NULL,
  `PercentTradeMarket2` double DEFAULT NULL,
  `PercentTradeMarket3` double DEFAULT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `UnitsConsumed` double DEFAULT NULL,
  `UnitsOtherUse` double DEFAULT NULL,
  `UnitsProduced` double NOT NULL,
  `UnitsSold` double DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_jsdq6frngqa7x6m0bhs79uavn` (`ResourceSubType`),
  KEY `FK_6943dda93etclwmk47yh3v7gl` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_6943dda93etclwmk47yh3v7gl` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`),
  CONSTRAINT `FK_jsdq6frngqa7x6m0bhs79uavn` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_livestockproducts`
--

LOCK TABLES `wealthgroupinterview_livestockproducts` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_livestockproducts` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_livestockproducts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_livestocksales`
--

DROP TABLE IF EXISTS `wealthgroupinterview_livestocksales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_livestocksales` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `LivestockType` varchar(50) DEFAULT NULL,
  `Market1` varchar(50) DEFAULT NULL,
  `Market2` varchar(50) DEFAULT NULL,
  `Market3` varchar(50) DEFAULT NULL,
  `PercentTradeMarket1` double DEFAULT NULL,
  `PercentTradeMarket2` double DEFAULT NULL,
  `PercentTradeMarket3` double DEFAULT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `UnitsAtStartofYear` double NOT NULL,
  `UnitsSold` double DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_issnv1qfr1xgj8b62edw1j1uh` (`ResourceSubType`),
  KEY `FK_6vctqt4kiiyavv8k78n3hp7d2` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_6vctqt4kiiyavv8k78n3hp7d2` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`),
  CONSTRAINT `FK_issnv1qfr1xgj8b62edw1j1uh` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_livestocksales`
--

LOCK TABLES `wealthgroupinterview_livestocksales` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_livestocksales` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_livestocksales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_nonfoodpurchase`
--

DROP TABLE IF EXISTS `wealthgroupinterview_nonfoodpurchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_nonfoodpurchase` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `ItemPurchased` varchar(50) NOT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `UnitsPurchased` double NOT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_45y3vct59px98sd68t16rh7ar` (`ResourceSubType`),
  KEY `FK_l9nqlkbokwg46ievqhq7xisc8` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_45y3vct59px98sd68t16rh7ar` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`),
  CONSTRAINT `FK_l9nqlkbokwg46ievqhq7xisc8` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_nonfoodpurchase`
--

LOCK TABLES `wealthgroupinterview_nonfoodpurchase` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_nonfoodpurchase` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_nonfoodpurchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_transfer`
--

DROP TABLE IF EXISTS `wealthgroupinterview_transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_transfer` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `CashTransferAmount` double DEFAULT NULL,
  `FoodResourceSubType` varchar(45) DEFAULT NULL,
  `Official` bit(1) NOT NULL,
  `Market1` varchar(50) DEFAULT NULL,
  `Market2` varchar(50) DEFAULT NULL,
  `Market3` varchar(50) DEFAULT NULL,
  `OtherUse` double DEFAULT NULL,
  `PeopleReceiving` double DEFAULT NULL,
  `PercentTradeMarket1` double DEFAULT NULL,
  `PercentTradeMarket2` double DEFAULT NULL,
  `PercentTradeMarket3` double DEFAULT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `Source` varchar(50) DEFAULT NULL,
  `TimesReceived` double DEFAULT NULL,
  `TransferFoodOtherType` varchar(50) DEFAULT NULL,
  `TransferType` varchar(50) DEFAULT NULL,
  `UnitsConsumed` double DEFAULT NULL,
  `UnitsSold` double DEFAULT NULL,
  `UnitesTransferred` double DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_rmyronihwbnxofuk0ojw2txq9` (`ResourceSubType`),
  KEY `FK_6ui6qpma5gyfdmdb03vmkg3nv` (`WealthGroupInterview_WGIID`),
  KEY `fk_wealthgroupinterview_transfer_1_idx` (`FoodResourceSubType`),
  CONSTRAINT `FK_6ui6qpma5gyfdmdb03vmkg3nv` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`),
  CONSTRAINT `FK_rmyronihwbnxofuk0ojw2txq9` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`),
  CONSTRAINT `fk_wealthgroupinterview_transfer_1` FOREIGN KEY (`FoodResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_transfer`
--

LOCK TABLES `wealthgroupinterview_transfer` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_transfer` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_transfer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wealthgroupinterview_wildfood`
--

DROP TABLE IF EXISTS `wealthgroupinterview_wildfood`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wealthgroupinterview_wildfood` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `Market1` varchar(50) DEFAULT NULL,
  `Market2` varchar(50) DEFAULT NULL,
  `Market3` varchar(50) DEFAULT NULL,
  `OtherUse` double DEFAULT NULL,
  `PercentTradeMarket1` double DEFAULT NULL,
  `PercentTradeMarket2` double DEFAULT NULL,
  `PercentTradeMarket3` double DEFAULT NULL,
  `PricePerUnit` double DEFAULT NULL,
  `ResourceSubType` varchar(32) DEFAULT NULL,
  `UnitsConsumed` double DEFAULT NULL,
  `UnitsProduced` double DEFAULT NULL,
  `UnitsSold` double DEFAULT NULL,
  `WildFoodName` varchar(50) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `Unit` varchar(50) NOT NULL,
  KEY `FK_e51mur5ir7bdvy6spp3a6sgvy` (`ResourceSubType`),
  KEY `FK_bp21nuxupo63nifhu9idgy51o` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_bp21nuxupo63nifhu9idgy51o` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `wealthgroupinterview` (`WGIID`),
  CONSTRAINT `FK_e51mur5ir7bdvy6spp3a6sgvy` FOREIGN KEY (`ResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wealthgroupinterview_wildfood`
--

LOCK TABLES `wealthgroupinterview_wildfood` WRITE;
/*!40000 ALTER TABLE `wealthgroupinterview_wildfood` DISABLE KEYS */;
/*!40000 ALTER TABLE `wealthgroupinterview_wildfood` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wgcharacteristicsresource`
--

DROP TABLE IF EXISTS `wgcharacteristicsresource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wgcharacteristicsresource` (
  `IDWGResource` varchar(32) NOT NULL,
  `WGResourceAmount` decimal(19,2) DEFAULT NULL,
  `WGResourceUnit` varchar(255) DEFAULT NULL,
  `WGResourceSubType` varchar(32) NOT NULL,
  `WGID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`IDWGResource`),
  KEY `FK_hr7tgaeagm3jtb00vmb1t4par` (`WGResourceSubType`),
  KEY `FK_4n60h91nbv0sol2p7423gvpx9` (`WGID`),
  CONSTRAINT `FK_4n60h91nbv0sol2p7423gvpx9` FOREIGN KEY (`WGID`) REFERENCES `wealthgroup` (`WealthGroupID`),
  CONSTRAINT `FK_hr7tgaeagm3jtb00vmb1t4par` FOREIGN KEY (`WGResourceSubType`) REFERENCES `resourcesubtype` (`IDResourceSubType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wgcharacteristicsresource`
--

LOCK TABLES `wgcharacteristicsresource` WRITE;
/*!40000 ALTER TABLE `wgcharacteristicsresource` DISABLE KEYS */;
/*!40000 ALTER TABLE `wgcharacteristicsresource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `wginterview_cash_income_details`
--

DROP TABLE IF EXISTS `wginterview_cash_income_details`;
/*!50001 DROP VIEW IF EXISTS `wginterview_cash_income_details`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `wginterview_cash_income_details` AS SELECT 
 1 AS `Project`,
 1 AS `ProjectDate`,
 1 AS `ExchangeRate`,
 1 AS `LZName`,
 1 AS `Country`,
 1 AS `District`,
 1 AS `SubDistrict`,
 1 AS `WGNameEng`,
 1 AS `WGHHSize`,
 1 AS `WGNameLoc`,
 1 AS `WGOrder`,
 1 AS `WGPercent`,
 1 AS `WGWives`,
 1 AS `CommunityID`,
 1 AS `WGID`,
 1 AS `WGIIID`,
 1 AS `WGInterviewDate`,
 1 AS `WGIntervieweesCount`,
 1 AS `WGInterviewers`,
 1 AS `WGInterviewNumber`,
 1 AS `WGISpreadsheet`,
 1 AS `WGIStatus`,
 1 AS `WGMaleIVees`,
 1 AS `WGFemaleIVees`,
 1 AS `WGYearType`,
 1 AS `ResourceType`,
 1 AS `ResourceSubTypeName`,
 1 AS `ResourceUnit`,
 1 AS `ResourceKCal`,
 1 AS `unit`,
 1 AS `status`,
 1 AS `val1`,
 1 AS `val2`,
 1 AS `val3`,
 1 AS `val4`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `wginterview_cash_income_report`
--

DROP TABLE IF EXISTS `wginterview_cash_income_report`;
/*!50001 DROP VIEW IF EXISTS `wginterview_cash_income_report`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `wginterview_cash_income_report` AS SELECT 
 1 AS `Project`,
 1 AS `ProjectDate`,
 1 AS `ExchangeRate`,
 1 AS `LZName`,
 1 AS `Country`,
 1 AS `District`,
 1 AS `SubDistrict`,
 1 AS `WGNameEng`,
 1 AS `WGHHSize`,
 1 AS `WGNameLoc`,
 1 AS `WGOrder`,
 1 AS `WGPercent`,
 1 AS `WGWives`,
 1 AS `CommunityID`,
 1 AS `WGID`,
 1 AS `WGIIID`,
 1 AS `WGInterviewDate`,
 1 AS `WGIntervieweesCount`,
 1 AS `WGInterviewers`,
 1 AS `WGInterviewNumber`,
 1 AS `WGISpreadsheet`,
 1 AS `WGIStatus`,
 1 AS `WGMaleIVees`,
 1 AS `WGFemaleIVees`,
 1 AS `WGYearType`,
 1 AS `ResourceType`,
 1 AS `ResourceSubTypeName`,
 1 AS `ResourceUnit`,
 1 AS `ResourceKCal`,
 1 AS `EnteredType`,
 1 AS `unit`,
 1 AS `status`,
 1 AS `UnitsProduced`,
 1 AS `UnitsSold`,
 1 AS `PricePerUnit`,
 1 AS `UnitsConsumed`,
 1 AS `UnitsOther`,
 1 AS `Market1`,
 1 AS `Market2`,
 1 AS `Market3`,
 1 AS `PercentTradeMarket1`,
 1 AS `PercentTradeMarket2`,
 1 AS `PercentTradeMarket3`,
 1 AS `UnitsAtStartofYear`,
 1 AS `cashpaymentamount`,
 1 AS `foodpaymentfoodtype`,
 1 AS `foodpaymentunit`,
 1 AS `foodpaymentunitspaidwork`,
 1 AS `foodpaymentpeoplecount`,
 1 AS `UnitsWorked`,
 1 AS `PercentWorkLocation1`,
 1 AS `PercentWorkLocation2`,
 1 AS `PercentWorkLocation3`,
 1 AS `WorkLocation1`,
 1 AS `WorkLocation2`,
 1 AS `WorkLocation3`,
 1 AS `CashTransferAmount`,
 1 AS `Official`,
 1 AS `PeopleReceiving`,
 1 AS `Source`,
 1 AS `TimesReceived`,
 1 AS `TransferFoodOtherType`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `wginterview_details`
--

DROP TABLE IF EXISTS `wginterview_details`;
/*!50001 DROP VIEW IF EXISTS `wginterview_details`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `wginterview_details` AS SELECT 
 1 AS `Project`,
 1 AS `ProjectDate`,
 1 AS `ExchangeRate`,
 1 AS `LZName`,
 1 AS `Country`,
 1 AS `District`,
 1 AS `SubDistrict`,
 1 AS `WGNameEng`,
 1 AS `WGHHSize`,
 1 AS `WGNameLoc`,
 1 AS `WGOrder`,
 1 AS `WGPercent`,
 1 AS `WGWives`,
 1 AS `CommunityID`,
 1 AS `WGID`,
 1 AS `WGIIID`,
 1 AS `WGInterviewDate`,
 1 AS `WGIntervieweesCount`,
 1 AS `WGInterviewers`,
 1 AS `WGInterviewNumber`,
 1 AS `WGISpreadsheet`,
 1 AS `WGIStatus`,
 1 AS `WGMaleIVees`,
 1 AS `WGFemaleIVees`,
 1 AS `WGYearType`,
 1 AS `ResourceType`,
 1 AS `ResourceSubTypeName`,
 1 AS `ResourceUnit`,
 1 AS `ResourceKCal`,
 1 AS `unit`,
 1 AS `status`,
 1 AS `xx`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `wginterview_food_consumed_details`
--

DROP TABLE IF EXISTS `wginterview_food_consumed_details`;
/*!50001 DROP VIEW IF EXISTS `wginterview_food_consumed_details`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `wginterview_food_consumed_details` AS SELECT 
 1 AS `Project`,
 1 AS `ProjectDate`,
 1 AS `ExchangeRate`,
 1 AS `LZName`,
 1 AS `Country`,
 1 AS `District`,
 1 AS `SubDistrict`,
 1 AS `WGNameEng`,
 1 AS `WGHHSize`,
 1 AS `WGNameLoc`,
 1 AS `WGOrder`,
 1 AS `WGPercent`,
 1 AS `WGWives`,
 1 AS `CommunityID`,
 1 AS `WGID`,
 1 AS `WGIIID`,
 1 AS `WGInterviewDate`,
 1 AS `WGIntervieweesCount`,
 1 AS `WGInterviewers`,
 1 AS `WGInterviewNumber`,
 1 AS `WGISpreadsheet`,
 1 AS `WGIStatus`,
 1 AS `WGMaleIVees`,
 1 AS `WGFemaleIVees`,
 1 AS `WGYearType`,
 1 AS `ResourceType`,
 1 AS `ResourceSubTypeName`,
 1 AS `ResourceUnit`,
 1 AS `ResourceKCal`,
 1 AS `unit`,
 1 AS `status`,
 1 AS `val1`,
 1 AS `val2`,
 1 AS `val3`,
 1 AS `val4`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `wginterview_foodpurchased_details`
--

DROP TABLE IF EXISTS `wginterview_foodpurchased_details`;
/*!50001 DROP VIEW IF EXISTS `wginterview_foodpurchased_details`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `wginterview_foodpurchased_details` AS SELECT 
 1 AS `Project`,
 1 AS `ProjectDate`,
 1 AS `ExchangeRate`,
 1 AS `LZName`,
 1 AS `Country`,
 1 AS `District`,
 1 AS `SubDistrict`,
 1 AS `WGNameEng`,
 1 AS `WGHHSize`,
 1 AS `WGNameLoc`,
 1 AS `WGOrder`,
 1 AS `WGPercent`,
 1 AS `WGWives`,
 1 AS `CommunityID`,
 1 AS `WGID`,
 1 AS `WGIIID`,
 1 AS `WGInterviewDate`,
 1 AS `WGIntervieweesCount`,
 1 AS `WGInterviewers`,
 1 AS `WGInterviewNumber`,
 1 AS `WGISpreadsheet`,
 1 AS `WGIStatus`,
 1 AS `WGMaleIVees`,
 1 AS `WGFemaleIVees`,
 1 AS `WGYearType`,
 1 AS `ResourceType`,
 1 AS `ResourceSubTypeName`,
 1 AS `ResourceUnit`,
 1 AS `ResourceKCal`,
 1 AS `unit`,
 1 AS `status`,
 1 AS `UnitsPurchased`,
 1 AS `PricePerUnit`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `wginterview_nonfoodpurchased_details`
--

DROP TABLE IF EXISTS `wginterview_nonfoodpurchased_details`;
/*!50001 DROP VIEW IF EXISTS `wginterview_nonfoodpurchased_details`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `wginterview_nonfoodpurchased_details` AS SELECT 
 1 AS `Project`,
 1 AS `ProjectDate`,
 1 AS `ExchangeRate`,
 1 AS `LZName`,
 1 AS `Country`,
 1 AS `District`,
 1 AS `SubDistrict`,
 1 AS `WGNameEng`,
 1 AS `WGHHSize`,
 1 AS `WGNameLoc`,
 1 AS `WGOrder`,
 1 AS `WGPercent`,
 1 AS `WGWives`,
 1 AS `CommunityID`,
 1 AS `WGID`,
 1 AS `WGIIID`,
 1 AS `WGInterviewDate`,
 1 AS `WGIntervieweesCount`,
 1 AS `WGInterviewers`,
 1 AS `WGInterviewNumber`,
 1 AS `WGISpreadsheet`,
 1 AS `WGIStatus`,
 1 AS `WGMaleIVees`,
 1 AS `WGFemaleIVees`,
 1 AS `WGYearType`,
 1 AS `ResourceType`,
 1 AS `ResourceSubTypeName`,
 1 AS `ResourceUnit`,
 1 AS `ResourceKCal`,
 1 AS `unit`,
 1 AS `status`,
 1 AS `UnitsPurchased`,
 1 AS `PricePerUnit`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `project_details`
--

/*!50001 DROP VIEW IF EXISTS `project_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`efd`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `project_details` AS select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_assetcash`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_assetcash`.`Unit` AS `unit`,`wealthgroupinterview_assetcash`.`Status` AS `status`,`wealthgroupinterview_assetcash`.`Amount` AS `cashamount`,`wealthgroupinterview_assetcash`.`Currency` AS `cashcurrency`,'' AS `TypeEntered`,0 AS `Quantity`,0 AS `PricePerUnit`,'' AS `Market1`,0 AS `PercentTradeMarket1`,'' AS `Market2`,0 AS `PercentTradeMarket2`,'' AS `Market3`,0 AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,0 AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_assetcash` on((`wealthgroupinterview_assetcash`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_assetcash`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_assetfoodstock`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_assetfoodstock`.`Unit` AS `unit`,`wealthgroupinterview_assetfoodstock`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_assetfoodstock`.`FoodTypeEnteredName` AS `TypeEntered`,`wealthgroupinterview_assetfoodstock`.`Quantity` AS `Quantity`,0 AS `PricePerUnit`,'' AS `Market1`,0 AS `PercentTradeMarket1`,'' AS `Market2`,0 AS `PercentTradeMarket2`,'' AS `Market3`,0 AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,0 AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_assetfoodstock` on((`wealthgroupinterview_assetfoodstock`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_assetfoodstock`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_assetland`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_assetland`.`Unit` AS `unit`,`wealthgroupinterview_assetland`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_assetland`.`LandTypeEnteredName` AS `TypeEntered`,`wealthgroupinterview_assetland`.`NumberofUnits` AS `Quantity`,0 AS `PricePerUnit`,'' AS `Market1`,0 AS `PercentTradeMarket1`,'' AS `Market2`,0 AS `PercentTradeMarket2`,'' AS `Market3`,0 AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,0 AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_assetland` on((`wealthgroupinterview_assetland`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_assetland`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_assetlivestock`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_assetlivestock`.`Unit` AS `unit`,`wealthgroupinterview_assetlivestock`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_assetlivestock`.`LiveStockTypeEnteredName` AS `TypeEntered`,`wealthgroupinterview_assetlivestock`.`NumberOwnedAtStart` AS `Quantity`,`wealthgroupinterview_assetlivestock`.`PricePerUnit` AS `PricePerUnit`,'' AS `Market1`,0 AS `PercentTradeMarket1`,'' AS `Market2`,0 AS `PercentTradeMarket2`,'' AS `Market3`,0 AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,0 AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_assetlivestock` on((`wealthgroupinterview_assetlivestock`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_assetlivestock`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_assettradeable`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_assettradeable`.`Unit` AS `unit`,`wealthgroupinterview_assettradeable`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_assettradeable`.`TradeableTypeEnteredName` AS `TypeEntered`,`wealthgroupinterview_assettradeable`.`NumberOwned` AS `Quantity`,`wealthgroupinterview_assettradeable`.`PricePerUnit` AS `PricePerUnit`,'' AS `Market1`,0 AS `PercentTradeMarket1`,'' AS `Market2`,0 AS `PercentTradeMarket2`,'' AS `Market3`,0 AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,0 AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_assettradeable` on((`wealthgroupinterview_assettradeable`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_assettradeable`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_assettree`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_assettree`.`Unit` AS `unit`,`wealthgroupinterview_assettree`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_assettree`.`TreeTypeEnteredName` AS `TypeEntered`,`wealthgroupinterview_assettree`.`NumberOwned` AS `Quantity`,`wealthgroupinterview_assettree`.`PricePerUnit` AS `PricePerUnit`,'' AS `Market1`,0 AS `PercentTradeMarket1`,'' AS `Market2`,0 AS `PercentTradeMarket2`,'' AS `Market3`,0 AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,0 AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_assettree` on((`wealthgroupinterview_assettree`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_assettree`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_crop`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_crop`.`Unit` AS `unit`,`wealthgroupinterview_crop`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_crop`.`CropType` AS `TypeEntered`,0 AS `Quantity`,`wealthgroupinterview_crop`.`PricePerUnit` AS `PricePerUnit`,`wealthgroupinterview_crop`.`Market1` AS `Market1`,`wealthgroupinterview_crop`.`PercentTradeMarket1` AS `PercentTradeMarket1`,`wealthgroupinterview_crop`.`Market2` AS `Market2`,`wealthgroupinterview_crop`.`PercentTradeMarket2` AS `PercentTradeMarket2`,`wealthgroupinterview_crop`.`Market3` AS `Market3`,`wealthgroupinterview_crop`.`PercentTradeMarket3` AS `PercentTradeMarket3`,`wealthgroupinterview_crop`.`UnitsConsumed` AS `UnitsConsumed`,`wealthgroupinterview_crop`.`UnitsOtherUse` AS `UnitsOtherUse`,`wealthgroupinterview_crop`.`UnitsProduced` AS `UnitsProduced`,`wealthgroupinterview_crop`.`UnitsSold` AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_crop` on((`wealthgroupinterview_crop`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_crop`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_employment`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_employment`.`Unit` AS `unit`,`wealthgroupinterview_employment`.`Status` AS `status`,`wealthgroupinterview_employment`.`CashPaymentAmount` AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_employment`.`EmploymentName` AS `TypeEntered`,`wealthgroupinterview_employment`.`UnitsWorked` AS `Quantity`,0 AS `PricePerUnit`,'' AS `Market1`,0 AS `PercentTradeMarket1`,'' AS `Market2`,0 AS `PercentTradeMarket2`,'' AS `Market3`,0 AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,0 AS `UnitsSold`,`wealthgroupinterview_employment`.`FoodPaymentFoodType` AS `FoodPaymentFoodType`,`wealthgroupinterview_employment`.`FoodPaymentUnit` AS `FoodPaymentUnit`,`wealthgroupinterview_employment`.`FoodPaymentUnitsPaidWork` AS `FoodPaymentUnitsPaidWork`,`wealthgroupinterview_employment`.`PeopleCount` AS `PeopleCount`,`wealthgroupinterview_employment`.`WorkLocation1` AS `WorkLocation1`,`wealthgroupinterview_employment`.`PercentWorkLocation1` AS `PercentWorkLocation1`,`wealthgroupinterview_employment`.`WorkLocation2` AS `WorkLocation2`,`wealthgroupinterview_employment`.`PercentWorkLocation2` AS `PercentWorkLocation2`,`wealthgroupinterview_employment`.`WorkLocation3` AS `WorkLocation3`,`wealthgroupinterview_employment`.`PercentWorkLocation3` AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_employment` on((`wealthgroupinterview_employment`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_employment`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_foodpurchase`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_foodpurchase`.`Unit` AS `unit`,`wealthgroupinterview_foodpurchase`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_foodpurchase`.`FoodTypeEnteredName` AS `TypeEntered`,`wealthgroupinterview_foodpurchase`.`UnitsPurchased` AS `Quantity`,`wealthgroupinterview_foodpurchase`.`PricePerUnit` AS `PricePerUnit`,'' AS `Market1`,0 AS `PercentTradeMarket1`,'' AS `Market2`,0 AS `PercentTradeMarket2`,'' AS `Market3`,0 AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,0 AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_foodpurchase` on((`wealthgroupinterview_foodpurchase`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_foodpurchase`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_nonfoodpurchase`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_nonfoodpurchase`.`Unit` AS `unit`,`wealthgroupinterview_nonfoodpurchase`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_nonfoodpurchase`.`ItemPurchased` AS `TypeEntered`,`wealthgroupinterview_nonfoodpurchase`.`UnitsPurchased` AS `Quantity`,`wealthgroupinterview_nonfoodpurchase`.`PricePerUnit` AS `PricePerUnit`,'' AS `Market1`,0 AS `PercentTradeMarket1`,'' AS `Market2`,0 AS `PercentTradeMarket2`,'' AS `Market3`,0 AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,0 AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_nonfoodpurchase` on((`wealthgroupinterview_nonfoodpurchase`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_nonfoodpurchase`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_livestockproducts`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_livestockproducts`.`Unit` AS `unit`,`wealthgroupinterview_livestockproducts`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_livestockproducts`.`LivestockType` AS `TypeEntered`,0 AS `Quantity`,`wealthgroupinterview_livestockproducts`.`PricePerUnit` AS `PricePerUnit`,`wealthgroupinterview_livestockproducts`.`Market1` AS `Market1`,`wealthgroupinterview_livestockproducts`.`PercentTradeMarket1` AS `PercentTradeMarket1`,`wealthgroupinterview_livestockproducts`.`Market2` AS `Market2`,`wealthgroupinterview_livestockproducts`.`PercentTradeMarket2` AS `PercentTradeMarket2`,`wealthgroupinterview_livestockproducts`.`Market3` AS `Market3`,`wealthgroupinterview_livestockproducts`.`PercentTradeMarket3` AS `PercentTradeMarket3`,`wealthgroupinterview_livestockproducts`.`UnitsConsumed` AS `UnitsConsumed`,`wealthgroupinterview_livestockproducts`.`UnitsOtherUse` AS `UnitsOtherUse`,`wealthgroupinterview_livestockproducts`.`UnitsProduced` AS `UnitsProduced`,`wealthgroupinterview_livestockproducts`.`UnitsSold` AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,`wealthgroupinterview_livestockproducts`.`LivestockProduct` AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_livestockproducts` on((`wealthgroupinterview_livestockproducts`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_livestockproducts`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_livestocksales`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_livestocksales`.`Unit` AS `unit`,`wealthgroupinterview_livestocksales`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_livestocksales`.`LivestockType` AS `TypeEntered`,`wealthgroupinterview_livestocksales`.`UnitsAtStartofYear` AS `Quantity`,`wealthgroupinterview_livestocksales`.`PricePerUnit` AS `PricePerUnit`,`wealthgroupinterview_livestocksales`.`Market1` AS `Market1`,`wealthgroupinterview_livestocksales`.`PercentTradeMarket1` AS `PercentTradeMarket1`,`wealthgroupinterview_livestocksales`.`Market2` AS `Market2`,`wealthgroupinterview_livestocksales`.`PercentTradeMarket2` AS `PercentTradeMarket2`,`wealthgroupinterview_livestocksales`.`Market3` AS `Market3`,`wealthgroupinterview_livestocksales`.`PercentTradeMarket3` AS `PercentTradeMarket3`,0 AS `UnitsConsumed`,0 AS `UnitsOtherUse`,0 AS `UnitsProduced`,`wealthgroupinterview_livestocksales`.`UnitsSold` AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_livestocksales` on((`wealthgroupinterview_livestocksales`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_livestocksales`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_transfer`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_transfer`.`Unit` AS `unit`,`wealthgroupinterview_transfer`.`Status` AS `status`,`wealthgroupinterview_transfer`.`CashTransferAmount` AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_transfer`.`TransferType` AS `TypeEntered`,0 AS `Quantity`,`wealthgroupinterview_transfer`.`PricePerUnit` AS `PricePerUnit`,`wealthgroupinterview_transfer`.`Market1` AS `Market1`,`wealthgroupinterview_transfer`.`PercentTradeMarket1` AS `PercentTradeMarket1`,`wealthgroupinterview_transfer`.`Market2` AS `Market2`,`wealthgroupinterview_transfer`.`PercentTradeMarket2` AS `PercentTradeMarket2`,`wealthgroupinterview_transfer`.`Market3` AS `Market3`,`wealthgroupinterview_transfer`.`PercentTradeMarket3` AS `PercentTradeMarket3`,`wealthgroupinterview_transfer`.`UnitsConsumed` AS `UnitsConsumed`,`wealthgroupinterview_transfer`.`OtherUse` AS `UnitsOtherUse`,0 AS `UnitsProduced`,`wealthgroupinterview_transfer`.`UnitsSold` AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,`wealthgroupinterview_transfer`.`Official` AS `Official`,`wealthgroupinterview_transfer`.`PeopleReceiving` AS `PeopleReceiving`,`wealthgroupinterview_transfer`.`Source` AS `Source`,`wealthgroupinterview_transfer`.`TimesReceived` AS `TimesReceived`,`wealthgroupinterview_transfer`.`TransferFoodOtherType` AS `TransferFoodOtherType`,`wealthgroupinterview_transfer`.`UnitesTransferred` AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_transfer` on((`wealthgroupinterview_transfer`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_transfer`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) union select `project`.`ProjectID` AS `ProjectID`,`project`.`ProjectTitle` AS `Project`,date_format(`project`.`PDate`,'%d/%m/%y') AS `ProjectDate`,`project`.`altExchangeRate` AS `ExchangeRate`,`livelihoodzone`.`LZName` AS `LZName`,`country`.`CountryName` AS `Country`,`site`.`LocationDistrict` AS `District`,`site`.`SubDistrict` AS `SubDistrict`,`community`.`CInterviewDate` AS `CInterviewDate`,`community`.`CIVF` AS `CIVF`,`community`.`CIVM` AS `CIVM`,`community`.`CIVparticipants` AS `CIVParticipants`,`community`.`Interviewers` AS `Interviewers`,`wealthgroup`.`WGName_Eng` AS `WGNameEng`,`wealthgroup`.`WGHHSize` AS `WGHHSize`,`wealthgroup`.`WGName_Local` AS `WGNameLoc`,`wealthgroup`.`WGOrder` AS `WGOrder`,`wealthgroup`.`WGPercent` AS `WGPercent`,`wealthgroup`.`WGWives` AS `WGWives`,`wealthgroup`.`CommunityID` AS `CommunityID`,`wealthgroupinterview`.`WGID` AS `WGID`,`wealthgroupinterview_wildfood`.`WealthGroupInterview_WGIID` AS `WGIIID`,date_format(`wealthgroupinterview`.`WGInterviewDate`,'%d/%m/%y') AS `WGInterviewDate`,`wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,`wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,`wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,`wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,`wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,`wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,`wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,`wealthgroupinterview`.`WGYearType` AS `WGYearType`,`resourcetype`.`ResourceTypeName` AS `ResourceType`,`resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,`resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,`resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,`wealthgroupinterview_wildfood`.`Unit` AS `unit`,`wealthgroupinterview_wildfood`.`Status` AS `status`,0 AS `cashamount`,0 AS `cashcurrency`,`wealthgroupinterview_wildfood`.`WildFoodName` AS `TypeEntered`,0 AS `Quantity`,`wealthgroupinterview_wildfood`.`PricePerUnit` AS `PricePerUnit`,`wealthgroupinterview_wildfood`.`Market1` AS `Market1`,`wealthgroupinterview_wildfood`.`PercentTradeMarket1` AS `PercentTradeMarket1`,`wealthgroupinterview_wildfood`.`Market2` AS `Market2`,`wealthgroupinterview_wildfood`.`PercentTradeMarket2` AS `PercentTradeMarket2`,`wealthgroupinterview_wildfood`.`Market3` AS `Market3`,`wealthgroupinterview_wildfood`.`PercentTradeMarket3` AS `PercentTradeMarket3`,`wealthgroupinterview_wildfood`.`UnitsConsumed` AS `UnitsConsumed`,`wealthgroupinterview_wildfood`.`OtherUse` AS `UnitsOtherUse`,`wealthgroupinterview_wildfood`.`UnitsProduced` AS `UnitsProduced`,`wealthgroupinterview_wildfood`.`UnitsSold` AS `UnitsSold`,'' AS `FoodPaymentFoodType`,'' AS `FoodPaymentUnit`,'' AS `FoodPaymentUnitsPaidWork`,0 AS `PeopleCount`,'' AS `WorkLocation1`,0 AS `PercentWorkLocation1`,'' AS `WorkLocation2`,0 AS `PercentWorkLocation2`,'' AS `WorkLocation3`,0 AS `PercentWorkLocation3`,'' AS `LivestockProduct`,'' AS `Official`,0 AS `PeopleReceiving`,'' AS `Source`,0 AS `TimesReceived`,'' AS `TransferFoodOtherType`,0 AS `unitestransferred` from ((((((((((`project` join `country`) join `livelihoodzone`) join `projectlz`) join `site`) join `community`) join `wealthgroup`) join `wealthgroupinterview`) join `resourcetype`) join `resourcesubtype`) left join `wealthgroupinterview_wildfood` on((`wealthgroupinterview_wildfood`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`))) where ((`project`.`ProjectID` = `projectlz`.`Project`) and (`livelihoodzone`.`LZID` = `projectlz`.`LZ`) and (`livelihoodzone`.`LZID` = `site`.`LZ`) and (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`) and (`community`.`CLocation` = `site`.`LocationID`) and (`community`.`CID` = `wealthgroup`.`CommunityID`) and (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`) and (`wealthgroupinterview`.`WGIStatus` = 4) and (`community`.`CProject` = `project`.`ProjectID`) and (`wealthgroupinterview_wildfood`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`) and (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `resources`
--

/*!50001 DROP VIEW IF EXISTS `resources`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`efd`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `resources` AS select `resourcetype`.`ResourceTypeName` AS `rtype`,`resourcesubtype`.`ResourceTypeName` AS `rsubtype` from (`resourcesubtype` join `resourcetype`) where (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `wginterview_cash_income_details`
--

/*!50001 DROP VIEW IF EXISTS `wginterview_cash_income_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`efd`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `wginterview_cash_income_details` AS select 1 AS `Project`,1 AS `ProjectDate`,1 AS `ExchangeRate`,1 AS `LZName`,1 AS `Country`,1 AS `District`,1 AS `SubDistrict`,1 AS `WGNameEng`,1 AS `WGHHSize`,1 AS `WGNameLoc`,1 AS `WGOrder`,1 AS `WGPercent`,1 AS `WGWives`,1 AS `CommunityID`,1 AS `WGID`,1 AS `WGIIID`,1 AS `WGInterviewDate`,1 AS `WGIntervieweesCount`,1 AS `WGInterviewers`,1 AS `WGInterviewNumber`,1 AS `WGISpreadsheet`,1 AS `WGIStatus`,1 AS `WGMaleIVees`,1 AS `WGFemaleIVees`,1 AS `WGYearType`,1 AS `ResourceType`,1 AS `ResourceSubTypeName`,1 AS `ResourceUnit`,1 AS `ResourceKCal`,1 AS `unit`,1 AS `status`,1 AS `val1`,1 AS `val2`,1 AS `val3`,1 AS `val4` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `wginterview_cash_income_report`
--

/*!50001 DROP VIEW IF EXISTS `wginterview_cash_income_report`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`efd`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `wginterview_cash_income_report` AS select 1 AS `Project`,1 AS `ProjectDate`,1 AS `ExchangeRate`,1 AS `LZName`,1 AS `Country`,1 AS `District`,1 AS `SubDistrict`,1 AS `WGNameEng`,1 AS `WGHHSize`,1 AS `WGNameLoc`,1 AS `WGOrder`,1 AS `WGPercent`,1 AS `WGWives`,1 AS `CommunityID`,1 AS `WGID`,1 AS `WGIIID`,1 AS `WGInterviewDate`,1 AS `WGIntervieweesCount`,1 AS `WGInterviewers`,1 AS `WGInterviewNumber`,1 AS `WGISpreadsheet`,1 AS `WGIStatus`,1 AS `WGMaleIVees`,1 AS `WGFemaleIVees`,1 AS `WGYearType`,1 AS `ResourceType`,1 AS `ResourceSubTypeName`,1 AS `ResourceUnit`,1 AS `ResourceKCal`,1 AS `EnteredType`,1 AS `unit`,1 AS `status`,1 AS `UnitsProduced`,1 AS `UnitsSold`,1 AS `PricePerUnit`,1 AS `UnitsConsumed`,1 AS `UnitsOther`,1 AS `Market1`,1 AS `Market2`,1 AS `Market3`,1 AS `PercentTradeMarket1`,1 AS `PercentTradeMarket2`,1 AS `PercentTradeMarket3`,1 AS `UnitsAtStartofYear`,1 AS `cashpaymentamount`,1 AS `foodpaymentfoodtype`,1 AS `foodpaymentunit`,1 AS `foodpaymentunitspaidwork`,1 AS `foodpaymentpeoplecount`,1 AS `UnitsWorked`,1 AS `PercentWorkLocation1`,1 AS `PercentWorkLocation2`,1 AS `PercentWorkLocation3`,1 AS `WorkLocation1`,1 AS `WorkLocation2`,1 AS `WorkLocation3`,1 AS `CashTransferAmount`,1 AS `Official`,1 AS `PeopleReceiving`,1 AS `Source`,1 AS `TimesReceived`,1 AS `TransferFoodOtherType` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `wginterview_details`
--

/*!50001 DROP VIEW IF EXISTS `wginterview_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`efd`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `wginterview_details` AS select 1 AS `Project`,1 AS `ProjectDate`,1 AS `ExchangeRate`,1 AS `LZName`,1 AS `Country`,1 AS `District`,1 AS `SubDistrict`,1 AS `WGNameEng`,1 AS `WGHHSize`,1 AS `WGNameLoc`,1 AS `WGOrder`,1 AS `WGPercent`,1 AS `WGWives`,1 AS `CommunityID`,1 AS `WGID`,1 AS `WGIIID`,1 AS `WGInterviewDate`,1 AS `WGIntervieweesCount`,1 AS `WGInterviewers`,1 AS `WGInterviewNumber`,1 AS `WGISpreadsheet`,1 AS `WGIStatus`,1 AS `WGMaleIVees`,1 AS `WGFemaleIVees`,1 AS `WGYearType`,1 AS `ResourceType`,1 AS `ResourceSubTypeName`,1 AS `ResourceUnit`,1 AS `ResourceKCal`,1 AS `unit`,1 AS `status`,1 AS `xx` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `wginterview_food_consumed_details`
--

/*!50001 DROP VIEW IF EXISTS `wginterview_food_consumed_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`efd`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `wginterview_food_consumed_details` AS select 1 AS `Project`,1 AS `ProjectDate`,1 AS `ExchangeRate`,1 AS `LZName`,1 AS `Country`,1 AS `District`,1 AS `SubDistrict`,1 AS `WGNameEng`,1 AS `WGHHSize`,1 AS `WGNameLoc`,1 AS `WGOrder`,1 AS `WGPercent`,1 AS `WGWives`,1 AS `CommunityID`,1 AS `WGID`,1 AS `WGIIID`,1 AS `WGInterviewDate`,1 AS `WGIntervieweesCount`,1 AS `WGInterviewers`,1 AS `WGInterviewNumber`,1 AS `WGISpreadsheet`,1 AS `WGIStatus`,1 AS `WGMaleIVees`,1 AS `WGFemaleIVees`,1 AS `WGYearType`,1 AS `ResourceType`,1 AS `ResourceSubTypeName`,1 AS `ResourceUnit`,1 AS `ResourceKCal`,1 AS `unit`,1 AS `status`,1 AS `val1`,1 AS `val2`,1 AS `val3`,1 AS `val4` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `wginterview_foodpurchased_details`
--

/*!50001 DROP VIEW IF EXISTS `wginterview_foodpurchased_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`efd`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `wginterview_foodpurchased_details` AS select 1 AS `Project`,1 AS `ProjectDate`,1 AS `ExchangeRate`,1 AS `LZName`,1 AS `Country`,1 AS `District`,1 AS `SubDistrict`,1 AS `WGNameEng`,1 AS `WGHHSize`,1 AS `WGNameLoc`,1 AS `WGOrder`,1 AS `WGPercent`,1 AS `WGWives`,1 AS `CommunityID`,1 AS `WGID`,1 AS `WGIIID`,1 AS `WGInterviewDate`,1 AS `WGIntervieweesCount`,1 AS `WGInterviewers`,1 AS `WGInterviewNumber`,1 AS `WGISpreadsheet`,1 AS `WGIStatus`,1 AS `WGMaleIVees`,1 AS `WGFemaleIVees`,1 AS `WGYearType`,1 AS `ResourceType`,1 AS `ResourceSubTypeName`,1 AS `ResourceUnit`,1 AS `ResourceKCal`,1 AS `unit`,1 AS `status`,1 AS `UnitsPurchased`,1 AS `PricePerUnit` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `wginterview_nonfoodpurchased_details`
--

/*!50001 DROP VIEW IF EXISTS `wginterview_nonfoodpurchased_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`efd`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `wginterview_nonfoodpurchased_details` AS select 1 AS `Project`,1 AS `ProjectDate`,1 AS `ExchangeRate`,1 AS `LZName`,1 AS `Country`,1 AS `District`,1 AS `SubDistrict`,1 AS `WGNameEng`,1 AS `WGHHSize`,1 AS `WGNameLoc`,1 AS `WGOrder`,1 AS `WGPercent`,1 AS `WGWives`,1 AS `CommunityID`,1 AS `WGID`,1 AS `WGIIID`,1 AS `WGInterviewDate`,1 AS `WGIntervieweesCount`,1 AS `WGInterviewers`,1 AS `WGInterviewNumber`,1 AS `WGISpreadsheet`,1 AS `WGIStatus`,1 AS `WGMaleIVees`,1 AS `WGFemaleIVees`,1 AS `WGYearType`,1 AS `ResourceType`,1 AS `ResourceSubTypeName`,1 AS `ResourceUnit`,1 AS `ResourceKCal`,1 AS `unit`,1 AS `status`,1 AS `UnitsPurchased`,1 AS `PricePerUnit` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-10-23 15:47:53
