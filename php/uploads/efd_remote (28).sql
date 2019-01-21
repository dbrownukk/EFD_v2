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
INSERT INTO `country` VALUES ('4028808860bbdf630160bbe5f21a0002','MWK',NULL,'Malawi','MAL'),('402880895ff401b9015ff403e4150002','GBP','Â£','United Kingdom','UK'),('8a80848460c2ee580160c73af80f000b','USH','','Uganda','UGA'),('8a80848460d98aad0160dbc35b50000d','KSH','K','Kenya','KEA'),('8a80848461a48f910161a543f02c0005','CFA','','Burkina Faso','BFA'),('8a80848461fbb5780162062bad4c005f','EU','â‚¬','EuroZone','EUZ');
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
INSERT INTO `livelihoodzone` VALUES ('4028808560a7f1160160a803220a0003','Z1',_binary '9','402880895ff401b9015ff403e4150002'),('4028808560b7ce950160b7d003380002','24444','','402880895ff401b9015ff403e4150002'),('4028808860bbdf630160bbe5f6540003','Coffee-Banana-Pigs',_binary '‰PNG\r\n\Z\n\0\0\0\rIHDR\0\0\Â\0\0\0\0\0¸6w\0\0\0sRGB\0®\Î\é\0\0\0gAMA\0\0±üa\0\0\0	pHYs\0\0\Ã\0\0\Ã\Ço¨d\0\0ÁsIDATx^\íý‰—$\Çu\æ\â™™3šw\ÞLK³tž¤nõŠ%‘-‰\â&v7%‚”\ÔIP\ÛA²EI\\$R¤¸`/P¨@¡€\ÂZ\Ø÷}G¡–\Ü÷=#\"3r_ª\n€ý®Ç´ô4ppðˆ\Ê<\ç;\éaf\înnn~?»\×\î5»¢4?j\ÂX,Mšõ%³º\\Ø“\×*p_ú>²\Ãry\Î,/\Îzóº…1³±Vö\æµó=·˜÷Ë§ŒYy\Ü,\r\Ü`Š\ã/xËµq¿\Ñõ\ÕE³bû\Çe+[V–\æ%ó\×W¤\ß-\'öœ\×j,\Æs!w¨ƒ¶\×>v\0}ðÁûÞ¼f±‡µƒ†\Ó[\r\êÁG\ã\Ë\ÛG6XZœ±B\êòú\0W–\nòÜ¾¼v¡8ñªY9($¸6~\ØFŸô–k\'\Ôš/Ï‡Í³½µf\ÖVJ\ÒÞ\é\àryV¹Ã DóZ\r\îŸB´2Ð—w¹‚w\Ã_\ßk•—¦¥#ð\ß-\Ð.\ÈH=„|9Ž† ò\åu+6\Öò\×\Ç\æ{˜K{nô–i7\Z±\Äùž\Å\Ze\ßI«	Q´Áœõ\Úx_\î\Æ{—.d2@\"\Ìkƒ\ïw‚\Ö#Ä%ò\ÖÇŠSo™å¡›„7&™\Â\È\ã\Þr\í$ˆ\Üð\åE¶f€\ë\ËC§gZ¥q¯¼hƒ.´Ê¥)oþå†­\Ísa{Ã›\×®Ø´¼µ±\â\Íl70½\\nsV\íFÞˆ!K`bi\ç<¸óýwš‹…{…K=\×y\Ë\äK‹Ó‰L£ ¼0\Ø\âP^˜’>™¥¥\"/sƒµ@ý’¶w7bu¹h>x?ýy\Â+\Ö\×ò1\'…\ËI0\ç—S{\çMû-NŸ1\å\ë…7§\ï4…¡S\Þry\0\Õ\È ¢\ÑþÅ€˜s³¸p\Ý<jƒa0XÏ¡cW+Á{ú\àƒR·ˆi–e\Â:œ™\ìakq¹´7Ï™—ùpEa\à¤Ùž»[ˆp¡÷ZSšð–\Ëpd[])zój¡\Ùþ…V\Ä5’šeýü\ÖV<yùr:À\Å31w#ˆ\ÓLŠ\ßo\ÕY&¯’Î›À\êf0\âŒ;Ó©@ˆ\æNø\Íö™EK~\àö\ì	S¼\Ï_.\'hÔ±*p‚i¾!d0Ó !š%\çw‚&u–g¿Œ\å\"ó„/lzó\ZA•i\å\Ñ½PÄ›\ëò6´\Ì^¾¼n€\n@_^;Qz\Ðl\Í\",÷_gJ3\ç½\åò\æ\Õ\Z1O\Ñþi\np4¼\Ï=\ÏSÿ\ï‹·\Ì\Ö\æª÷z}®¼…ÿ´X$\Þ{ï¢¹t\éB*\Ö\Ì*\Z\Öý\äµ^\Ý>ªnŒ%\ÔQtsLMcnØ”z¯¼P8i\ný\'ü\årQ#\Z!$H\ì¦/¯U`À\ßMNxª\ï\"~¬;VxOk+ÅŽ\Ô~k\Ë\Zþ6×—›z¾]D³6b÷\Ï\ZŒúp#ö\å\í#]Ð™\ÖV:g\Î$d^\É\n‰¼š|ÃšÍ©Û…—o4Å©w¼\åòdE##q\Þ\ïÂ—\×*@¾ônm\æ\Î7\ÍÀ…\ç\îFí‘\0š¡14Lˆ»ˆ´»“ú\0	vÒ„v§#} Qà²Ÿ{Á\×sƒ\à¥\Òýf¾ï˜¿LÎ€–\Ñ\è\à”Uišõú\ë›y\Û\â-Á\ìÜ ÀWÎ‡n\ê\ßIÁ·\Ç)°4€\×(!\î!BFy$\Ëe—t‹ \í£ú\ÍB\Å,º>y\Ô\Ç_ô–\ÉšmW–kT O\Ìöš³Ó¯\í#ùQ\ÄH`z#&\Ýnm\ßÍŠE#„¸‹\Õ\Ö\ì¦\åûD\Ø\Z0‡†3/¯\Ó@Ÿi\å2]B½E\×\Ç›\â\Ä+\Þ2yC\Z\ß#\×H\ê˜ÁùH\Ð1\îO³`ž¶\Û5c—\ëY ªDH§„\ÝÌ¼€‘=‹öúòö‘dR\Ý\n\nñ\ìë‚¹„\Î<˜Å¾€\×\Æ™\â\ä\ëž2ùCZB4\é;\Â$\ê#½¸(®Î˜É¹þD¦\Ôn\ßx#ßˆ¬ö\ÓAžü\È5þj9H	\Òy|\Ôe\ì\Ç\ê7Y¬/·0bÒ!+´y¸L§þ\ãK\Ï¬@.÷«É¬Ž\Þ*kz\Ë\åiµ-ƒ®¸N7I´Á(Îš\Â\ê´`ª<lfO›\á\Ù3—-1\êZ¦q\ç\Ót\É;L\ÌüoDy\Âs˜¹{_^V\Ø\Þ\\\íÐ—®‡\ÊQ\ÜX\Ô•¾\æÀš‘´mx¾¦Ú»#L?³}fi0p–a\ë¥\âôi¹MšK}\Å\ík\Íjƒa¸¤ˆ	\Õw\Ï\Ë|+õ\â;}s\î²aƒ=·^8\n\çB¶h’ô,O\\+À8\àþ\ÄF­«}E\Þ<ˆ¢>Šn\ÌIÀH\r-÷ƒjO¢Ã…\ã\à\è\\”¡}\Ð\ê\â\ì\á\È9ºÿ[0¢\ÛV\Ð\r\í\Ý\Ï0s¾º\Û\Äò\ÐSœ9\ë/—# ôÒŒ5\rúm\íÁx\Z\Ú` \Â\á\Ùw½÷½œÀ÷R‹ ¯(¥Id”=%ÿ]\Ø4%¿ðù\È3-,›—]ˆ\×\çÏ§\Å\îñ\Zm\'j\é\ÒP¾ôn‡N‰\Æ\æš-Ô®¯@ˆ,W\ÌÚ‹\Å \Ì\Ä\Í:\áb0÷·¢·åº„I§	:¦?¿À³ú\Òó\Ú^¾Yˆ\Â\ÒL·\\ž@Ÿ‹kJ‹\Ñ4l?õ\å,Ip¸\Øc&“…_t3x·‘2¸\Æ;J¢]Vd2*‹\é:®\Ë.÷aù—+\"¤’¬\àó\ê\ê¡–0Q¤é¸¤f‰8Ú¢‹¬;~\Ö\è\"<k	0\Ð\×\'Ž˜\â\è3\ÞryB\íJ_£Ÿúò\Ò6‰º\Ø7‹\î…hi¡o+@\Ø\Z•%ðöVK´\Ét\æY£ô\â…Ýžñ¹\"B £\Ã\Ê\ÃCˆŠ\Ë\Ék4\íw£\ÈB\àµB\äó=‡\Ìû‹§\Ìû\å‡\Íüù[¼eò„,ú¦Qú}8=KmÌ¯N\íkƒ@|\î÷\ÓnY€<D9PnÀº%\ÓG–™g\Ô)$ú–\Øg\ØõÛ‚=x\Ã&\Ò\Ü¡‚\çÁ—“\×(/Ë—\Þj`RÅƒÔ—\×	\ÈK;\ÖCq\ì9³6~xgž0Çž£\Ìñ0¿\ì\Ëk¾÷•¥68Z\ê3S‹C{î¹\0\r\ïD53_™v~/	Bˆ\Ô7n\Üð\æÆ²¹tq»ú;·DF§µ4—g\ÅF\ßJsH\Ú\è¤>S\ì	B(d\Ñí»½eò€,µl¦F\\óh\Ö\Ú f\Ñ}m°Æ„ýyH\Ó\Õ\n¯h\Å\Ê°w³šìŸ—X\Ç,!j~ŽœT:y\0\ÒIu—m˜f\ï2”Mys( ™c\Î2¾4p\Ð\Úq§\ÏR<?ó†)­¶\Æu?ˆeœ˜Í¿£T7Á\Õ\n¯¨;\Ò, 0Fz¸\Ï\ÂÂ¾2qq9h…Á3\æg—„}\"lfÎ™ò@ nL\Ýn\n#ûËµ´g”CK\Z`PŽ¬\à8K\ÓK£f´\Ø^\âA…\0µNûD\ØZÀG\ï½wIg2#³\í\Ø\'\ï9a\æg‰wk\Î\ÌÆžZÝ°\âI-\äMx£¦\éÁ\ÚJtŠ³Œ‚\'.-<`\Ìòc¦\Ðs³·L»Àœ}+Ú“þŸµI´\ÓI†µO]T\ì\Ç2¶ô\ë6\Íqöž«g>\Åô	¡â±ƒ\ÐÄ„YuµDx\ï\É\æù\çž­\Ðw~¨$\ä©*¬þu²G=\äQ‹\é(\ÍÊj‚\â\ÄË²\Ì\ZZ\á\Ê\È-¦<û¶·\\; \Ú`¾;î“µ68T8gfË­·ºD bŸ\ÛYbÍ— 3qGµÅ§9BTš¿Xš5\ï¼õº\ì•\ß\â\êò‚y\å\å\Ì\Ï?cºE\Òq“æº\'ó8cp\r¶\Ë\Ø\ÞZ“Ö„{ÿý÷„ô>ø\àalÎ¥üòâœ˜[\Ãu\é\ä‘t:bO¿´\ïúòòŒbOSx©ô€)\r\Ü\î-\ÓjÐ–­zÿK\Ësf°Mi \ÕN2`rßgl®\Ø\Þ^Ù‘\Éjw©#\Õ\èž}\æÉ€\ì,ñA`\Ì+òq\È*\'6ý\àÁæšŸÿ\Ì<|\êó³Ÿþ\Ä<ÿ\ìS’ŽIô\î3CC\æšk®1››Bl\î„ñAv[›k\ã{6\å\ÃG\Éý}yŒ<o\ÞG\'™H\é«\áúòòŒ\Â\Ècf}â¨!\Ú\á\Òlûw£o1c_Åˆ\ÕÖ˜¿óEZh•“\è»-ø®³\ìq…gN¿ef¦X\æˆõ,Ë¢q\ÉZ”öxb|\Ø\Üz\ëA9V\íOŽ\×Íª\ìiU–|\Õ\'Æ†\Ì\áÃ·™‹·\ÍO~òK~›2!y\×]\Ç\Ío¼&+\Ç@p*_…\â€s;MC‰„M³ó¨Y‚6o•@l\ÌO3¸ó\å\å…ž\\\Ï\\a©÷zSš\í÷–k˜>az‚\ï[b·0W\Ã\ä£]…	\0\Ùò¨8d\Æ½{04vú\æ\Þ1¥µ9/I¤\Éò°+õI\ÃÏ™j™?\ë!\Ëz\í#\ZWll¬›cÇŽ®½ö\Zó\È\Ã‰yrfz\Ü<ú\È)ó\ê\Ë/˜ƒ7\ß$ÁðPŸ˜8\ßv\Èôöœ‘4„\ãc>,˜žš0÷\Ýw¯yé¥—d¥\ï{O\ÞcÎœy[\Êa6=rø\']\êË‡n$B4ð¼M§´;–‹N,Ž¿$õh…\Ûs\'L¡ÿ¸·\\«°¹¹b¦Ë£\æ\Üô\ë¦\î´)ö˜\É\Å!3»<!f\ÆzX\Ú(Y-odV·\ÊfnyÜ›\î#‰4@}ø2M\Ò\áZQ\Èˆh¹3Kc\Òfs+“Õ¶ÁiG\Ùj\ßs´=¸bii\É|\ï{\ß\Ím{k\Óü\ìg?S\å7\Þ`^\íUóö[o˜ý\èG\âbzð\à\Íf|Ü¾\Ì\Ùis\Ûm·‰YS\ç\ì\ì¬ùÛ¿ý[{Î–(o5>pŸdy\à¦\Í\éw\Þ0w¿Cˆ°07™ŠÖƒ\×7o\Ù\É@\ãÎ»É·S´ñ@»\î\ÜþQ¸\Çl\Í¯8\Î4Å±\ç½\åZÍ­U¯€ƒ\ÑR¯±§Ccý{\Ò\ç°¶/¯ôÎ¾m‰h|WZ³„X\Ï†\ç(Û\0šn\ï\ì[\æüÌ›\æ\Ü\Ì\ëÞ²\á>¶W\ÌLO\n\éa²œ5\×_½Y[[37\ß|³¬\Ò\r\Ù\Ý|\à€~kb\ê¼\ç\îb\æ¼\ãŽ\Ûe\Ù3öwb\Ó\Ãn2#Vc„\0\ï>q\Ü\ÜÿI9~÷ô›V3¼Ûœ?{Z~ƒz{W\ÅE·i…ò<Bò\îˆB£ž¾¼ŽÀÜ°)ö\Ü`>Xz\Ä|°ü¨—\æÚ³,ýó\ïz…w= õ\Î\ívÁks~ejWZ‚/½Œ/˜\á\âyo„¨\æÜ¸ˆcU­/œ12€˜µ\Ìú…•}\Ï\Ñ6áŠ—^|Á’Ö½BP\Ï<ý„9õ\Ðfsc\Õüü\ç?«\×õ\×_gV—K\æº\ë®1\ÅÂ´¤MN\ËLPüG|ú©Ç«\ç¼øÂ³V¡ü\ÆqA\æS0‹*˜§lf®1o\è$bg9O+\à„!«”¤4\àjŠ\ã/V·hB;l\×\Òk´#\ër†…y \ä\Ñ\Ä\Âi\îo¦\Ê#B˜¾¼F‘¹\Ö\ÂÀü!ºòFQž‡û+Ðˆ\Ùª\Çj‰Zžt%\äý¹\Â\Ö\â\nLŸ\Ã\ÃBVÇŽ1\ç¬æ†¦w³\Õð^}\åE3\Ø\Þ|ó›\ß0\Åùi	ƒÀCô¾{\ï‘yB\ÎÁ\Åú\ÜÌ¸„J†\éS\ÈÏ¦CTY‘®\ÝJ\Ñi\Z.õ\Í\ë@D\æ[#¼Ž;	…þ»\Ìö\ì	!CY{\âeo¹,Á;nÔ“A\ïšOXCŒ‚{^\Z€Œ|š-\Ú\é»Ö£52Š‰­\rM\'Ž6\\C\æû–w\æû\Ðny.w¾/\ÐðVMÁ\æ-m,˜¹ú\ZuXKN{sÑ8B\Ó’ÿxˆ•\æ\ÌC\Þož{ö)1oŽ÷K\Þ\Ô\ÄHõ-9-´<\Õüv\æeZ°\Úi\äQ¶\n\Èkq”ÁZ\á\Ë\ë(\Ìˆ\ç(Dˆ\'iñüMþrÁ\î\n\é$€\Ð\nk\Í†\á@$ýó§½ù\0’\Ï=R7\îÉ‘\éQG4`\ÈRD3%¶²\ì›;-DÁ×š\ïJ\ä‹C±b#©ƒ—¨÷	1s\\aA&Œž\Ù\ã‰X1q=¯„O\0H.\ØO<\Ê[_„/–\å:„QhT³J†<³}L¬´\ÂW>k0\ê\îD\íV\ÛÐ—\×NÐ—\ÓðN\ÎŠcÏš\Õ\Ñ[„7§\ï0…Áû½\å²D3Di,n\ê\Î¢™Q*fQ%“´\à^g\Z)õ\ì\ÊO\ë%^Ž!\Òñ\Î?n­\Â>!fÙ†)Jø·ƒØ’BvkX\Ú=W©º9g	ó\â\Å-³½µn¶¶Ö„\Ü\Ñ^!w}NÊµkù¶Nv\îh\×\à¡\Ô,\ï\Ë\ëD\Ì÷\Ýn.\Ì\ß#d¸4xƒ)N¶8\Ð~m\Ö+œ\ã‚óUû\Òðõ\à„\0ø=o\Ó\Ãó‰ã‹Y\ÒMkJ„üO{Ò…\Þ°\Ó\Ï\ì\æû€W)\ÎE¾<û„˜>ja§€ú\éAx\0CŽ.\ÉI9K†ü?/é¤…	µ• þ\îª>„p{\ætºjõ¡™³\Ð{\á{‹µ|Q\î\Ík¢AA^\ÄÁœš\ë\ìh}\ç,1p¼°6gV·—\Ä$.\çR!†Ñ——2og¯G\Ý}ùi\0bŸ,\íJs‰±\â–û„˜\"\Ì\é<O\\ \é\ÅqŒP\áˆ\àV­‘\ã<\Ì%¡¹ú\Ò;˜F\ã´+!\í\Ù$(Œ>i\Ö\Æn2Ü˜<f\nC§¼\å²@Áj4^\Ï\ì›B\"!DVX‚D€+ø½¾½\"fNHb€\0\ç¡\×f\Ì\êÖ’WÀ‡!&\Ó&L³.¸õñ\å¥_]³ B\æ(ù¿Oˆ\ÍCˆ°\ÓG\Ïq54>L¥	Y=\'O\ä“G­*.dp‘3\"\ï\äö¬‰;\Ì\ÅÂ½B†‹}­\ÛÀw®<¶Ç”ˆ¹³Ÿ›¦@ £\Å\rÌ¿+ó^ºj\Ì\ÊVY®3½4&¤\Ç\ì\É|\á\Úör*¦\Ì`¾nÀ›—(ø\È,.‘\Ç%BHkºÞ¼û„\Ø8„eD\ß!kHúÀ|P³\"ó‰„† /\çh\Õ\Ú\ç_^§ oV…n%Â…\Ùófi\à!\ÂÕ±C»\æ\n³œ*\r\ì!(¡7ZÁ¥š^™\Ós£\ïÌ¹•`	7´J7.™\Ô\ä—ljB… ñ2%\ÌB\Ó1û<]	·\Ïú¤n”e0ÁWË…÷c“Aˆ³V\'/G%DR\Ãs‘gC0\âù\Ê\î¾2\í„\í7\ã\åmqƒÀ‰ª»–\àS,ô[5m\Í\Þe\nÃJšl‚mû\æ›\Ã\çDa\É\n).¦\æûMii\Ê,¯«XX›7+›‹{±û[\æ‡&N‡4†*mS«šNIƒ\ìp¦ð ”p¼a\Z’\\‡ðHO\ëÅ¹\0³.š.dˆ“–\"k\r\Çð\å¹\à>¬\ç\ê\ËÃ½ŸÆ¼¯%\Ö\Æ\ÄÃ¿$\"D€A\áB„\0Ž0\ê-\ê†D¸+ \é8\ç\å>G‚ÁH¾´ZÚµ<Ÿ“¢8pÜ¼·xJœfð&\Å\Ó\Ù\íC˜ÿùLy\Ô~~4šk„<$ª˜\â wÓ¢„~Ñ¸\é\Ä\Õ!ô9\æ:,/F>±~Z&´\ÈÔ——Qõƒr\ÌsúŽ\ïs¯¡Ç´_Tø\ä¦\Ï[„s\Ä]\ÅÇ­ƒ²‡û„\è$h†ÿ}\ÅYÆ’CšKŸµ8Ì°¾„D\È\æ¿~7\"¤¾¾˜\ÌND\Þ\Ý\ç!\Ü\r(?b¶­6ˆVX\ì¹1²Ý‰£¬÷ü.B4À \nFJ\Üt÷w\Ô9a\Ä-Wsö9\ÔÑ¤j\Ý\Ëh¹zu«—\é˜\àû¸\ÛP¹×«G„Š}BÜ*ò¡tòü g8?y›\í&A\ÇgÁ\Ú\ÑmdXœxÍ¬\",\\o\æ¼\å\0ý½Öš°.\0¥q…‰³¥/\æ\rm&J£	¿\Õ\ÙÍ«\Ö*-\n\Êo\çË‹\æ:\ãm\åa\ê>¤‰A°n™08‡yB\ÖE;\ä·_ˆÄ\Ùt\É8.*ö	1@•;z…~Á<g¼Nò\"EH\ç\ÌÛ²\äU»%<&o\ÚjS°\ÄW\îb\n\×\Æo³\ÄøŠ¿\\µú»K„8yD\Åô·S‚ð™\è\á4,o¶;¶nq½P%S¼Gk™E\Ô\Ñçˆ“˜e\ÃuñA\È>\â^\îsr\ì4sŸ÷ ]‰ŽFœf\Æú¼5Š¨6\Ã-—”—;!\î2v\â\n»¼!;8t\Ë2`v›\"«´Ó™FcF}y\Ö…·\ç\î6…\á\Úñ„N¢–\ïs·B\Ï-ûW@A\èº\ZB\Þ”%\Ä\Ñv\ÂN h4ªÕ \Æ%€fÁs@P¾¼0|u:?û†˜/\Ñ,™O]\Û^1[—6¤,@£#\Âu\Û,\îó\Å)‡\æ8µ´3\ç\Ø(*.WB\ìN\"Œt\Ì!„1‰\"Xfm9\à\Ý$˜:‘µj\ç“ù:›\×N§,L„Ý¢\Ï÷–½\n/\ï3‹cWBˆ¢UQ}\Í%B426‘U\îg´747L{¤¡Õ ý`6eµ<E£B(\ïþ\æZÜt´PÑ‰\Ë\Z\áúø0\\<g–7…\ì(„¼-‰²\ë<Äƒù”t~»\çú®\Ïs\ÆY\Ñ&N\Ý\Âeš%B\Å\åFˆ]I„›\ëË²Á0½œh«KCP=\åHo§PV\ïV_^\'ƒv\Ý\ÜX‰4‘’\ïKo˜3\ë\ç¤ùóÁ>…¬?ZzHÄ¢,\"Á‚õ‰O@¸+\Æ\Þ1¿ya&]XŸ\â\Ât¨\ÛR€&DY\0i\è1fA÷š¤ñ­©\ÖJ/zb\n!Y>\åk™\ã@\ï‰\ÒIi]„‹ð¬µ\rPòV\ÍVA›0o\è¦iÜ¡›\æ\çúÞƒ‚|]\Ð[‘* DúF·“bW!n\ä„Møò% v\n\åvBV¨\ç©\Ì#¶o^:\ÐL;_+,ö8±„#K\Z\Ú¸ŽcüŽ2K#\ìÔ¹Áˆ\03\Ü\èB}—þ¨ó\ä\å:\×@º\æh-@°˜!#‚\ì!„¾®ÇŒ·\Ì÷ñ¬hC=u\ÍQ\\ \'_\Üc˜Ü¨/&a\ÎnHE¸,$\è:\È-Ê¡Íš6‚\ì\ÑBk\Íar\ïp~\ÚDè¢›µÄ®$BPož\Ð\Ýå¡\ÚA·!€\ìji»\í~ö®hû\Ê…›S·\Ë6Mnž;m°\ÆóŽ\Í;? E™&\Ñ£´\"$Áu|y\nòuu‚\Ë\Þ\á2aP§°f 6H2œ9D¬J£€„qö\ÑkA¤Oø|Rß¹·,9\íh‹q¸Ze˜\å\Ãih\ÒJ\äzM\Î\å7!{Khõ–žm\ÜXØ¶ÈŠ\ÝHˆ]K„h„µ¶RBH»\á\íŠ\Ýbž«\Ú5JA#k§\ãL\Ç\áÜ°¬3º\ã5ú\êž2e´C;(¬õÀ\Ð|°j\Ê\ÚÖ’—p€OÈ‡Q+\Ô@vˆ‰u\Ý\Å1\Çúò–6„¤•P\0\ÏB=j™dT¯¾\\›}ky³¢•bö\Å%B a&k­õ‹&a\ê\Ö&}Ð¶ä™£¼}³@7b\×a\à¸\ì\Í\á}\ê\Ú!;^Ç€\ÏD*óX6­Ý«\á4\Ó\É‘\âô³<\Ì.0Å™s\Þr«\Ëó\æÂ…Možb\Ôjfh\Å\Z±k.yEAM\n\Õx0G2ˆ¦†“My£X%Ì‘õ®æ¨¦\Û0p\ìÁ\Ô\éË«\î]< +×”Ž„È•\Ø\Üó>-9ùƒz\í\\ó®/?kt!v%\"\Ü´\ësŒ¯Œ»\Ö\èö\ÖZ\ËWÔ„q\Þñ\åuhß­yfÞ‹;Ë»\"|\Ä-\ß*4;O\Øn¶8ñ²Y?,D¸\Ø{­·O\Ò:‹GŒ[’A;\Ò%\ÎÂ€¸Ð¼\Â\éar\"\Íýv	¹B„-¤\Ã9 K\äA,\î¹.ð²dÿ\Ãp:D‹ypi³$±y\áü8ˆCJ\r\åx>ƒ\Â1€\á\ç#œÓO{£\Þu\Úr=­¶\èdB\ì:\"DÈ²¶¢þ\Æü\ÖH\ÐD\Ø}ó¨¦h\á2Yƒ:vúrvq ^º\ËE!<–¿\Û\å¼aÛv\à?\Z¼{^« \ïÿII­\Þ<t\ÖÀ9fk\æ¸a©÷&!öpúø\Å[{\ÒÃ˜\\­2\n¯§	˜‡ó™Ã‚:L*û(ó$\éžÏ¹Qó[\Ä6²à·›Fyõ¾\ä<\È:j~\ÓžA›ó\å+\Ðq\\ñ\å8±2\ÜA\ë–\Ö2µ*¢®‹©Õ³d@\à+\×jL\ÌöxûW\Þ‚® B„™\ÏQ½\èt\ÍEeHG0ë±¬:bóZ%¹—/½›€v%2_E;ÿ5¯]\í\á\Þb‹K†|»\È[Q¸\Ç\\,\Þ/q„Å¾£ò,‚\ÊÀð=ðê™€§‡«ZšÏ‹3Lp\Õô•\Ý\éaÁ]‹š¢{\r\Î÷-±\Æu0R\rqpµ \È\n2PoN÷\\Â©„<õ‚  %ýÍ³s/l\ZL:ƒ\0\â£¼SÙ…>Žƒ\n\×\ÒcÚºž#Ðº²úŽ«•rnœX\ÄV£5\Â\â\äk²V/ž\×O„|ô®`\rM\Ä\ÝÁ>{…C\Ü]\î›÷÷¥7„]=A\×jÐ†>‰t4„³›.«\ÏXî¦µq\É0\Í÷\×(\æ{™–•`ú\Â\à½\Õô½ša\íP\0I 	º\Â\ÚET:\ä\ãjŠ\árõˆˆ“G\Å\ì	¡ù\î¥\×!Ç•¡\Â^S ž\Çük\âEK„p\ë\í¨s•\ß\ìÊ„=gg»$H\r\Ì-¯ð\Õ\×dL$\éóv\rc\Âj\ê¬P“D\Óm\':Y¸žuz\ß_<%–•Ž&B>ò¸fFsV¾9Lxj.\Íz\î\'m³Z’6h\ê	\Þ`u—`‹ ]	¥\Þ9­B½÷ù=\Î\ÐY£\ØS	˜¾\ÓFŸô–Q\Ô\ëoZ4#_ 7šy\átÀy®—iXsŒC„\02Q\r\Ò\n;~@\ÔË›B\n>¸„„cN\\‚ò:\07\Íw=q‚\ãZ£\ë˜¥eˆ—û±0»¶k\Þ\Ñ1D8\Óc\æ{šµ\Ê;X½5¿D¨†QÁ\×\ä\Õ\Ò}ÀQ\Æ5\Ùu3\×\ßY\å,®Ÿu\ã\"IX\ÈbqR–ºÃ«‘yÛ¤\ï2+Ð–>2¬\×2XS]p;´C½õÂ‰ žÂšŸ8 ²¨\Ð\ä\Ü ô0Y¸D\È1¤\éÂ‚ 0]2‡÷\ç\ÂzAÌ h~\×\Ä4ˆ†VË¡&|\ïf\ÈR\Æ!\ÆM_\ÄdŠ9–g$_M—Q÷u\Ó}eh\Òu Ç«5Oðõ­¼¡0ò˜Y\è½VVc\âû¹Tz@œ\ÍL\æ’8xb\êQ§­ð\\[i\Ì\ãP4{\rý\ír–ÞŒ\áû¥,®\Ù’Ôƒ²,w\Ç1óµ\î{Fx\ën;\à{Ò²¶\ÄAq\ê-³:r‹|\ÈKƒ7š\Òl¯·œ“4m\ë\ËðžOH!§\â9Ñˆ˜Cƒ \ÆhLzAùiñÖ„° „µ\íeYxZó \æ\ÙH\ç7dÁ®õ\n<\'qpA£\ÂQG\Ã+Ð‚øOyu(\á|÷¿¾¼F\Érkz\ÌeºK\ÇQw\í-\ìH9ú¼\\!8ÈŽc·^j\æ\î¢q5\ê<\0¯Q_\ß\Ê\ný\'\Ìje\ë2°9}‡)ô´\ß\Ó;’\ßv\"TS‚F…!,¾²\0f£$¨PmsscyY\Ñ\'\ÓD\Z\×G¸‰×¥½ˆ\ni%2«¿vª\Ì!\Ú:\×\"´/5Uûò“‚k%1kû\êoq¶\ÊÁ<&`™©\Û\åc.õ^\ç-F¸\ì\Ñg\ÄD}aE¼2g—ƒµ!@ˆ\ÍmbB€#ð\ã˜&\ã\0¢a%›pº^2Q‡M«u`¾r¯\æ\Z\ÕP@8h\\<\'/¢\Û\ë\álÑ´cVM\Í%©z <+Ä¸i\\‡zr\Ìý¨/\äGY_\ÝA-jžg\"$\ÆvþüA!>¾™÷4K7˜\Â\à}»Êµ!3>P\×ybª\'$\ÓŽ%«m2g¨¿ˆyW¹”±¶R¬Î\Åm¡¤ Áž\í\ÄE½÷‚v/;R$ ž‘\ë2@’PK¶Ì‡rŽ4¤£\Öuh_Ìµ”ûÊµ…¡Í…ù“òQ3¢õ•	´öú8\í©\Ï\ïl\0\Í(¼že’\nlÀGª\Âa	\çõ\ÔT¬3Wk£]\Èp}{\'ØŸº¢Ù¢\éB„¢’ù¿š$17JF¾ó4\ró/¡ª!Fúð\\´—j\ÈyD^‰x\ÛR\Ïõ\æbñ^ù^Ø²/\Ñ\Ò\Ô\Ûò-¸\Ü\Ó\"„\Ü\àjž(!ZK[L\nF\ÅE\Z%ª>iƒûÕ›G“\ÑzEp!Àó°eT-0Ep˜™\\¶/\Ù ƒùav\nÙž6K\Ï>n\Ö\'†ew‚ôy_#\Ö\0®ë¶±*tÀ!ÿm}yO”\ç\äA\ëf°¶0x—y¿|JF¶…þ\ã\Þr\n6ª\æy¢L£88ø x¼ž V A)a\ÅATY¼>\ÑH\Õ\' \Å`nS!„>ˆ9Ó’¡/ ª6VÔ‹ûrO\È1\É3…i‡½C1©¢e£\í2\ï\Éõk\Õm]\ç!£´\Æ<`xö]oÿj\'\nÃŠ\æGˆ$¸>qTœd\Ø\ÔZË¨\à¸-D\È\Í}AÀ\0¡\ã\Óv‚]\Æ\Ó7O6Ñˆ–\Ñ( úò\0mƒiÎ——W é…½)U›«Gú>@nœ»Qœ5ó7þ\Äý\Æ/›\éo|ÍŒ~ò·\Í\è\'>b¦¾þUS¸ù\ZS~\æÑ¦\É1 ñ€¨\ÑüT[,;\Ä\ÍGÃµõw\Öp PP\ÇB\ß1K„›K¥û%ž0\\FAýõ#B-\"LJhWqÏ‰*‡°‡° aŽ¹¦\ë9Y\ï<_žr«Š@!DK¨…\Æ&1‡ú@ <\Ñ\ê¬6\èó…ø¹W8ò\Ö@ü¸\í\Ú.\ä\Ãó+\Ã7›\Â\àýÞ²\ZG\Þr\"„\0k} :_\èŽÀEX\Õù¨×\"\å¬Póù3z\Î,A\Ñöø\Ïû«Ž´¶«\ÌZa¿¹jþm7˜\áþg3÷\ãïšµñ!³Yœ1\ÊE!>\"„!\Æ4\ÉQ\Û¯Ö°\È \r-\ËM\Ë\nÒ– ¡ª\×*¦>n\Ù~ÉŽz}çŠ™¿òµ&\ÌwõÎ«EZñõ\É\Z$¤¦RÍ‡¨\Ðõ7\×Bc\âžq\êª†\é\×`.\'Ÿ(oÙ¤@«‹\ãô‚9²\Ô\Ý-\\\Ój#ï¢•\ÈËª2¬Áž\\\ì»\ÆGŸñ–w\Ñ6Ð—\î-CM:Aùl\È\n\éÚŠ[Z\Ï§mò„`nu÷RuI5l®aA^¥;	±\Í|\ï[fµÿ¬\àê¬½\æxŸ)O˜•\éaû{Ü¬&%o³4›*9\ê*8hƒ¾ùbž5\ëyd\à¶\'õd}\Úõ•yg×‰Ã¦8ñÊ®s€8ö\Ä\ìCQD¡5êºiQ	Å—\æ‡)‘ódqúõ=ól˜\ÝUb Ì¨¹Á( mú¼:&\Þy{]f|ùIA&mG\ÂH0ŸÎ¯\ì_Þ‰01„\ÝB\ïu²\Èß‡\Î\'\ßô–#·D\Ä\\ua³JˆY!ª> \ÕP}ù¢\Öõ¸Ÿ/=¯P\'_^=`–`‚Z¸\ï¸ûo¿o¦ÿþo\ÌÊ»oÁ­ÎŽ™òDŸ%Á\ÞH\Ä&\ÇO\Æ\'\Çõµ³½½\î­3\ày!o_^Z \ï‡:1\ê„N\Ì\ì‰\'uŒ\"B„q\Ø\ë1	t\×\ß.òQó]£\Îª\à\×ÿh\äC”hXºJK£€@\Ã¨‰’p(3f@\Ú\Í\í\Äùc¥þ†%­B{‰pDL¡+•\"PœÝ™¬‡¶!&§8ž“\"pLVXúòÓ‚+<\\òSM1‰p‰ƒ¨\ë1Ï¶Zcþ0`Ž5©¶Ì“–„„½ßŒ\á3f\êÿø²Y~\í…œ\ì÷_4KŽx¿ÿÞ¥ššc\Ð7³›\ËE#u\ç(Aaô\éª\Ùo87\Ðo“šùGf\ßÝƒòÚ¼7=)\Êk3U\ZÚ“\æþ;gfF\Ì\ÊÆ¢™/O\Ù\áQ	)V·²i„Xo¡\ì¸\àú\Ç#š¦›\Ï}Ý¥Ö’€Au÷\å%\Ú!ñšnHGÞNZ;b\nC˜\â\Øó¦8\í™{œ\í—Ø¿\âøK²¼`\á\ìÏª\Ð3w\Î\0\Ï\ê=\ç\ÕA•ù°ƒQ\è\ÞBióS=\Ç\ê\Ã<‡ó\Ó\×ß´Z‚\Äg&MSKc¾“¹&_&7„ošž±Y‚v+—\âkF8\Ð(–Ÿ}\ÌL|ùO\Ì\äW¯4K\Ï?\à\ÜxSX‰\Èñ“¿Ks\Ô“\ïY›…Ì¥‡¾G¦Ÿ–3ó=·\î\Ê¾—tBj\Z\é\ïh£\â‘kÛ…oH\æ‰mý(\Ñv\Ôð[\É\ç[ Ÿs³§…„¢´>\æø\Ümš÷Y\Ú(\ÉÜ¤/\Í0*d#\n5\×¾üF¡\Z&ˆªo»\à\ëIÁ ocò¨y\éa	Ú˜<&N.¬S8@Ìœ³>\è\Ê\ÈAûM‘bXsÜž=a‡7x§\â@ˆ¼ºTLmÅœ÷ñ¥ò”1‹\"|\Âe\Òf®Z\æW>Ø´j\ê\ÝK\ÉB…E^=H\ÝwT?\äÀ[~õ93õ7ÿÃŒÿ\Ùg\Í\â\ã\n­	x	,K\ì\ãX•·\ç­\Öe\ë\ã’c9G\'\Ä#Jsl\áoò«.¶mI\Ñ\ÍK\ÜR[Zœ\ïS\ä\Ïii¿ƒ<\Ê K\è\ã)\'ÿfúfÞ®I\"h[a\í­p¯Z§\ä«KP¾\ÙÐ‹zÀ£”½\Ù?ñ…¾÷˜\Åñ\Ä\ìÏ²gj\âŒ˜˜\ï»}WhDRÒ‘\ÝD5ia—ø4¾§›\î\nX~g=G¢\ê0W\Æ1\å\Æ\ë\n!\Ä+€|õ]€°©¬] .q\Ú-úËˆÿô\ëfú[e\Æ>÷q³ðÀ‰€\0\ç\'\Ì\Òä —¤\Ú\È0LŽ^Í±BŽ\ÓW_eŠ·\\·Ws´\ÄÐ¬†¶§OVÛ¾ÃŽ ŸÚ—\"\Ôb\ÒO!6d/¤#m\Ô#Bv¢ibn\Ý\\\Üó\ç‚ú\Ä!\ÊõÎ½]‰ý«·ˆiX=.1/ö\Ï\Ä÷T\å^\Zó\È1sŠ¾9Ù¬‘f0=Za¡ÿ.S\è	4Á\å¡f\Ýj‰h~xI£1²@¶®úþ\âÃ²\îni\à.\ïõ’\à\n\Õ}™˜Hð\Þô\å5´W\ÛQ-È§e—µV\è\n\Èb2_cGûú»Qð´\'!\Ã=“´¯†(øòZî¦\à\Ë\ÛÁ˜<„°\Ú{\Æ\Ì|÷fôS¿cJ\'Ž¤2?i–¦†¼D\ÔN ™®\Ì\ïI÷iŽ.9–n¹~\æX~\â!ô4C†|—\Õod¦G\æ?\0Œœ\ãz\Ãu\Z†g\ë¯Z/?	 U\ÈPe|e\0yµ¼I¹Žn\ÙT‹]ò‹B=R„ðÔ±H)ªG-÷Oj\ÒmY®*C8šba\è!qˆ)¿\Æ\\,«\Ä@†b\n|=™x\Å\Æú²7d-t¹>\ÚV \\k›\0!£,ëƒ‰K\ç| ]wž“q³Z©\Î-…\Óm\ë=\ã˜_³l\ï¢j«´‰¢žVJ>ž¤k£fö_ÿÉŒü—ÿŸ)9`¶l?\Û(L™eK(a¢\É¨\ÛFqÚ›†KŽÊ¥=\ä8ü»ÿ©i2\ä<m÷•\â\Ù\ê\Ö1\å>;\nžòž\Ó\éP\"D³ò	^\0i5\ê\È\â\Ç\×	…¹·Z\Ú&õò\ÍO’Îª8\Ä)\âq\Î`R§\Ês^˜©c-\ÏYH³)\å¨\íÕ¬§mZL\ÏÞ\î*1h†ó½‡Mi¶OòQ0°b„\ÏK‚+X‡\ÐGBÀ4\ç;Â€X.l³‹\ÄÞ¥«¨OUø:‚8K\Ó õpŸ70‡¦a\ÌCƒ\æ%…²:ó®ÿeŸµ^9— ³\0¤\Ï\Ê7I…·®d²>;a\æ®ù¡þ\Í_1…?3›‹Vóµ\ä’gt3\rð\å…Ás\æù­\ä¸\á¿ñ’—™/\å]ó?h;¬“ò!Ö½ƒ!,Ë–.®¼c\Ìz°«öN~`½ \ï\î5]\Ö\Ê\Ë\' Bc‹Ò¬X§´a\Å!	\\\ß\r\È\ì¸Aˆù>S#e\ÜtLª\r\ÚX8X?Žö®–\çÙ©§Cðü\æ<\â(uAó45²Œ\à\Ú\ØmB€À¿JLý\r¨\ë!˜#\\+\ï\"¤€t²\ÙGM‚Jj¾hö¡\â‚û\èvO·\Û\Ñòüµ\ê¹\Å\'p\Ñ#B(jIM\Õ<›h¸öâ¨Œ®œA=È \ÅöŸ\ÍrA\æÏ†\ëW\Í\Ü\Ï`\ÖgÆ­–4\í55\æ…xÄ‰·<\Ñ/&Uw®“´(2¤}\ßxù‰\ê÷F+\ÚóI›³÷ôY \És¯\ßg¾y\Õ\ï›6Lq\à\Îj$\Çù\ç\ß}\Åö³\ÝV‹ZyyD¨B\'•¨ºfˆù¾¨ó\Ùn‰ÿ\Zž$\Ç\îŽh]¤„:aÿAúi’Ÿ\â\Ò\ê¼œ‹öœe\áð¨tt^‘\ç\ç9jÍ\ÆAV\ÏZœ>-Þ¢\Z\Zq‰Ubz¯1\Å1ÿ*1È·8µ(T\Ã\'\\§Œzf\ÊFÁG\Ðt\Ó\îýø	‡!B¿bš\Äó\ïÒ¥\íj\0]µ?ˆ•ó_#\n¼\Îm¤-©W¸\0\í·mºúJlú¬\á².\ê\å+\Ð0¨\'\Î!Å£7›‘\ßû™ý\á?šµ‘~!ÀÕ™‘]¤\Ñi€\Ñ\î|y\n@+´\ßOEC$/Š\égø\á_÷+\ï\Ãþ¿\êÊ˜÷ª\Ê g\ïÇ¼¼0i\Þ|öV9\ï\â\ÊYKº\ÏVó0­?÷\ÄI¹÷¼Zyy…K„\0a\íþV \Í5\Z«uM•‡\ÈüŸ¦GHd”­f™!ù…¡ƒô¨¹Dê´°nÉ²Žƒù<‹‚d­U_\Ù(dñ\Ü\Ä\â,\Ãzº Ž2Åž›Lq\êmoy –¬„J‚‹*„ZV$h~!«\ÂW\Ê\ÄÄ\Î=\0B‰•k‚ô±]\Û\íˆ	«Ï—´N:\ï×Œ9\Ê\readø\rÁù\Ê+ô<\ìŽù\ÍÍ¯M†µò¼\'\æò\ãW~\Ú\Ì|\çj³\Úó®Ì“\ÉrhuVƒiZ\Z\ä#„c‰ª\nû{9ç›µ9;˜°\×wc1›’Æº§›E]ýf\'}~¢Z^\Épõ\ÍWªd\È\\´!€\0\Ç,‚0„‚¼;Ú™\ß\ë\åQóÎ‹\Ç\ä¼\Í\Ò\ë¶l°\å‚\ï*¸¹]\ç÷Nz6Noi#L„h[h1n\ZBg\äI\Ã\Â\×sA¾/=Ì¡[\×M±œ\ÝôQø\Æ/V¦•1¢’\"N2 4´Užr^D\Ö]\Ëg  <ÿ\ã,5\Ç}}ul\Ã\ã-ª¦P\æÅƒÐˆús\âqdWªD\È#«UMD;©a–\ã…\Â\æ|°¾ü´ \Z©m,W…v\á®\Ç.’4°h\Ö)\Î\ãq=iŸŠöU\rH¶÷¨þ\×c‹8%\×§C\à>‡\ÔG´À\Ã7™‘ý\ïfñ‘û\ÄI$L\Í\0RS\ÂQ-oMH6*\Þ\Â!Ã—\×x&\êÁµ©\ÏI:u„”\Ã\å\Éw=b9Ÿ9Äµ·_2\Ü\Í\Â @´5Ì—JhWõ“b&\Ýùý	ó\Æ3‡Ì™\×\î–ó~ü_ÿ_üô¯‰É“2”\ç<Žøó«?/ù»y¾÷™7„‰\ÒB(s\Ì\Z¡iWx\æ½H¯·P6óx˜/OQ\Ë\ä\Ê}V­6¶\ìS\é«+À7\ÎÀX½U9À€g\É,-Íš\Ò\Ú\ìžy@7 m\Z’«õ¬©†f€¨ þÔˆ\Ð^g¾\ï˜!PdÑˆ¥Á›,1žò—÷\0¥\ïË«‡*\â’\Õ\Ë\åù\Ò]´j!\ãpCA,©E\Þ{—.Èˆ\Ú\ÍAG«ÿtFÁ¤	™\ÓôhzIÁ3…¯#X„\Æ8ó\Ø\ç4S_ÿŠ€c<A—\"\È)	Ð¢”ô2I_È¹¾ô4\0Ù‡M¥˜€\Ýß¬‹\ê«d¸e\Ï\ß8ý†Y¼ý\Ù}\Ço’ÿ£Cg¥]e\Â\ßþW\"”A¿¿ü1ó\î+\ÇÍ™\×\ï‘ò\ç\ßzB\Ò¹ï°\Ça\"$]¯\Û\éD\Äúø\Ìw,\ÖM\Zd8@˜}y\n4¦ðœ™¬³<%\äW\ëûh\Äò\í†\Ó\Ýo§+”š5\Ì\á–ñŒu\×w\Å#3o\Ó2]òNÁ\0„¹gmÍ´Kqò\rq\0Ûž;!$È‚¥\Þ\ë$t\ÂW¾|m»L£^$\n\ÍÌ“e… \àyg\É\'4õe”Ž\Çd˜µ;\0¸\é.¸V\Z±†õ u±š›//	\ÔDì¦…+Ê´\Û\â#÷Šˆ6¨Z`X\è7­Ë——r½EºÚ¨˜8]³¬ý¿µ0oV*s¡8Ë¥«f¸=3)„v\Õ\ç>l¾÷\Í/š\ï^}¥”˜¾\í—\×K\ç\Ì7¿ú{B‚o«i\Ôö\Ñ@(\Ähµ\Â0Ž!w\ZN\Ì\î\Õ<t??Ž\âa~ûLœ\ê\ã†E\Ä]\é…\ÐuÎ™[ò\ã›H\ã{k()*¯|ù5±˜;!EþƒÐ“8m\â\"\\rÅŠD?[[©lÀ\í\09ZOöFž ø÷Üœ¾\Ó\Ì÷”B_ùz@¡jd®p²Þ¦û» \Ñ¾\0ùv@½Uù¯\ÄM5c¯fhµ\Âð\ÈG\ç\ÜZ5R„lUÿ\Ã`\Ô\è^‹\çpó«16}öG\ß1\ãW~J\æÑ„\Ò\n‡9?K2¾¼¤À$™…VAx¾Upb´\Äã¾\á{›\ëB`S·^oJg\ß2ú™ÿh\î¹ý:!*\á\ãfyö\rsa\é´ù\ÖU`N¿t‡yý\Ù\n\Úo”\Ñ?eù\r\ÑE!\ï¶ˆ°“»»´\ZBÁ|aÌ‰‘§$\Zµ-”‹Ò²}—›Án*­rØ«d\n\ïú¤¥L\Ì\Í™©Š‰y}{5ö.\ì©+\ê\ÓS•» LP¬\"\ÃJ1:\Èñ\â¡{\Ú,i{U‰0p\Ù*]±;£\É9µz\à\ã\ãü<t$¼„zdå’ŒK†w_°\ä\í¢\åÅ¤\Úbƒ‰2M¶>\ï\Ùmò\Ûuˆ	ŠÇ$J\ëI\nK\Ò\Ò*\â\É)ûúó“‚ú\Õò…\0Wf\ã{\Èn­,\nmMO˜\â5ÿjzžy\Ø|öc¿dzÎ¼*}\é\ê¯üy\äÄ¿š‹+\ç\ÌZ\é´%\Ê_1o<s›%´\Ç\ä¼ˆ×–Cü\ìGIŽ/\"\ÌmApnšš3\ì>\rqa½`\Ê%)\ã[mel¾Ç”WaÝ¶•!?Y\r°™\Û#\ÔCW£©·\re\ÂiIW•QYƒ¶„F\Ü)ˆ6ˆVXy<\ä92¶Q$½F•1û…\ã\Çü\Â\è6Ê‘†)gw\ÙÀ«1+‡›fgŽgr\çø <Iyþ`8£h\\\æB÷üV\0B§¾¼F \Z-^hú\Ñ!v;\Ä\ÜÌ‘¥Á¼b\Z\æP\ÄD™\Ò.õ4½8š ‹\å¥!´‹«f\å\íWe\Îð\Ñ\Ã\×X\Âû5S˜xÝ¼ðè–Q4Á¯]ùæ‹ŸúeóÆ‹§„Ð¾h\Ë|\ãªO	±A‚\Ì\Ò7}DHa0\ÓMDtŸ>ßž†¤‡‰RAyö ¤[A-®Žs´KX–\å˜À³ö9\0\â\\\Å\á(*\ä\"\r\"”A|±Ç”z¯\ß\Ù@wöDe\Ý7ª\ådšÀ¾÷Ü¤À™\ä\ZU\"Œ{\æ²eûiy~\ËòdMV<k\Ä!B \å˜\æ™ }Íƒ\Ôk“\ßq´\Ì4ÖžU<Á\Éÿ¬b\\$!H\r\çˆX¶Qš·¼\â\É\é+\Ò Y\î\Ëü /O‘\ä9F^²ù¸yý…‡Í…U;¨š2+o>m\ÉðVóÖ©£fv\âŒ1\Ûcfvôyó\Öó‡MaüU\Óó\îK¦d\ï±Pœ1}\çß´ýo[\ÈMaxg««)\é€t„\æ¹ý8Ï¨G„@Í¢¾`ð\é¥ X<œŽ\Ðvc\Þò2eS­”«b\ÙJk3\âƒ\Ù\Ç$L¢c»Í¢ \éª2\Åñ—\Íb\ïµ\æý\ÅSB‚xˆ.\Ým\ß\ÅÞ²\Z+NOu\äñ\å…!Dˆ@O:!,Z‘ýð ^–’VXarÕ—\ç\"³j\ÜÙ…	Pcóh\'÷:,>\Ýjs\nm\îKo˜a.\\\Ø0‹g\ã\ã\"X\Æl·v	\ép/ˆe,¡©	•Uj˜§‹cš•uCqp‰¹\\šœOl¤8\ÇT®¥ÐºùB(¢P;g\Ê\ãgMy\èY³\Ðs§Y\í?f.\Þ6[\ï>m¦¾ô9³ñò)sqµÏ”G4e+d\è‹hvh,À#\0i|«Xk‚ong5ùo<\ç1`õ½÷¼!*€Ã«¨U\â\Ý‚V¼§‰¸r+m\ÌF\Ì\ä\Â`54\Åm_E\"\ì»\Æ|°\ì¸:rKu©4\ß3.Ù·µ9\Ë2<Šù>Xg›{!Â¤\r!ðA*có¡\Æ1/Ä½\å\Òþp\ã\Þ\Û\rªW¼xa\Ó>_ ùñr\\-0i\Û5‹,\î\'¦P; ™ÿ·\ï\Ë|`\Ú1a \ÝA²x]*\á¡\å¥}?õ\äô\åÅ{.\ä\Ëõ\ËSÁ‚\Ûq\Â<­V\×s¿Y8s\È,÷\Üj6GO˜÷\ç0ÿ¤¹8ÿ¶Y}\å>3ü»ÿ\Ñ,>r—‘h6Vvõ3w§hw 	*t-\ÍpŒ šQ!\Ä}\"lh‰À·‚M\Ü6\r“ ó…‘Çªùj‰r\Ï\Í>;Êš–\Çw\Ãt+†m3È´ùpŠWl\"dºU‰·kuÂ¼MÏ½.\×\Þ\r	\Æ5e\ÆE\ÜF…¨\'\åƒ‰vŽ\æ^“4\íz\ÖC\Ü\çH:!\ëƒ\Îýø»\æ\Ò\Öfj1Ž+bÊ¬žž\Ç\Zž\ï¼4¡\ëúò\êakaÎ›®@Œ\nðò;\Ò,ž=h5¿£f{\â¾ò³¸8ù€Y8f\Êg™…\ÞSf}v¸²Û¯›\å§G,\ÊûÊ \ä¡‚€{Ì¢xŠº\é\âüBsžˆ\í\02·s„q¡„¨¤\Èo_9\ÅÉ·<$øøžr>9\ÚH?‡‹8),üUPò@xbIqr´œ!	S†¡fBÀa9÷¦\n!B\n\n9\êùº”Y\\À\Ôhž¾¼F@¸¦\Ö\Ç7\ÑFtµB\éŒ.­»ù\\\'ŽY8-P_z3À;tú\Û#;\Æc†„¬’\Ä\ãAxA\Ä\ÙqŒ	-/-\Ç\ÄTZ\ãúµÈª i}–(óg˜d\Ã\äwa\âþ]\ä·=~ŸYò»\ÙÏ°\å_º¡}o–\æ\Ì\ÅrÉ¬¾\Årl¿^s\'ú)q[\áôn‚j\"hx7\êvD>\Í$¼K\ÑÕ‹”E¨—–;K‹Fhó­G\r†\Ú\r}?¾<\Ä²`v-(\Za‰+\ëV\ì¹\È0Ê«•R\ÏEVû\äóŽ/\ßR‰ý\í\ÉöLF˜ø6\ÖË»\ÈQ¡D\ê–\r#\É\Ü/Ÿ\ëúò\Z=<´þ\æùôX5@\Í÷Ý›4\Î\á9\Ýü@µnnR7	\Òn:	\ït\â/þX¼e3+\Ü!²z&se•y²,É®¸/d\å\Ëhž¬C\Ês\é1\ç+)\Úl°/ó‡\äC€\Åw{\Éok\ì³\Ò{›˜E\Å<:ø¢=§§zm€‰˜zŠö\\Ð…º\Ý,\n.{…!\Ö\n·?_n¥’¤¥®4Ãš¤\ìÅ·¶ž“Y\àOS	h\ØPwcò˜ k†F‘ 9»Gˆ÷h„Åò;–»E9\Ò,ùa€|7¢Œy¸GCS\éh…U¯Q\ÂB—93ŸsdÔ¤¤‚Çš÷£ARön#±€»D\ç:\Ã	ð<a‡k\ÈB\Ýv”¡•\ÆOs!‚zH«mt\Ú`ôÓ¿k\ÖÇ‡v\Å\Ì\Õ\"q‰Ð”Z\r\êeþ„°\Z1Á²¨¶/\\MY=X7\æ\íˆ\Øjv\Ý\Å\éS~ò\ë}\È,¿¾\ç:\n\ê±f\ä`½Ö½d\è\×\è\ßõ¾»\ËJ”3\ÅaS^³£þœ!²•Á\rß·;H\ïT\'\ß4\åþ\ë…	•˜·ßƒ¯œŸlCÎº²˜þN9i\'\Ûfš®ij…”ö´òžcŸ…oyiN¬”\È>~\Ç\"B€\n…Í‚\n*Wo\Ï=»žC>D\ZŽ2™\Ö_SWž…<\×ô\Ýó\ÂV@vÒ˜V\à@vŒ4.]Ü¶Ç\ršt\ÎõµIVˆ\Z!5\n:šÿÐ‡þƒ8Ì¸\Îª¥¸‚0wVo‹¢Vó\"\Ä\ì\Ëðn\ÌóM×«MŽ·\Ú\Ýf\é\ÜÁ]skƒ\Çb‘Ÿl \Ùj8H\"2$=†£\Ú\åŽ<“ï¶\ïýv*\\“(!q–LGDO_¦}X\Ü™„ó]0}¥\å´=wd¦•gV‘\ãzpK¤5‰P„½¾¤Jù.\Ç\r\ëU\n¸7vÁ5©,e\Ü\ÅcN„†M˜Á¢\ÑK\æ½÷.U	PAý\Ã\Ï\Ù1A\ãs\Ó!=7\Ô¥Q\Ý2Y\Â\×þÍ€ÁúÄ°ý\Ô\ïˆ\'g\Ø\Ä\Ö\nùI0\Í\àú4\Ôs\ï¼&$5\ÇW\ág\Ì\ë-ž9h6G\ï®\à\æ\È]6\íS\êrOùZ\ï`\á’yR2§\í£s\Ðm\ï/‰I4\ÚBÁ\à™Ö†¢%r\Úc\æt!&V§=ù½½¹&\×\ë_D[\'2F¥\Å!‰!™q -´.·\Ùõ\å\0\Þ?4œjwÚ ¤‰i\Ö\Þ‚dA€ð¹4px\Ô\ÈÜ§û`\Z¥Aõ\Úb*µ\Úc¸\\V`Àn\Çf€\æ¼ò\Îkf\âKŸˆ0\ä$ƒ°\Æ)„cˆA5FŽ1\é¹e\Û	œf´ž.„P<\éõ \Ú`X\ë;+„wi\æa!À\íñ{Mù\ì³\Ð÷°m`½Ñ¸˜_9‡{Qw^R\Ê\Ä$CúaxÀ¶\Î2)<8\ïT4bõ>½e	,l©#\Ýý\íePºø/ÇŽ\\:ÿŠZ/À=‰\nF°½½—\â¡o±h\Ñ­¦†\æ•\ÆÄ¿šg¨\Ù,õ\n¯\Æ\âk\Ú‡\ZŽ\ÅcË’\'\ä„P\ì”\ÃT\Ú\È\ËI\ÔÍ—žÅ‰\×Lað>³2ý´)?r›™þ\æ—\Ì\æô[i\ï\ÌaS<{»Y\è\Ê\n\äó\" \Ãq…˜Fƒy´ü˜H}\Z\\£D\È3CNnZñ\ì]2ÿ	®ö1¥sw›\ÅÑ·w•Á”Ì¹hw\áó«ù¶ž´¦±jd¨\Ä\Þ\å‚ôµw^2\\~\æ1±J\ÈÀ\ÒöYù_\Ï\"³ü\Ø-\ï/0‰>\È$\Z¯œ®Lm…\Ó \n^¤¾Á¡\Ïÿˆ\×h8Q¡¦Q Á‰ö\"°´«¨¹C|¬µÈŽ\ëøÒ“€F³×¢siºN:[$1\'¦fZ1™\Úò&\ÏÀ¾¼\ãKž˜Y+\×õ5b\ZuO‚8Z!e¨—[·\â\Øsf¾\ï\Ùë‹•\Þ/ÌŸ4f\Ë\n\â»n0¥Ÿÿ£yñ|\Õ\Üwi\æ”Y¼S\\ý{\ï7³\ç«óe\â©Y\Ñ^øŸ—ùB\ß\îh¸õ–I‹‚ûl¥¾\Ç%\îOL¡£w[¼G\Ú-Ú«‰V\æü¸e„\Ø,t\à®§›&P\Õ—^yNVýY²e¥_¤\ß\Ò7«\Ê\Þ~°\Î@«eHh\Æ$\ê2\ÅC\×2Š‡©m\Ã8\ÓT¾¶®I„¾<Ì.\É(\ê½HÎP|y ––iù\æF:\êX\\_bµÿ!FLšBn¡†õ=‡\ÏDª€\Û!„êµ·\æ\Ëz{%;R³ù\éhMñ\ÞZ)\\óf\á\èOÍ…\Ù×¬¿\ÇlŸ\"TR¼0y¿h@‹go3KCOY\íp·c\n<«Uh’\ÂG†QŽ4q \Ï6ñ®˜@i¼CÑš\Åj¯\Ë¤jp¾óÁf)³pyBpú\â$†\Ó-\ãBH\×Þ£t\ç!3ù\Õ+e0\ÚÊ°}dd2’Á8 4”\ä[-ù—\ì6‰\ÞÛ°I4,díƒœ\r+SX\çDŽ\Û2q§|²óŠZfI\ß	ˆ\ï†T&œ\æ‚\éSi!\"\ÎõiZ.\Âª\æX\Ò\ëtÄ \×@ø´ª€\èw\Ïª\Ö\è¦\éõÐ¼\ÚA„µ´Bê«¦–\Â\è“f¡\ïz«ý\Ý-ôƒ¥G\Ì\Ö\Ìq³<t“¬ú¾Q23ÿð\×fñ\á»\Ì\ê\äi«ù<f5\ÆS¦x\æ˜Y>‹£\"@Z<{Ä¬\Í®ÿ*¨…0*\Zc»&\ÃZ$g™‹“\Êó\ã)º:ù\Î-˜¹S\î\ãz\ÝJz„³k­º×ˆ\ï\Ès¡]\Îþóß›\ÙWúp”\Ú>:ŠW\ä±\Ä\'\Û\ïKZ\Þ\ßqq\ê´È’´L¢\n7!sU\îÁEüN:H\àœò\â\î)Á+y\èÜ˜Boþ\0¡Oz˜||e]@@TÜ—WA‡À{3ð\rÒ‚9<Ÿf\Z\Ô\ÇWg:dN§\îtJ9®0\Çü__[Ž\ì–o¢ÚœšgN›ùž[\Ì\ÚømU\íÝŸ‹\ço2…\áGmGeÁ\ÜJ0ýW>o–\ß|Y\æ¯\\Á»0ò–\âÂ™[E#D3­hòY=eyô5þzfÀ0´.6C„\Öo—\ç^³ÿ\ËOT\é} \r\\\'¢(‚\Óe¨\'Þ®q\ë¸:3\"ûBNüù3¥“·K\ÏYg\Óð¥\ï\ãòFqò5™j¹T\nH0\r“¨\"J¾±ü Sta…%P¼˜\ZCžW§,—\\Á(ÁŽ\ßÑ°“‹l\ÌjIˆQŠl\ÅoO€™ g!j·¢õ4B§Œ5¥r\Z˜jaQ\r”Z\ÈO4S{]\Z\'*„ƒ@›0\"a°@yu\ÐI«NIÀ€@5¿*\æ\Ì\â\è)³4¸3B{o\á!³<h	pð\Þ]eIA„£ô1³6:Ps®\ÕS{\ï2€Þ›}D´#B\ÔA¡/Z‘\ÇI¤P2l”†_“9R!ÿ©‡¬–|¤z½Z³\ÚG\Ý³(D(eS»œg\ê\rsµ\ç´þ­_5\Ëo½R\Óy -ð-\ê@pû\0\ÅñDû{¿üp@‚c·™\ÂÀ\ÝÞ² _4b\rAn«Â‚Ó§*R\Èn	Ÿ@ \Êd–\0™|\×K¼\Ól\Z€Oýt…\"ˆKh!_%\ÃÊ¹b\ÒL\éƒ\ÔP\îSO½¦].\ØÆ“]\ém]\Âó3\\£V½\Øx²8ö¼-=&Þš\Ì\Ù\Í÷\Üj\nC˜\â\Ä+–À†½\ç\ÕuÑ½\Õ0ƒ–z®3[³wUµÀu;B›?\Ð\Þÿõ=\ç2¢\âù‡>ü¿™\Í\ÕrE;ñ_€ð^\Z|Â¬ö\Þ&\ä\0VzY2|Bò\ÑVüÛ‹ó¢ñ„\Ïox&õ\ÆT°¨8aÌ»ù\ÎQ°lšk]\Z{«J€;d\ç_“U‚ümþfqÖŸòfÅ¡§^}v`5H\æ=¾×Œ}\î\ãf£\Ì%…\ßoš`ºgÐµ\ËÈš¥ŒY~L\ä\Ì\Ê\ÈA+\Ëô–m\ÈX•ÿ.\àR\çù€\\\ÖsTÎ»«!C«q„hz˜Å¡¤B:ú?\nn~œJÕ»\à\á)§syTž4 X\Ó4=¤knõ!˜‡V¡Q\Ó\Za¸ ^\á\nvr\é½7ˆ£\nU\Ô\ÅÂ½\æ½Å‡¤]˜¿GFR‹}×Š)\Ï+B\Ü\ë\Ô\Ãj©gt{ö„˜,\n#Ox\ÏŒÖ§\Ç\Ì\è\'>\Ä\Æ\Ä\"üžbxov/\æ!Ÿø‚=[„µó›zA„y\0\"rM˜˜…\Ù2‰\çC^\è{\Ì{\Òj\Å²\n\\?\äY*fdGs†°“,Y\'¦Uû\Î\æ¯ÿ±™þû¿	ˆ•¹“¬\çû\ÝG·cDö\\>P•5KX›œm•\ÒD¸Ï©o\Ç\È\æZ}^¹Dpœ\Ë1i|/\Ê%pW•)\äÜ¨–¦D€z#j\Öô\å)°\Ûr\Ïp:\çQ·,\Öô”:9N8€\"$D\î²C\Ó¾\ç ýxFŽ‹c\ÏZb:$#&W;S|`	ð½…%\àtwú£f{\înKš‡¬\à…™\Âð)\Ñ&«÷šb\0‡±Z\å1«\Þ £1]Î¨j\ÅD1Û·sžhû+gÞ”ù&!\Â;N;%Ìš\å\ÑW\Ål&Cq\Z±B­¥d9Q\àsV	\r­\Z\Ëg\ÏY~®ºzLù\ìAIÛ²š®\ï\Ü0Aú€©¦g1m\Ú\ß\á2\\\'\É+\×\ã]LýÝ—M\á\Ö\ë\äû¨g\Ùhõ¾\ß}t7¿B\ÞlN\ßÈ«¥GL¹ÿ:‘w¾òi\0¢s-.‚°¦SV€)½jšh„;ò\\\ËÈ±K„>\íh	§”u¿†Ka\ÈZ¥6\ß} 0¸·\ëDÛŒsL”  ê…„:n„i„MQ«\å‰`\Îja\ëG\Ìû\åSU‚»P8iV†Ú¼kÅ»J®ÿ¸%«{\ìï›„07&šK¥û«\çhGC³[½\Å,È¹7I\Ç[;d\Ó\ï²Z\å\Î=ÀŽ4žF)Z\ìÓ˜\éoýeM0\Ø\Ä\È-sh..¼P-#\Z“%V“!\Ú]­P„Z@+$f¯xö¸„J\0ŽÉ‹\"B!¹\Ð&\\Ÿ6ñ\åƒ8¤\ê‚{‹Vÿ©ß±\ïòQ\éÃ¾w˜2Q3ü>.\àX7\ß{T\äƒud\r–,‘I/{\ÏI®,!r˜~)\éù,Gq‚3˜þ\nOcd8N3¢Ø¸DH \Â\â‚3F™\ÃÀq\Ãw\ÃF\09ñ€:\ç&[ƒD›…,“¶½±\ë#\çy\ÜC_Nqòu!5L›hsJJL ¯Oµ\å:\Ñ\Îviua\Ìô\Èh«0pBˆG—©cUO¬(`^E\ã\Ä\ìZ\ê\ïõ5&ï£–† {Ú8b\æ~ò}+œ\ã\ÇÚ¡\Ñ7ˆ^{#D†·™ò\àS»\Ì|­\"C´:\îU\Ë\é\'¤-\Î\Ü\"\Ï#Áó}I:\ÏM8D¸<ˆC`”©W.)Ì®K/=cF~\ïCfm¤_r¾÷\Ý,øÓœš\ØGþÁT\r²\éd\ÎûKË ^\æ˜9\ç=\'m@j\î\0\Ù[5sZ\ÎÂ¿E\Ðòý“còp\êT9†LY%Ÿ\Ív\ázYœA\\©/†.„-\Ø\Ú\nO\Äû´2™É¹úŸt\ÊQ	\Äw-òÔƒ“\ß\Ê\Ø\ári\á\áz\ß¬e¿\æISún³¤k\âDƒƒ\Èp„)Ž>cË\ì=¿f\ÎË¹…þ»\ÄA\Ì\ß\æ\Ô\íB´\Ì–®4Ë¾ÛƒPKÆ¼§­Í•`¡\0\Ú\ÜBm\äQžoþ†3\Å#kP*°Ñ„v\ÈðQ!Õ¾\ÃVû=\å”\Ï\ÎLŠ\ã\ä\'°uJ²¡p˜D»\ÕgÁ{”ô•\ÙQyŽpy ƒgŽ\Ñ4\ÍZ$M\Ý!B\Ú“uñö[\Ì\ä×¾(ó\áð¦´ \ß\å>º\Åñ\Åµa\åŽ\Ê6ŽI#\ÏwN– \ßUýF*òM7?@¾\á?O„û½ð——?v‡\Ï\í\"B\à\ë\è0§\Æò©\ÍV*S%8öŒ\Úñ.’Í»9ß–›­=/l^ô\íEü\Þ{­,\à	\Ä£(N½%\Úówó|\ÚIR_\ì»Æ”F4K…þ]\ç5‹\âôYS}ÊŽ\ÎN¦st\ß\ê\ê\á6Uð¬3ß¹\Ú,>r_\â½U;B{\âXv[·d¨R³Im©\çA)+•‘™4r-õ?e6†\Ës\Èü ­?@óZ®\Îû\Î\ãù|\éŠ8$Ü£6¡úÀ9\Ì\Î|ï›¢\áC†\ÄY\ÕCT_\ÚG—`¶\×\Ì÷·Z\ß\ÍVû{Dd\Ú Z!Ú¡÷œ@¢ˆ¾‡\å²Ö´\Zy¾sÝ´=D£úL+\Ø[QO!¯´\æ\ê°ÓºË—Q1—|¸_­L†±m\ÓA B«ù\Ð±gô8¿\Üb5³2ß§\äðø,õ4Ïšra8ý¨#,T}÷j›MBL\Êö9\Ý4\î\ç\Ó–hË¯¿h\ÂÁôõ€Sa\î\Ü\Ø\ÒØ›BºI\í\ÚÀ1û=´\ÇL\Ú\èü]«\0²µÏ°p\æ–=ùBV\Ó{\É*ˆ‰C„€rqw±pÁ;$füOÿ\È,\Üwgð=y¤Í Ö™}´x˜3•£S<\Ì®Ž\Ü\"Nyi¬\Ó,|r\ë%r<œF w[\Æ\Ü\ë\í!Bà»¡/­Y °\Æ|\\˜M]aM¥}¤‚\ë­@þñw¯2Ÿý\è/™?üð/˜«®üˆyî‰“ÒJÀÔûg\Î\Ù\Ð¦\Ô{}\Åù%\Z\ÌË±®^±÷)O½ \Ï0:tÖ¼ñòUm9\ÊôKˆ\n×¯5\Âit\ß@&|/\êG½Ç¯ü´Y\í;\Ë\Ù\ÃE0·{^QÌŸ£oÊª3\Z·>x‡)·#J§ó‹¢\Ïs2@»¥ÞFx³·\Ì\Ö\Â^\Çi“²§–‹v\'_#\í|\å\Ü\Ûf\èÃ¿,\Ûk\É<°óÞ›\Å>vŠSo‹\Ó.U7s§Y\è»\É*\Ï{\Ïi|ò«27œ\îCø|-µrz‰PÍŸnZB[¯\Éý\Ø6ß——xX\È\â»ú«Ÿ4\ç\ß}EHñžÛ¯B„ÀW[+˜gš~\Él\Ì?g.,<c\Ì\ê\Ó\Ò1ðÝ˜¼\Ã,\ÞkV\n§\Íj90F\Ãmý\Ø-?’kqˆˆ	X\ÒYÁ\0­W²¢R\'ž/\í\Ñ:\àš\î‰\ß<[¸=!b!pV•L\ì`GC\"\Ø\"XžxWœMtI6Y‘†òM:·¤\r\æWû\í\à\Ç\Ö}\é¼\ÕG\Ï\ìÊ§\Î\ÌÉ¹Ï¯@\Û\r*’’ Bö>qÍ¿\Ä%j ÿ\ÂC÷˜ñ?ù¤\Ù,kWº\ï¾¸}«Õ \ËtŒ­ƒI¾3žÁ+\Ö,U\á\éŒ}\Ôƒ}õ\ê±\Ît>xÀo\Ôpl¢8!.W„Ë±*ý	ù\è%B>‰‘\åŽ	1}Ð©Y#S£!¦åª7ä§¤Ç‡´´8e\ïQ0\ßùú\ç,þ»Y[\è7«\ÓÏ˜Å©\Í\ËO\Þj\î8øm3|þQóÞšdc·[\áxÒ¬/\Éu \ÐG\î;l?ºi!Ž¿qÕ§\äúGü‹h‡²jº½yGoþy\â\Ôq³¼T\ÍT\Z)j»aðr…xEpð¬{ƒNi[\ê>òû¿aI2J6/&B´A%4XV§ûd\Òzd¨\åŸ\ÕhG\ß\Ùq–\é½MœgH’±š¬\Îá¡±ù\æóÜ¹BÚ¨T &!B»•`ûkÿ\Õ\Ìü\ã\ß\É{öõFµ¨W&1¸å»†\0!B\ÂwEŸ—¾/ý÷ÿ,¿»NBqü%Yw_\Õ±|ÍŸ¿Yœï¢¦‰\Ú	÷ý»iq§\ê ¾°yTI„_\ç4‚5‹9-\ÜO?2®¹N;\"\çÅ±gLi\ì)S\Ý|ö£ÿ\Î\Ü}èŸ¬\Ð}ÃŽ\è_3›…\ç\Ìö\ÒisqùŒ¹´ò®™{Á|öc¿h¾u\Õ˜ÿ†	õß™‡\ïþ™Y_a‘ï²¹ñ\'\ß³*ÿ\Ñ,¯ú\Âo\Ûvš²\ã·¡%=ˆ’üš\Í\'ý\ØÁ•c\Ê\ã\ê7$¥Qð®\ê9J€d½\ÊÍ•\ÅD\Ü\0\"\Ä<@b€ &Ÿ9®µ™þ\Ød¨“IÌ;\çf\Ö\Ôg\Æ\Î\Ë\Ê9Ô™yÎ…¡`Ar\î.ë’ž‚z\èŠ2\Íj»i’‚Ö±\Zlÿ7_2…\ÛnL-ØžÁj\Ör \n>A˜\\#j\Ú\â²ÀlŸx£/\ãS™\îÁ\ïa±÷\Zñ@\×r´°\ë\Ü6#üþùt€Õ‡\"‰`~\Ðµbt f½JE\åE4ò\á\Î\r\Úú°)õ\\/Ž,A\ØÁIóþF¯yòÁk…\èþò6\ÜùC!>³=l\Ì\ÚKòÿûWÿWóÝ¯Æ¬\ß2\ë\åaóØƒGmù_T’ÿ2\â´ø\â§\Í\Ü{\çrŒ&H>D1÷ø½o~Q\'\'$S¬h¤¯<!\í\ê­{Á\\\"8üK„V`F\Å\ÇE!˜#´Z\\(\\A\à^´\é\\s}Ö’\Ó\Ù\Ûb‘!@«A˜+¡\Æ„ƒYRÈ§¯\Ë0pú¡¾Cwš•±\×\"\ë#m\à°’3mZkaî¸6L\à\\\ä³˜d\'†e	½òsv\Ä\ï\Äb5\n´¯¨QuÖˆbI€LI\ã:Y‹XœaN²\ÂAò3\Ì\Ùj¹ n/» g\Ý\ãFú\á\îwO¬u \ä\Ô$B\0òe\áŠ\íG\ÅÜ‡Ž+pY\×¯\']\È\Åû+\ÏX2\ì3\ÓCO™ûŽ~\Ï|\í\Ê	1\Ýø¯_¶‚\ë\r!?~\ß}ô\ß\Ä|\n 3\ÒzÎ¼*D\Ç£\æ\ï\\ýy!;\êªDˆ¶‚U2?x»\äi>\çµK¸XZ´š\Ôü´˜FÚ˜%Uûb ¹2þ‡\Épc\èxuµ–(\è¹,\ä\íË¯9¯IGœÒ»Á\Â\â\Û\ã÷šò`Pus½m¹\'÷VbŽ›7÷r_º\Ô\Ç\ÕB\Ñ(—^xÒŒ|ü\Ãv`8\èu¤J‚(g¬V -cÚ¥]Zm;Pœz\Ç\Ì÷\Ü&Š\ÊCÂ½ŠVYð-–7“(`\0£þÁüpò…\ÄW\Äö!\Ý8 ‹QÀ\ê!#ƒ÷Þ»\è\Í\Ë\nhU±\Õò™\ÙÍ\Ø>^®¾hvIf1X4\Ã\ÒÀ\ífy\æ%³V<#Ž.\ë\å³¾<e\Þx\éaóÙþ¢\ÄQ©C\â_û´h”Á,\ê\æ?û¶\Ä\ã)\ÑqLŽSãƒ¢MB–y$B6ýD0Žý\Ñ\Ç*D˜Ü¬\è3º\Ø(Lš\írAŽË£§-2\Û÷	Á°b\Û\Z-Ž\ÛsžuÀ©·3Fhp>g–¸`cb€¼K=÷{\Ë\0%>\Ú\ÂG¾IH,\nIÌ£¦¾c^¦nlä€™ü«?“¾\É(\Ø\×\'\Â`-Z. _ù¬Áw“	\çQØ§\r‡\'ž3\Ë#\Ç\Ì\ÅRe-\âÅ‡\Ä¦4üUn&DÁaP\É@.‚•úmÌµ)+ƒŠ\Z\æIò(C\Ùf™¨cu¸BˆI ý–úp¬ÁøŠH\"•ÑžÀ\Í\îIm±\Í ð|¬\ÝQ%\È|\àž`i³\Ù@\Õ\æO\ÊÚó}wJ\0<e‰Ed\Ð	žG1‘\Øc\ïS\"\Ôp\n\0!³\Ç$©y\nHó§K„Ž\Þü\Ã=\åsE„öý6Aø„¡ ®@ƒzBG]s–‹V\Ó\Ò]\ï·\ÆN\ÊVGõ\È$5•H´Q2\\x7p–¡®ºÄš\Ì²\ÕR\ØD¬Hƒ!A\ÚÒ—\çCøž\Õ`û\ï\\m\æ~þ/B†õ,<:zö\åµi\Õ)ªÝ»\\k€®Î¾`\Þyñö`\nh\ã-³>y\Ôzn6\Óo‹<B\á\ÏÀ1r‰—»\ïzaÀœ¯²\ÓWG\Ê6\Ã!\\‡~›Ä¼i*¶6v;ŒÑ°\ÜAŽ\ï!B!>›yµú£À;\Í\×p\Å\é\Ó2\Ù\Ë\â\Ó\îÚž“Fe\Üs`^8D\ÄË¦Qóv\Ìó\Ñ	ø\rQB^¤oo®™É±þjž\ï|¼GyÁ¦šQ9\Æc\"\ä\Ú\\‹²œ§D¨«¢·xPg6ñ\å?!µ¯^-\Ôóhô“ÀyS:{¼\Z°ž„h”õ4W[O7\äaaø\êL˜qŸ\ÛUpMÎ«¥©Š¦˜‚$\×A#l0™n./\ÈÀg\áò=3\à¤/p\ÌÀ\Óý€@#÷›v“fZsì­–i-Álo\à3tÀ|°9(2\ç›_ý=û¿nŠ•µˆ‘\í*¯À#Ÿ «£(f\Ã=\×ô€Á|R\"lFÀy2\é{\çý2\àh‘a>ÁEEi\êf¼\át*\Ò\êŽ\ã\Þ\í-m­¯J€V”\å\Ø\ÉÁ™ðuh˜eyù“š8\éü‡¬h(þó“&&Q\í(2j¨\çB–\\‡ÿ˜M1OŒö\Ëy_üÌ¯‰CLya^ò)OY¹®=†<lø\ê\ÙJP‡¥—Ÿ1Sû%«\Ñ\Ä\ßy\Âš¡nš:Ñ¨I\Ó\ÍS\Îa2®µ3\×dk({\ÏZN4hœ”¡l\éü=f{<0\á[0M\ìrXá¸žC\ëŠr½FL\Î.hO\î•\Ä<ª\å\Ã\é<\ãÊ»oš¡ýùOŸo\Þ;»õ\äBó1¸\Óp \Ê\Ø!ª\n\Âö°\È0¼\ßGÞ–\é•…L\Ù —\\\"$šcóÁX$\ÙIWªK„”uË¸i”¡,\çø®×‹—÷–œj}\Zd)×¶eƒ\Ý\'–$F=\Ø\Ë\Öoò\å;ðšF¹OX7l6#\Âz\î¬x5±\Çó~J€\Ì2/\Èü ó„¾ó]\èd+\Ïyñb\Ô,€™¢Dý=o™©6GccN\å˜4\ÎU\ïQ^,\×~\ï\ÒE3>|^ò(sa+ˆ#t\Ë*ò\à–\Ì{,?õ°l\ê\ÊRXh\ÆEX\ÛBp#„q\Øpc](A\Ö#C®-Þ …I!VM#šR=Ìµ™YM†:‰Y´7X/r¡>kóñ4\Ì8\á(\\‡!a{M\ê\ç\îòÉ•¶d\î\Ñ×Žµ\à#aõh]x\à®À$n5DÊ§ÿúúH^„v¥\ãÕˆÀ\ì´\çƒ0±ùžƒ\è\ÊÇ\â›B„XµÔ²…V„\ÜcPNš\á7®ú´\ÄBCŽz\é\ÈL\äƒz\Ò\ÉWy†lS\"œ\Ãu\Ýó!Ç¾k%Q¸†\ËA¼?\Õú0u†\ç!!Y±nÀ%£\"rŽ-\ÇGzA\Ç\É^\'^5ó½‡\Íòð]ûô\á*[±óûlòÅ­iF‚4\n§\æ!Ï·¶\Z¨\ÑÁsºùc’&\ç\Úk\èHsÒšml\ÚÓ¦½*7¿Ý²yë¼²ú\È\Ì?ÿÏ¦ˆx5w-Q%BŽ!/º\Ø\ÛcÅ–˜bƒòQdˆðw\ÉOœ_œÀtL’J¨I Þ¨Q\Z]©\ç\ê&„XžØ­¥\n)\×ðd%\Ü!j>SHÝ¶\r÷\Ú6ª\éñ_Ÿ©‘gs!š¹\ç\Z„§`\nŸûÙ¿Èœá¥‹:Òƒ2-\r\É^Ë——{\à)?p·(	8Á Ùº\r«™¬cµ]grl@J‰	òc\ZˆÿB„öù±€¡%’\Ï@²\Òi Ò•ü°h)ù¹DˆµMÉó!9%?—£®\å}¾@l(e\â\ç\àp\Z Ï¢¨j„X9%B 7§»e§\rV<`#¶7rww`<@%Fpn\Ð{n£Àv­\ÇÜ G©\Ò\nýˆtÂ±›Ÿg\Ï4Fz²\áO“\íE\èƒ+p]\"$ÁF˜x}d¸4¹[«\á?\Ä\á\Ö2Œ\Ú© ´=Da	XµA\Â&ˆwT\âR²‚œy†]\ç9€`Õ±%L|{XK\ãy\Â\ÄI¹F\ÝƒzøŸÁ\0d8õWnJ\Çn‘‘t§iE,\Óú¾ø0úòòŠ\ÂH%&\Ðqd­P\ÙþmòM)Á@„˜\Ñö”\ØTSvü\Z $\Õ\ÇJŒ€sIƒ@¹&iaM“2€{\èõ\\\"¾k…Ÿ¯xg\ä†ü\á\ØG‘\Í\Íj„\n\æDhV~C\Z\'go’\æH’\Å]Q\ï\ÙdVG7\0O§R\ï\rV\è=o5«t4QL+<“:DM\â2\Ú“›Á4ôŽ\Æ\ÈuJ;\æU\ã[zñðMfþÆŸ\îÒ´\ZB\á9Ó£a”3\riaó¢W3?/\å\Ä,Y!-„8+ÚˆöDž%›=„¤]¿\ígT\ë°|þ³0ò¦”S“\"\ÏiùžI\Ê\Ùt\ÚrŽ\">(\ç>C£\Ïm¥Šf>9.ñ…\Ä\",|ý$Ïø\æ…t\â›óú†Á6lì„³6~¸*#\Ù \Ôs]0\ï”E)Ñ¼ø\ÌCBHhnh{¤\ä÷ƒH\"T’£<\Úd†\ç#B½.$G:\Ç>\"Œº–[\ï¸\àý³u²\'¼JyÞŽ|N‰¤!r!·\Ã@$[Vƒr\Ë&…\ì\Ì~þ€Y;T]ò‡\í?\Ö\'›bUñGž¨–\å\Þq=‡ »@u¶B\É64\ç*ññ,˜E}\ç…!óö\âX\Âin9I§ñC/2­§Y¶t\"H2L²rIþ˜H]Á,\Ä\íl§l8¯\Üs\×LªD¥\åp\îq5ÁFÌ¤˜t\Õ\Ñ\Ç®¿p&XM†\ØÁ\âÙ»v•‡8 dH­W\Ó9öò³\×I:—§`±Yšb^m^TD\Íg\Òö\Û(ž{\\Vža\Z\ßTH\Þ\áû“y\Ô#±e˜6\Åñ\Ì|ÿ¢\"\'/U,e\Õ²\î1¥Ù=\ç\"‹”h Š/~\æ?VÉŠ4Jdlr\ì\Î\ë¡\rBTz\rõ—PoSŽ]\"„ô\Üó£ˆ0\êZ\áºÇ…\Ï*\0Oñ>\Õ4J\èD*DV—K/\ç¦Ñ”\\’Ž\Ìv`e\È÷—–\Õ\n=7™\Ò\Äó\ÞN.-62\n\ç)‚ŽÍ¶Ns\ÞyÀFÀ\Ëb@Àõj}|¾¼F\Ú\'kð<l\àŠy4JkHÁ\'t!(ˆ‚ÿ\ä»N..yA Ê…º4p4X\ê\ì:¤f\Èø„\îÞ›cb	•\à¾+,´=ôª·¼šfyŽ\à™¦\ÄTŠöÕ¬f\r¸f-\ÓkR¸Ï©Ð\ïŒAk‘²&i’`û¼€o²™=Kù6s7\0`\îoôi3\ß{TQ\n˜ûS\r¬[pþüÁ\ÊÙžkX(ñð@z:WˆÃ¼ ð²R@b@	R\Ä\ÔIÿ}D\È1\×\Öó9öaÔµ|õ¯\ä{”5OFXµ`¦A„€\n«9”\n¸£(n\Ç\ÙK€V\\µ¿	ö$_\Ë\âd\â#@ºk²u\Óaÿpz\Z !1#pAe @ˆ@À¾\È§µŒ”p”Áa&\r\">3eØ”¨\Î*›sŒ.j\ÐQd‘1–x+\Î º}\Çzz@\ã’s+±ƒEKn•µE/N>`\ïG¥œ}\ç\ÜDŸhk0šð\Í\áEi_IAÝ¢œy’B4X§ý\ÛV¼3Ú”]*Ø­\"¶o¿Wsø´‚8\ÐoÖ—\×R\Ì\r\n¡F7\ìH<ô\Æ\ä±]SDX\Éð]º\É­¢\àZÊ¢€|„€d\0Ü¶5<\â/l­90O¨žóC|üV\ÒBÆ‹°’¯N.š_°\åI\ãú(œOž^Ç½6e‘Ë¾kù\äx\\ð9Ÿk‰V™lÿc‰‹rRt­t+v ”ˆ\nt_\ç\Z\Í\ß)\ë \ÈZd(\rkót^Ž\ãvj^º\ãþ\Êò^\"M²Á6ð!:AE”ù²p-m-­-\n\í	\"R§–\"\Ã\ÒÙ£f}N	l‡„ Z–qs=Wã‚º2ß¸4ø”\ÙVmð\Õ\íG\nY’\àúö¶­§ø\ïuB±\å]O\×f\0©*Q7ƒ N»µð01’Ï¾…\ì_\ÈÀˆ\ï(\r+J«@S\ÜÁ¯\ÄM\Ú\ï´m²bv@\Â\Ø\éAV¿:“¾V1%?\ä\ã\æ\ÔÁJY\ço¶\ç<bŠ\Ó\ïú¯\ërž7˜\ãñ>!V½õ²2	\Ë\Z\éü§,m\"^\î\Î`^\ÊJ:a\nA99§r^@6G=\çrÝ \ÌÎµùOßµ|ŠCDYQ\Úx\ÏUrt Ü¡Ç‰ˆ+“\n\éÙ‹„\ËÀ\În§Ä¾\r\Ñ%%@4nÐ{ó\ÔsM,v~\Úhk+ð<\r×™¶Ë‹†¥\ã¦\'¨\Þ”\é O\æ\Ý¡»;œ4›ƒˆ|÷ö‘\áöøI\Ù\Ò	“%yJ†U\Ò‡;w}ö<ûaV´Á`nð¸‡:ü¸\à>ü‡ \Â$\Å9\ä»s—\Í\"lJnZo Q\ä\Ê;a¥!\Ù\Ùþ\Ü\ÛM\ÒÛ¨A&ß :½ñ°B\ØW6u\ÌöK6\ßw»ø<@z\ÈB\\\\¯xi“Ge‘\Ê\Ío\æ¼ÿú	Áj]\È ŸÜ¾\á\Ê\çd¦QÛ€qFŠ\Ò\ÐS¯‹+/#u‚IJ€I€+m\ÌŒn\Ä<j¬||VewóIc\Ää¦µV\Ú\\•\å\Õ~i:g¸PoK Zia\ÓÐ´\Â\ç(v‘\áp@†§OY\Ò:`ú­gó C¹¶½&\×O\Z)\ç<f\Éö„\\Ÿe\ÕV§\Î\É\\¥¯<¤„–\\‚\â:úr÷™M\äÕ¬\Æ¡2Z\ë:ú®\î»ÓŒÿ\é™\ÍUÿ¢\Zyß¾o±è¨¥\ZS\ÇÜxt\Þ\Å %Ho \Ø\nn\ÒsÌœŠ÷OÉ–Hx€¢õA”¥Á»\Ì\â\äK^\ç—f\0\ê\à“g§\rt²F\Â9\Âú#‰\â\ä¦\Ø‡Y¾ÙŽn‚—ÿ\Õ!ÀRß¡\Ô	\Ð\Ñ\ÅNœ“‘Ž[TÚú‰‰ÀB6I­ƒv˜¡0\Îþ\è;¦x\ä&\Ñ\Þ|\Â1-Z\ÛL\0GsS\'š°©NAÿK\çOš\Õþ£BV€]\ãYñ%  S\äV…`õw-¿nÛ½ª\rNXmðœJsv\0S\Ñf\Ã\ç\àœ#q„–¤]R\á™\Ürq\ë´“še“‚¶÷ëŸyZ‚\íò}3ó½o\Ê`)\Í©¬õýg&fzMaô)3\ß{\Ä,ö^+„±ApaÒƒ·gmÿ²\ÄAB”&\Ä\ÉÙ¬ª\×\rê›žLÀt\Ì\à\ÜMGG{Ð’BÎ‘\ZB€x9±Ø«»\ÞM¥¾[LqüùXD\Ú´£óBó`õ=§’5@À¸¯¿q2\ÒN\Éq”8M`.Z~óe3~\å§Dø…M˜\é¢\ß,L\ÚAÁÔ„Y˜\ØmšSMaOAxÅŽÖ…W\'±}\ï\Í>\"\Ä\Å¿h‹‹£g$Ÿ]\Ð\äVglû\Û\ë\ÅY\âli0\ØJ´A\â‡ß¨’X˜\Ü`J’ú¹\Ä\Ô}‡\Ì\Ó&BÀý¸/¯Ð’“¬%‹\é”þ0ùµ/š\â\íli‡>\à›J\Ë\n\Ã˜:m)÷_/}„‰ó&¦OL ˜B‹=7ŠiT\æø&^µ¤\×ç½¶‚öV™\æ\Ëó¥GA¶\Ò\nY¥\\0—õ?+ò§S\Þu\ÚHD„A‡\Ú=1E€x=•zo²\Z\à\ÓÕ²Y!®Ó‰þŽ\ê@­Dœ:Dwt4\ÈÀ*\Ë\Õÿ\ÑD!\äñ/|\Æ,¿ñRM³Ys€\Çwaun÷üZ‹®«‰ÀcHm†c!ž¡—Ä»S7ø\Ý\Z½G4:\áÉ—À{´¹nF\ßÝµŠL¹\ï~1Õª\Ó\r\í	4|¤º¤„)Ô{o\Ø\Äó•õAx6_ž”o$¤­wm¤ÏŒüÞ‡\Ì\ÒK\Ïx\É%¸ž|‚\ï&laQ?7-F„\Ä\ë\Âk§––­\Ê:ˆB„ñ\èœ\ï»Ã–Tœb\Z5q2PU’²²3ø©\Å\Ó‘Iž[\Û	mÑ—\ß\ÍHD„2\ïU!³Z¸\Ð{£‘\ï5¢­¥=\â¿\è$/>+Ä©CœAz6\Å	Ki,IT<zÀ\Ìþ\èŸÏ¯%Z %†òÔ°\äVk·„§D…&\Ç1š\ážó,T3“Ukˆœµ\ÄzöXU“x|–\Ï\Þf–†ž6‹c\ç\ÅtY\Ïi…\ÍvuMÑ¥sMy\ì´\ÔC½A\Ñð|\Z˜š\ÝyUÊ²\ê¦E\ê‹&«\ÎB\î¹a@¨¾ôZE¹½\Ú\ên4J‚\n\Î-?ýˆý\Ô\ï˜õ\é1Ê¾>”&\Òøv\àù®\Ã÷Öˆ\ì)N½-ž\Ê8@\\‹\\c\Å4Š‰\Ô5o¦ø\Ùr‡ø \Æ8\æj\Ê4ÚžI	´ˆÁj©\Ïú¢	°8\Z\ìyÅ„,ªùtDF–i¡L‡F‘yx<g=—`\Ú&\îü‹„\Ä Î¤X\Ð\É3ü\Ñ_7[öú>\r(-\à•\èZ¨Eh_¾:A<l•„9Sµ\ÃB<hV†Ÿ2\åñ\ZûŽ¾S\Õ	\ÍX\Z|Xˆ3\á«[T}\Ñ\Ãyiøš¡£µ\î,4\å\ÉñPµ\çs-Î…ô\\\r´Y´±\Û\ßz™ú»/‹õ ™Àõ8H\ë\Û\å:a9£r\ÈM«<\Þ\Ù\ÖHWr¹X¼OÌ¬ð\"¡ø;\Ì\ry\Ï\Í\nx¼\ÖÓœ›!Aƒð\Ëi\î06û‹$À\Ò\è\î\0OFfº\Zxg²ûziÀ÷²›\í\0i@:Q’\ï\Òšža\Ê\Ï\Æ{AÀM\ãkfñ\ÔI!+ŸPl\ë(a/\æO‚\ÙC\é`«\Ä6D\Ñ$	†_3Å³wŠóðJˆhz;I`\rŸ\Çb\Ú e)·2\Ù#\æ\Íp¹0±¡ñ¹\Î9\Ì¢\ÑòlhŠnYû\äX\Û\0²E›t\çAø>µ€¶Iy5so½>HƒB¸ñ¦ó\×ÿXúJxj$M¤Õ¿\Õc;œNZ\\\Ç4L¡,_f–9‡“ò\ÏP_ùV\"ª4V9\rÓ¦z¿ûòºu‰°8õŽØ¼\éŒˆö\à\Øn/Iû¢°e3\Ç\åsiNªq†\Ó\Óú˜š$\Çõ<i]³ C\Ì/‹?h¦þö/D\è¥9·…\Ö&ä‹™\Ï1‹b.D #\ÜÝ².t\Ï>5O.½,Ž3„?è†ºaB\\y+(kÿc\n­\æ÷?,\×r¯¯¨\Î7VÌœß³\"\ÇA\Ý	m¨‘­7\Ä\Ô+˜o…´ö—\Å\Çm»ú\î£\æ\Ôpº%?€6\è+#°\íH¼y\rB\î]š3cŸû¸Y|˜­}\â\ÏQ%};­\rñg\Å28\ïP\à[\áå®²ŽyA\Ùõ\ÆS¶\Ë\0\Ñv±¸¥8…W†u\"‰P¼£úK|\Ë\Å\ÂÎ†¸\Ø\Ê}s€t:^/\Ãu\ÕÜ”m\Ä\Ó3\ÊT˜6Q4‚¸£)„JRm9\í\ç£\Ã3\Òùƒ\ß‰fc\à\\\íD„¼Gøq8a\ÊjGZF~¸lª\Ú\Î)ö:x}b2…\èp€QB\Ü\Z³i\ço±dy—)Û½û<^§Q÷d®1‰–¬\×\Ñ\àz.¡Cî½”`õ·‹\Ø\ä\×\ÈvNo½b†?ò«fµ\çô.ç´´üH\â43ñð/µ¾z´®@!\àw\ÚV·¸s‘Ý‚=DXœ>k	ð„Äº\\(œ¬`°!\î\rU\rP_†j)Q‚_\Ã|yŠ¤ÂòQ&š¤\×\Ê\nq\êA\Û\Ð\á|yQ -\Ó6Om®/›¹Ÿþ³\Ì!œ}\Â0.\à\î¼^”¶#Þ—\íSL‹V\ÛB\à†\Ë\Å\×RR4\Ðú|„H¬ Z#\ÇÑ—/\ÑZ\ZYQù€v\è>;õróÏŠf\É=\ÃZ¯šf\ÈÏ…j³¥{n7þ\ßÌ–\í/Yz4\Ó\Ç\ãš÷ &3ü\ï[†\\!Z­U ¢ƒÒ€ø9d4¥•G\ì\á\Ì9\ÙÎƒ¸—ó÷8x§%À\ëLiô1\ÙDË¯\ÙQ«¹\Ä\Ñ\æ\ê9z$y™\\«\ÖH¥“ˆ4R\ßz\í™ñ\Ê\é\×\Åü…yM\Î\'\ã`zˆ`¾LMˆ€ù@\ßúq\à\ÞCp ¸€\ï\ÍO—jS¬ö\ípu¦O\ê£\ç«-P3)\×Ã&lªd\î6v\ÛHµ¿<‘_¼3\ê=û\ã\ïš\Ùþû`¾0Ci8f\ÌË‘U[K[¸È‹M:&dýOÿ\Âò ñ¥W0X\è¿Kp{\î\î*+³Ž<\"«£\ë\Ñbx²2A\ÌÁK«5Â‹O\Z\Ëu=×¸V\Øc¬ˆýL\rtf	bFJcB›\ØE£ý\åWž2ñ	\Ã8P\"Dp\â\É|^¸Œ’Z\ÚQ8=)„|B\Úu l•f\Ì\Ò\ÐSfñ\Ìsq\ê¡*Jü\àÙ›m\ß?\åuª´…o\Ý\Ñ(@ð ¦_Ìµ˜ƒ`ö+´õ±ÏŒV¨\Ï\ê\ç˜\Í!\ÃÉ¯\\iJwKB=\ï\èfÀU½\ï#Š\Ç_ø™¥\Óÿozø÷K\'v_ƒ´³ü?w¥)ò@„LC¡„d¹TcVD\ÈT\ï•ú» /eéˆ…¬œ·pîµ±lz\Þ|Á¼ô\È]rLþ8Á¸&PVD`Á\×\Ò\È)Ã¢±z!!@\Û8®™3\ÊaÅ‡¨†\åzq\ZS;;øò\\ðafõ“ nš©«\Î\ÈÀ¤Žù¹„P­p›ý—¿÷šô\âA°ôyGH\Í\â\n\0¤\Ù\È. Ê¼	©	v³dËœ\'\Ä\âÀ®8Dœf\Ô_|~\Ïù@œ`\ìsùòÂ€€UÓ…bœ”4HO\ËQ_Y\0 CHPÁÀ`m°ÇŒ|\ì7KvÐ”õJNõbÚ¢ˆð\Ê\Ïü‚ù\Ë/üßª¿!6\Òþ\×ÿ\åÿTMƒ\0Iûñ·þWó‰þ_%\í‘CÿNÒ¾ýµÿe\×ùŠ<!`e*_zZ\ÈJ†¢…--Ì™#?º\Ú|ýS¿jþ\ê÷þ\ß\æ_þ„yûù‡…¤²Rb¸6÷{\êž[\äø¡\Ã?3?û»?‘cò¯Ø­^k†\ÜJ¬\Â\Ö%š\æ\"nƒ¹1\êX\âN\ÈRV=R}ù. †vk…bc1ZŽ\Û~>¸\Î6b*±£*}±IÁûYŸ‡ˆÍ¥\Ò.\Ó]R \Ü˜a aõ‘ ¢Q¯G\Ñ\0Cd\Å=ƒû&W\î)Z\ÚÜ˜Y\è}HQ\Ã(Þ›}T\âeƒ^[¢Rs)¿¹\×s¯\Êsnø\Ý\ß\ìÞG©›\ß)@\Ó/?yÊŒ~\æci¶‹\ÈÓ·£,>\"D£ƒÌ”\Ü\0DHº›F\Õ!>Ê¯š¤›¯ˆC„qC4šA3r#\êYñ\Z2ùñAB#½\ï)BN\"Z\Z\ïYM—ô-46ü\î|%ùð;\èPGx|4M\Î\á\n~«ª÷\ã\Ø%B®sEq½5?¹k\r<\Õ6\êypq\à\ËŽkRù$mPJV–\Ø¸¶90Z!m‡\ä›\"\ÂÐ¹´Ë²½/\é¼?7¯xt\Zb\Æî¿«a\ÇÀ£ln»4™„&ªd¥q|.i\åöò‚Y±š™:,Mž—˜Bv¤W\íÅ½\Ë}\É5\ÐNeÙµYûñ\Ùßt3)Z o¾\Ï}n´CŸÙ¸€¦+&Ò¯^iV_}Ad¾>•&\èÓ®0TøˆðÀ?ÿ\ßE³\ãÿñkþ»ò\\\"t\Ñ\nUCTòC#›M\ÃD¸X´Ú¿•7`ia¢\n~»\å\ÒF+\ä[\Ú÷`À)\éABJRþñ+Ž\å +H“&\Üjy\Î\'_5I,b”#-5‰†•Œ9¾‚]‘ƒ\Zb‚th€¸&OPÏŽO\å¹f\Ò\ë*\Â/sL=ò¥NY›mj¬WG\Ðhg\ã}…\ÛÝ½–n>šd‡\ÎÄ²Z“ùg\"\è|¦\Íf\0H\ÈD\ÅTY\Ö\æRQ°†¦\ç\Ä\ZJþ¼\Õö*ù«Ž\æ >\ÈHoH£Ü±?0cý§«\Ú.\é\Ì{-<-s…ló$¦\Ò\á\ãfñ\Üq³<,©\æškÕ±E‡Á½•8÷\æ\í¬_\n¡¦Ý¾­ý£xÝe^ù\Ò\ÅmoJQƒ[þò¿ÿ?©™\ÓÍ«G„h…¿ùŸÿ/’\Çÿð\\¡K„aòs‘%ò¶b\ÕH\"Cj\Í\r\Ì5Q^\Ø^—ùÁ\à8\0sw”¹û†\ï‰	U‰\í|4\Êøüo	ñŸ2¤‡‰b\å\\%?—€\É\çzñ\ZÝ°7²²‚5©;?@kÄ´NWt	¡i†!Aùò\äºõ&‹u^3ë¥¡| ó\Äò¨õ|\Üq˜C\í6\n¿FÐ´w\ÜùC:>Z\á\è\'Û¬œ³‚;=EI ‰¸€¸®þ\Ê\'\Ì~øLij\Ä,;ók¢‘\Øü/~ú\×$ŸcL‘z}L¤®‰U‰²o¾ð¨YqólY\êÆ†¿lü«\á#\Ím¦<òº\\[\çý\0\Ú^\àü2e‚\Å\Â\'\ä˜ûÎ½©K˜ðUK•ú71›ðŒœ0³ÿü?EùúS\ÚÀ\á!\ì©\Z&B5oBl\02sÍ›.ùA’Jtha\Ò\ã\\÷7\à~h>òS\ï\Ö1m4#3’B\Üö~i/×‚ø”\è 4\Õø \'\îÁ45È‰c%-ýMy\ÈKMœœÁ¡\Ù!»\Ô4º—p÷!\×Ñ¼+ Á÷\ß/–öR\î\Ëñ Â+ŒƒZ/=n‡À\ë#\ê¬§~qŸ¡h\ÏZ\×Q‡ƒ8\ï˜ró\×ý\ÈüL„»O6‚€\0\Ù\Æ(9\Þs\ä\çò[óV­V	¡‘\'D¸\\’ z4\Ã\ÕÂ„=V\ÈA«¤\ì¦\Õ\êªDøâ£’¶l5ÁµÒŒ¯\Û:­\Ùÿ+3ƒfm\æMs¡ô¶y\áUsq\æQ\Ñ—\'\Þ2[öü\Íò¼Y·\ç\\X-›\rû£ì¶f¹qÿ\r\Ê,Î™õ\â”Ì¥\Ä\ÌRµVÜ‰B^4Hê™{þ\Ú5\Å\Ûn¬;U‘&\Âý<L„aB\Ã4Š‰T»Dˆ¨¤\é¦ó’›U÷ó‘\È\Ú\nZ¥\r†ù;\ÊO$	P^ ’‚!(´?%%ŽI‡0•ð$\Z „¦€Ð¸2—L\Õ\Ô\Ê\à‰ÿa\"\Ô9Bú\nô\â…-o¥“€\ÄG\à\Ö\Ú.¨`­X¢¸¥1ûyŒ0\âh¿q\Û\"\r\È\0¥N(³zþ3ö\Ùÿ\"\æ/\æö|\Â0	¸sŽÌ‰\Å]¹F‰ð»_ÿ\Ñüø\r\Ù`\"\åø\ßþ\é+Uð\âúŠÙ°\Äò\È\É[\Í;¯<\Õ\È0ƒ¹ñû\æ\ÂÚ²%e\ßzñ19-“¼\ç;!\å·W—\Ì\ÔP¤¹\î[f\âü\ãæƒµ!ó~\á9³Ux\Ç¹þ$ÿ±û‹‰•ûp]\æ !U®	9s>\Ä\Íõ\×!\ä\ny\Ñª5\Ò®\Æ\ZBª¢Q¶—Å¬û\ì\ãfô>f6¬ b\Þ\Þ×—²@`}Ú¹ŸO#t\ãøâ¦…ó\Ñ\Ãó€üö…^€(\"\ÌZhgþ¼\ìÕ©™ûC‚œ¿½µ&ÿ\Ñ!\"\È	\í\Í\Õ!6ˆ\nò\â¿jC|üw!¡+\×\áz¨r\éQDH¾˜F\×Vð¼i\ÜóK¬}°¸k‡\ÍuQ^žõ®\ÉG\ÉÄ—\ç\"n\Ý\ÒDœ{¶º^õˆ÷À\àh\â\Ëb–^´¤\ÓS²DxWT|ù>(\Þø\ão˜\Ï~ô…dMnXHRƒtø¿m54\Èð_ý¤9z\Ó?[­¬ \æO\Õ\Z·V\Íû‘¾ôx•H¯ºò#r-~C¢\äÿ\ä»_³¸JŽ;ñcK†ƒBˆüþ\â§\Åüô{)eøýøýG,.U¯G=©/õ\æ¸\ç\íÍšgQñ­\Ò\\\â\Åx\" xV\áiWð}0G:kF?óQ³ôüm™‡—of¶_öüa\Öp‰°\\²ß†\Õhû\ê™6\Ìgk2cû<\ï ©C\Ón–\Î\éÁ9<\ÇhJL•j‚üwi(\ãjap®rL\ß\ryQDH„e\"º‰‘ \rROôóª°\r‹ù\Ò7²$?œ¤Z¯q®“6\âÜ³\Õõ¢ê™‰\Å\Ôu÷Q3ó\Ýo¤2—\á\Înq D\ÙA2€4€\Æ‰)Ñ‘†F÷Í«>%\åù™\í|Hò\Ò\æšsŽK‚hmü‡¸¸\î¶}ö÷6\×\ÍÉ£\×HZi\äyóþê œ{\ï\á\ï\Ùö4R%<H\í|ˆO\ëˆ&K>\Ç\îsÉ€ ¦yØ…¶Ý¦%Q4B´\ËF´\Êf€ö‹•€~!»Ql¬D`³Bqú´)\r\Ý+¡^,û\Ø\"ô\Õ+kd¹œZ£PùüŽ3õDÓƒ•¼ ;´<%¸ð\Ü\éJ„:¿Èœ ž©q]ŽÃš¥‹0RV5\Í\êkõ4Œ(ßˆh-ø^tÜ—_¯N7qF\Ò„R\Ï\ãª»\Þ=eN\Ñ\à\Ðoü%¶\Ð\roq4\×\á¤ %B¢Q\í\r“(\ZœK„–\ç÷örÉ¼ýò»ò9†\0õZ˜S]Í‘rK\ÓC¢ujù7ž»\ßlÎ¿-\Çg^¾\Ç\\(¼b\Ö\ç\ß5Gnøn•\è¸\'\Ú(\ç@R«{M÷¹ /Všq\Ó|P3*À\Óm…ˆ”\Ñ\ÎÜ•y\\„ušG0Y¸÷N3ñ,£\í–9¡\Íöš\âø‹f¾\ïv\ÙÀ]bj5¼u\Ì\È\ÛVižÀµ\nF\Ía\â¼\éò\0ñADheÿUT²cž(ñ©\×(Ä¨\çI*!’†#\Ç.ržÖ¡J„¨­õX|4mTøt‚æƒŒ£•¢õÆ‰\íKÔ©\Þý\ê‘RˆsO:\ëJ\ÊB\ÜÁ\Û(8Ÿ vþC˜õ F_Y\â†\ÆÁˆ¦f‰¬b’|û•\'«ù˜O9\æ\\P-Wš\ÞMZ„iT\æ!IÃ”ºQž‘\ãw^¸\Ó|P~Ó¼·\ÜgŽ\\ûu{¿K9®Å½\Ñ$!,L¸QDWt\Û\\ÉŽs!Z\îQÞªYª©UW«!O\ÏO2p\ÞÿÖ¯˜•³o\ÙAeóŽQoFŸ–¥‹=7š²%?vƒ¿T\Ü\Ù•°X’-\âŠ¯z¯\Ó-©¤6†‚%Ã˜ÈŽ5;õ\åc†\Ü\ÜX2‚\Ü &\Õ\Ê\à\ä%sˆü&O‰™…õAa8\Ç=—|þG\ëÿ¹7rY½K×©!ˆ\êu˜\ÄÛ³øH/ŽÐŽS1EŒV²\0/µA·Ã‰G4UOp²\ê½\Úó®ý\ÄGDa%Ÿ€¬·k\Â\ÃiA^\Ïkq‰\r\rM\Ð5“ºdƒI­LË¯/Ì˜Ó¯>\ã\ä/\É1\Î1zÿ\Ý\ë@®\ËV\Ë\ÂÛ”c\Ò0{ª£\ÍO\ßa¶¦Ÿ•9\Ã;nú{ó/\Ìl”§ˆš{\àM	)¿ðø=r\×wŸ+\âä¶™’\"\ä¦\Ú9\ïDœ™¦g&\ÊºÁý\Ýóù/\çCðöw#`\ÐÂ½&ÿú\Ïe>¾·4µ“\â\ä²\î|\ïQS\ê¹^6ÿÞœº\Ý\\*=P%>\Å\æ»\à\\g\nƒ˜\ÒL÷zÝ†¸ò-/@¶\à€‰.\ÊtN:Š•\Ê\ä°<\"\Ó+_ƒ²œunH=,¿\Õ$BnBZV›\ë†)„?°z€Fˆ\ÛQ¸~+]½\ë\Ç¶Ç›U:`‰n\êÎ¨iú›i\î?.ž‚>!Ynˆ/œ\îj<>@ .BLhr‹\ÎÅ¹D¾wõ•\æ»_ÿ¼%®!#uz!§Ž•ü¸®j–ü†d™÷\ÓkA¶\â­jIPÍ¤œ»<ñŠ\Ù.½k‰ð\Û\æ›_ý³]<mF\Ï=+ù\Ï?v·x’rÍ¯]ù\Û\âT\Ã¢>\æ\Ê8\íˆÆ¬«Îˆ†\é´í†‰Y‰Ž\ëA®UB´d)$i\ËQ\rQ\æ\ë´w8—yb,\Ó\ßú+\éM\í:1\ÛoŠ\ã/Y\"»\ßÌŸ¿\Å,ô]kVGo\r\ïýò©]¤÷Á\Ò#²\0»Ã³þq\áü[Ÿg\ÌbÁs\Ý.…H3²Àe	”\ZV’‘H{¬@™\n\È+_f\Þ]D¨šŸ`«\ç\ÔT5v\Ó\ê‘ùqG”\åÅ´\Ò\ÌP«þt†8¯iƒû\Æ!`@K/>m&þ\ì¿\ÚGŒ¹­0\ØöJOB„\Ä\å)9¡yqŒ3ŒK„˜#!\È\Ï~\ì—ÌŸZƒ\ät>|J\És¨^§Ž9\âsAš”­h„œ»b5Æµ¹sôº¯[\"ü}\Ñ/,œ5\ÜõS¹\'\È®-$k5_%ª¸Dèš‘\Ã\í)Êª9!Ó²¢-\Ë}\Ôt\ê\æCž,hŽºüúf\ä÷Ã¬O‚\Ù\×W¢Pœ>cŠ£Ï˜\ÂÀ	S\ì¹É”û¯7\ë\ã‡ež\ïƒ\å\Çv\ß{‹™­™;\Í\Ê\ÈA\Ñú\æ{n3…¡‡\Ógeü\êM\át\êÉ¿¼\ëóp…\éC\È=žM\Ðe Œ\Ý\Z¡el]\ÔMoiº£Ÿz;”\ç\Â\ë\Æµòù\êÝ«]m\ç¾td4€‰ÿñ\ß\Í\ÒOVµ$ˆ\"¼zD¨K¬\n\Ð\×X=°27&\äŠS\n¿!EuR)M\naCH\Úù\ÛK%ù\Äö\r\ìZÂ\åÚ”9pL\Z\×\å|\Â/.­[mÈžK}6Jöüù·\Í\Åù\ç\Ìû\Å\ç\Ì\ÅòyS~É¼ýÒ£U•\ëªI¢\"l\"I8\n÷ô\Ü4m7™´\Ça§¹Ï‚½£E*„(Ä…²²Ð–\í\'\ãŸÿ¤Y|\ìXƒ¶À\Ìù¨™\ï;jJ½™sCÌœ;ó{Š‹\Åûk/\r\ÞdJ=7\È|K>§\Þö^\à=\Ù\êz»Às¶\Ì!)eðž\â†\Ö ÷\ážzVª,±‹!–¬·÷¨\Ý\ÄG\ÃDþDkM0’pK‰Ð¾`_ºB–k‘\é\ÙE\Ü6 3/<x\ÂL]}•h…ƒOhF¡Q\"l–tÐˆ˜\çƒD 	„9\Z\ÒVŒ•\\‚dŽ\Ð~œŽÀOT55<(I—\Ï\Ü&\Û8½?gû\Ìcfe\è³2ù®·ðòkrµ \Z¤\Õtõ·Y\Å*ùö¤…H\'p7\Í\Íó¥‡aò¾güqšb@„p\Û\Õ?fûÄ›30sZ-\Î5s.†ÍœŠ¸6~›8¹z\ÈF\àÅ±gMq\æ\Ü\î\ë\ÖA»­F§?g\Òú#\Ûõ\ÌWŒ÷›\éÉ¡j\Âf“\áZ\\LŽš9 =\å¢\à6D-\ãÚ™\æ®Éµ•\ç^\íxùŒ6}¦‹0h7„\à\è§~Ç¬ž{GLl®À\Ôø@AÅƒ\ÑEX£Q\çK\ÏÔ™5A}yõ€‰\Ñ\Õ\â”|v\Éj‚g›õÁ;d\ÒK3›¥sM©\ïñ\ê9\n4\êd\Z™%\åŠs\ZeV\r\æ\íÀ\Ð\ÖU\ë\ç+D›\r™M}2Vÿü§D+d@\Zxs>\åxs^/Þœ\æOF˜9WÌœ×šùž[Ma\èASœxÅ”\æ¼}-.\Ð \Ú1¥\ÐJ\Õf»½U\Ö]\'|,\Þ\ëžWpB¢ £«\ãIan\Ä[Ø‡\âÜ¨™š2\ÃCýfp ×Œ˜Yû±A€!„H:ÿ)ë»†³¨\ë™Z‹ñ&Šc6Ì«»¯\ÓJ\â\Ék,!Y\\ºp\Û\rfö_ÿI®jDU¡YCðúò„<c\ã4‘TSPŸvG:\×\Ôß¥ž\Ìj\ßa!C°>t§Y<{‹)õ>h5\Ç7ƒsbxŒ*\Êcg\Ì\ê\Ô\Ù£\Ô{Ê’\Î#¦t\ÎjP–t‹gYœv\Í;Wmc\ê‰7n½\Ð	\×û\Ô\ëöüþ‡\Ì\ÒK§¬†ü’­\Ãufyo\Î;¼Þœ;f\Î-Ibæ¼½b\æ´Z³§O5¾\év\ÌV\09ˆ[?r!\é\ê-yƒè¤‘xq&±ô¥1®¬ÁRe/n‰‰R«ZõKdsö÷\ÔÄ 0Cƒ}ù\r\Ô\Õü&\Æ¥\ìÌ”¿‚\Ù%,!\å\Z\rB\ãØ \éL\á¦•\ÄC|f\Ø(Œv!ˆ{_I…3ô›ÿ›YŸf§õ\ÝóOQD(Ú‰þh…®¶ˆ¹/Í-â€º\Ô#ˆ(Pw\Ïeµ°\Ê<]©ÿ	\Ñ/N=X%Ä­±{\Ìr\Ï!S|\çfS\î½GHm\Î\ßgŠ\çNXb»Ý”Þ½\Í,œ9,ôm5(öF\Ü>.;a\\˜¼_®{i\æ”l\"\Ì1d[>kµ\Ï3·\Éu\Øa?\ê]„!u¯¼°9?d\ëÿ”)>f6gO›©¯ÿ™)ú‘ycÀ|°ò\ä.\Òs½9\Ëxsö\Üd\nwWÌœg½}(\Ô\Z(w*Ô‘\Äý\Ís\"“‹GüÈ¶v\È\Ã]s„\n!%[\0AB`ú\Ì\èH`òœµ„VL 9‚‚~\\\rÒ—\ï‚{‡\ãGÂ¨\×`\äûHˆô¸f\Õf	\×Ó¼\ê=GVHr_‚d\ç~ö/fž])B\ÂV´K\"Tw™GÙ¦hfOùv \"<C˜ƒ´ù\Ïó£)/¾$¤¶\Ú{›Ùž¸¯JˆJ^š‹“\È>ˆ\äkYÈ‡¹¤ø\Þ\ì#»òI[¼]ˆb]|nW]ôX\íô\r\É[\è}\Øó½B¢/›oŽž4—Î›ò‘Ÿš\Ùo|É˜SÆ¬½,\Úš\àÊ°zsJ\Í\Ì\Ùº\Õ[ß—§Ë™QÆ—ŸW4\âðƒ‰w©//+x‰Ð…ŒH\ì@;K\Ë1<\Ô\'\Z¢/ˆzCH\×*CcF©\åt¦V\Æ\æ\Ô{–v}\Ðq\ÚXA?X</nô[\ëË»ÌŒÁ\Îó{C$Hc^«oÓ´Ñ¨i\ÔE˜]‚—¹7û m­MŸ3\åþSBLë– Þ :\í\â\ÔCBd[c\'\Í\æ\È]2ßˆ™\í2\ÐøWÌ¡\Ç\Å<Z\ê¹\ß\Ï•|\ÊB¸\ï\Í\î\\ó\â\äƒR¼\Ù\ß=\"e\Ùk±|ö€ô†\Õ$¹—nD\\\ÅÂ»fó\í\Ç\Í\È\Çþ³-ó¢Y~Ì’\Þ-V\Û;)+½\Ôò\ælè¿­üŽ³D\\™‡ŒrqC\Æò€¤ò\rLjRmu‰P˜\"\Ó)B\ÑW&	\Ð,1¯úò\â¾h:EXk\ÔX\ÈzuŒ\Ó\é\ÒB½Ž\ÐÊº¸ˆ3©\à}H€ý·ÿÖ”\î9&§$\09@\0Z—j‡8¨°<X»vJp‘d~®ô99&h=œ(8\èL[m¹`{\î5›–|Þ·d·púFKN·\í\à\ì–\Ü\îr+Y-\ró\ä\Â\à–„Î›Å±³\Þ\ëW1zF´;\Î]°D‰9usô\î\Ý\Ú%é¤ˆB\ÄÄº>úˆ\ÙZ:oÆ¯üC³ð }¿\ËMÍ·hôc_^\'!‰\à›¥<\æR_~\ÞÀôPR\ÎhµLŒM„\n*ˆ¹”ÿ\â\áS\Ç|Y\Ì-\âD\ã¦\á\Éw²\Í\Îm`F±@Z\ØÐ´S-boõKW\Ðvõ\æ/]Ð¾Ë¯=o…å§ƒ\0{Ç•oHQ\æ\Ë*®ûÁª2\í\'A\09ù\ÒA\àyY\Ã#¶Bº²Bš\èø[fcð.!\ÂÕ¾c–¼ž\ßsNI5XY¿uòœ%\ÓG­xXHQ\æ­¶ñ@3]\é=$š\"&\Ñ\âÙ»dž²<ò²%\í	3÷“\ï›\Ùþc°zL9¢‚Ô®o(\r0PnDŽª)µj’¾ŸV¿\Ï\ÄDHÀ½c*…´¨ôJ¹1-‘ùFœnô7\"\Éü\Û`I\Z¯•\æHœMju\ÖV\Ö\Å$˜d¤\ÆûFHN~õJ\Ñ\n!—¨x5€VD\ì/¯\ÕHK#tE®Ì‹2wˆf,¦Ô±sfñ\ìóž%\ÂK–”JV{c¾\Îw\î’m[\Å}v™ G\ß1Ë£x{>XÁ£¦9\Ô\Û\Õ\Ò%Vññ\Í\Øÿ¡\Ù\\Y¬öU¾\'&È¤®ð­\ßX\ç¹<AÍ¡\Í\Îû1@\å:iM]eúTs\'\Ï\ãK\Ï\n‰ˆÁIœ!‚¸š\ì6¬\r+\Ó\'y)\Z~¡k\Óù\ÊDAID\Ì	\Ün[\Ù\Ð\ÂQ\ã~­~\é\n\Ú,i{ôº\Ús\Ú}\è?\È„¾o-Q°ó“U\Êv\Í\Ê\ruHQD…ó¤„Ö¬1ˆÏ˜µþ\ÛE+\Ü<nJ}\î9/Œ$D\\²—`üˆ¸C@½¸>\æ[<G>þa³ü\êsUs[x\ßX6’|c­\ßQ\îc\ï¬,v´u\å\ÛK\Ó)Y˜\çÁ@9\'Öª&Iˆ!8\íh˜ú0CòpÁÿ\åj9^.y¼ª\Þh’øCV,\çœ@K‰§j\ãR‡$J	´U‡‡¸H\ÒA\ÒF\Ò{WIý\î£fø£ÿY<I7\æñµZFÌ°ŠV#¨G´\æ\Ú(¢ž\ÏMg¹³µ\n–Î0X…Æ’aùü\áj|a’¶D\È9ú¿ž—,ro—f\æ\ÛkŠ·\\\'žÁúFõ‹vö\Õ8\Èsý$\ÉÊ€f¦“êJ’\éŽVbiq:ö¼&m\ÔJROD„8\Êð\"\Ãf>FŽ¬„à¦¹Ñ¤ý\ÐIöeAv\ZƒÊŒÉ½¢X™F^\ä~•òk+–X\íÿ0\Ùq®¬U—ðHZ>\r¨)$lúmG]Ü›wÀyÜ¹ŸÿÀ\ä\×\Ìü\rÿf\Ög¬dp#˜Ì´—*+\ßxóš\0&\ß(¢q\ï\'\Äd†¶0üšY\î9\"D¸m	±tþdµœëªÈ²ý0ió®J\Ço3“ùg\æÂ…\rù\Õ|\Õ/dyž?DN\ä\ÑDˆ\ìj•`o§<©‡ nÉ”œV\àŠ\ÙÊª\îq ƒ\Ä D~CrüO2¯G\Ùe{žjJ¬hu\\[ƒów4([Þ–L\Õ®`»m?`7µ¸F»xE“¦\Þî¨\ßn™V¢™{óN¼¬± ‚\Õ\Ù\Ñ~\îššõñ!²xŒÖšCl +×©\'-D‘T`’’2\âD\ä1\Î)›C;Ž3\å‘W$õB«\Z]Q\Î8i\0\Ó\í\ÊÙ·e‘„\ÕþsUDkacÔ¨\ï	’‰;ªoÐ†òR¡1Ù­4ó!C[y¿$H\â\É{¤?úò\ÒFCD\è‚Ê¢¹…\Ó…š\Ô§\Z $\é\éQŽ‘¦Y\Ê\ì5Hƒ\ì|£W\ßs´\Z\Ô10sÜ¾y\îd\çòHØ¦gþº™\á\ßü3ÿ“ï›1qF\æ\ç\Ú1W¨a¾¼fu]ˆO\çµŒ\ÆJ™±³U\Ç°\Ü{wõ\Ü0²šcÅ“—ùË‰/ÿ‰Y¸÷\ï÷ \ß^„f•‡\ï§òR¿v-\"g\"´I\\óp«\ÚïŠ‰\Ùø»<û*Ù¨@O\â<\Ãoµ aƒ\ÐÐ˜XuSP\Ñbób\Úa\Î\ç£õ6!™´\æDCd@27eŠ¯5Ã¿ûŸ\Ì\ìÿÁjg%\è/\ÒV\"\ZU^«Ì…ú¶9žÎ•ºË˜¹eÇ™;D+\\¸\Óþ~jW¾‚\åÛ’„P\Äƒ\'ó\×ÿ\Ø\Ì|\ç\ê T\"bn=\ê›\Ã2“\ç\å¾v,I\ím—®\\Œ¼a\à\Ø\ïÑŽqbÊ›E\"\"DóŠ£AD},\Í\0\Ã4ƒ\r¿k9št:–\Ë3\æ½÷.\nÙ·c’›{ð\ê\Ëk«öz\âø´0g\n·^oF~\ïCf\æûß’\Ý+OJöÌŽ\Õ\É=Ðª09¦e…\Èk™,!H%?€CŠ¯\\\é\ì\æ\âø½B†gÚ´ó\Õ<Ú‡s1_ÖºF# =–žÂŒ~úw\íµw\Çá†¡s\Ú{\Ò\í@2\Ï\æ\ÑÀ\ÊÑ¾PdUÇ½´‘w\"¼£8¾”i\Å\Ü\êÃ³\ïz3|€œ˜\Û\ã\ã\àe£úg¥\Í0\ê\Ä>\ìnÕP\"b”\ä\Î¢]µ\âc\Ór‚ù\Õ8 O ,\ÅK\Î>\Ë\æò‚)9`F?ñ[f\æÿÎ¬œ~½\ZZ6!ª94¬y\n!’\Þ\Äý $®3P”F\Ó\×wLˆpŠ…þ\'$\ë‡!Òš»ÀD½Qš3£ô1S~ö1ù\Æ|\ï\Ïý«J8\ÝGyýŽzûò²§]³D\'!ˆÛ‡Z\Ñ\×¡„(8fEF…\ËK\Ùu8\Ñ­ Nº§a\'!ª\Óò!óA\ÑU\Øhw\Ù\ÕÂž—†‡\\šM\Ü4ñ¶ä¸µ±bJw2£Ÿù¨™þŸm–\ß|) D«ù¸\á6\n]\0Û—§¨—\Ä\Ä!Zš¦\'½Ž„S\\;)d¸xö°Y=y]Ì¤\ÔÁ—hÄ´õ\Ì÷¾)ó¸¼‹\Øó4|‡\Îo4þV§†aeF»\ên«v \×\ïÆðGg˜V8\Í\\\Ñ7¿0V@w…v\Ö#1\ÍØŽ\Å\Î¾ün\0j\\¤ | I\Î\ïžU$4\r¥õ\áH]\ìûŠzúŠ\â\æª\Ä!Žý·?0S_ÿªYz\å9Ò˜%D}rÂ™¥^91­:±xhR\á¹\Æ4ˆpa\è%³\Úh…›\Ãw›\Õñ\×v¯y\á\Ý\è\ã‚ú.\Üw\ÜL|\és\ÒþIK05X~\è+\êašg¤Õ§“¢]÷U>³9\íEø›/\ÏE\ÖŒ+\ÎN¿\æ\Íð€H„¯þn…\n®\Â^gº\rY|8h‰\\7.!6SH÷ƒ\×h\\s}s,y\áþ\ãfüóŸ4Só?\Ì\ÒOV	1\ÉN\îqIPi³Öª+\\«ž	‚LÃ¬[:{\\¼G\ÑK\ç\ïõ”\é«j£h…\Ìub‚f\ç\áüªY9ó¦¼/\ß{©…\ÍÍ•Ž\Ú5½šƒW>¶y\ÐH“B\Ïu,[È¨,§Š!BÏ¸l\Z\n\rHgº	Y¡B	±ÞœE\Ü:\Ðù\Ø\Ådk\Ï\á½pLŸhdN†w\Êù\ââ©“füO?k&¿öES~úÑ€-\Ù,Lö™kuz<HJ‚\n®^vyEH8Ž§&\Z¢†I4ƒR\ßcV<!Zaó\èxh¤Uò³h\Æs”si\ËÉ¿þ¦xÇ­\â¥\ÜH¸\Ì\naR\Ò“\Ê\Z:u\à\Ë\Ë\nò5Ð¶i\"Ky’%¨w­Z\ÖN3B„Ib	ñU\çŠV!@\è¢v›óL+Fp\Ø\Øk95E\Õ\â£ó	\éYˆ³”%¼ðryt\Òf\Ìe˜F¸\âc˜‰¿øœ\Å\ËB\Ð\ËvðUœ\ÜcšT4B‚\nu Q°Hv\Òi\æ\Þ\nV›Qó\èú\àq³6uF®\Ûù)\ÐXYØ p\ëufú[)\í\Ë\à\Ã÷|]l\ß\à\Ý3\0©\Úö\nû8\Ú[ müy­B+\äIV@¶Ð†Q\Úa–\í+D˜4–P—4ƒÁ[E„Á<E0_83\Õ=\Î3­úx´D}$¾÷¨ÁÀQó}.„0Sð<\Å\åœ	tvù\éG\Ì\äU_0\ãöY³øðI\Ñj .7”@\çò\\\á\ß\Z[ý&\r\"œ­,»6rw¬Å¸\ã­wùµd3\åõ\Éqrñµ»÷|Z¾Ä€\Å\ì\íF«¾-@\\e«‰×‡N&B\ïÍ§f\é4“˜˜:ú§\ÑYb‹ÿ­ D\îKcôÉŽ¾2„€@ZL¯#._ž¦\ë:¢I— KS\è0\è\áC\r\æù\'Å¬7~\å§\Ì\Âýw„X°„XÑ˜0e¦Z\Ð0_¦±\é0sƒ\Ì[4\Ý\å-“´É–ý.™EËŽ3P‘ˆ\0ùò\Â\à}·3V.\Ò\ì“õ@¥\ßúòZ‰n B¥fõ|‰‰heÜŽAñ,Wn\áút:Ì¤c=gˆö\Ó\êGGü\át\Þ\ß\Å›Ò¾\á¼8\ÈB\èÐª„øÊ³f\ê\ë_1cÿýd/Ä€SfjšYHC#]\è\Êl\æ	\Ï\Ýf\Çv‚\ëÁ\Òä€´\Ïì¾c\æ~ü\ÝÀ\ä\\\ç›d +‚\'¦GyBc|yy@+\É)‹þŸY\Ä·p\r\ï±úÛ¶sN3B„Ib	¾t\ß\Ë\çCÊºSˆ™\Ï\ÞcldÀŒŽt®7i\Ö\ízú›°~‹9´ÁW–Ï‚§nô\å7^2\Ó\ßú+	\ng\ç\â\áX2lsa\ÎK\nY#N\ìb=,Œ¼aV*ó„«ý·›…¡—½\å\âB6~ôÑ¢·\Ö\ë{ó\"< ¼÷v{JFµU¡\íú–/Tµ\Ã\â¤l²0A§N„LÆ³Io8=m¨G$÷\Ú\Ù\á¾S€f\Ö\ÎxŸ5+ t›+WXÐž!\Z¢[¾²2Y¸`E]!DV§a•š\ÑO|\Ä\Þl.n®	!…Wei0‘6\ì.¨\ÌnœORo™:]ñ\ç\'\ÌöÌ¤,k·ò\æ\Ëv4-4°H\è7\äËH”Š/¯\ÝðÉ¨´Á@ «¹«}\ì K\é/dŽ\\\"LT¨j¸ÏŒFe³P]£ c?Bœh\n³#\Þ2yD+>PTc¯7RV­;\î\Þ\Ê\ç	±â¹·eµB\n‡®7›‹¶\Þm DL¤Í¬Ž\Ó\ì<!K¾a-?õ°˜§¿~•„;ø¾Å´\Ð\æ«za:\í@+úe+ûþ\å\äÒ¶U´Ò´Bðþ„¯€|<!þû*ÓŽN¡ó“\á\ìtþ=J…ŒZ\è$\ã‚vJG†ó\ç\Ô[ý­ ‘ø´f¬\Ï\Z\â\Æp¿™ýÁ?˜\áþº)øy\ä®ùY\Ö-KÂ¾¼8@\Ü\Z¹[´\Â\Ò\Ùc\Þ2>\"\ÃŽ1³ÿòm1‡²¨6m\ÖÒ² @X8ZaH\È9\î“6òö\ÌÝŽõ²ø3øò’C)HL„Fì´œö\Z\ã¢õ\æ#²umÿ\ç9Ö6\Ã$™•0ª}é¾¼z\Ð zHÜ—/ý¡EŽ	a õ°™\ì\Ö\æŠY\î5sÿö=3ü[¿\"[\r­O	!Ê®ù–¬|d\Ò4 _\Ñ\èN¥Þ‡Ì…\Ñ{d•™8\Z!÷!<-pù­W\Ìø?#\Ï\rBx\î{Ú²m“%ºg¬ó\áija^ô³¤Y§9ƒõþ\ì\ËO\n½N•\ã\Õë‰¾ÎžV\åšu cNŽ\Ç_$ U\à%²Y»Ú©\ÙûB8\\A\Îc\0\ÔNB&\Ðþ=Bz\Ø%\î\ç?0CúòmlPœH‚M‚\Ó!\Äðª6hg,ƒæ–‰‹\Òù“²-(¿\Ï[†z£\án­¶k	\Õxˆµû\Üš…O	ºQ\Õ\0³$ \ÜvY<|\Èò{k×·|9Bœf*ý*°@5§\éó\î4Â¡J„qC(”\0\Ã\0ç•¼\Ì@8\Ôo,GK²Q­W+\çP¼·´46\ß \Ói;GÆŒw6‹e\×ü¹`\×ü™q3Ã¿\É:›„¬\r^•\Í\îšµ´›/-Xs³(Za©÷Ô®<\æqÆ¡\Þ\Æn¹ÖŒ~\êw\Ì\Ô\ß}E€\0‰G\Õ÷›µ	4T­ˆ-Žƒ°¬Jô÷V2.W`Is\åN³š>\ç»|•œ+*Ü¹`\ê(³Y; «\ßÔ‹Ÿ\Ê\Z\â\Ý\n	\Ù\É\è£]#eÕ˜|yI¡‡…ó0\í\\Yƒ:ùˆžQ¤\ìš_˜6…›¯1\Ãý\Ïfö\ß6«}gD«‚`’.ž-+\ÚD\æ\ÊFœf\Î\á[\Ãx>.i»ÌŸo¾$NAB\è?ù¾Y\í9˜A‰•«LQ\ä\0]dE@I\á¸5„p^ž¯Û¡«¹i\ÈU7¾0)\Â}\"\"hô\æ\áŠe\Ñ\Ù\Ò\0õd•”\âÜˆ4(\Ïhcüi8{¸\×c¾\ä\ÂöF ¬\ì}ƒ6k\Ï:|¸iŽ\Ð}B }@ž¯\ÈÜ•­óf¹`\n·\Ý`Fþ\à7\Ì\Ìw¿iVÎ¾•˜Yl;j.„¼|y‘=m\Ê\çn\ÝmV\Æ\ß¢¥^\Ým&¾òy3þ\'Ÿ½70ý.\Ëóºcò6W%£\î\ã\ÓÖ´\Ìö|\ßô#^¾ü}¤‘5!\ÙÜŒ<cÀ>·J„qb	ƒ\Ýa\ã\nB:\Z\Ð\ßyZX`\Æ(š7^~Â’\âxUh\Ò £Cgi;ø\ëõp_?r\à_:Z÷\Ó\n 5¤ùñúž¥\ÏÇ»\ã\ãðå¹¨\âj\Ù4£Ÿüm3ýÿ‡Yy\çµ\n!\Ö\ß5¿–ùsk­-ž|(õ>b¶F0\ïÏ¿j¶\ìw¸>\Ögµ×ŸKŒ\ä\Ô\Õ_5\åg«˜?½gog\Û\×\ß^=\ã¬ÁTD\Z„Lÿb°—\'\ËW·ƒ~\í³\êÑ¯\ZQ\\\ÄB\çùV¡{¨\ã¼{0þø\á_07þ\ä›ACV\ÌyJVi˜ö,pý?û¶˜\äô\ÚÜ¯½\í\äûlºôY8½Ï‡pò9ñDA\ÈÃ¶\Ã\n5cô_d‡†\å7^\Ñ–|&NH’¥\Ý\ÂéŠ¤D¸\Ìõ\Æ_4\ï/ô™õ\×23ÿøWføwþ£™û\é?›\Õ\Þ3;\æO\Ù¨†\â\Ë\ËòP¿f¬“Æ¾\ØJˆÃ•\í7Q\ÊI£}*\ê¼*\Æ	ªw/uœg`¦„?û\Ñ_\Ís\Â\Ð%+4:!­\n P^\n›\îZ§–C\ä¿{m\\üù\Ð\Ö{3\Ê\â¯v_\Í[Bœt€FS&œÇ½\Ñüœn:÷PO_»$¦fŸcT»ûA#ÂŽ\rG!Ä“·›±\Ï}\\œP–^~V1\Ø$x‡1{\Ö\Ú48\îxN›Í¹A³tÿA3õ\åÿj\Æ?ÿqSº\ã \Ù,e%\ïÍ¾k_½´y½2\íZl;ç¹?!KÁ€\"¾\Ï@ð-\ãu»‘5\Ôb¦`§”¨Áh\ä[¬‚~«Q•¯€W\ÐE\çDx\ïñ›\Ì>ýkò¸d…\Ùô;W¾B˜¿d¹ï°¤CœW]ù‘j\Þ\íù¤]ý\ÕOVŸ÷){\ìà¿š£7ÿ`×µ1•R®¼8/Bs9‡2œÇµ\Ã\×\á<\î§÷\à?õ\Ós(K:e8Ÿk’÷\Ü\'«yœCY!\Î\ßB\"L†\í\î\ÍÜŸÁ\æ3q\áfü\ÊO›É¿þs	N\ß!\Äú›\0\×\"\ÂÀûs\Ü^oÆ¬šù›~jF>þfö_2k\Ï\Þm6—F*\æ\Ïx&z\Þu;CV’€w\ÓJ\Ç55ú>i\ê\ã \é¾\é5£’¥\ì#}\ÐÞ¾t°‚$6u×±œ\Ä&B´wT\ç^4\ÉC´\È\Í\ì_û´¹õ†\ïW	G‰ÿJ~\å9†d8vI\n¢\Ôß˜CüÝ«\äø\Ø-?\Úu==†¬\èŽjy5\ÑBdz{n¿V\ÈK\Ï\å%?Ž\É\ç¢\Óc­\'u„p\Ýzñ,J¸8YøÚ¥Q¸de{o%Ò¸¿¢}&qñ‘{\ÍÄŸÿW3ù\Õ+eé²­…9‹y	ðð!;d°(„ºü\ÚófæŸ¾n†÷?™¹Ÿþ£\Ù\ìyÖ˜­1³Yx\É,\Î\Ä_ó—o\Ñgž\Î3š1OÆš\Ó\0}³\Þ’\Î‡\ÑJ\Â\ÞG\0\ÞEx%¤(0@I\ê“Bß«5·»‹k\ÕKÀ´­¬þ¦\ÃøŽ}Hº\ÍSV€”\Ø\ÆG{\Íg?öK\æ7_\ÜEV\0B\Ñ4%%Bò@üV\Â”\Õ\ßh„z=½\Ç\×÷¾ùE9v5?\0aQ2\Õû¸çº¿•øH“\ßþ\×z\ê5Hbov\î%\æ?O»4%\Ãz} H³´dC›•Ÿx\ÈL|ùO\Ì\ä_ü±)?þ€\Z\Ä¹k>\ëŽNô\ÉúŸh¢e\Þ\ÜL|\ésfüŸ6¥G\Ì\æ\â¤\Ù.Ÿ5f\åUsiþ~³4ö ÔŸö\ä#\çcGƒqcN1ò>)§!LŒª¥	mV½jGŒ\î>\ÒAÜò7ðü÷\ç‡Áu‘“¾<\Å.\"¬EX¨¢n\'s…N-¹rm_^«¡„!Ž\Å\É;®7_û\Â\ï˜[¯ÿ¾9|\Ó?K\ZZ\Z\Z\Z\æE%¿02(¨E„G…w›F9F³\Ó\ë¡\Ý\é¹s_\Ò!K½{®û\Û%BF9ü\×ë’¯š¦\nN®O^D\Þy0\ÑA\\iÏ—æ´€Wž\ÊLþåŸšñ/þ‘Y|\èž\n!»\æñÍŽ\Ë\âv¹`..[­rj\Ìo¾ÆŒ~ü7\Í\Ìÿü³úòs\æ\Ò\Å\æ\ÂÖªY›~\Â|P¼×˜\Ò}f±ÿfSœ´A\îŒxƒ\åðt¡¨5ª\íˆ¬hÀ\Û/\n´“;@\ßG\çBc¿\ë\rf¤y\Ò}€·\âl2›\Ã7wGULI°Þµ[%!Lyöÿ7,y¡B†«+‹Bf y.q=·.\ÞüCAþ9\Þb› û\Ð5k®«\æO\î­÷Q\â\Ór\îoÌŸÔ\ãp=¹¾^Ò¤l\Ó\Üzº¦Ñ¼¡˜Õ¼\×Þ¶!.½ø”™ú\Û/™ñ?ù¤Y¸\ïN³eûÿ…rÑ¢`¶\Ê%³d5\È\Ùú†\ï\Ï]óC³:p^\Î\Óuzƒ÷Z’¼SHpu\ä°)Ž=\ë½g7Bn	Y=¤y­}´\êõ3Ž#¢//4G“¾¼02%Â¾™·«\×Nº\ça€”dø\ÍHA\æ\Ô,;ø#yŽG¯\â€\ÔÐ¤\\‚\ÑsyIüV\âCq‰c\æ	ya\ã\ÌÍ…›\âµúòs\Ëu¸¯š5\Ñ@!/\Î\ç¿\ÞG‰\ë\ë=\0y˜R}õ$Ð½eE£‰ð˜\êÐžY˜\Þ\0}ƒ”\í¹ü\êóf\ê\ê«\ÌÐ‡\ÙL~\åJ3õw_–\ã\éo|Í”8a¶V—v¼?+£\Ü\â\ØsB~\à¶%\Ã\Â\à\É=÷\êv\Ð~ihqjñð\åí£³Á÷\æ’ß¦JLm\ë\å“8\Ô\ì\"\Â(²–u8—ü\Üc…«\r*\ÂeZ\r\Û\ã?¿\Åû\ÈZ\Ó\Ø\Ð9\Ù\ç\nÁ5\Ô¦J*\ä¡]=ûtÁþ&‡!6~s\Ç\0—`=†t\É\ë\ïy»j\åþJ†œ«\ç\ë½õ\\®¯÷\0\äi>eU#\ä|ñ\r\å\Ñ!0±%q\ïD@6‰½\Éb€w¾.!%ŽW7\Ö\Ìò\ë/ŠC\Í\ær`F\å\Ýó\ß=§8}\Ö,ô\Ý,$øAÁ`\ß-¦4Û»«\Ì\åŸ\ÌH\n\Ú\rÛ—·Î‡†˜!£}ƒxúPZL\Ä\"B@§sGÛµˆtµAE»Í£Œ !w	\á\É\Ç\Ä(ƒ<F\Z\ã#b¯\æ7\éšG¹\à\\–Q›t\ÎWMA®Cze‰5÷³Ø›¯<i¾qÕ§DC£=w\Õaa*@\å^r\ßÊ¹\\_\îQ½VI´HÌ«\Z\à¹õ¡.p\ÊK0}\n‚§\ÅsÖº\æöÖº´3*\Ä\ÈG¨\Ë\èAž® .ô\ße.\Ì\Ü%D¸<|\È\Ç_ª\æ]nð\r.’\âr\éÓ—3ÀcIó\åú€«9\"7\é»ˆ°^P½{ƒ¨c\à#Aóh=@„lð\ë\Ëk¼PHP\ç‚ò—­\ì\ähz \æO¼Dù\ÍÜ¥¯<h¤st\Z¨8\í\Ëk²|`D»Ò¦\ãG¶­h^qôI³6vDHpsòvSzpoù\Ë|\Íx¿^}úr\Ú^œw,›‡Û¾\ÔÌ€_!\Ì¬¸\ÛT\ê\Þ\Ügu¡\åò\nÈ„\Ñý\ÔDº›û¢)0²¡­\Ð@\ãóY;&U…«‰ø\Ð\ÍBÍ›\çc5\n´e\Ú\ÆW®Qø\ÚN\ÞcEˆ‹%Áþö\rDx7z\n}wˆ—\è{ó÷\Ø\ãÃ¦4—\ßÍ£[‰¨v‹\ß{\ÙGw@=H}y>0\çŒO1_~=$\"B*‡ðFð`·\Õt­p”I\Ôeô¼¼‚\ç\êó\æ%¦T^.øh+\\»S\Ú>pŸ½[ô¿`€YXûcZÀJ\àšÓ¹~0ð•³é¢›Æ‡)õ\é;(\Ú\à\Ö\äV;|zW™\Ë\Èw`i¿\ç}´J€¼[¾S_z\ì!\ÂzDE7-\\®•®G‚ aõ “ðl\Ý\äË\ÂD_$À$Êˆ—ÁC¸¼\É@T±\'¯@?T\ç\'E3„ôÚÁÿky<ú\Úz­<Võ]=lŠ“o\ì)s¹ƒo‡¶ó}\'Q\è\æ~}¹\Éût}\'’\"U\"Œ³ø¶šøVó»žIT\Ñ	ó„ªULŽ\Ç\×^) kõÀy¾ô´ \ÚLƒ\æ‚N€¯ý˜[h\Ü\ì\r®[/8WÍ´nZq\âe³9\Ä\r–ðM\Ç\ÚÐ\Ð\ïÈ—\ç‚÷\ËÜ/o­…~8ªøòk÷\È;O\ã{mF–\î!\Â8d¾\á\æ\æJ,mPáž›W@ Cƒñ\íÑ¨‰m&«\å²èœ®GU·€ö’\È\Â|2\nk‰­D¸NÅ‘\'\Ìöôq!\Âbß­»òö±jUñ\å1Ÿ¥¬õ\ï#\\&K\çA†ò\Þ\ì{\Øt¬aj\îÔ‚8ÁT¾\Ý4Wª\Õg\êÁK„ Š\Ãóƒ‚µY\ï5¢\Ð	\æQL5\Ì\ãMM\Ô\Ö\nñ\Z¬§%Ô‚´g±o ™Ž‘W\È{±$Xk\àA^;·\Î	×¯0üp5l¢\ÐwtW\Ù}øAû\Ñ}–n\ì\×ƒŠµÒ«gi‚üø^ù!E\Î#V7\ß9\Í\0Y\Ü\èu#‰ø\ÈP?r\æMx •µ’)¬N{ÏB\'˜G/PŠùYÿ\\!!\r+‹\Ñ-\ïgÏ€¥Gò^\â˜×²Bx°X¼\ß\\š»Û¼_8i\ný\Çw•\ÝGmð¾]³¿i_·\Ì>ZÚöwµ»<\ï¾Ñ¾Q“AX{£!‚\ÎÌžöžW\î5ó\nžsvjX\Èpzr·»{@4\éhrsN\ãq…>H‡\Í`\Ô\Õnð\\¾t Á´vP,k\Ö>¢\ë!¼4ö° „N\\†Kª5ˆ…h\Ë4Miûˆ¦#ònŽ¦Ÿ4:\ÍT—A”)sxöŒ·|t„÷(#+\\V#\î7\ÃCýU\í0 štFF‘®y´at*,ðNv¥ÙŽ\Ï@BI\' \â\\[ÿüºªEø]\î’\ÂK³\'La\è]yûˆ‡5;ðv5\Ã}´2§\×ò\ÏS\ê\ê\ËC.,£ƒXD\Â\Ä\×K4\n`\r\ì\Û;&6‹H]Ð¦\Ù\Ñ wŽª[€\0¨Ä‡\ç \Ï[­•®†¨\0Ÿ^\è»]ˆð\â\ì]–Ú“¿xh\å;\ÝG\0\ä_§´{X^»(—XJ3Zq‰M„À1L\â%\Z·\"yB\r\ï&#\Øß˜i\Â*7\æ”\Zq?ö¡[\æF´­8$ßª6ÀÑ‰{Õš›ÀAGf\nÃx\Ë\ì£>ta‹À>²Î‚Y9òe\ä´\Ïgc§…	A”V˜ˆd81\Û\ë\ÍKŠN0²l˜ºB#}–\ÍB\02…\é\Ô\ÍkµF3I€y Ž\"Y\"	¹ñg?‰@¦>q\æ$XRMˆpú¸)Œ<\æ-³\ÚPwý¸{\Ñ\í£9 ó:mÐO\ØAeFIDi…‰‰€{_z#È»y\\=RQ3]£\ÞJ\n^X­\ÑWanD\Ê\Ô\Ó\Z“E\'A´ó\ÏÆ€\"\Ð\Ú\×R\×Ú¾o\î\ã\Ë÷¡\Ð{›!ûS\è+³½\à›’ùßµÀ]¬,\'\Ûgn\ÉAÿ\î\ÄÁt_\ì\ìDaµ?—\È÷<˜\ÓF¸ByB\\RQ!\Ý\èªh1?3t@«\Ññ\ás,\à¸ò@¬ð]g’´nòÚ·\Ö\Í \Zºj‚\â¼bÛŽ$\Âe“‚m„…½}ùµ@=D¸5\Å:£Oy\Ë\ìc7\Äôy\Ì]š\ÆoýŸ¥¶¹CÛ¹\ÓÀ U§M\\s¨‹\Ô4Â´‘WóhÜ¹(º^f_~“ƒbbEÀss`\\cfj\È\Ì\Í›\â\Ü\Þs\ÐE\ëñt\Ö\ËA@@l¾t	Q£XÙ¦…öòŒk¶\äºÜ“6‡X\ÒFún\"\ÜdÁ\í±gýeöQ\ï*<¨”£ªÛ»Œ\Ý>\ÒC§¶­\Ö²ó‘ ðiƒ \íD˜Gó¨hx· 14“(­mfj¸Jd\0›¶\Î3%\é€…°ö×©8	\Ôq\ÉM£\r\rºia¨ðŒcò\á}P–>6E7\Ò/*;OlN\Ün‰ðyo™} \Ý}\Ðð;÷½›}41/v\è\Z®ô‘Z$˜š\×hVðU¬\àk\Ô\Ì	0sò1óbp÷G³Ð¹%˜\ïöŽL¸g’;\ÊÁu87É¼U\'\Ã\Õ\Öy\î$‹/ó\ÄûT4\ÈÀ”¢\ïB4zÛ¦Á„»\ä¨\á¾¼(¨³\ÌE\â\ï÷–\ÙG\0úpx1Þ·k\"\ÍV÷\r¦\0\âZ´ò†zD˜Ja–È›y\á˜\Öd1û¢!\Æ!)]\ÅÝ—\çs‚\\›c:A8¿ðAP\Ú\"o#o\r¬g\ä\ÚÈ€…¶Bòl\Z\Ú;B5.Á%mk\Ö\Z\ÕE·1“\îo\Ê[nû\Ö\"¼F¦/öQiË‘VA¬lö;Ž\"B\æ}\ç)rA„y3¢D­P%÷\ßdsPtÜ•ò|b-%\n\Ë³-‹cÔ‰´8¡­Â…\íõŠ\æ\æÏ¯…f5	\È7É€…ýÙ‡Pf&\ï0…ýe\ÖjÁ\Æ·Z±N\ÜyE\'¶\'ß£Ö›S˜ki‚Š\\!ðU®]hggHzo\æ¾0õMM4¯epoÈ…yNòDˆ\ì\éKƒ4Ljh_ðn–\Æªj…\ëcG­–ø¨·\Ü>‚(–\Ì\×¶7½e˜\Å÷\×MfnFð)\"Z ÿ\Ãy>\ä†óbk\Æ\Ì\n\Ü?‰V\ÈhˆE¦!¨¨]2\êA]\Õ\ã˜@™ÿl7!ª¦\êË‹\ê\îKO\n\Ú>ŽVŠpYœ0\Å\ÞC²d¸<|h\ßq¦¶­\Æ˜º\ê÷±N\ÞyŠ´¦„Z…´\Þ}nˆ0\æ\ÑFL“Y@\ì\ÝM! vqŽ\é\ÄI;…zR2¢šö–‰\á\í\"Dž3\îH/Î‹ò\èm´]-b¥®:°*N½eûo–uG!\ÃÅƒb6\rŸ³\Ûn¶Mkµ«„w§	ð<\"h\ïÆ¾«v¡\ëˆ¸k™¶¬<}Pt\Ì(g<QÃ‚BœglZVœxl\ÙF	E\ÑB?wˆ–‘²ƒ…B<u\"ö-\ìY\Ñ,öl\ËT\ì½Õ”f\Î\í*³Q±r$™¦ý1©úòö\Í|W\íBWa»µBqŽaŠi%¢:\'\é>+Îˆ°GˆŒˆ)“µP\Ñø\Øm/»@h\ÌJ@øüF1g¯Ÿ!\Êj;öºA½™?j,´…gOSt¡íª¿P…9\ï€þµ2ó²Y?&d(q÷3¥¹d\Zy·\ÃmË¸\Ðþ\á\Ë\ÛGm`J2ð\Èx\ç\ÍXñ—…\Ù\á|!h\ç\\aZ£‹4„Ý„˜\Ûjk5ª¥\0´¿ÀŒ\Z,\Ó9ú\ÎIi\"¤Žf¥¿t|´n™¸\ÚÒŸ\'\á\"–¼h7\Ú\n\ÂJºJšš©cø^F´]\ä÷\àý`²\âž¤\ìy\éžs¹‚9\à$ŽH.h\ß\à\ì;\Ð$NG\íšóoXxx\ç\ç¦\Í\ÌdþˆE½}nòH„ô\í˜:I…N0ùVvt´Ÿ$„ˆvKû‹ sþ\×#ü¸\áC\Û“ˆV‡\0•ß¶¥\Ç5!ó¬xñ†\Ó4a\ï¶Bÿ!O\ÒG+¹e.G\Ð\æ¾ô$\à»	ú¦ð\Îð­$\Ò\éƒ\Ý@˜\ï×—\Ú $˜K\"\í\Ò\n\Óø\0\Óš‰ŽtD«\ËakÁ%\ÄzšÄ—\ÅZ©\Ü;zÞ´þ\n%ªMsŸ«¶\Â7o\"\ß\ÛL)ö\íx’®\ßv\Ù{’2ºw-\0Í‚w\Â÷CûcÁ!\ÎWnù”\0\"\ä{­G\ì\á\ÜtN5B…¯\âY£ž@lè ¢1uxG\Õ\çð\å1-¦ø\ÔT\É\Â\æ¾|\ä\Ç7µÊ‚R<}\ÏW\ï½§\Þ6O\Ò÷\'M¡\ïo¹\ËµÚªY0¨DC\ä\ì\"\Ò\í`ÀÖ‰ó„QÀ\ÊÃ»®·ú„˜[\"lµ\ãŒ\î\á\Ëk7x™ m©•ˆÒ˜\\\àÌ’†)+\ÎÀAM¢¾¼8`Ä©¤Â„:=9\ë½Çž1\ÇD+\\\Zº\Õ§ó·}+€	¢ò\å¥\r5£	\ì=™\×\íô\ï+\røúq§r\ç¹|yŠ\Ü!he8B-¯g\ìƒm\Ú\ë,ˆ°«\Ó)\Ó\Ð\n\Ô\Ô\"T!±:õh8\r‰€³T\ßlŸ\ÄB„\ìYX¹<W\á}´ƒŒ4\Ñd\Ë-[@ÿ©§It%@\æ\Ô2\Ötz\"ª\åš[©\Òñã¬¬\Ò0/\ÕM¥xy\Õù\ãœ\Òì ¤\É1\ÕÐ¾‚1Á>’h›I,…{Ì¥\ÙB†\Ìú\Êt3˜»«õ\ÎZ\rd.–\Ðx\ÎR\Ý\0¾¿$k\év\n\ÔJ\ÄôF8/\×DZ\å8“§0L\Ýd»a­Àöf\ÞÇª%¡¨\Ø\"®×«¬Q\È\\T\Âú\'^6k£G„WGn3\Å\É×½\åº´W^Mr\Ô-)\Év_Ž<\Ë\Ãf\ì	RsO„­\n§\Èû‹\ï6s‹x\×jsLUI4*£ø­\ÍÕº;\Ò+9e­õŽ4Þ—*;\ÙKýe´o!\í\Õ	!\Zz9˜Ly\Æ,­&\í†x–:\ßi\î‰´B+lXxµy¯_#@°\Ô\"»¤\äO	\Úcq&\Ë\Ã`sV\æ!\È}h°Oþ\×[\Ô|r|P\î\Ó(\Ù†OU\ã\nún¾,ö-¤½:mþ[Âºp-Œ¤\ßL§ù£ò\"U\"\ì·\Ú\Û\È\ì»È‹ÿ¤ù\Ê&Eø!\ÒF\Þ;v8°¾\0±qD˜\ÄÜ‡kALWÔˆ‘zR“±ƒ½r=>\ÎGƒ%mÜ’£\ï™\ã²}he©ñ¹B)Ø•\"$À¾8þ‚·\\·€wÛ©sQ\êôU+®´\Ó\Ñ\íDô›&Â¸\Ú\Z ”m”³rœÑ…ª“\n\ËVƒúE\Í{u2\ê…9È»©¡a©¹Ê—\×y€ð\Ò 0\Ñ\Z%D\Ç~“Ž¹•ÿ\êT‘†CE¡÷ˆ\Ä² w¡ÿ.o™n\0Zz7ˆ˜\Øl¿\è\Æ\Õk\Òü®ò\n1[¹\Ú6:)ú®Wi‡S¨ùÌ——Gtk\Ç/µH–hwn:pšƒL›\Ú\'j\Í1(Q³\nÿ\Ó‚\Å\Ñ\'\Í\æd°iyð–®Ü™‚vkdþ7\ÏÐ“/¯±\ãa\Ùýk¶òœMa3\Ä\Ô¦¡«\'ñBY\íDº•%ð¼†i\Z¡þ \å#MÙ¬\Æ5\Ó\\Þ«!Ìœ7åƒB„bq\ä	¹žn´l\0u\È\Ê\Ú+¹•\à\Û\ã™x6_~7€m\ÃD˜†v\Æ5’šJ›qœ\á¥bŽÉ»4\n|`\Ý6O¨!Rƒ\ÝA€|˜)ha\ì¥ÈŠBº@s\Ö¥qQ\è?aÞ›»[L¤¥¾›Mq\êo¹Nƒh\Û5\Þq·@Må¾¼NJ\ÏÔ­kµ6D„i†4$%\ÃF\ï-+t¸\é‚U:M‹\Ý\à7Š\èqBAÀ 2G\ç+Sl?Ìµ.ˆ@\æ\ÃF\Ì\Û\à‚\Í{WG‹Vxi\îD`o5E_\ÙN¦\æn#‡ZP\í°M\nÿú\'\×Iú7ƒf‹i.Àw\Ç\Î*\Ü#P(º‡\"Â´W’’a#Z!/Ï—\Þi\è–\çða|,ˆÁce_>y<?ËA€z~\'X\nC™õñ£B†fŽW6\ïmÝ’ƒi\"ðþ½|H\Ðý-‰SP0`ú)$H\Ûù\Ê\Õ\ÅuD3­hr®k“N~\í.ð†®\á‰1\Ü\ÞZ—¼nX£µaÓ¨\ïb\Í )ú®£\"\íTj\r£\ÛŠ.†\ì{_[«5X\Â`o¿*\Úv\ë4³Nað\Þ\êæ½²\éÀ	q\èñ•\Í-\êx_À9¨^ˆ¬²e0Ó§©Å…ÁµYÑ‰û©¯„\ÊH óñ\åÙªOžoÊ€<þk	\èdBl\ëaI\È0J+Uwz¶3E@¦£úò:a\ÒÈ«2\n­\Ï\×\Ó\ç›N&@…þ\ãf{\êN!Cv¨`\Åý}eóˆNiAwQ“\Ê\æ\ÃÁ\r4˜~ˆþ\Ö|\Ó	þ½5L„\Í8­\ÔB2T2\Æ./!G*+ˆ[s—\Í\ê\Ä÷Ã¤\É¹µ¹*f÷¦&w>H‹N&À*fû%¶ð\â\Ì]B†kcGLaøaY\é\Æ[>G\àtc|]3~^\é\ë \Óä•†Uøò€|•\ç\ë¤\ï¯a\"l\Å\Î\âºr\rd©dLÃ‡\Ï\íf\Ôêˆˆ\êóØL\æ5\ìo\ì¨ž\0\í\Ç6\Ô\ä<Ì…Ý¢úPœ>kŠ½·Vw²—E¹GŸ”¥ß´\rò\Þc7Zd.w@\Üq¦\'\Ü)Ç¾2yB\ÃD|l5t•_^·‚\Î\åK\ïTø2¾yœhp¨s£N4H9¶m\Òm\è¢8õ–¬?\Ê.ö\á2øŽ=/«\à0(ð\ÓNt[\ÝG\08I´üN!Ä¦ˆ0+óh\\\ÈD­Gˆv;ºŽ~ž@ƒ\ìftQœxÅ”‚*\0÷Å‰W2¬³ x«±O„\Ý	¬3x²ò\n!ZyGBlŠ[a­…\Ëõc\Û\'\Â\ËÅ±\ç¬6,ÌvHÀ=»T\äm\ÎÄ¼,P°ôÀœ}3DV%D‹<bSD|mj¯O\Ù\Ý\Ø\'\Â\Ë\Ì®Ž÷Á./š\á¡~3;5\ì-\ß@‚Ý¶™ô>‚N\Z^À\\c‡\Û?`jš\Ûa%˜3®+}« ¬­@7\Ç\å< iºK4…»eežÁ>o\Ùva€\Ó}•­<ó÷B±°\ßNB\ÓD\Øjó(\röFž=\Ó\ÒzL\Ìö\n¸o;Ú¡[\æEu\"Ý—·\Ú(Œ<a¶&\ï­p±ÿf	³_ùv`\ß<\Ú}\ÈJö´››&B\à»pš\Ï@\Û@ <Q!i=Z\Ò\á>»b_#L>ª,W\Ñ\èf§Ï˜¥Á[…7Æ\É\Ü!^µ8\ÎøÊ·û\æ\Ñ\î²\'\É\êNI¡q\ÒÈ†Fœr\ZE*D˜•yTW‰Á3Ð—\ï#¥¬\ÉpŸ\ÓÁ¾I´yún‡™`\ß\ã’FL%!&\á²\íB·\Úö±ˆ*\ë\Åÿ«„hûO+1\×DXQ„\Ò\Ü#\ß}³¼Ÿ.\\\Ä$\Ú%\æ\ÝFÀ\0€A^³xqô\é\êZ¤„U\èh……œ„S\ì›G»j¥\ËR;¢l%\Zbs«Iü±­³/\î<\"lõ<!d\Ô7ó¶·.Š¬\ÈiŸ›õ¿\\M¢k5ò1“¥£\Þ*\ìo\ÒÉ¯K ²‰oW!BŒ¤³\Öj^\Â).\çw}9\0’òi‡„I¤»²„,ž„Õª”\0•ùÖ¸\ßd*D\Â\ÈõHP‘Aùˆ°\Õ\Z1/Ñ—\Þ	\È\Ò$\Ê|±/=/`T\ç\Ù\Ñù€ñŒ\æý}\ï“(¦QL¤˜J5}x¨\Ï\ÌN·?œ¢“û\ê>\â!¬º{(f+(„ˆ†Z\á†0ùnœû\×úÞ¨›\ÆE¦F„­\"\×S3\Ò&C	CŽ¾²Y¡SÍŠiz‰ú<w[­™\Çóö¹›5#1¢eô\ê¦\á$ƒ³Z!\Î38ÑN8k‘ºe[\rž—/o\Ý1a\Ò\Ïu\áüJ¿—K|2P¬\0RKJ\ä›\Z¶\Ê<š”2-²\na;„o–+KP\ï4\ÌdQ<¡\îTž\ÖVD²û¸krš\í3‹ý…	§(Œ<^\Í•pŠ¡²-FZ\ï{‹eKŠY;Ö¤Ôˆøn6\Z!B\æa~`\Ûö¥\çi™D}\ï@‘7\"\Ôù	_^3_“€ú÷\æ\î–\0ûB\ß\áj:;S´3œ\"‹g\ßG\çÁ[ºó…\é#U\"l…y´Q\"Í’¡³¨hõü #\ìN#\Â4M¢µ\æ‡óD„òž2\"®K›2¸\àxµx¦\ê=º<|\È§Þ®–¸\ç·\×O÷‘\è§y^?U\"¾›¤‰fˆ4C†¾{7«e&–\Éb_^^!A\n&²Z\Ú hµ÷r=0\ÎÂ½ü\â\Å-¹6»žK\ÚÜY\èÌ£\Û\Ó\ÇMa\èÔ®òNÑ†}³\Zt.§ö	ðù}\ÞÔ‰0k\r©Y\"j>m\ÄW.kt’³Lš^¢µ´A7\"d\â^]¶\Ó\Â\Ó\ç>^¼\ß\\¨\ìb¿4x‹)N¼\\\Í#œbt¤õ\áûDà»¸¼6.^^œ\Í\í >u\"¾¥…4ˆ4B\Øyp”xGUµ#M“h=m\ä\Ñ\Ó¡-—ý\ï½8ù† D\È\\!›øº&R<H\çZN±O„\Ñ\è¤ÁlšÀ\Ó3ó…™a–)-\"IÍš\áó\ÛE„Y\Î?¥	>ö4L¢ ž6Z=_[\Ä\ë.)\ê8#°\íR=\Ö\ß\á4I/‹\à\à\Z\Û[\ëuµK\êWFn2”])zo3¥Jl%1…­§\à|\é—34Æ­•\ëh\æ\r<Z^\Ôi!\"¾›¥4‰0	‘ù4’vj y2išD\ãhƒ oD\è\î¹\Ö\Ð(\Ø\"tU~%\Ôz(?,{V\çû\ï¬\æa\ÅL\ê–\Ï—«\Ö\ãƒ^‰q\ë6\äq¾03\"ÌŠ$|ž›\Í ®ðôp;/f·¸Â±\Õ„wz=Ž6òF„i€\ç\Ñ\"|yQ(œ¬nÑ„7)ó‡’>\Û\ÚpŠ¼\ÖZ™\"°\ßCÞ´ vÂ·8D;‘‚¤¦\Ç8H›A½zF	\â,ž/	ò*h\Òù\Æ\ÕA»\ßG\ÚÀ¹ ¡w<7d\n}Çª\Î3k£GLa\ä1É›hI8Ežj­&\ÐýÁ|aº\Îd\"S\"\ÌB+Ì‚1‘¢ññ_\á+F»/\Ú\ÂÒ—\×.¨9Ð—\×\âjƒ ó‰p¬²,YÁ¶a°\\•¿\\Ìœ3\Å\Þ[eR\ÈP\âÇž—<\ä\Ív«¦}\áöÛ¡6hŸ<hÊ™!H\Û\\•DC\È\Zy¼y\éH\n\ê\Ómt\Z\Ê\î¶½ªŽ2ke!Á´<‚ñ$\Õ\å\×À\âÀA›öº\äY\"\Íh‡Š´Mãˆ†µù\Ëy\é+™a\ÚZa^ˆ0‰£\r \Þh7i\ë<y¦]—$\Ú`\Ò÷\Ñn\ÐN­p#/Ž¿`–‡	¢–ún1\Åé³’71>(»T„\Ïi˜¼š\Ý;®“ÁZ°—û@ 	ò0_˜9f! |÷i5’>—+\Ô\Ó,\Ó5G6\n:ô®¡›D·!\ï\n³™//°÷\Ú\èa!Ã‹3w™B\ßQSª\ÈX”;\í|ó20kÄ¬½¿´\\bh\ß\Îù\ÂÌ‰øn\Ü|÷h5’hva-6\ïFb\Ö\Ú=\neT—\Öz‚I5ÿ´5\í, ó§\íØ±½0ô€Ù˜8&d¸5u‡)Xí°’7;5,d˜\Æþ…„\Ì\ä}\å(4\Ówyn\Þmš(\\n@~¥wœûD\Ø\0’h>ž•\Ðf£\ÊvŽF\Ó$\â$\Ú \È;\ê@¥]:(ô\ße¶§\ï2\\?j\n\Ã;k’\ÎÏ¤Z\Ñi\Ú ƒ\Ýùœõ[ù\Ï{Šóh0Z6/Þ(úò²FGaR!™6’˜6\Ãu\ÍÚ„×®Ž$r)¡\Ûfõw³h;?ð]˜\í“mš.Íž2dš\â\È\Õ|\æ\Ç\Ç\Z÷&í´	\Þ	ði\èÚŸ\Ãy\ê	\nöµ¿ô5‰//K´„\Ó­·›\ã>¯žYk.i’Q¤)\èi£p»\ÕB\Þ\éi\ÈÐ—\×j§O\ËN\ïN\n®‘ý!Iò\Ñ\n\Ñ\Ã\çÕ‚\ÆÊµC€5\n¼ŽCÚªñ)X1©Zý\å\04ôVÎ¡ƒŽ$\Â4—YKŠ¸Ú‡/Þ±šK;ˆ‘\æ\"\àI‰0\ëÁE3\ÈSÐ°¢8ñŠ)\ÜR1Üžº\Óû™\â\äkffj8¶\')sjBbŽ\ïr\Ðzûòö\Ñ~\àqÌ€Ã——\rF\Íÿ\ì†IrŽ+\ãe\0\0\0\0IEND®B`‚','8a80848460c2ee580160c73af80f000b'),('8a80848460c2ee580160c73ba180000c','Karamoja - Central Sorghum and Livestock',_binary 'ÿ\Øÿ\à\0JFIF\0\0\0H\0H\0\0ÿ\á\0^Exif\0\0MM\0*\0\0\0\0‡i\0\0\0\0\0\0\0\Z\0\0\0\0\0’†\0\0\0\0\0\0\0D \0\0\0\0\0\0q \0\0\0\0\0\0—\0\0\0\0ASCII\0\0\0Screenshotÿ\í\08Photoshop 3.0\08BIM\0\0\0\0\0\08BIM%\0\0\0\0\0\ÔŒÙ\0²\é€	˜\ìøB~ÿÀ\0—q\"\0ÿ\Ä\0\0\0\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0\0}\0!1AQa\"q2‘¡#B±ÁR\Ñð$3br‚	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzƒ„…†‡ˆ‰Š’“”•–—˜™š¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\á\â\ã\ä\å\æ\ç\è\é\êñòóôõö÷øùúÿ\Ä\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0w\0!1AQaq\"2B‘¡±Á	#3Rðbr\Ñ\n$4\á%ñ\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz‚ƒ„…†‡ˆ‰Š’“”•–—˜™š¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\â\ã\ä\å\æ\ç\è\é\êòóôõö÷øùúÿ\Û\0C\0										ÿ\Û\0C																																																			ÿ\Ý\0\0ÿ\Ú\0\0\0?\0þ\×\ÏJ;\ÐzQÞ¿‹\Ï\Ø£Š( “½-\'z\0;šO\á?/zO\á?\0)\Çw ö£½\0-\'¥¢€ô£½¥\è\0M\'ðšQ\Ô\Ò	 À¥¢Š\0)¿\Â\Zˆ\è+šñŠt¯iSk:\Ô\Ë´#,\Í\ÜöUK€I§m\ìEZ±„\\\æ\ì–\æ\Ý\Í\ÂÀ†G`ª¼³1\0(I\'\ÐW\Ë%øù«ø\Ó\Ä|;ýŸ- ÖµHT=\æ£9#O±¸Vb0\Ó;rUƒŽXWø\Û\Æ(ø§,kht¯ntð\ß\éC·Ú™xT\Ïü²RsüGµsW\Z;3[\êšÌšEý–½Í®¢ùfé²E\êŒ1\éƒ\Í|V+ÄŒ¿	Š¹÷»\è¿\Î\Çóö\ãþY†\Ì#…¢¹\é«óI~Ï¥>ü\n\Ñü1¨ÿ\0\Âi\ãk¦ñ?Š\å\æ]NñAò\ÉþX¾\äŽ€\'>¤\×\Ð+q<\×\Ï_¾0\Ï\âXx#\Æ+:òÀ\×*\Ð\äAu¶\Öx·rxÞœ\í\ÈÁ\"¾‡F\Ý\ãÞ¾\Ö9Ö¡\Zð•\ã%t~Ù‘g\\v\Z8œ¹¢úŽT \ä\Óè¢’G°\ß\á4\êoðŸÆ€ö£ø¨=¨þ*\0SA\èhM¡ À¤\ïKIÞ€\Ô\Ò	¥M\'ðŸÆ€ö£½µ\è\0M/¤M-\0\éi;\Ð\0:šO\á4£©¤þ@\n{R\ÒÔ´\0QEÿ\Ðþ×Žh\ç4§¥\ëø¼ý€>j>jZ(>jNsN¤\ï@	\Îi9\Úi\Ý\é?„þ4\0ñG9¥=¨\ï@\ÍG\ÍKE\04\æŽsJh\ï@3\Í\';M8u4Ÿ\Âh~j\äš¢kŒpx5ó\Ä/Z\Ü\Þø_,o<,\Ñ\Ý\êL7ÁjÃªÅŸ–iAí«ü^•ÏŒ\ÆR\Ã\Óu«\Ê\Ñ_Ö\Ï>\â&Y‡xœdùb¿C\Ðþ%ü`Ñ¼\ni©j:\Å\Ð&\Ú\Â<\Ç7¹<G=]¸ô\É\ëò\î£\âj+¯ø\Ò\á./å¼Yû5¨?\Ã<³z»r{`qU¬¬¢¶¸¸»’I.®\îH7S6\éfaÀ,Þƒ²Œ(\ì*ç–™\Î9¯Á¸»\êc/C\î\ÓüYüA\â?Œx\Ì\æR\Ã\á}\Ê=º¿QUW8\ÜsœÒ6‘KG\Íü#5ù½”uG\âÜºh`\é6\ß¼;q\â™þÁ¤Y¤\×])Ú¦÷<©\äþ\Ù	 tsÁ<\0D-n\"–\Õ$‰••\ÔT\äy\ãÒ¿<µgI\Ò\Ú\ßL\Ôþv\Õ¡†\Õcy\Þ|°¢± ¼`WÔŸ³öŸ®i_,´­r\Þks·o\ãl‘Ú‰[\ÈR#\à\0y¿¢|8\Ì\'W\ì;(}®\ï?³¾Ž\ÜAZ¦\r\åó¢\Ôcª•´=u/\ÍQ£\ì\î*Zý£úPOš›\Î\ÓO¦ÿ\0	üi\0ñG9¥=¨þ*\0Ažh9Á¥M¡ \æ¤\ç4\êNô\0ƒ<\Òs´Ó‡SIü\'ñ \0\çŠ9\Í)\íGz\0Aži~jSK@	óRsšu\'z\0Aži9\ÚiÃ©¤þø\Ð\0s\Å/\ÍA\íK@	óQóR\Ñ@ÿ\Ñþ\×\ÏJ;\Óöœ\ZB\í\ÕF~•ü_\ä~ÀKEx—¿h?†?µ%\Ð5{Ö¼\Õ\äÁM7O®\ï=ü˜·0\ä]O\Ãÿ\0Š^øŸ£¾³\à\ËÅ¹X[Ëž\nOƒ¬sD\Øx\Üz0®‡„ª£\Î\â\ìG´s\Ñ);\ÔQ\Ì$ö©{\Ö\rX»zO\á?/zO\á? ö£½¨£½\0-Q@zQÞƒÒ¢yUM\09›nMsž#ñf\á-&]o\Äwq\ÙY\Â2ò\Êp3\ÙG÷˜ö“Ú¸Ïˆ_tO*i\ÅZ÷V¸‚\Â<\Ç7¹\é`õv\ã\Ó\'ŠùPƒ_ñn¸ž+ø2\Þ_BÅ­-›; zyH~ô pea¸ö\Ú8¯\â\'\Ãe\Ôùª»Ë¢_¯có>ñK‘\Òi¾j\"¿Sw\Å~?ñ\ÅGh™$\Ñ<1ž-\ßå¼¾Œ¤`ˆõò\Ç\Ì\Ã\ï8ª1[Á	on‹(0‘ Úª=€©J†$¹$\Ò\çŒWó\ßq&#1©\ÏY\é\ÑtGð¿q¦?:\Ä{ld´\èº/Šª£\åâ–Š+\çO”JÁFð\Ý(¢˜ú–ƒ¤j\æ©[¥Á·bÐ³d<l\Ã£\n’8\à\×m\áÏ‹^/øtŸa\Õ\âmwF7‰ym\ZŒ°mÿ\0\ë•G \ä?®k¹;ö¸ñ´>ð\ÜM<—Mö9\îB•¶·BÁg/3mMÊ›°ªKn\ã÷|\r™\æQ\ÄÆ†¹Fú­Õ¿Cô\róü\çŽ§K*mÝ«\ÇW_[®ž§\èo†µ\Ý7\Å\Z-¯ˆô‚Z\Úúž\"\ÊT”q•$Gµ¿^i¨x\ÃÀ\r<=æ¹ª\Ú\Øi\Ö\éºI4ª¨€¨\'¸º\Å\ï‡)\Ôb\Ò<=¬\Ú\Ý\Ý\\)x£ŽAºE\\PÀ98\í_\ÒŠ\æ²ûô5ci)F”æ¹ŸKþ‡¥S„þ4\á\È\Í7øO\ãPv\n{QÞƒ\ÔQÞ€\Ô\Ðz\ZSA\èhi;\Ò\Òw \0u4Ÿ\Â\ZQ\Ô\Ò	ühOj;\ÐzŠ;\Ð\0:šZA\Ô\Ò\ÐIÞ–“½\0©¤þøÒŽ¦“øO\ã@\n{R\Ò¢–€\n(¢€?ÿ\Òþ¯µ\ßÚ«á„·#Døv\Óx\ËXrV+=\Ãwý4›ˆ£_Vfúf³Ïƒ~>|ZÓ„5dð^1ôý¼Û·N¥d¼a„\'¡ò”}k\é\Ë\r\'N\Ò-–\ÓGµŠ\Ò%BB‹\Z€:(®ˆÿ\0‡ü…d ­J6ó{Ÿ­ºNO\Þzoð\ë\àÿ\0\Ãß…Vm\àm*+G”\æ[ƒ—¸˜ÿ\0zIœ—cõ5\ÆüAø\áO\ë‹\ã\Ï?†|M]WN\ÂH\ÅAgŒþ\ît=p}ˆ¯ \ê6\\œ\Ö\ÇTSç¾ \èAô>I½ø¹ñ#\àö °üq\Ó\ãºðø!\Äzb±	Àò\×\æhA?Æ¥”wÀ¯¦ôhž(\Ò\á\Öü7w\rýœ\ê\Z9\àp\èÀ÷dVŒ\Ö\Ëpñ‡Gá•°A„t¯›µ\ÙÕ¼=ª\\xŸ\à.¨|¨Ýœ\Ü@‘	ô\é\ÎsºKBT+ó÷£d>¹­¹\è\Õ\Ök–]ú™<³š>˜ópy J\Å|¼þý°mm\Ö;Oør\åÁ\å¦\ÒfBGü\äŒþ&\Ï\Û\"\Þi?\á»uû }²-\ØõûÀgô¨xŠqû\Ù~\ÕuLúx\È=E/˜	\ÏùþUóL#ý«--Uu	ø~\êo\âh5I£SôW·b?:­\Å\Ú¤“Rød\ìS•Ú¥«\îÇ¦í†\ì\éôiü\Ð:\Ñ\ìþ\ã\ê0wÿ\0?¥AŽ?\Ï\é_.\ÚüwøŒ¥“WøW\â(Šd“–3)\Ç÷O\Ú?—\áTWöžœH¿oøw\âûd\Î†Ád\èv\Ç#1üZ\Êk½—\â/m\êÇœ\Å|\Ï\ãO­y©\\x_\á©K™ \Ê]\ê7\Û\Û6>\âv–Pz¨;W¹\í^iâ¿ˆ~/ø¢lmnü9 °*ö\Ó+A¨\\óÿ\0-zc?\Ý3\àqX¶¶–¶‘\é\Ö­½¼@Ž0¨€W\ä\ÜaÇ°Á\ÊX\\&µï·¡ü\Õâÿ\0T”²ü¯\ãZ9vô+\Ø\ÙI%»ži.¯.ë‹¹Ž\é¥oö@e°«\Ù\ç9É£ \Ú:RW\àø¬UJ\Óu*»\Éõ?±¸Ê¸šŽµy9I\î\ØQE\ÌsQ@Q@q|=ð•´ò\\Iiö†–g¸\"\á\ÞTW–bˆ\ìUrI8»:RsŠ\ë\Â\ãkQ»£7û;8lmj-ºqo³±ç¯ ü?ð\æ»hš~™o6¯zþU­¼©\Ë»b“€ d³\0;×¤·\Ã_‰òø‹A\Õt\ÍKIô½J+>YaÚRu\Â9\'r0Zòßˆ\Þ¹ñ¯ö…­ô\Ä\Ö\Âù1\Ý\ß\Â\îc¾öÖŒ«\äu WE\áû¾\ZðŽ›\á³\ãI¤—NX\Ó\Î[X±\")ùƒù›ÝŽ\Þ\äc‚s_¦ð\Æa•RŒ1XÚ’ö±zj\Úü\Ö|>\Ï2<;Ž77OmhÓº·¦¬ý#W\ÝÀ\Å8}\Óø\×Ê¿\nþ)\ëk\í\à\Üý Ü¯™§j*\Ç\çs†·“h	\æ¯UÀ”ôÈ¯¥®õ}?M·7\Z”©o\Æ^V£>\çŠý§‹¥‰¥\Z\Ô\Ó?¸ò!\ÂfXX\ã0²¼_Þ½McÚŽõ\Ìi~4ðž½w%†‡©\Ú\Þ\\@q$PL’:Ÿö•I#ñ®\Ìk£•\ì{©+\ÅÜu4†‘áº”ô4š,ZNô´\è\0M\'ðŸÆ”u4Ÿ\Â\Z\0SÚŽô\Ôw \0u4´ƒ©¥ “½-\'z\0SIü\'ñ¥M\'ðš\0S\ÔR\ÒÔ´\0QEÿ\Óþ×ˆ£4§¥\ëø¼ý€0´a})h \ÂúR`fIÞ€4˜I§w4Ÿ\Â\Z\0BGƒS\Ôw¦˜l9ô \Æ\Ø#$f¬S$Ý°\í\Ò~@yOÄ¯\n6»§\Ë4&\î\Ô´c2\Ç\Ýy\ï\Ü{\×\ËCö6òd\àûõ¯¼¥˜«¯+\çŠž–+\Çñ -\Ø(q\Çñqü-\ß\ÐóÞ¿-ñ„\Õh}s½\åº\ï\æ4xñ\áŸÖ¨ÿ\0l`#\ï\Å{\Éu]ýO ©Á¤¤\Î\ÖòÛ¨¥¯\Âlú«3ø\Öý‚Š( a\\_‰~\"øÁþ \Ð|)\â}N-K\Ä÷2Y\éVòœ=\Üð\Â÷G\Ç,±F\îzp\rv•ø\áÿ\0xýœ¾3~\Ô_\nþü\Ô\ï´\rR\ëW\Öµ‹8¥h­Mñ.¦‹\r½\Óm$¡•€\ì%°»\ÃYef28|MNH5+Ëµ¢\Ýÿ\0W\Ðô²Œ:ø…J¬¹b\ï¯k&\ÏÔ…_~\Z|qð¡ñ\×\Â=f\ß\Ä\Z7\Úg´[\ÛB^–\ÚC\Â7ÀW\êT²’¹pjÎ…ñ7Á\Þ$øƒ\â…\ÚE\ÃI­xbFª\Å¢%kb$*÷ˆd\ÈRv\ã\æ\ÆF—\ÛO\â\Å‰¼-á¯…ÿ\0ü[ð\ÚûÃž\n¾¶±‚\Ö\Ï\ÅOý—¯é“›a¦\évš‘Zy ˆ\î \Ôo\ZKym\ÊaX‡Zõ_\Ûk\Âzµ\ç\Å\ï‰>!\Õ<=ñ>\çÇš¯‚<,¾¾ð”:\ÊYÂ— ‰lvÚ‰\ášHÌ†ó÷kd1\Å~…O\ÃJR’\æ¨\ã\Ï\Íe\îÉ«N\n\í©ZJ\ÒmµkY½R\×\êa\Âm^ms^\ËFÕ¥­š³m\ÚÖµ\Ï\é\ËŒ\âŒf¿˜\Ú\í¨øÿ\0\Æqx‹Oø?\í¾1\Ð‚5\rj\ë\á˜ô!öpb–4t·òEÿ\0\ÛV\ìn2’\0\åq\ïZ\ßÁ‰ž\ZøÉ þ\Ò\Z+x\ÈøŠ7v7™¾\Ô\Ú\Æ/\È.\Õ\Ó\ìþÈš{\íI\Í\Ý\í‘ Ry3\à\ZP„e<B¼–šnùT•½\ï\Þ\Ê]Ô—.‡¸bŠ”ª­Wn¶O¿\Â\ïk÷OM\ß\ìg§óŽ•ü¼ÿ\0Á(u\ÝWÇ¿´ÿ\0ƒþ0|U¹\Ö4›ÿ\0xs_¼\ÒVþ\ï\ÄK\ây\ä½I¤\ÔK]\Òcû¤‚$ŽÙH$\n\ß\êOÍ“\Íx<]\Ão)\Å}QË™\Ú÷µº´í«º\Ó{ù;4\Òó3Ü\àkû+é½¼\Ú\ÓW}¿áš±Vò\Æ\ÛP…\í¯\Õ\'þôN2§ð?¡¬5ðÆ›u<gW{B+U\Ùkô†x­\× \â5l÷\É€@®’Šñ0™¶&„%N„\Üc-\Òg67\Åa\á*t*8\Æ[¤÷0\çðÎ‡)V{tŽTmñ\Ü[Ñ¶xd‘0Àþ8=Á\ë>øÝ¯hÖ‹¡üHÓ®î¥¶,‰©\Ú*J“ \'cI\èûp\åÁ9#Š\à\Ôd\â’\ä2\á[\0\×\Ñp\ç\Z\ã2ù5\Í\Ñ\Ýý\Ç\×ðo‰y¦I?öisEý—v¿3\íø\ß\Ã6°“Pð\ÝÈ¸Ž2¬¯€QÕ€e8 \àŽ‡5Ö’œÖ¾^ý—`i<©ø†aû\ÍSY½“;²v\ÄþJ\é…N«\é\Ñ÷Ï½H\á\æ\çN3}Rgú\ÃÙ…L^–*¬lä“·©6“4\êNõ©\ëˆ\0\æ“i4\á\Ô\Ò	üh\0 qFiOj;\Ð\02ip¾”¦–€\éIšu\'z\0@4˜I§¦“øO\ã@Š\\-µ-\0&Œ--ÿ\Ôþ\×\Í\é`f¿‹\Ï\ØQIF\0-\'z0)03@\Ü\Ò	ühÀ\Í&\Ó@=E\éQš\0u˜`P\0@ªF·I\0TŒFAÖ¯*£€¿ð*4ûJ\èR‡2q>Gø‘g¦\é^):V—”‚™\Çð\îv`6{|§#×¥p„\ã“_DüOð=Ö­y±¤\Ã\æ\ÈPE6ß¿µrPœ`d\äy¯ž\'…¬\å\Û($u\är½7ñöU:„\åZ\r\ée¡þ~ø\Ï\ÃUðy\Íj\ê—-);¦–ƒh¤Ü§§~”¾\Õð\ç\ä7AKƒF\r%1Žõ£$\r´\Ú(LC³Žœ\Ônªc•AF \à‚PE:Š¥6._3\ç/†²?\ì\Ëð_Å‹\ãO…^\Òt=N($µ‚\â\Ö\0o\Ì\ZXmÈ‚9TºDXq_G±P8\Îi´WF/[.z\Òr}\Ûmþ&µkT¨ùªI·\ç¨QE\È@\åû\Õ,A£—-\éY\Z³\êi“Ë¤Â·7JŒa‰\ßb»ò©l ž3Ú¦øg\áü_\Óò\ß[\èMù:…´\Ò\ÞÁ&2bÂ¢b@\Êó\ÈúN\á¼Nc>Z²\Þ\î\Ö]Ï«\á.\Æ\çýŽ	&\Ö÷v·™ìŸ²U”| Sµ Å©\êW÷ˆKpø<ö¯§ø2W1\à\ïøÀþ´ð¯…\àû=•š\íŠ<–\ÆNI,I$“’I<š\êDay¯\ê:päŠecý#É°O\r„¥‡“øb—Ü‰i;ÑIšg¤(\êi?„þ4\09¤À\ÚhÇ¨£½!Š03@\n:šZhš\\\n\0ZNô`R`f€u4Ÿ\Â\Z\0\Ò`m&€zŠZiŠ\\\n\0Z)0(À ÿ\Õþ×‰dfƒš9\ÍŸ°‘FE55\0™¥ù©9\Í\0¤\È\Úi~l\Òs´\Ð’8£#4ñG9 È£\"šƒ(	dn\Î)\×¹\íù\Ð\îW\é\\¾¹\á­/\Ä6’Y\ß(%”…\0Obµuy#Îšp8jQ©Ni4ú4s\âp”«\Ót«EJ/£\Øø?Xðî³ \ßÏ§]]îº‹R¿!L\ä`y\é»?Ê³MÍ°š;YË¸“;cpA8\ä€z{WªüE\Ô\ä\Õ<c6û)tñötf\nL›°\å…\ä\0pkƒ\Ô4øµ(D2³&\åt;YXt þ8>\Õý=œý2®+\á\Ü>a…‚\ÃbT4P\Ò\íÌ»ùŸÄœyÀ˜%Š©O¹yL\Åp8c\í\Å-’Z¼P]H†\',…Bm`2 œ\ã÷¢)\í\ç\Ù\Í\Ø\ë\å°o\äMœ> ø]›ð\Æ9\åÙ¬-$¯uvš~g\âYŽS_	?gUd·,0h¯\Ï\Z\ìy\é…QJÀQE\0QE\0*º£\r\Ýñ®\Ëöh\ÓÄ—ú\ÆKR°Yj‘ÿ\0g\Ú\Â\"Y\Í\"™¥\È³‹\Ôó\\Yõ}G?…Oð\ï\Æ\×	/¬|<™¹ðÖ¡v¶Á$?¼°š\áŽÒüP¼„)SÊ“H\à~™\á†/\rO(U\ÒRV‹ý>g\í^\æ¸.tž5µ)+Aô»\Ý3\ïd9^y§0x¦¦BÓ‰85ûë´?½¹™¥ù©9\Í \0G4™M(\Ï4œ\í4\0¤Ž(\È\Í<Q\Îh\0sK‘H3\Í/\Í@E&Fi~jNs@\0#šLü¦”gšNvš\0RG¹‡<Rü\Ô\0dQ‘G\ÍG\Í@ÿ\Öþ\×\ÏJ;ŠiÇ¯\ëG\ëú\×ñyû\0úNi>__Ö—\×õ \æŽôŸ/¯\ëI\Æzþ´\0\î\æ“øO\ãI\Æzþ´qƒ\Í\0)\Ï½\Å4\ã\×õ£Œõýhô\Ö\ásG\Ë\ëú\Ó\\)R3C™ñ6¡6•¡\Ýj¶\è²I\n\Ù\Ç\Î;^,Ÿ|B…CX\Û\É\ä…vR}†Aú\æ½s\ÇZdúÏ„uM\Îa·V“F²\áK)\0ñ\Ïòm£\Ç%¬RD\ÛÔ¨!½F:þ5ýGô\àL£>\Ãâ£™S\æ”Z³»N\Öò?1ñ?\Æ\à\'NXiY5ò>¢ð\ï\Ä/\ëù‰˜Y\\.3ì¡Ž{©\ÉgŠg‹üug\á«\å·)u4\ï\åÆ¨\à\à\à’Í‚p£‰\â¾gdG\\=êµ½…™-k\nDORªò¯Ñ—\Ñk\Âˆ\×n’zÁ¯Ã™4Ï˜~*\â%‡t\åO\ß\ïÿ\0\0Ò¼¸—P\Ô&\Ô\ç\æ\Ü?™)Q´3c\Ç\áP•\ÚKz\Òv\Å5ý=ÀR\ÃR\n´b¬—d~YW)\É\În\í\Ïo\r\Ô-\Â,ˆy*\Ã \ã§Z\æ5H&±¿´m>\r¶Á%óVðP\0÷\Íu´`W\Ëq÷ax‡+­•bô…Ef\Ööºz?‘\æfX(bhJŒúœtº¼3¬\Å\Øþ©ûúNú¤r¤\",’`ªCûbºªLµüq‹ýŸ\Ù¿«Uú#\ãeÀ~•\àqW\Þ \Ñô\ÉþË¨\\¤R`¬\Ü\àþ­Ÿjn-JÈŒ2\nœñ]gO¥\ÚN\æG@ÿ\0\Z­Ï¸\æ¾[=ýŸ4½ü\'f/Ÿû\ÐVs\Ð\Æ~A«S©¯™GŽÆ’™1]:hm.\äY\Ë\âg\ÇÉ´d`\0ç ¤21!H\Çzþñ3\Ã<Ï…3\'–f±JVºj\î2ôvÿ\0†zŸ\æyu\\%OgYQEù\é\Ä(\Æy¬A‡ÄšU\æ…;\Ä\Ñ\îŽA\Ö9#;\ã{£€GÒ·\ÕwS\í\ÜE\'=\Æ=\ë¯‰tjÂ¬^©\ß\î5\ÂÖ•*Ð«fÏ©~x\ÚOˆ_\n´/\Ü:I5õ¢<¬€…2•ð#\æ½<c{`\×Ì¿²¾¯¥Kð²\×Ã¶wó\é—7vò\ÂY|\Ô	;\íÞƒdc\×\ÓQ!\"¿®\é\Í\ÊŸt™þ£d8\å‰ÁR®ù¢Ÿ\àYæ“½\'\Ë\ëú\Òqž¿­Q\êŽM\'ðšA_ÖŽ0y \Ôw¦œzþ´qž¿­\08u4´Á_Ö—\åõýhy£½\'\Ë\ëú\Òqž¿­\08u4Ÿ\Âi=Z8Á\æ€{R\Ó=Z_—\×õ QMù}Z>__Ö€?ÿ\×þ\×\Í\è4w¯\âóöh¢Š\0);\Ò\Òw ¹¤þø\Ò÷¤þø\Ðž\Ôw õw ¦¹Â“N¦¿\Ý4Ò¾Œ\ë\Ä2Û¼küj\ËÓ¦E|G§øvK[24\ÍL\Þ\Åò£óU0‡i\Æ\Õ\rž;“_r6\Ð>¼W\É=„·×iù\Û\\\Í\È\Û\Ê9Ó½yY§\ç<?\ârj\î“ms%´´v¹\ée\Ü9Ì¥*8\êjj\ÝNYt\ÝnUÏ™\r¾„†|÷²*µ\ïöŽœ\ÑÀ\nO3.J¨(\0\Ý÷²IÀ\Æp0s]¼žaS\å\à\ãœ\Z\àm\Ò\ê\Þ\êIõDo:vù¤r`tPG@¨û?€:q/ñhgYœi\ÒZò¾X¹¾‘\×\ê|ˆž\åY~[*˜,#”û«¾_6m\ÑM03þsNÁOZÿ\0Oºº?”%=BŠ)’K(d”…QÔž”\å5¹KdJM\è‡\Ñ_˜~	ÿ\0‚Œ·\Å\×³G\Ã_	Çªx£HñN©¦\ë£\íûm´\é.±¾±{(¼¹.¤o.\ÒÐŒ\Êá˜¨¬ã®¼ÿ\0‚¦þ\Å3xs\Æ\Zçƒ¼V\Þ ›Áú&¡¯=µ¥\êF\ÓMùg}2ym\ÒôY6£IjòÆ›ƒ;*|\Õ\çC9\Â\Ê<Ê¢·\Ý\ën\çT°“·)úNÝƒÞ¿=¾ÿ\0ÁCþ|OÀ~4Ð®­´Ÿø³\ÃZ\çˆo®õÏµé·–¢Ec4\àCql‘KKyû\éÌªœ+BfRJù¿\Ã/ø*¯\ìûñ;\â÷\â\Òõ\ë+o†~ð~•\â+½zú\Öÿ\0Ož;Bò\êÝ¢’\È\ásEnRe2…\Ã-/\íœ5\Ò\çZÿ\0•\ï\én£Ž®­\Å\éþv?Se\×a\ã¦+\Z[¨Pý‘Ã¨\Æú¡\ìk\Æþ\0~\Ôÿ\0j\r\'S\Õþ	\ë‡TþÅ¸[]F\Ö\â\Ö\ëO½³•\×|kqg}1y‰óF^ rT\r{ý|·\Zøu‘qF\êù­Õ‡G\ÕzIj¾G‘Ê©VNž\"?ðiœÄ€Ü«BÄ‘†^?\Ú~u>\Ç\ØŽ¿oaz°\Ía\ÞøoL½o1•\ã;ƒ~\êG$t\Î\Â3øñ_\Äqô\ÂNN§\â\Üoöjk÷Ik÷£\àó\0Móa\çoQ HFb4\ÌKÕ…P¾°µ–ö($—\íN¬\ï;V>ŒÌ£\å9\è¹y­…\Ð\áˆ\Èö3\ÉKŒ\íbÀ``\0¯¹G€Wå˜ _Æ‡5<U7.\Þò_}¬}_ýól\Ë	õºU\"—Dúœõ\î…p\ï­\á\çk\rZ\ÞA4±(.¬*\ã\è\Ã!”ðAõÁl?>4\è&{\É/­5Ã†—\ì³Zý™¸Áò\ã’6 p\Ý\Êy<šTÑ­T™$S+œ\ä³\×Ó°\Ã¡“AÓž_>T,º\Î\Å{	8\í\é_{\Ãÿ\0CN)\ÂS¥˜\ÓQºr+vô¾\ç\ì<9\à\ïeq§.=(\ßX\ê\Ò>¯øñ\Ãÿ\0¼-o\â¯\n\Î&¶›\åu\èñH¼<n§•u<k\Ð{\×ÄŸœ\Ù|sñ>fV(.t\Û;©c\É¦\ÞñùŠ9P=xö\ßzø\Î9\áe’\æ•r\å.e¾§ô5%Y\êÖ—\î©¤þøÒŽ¦“øO\ã_$j)\ê(\ïA\ê(\ï@\0\êiiSK@\'zZNô\0¦“øO\ãJ:šO\á?\0)\ê)iQK@Q@ÿ\Ðþ\×\Í\é£5ü^~À:ŠL\n0=hi;ÑIš\0^\æ“øO\ãFi06“@=E\éQš\0u!£Œ\n\0ŒÆ¼C_2x¶¡ñf¡m#F>e’5N¡ËRÁ«\é\â|\Ó\ãD¼.½šõ£$\í\Æ\á\Ñ\ÃqŸ¿¸Šø£\ì\ÙJ[¦­\ê}\'\ÎK§cš\n$O˜w¥¡\æ‘\0Á=ø§`m5øJª¢\ï\Ôý&pRVgœZ\Ê\ÚI]6ö3nK1\\xÛœü\é\èµj´÷$«Am3Fy,F0>‡\æ?€®\Ì\"Ž”\ÆP¼\çšþ\Ñ\Êþœ\\A„ÀQÁ\Ã($œõKº\ï\æ~‰ð-«ˆwVI=’µ—\àr]G(>Sn\0\í8\çuÐŠ[¡)„¬3\ížt©u\Ý¯,®M]\éC\åÉ’¿7Q»\Ï>µ^%3\Ä¹!½qŽ\ê}}\ëûsÁ0\\–b(T¥\ÉQFÓ\ÓMI4í»·ªû\Ï\Âøã€«ð\æ*•eQJ-\Ý;k£¾½>\ãñŸÂŸðMÏ‹¾7øö†øK¬\é:vµ\ã\ëQx¦\Õ\ä¸ú¿„u†SšV<¦©¦>ù­HR¹¡y6G9ðóþ	\ïûWkžðÁ¯ŽšŸ„ô\ï\rü ðV½\á/\ê\Z\ÞO}©Ë«iM¢\Ãw{\Ä\Çiv¬e’e›|\ç‡\n¼þ\â•QÈ¢¿t¥\ÃxX\ÅE+%\Ñ=:’ð\ìü\îY­Fùžÿ\0ðÿ\0\æ~$ø\×þ	ûûP|~øà¯†_\ï¼5 \ê^øcâ¯‡—iW—Ð³j–šU®›t‚{kvaþ€\ísÛ³r„2d\ã›ñ\ìûo|x\Ô<añ7\âõ\çt]\è>²\Ðlô‰u\Ý4\Þx?W“U‰u¸¶‚G·»g(\â4\ÌJB…}¹oÝª*\å\Ã\Øw»{[+~D¬Î¢\Ú\ßÓ¹ð§\ì§ðã§…þ5|Sý§ÿ\0h¶\Ð\ìüKñ,\è–\ÃHðô÷–vVš´¶ð³^]Cm,\ÓNfvÜ¢ \nl“_vcŒ\ÖMþe¨\Ü\Çup\Ó$Œys\Ë\ía²¨ê¥¸ùX‚\Ë\ÕH5v\Ò\Ò;8\ì!\Üc‰B)wglŽ]‰f>\ä’z\ç5\è\ápÑ£O\Ù\Ãm6rV©\Ï\ï=\É\Ô\ç¶*•\íô6*¦_½!Ú‹\ÝÛ°õ‹\â¯\è¾ðó\ëz\Ì\Âk}ª]Ø7\ÌrzžO5oBûý¤z\ìs¥ÿ\0œ7E2` FŽÞ§©¦±0\çöW\\Ý¯\àž–k]\Ó\æIF\×\×SGK±ž\0n¯i\ç9|tQ\ÙG°ýkd¨\Í5a\éOÀ\ÍZIlX`pT°\ÔcBŠ²Ž‚*(&š+`w§€2i\n¼(o¡\Ø\ä\Îj\Ê\í<+ñcÃ¾+µ&)/\îH¹!w	 Ÿ,»²\0CvÉ¯½û\×\ç\ïŒt[ýgHJa\í´\Ñ][»1P%Ã®XdŒ\ã\ãŒô5\î~øùoªj6šŽ´©4û\é|‹s\æ,ö\ÒÉŒ…Y”)ó€\Ê3Ž+ø›\é#À¸\É\ã\Ök…¤\å[I®w<lU6¦\ßs\èñ\Ô\Ò	üj ˜&KµM»w\"¿“š\ês“¢Žô„(À\Í u4´\Ð4¸´\0´\èÀ¤À\Í\0(\êi?„þ4\09¤À\ÚM\08õ´\Ò¸\0´R`Q@ÿ\Ñþ\×N=(\ã=)Ç¥\ëø¼ý€N?»GÝ§Q@\r\ãû´œg¥>“½\07Œô£Œ)\Ý\é?„þ4\0‡”qž”\ã\ÔQÞ€\î\Ñ\Ç÷iÔŒvŒ\ÐN=+\Èþ\'xJMRñLl¿·€°•N	òÁ\Ú³“\Åz\Ë9ö¯ø\ã+\rKL¾ðŽŒ¿ižd0\Ï 8Ž\ÝIl|\Ì?º?W›ÊŒp\ÓúÅ¹m\Ô\ìÀF£«J÷ò<\ÊZ \Í\Ô\â¦\ãŠðŸ\Ú3öŽø?û#ü\ZÕ¾;|v\Õ‘\áB.n)\'}\ÓÊ°Ä‰A¤vw`¨\'¿AKuûEü5·ñ÷ƒ|\Ã\Î|w£\ßkº]ü~_Ø‚Ú³»\ÊÎ¤y‹y‹\nÁ†rGþkþÍ¯Q{Zpn-\Ë[i\î®g¯”uk±ú\äñ4\ã.Y=tü]—\ã¡\î\Üv¨nØ®‰:\ß\î>y‰{k¦Áª5\Ë\ÛH¶-ó<(‰tG”\ÓnBL@—\nU±‚+°McJ˜E\ä]Bþye‹lŠw”ûÁp~b½ñœw®)Pœw[«ü”\Ö\×.¸sšóh¬üo¨iV£e¹·‚\ãn<\É\ZEbA£ßšô®­µø\Ä\ê‘1ñ2Kl\Ã\ÂV~3¸û±ž\Øù¯\ç_\ÔC|Âµ7¡\nMò\Î3RKk[Kú3òo0ðžAVRµ\ãf¿\àyšC§J(ô\Ï4\å$t\â¿\Ú$\ì8¤\ì6ŠU\Ú{\æ¸}{\âW€</x4ÿ\0k–su\Ù$ª\ã<~5Ï‰\ÇR¢¯ZJ+\Í\Ûó2¯V\×4\ÚK\ÏO\Ì\í\éGZ\á´?‰Ÿ¼IpÖº³gw\"õX\æRß—S]¶\äeÊœ\äd\ÍNF²½))/\'riV…E\Í	&¼š‘ò/í«\Ãkð®=+\Ìd“P¾‰(\Èe@Y=¸¯~ü|\×~Þ®t>Õ \\Ì¦dbK[Œ\à¼@~dw¯jý¸|F\ÇQðÿ\0„!˜p²]I9Áù\çþð\Å|\Û\âg\Ç>h\ÚA\í_Äž0ñ®+/\â\Ç[;8F*\ÚêºŸŠgüeÊ¸‹\ë¸)ò\Ê^Mv}\Ó?t\Ëû=R\ÂKN‘g·C\Æ\ë\ÈenA´8\ÏJù3ö9ñu÷‰>&—}åƒ£LÖˆ;¼°R\Ù\'ž¼Šú×½bp\æu\ÃK\r¦®§œ\Ä4ól²†eOj‘O\ç\×ñ\Z1\éA\Æ:S¾`7cŠlr:×µs\é²‚:W5\â\ÛxJm2gx[zK©ñ\ËGR{«\0J\éù\éHA=Ec^ŒjÁÓ¨®š³¢žŒ\æ5[‹~\"´þ\Ï\Öü]:[¸›#µ™±\Ê\æQ“×’\0±\éœõ~ø\ß\â¯\êzO\ÅÛ«k­`b]`!†H\ä\åûJ¦q\æ.\Ð¨T6\Æ\Æ0*›d¹… £‚¬sÁ{\×\ä¹\ï‚9+,5*\nVš\Ñ\ßõ8ç†ñ>Ñ‚\ê\Þ\êº´u–)2ºU\àö«*r\"¾]ý™õß±\é\Z‡Â­Jå¦ºð\ìƒ\ìþ`ÃµŒ\ß4\r\èB\Æû5õy\Ç5þvñE[-\ÆTÀ\Ö\Þ\r«÷<\Ûtc†=)xþ\í(\êik\Å\Þ?»I\ÆzS\é;\ÐF=(\ãŠp\êi?„þ4\0‡”¼v”õ´\0\Þ?»GÝ§Q@ÿ\Òþ\×\Í\è=(\ï_\Å\ç\ì\ÑE\0Rw¥È¤\ï@zO\á?/zO\á?\0)\ê(\ïA#ƒGz\0ZŽQ˜\ÍILü†€<c\âŒ\î¬\î\Ó\ÂZGG\æ\Í(\çÊ‹8\0¶\Ç8ôÁ5\ä±B!d/\ê}I÷=k«ø‘šg`™\×lZ”$Àÿ\0[ð\ç©Êœ\ãÚ¹Ä¯\ÂüC\Çb\'Œt¦\ík#ô¾\ÂÓ…x\î÷?/ÿ\0nOƒ¿´\íñ»\á‡\Âß†v:u¿…|3%\ï‹uO\Är\Þ\éGö?O’\'·’I?\Òf¹\Æð«\å!;³Šüºÿ\0†lø±\á\Ï\n|øuû`ü\r\×~2øs\à\Âx\ÓÂ³Ã£\ÛGu¡dï§PKK›¤3\Â\Ð)UÝž7…¥*L`\êI4F@\Ç\ã\\\Ù_\×\ÂR†œ,o\Ý;µ4\ß2i\Þ\Óit\Ñv:q¹+Tu\\Ý¼Õ´v·k¤ÿ\0\à\Í=\ï\ìwûR\Ý~\ÊVÿ\0üo\á­Zÿ\0U—\á…´;ˆ\â¸i\çI\àñH»–\Ç\íK vš\ÒÀ¯˜\ê\Ù	V8»\ãø&®ðû\âÄ¯|\rø\\úm\îƒñ\'Áþ—OWE²\Óô\ç\Ö\å\Ó\Ì\Ù2´ý·`_8À¯\è\ïu§r;\×l|L\ÇFRpI^NM&\Òw”%mö÷9}%%\Ô\äŸ\nÐ’´ö\èºEÇ·ýR#— ú\×hÐµ\Õ\Õ\êÊ²‰\æl2ö\Ùòm>\êF+¶`HÀ<ú\×:þ·…‰\Ó_\ì\ìYœ¡\æ2\ÌI\'A$\ä\ã\×\é\ßE\ÏrŽ\Ï\å\Í\ï\É8ò&•ù[w¿s\å<Z\á,fq—G‚j\éÝ§\Õ.‚°!rkÍ¾#üQð—\Â\í\ë+Ÿfìˆ Ne•€\è«\Ôûž‚ºOxŽ\ÇÁúeÎ±\â`mm­\É$ oL“‚ þ W\âŽ<]?ü]¨xŠ\æi\î\"žw6\Æ\å‹2\ÂNU@\è\0†1_\éŸ\Zxñ–\áò¨c2Z±­)\íg·›Gù\Ù\âfe‹\È°«O–«\Ù>‹¿š=?\â_\í#ñ\âT\Ìt7q\Ùmj\ÅY—·™ ù˜ý0+Àdýôq?\ï²K9,I÷\'“N\ï×¥!\Æ+ø³=\â\\veU\Ö\ÇUsm÷v^I\Ìxü\ÓŠ›©ˆ›“ócV(Ñ·\Æ6ž\ÄpGÒ»_üFø…\á\'\á\Ýnò\Û;D¥“þùm\Ãô®7\"Š\äÀ\ç8¼3\æ\ÃÕ”}G5MJošœš~Gg\ãoˆ>,ø‰©[\êþ/¸[«‹XŒ(\á„ç¸\Ï5\É\Æ\Î\Ô\Ðx$Z\å\Ìqµ±s•\\DÜ¤ú½I­VU$\ç7v}Mû0üj|I/…üH4R@\ÆnñMŒ)\'û‡¿¥~°ÁuÂ¤°°tp\n²A¡Wóôw‚rQ_i~\Ëß´$ž\Z¿_‡¾:¸?\Ù\Ó¶S\Êr où\æXôC\Ûû§Ú¿§¼ño\Ø8d¹‹÷v„º/&aý<pŽ	\Ã \Í]©¿‚_\Êÿ\0•ù>£\ÜøŸöƒý¢~!þ\ÍðV_ütñOŠ¯mþøcIðn\â­6{™?³,-¼Rº¢E¬KyQµ½õ•¬rH\Ì98\â_	¿l\ÚS\áÏ~.~\Òz\íüÚ†£ñOKð6§\á=_»˜h\Ú·Šµ\íKI\ÒZX”iaµ\Í\Þ\Ì4’ó¾¿ þ\Íÿ\0\0>(\'‹|-a¬ˆ\Zuž—\â8f\Z•”\ÚE6\r°™¤1”\ÚÀ±9\Î0¾1ý˜¿g¯ˆ0köž7ð~™ªÁ\â\Ó\Ãú´W1oŽ\ëL°y%´µt\ÎÐI4P¬Œ\Ù\\Lÿ\0cb\\¹\áSK¶·\Ý\Þ\ß$­øŸ\Ü\Ë\ës9Fz]µó¿\ák[\æ\Ï\Ë\Íkö\Êý¸¼-\â\ßþË¯7ƒ5Ÿ‰\Zñw„tmy,o-4™­<Ykqq¾}<\Ý\Í4SÙˆ	Ú·,&\à|™$g|Yý¸m†\Ö?¾!\Ù\Ýx2\ïAýž®ôm+\Äú|¶7q_x–\æ\î\Î\Êòòm>Oµ”\ÓC-\àŽ\Ê	\ì\É\"mi\09¦\ß\rcŸ\Ùo\àï†­|!ð\ÇÁ:n§\Ù\ëø‚(\áþÔ…J\Åvò»´²JŠv©‘˜\à\08ª>=ý‰¿dŠ¬þ=üEø£\ë0°kg‡S¸‰šMödµ³È„R´$\æ6•“`cie¸\×OI\ë\êôZùkk®š\Û]\Ëx\\G.’\×\×\ÖÞ¶\Ó\Ö\Ú\î~Rx{ö®ý·¾Xübñ³\â¯ø•.~3Eð\ã\Â\Öú•…Å¼z<ú¼º|v÷Ì—m\æYZ\ÃpH´TYm\Ç\Ï\nÁôKö<ø\åñ§\Ç^?ø§ðö€—G\Õ<Ið·T°´}k@¶š\Ê\Æþ\rR\Éoa\Òy\î^\Þ\â\0JLžs¸À\ÄAñ_\ì5û x\ï_ñŸŠ|_ð÷H\ÔuˆGo\âI¦\ÉÔ’ŒÌ¡Â‰\Å	P,›”6\ìó^•ðGö~ø)û6ø:OüðÕ—†4‰®d½š\Þ\ÍX®fÀ’i¤vi%‘‚¨/#³mP¹À¶Á`qT\ê®i{ªý[\ï\ß\å¯KXÒ†\Z´f®ôõ~ð=-c\Ùÿ\0g\Ùô\ÖøŸ\â¹õc‹Te·‚\Ú;ek(“q‘AÀe2¹\ÉÁ\â¾Çˆ\ä\ë_\ëV:\Äz¦›\ãy+¬h\Ò4–\âaòKŠVH\\ŽBºô#£\0pqŠú·\á¯\Äý\'\âœ\ími¨Z¶R¬\Û=ú2®8\"¿†þ|ŽÁ\æ\Õ3;9R©g\å{X\ä­	;õ=Hu4µHKqN2\Õüð\Ó –“½h\ïH\0u4Ÿ\Â\ZQ\Ô\Ò	 =E-!\íK@Q@ÿ\Óþ\×\ÏJ;\ÐzQÞ¿‹\Ï\Ø¢Š9 “½/4\è\0\îi?„þ4½\é?„þ4\0§µ\è9\âŽô\0´\É\nI§\ÓXn\\š\0\à¾ hRkšlcI.­\ØKa’JžU}÷¯\0už	<¹£h\ØuG0úŠú\ã\Ë\0`yÏ‹üú\ì\ëª\é²$7@}\à\è;t ônp3\Å|WpªÇ¥Z–“];ŸGg_V—³Ÿ\Âÿ\0\ÄT\åwt\æ\á?Y½Òµ.s§n\Ñuþômôa\ÇçŠ¦|Á8Û€3’p?:ü„«B£¥Z6—côZXºS<dš\î;x\Ç\Í\ÅÁ\á2\nùû\Çÿ\0´§Â¯‡ªö÷7£S¾K%|ÿ\0´\Ã\å_\Ä\çÚ¾Uñ\í\ë¬NŒžð\äPJ¤mkÉš@}\ËN}>n+\èò®\n\Ì1kš=<ôüõ?&\â\ßøO%¨\è\ãq‘\çþX\Þo\çÊ¾g\éf[Áb9-É¯\Î-7öð¼eûs\Ã^c…½\ÑP[\Ø:©?Z\ì\í¿n\ï\n˜\â¿ð\Ý\äQ0;%ŽB ^3õÈ®ª\Þæ”µ•/¹¯óG‰—}\'8\'ýyEö”e\Í~§\Úú•ž¥e6Ÿ¨ ’\Ú\á\Z)Q†C«¡¯\Æ/ÿ\0|AðgX{«¿\ßhwR•³º@J¨bJ\Äÿ\0\Ý`8¸¯®\ï¿o?\ìtÓ¼=},ª\ß*;¢©¤Œ\ãò®+\Ä¶w†|Y£Æž	ûe´§\æˆ\Ü+®1Á£\ÈaØŠû\rÊ³œW?e\îKuuU©øÏŽü_\á\ç`ã†«Œk\Â\îQ›K\É\Ú.\éÿ\0V> 	„kÊ·ñR¸ S\Ø\Úý¦°£\Çndc\n\ÊÁ™P’@$\0	ŽF\Ù\æ¿o…\ì®šµ £7»¥×¿˜QEFaMþøÓ©¿\Â\Z\0RqÓ½+\âH\r¼ƒvzJCÚâ£ª`}“ð/ö©ŸÀzü\"ž5µ¹\Õmm@û4ðmicSüŒªÿ\0	\Î{Wè—üy\áÏˆš\Zx‹\Â\×\â\Õø=™º:ÿ\0Jü##Ì”;’\0\ã\nqšöƒ?uïƒ¾\"}WO\Ì\Úd\Ì>\×hO\ßS\Õ\×=v=úWô†>:\â°u)`sF¥J\ÖR·¼ŸKÿ\0\Ã\Õ^}#ñ¹uJ9nq.|:\\¼\ßj*ú;õK\Í^\Ç\íxe¥\È\'Æ¹ø·Añ¿‡\íüQ\áÛ…ž\Ò\äeH\êuaØzéŠ–g\'µjÐ­\Z±U);\Åõ?\Ñ&*…zq­JI\Æ[5­\×¸°6·½&\ÚHÁš\\½+x¶j¯\ÔFQ\ÎZ\ç®-µ\í#_¶ñ‡ƒnc¶\Ôm\Ô\Ç\"\Ê	Š\êÉŠP¼\ã<«•=¹5Ñ’r8ÿ\0?4€{Wš\äølvX\\\\¡-\Óþ¿\"*ÒŒÕ¤qžñ§\ÅO|S\Ñ|C\ã\Ýl\Ía¯\Þ\Íe=„gu¥±œf\ÐDYU¾VY\\\×ßª1•<b¾\nñg†-|W¥2\âi-\ÙdŽxfˆ2a`\é\"neXw×¥üø—\âIüO©ü9ø•ªG{|\n\Üé“º$/ql\Ê7!„i#ps´giü_ã¿„Q\ä\ÌrºiQŠ³Ku\Ùí¯÷<zô=œ´^\éõ’¨4½é©‚Þ¿•H`:šO\á?(\êi?„\Ð!OQKH{R\ÐEPÿ\Ôþ×ˆ>¿\çò£=\Ï\åJqŠ;\×ñyû\0`úÿ\0ŸÊŒ_óùR\Ñ\Å\0&¯ùü©0s\×üþT\êNô\0˜9\ëþ*L§špûÆ“øO\ã@Ž\Ï\åFzÿ\0ŸÊ”\ãŠ;\Ðƒ\ëþ*0}\Ï\åKI\Æ(>¿\çò¨\Ø`óRž•«2€:Šhhðo\Ú^\rzO\Þ#¸ð´òA¨\ÚÚµÍ»Á€þd?8\ë\Æ8\ç=«ù\ì\×~9|Yñ.Ÿös_½{b˜‘<Áp\ÝA\Ø}\Í~\íþ\Õ\Þøõñ+Á\Ë\à?ƒ¯gimx\Ô.\'œ\Å+G\Ú$Âœþ#øWŒ~\Íß°O…¼lž\'ø½ZÆºND$‰--ö6P\Æ077–A]1£‡”9ª\Å6¶º\Ôþjñ_…ø‡;\Î)a2ª“£F1|ò\æqƒmôI\ê\ì~7\\|7ñþ™\áX¼yw¡\ßZi„»0°½\ËzSÁ®O\ç*$!º\ÔýkúÉ¸\Ómn­M\ÄHð²\ì1•\núm\éjø³\ã7\ìð‡\âj>¡\áØ¿\á\Z\Õ0Hš\É@…\Ïý4‡…?U\Úk¦–.\â\Ðü¿‹~Š¸ºTý®S_\Ú>ªZ?“\Û\ï?·«eXþ\Õ¨?.G\Ô\×\Ô??d/ÿ\0dyõ4\ë:hbúÁZEý¸ÀÞŸ‘õóŽEw‰ÑBCdt>ã¨®\æ.\ç¼9Ž\Ëk<>:“„—uùw\Ü\î_—\ØR—l\ç$Ÿzkl\'\ä;‡¨ Žô¬x¢‚Yƒtæ¼Ÿþ—\Â\Ù>6Ÿ\Ù\áµUO8jË§¼r){B\Åw¤…DlA”X\0I¯V\\†8¯\ËÏŽß³\ç\Äo~\Ò^4ø\ÉðúÁ\í<O\á\ÃZ„õ)P¬7\Ö\ê&óOó8\r\ÌˆgPxóA^Ž]‡¥VRU]´\Ó\Ö\é+ùk¯‘\îdX>\"sŽ&|©GGý\æ\ÒWò\Ö\î\Ú\Ù3ô7À<!ñGÁz\Ä\Ýý«I\Õ5´Î	p’4mòH\ÇÌŒ9=zWhY…,\'\0g¿¥=_>|i\Õe¯\0|>\Õþ^Ë¨YøWS–ÿ\0±®5[\ë=ZkÉš;TE¸·]:ES«v\ê\ìG\n¿)®ø±\à{\ïø“\ÅúÏ<G\â?ˆ\í¢xN-YµI-Uû&I®&óm%I™,¬¹‘¡jö¥\ÃÔœ½Úš]öz)$\î—[¿\Èú\×À\ØyMò\×÷o.\Î\ÉME6ù’j\Î\í\ék=û\Ó×œ@¬7œqžyö¯=ø]ñ7\Ãü\"ž5ð°™,ä»½³U¹Q†K™mfùC7\Ë\æD\ÅNy\\p?\'n¼ñTýº|9\ã\ÆðEÞ‘sa\ãŠÿ\0W²Ò§\Ùu£­³\Ænn5“rc’”›E·	0X0ù»_Ø›\á\'\Å_‡nõŒ>Ô®\à\Õ¸\Þ\Z\Ô$–\Õne¹³–í¯\Ã\ÇsÁ¦B#\ÎW=\\š”(¹ûK»\'ù\Ýo\è\ïÛ¡\ç\âxWK*®²råŒ’Ó¯5\âµ\×d\ï}¯dÝ“ýp¦\à\ç¯ùü©~£|\áð\Â\0y\çüþT\à½\Ë´ƒ©¥\Ü•M¾€}gû#k9ƒ\Æ:†.„zQ‰¦ºI—\ÌU|aFFž:ò+ôcCÕ¼Aºš>®ñÜ¬Ð¼¢X\ã1„(T`\Ì>m\ÜsÛ½|\åû/øøS\á\ìz\Å\Òb\ëV\"w`ˆ\Ç3ô\çñ¯¢%{«}cOº…\Ö;p\ì·ð2¼öùñšþ½ð›9\Ç`©\áð\Õ\ê7ö}\Ò<ñ\Û:\Âq^\'x§õ75ÕµO«\Û[Œ±É§˜ŽÜ½Jy\ÚW5ý]}Oõ\Ú\ÖjÂž\Ôu4„(À\Íÿ\0‰u»Ohw¾$\Ô7{y`±X\Æ\ãŒ\àg¹­ƒ\n5k\ZwÆ¿ˆ\È\Ö\â·Zš’ ŠXð\'œ¯Þ™\Õ\ÏË’¨=O5W\ÄZT:Þ…y£L¥\Ô2BCŒ:‘\Èük\è_€š²k|9<n¡³Ž\ÚB3öÙ…³žz¡¯\åÿ\0¤\Þ{Ž\Ã`iP\Ã\Ê\Ô\ê;K\Ï\Èó1÷mG¡\ì‰÷G4¸9\ëþ*½\ëøP\â\Ï?\çò¤Á\Úy§¦“øO\ã@Ž\Ï\åKƒ\ëþ*8¥ Áõÿ\0?•>¿\çò¥¢€?ÿ\Õþ\×\ÏJ;\ÐzQÞ¿‹\Ï\Ø¢Š(\0¤\ïKIÞ€ôŸ\Â\Z^ôŸ\Â\Z\0S\ÔQÞƒ\ÔQÞ€Š( =(\ïA\éGz\0SIü\'ñ¥M\'ðŸÆ€Q:1`Àô\íR\ÑI«ND/\Ã\'^ù¯—¾3~\È?¾3\Ã5\æ¥`4\ÝQ\×dRg±‘G\Êÿ\0ˆÏ½}[Mþø\Õ\Âmu<lï‡°YŽ\áqÔ”\àú5»·\Èþ{>.~\Ã_þ\Í6£¢Z/ˆtx¹YÞÜ¼?{\×n\á_¼rý¥¡h\Ù>YJ•>„AýiÊ›³¸ŸÂ¾zø»û/ü#ø\ÏM\â½1\"½*B\Þ\Úþ\æ\áOl²ðÀz0\"»\éc–\ÒGò§}¨Ïš¾GW•ÿ\0$õ^‰\ï÷\ß\Ôþk‰\nHS“\è:\Ò?\ÎC¸\ä~u÷\ß\Åÿ\0ø\'·Å¿E&¥ð\êTñŠeŠÆ».\Ô¸rªGÒ¾\Ô4\ÍOH¾“IÖ ’\Úò\"VH¦BŽ¤uÊ°»\á4\Õ\Ñü›\Äüšdõ}–cE\ÃÏ£ôkOÄ©”o¾3Y¶ZF‹§j7zµ…”0\Ýj3u2(1‰vFda\Ëm_•s\ÐqZ‚&\çv\ßZ~»\Î(G\Ë)5{ù{K¹@\ÜJe7øO\ãB\ì!\ìw“\Í7½¨£ø…\0©®\Ï\áß…fñ¯Žt\ß\rD¤­\Ä\Ã\Ìô¼±?…qOòõõ¯½¿d/ùwž?¾‹\r6`¶,vŽ]‡\ÔñøW\Ðp\ÎXñxµ‹VyY\Ö`°\ØiUgÛ¶\ÖñY\Û\Çmn¡R5\nª:\0¼Isw0< ‘\\U†AÏ¨52gði\Ý\ëú\Ú8\É8icðH\â\'\Í¯Ä\ÃW\×öòÍ¤\êlQ‡Ù˜pY1÷OmÃ§¸®\Ø8<W¨@\×\ÍgkõFþ\ë‡‚:\Z·i\âi )¹“»ƒ2\Ñ\çÕº¯#¦k÷~	\ãŠU©,>.V’\Ñ>ÿ\0ðOõ§\è\Çô¢Áf™|rž!­\ZxˆZ1“\Ä[-^ŠK®ºô±\Û6\á÷E#6\Ò=\èYV@9Eq\Þ&º\Õ%’Kh¡’ñ\\_,QT•A‚IŒ\ä\0qÖ¾ó3\Ì\è\àðò\ÅW~\ìU\Ï\îª\åRIA\Þ\åýG\Äv\Ör5´Kqp¸\ÂÆ„©b2º=òx®ÿ\0öP\Õe:‰<3x²\Å&Ÿ¬\Ì\ë¤‘]*Î¡H\ÈÛ¹˜Œ\Zó‹k(,¬Å•¨Ø‘€:“\ê}\ëC\à½\ïö\í}§>~ Ñ’U\Üç™¬d\ÚÀ/L””dŽÂ¿Šü]\ãzù\æ\nT\ÜTau\Þ\Ý\ÏO?\ÈU7·½\Ú-O»cû‚Þ‘>\è¥\ï_\ËgÄ°M\'ðŸÆ”u4Ÿ\Â\Z\0S\ÔR\Ò¢–€\n(¢€?ÿ\Öþ\×\ÏJ;\ÓN\ßJ8\ÏOÒ¿‹\Ï\Ø\ÑMù}?J>_OÒ€IÞ“\åôý)8\ÏOÒ€\Ü\Ò	üi>\\ô£Œ(Çµ\é§Ÿ¥g§\é@¢›òú~”|¾Ÿ¥\0)\éGziÇ§\éG\éúP‡SIü\'ñ¤ôý(\ãŠ\0}ß—\Óô£\åôý(\Ô\ß\á?/§\éI\Æ\0\ãIü_…!\Û\éúQ\Æz~”\æ.†¼c\âÀ†?,\Z\Ë\ÆúL3\È\Ý.\0ò\îú¤«ó¡${WµŒz~”¸\éúV¨\â\îŽ<~]‡\ÅStq0R‹\Ý5u÷3ñw\ã/ük\Å\ZW›¬|¿þÕ2\ßa½!\'Ç¢IÂ¿ü)¯\Îx[Ä¾\Ô\ßAñ6Ÿ>—{w\Ãp…\×á‡¸¯\ê\ÊEY–A\ç<ŠüŽÿ\0‚¡Yið\'‚\ïŠ¸‘\ïb\ßþÂˆ\Î?3šôhbe6£#øû\Æ\Ï2œYW8\Ë/7\â\ÓvÓ±ù7Mþø\Ñòú~”œ`ñ]gñ`\ãÚ\âœz~”™R\Øi¨·°>\Ð5O\ëv\Ú–\æ\é\Â ýIô\0s_²>ð¼~ð•‡†¢Áª3701Ç¹\Í|\Ùû,|(Ÿ\ÃZTž6× ò\ï/F 0\És\ÏB\çô¯°I\ã•ûgdT\Ãûj¿¿òN3\ÎýµO«\Óøc¸úNôŸ/§\éI\Æz~•ögÁ\Éâ š–#®\à\Ü\ëSz~”hô5¥VQi\Å\ØÆ²\Õ\Ã:¬ZL®¥\Øo)Iù–D\\\íQ\Ü0\í\Øû.\èV\Â÷:½þZ\â\êVpX\ä\Å\ác¦3ÜšP\Ó,5 t¹’\ß¼ŒnS\ÔUm?X–\Ê\î=\'^+\æ\ÎØ‚T	•a\Ð0=p}«·Šx‹_°.^\êûß‘þÀ}¾¹v/\rO…óª\Ï\ëwj[ItŠ•þ/]ú™]Ð—\ëŠ×®\î<9\âøþ\Ü1þ\Ç\Ô\ã[€½Z\Ú\ë÷\0I\nYXj\íœ}\Ö‡\â]1µŸ\r\ßè±’­uÆ¬2\n±Sµ¸ô85ùn&”gIÓ¶Xÿ\0Hó2­‡•\'\Ôý\nG] \np ž+Ç¾x\Öo|*\Ñ<Qy…¸’\Ýc¹Q‘¶x¿w\"I †©¯]B	\Í~‰Ãºst\Þ\èüFI­\ÉGSIü\'ñ¤ôý)8\ÛÒ¹‰zŠZaÇ§\éKòú~”\0\ê)¿/§\éG\Ë\éúPÿ\×þ×‰>Ÿ\çó£\'=?\Ï\çA\Íæ¿‹\Ï\Ø\Éôÿ\0?>Ÿ\çó£\æ£\æ \'\Óüþt™9\éþ:_š“œ\Ð“žŸ\çó¤\É\Úx¥\ç4œ\í4\0¤ž8ÿ\0?9\éþ:x£œ\Ð\äúŸÎŒŸOóù\ÑóQóP}?\Ï\çFNzŸÎƒš9\Í\0\0žxÿ\0?&N\Ó\Å(\Ï4œ\í4\0\ìŸOóùÑ“\éþ:>j>j\02}?\Ï\çM\É\Úx§|\Ô\Þvš\0RO§ùü\è\É\ÏOóù\ÐsG9 \0\Ï\çó “ƒ\Çùü\èæƒœ\Z\0cy\éŠü\Èÿ\0‚˜h·ÿ\0\nôO[Ã¾=?QÄ¯”Y“\0\çÐ°Á÷\Å~™¹85ð\Ïü$Ÿøf»\Å\î\×v¸ü$\æºðšTLü\ß\Å\ì2­\Ã8\ÚrþFþ\íCð7>\ß\çó¦\ç ŒT‘$\×¬\Èe‘\Îe‰ô\0g5\ï¾ø²\Þ\è6ÿ\0<A¦xz\ãÄ\"iú}\Ü\â;û¦‡™$\Û\æ2¦„,BH¯¦Àe8œSµs\ä\æ/J‚\æª\íý_òG‡\éºv£«\ßG§ipIq<‡`³ ö\×Á/Ù£Q\Óuh¼Yñ5O$\î‚Ó†%»4¸ô\çšú\'\ág‚¾øP\Þ\éžš\ÖòûJ›\ì—ò$±\Íq\å_*m¤˜œ\Æ\èû\ÖV\Æ®\êO\ZøF\ÓÆ¶\ß\'\Ô`Mv\î\ÊmFß¾’\Ò\ÞH\â–e^\è,jOb\ÃÖ¿S\È8žJ¶\'Þ—nˆüó7\â\ê•y¨\á£emú\Û{ù+D{Bü«\íO$\àñþ:Lõ9ªzŽ£a¤ióêº­\ÄvÖ¶±¼\ÓM+Ž8\ÑK;»…UPI\'€M}\áðNJZ²}?\Ï\çI“žŸ\çó®{Fñw…¼E\áko\è\Z­\î‹yj·°_Á*\Ém%³§˜³$ªJ´e>`À\ã\æ¦ðß‰|=\ã/\Øø»\Â7öúž•©À—6—–²,\ÐOª\Z9\"‘	WGR\n²’\äPcVƒô\ÛG\ë\Ø\Ûó\Çùü\é2vž+›Ò¼g\áo\Ä:¯„´mV\Ò\ïU\Ð\ÌQ³†Ty\í\r\Ì~lxÁ-–?7¹y\Û\Zø?Uñ§\á3V³¸\Õt_#ûB\Î9‘§µûZ—·ó£t~j‚\Ñ\îpŒ\â‹\Z}U\ÛTôIü¬ý5_z:s\Û\"°u\ÛX®m–+,r$‘3û\Å9QŽ\àž¾¢·I÷¬\ÝÅ«^\Í{«›IZ\Ü€u\êH?•r\ãq1§Ióu? þŒ>ã¸Ÿ‹0ølgKÙµRS[\Æ1}<\Þ\Ë\Ô\èí¥šhÄ·	\å\Ê@Þƒp¥Y\Ûüþt\Õe”y\Ñð:~T1`¤ûW\Å6\Ñ8r\ÅF÷¶šO\ìr\"\Óü¯øY¤ó.t\ïj@AùÉŒö\Ú\ÃÚ¾Ãˆüµù³\áiž\ný¨ü/murl¢ñŸwk\"¢±7PÛ‡\n+óaŽ\0f¿I“\ä\'µ~E\Å8_e‹—gf~5œa\ÝL\é¾÷û\ÉA<ñþ:L§ŠQž´œ\í5óg˜)\'Ž?\Ï\çK“\éþ:Cž)~j\02}?\Ï\çFO§ùü\èù¨ù¨ÿ\Ðþ\×\Í\é`f¿‹\Ï\ØQI…£@IÞŒ-&h{\Ò	ühÀ\Í&\r\08õw¤ Qš\0u˜Z0´\0”w¤ bŒ\ÐŽ¦“øO\ã@4˜4\0ú)0´ahi¿\Â\Z\\-7i ”w¤ Qš\0Q\Ô\Ðz\Z@40h	\r»=|\ßûNx7Cøð\ÛþŸ³‹y®\"l#c°–\0\Zú6ooZù«öm§\Ã~‡Qn=\Ñå¯¡\ázq©£NkKþ‡\å¾8W•.\ÌjGe/\Êß©òƒ¾øÀ‘\ã\ÃZtp\Éÿ\0=\\o“þún,W\ã—ü‹\àV³ûDþ\Ô?³·Ã¯\ê²h~)·ƒÆšÇ†õÜ¡¶Ö´»+»	n‹ÏVU<c\í_ºxšÎ¸Ò´Ë»\ë}V\ê\Ú)n­7ˆ&tV’! ö9—p\06\È\×ôþ\é.Zq²\ìñ†ø¯\Ì`Ÿ4£¥~ò„¢ž·½¹®\×U§SùVð_\í¥qâ¿€ÿ\0~ \êókÿ\0|Iñ\ãzUÍ¶¬Xxq\ìõ;i\ë¨Z^k:œSCcoö\Òm,\Ë1D\çlpWÿ\0µ\Ç\í5¬| ø7ñ\Â\×_¹–[\Ïx§Iñw,zŽ§£øj\ÓÅ–vk6Ñ¢¢^\\EobK„\03Üˆð¸\Ö^£\à?jöW\Zf­¢i÷V×“ýªx¦¶‰\ÒYñ6Ee!¤À;\Þõf\ÓÁþ\Ó\í\Ö\Î\ÃI²‚%ŠX#‚5QÍ¾XÀ\n\0I—^Œy š\èö\Ê\Ç\ß\Ã\Å,¶\rJ¤¯¬¯\îò8r\Ý\Ç\Î/[¦â¹”–‹ù\Ø\Ö?k¿\\ÁHü#\àÿ\0„_µ\ë\Í\ß\Æz_…/ôýS\Ä:S\ÙjW\ZO\Ú¬ô\í\Úöxd†o\íi\'¥Üª\á?gŸ\Ú{\â‰¾\'ø.X~7\ê><ñŠu\ß\è*ð=Ì–“\Ú\èú.“¨ö7-gBh]$·µj˜N³ù|Ž+úQ·øqð\î\ÓUƒ_¶ðþ™õªÆ\Ü-¤\"h\Ö\Û\Z£„Ü¡\åP\Ú8\æ¿?f¯…_³¿„\Â>°I?¨N÷·Q\Ä÷’\rJökù£’eY£\ÎÁ\ç\n‘šn´yls\Ë\Ä¯êŽœp¶’„`¾v¢rmÅµg(\Ê\Ê\Î\éj¬~?~\Ìú—\ÅÏŠ\Þ>ð\Ä¿u3\Ãø\ái\Æ\ÚGRÔ£\Õ ’[±\ä’aò\à†=ˆ\ì‰ý\Ø\ãO~Òž*\Óþ\Z|9\Ñ<]ñ\ïQøOi\ì\ã¤x\ÃKƒO¸±±\Z·ˆ\á3¬H<\èYd«\Ú@\Ü\r p•ýX\Ùø{@\Ó\ß~Ÿ§\Û@L	k˜\âE>DY\Ù\n?v››j}Ñ“É¯¸ý–¾\Þ|p_7údW\Zœ^¶ð\Ôr\Å\ØÁiiw%\äO-\Ù*\É)”€\03JRÜœ¿\Äl\ÄN®#¹l¹RPz¤\ï¼Z÷›n\í6¶\ì\×\âOŠ¾1þ\Ñ>.Ñµ9,¼Qu\à?x\ËRø5a©_\é\Ö\ÐCug/ˆí¼­H¤C—ù\Ø*K»aUq\\\í}á‘¥x\ã\âg…¼kñ?Äšð\ï\Å£¸\×ö\Ú\Þñ¢¸\Ùo-ö¥xmŽù#8¹¸	\Ã\Ì\Ú¯\éŽ\çÃ¾¼¸{Ë«ify\"•\âFc$\Ää‘’ÑŸ¸O+\Û^ÿ\0\Â~\Õc½‡T\Ó-.WR·k,1¸¸XøA(e>`A÷Cg©ûekX\Ë\âM*5£RžE.]¹o£¥\Õ\Åô¦Ò¾‹šý\ïWÀ\ÃLO\èË¢ê¯¯Y}†\ß\ìúœ“%\Ã\Þ\Ç\å.Ë–š0V™p\æDX\Ã\0Ö­«.‘tÄ\Ü\ÜK¸\0\í€s\ëœW­\à´\Ó\í#³µD‚\"\"\0¨Š£T\0\0\0pE{ªX\é¢\'¼f\Ä\Ò\Ç„F‘‹J\ê€\í@\Çh,¶6¢å˜ª‚G2’•9&{ÿ\0GŸsN\ã&/*‹”§5	F\×\æŒ\ä“VK{uKG\Ð\é\Ø|\'\Ýô¦7*@ô¤\ÄsKžk\á™ÿ\0Fª,ù¯Ç°ÈŸµ\ÂY\ÈùZöu\Ýv’?&úý†\Îq\Å~7|K¿¾ö½ø)§G†\ÖYõY$(ò\ÖHÍYˆ\Èf\å@8![=?eAù8¯Ì¸\Ý~þ\ì~O\Å	,d¿®ˆx\àR	üiM7ýk\âÏzŠZiŠ\\-\0-˜Z0´ÿ\Ñþ×‰>Ÿ\çó£\'=?\Ï\çA\Íæ¿‹\Ï\Ø\Éôÿ\0?>Ÿ\çó£\æ£\æ 9Áâƒž(9Á ƒ\Ç?\çò ù©y¥Áõÿ\0?• ž\Ï\å@\Î(9\â‚?\çò ƒ\Ç?\çò ù©y¥Ç½ ž\Ï\å@\Î\r/\ÍHAÁ\çüþT¸>¿\çò \ç4óFzÿ\0ŸÊ€<ÿ\0ŸÊ€»¿5!u¥Áõÿ\0?•\05\É’(\É\Úx¡‡s´\Ð’}?\Ï\çFNzŸÎƒš9\Í\0\0žxÿ\0?œ?\Ï\ç@\Ï4\à\Ð\àžœ\â¼\ã\Ð\Ñ[G\ÒÎ¬H¸y³\Æ\Öyn8ÿ\0cwZ÷ËŒ…>¤\×\Ï_´“©j6zÕ„\r4v7­4\ì½#Œ\Ã\"\îom\Ä\nú´s:\r÷ý\Ê|q¥)ðŽc«·J_‘\àÀœtÿ\0?&N\Ó\Å\0zš9\Úkú€ÿ\0§J\Ê\â’x\ãüþtd\ç§ùü\è9âƒœõ \È<ñþ:	8<ŸÎŽ\çšBx\ê?\Ï\ã@\Éôÿ\0?&NzŸÎŒûóø\Ñ\Îh\0ó\Çùü\éJœZ;œšü·ø\çûrüGøaûkøk\á>ƒ¥X\Ü|0°¹Ò´o\Z\ë2‰Z\æ\ÇWñO\ÚSDŠ¬#TY`€]RUo­Û€IN<\ÓP[½>}>÷¢óiöI\ÃØŒ|\å=›Œ\\û.‹»m¤’Õ¶’?QdeT37E™\á\é\åÔ¯?\á!³\ÚÖ—(²	8\'•Á?)\Ï|\Zü\ÚøÁÿ\0AøEð\Ë\Æ\Þ\"øqs\á\Zß¯‡µ«o\ß\ëz^’·šl\Z®£mÅ„\n\ÑNg™\î\ÌQF\"\Ê\ÊÊ²ùjñ³ûŸÁŸ\Û_\á—\Ä8ü9\á\ì{Ãº\î©\âK\ßÏ¤\ê\Ö\Ö\Ð\Þiú¦¦K«H·‚\Þ\æxBKi›Á,\Ê\Þl}2\Û|\ì\ÆeIû%}/ò²wûš5\Ý_ý&ú\nø-õ|\Ö|GœÓ•7*<\Ú)s¦¶\ßd\í¶Ï³·Ü’¿˜ÀžÃ§¥0“Œ\â¿>uø(\Ç\Â\Û[]\Z?xO\Å~+\Õõ\ÍKÄ–0\é\Z-Œ\É…/\ßN\Õ/\äVºŽ1k\ê‚0²5\Ä\Þlk&F(<»Á\ßðQŸþÈ¾ø÷ño\Â>+ñ5ž©\á˜|C\â\r{I\Ó\ì\Ò\ÏKµrw\Ív³\\\Ú1dP\Ï$‘O2\"–1Œ®ï—†m¤——\çøhü®š\è\Ïõ{ûW®”¶W~[~:­7³Oª¿é¿‚<+\á_~\Ñú\\~ S<\ÞÒŸR¶Œ]“\\L\"¥s\Â`)\È\ïŠýŒ¥½~+þÁ¿µOŠ~4~\Û_¾7\Ãýn\Ã\Ãþ·\Ólô\ïÈ¶¢\Æ\å_h>gúI¸\à\È^\ÛË‘¢Reh\ßjÚˆŽõù¹}q§µ•¾‘ù>q‹§_R¥7u{}ß¡!$ö¤\É\Úx¥æ“¦¾HóE$ñ\Çùü\ér}?\Ï\çHs\Å/\Í@O§ùü\è\Éôÿ\0?55\0ÿ\Òþ×ˆ>´`\ç¯ùü¨!i>\\ö¯\âóö\Ø>¿\çò£ÖŒ)£@FG­Š\\-&hp´˜^M\\\Ò`m4\0¤P@È \Å \Â\Ò|¾\Ôah\0 b—HBÑš\003F“@šL\r¤\Ð•¥\ÂÑ…£@\ÑMÁ\Úy§ai¸M\0)ÖŒõÿ\0?•\n03@\0ž\Ï\åAš\0\Ò¸\í@¶mnk\Ë~.\Í}ið÷TŸLŒ\ÉpDj\0RÄ«H¡°=	¯WÚ¤\æ“\ËN¤WN\ìkFª\èyy\æYv\n®N\Êqk\ï?<\åt·¹S·—6\Ýûmb¾¸<\ãô¨ž\î\ÙA&E\ã¨\ê.¿Ö¾õÕ¼-\á\Ýt \Ö,¢¹òþ\îõŒöú{t¢øn\Ø)·°·C¶5\ÇLqÚ¿_^,C‘/b\ï\ë¡þ~\ÕúÞ»\å\Ìw\ä\×Ï­®/àµŸ\ì\×[\ÒO\î•l¨\Ç\ê¶\×YXf\î{‚DQ@»\àp \ëôS\ì–À\ä \É\ë\ïU-ô]\Î\á®m-aŠG\Î\çDUcž¹ gš\æÿ\0ˆ­+?\Ü\ë\Ó_ø­ f5`ÿ\0´$\ãyr+µ\ä\ï£ó³>*\Ò~|Eñ8‚q³E´‘ÿ\0xg®‚\îÅ‚¿7ûM;Wª\Ú|úT‰w«¼÷Ž¿»Ä©\Z‘\Ó(	?^k\èõŠ5\Î\ÑN(¸\Î+\äñ<{™T©Î§o$~÷’ý¸\'‡Xy`\ÕO\ïI·\'ó>1Ô¾|@±Id¶µ†\íb\è\"”z¨m f#Ž\æ¼\âò}OO»ž\Êÿ\0O¹†kr# \Ý\Ï € úŠýh\ÑT•¯>ñwÃ­\Å\Èn.\ÛÞ„Ø—18FG\Ý`f½{¹_‰¸•5RN\'\Æ\ã>…\\Z¿´n­8öŒ—\ê™ñ+j’yJ\Ëipd|•M„S\Ð~&¿*üKÿ\0ŠøWñk\Ã_õo‹ºœšŸŽ~$j—Ú¤>\"Š9`—H”üº?•w>\\§I‰-\Ò\"\Ì7˜‹ü­#W\í×‹~xÂ¾›[µ˜jQ2Šˆ\"À3I$)\'\ë\\ •!Ü®2¨¯µ\ÃqW\×–Û¦ž©þi=;»ø%ôV\àþ\ÅT\Æ\àœ«\Í\éû\Ë4—n[$û\ë¥\ÒvºG\ç„ÿ\0°n¡©x\Å\Z^±\ã–\ï\Å^=ð¿Ž§¸N¢K\á\ÔÑƒ\Û,B\äŒ]6[\Ì\Ü<¯;$òòù¾>ý…<{}\ãc\â\×\Â_ˆ1x{\ÅW^=Oió^io³ƒ>‹Ã“\Ø\Ü\Û-\å»Ü¤,’¬‰4”m!	o\ÒR\Êø·\\¾ðö%\æ\0¹¿ã¶´„\äù—0D\à“ŽÀ\×Gö•H¦Ü¬­n‹Oq[\ÒÐŠùy»ÿ\0O\Ô\É0T\é]\ÇH\ë»þû\ï½\ç/¿\É[ò\ïÂŸðN‹\ß\n­<âŸ†?£±ñ¯†Å°\ßëº‡\í\î£\Ô,<]ªÿ\0k]h—0$V÷	C29‡*û­Š?–¾\ãÿ\0ø\"Þ¿ûRxC\Â4O\éZ•–…\àuðv¡\â=\Â\Ñ\ê\â\ÒH%yN¡¢,—\Ñ%•ý\Ó>Û‡Œ\ÊLq¦\Ù#hÁ?Ò§‚gŸ\è\Úz?Œ-“_\ÕØ‰.//T;4žˆ¿uz*€:\äó^ñmceg\n\ÛYÄ±Fƒ\nˆ¨À`\nø\Ë\Ä\nŠ_\ì\ê\ÒOGe\ç·þô\ÛSó¬\Ë0\ÃÔ¥*4i\ÙKGyI\ÞÜ¶ë¿»w\Ó\Öÿ\0|*ý•|eð‹ö ñO\Æ_\nx®\Ðx7\Æ\Z^™o}\á·Ò¿\Òc\Ô4›qgÅ¾¢·ARÝ PÝ­]·\Â`	Jû\\.Ó€iÁTqIšü\çŠ©UóTwv·\Üx4©F\n\Ñ\0<ÿ\0ŸÊ“)\æ”\Í&\Òkœ\ÐRÿ\0ŸÊ—\×üþT„)p´\0`úÿ\0ŸÊŒ_óùQ…£@ÿ\ÓþÖ¥\ÉÀùÿ\0ð/Mñ_\ÆO|E\Ô<s\ãŸ\é±i^*½\Ótøôù-âµŠ\Ö\â*œ\ÛHAŽK·5ú&w\×\Ëÿ\0°ü*ð|R$uñÞ©‘ôHkø\Þ§J„\ç\îº\'ù£ô¼\ÞRP\\®\Çkû7\Ç{\Ïgñ\Åò£td½´`iV\Ç\ì\Å/ˆ~1ÿ\0À\ËOþD¯IÔ‘~\ê°jv(mô‹‰ºŽ%R‹4\ÌÉ·‚2O\Î\Ù\Ç;×¦Á{ku¸†Tx\ÏFR?ˆâ¼Ÿ\í¬J×™\à1ÿ\0#ÃŽ\"}dþö|\Õÿ\0\Å/ýOÿ\0\àe§ÿ\0\"Qÿ\0\Å\'ýOÿ\0\àe§ÿ\0\"W\Ó\áU†A£`\Î?\Ïó£û\Ý\à1ÿ\0\"½¼¿™ý\ìùƒþŠOú(ž1ÿ\0À\ËOþD£þŠ\\\ÉDñþZò%}«\êphº]Î­r¢µ‰\åuK¹Tª9b@\àI\â¾KøGûlüø²:5Æ†­(‚\Ô|¥y¤h\ä—`XžFGX\âvd#\0:WV1\Ç\ÕR•8\ÞÖ¿»¿\"e‰j×“û\Ù\Ó\Ã1Iÿ\0E\Æ?øiÿ\0È”\Ã1Iÿ\0E\Æ?øiÿ\0È•‰¨~\Ü³mžµa¥Yx‰oRõ&\Ü\Û\Å,–ñ$\ßkrò…\Ú•ƒ´e¹Ö†—ûnþ\Ë\ZÍ½…\æ•\ã;Kˆõ)ž\ÞE”¨‘&[vYLE‰]S\ç\Ûó;\Ö\ïš%\Ì\é»}eÿ\03û\Ù©û,\êW–ocñ/\ÆV\Î\Ý$[»2\Ëô\rfG\æ+žŸöeñ¶“fn?\ácx¯Q(Å~\ÓkÀþZ°-õûWÚƒwÒ‚	5\çÿ\0ob{¯ü?\ä\ÚwøŸ\Þ|aû9\ÇOoñ\Å\ìg‹\ËLûƒþ‰\Ô\Z¿ÿ\0\Å/ý?ÿ\0\àe§ÿ\0\"Wº^xZ\Ù/\ÓV\ÒX\Ú\Ý†t\è\àòC©\à\ä÷\àû\Õÿ\0\êrj\Ú\ÚnL{\ÖGŒùg#(\Å~ ñ\È=:V<\ÄZ\é¯ü?\ä(×\ì\äþö|ûÿ\0\Å\'ýOÿ\0\àe§ÿ\0\"Qÿ\0\Å\'ýOÿ\0\àe§ÿ\0\"W\Ó\áùÿ\0õÑ³Š\Ëû\Ý\à1ÿ\0\"ý¼¿™ý\ìùƒþŠ_ú(ž1ÿ\0À\ËOþD£þŠOú(ž1ÿ\0À\ËOþD¨¿hÿ\0ÚƒGý.|7gª\é\í¨K\âk©\ím³p–ñ\Æ\Öð4\ì\Î\îT…\0rk\Ä.?\à¤?	4o\Z^x/\Å\Ú>³`út^u\Ý\äv\Ísjˆ¶p\ß;¬‘^4Ša¹ð\0Á=¯ZŒ³J”\ÕXF\éß¤z;v0–`“³“û\Ù\îŸð\ÌRÿ\0\ÑDñþZò%ð\ÌRÿ\0\ÑDñþZò%q–?·‡\ìÿ\0qª\ÝiW:”\Èl\ï\Òi\Ò\ÞYm\âý\àŠ\'šT]±	œ\íqùœÿ\0ðPO\Ù\ÞX,nl¯\î6^È 	\í\æ‚O&Hfš9\Ò93\Ç\"Á F\íP\ëfiÛ“ÿ\0%_\ä5Ž¾\Ò{=þŠOú(ž1ÿ\0À\ËOþD£þŠOú(ž1ÿ\0À\ËOþD¯Fø-ñ§\á÷\Ç\ïEñ\á¥\ã^\é¯<\ÖÅž6\Òkw1\Ë£rX`×¬\í¸*\ç8ºrp“_Ýù\Z¬Dž¼\Ï\ïg\Ìð\ÌR\ÑDñþZò%yWƒt\ïøö¡Õ¾K\âm[^\ÑÏ†-5$U’Z+‡¼ždh¢ˆ\á‘sÓŠû\Ð ÿ\0?þºø®ôgö\Þ\Õ?\ìI±ÿ\0Ó…\Íz™>cW)Ó©kr¿²¿Dt\á*\ÉÕŠ\æyôbz\n’¢LóŠ“\æ®S\éÅ¦ÿ\0	üi~jo;M\08ö£½!\Ïsš\0Q\Ô\Ðz\ZAžh9Á ##\Ã\Z–\æŸóRsš-\Ô.V–¤%d\\‚1Ï¥|9®hóx;Uþ\ÊÔ­þ\Å\ÌÒ›P[tN¥Ø…G\ìvó°\àõ\Æqšû¬‚r+XÐ´j\É\ìux\â\Ó\Ó#\ÐûŠö²L\âX:Ž[¦z¹Fk<%Oi~\èøgQÕ´\Í\"!s«L¶ñ’sÕ˜ôUQ’\Ì{\0	®§\áŸ\Ãx\ß\Å\Ú/\Äo[ÿ\0ghúL“\\YZ\ÊX\\\\J\Ë\å\Ç,\Ñ2PXª’[$Ž•\ç_´«\ïübð\ÏÃ­-ä²»º½ŽóJÔ¯?\ã\ÙDC3[™?å¤®›\'Rs\Å~‹¤{À$óø\×\Öñ&%B+´×©\ë\ç\ÜJñ1T¨?u­{ú&wšš˜ Ž;\æ¯\Íâµ>LZNô|Ôœ\æ˜\n:šO\á?5›h$œT^a\Ç,1Ò‚\åƒÚ–˜	 Ó¾j@-Ÿ55\0ÿ\ÔþÖ¤ûÂ¾,ý‹õ\ßÃªüU´\Ò4±ugo\ãH\É!—Ë‘‹G	%+µ±\ß,9¯µ\àŒW\Ì_°\Ø\Ä?\ì{\Õ?ôkøÂ¬í†žQúNsðDú…¼q\"Û©\×t]F\Êx%¢cÙ„\r!úV¡&›¦_Á¬h–71\ÅŸ¿‘Q’3vb8\Éw?Zö\\`ðMF\Ñ,Ÿ‘\é^ª»;(ù‘\Ú]Z\ÝÀ³ZÈ®„\nž\ÄdU\Ã9®*ÿ\0Ã—v³\\\ê~•-\î®\n³, ´,\Ê6\å•p\Ý1\È=ª+{\Ý[yº­\ìv÷!Hl™‹98?¼\Ëñ\ÆF)J”^¨ò:\ÍB\Ê\ËW°›L¾d´n¹#*\Ãd`Ž=\r|·uûþ\Í÷\Ï=\åö‡,\×s:?\Ú\Úò\äÜ ‰YR3\ÌU\n\î¸\r\Èb\r{s\è^&º¶K\éo\Ê_\Æ\Ê\ê\"Ü¶øSó)Lü\Û\ÇRy«z\ß\Ä\Ö\êV\ÓY\ÛgpXG±ø\ç F\èsÛ½mKV’~\ÎM\\N\Ït|\Ôß°—\ì¼lÿ\0\á[‰\áû*\Ý\\¬’³;Ä›C¼ #8Ž9«\Þý‰?f\è³øs\Ã\ÞK{;“ºT\ÊwŸ:;Œ’XŸõ±#q\é\é_Q\É}d“i&E“®\ÒÀ7\åœ\Ó!\Ô,\î^D·š9\Z#µÂ°;N3ƒ\é\ÅhóLS\\®o\ïct\Ñ~4\Ë\Z{\Å~k~\Ñ_´_\í¤þ\Õ\Z\'\ì\áð_\rÚ›\ïÍ­\\]k\Ë;ª´sˆ„k\ä0\ÆA\Ï5jðS‰m¼\Ý;SøuvYK(Š@‚}\Ûð+‹Ù·«>\â<(Ñ¥Rµzpö‹™)6›júEö}O\ÒVÉ®9ô\ÍcL\Ô\Éð\Ú\Û%µÁy&I\æ±r\íþ÷Í»\ßÖ¾ð\å\Çü\ï]¶mWN\Ö~\Í¸>Ï¨~\ì¯¤\àƒ\×&³¼=ñö\Ãð\ç\í9\á/€Ÿ´9ð\Ô\Úg‹l5˜®t»‚xž\ÄFpZW<7˜:\n\Ò\n\Î\Æ582R„\åGNn)\Ê\ÊNöJ\î×ŠOO3ô\ÃÚ¼úµ©šu@\Ë#\Æ\Þ[\\£`\à\éÖº\á5\Ç\é^}¤M&ò_*Vóg&l1\í,r3\ÔûÖ„\Ã_€ –;Œõe6g\è\Ù?Ê¢p»\ÐøÕ¶§1ã„~ø‘w§_ø\ÊÉ®n4‰$š\ÊX\æš	 yP\Æ\å¾d%H\ÉWsû0|\n½\Õ5\rjû\Ãñ\\]j¶oay$\Ò\Í#Mo$K«–\ä´H¨[\ï<W²\Ûê‘¹Ž+±\äJ\ã;×¸¡ü+Wz\×D1øˆG’3võ\î.Tõ±ó,ß±\Ï\ì\Þ\Ò\És…­¢–W2³#HHN\àî»¶;#üÉ½N\Ö\ä`\×=\á\ï\Øgöa\Ðü\'g\á¯Ã©Eh¨×$³±Š7‰	r\ÙÂ¤Ž®w\0&¾¹y\0z\×:¾![…a¦\Â÷D‚ƒ	q\çò\Íi\Ç¿;û\Å*q\êŽw\á\ÂO‡<0|ð\ÇKH\ÓMÄ·F‹°3N\Û\år]™‰f\äó^Œ\\\×õ8fX5[&ƒ+\"fX\Çû,Td\ÃÖ›\'‹ þÒG‚)eº`¨€	\Ý÷0hõ\çÒ¹jF¤Ûœõ¸\ÓKC°M|Oz\Ëÿ\0\r¿ªú’l¿ô\ás_TOyâ«¦¸¶´¶ŽØ‚)¤}\á‡vÚ¼Œzµò,ZlZí³ª…,\Ò?‚lZFbIfû}\ÎO=>•\îð\ì-V_\ágfþú\'\Óñ÷©*$=jZ£ë‚›ü\'ñ§S„þ4\0§µ\ÅA\íGñP\0:šC@\êh=\r\0.);\Ò1 dT{Wd£©¨\Ûü\ë\Ã<EûKüð¦­>…­xž\Ío­›d¶ñšTn„2D¬A\ÇZÁ¸ý©¾¶\Æ\Ñ\âÕµ4W´\Ò\î\ä]£©$Æ£\×OY½\"Ìx-\Ù\é¾\Zxcâ¿†%ðŸŠÑŒE–Xf¶Mo:s\Ñ8ÁWC\È#ùW‡þ\Ëüc\â¸<Wðÿ\0\Æ\×þ÷Á\Z±\Ò´0\î\ã%‘GL\ÏŽ~9x\ÏÅ¾¹Ñ¾xcX›X\ÔG\Ù\í/o¬ä´²‡\Ì\á\çy$\Ú@ˆr\0fÀW¢|ø\áÿ\0~\rþÄ±‘¯u[\Ö\Z®£)-5\í\Ñt®I$úA]ó¢\éaœ+o\ÓË¹—75D\à{\Ìd\ç¥¨\ÑqRv¯\îÎ \Å\'z\\\Òw¤þ\Ð~$\Ö<ðÆ¾.ðô\ßf¿\Òô;û»ip—,6\î\è\Ø9\ä\Zü\0Ó¿à¡Ÿµ\Â\ßØ›J\Ðh\ÍV;?ˆ~!\Ó4=cÂž*†%Kmr\Òþ{SqV_.;\ëx\åe’00\éó§|Hþ \Ðôè—žñ\rº]\Ø_\Äö÷0J2’\Å*•ta\ÝYIzW\æG„¾*þ\Ç_´§\Ç/Á<5/\néšž•ð®\r6]>\Ú\â’\ÐOi´¼VÀý×°\Ì\0\ãnõ\"”a%R—2M6ú¥\×þóñqŸ2q•\Ô==\Ú[y\åš5$û•\Í]¨aTH‘#U\Øt©«\åù¯©\ß¦¡EP3ÿ\ÕþÖ¤\Î\ášù—ö\Ï\Ùþ(\ãþ‡½Sÿ\0@†¾š“\ï\nù—öÿ\0Š?ö=\êŸú5ü]ˆÿ\0uŸª?K\Íþ}\ÏóQóR\Ñ_0|\à\Ü{\n\\\Ò\Òw§q©XnMd\ëZe†«§½®£–1ó…\É_™y#>õ±Þ«\\Ä³D\Ñ?*À‚=Z¨J\Î\âzžOðóÁž›ÀÐ›¨\ÅØ¾/4­p\Êw9*¬\ä–mœ(9<]|þð\ÔÒ‰\Ýa8PþNcWU9‚\à0\É=z\Ö‡¢H¾\ZL\Z³\\\Ûü\Û#Ÿ`?(û±\nYPuÀ W¡Z]\Û\Þ\Û\Ëhœ|®ŒHö#5\×RS\æ´v&<¶\Ôü\àñ›h?\àª:;øyy»\0sþœ€gù}+ôb\Þ\Ê\Ú\Þ?\"\Þ5~UP_jü÷ñü¥S\Ã\Çø¿\á^^qÿ\0o\é_¡_Ù‹ñ§<‰ö‚¥\Ä{†ò¹Ám½p~•YKšÉŸu\Æö\åÀ·ÿ\0>cÿ\0¥H\å/-mü;\âñ\n!X.cò%(\n¬[p•ð$ÿ\0*ø—\ãn%ÿ\0‚‡|!¸\ZGˆÿ\0½~ˆ\Ü\Ã\èa“¡Æ¿4þ#@ö¿·¯ÁW›\Ïò´ “®\à¾\å\ÅLe£gé‰«ÿ\0^\ê\éý1X\ÊHÀœ\Óð1ùÒŸõ­øR	ük9³\å¯mNò\Æûf´ºP\È\à©Þ²\âðÆ™\n*\Æ\Æ‘ó\Ç\ã]•F…c“\Ân×tº\Þ\É«DÎ­\Ñ\Ô\0W#wrk£¶´†\Ò!º…UP \0§lœ×‚þÐŸtŸ\Ù\ëÁv\Þ0\ÖtÛM//#²H\íÊ F1\ß4’‘\Äò\íÀ$õ­T«5N\ní‰«+ž\Íw©y…¬*ZR2v2O¦j\r#Iþ\Ï\ß<\í\ç\\Js$¤r}\0ôQ\Øv¯tÿ\0Û—ö}¸Ô­5MkS}4Ï¤\é’H\ÚT¶W,\è“O™’EF1®ü¸•\Ñøkö\ìý›¼W=\ä\ZV³0ki®fó¬®¡!&`‘¨/\åH’±F\rw\ÔÊ±Qºt\Ù\n´o¹öšøª÷\'ö\ß\ÕýI6?úp¹¬k?ø(—\ìñ¬j\ZBxvöK\Ë\ra\\­\æ\Ó\ZG$s›y!x\Üy‚E`I]½+Í¾|{ø[ûAþ\×:÷‹>\êCS³±ð­­\Ãlx\Ú)ã¾™CµÕ½\"½¬-¯F¤\çV\r.Vw`j§Z*\çÝ‰žqR|\ÕG9©k™l®\æ¦ó´\Ó\é¿\Â\Z\0x£œÒž\Ô´\0™#5É‚Ö ¹º†\Ú\Ú[»‡X\ã‰K»±Âª¨\É$ž€\nø\ËÂŸ¾;üoñ¯\âƒ“\éZg„,e\Öz¥¬ò¶¢\Ëþ¶x¶ºb~U<†Á âº¨`\åR2’\Ùis9\ÕQÓ©\êŸh\rü<ñt\íô­S_\×\îmM\ìv:]¿šþ@m›\ÝØ¬h7q’k†\Õ&ý©¾*i\æ\×GŽ\Ë\áÎŸ>A–sö\íQc\èJ¢\âÜŽT\î|zV·Ãƒ\ß´¯ZŸ\Æ‰zÕ…õ\Í\æ•—½„2\Å\Z\"Hd-û\×c’OA_R”\ç 5\ÙWJ…•¦\í«ó2Œe?‰\èyO\Âo„>\røI\á(|+\áX2±³\É,óa\çšYNù$’B7f$ûtW©ª:ŽµH£\Òÿ\0	ükÏ­^s•\äÍ•8¥d†2³M(PAšy\ê(\ïX–„\æ—\æ u4´\0Ÿ51˜ƒRT,WœñI\ßdŠþ\Ñ\ß<+û7üñgÇ\È#\Òü+¦\\j3\äý\á\n¨=Ù°£\ë_\Ç\Ïü?\âwŒ~\'~Ïž?ÿ\0‚„\Ï\\kº\Å;wRA’\ï¤j¨°\ß\Å\ß+N²\ã§\îF1Šúþºý¯<I\áo>ý„¾ù·ž#øv³\Þ\ÚZ)’\á\ìm\Ü¢T\\±ó¦\ã\ä/\Ùÿ\0Áÿ\0c/\Ú\'þ	û\Þøkö°\Ña¼ðo\Ä\Ï6\ë\Äöòj>Ž\ê\n½\Ô|‰a\Øß¿ò\Æ\è:@l~¥‘\à\ã…\Ê%ZOÞª\ÒI\î\×_\Ôð1x‡<L`¶Ž¬þ«\ì/-\ï¬â½´q$2¨’6!•†AÐƒW±8¯ÏŸø\'G\Ä\ëŸ|Ÿáˆµ(µMk\á\Åüžž\ê7ö«X€}>\ìNV\âÑ£|÷9¯\Ð8\ÎZ¿9Ì°Ò£ˆ•­o\È÷)\ÍJ<È›\æ£æ¥¢¹?ÿ\ÖþÖ¤ûÂ¾eý†ÿ\0\ã\ß\âýz§þ\r}5\'\Þó/\ì7ÿ\0ÿ\0\ì{\Õ?ôkøº¿û¬ýQú^oð#\îŠ(£5ó\Î\'z\\\Òw ;*+;\03ùW›\ég^ñc.«u4–:|ƒ1[.\Òe€Á•¶’§¯Ê§§|\Ö\ç‰\ã–\ì[\éj\ì±\Ý\Ë\åÉ±¶°@^ fº¥UA…ÀÀ²vZwcæŒÿ\0|g\âxW\â\ÃK\Ë;mO\Ãlˆ[ß¬Ÿfš\è|§\Äw+!—‚#ŠüøƒöPý¯ü)ñ@øðû\Ä§†\ì,ŠË®Ir\Ñ\Û\ÄM„ð¼Ù¤ùp\×r$\Ùt\Ü1\ÃñƒûDH8¦²\ák\Ù\Ëø‚®>H§\ê—ùS¥£\Ðþc|]ð;\â_\ì\ïûN\Ú\Úü|ñö³yö\Ï\ê±i^!´MF\ä\é÷s\Ý@`\Þ¾AFòm—®«ÄºÞ•«k\Z§‰ôß‹7ÖºµÖŸ&˜º‚\éz\ï\Ú&ŽKµ¸23+nû` ^3Žq_\Ò;\Å‘‘œz\×=\âe–=\ì¦\Ô>Y\0\È\Ï\Óþ´b¯§Ü¿\Èú\ÚùžW^•(\â(M\ÊQº¨’vo[:r·¥\í\Ôü¹ø;ûeü\"ð¿\ÂmáŸŽ¼gªëš–™f-õa´}K}\ãr2Œa\Ü¼\Ç\'ñ¬›ŒŸ¾6þ\Þ	§øGõÎŸ\á\í\\Š\êY4û«h¡ó’-K\Í.[i\Å~´iZ]¦—g…´k1\"¢ þJ\Ñò˜@\0W‹V²”\å&·7¡e\Øh\Êx\\<Ôœ\\u¨š\\\Ê×²§\Ìl·I³\ÉÀÛ¸žÀµ\ã\Ú/\íðk\ÄR4Z\'ˆ-\'	jod>\"Š\r\ÛwK!#\É\è©=@\"½z{Vž!-÷Á_¦F8¯ÎCþ	‰ð>OI\à¯j:Ž“cvbšúvŒ\Ã{wo7\r\Õ\ÄL»ZU9F#\Ó\n\ÙÀ\Ç^_O6\Ö&Mz\'R÷G\Ü\íñG\á\Ú\ÞE§6»§-\Ä\Ñy\éº„;E·~ð»òWoÍ¸cž•=\ß\ÄoX\ßC§_k0Os Šä¹‰Y˜b¤\0\'z\×\É^ý‚¾øLò´n®\"Ž\ÔCö¤Œ&lí¥·H\Î\ÔÜ±H&bê¿€¯#¶ÿ\0‚bx?Yðƒ¼3\ã¿j:‡†®Rþ\â\æ-®nnR\æ+¥mò©u\Ù\å$!¾ñˆ`\à\×D0™{vö®Þ‚\æ©\ØýEFÜ¹<dW\ãÿ\0†^ø›gig\ãV¸[	þ\Ónñ\Ë$E(R»’H™eX‚3‚\"»\Õ,©†\í\\¿‹üo\á?\0\è­\â\Zjú]Š°C=Ì‹\Znnƒ,FIô×\nŽ¤\Ýúwü–½\ÝO™Wö.ýš ×–\ÎBB&œE FuV•<Í²8>Áa¸zW k²\ßÀ\Ïi·\ZF±\á\ëy­î˜´‰ºE\Ë4[“•`Aò¡2E\ë\Ót\ÝE\×uk}_E»†\î\Ö\ê\ËÌ†XdWI¸ù•Áâº“¨[óÕ±\èAú÷®ú™Ž*öu%ø™*P\Þ\Èù>\ØSöU‡Iþ\Äÿ\0„F\Ý\í÷\Í&Y\Ø\ï¸2V,\Ò\Ë9$œý+\ÂþüøYð\'ö\Ãñ†¾\éi¥Z\ßxJ\Êö\åY%/9¼š-\ìeglùq¢ŽqÒ¾³ñ¿\Ç\è\Ú\Ëx3L\Ô\í\ÓZdR¨\ì¿(\Ï\Îs\Ç×ž+\äÏ‡’<¿¶¯ˆ.d”\Î\ï\à\Ëò¸³}¾\ç<ÿ\0AÀ¯\Õr>\Í\ÞSW8Å¶©\Ú\Ê2¾·\ëo#\ËÁñûRž–³\ë\ä}\Í\r\Ç55V·V¹»Õšü\íZ\Ê\Ç\êR\Ü)¿\Â\Zu7øO\ãA\"žÔý\r#œ\ÓKÓ°\Z~Ü¾%\Õto€\ÓxoC»6W~&\Ô,´U”pB^L±Éƒ\Ô|„Î¾¢ð‡†4\ÏxgOð†\Ãi¥\ÛGm\n/EH” ¯ÿ\0ið¿þ)ü<ø\r`<\ã>¨š\æ¤#?<–z3tI.\×W\Û\Éþµ½;W«Š¼p°¦ô¾¦•\êJH“\Ë ð?ZŸ½.i	\0ó^Sw:\Ô\Ò	ühži…°1Š,\"C\ÔQÞ¡óqK\æsÚ“\ÓqÙ’Ž¦–£F\ÜMIE\Ä	\\’Xf¦\Í!\äâ”¢š³ó\ÏEÿ\0‚|ü\Ö?lm_ö\åø¿i‰¼u¶+½À/§Û¦\ÄKx\Û\åó‹;\ÈFA8^™?~\ÜYEwnö·Q¬‘H¥¬¤`‚PGÖ®…\\\ç\é„þ5Ý‰\Ì*\Öi\Ôw¶\Þ^Œ¡F1½–\çÀ¿a\Ï~Í¿´¯ˆ>2ü¼“Kðÿ\0Œ¬\r_\Ã\ì\Ìö©wnå º´O•ò³#\Æ0¸\Æ1ŒW\ß(7ZMˆ1RTb±•+>j®\ìªt\ÔU¢QEr–ÿ\×þÖ¤ûÂ¾eý†ÿ\0\ã\ß\âýz§þ\r}5\'\Þ¯˜ÿ\0a\Ö\"\ß\â‰\íÿ\0	Þ©ú¤5ü]ˆÿ\0uŸª?J\ÍþŸuSKb\ãÖšpy\Í|Áó\Éw2u¿h^\Ó%Ö¼Gy…œ8ó\'¹‘brp7;£\'“U<;\ãø¿O\ZÇ…o­õ;B\ÅÖ’¤Ñ–F\ä$dw¯(ý¢~·ÇŸ†­ð\è\ß6œ’\ß\ØÝ¼\É÷ö\Ú\\$\åWƒ†`˜¶zW\ç×Œ¿a?‰^\Ðuh>üJ¸Ð†µ¬\Ü_\Üj\íG,q˜c›\Ê+ky!@­\Z)x·+\ÌIöðX<%J7«W–W\ìÙ•II?u]¥ú\Í\ÄW^%·ñ¬SÝ®•\ØHGI0¤r@bK/ˆ|Dö\'Z1-ŽŸ\Z†´q0^K;ÁTŽ	\Î:\ã¥~[ë¿±o\Ç\Ôô_ø\ã4ö:V…¤\ÏgzN\'¹–{i–I%]ÀgÏ•e¡U@\Ï5_\Åß±\×\Æ\í/\0\É\ãY\ït­t\Ï–›}yvL~u¤\îD‰ wd‘%*V)‰1Šô–O†\Ñ}b?s1ö\Ò\ì~\ÉY\ÝC5´sDK«€\Ê\ß\Þ\ëWY”\æ¿&¼ûþÒžð÷\ÄGÄ¾>¸Õ®µý)“I±\Ó\în-~\É‘¡‘$.6ü¥PŒvó\Ç\ÌøgGÿ\0‚”xC\Äz¯ƒtF\áõ\ÒX]\Þ\Ío{2\ß²\æO2bdv9¹,”\\v¬\ç‘Ñ’j–\"7óº-Wi\ë~\Åo¹ª:•¤z„¶¢UÛ‘Ú¿.\æÔ¿\à¡:_\Å/J—÷º•\å\Û>«zV\Ë\ë}ø´Žh™U\á8É–Dþ®\ìš\Ãømü\Ä:5Ý·\Äk\ÈtK˜¯.¦‚Ha°•\ä…l·A*#kÏ“p\ÊrH\Îkðûµý´>ö_¶]™úšöžV;\åŽ\å\ãùþø9‘­[\rV\Êû+nÿ\02ðUVPy¯\Ì!¨ÿ\0ÁF\ì~6\èvvöP?„%\Ö\Z]I\ÃÚ·ú’®ðwþõJG’\'>œþŸMeow\Ù\Ô1‡>„r+—{Ÿ:•û*_K\Zƒ©¤þø\×7¦µbm\'ó\Ô`m›–\Çû\ã“øƒM‹\Ä\Ö\É(´\ÕQ¬\ælad)\'²¸ùIö\Íy®‹¹\ÔQQ$°Gzy`\rCV¬p¤\×\Ìß´\ïÀÛŸ\Þ\n\Óü;§\ÝZ\Û\\\é·ñ\ß\Â\×I)Pñ«(+%¼°\Ï€\Çk\Ç =A_K\î\ç®9¯È|ýªþ\êšÏ‹4E¨^j·\Z\ÇöMÓ¸]]\ÎòÁsn¨\íQ„r	òø1\èxoSˆŒ(J\Ó\é£w9±x˜Òƒ•M‰\ï?\àœ_U,ž\Ë\Ç\Å\æ‡Ks\ä\Ë\ÄjUh­¼¹–4†V4›‘¤b —\É\ÈñŸ‰Ÿ°v¥¨\ÙÉ£|$ñ­ß‡¦‘¦q=ÁFöP[7¥ÁFš7\È`w9#žj-Á?ðQýwPÓµ/üIŸN±\Ó\â@,\í\ä‹u\Ék\Äi\Ó‹1[R\è¤Kc<\×¯xöýñG€gÿ\0…q¯.“{oª\Þ[Y“öI\Z\Æ\Ëmu2²6\ç•þi¡P¤H¯\ê\ï|&\ÇGõ\ì\ÝóA­\"•¯\ê~W\Å<oE\Ã\Ø\àf¯\Õ\ßE\éÜ‡Â¿ðNˆ\Ðt\ËHþ ·Û´·¹hf\îÊ©=Á\"\Ã\ÊX\Çq‚yô¯Fý€¿gŸþ\Î_´ÇŒü\'\â\ß\ÂG.£\áûMB\'ñ\Çnw*‘]\ßr3ñ–<zþ‚xv=b\r\n\Æ/H²_¥¼Kr\É÷Z`ƒ\Ì#§²Eyg\Ã\Òö\Ò×ýIvúp¹¯\Óü`ÀS§Ã•½šµ’û®|Ï…Ù½z\Ù\ä#VW½û\\ûJ’£B[O$\Å\\þ»b\Ö}Î£ifUn\åHƒ¶\Å.ÁrÇ \êOj²f\Ú\ÛH¯•ÿ\0iO…\ÚdÐµÿ\0kk£E§\Í%Œ~l~drI©…6\Ãl\á€?;Xô5Ñ†¢§5;\\Š­\Æ7GÔ’»¦{ö¯ø\áñ{\Ã¾j\Þ6ñ\ÔqKgk$–ð³¢\É<¡NÄX‚\ä¶8‡ñöƒðÁeÓ´-Y\çÔµA’-.\É|ûÛƒ»„c¢ƒ÷˜\à{\×\Î\ß~kþ#ñ÷Œ><~Ñº;,÷7*4D\Ö<–ZtK¸b²@Á¾ù\ÎN2k\Õ\Â\àTW·­¢[{\Èç­ˆ½¡\Ï\\ý•¾\êZ\'ƒcø­\ãÉ›Qñ—‹ Ž\ëS¼”\å•nŠ\Þ1€(”€¹\'5õö¡g¦\"\Ë*B®B‚\ìz’9ô«ó\Å$+$1°Jò#Œc¶:W‚þÐ¿µ?Œþ±\Ðô\ÍF\r+\ì·Bifš\Û\Ïf„©Y#Œ\ï_(º’¦A–PN0y®	\Õu\êóUvMý\ÞFñ‡$t=¾\×[\Óo¥x,§Šgˆ\í‘Q\Ã>Œ\àýkNLqšù·\àwÁ”øk{¨\ëz>²/t½T·µ€7\ÙÐ´+Lä³¹|\\šúE›\Û5Ž*œ!>X;•	6µV>ø›ðöª\Ôþ!k¾(ðOŠ\ÐiW0¼zN–ò\ÉZ\ÌcŒ‰2Œ«(\à.Cš£\á\ß~\ÜzN•m¥\ê~(\ÓHŽ9k–Ä¬4F&|y\ÞJ$2¤•\Ú}\ßi¨\Ø\ê!²•\'X£s\n\é\Ã)Á\á\àƒÈ¬\ï\éƒ\\Ð¯4m\ÞP»†HKsÇ˜¥sÁ¿b\ruG8\ÅA\Å}\Ä:1ø‘ðm\î™ûo^\\h\Ö>\Zñ¯‡æµ•\Ï\Û&‘\çM\âŒ\"¯¿\ÜdU\r|7ÿ\0‚„h>\Ðtuñ†“tö\å#\Ô\'½Ï•\Ñr\ÊÁP³;n\ÎO­¿\0þ\ÌZU—\Å\å\Ö\"ñq¿¿ð\è³ûNž\"b-#Im\ã\Ý\Ù\ãIC1p\Å÷Šý\0Q–;ô¯Ct’ŠK¾\Æ4©¶›‘\ãtÿ\0úlzÅ¿\Æ{»+\ân\Ëi\ÒÙ§\ì\ç?$‰´\0\Ë\ÆN}«\Ük55#xlDó\Â	yö€\Ûs¤Œg¦j\ç›\ì*ð\ëM\Î\\\íQŽšRw¬ùõK;Y¢¶¸•#’v+\Z3g`2Bƒ\É s\Å\\I7óøV>c¹ \êi3ò“õª——öz|bk\ÉV%gTR\ä(,\Ç\n£=\É\à¦¦\r…\Ìû­gN²,/\'Ž-€3o`»Crxð­Imª\Ù]\Ï%­´¨ò\ÄtV”7MÀŒö¯Š¾:þ\Ï^	*Ö¿hk\\C‰¬¬b½1m\ìrO¶I\Óx\Ü\\ch\ÆTó]/\ìõû/[|\r\ÕdÕ¬5Ùµx¥…\Ö\'1;ù\Þ[1šM\Ç\Ì\åh$s^Œ°\ÔU.u-}!R\\ü­aaý¨\ÃûU_,ú\Z<³\èkƒ‘6?ÿ\Ðþ\ÖfV\êœ\×\ÍúG\ìõ{\á\r[[\Õ<\ãM{@‹\Ä\Z„º¥Õ¥¯\Ød„\\\Ìdd7²¸\r´q»¾•n\ßZl«ø\âiC\á?[«B-Î®xwü+ˆ\ç¯\Åÿ\0ß½+ÿ\0hÿ\0…_ñþŠ‡‰ÿ\0\ï+ÿ\0k\Ûh£\ë“òû—ùýN—òž%ÿ\0\n¿\â7ýÿ\0ß½+ÿ\0j÷Áÿ\0j–§\ê?üI<224zVÿ\0À\Z÷šQ\ÔSŽ2i\ßO¹ž‹û\'\ÎPüñ,ù\ßð±<Dò÷‘\â\ÒY\ÏÕ†MX\Ô~ø\ÏW	öÿ\0‰~&s`ŒLF]\Ýv²Ø‚3\Ð\×\Ð\Ï÷©Z¿¯Ô½ôû—ù\ê”ð[O„>±µK;o‰þ(ò\ãÆ˜\ì~¬\Ö$Ÿ©«ð§\â9ÿ\0…£\âû\ãKÿ\0\ä\Z÷\Z*^6m\ßO¹}Bòžÿ\0\n£\â \á~(ø˜ÿ\0Lô¿þA£þO\Ä_ú*^&ÿ\0¿zWÿ\0 ×¸\ÑK\ë“òû—ù\Ô(ÿ\0)\á\ßðª~\"ùª>&÷ýÞ•ÿ\0\Èñð»\â2ŒŠ>\'ÿ\0¿zWÿ\0 ×·QK\ësòû—ùÀ\Ñ_dñøUÿ\0ÿ\0\è¨øŸþý\é_üƒQKð£\âË²o‰þ&u=AJ#ÿ\0Hk\Ü\è£\ë\Õ\Öû—ù\r\àhÿ\0)\àö\ß¾ Z3‰þ(PÝ¶\é„ 68…[ÿ\0…añ õø£\âû÷¥ò\r{m\Þ:§—Ü¿\ÈŠû\'‰Â¯øÿ\0EC\Äÿ\0÷Æ•ÿ\0\È5\É\ë?³¿ˆüCªÁ­\ê¿|Q%Í´omÿ\0Ñ„r:Ž+\éšr÷úW^9\Ä\á\êª\Ô%\Ë%\Õ$¿$a‰\Ê0µ£\ËV	®\ÏT|¹ÿ\0ß¯t_ˆþ\'#\ßû3ÿ\0i\á›õ\áÁø\âqÿ\0‚\ÏþA¯¨—úÒ¿Z÷×ˆ¹\í­õ¹ý\ç—þ¦e=0\Ðû‘ò\ßü3~½ÿ\0E#\Äÿ\0ùLÿ\0\ä\Z\è~\Z|\Óþø\îÿ\0\â5Ö»ª\ëú®¡aš\Ò\ê-o¶;x$yUQm\á„d»±$‚z\nú”u®<\Z\æøªN†\')Eôn\çN†²ü=EV…\ÆKªV$UÀÁ¡‡\ËO¦¿Ý¯–\Ùh{…9rù)¤üø‰ûTx\ç\âWÃ¿>?\×±<1®[.”\ÚD±X´SG²\éX…F\Ý\åüwdu=kõžo¼\ß_\ë_\Zþ\Ìÿ\0òV¾1ÿ\0\ØÊŸúI}WRT}²¦\ì\ÔSOªl\ã\ÆÁ9E>§1ð÷ö\Ò~j\Ò\ë\Þøƒ\âdÔ§]’_\\\r6\æ\é—\Ð\Í5“¾qœW ø\ÏöWñ7|)¨ø\Å|Sq§jöòZ]FLRðÊ¥]w-ˆaqAõ´x}j\Ã}ñ\\O>\ÅO÷ŽZ÷²ÿ\0#¡\á¡Ê‘ò¾“û;x\ãD\Ò\í´]7â§Š\Ú\Î‚%Ù¥©\Z…Q“`IÀ“Vn>|C¸…\í\ßâ·Š\nº•?»Ò¹ŒÇ…}7ECÍ«¾«\î_\ä\ÉZ\Ç\Èý•|Gð\çÁúo€ü#ñC\Å6\Ú^“\ÛZ\ÂWK}‘\'\ÝšÄ±Ç©$û\×Z>üD\ëÿ\0_\Å#þ\Ù\é?ü_IQELÖ¼\ß4šû—ù\ÙG±ñÏ‚ÿ\0d½o\áõ½ý§„þ&ø¢\Ö=Nþ\ãR¸\\i¾\æ\é·\Êù{F\æ\ç\ØWb~|F#â¿Š?\ïÞ•ÿ\0\Èô±\ë@\ëU,Þ³\×O¹{4|¤þ\Ë#Ñ¼O«x\ËNø¡\â˜õh@·r\í\ÒÏ˜-T¤\\\Õ$p{\×Vÿ\0>\"²\àüXñG?ô\ÏJÿ\0\ä\núHt© ©žm^[µ÷/ò²¬|{\ì¯\â8¼m/\Ä%ø¡â‘«MbšsÍ·LÁ·ŽF™So\ØvŒ;3\Ï\\WWÿ\0\n+\â&1ÿ\0_\Å÷\ïJÿ\0\ä\núLu4´\åœb%¬šû—ù\rSŠ\Øø÷\Ä²†»\âwGñ6³ñ?\Å2\Þ\è3I=‹\í\ÓÊ’X\ÌNp¶89F#u®¸|\nøˆ¿óU¼QŸú\ç¥ò})Jz\Ô\Ë4®\ì›_rÿ\0!{(\ïc\äþ\Ë^$ñÆ›—\â_Š)¸†\Ú\î\Þú5¥®\'µKelA;]A\ÇC\Ðñ]Høñ\Íñ_\Å?ô\ÏJÿ\0\ä\núLô4´<Ò¿/-Õ½ù²\î|‹\ã\Ù[\\ø…á«¯ø\ç\â_‰õ\"ü*][2éˆ²Æ®¯´´vJ\à£;Xw¯¬\í-Íº%º5T9\áFò«-\Û\éR¼k\ZøÊ•U¦\ïaªj÷Œ\nZ+”³ÿ\Ù','8a80848460c2ee580160c73af80f000b'),('8a80848460d98aad0160dbc3718e000e','TestZone1 9/1/18','','8a80848460d98aad0160dbc35b50000d'),('8a808484615621fa01615719337b0011','Testissue36',NULL,'402880895ff401b9015ff403e4150002'),('8a808484615621fa01615be8e157003d','Zone 44',NULL,'8a80848460d98aad0160dbc35b50000d'),('8a808484615621fa01615be95f02003e','Zone 55',NULL,'8a80848460d98aad0160dbc35b50000d'),('8a808484615621fa01615be9fa000040','Zone441',NULL,'8a80848460d98aad0160dbc35b50000d'),('8a808484615621fa01615bea75800042','Zone4412',NULL,'8a80848460d98aad0160dbc35b50000d'),('8a80848461652eb6016166d38d93005c','nz1',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a8084846190b223016196757c2f0016','SmokeTestLZ1',NULL,'402880895ff401b9015ff403e4150002'),('8a80848461a8b5d40161a8cbc4a10008','New Proj ',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848461a9fe2c0161b3899e690020','MyTestLivelihoodZone',NULL,'8a80848461a48f910161a543f02c0005'),('8a80848461c84af60161cdedc1f9002d','Test25Feb',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848461c84af60161d1c7fbc0003b','aaaaaaaa',NULL,'8a80848461a48f910161a543f02c0005'),('8a80848461d664370161d66588d80003','anewzone',NULL,'8a80848461a48f910161a543f02c0005'),('8a80848461d664370161d7903cc90007','anothanewzone',NULL,'402880895ff401b9015ff403e4150002'),('8a80848461d664370161d7908f570008','yetanothanewzone',NULL,'402880895ff401b9015ff403e4150002'),('8a80848461fbb5780161fc334e380007','Cotton LZI',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848461fbb5780161fc369d330008','Millet LZI',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848461fbb5780161fc492b800012','Maize Bns',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848461fbb5780161ff68d3450046','Cotton LZII',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848461fbb578016204f717e4004b','Lambeth',NULL,'402880895ff401b9015ff403e4150002'),('8a80848461fbb578016205df1de20057','SugarCane ZN',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a808484620a360801621f43c4ff000c','MyTestProjectMarch LZ',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848463cef2570163cf8544a80005','OX5.9 Test LZ',NULL,'402880895ff401b9015ff403e4150002'),('8a8084846450800b01647ea8dee9000c','Issue113 Zone',NULL,'8a80848461fbb5780162062bad4c005f'),('8a80848464a2641d0164a8027bef0009','Issue 109 Zone',NULL,'402880895ff401b9015ff403e4150002'),('8a80848464a8f7af0164acb93aaf0006','Issue113 Zone',NULL,'402880895ff401b9015ff403e4150002'),('8a80848464a8f7af0164acbaf1690007','Issue113 Zone',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848464a8f7af0164acbd5f940008','xxx',NULL,'402880895ff401b9015ff403e4150002'),('8a80848464a8f7af0164acc163a5000a','testAgain',NULL,'402880895ff401b9015ff403e4150002'),('8a80848464c6e6f80164c7a5701e001b','LZNew',NULL,'402880895ff401b9015ff403e4150002'),('8a80848464d24bc10164d326f9ff0004','Issue123 Zone',NULL,'402880895ff401b9015ff403e4150002'),('8a80848464dc39a00164dc7ae28c0012','UnreliableRowCoutZone',NULL,'402880895ff401b9015ff403e4150002'),('8a80848464eaaacf0164eaf6ea2b0010','Rice and pigs',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848464eb7d5b0164eb9a59390007','TutorialLZ',NULL,'402880895ff401b9015ff403e4150002'),('8a80848464eb7d5b0164efc621300023','`testLZ',NULL,'402880895ff401b9015ff403e4150002'),('8a8084846513d34e0165208c6fec001a','Sugarcane and Brick Making',NULL,'8a80848460d98aad0160dbc35b50000d'),('8a8084846548a6e8016552c82c570012','lakes and fish',NULL,'8a80848460d98aad0160dbc35b50000d'),('8a8084846548a6e8016552d3c6780015','Rice and pigs',NULL,'8a80848460c2ee580160c73af80f000b'),('8a8084846558de0301655926aa680006','Banana Coffee Piggery',NULL,'8a80848460c2ee580160c73af80f000b'),('8a8084846558de0301655bcb067a003f','CPtest21LZ',NULL,'4028808860bbdf630160bbe5f21a0002'),('8a80848465753ce301657f4f6d00002b','azone',NULL,'8a80848461fbb5780162062bad4c005f');
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
INSERT INTO `site` VALUES ('4028808860bbecfe0160bbee432a0000','','Mukono','Ngombere, Mpunge Sub Coun','4028808860bbdf630160bbe5f6540003'),('8a80848460c2ee580160c743cb4d000d','','Nakapiripirit','Nakala','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c7457cfa000e','','Nakapiripirit','Cucu','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c74b48c3000f','','Napak','Lobey','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c75482280011','','Moroto','Lonyatha','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c754e2ff0012','','Moroto','Namatwae','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c7553c6e0013','','Kotido','Lopuyo','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c75585a50014','','Kotido','Moruongor','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c755c9d30015','','Kotido','Loputuk','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c75633890016','','Kaabong','Nariamaoi','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c75659ba0017','','Kaabong','Sangar','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160c7568b850018','','Kaabong','Loyoro','8a80848460c2ee580160c73ba180000c'),('8a80848460c2ee580160d6f3fbdf002c','','Napak','Lokoit','8a80848460c2ee580160c73ba180000c'),('8a8084846109b4b0016109ea7aad0001','','xxxxxy','yyy','8a80848460d98aad0160dbc3718e000e'),('8a8084846109b4b0016109eaccec0002','','xxx','yyy','8a80848460d98aad0160dbc3718e000e'),('8a8084846109b4b0016109f299a10005','','aaa','bbb','8a80848460d98aad0160dbc3718e000e'),('8a8084846109b4b0016109f2b1770006','','ccc','ddd','8a80848460d98aad0160dbc3718e000e'),('8a808484614805e90161525970650007','','Nakapiripirit','Kajaja','8a80848460c2ee580160c73ba180000c'),('8a808484615621fa0161571dbfd10012','','testissue36','testissue361','8a808484615621fa01615719337b0011'),('8a808484615621fa016157298e440016','','testissue36','testissue362','8a808484615621fa01615719337b0011'),('8a808484615621fa0161572f6a870018','','testissue36','testissue363','8a808484615621fa01615719337b0011'),('8a808484615621fa01615be95f07003f','','D44','SD44','8a808484615621fa01615be95f02003e'),('8a808484615621fa01615be9fa090041','','D441','SD441','8a808484615621fa01615be9fa000040'),('8a8084846161aa02016161d6b4ea0002','','TestIssue32','TestIssue32','8a808484615621fa01615719337b0011'),('8a8084846161aa02016161d9a19c0004','','TestIssue32','TestIssue32a','8a808484615621fa01615719337b0011'),('8a8084846161aa02016161dc1ce80007','','TestIssue32','TestIssue32b','8a808484615621fa01615719337b0011'),('8a8084846161aa02016161e36d900009','','TestDistrict','TestSubDistrict','8a808484615621fa01615719337b0011'),('8a8084846161aa02016161e8f5df000d','','TestDistrict','TestSubDistrictA','8a808484615621fa01615719337b0011'),('8a8084846161aa020161651d9baa0010','','District DRB','SD DRB','8a808484615621fa01615719337b0011'),('8a8084846161aa02016165263a4d0011','','Nakapiripirit','Cucu','8a808484615621fa01615be8e157003d'),('8a80848461652eb601616534a91d0000','','D36_1','SD36_1','8a808484615621fa01615719337b0011'),('8a80848461652eb601616537297f0002','','Dist36','sd36','8a808484615621fa01615719337b0011'),('8a80848461652eb60161653de83f0005','','district 36_1','sd 36_1','8a808484615621fa01615719337b0011'),('8a80848461652eb6016165412a490006','','d_36_11','sd_36_11','8a808484615621fa01615719337b0011'),('8a80848461652eb601616542ac730008','','d_36_111','sd_36_111','8a808484615621fa01615719337b0011'),('8a80848461652eb60161654a94ca000d','','D Z11','SD Z11','4028808560a7f1160160a803220a0003'),('8a80848461652eb60161667d6eed004c','','Mukono','Bugoye/Ntenjeru','4028808860bbdf630160bbe5f6540003'),('8a80848461652eb60161671bb0310065','','TestDistrict1','TestSubDistrictq','8a808484615621fa01615719337b0011'),('8a80848461652eb60161672105550067','','TestIssue42','TestIssue42','4028808560a7f1160160a803220a0003'),('8a8084846171df9d016189328293001c','','D1','sd1','8a808484615621fa01615719337b0011'),('8a808484618e3df601618e44537c0002','','testissue36','testissue36','8a808484615621fa01615719337b0011'),('8a808484618e3df601618e45c4790003','','test36','test36','8a808484615621fa01615719337b0011'),('8a808484618e3df601618e4711bf000a','','test36','test361','8a808484615621fa01615719337b0011'),('8a8084846190b223016191604fc40005','','TestDistrict','TestSubDistrict','8a80848460d98aad0160dbc3718e000e'),('8a8084846190b223016194132b4b000a','','OtherTestDistrict','OtherTestSubDistrict','8a80848460d98aad0160dbc3718e000e'),('8a80848461a472180161a479bb8f0002','','D44','sd44','8a808484615621fa01615be8e157003d'),('8a80848461a472180161a47e6b540005','','a','b','8a808484615621fa01615719337b0011'),('8a80848461a48f910161a490f3160001','','new2','new2','8a808484615621fa01615719337b0011'),('8a80848461a9fe2c0161adae3b150014','','xxx','yyy','8a80848461a8b5d40161a8cbc4a10008'),('8a80848461a9fe2c0161adb075f30019','','xxx','yyyy','8a80848461a8b5d40161a8cbc4a10008'),('8a80848461a9fe2c0161b39461770021','','MyDistrict1','Village1','8a80848461a9fe2c0161b3899e690020'),('8a80848461c467430161c489e4600004','cc','aa','bb','8a808484615621fa01615719337b0011'),('8a80848461c467430161c48bf1f00005','','aa 55','aa 55','8a808484615621fa01615be95f02003e'),('8a80848461c467430161c492efe90006','','anewz1','anewz1','4028808560a7f1160160a803220a0003'),('8a80848461c467430161c49570ad0007','','drb','drb','8a808484615621fa01615719337b0011'),('8a80848461c4a5a40161c4a72e850001','dd','dd','dd','8a80848461652eb6016166d38d93005c'),('8a80848461c4a5a40161c4a7fb990003','ee','ee','ee','8a80848461652eb6016166d38d93005c'),('8a80848461c84af60161c93d57090011','','Issue54District','Issue54SubDistrict','4028808560a7f1160160a803220a0003'),('8a80848461c84af60161c9485789001b','','Issue54District','Issue54SubDistrict2','4028808560a7f1160160a803220a0003'),('8a80848461c84af60161c9492b65001c','','xxxÂ§','yyyÂ§','4028808560a7f1160160a803220a0003'),('8a80848461c84af60161c94b2905001f','','Napak','nnnnn','4028808560a7f1160160a803220a0003'),('8a80848461c84af60161c94e595e0025','','xxxx','yyyy','8a808484615621fa01615be8e157003d'),('8a80848461c84af60161cdefe986002e','','Feb25Issue32','Feb25Issue32','8a80848461c84af60161cdedc1f9002d'),('8a80848461c84af60161cdf20fa1002f','','d1','sd1','8a80848461c84af60161cdedc1f9002d'),('8a80848461c84af60161cdf463690034','','d1','sd12','8a80848461c84af60161cdedc1f9002d'),('8a80848461d664370161d665eb760004','','1','2','8a80848464a8f7af0164acbd5f940008'),('8a80848461fbb5780161ff68d3520047','22454\"5','Balaka North','Nkana','8a80848464a8f7af0164acc163a5000a'),('8a80848461fbb578016205e45fdf0058','32\"44\"','NkhotaKota','Mwasambo','8a80848461fbb578016205df1de20057'),('8a80848461fbb578016205ec270e005a','','Nkotakota','Mwasambo','8a80848461fbb578016205df1de20057'),('8a80848461fbb5780162062febd20060','','districtxxx','villageyyy','8a80848461fbb5780161fc369d330008'),('8a808484620a36080162204bc886000f','76\"6677\"','Lilongwe','Nathenje','8a808484620a360801621f43c4ff000c'),('8a80848462208be6016220d4dc640003','','Hakama','Natola','8a808484620a360801621f43c4ff000c'),('8a80848462208be6016220d8ce620004','455557\"888','Zomba','Makawa','8a808484620a360801621f43c4ff000c'),('8a80848462208be6016220ec20ed0009','','Mukono','Bugoye/Ntenjeru','8a808484620a360801621f43c4ff000c'),('8a8084846225b55401622ad6e335000d','','Mukono','Lusera/Nakisonga','4028808860bbdf630160bbe5f6540003'),('8a8084846225b55401622ad85e37000f','','Mukono','Namakwa, Nakisunga','4028808860bbdf630160bbe5f6540003'),('8a8084846225b55401622ad8d89c0011','','Mukono','Butere/Mpatta','4028808860bbdf630160bbe5f6540003'),('8a8084846225b55401622adaf9180013','','Mukono','Mengo/Mpunge','4028808860bbdf630160bbe5f6540003'),('8a8084846225b55401622adc56a20017','','Mukono','Ngombere, Mpunge','4028808860bbdf630160bbe5f6540003'),('8a8084846225b55401622add14450019','','Mukono','Mugomba/Mpata','4028808860bbdf630160bbe5f6540003'),('8a8084846450800b01647eabefe1000e','','DistrictXXX','SubDistrictYYY','8a8084846450800b01647ea8dee9000c'),('8a80848464a8f7af0164acab572c0002','','District109','SubDistrict109','8a80848464a2641d0164a8027bef0009'),('8a80848464a8f7af0164acbdbfc40009','','xxxx','yyyy','8a80848464a8f7af0164acbd5f940008'),('8a80848464a8f7af0164accaf914000b','','SmokeTestDistrict ','SmokeTest SubDistrict','8a8084846190b223016196757c2f0016'),('8a80848464d24bc10164d32bee040005','','District123','SubDistrict123','8a80848464d24bc10164d326f9ff0004'),('8a80848464d67b7a0164d78e3309000c','','abcdistrict','abcsubdistrict','4028808560a7f1160160a803220a0003'),('8a80848464dc39a00164dc7bad950013','','URCDistrict','','8a80848464dc39a00164dc7ae28c0012'),('8a80848464eaaacf0164eafb94610011','','Salimatest','Lakeshore','8a80848464eaaacf0164eaf6ea2b0010'),('8a80848464eb7d5b0164eb9b401a0008','','District1','SubDistrict1','8a80848464eb7d5b0164eb9a59390007'),('8a80848464eb7d5b0164ebb4cd060011','','District2','SubDistrict2','8a80848464eb7d5b0164eb9a59390007'),('8a80848464eb7d5b0164efc725890024','','District1','SubDistrict1','8a80848464eb7d5b0164efc621300023'),('8a8084846513d34e0165208efd7d001b','','Homa Bay','Ariri/Ndhiwa','8a8084846513d34e0165208c6fec001a'),('8a8084846548a6e8016552cbb2ad0013','1234567','lakevic','hbay','8a8084846548a6e8016552c82c570012'),('8a8084846548a6e8016556b5b089001f','','D1','SD1','4028808860bbdf630160bbe5f6540003'),('8a8084846548a6e801655783e1590027','','District2','SubDistrict2','4028808860bbdf630160bbe5f6540003'),('8a8084846558de030165592787a60007','','Mukono','Nakisunga','8a8084846558de0301655926aa680006'),('8a8084846558de0301655928c1510009','','Mukono','Namakwa','8a8084846558de0301655926aa680006'),('8a8084846558de030165592994c1000b','','Mukono','Lusera','8a8084846558de0301655926aa680006'),('8a8084846558de030165592a6137000d','','Mukono','Ntenjeru','8a8084846558de0301655926aa680006'),('8a8084846558de030165592af55c000f','','Mukono','Bugoye','8a8084846558de0301655926aa680006'),('8a8084846558de030165592e41eb0010','','Mukono','Butere','8a8084846558de0301655926aa680006'),('8a8084846558de0301655bcf502d0040','1234567','Mukono','Bugoye/Ntenjeru','8a8084846558de0301655bcb067a003f');
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

-- Dump completed on 2018-10-23 14:31:37
