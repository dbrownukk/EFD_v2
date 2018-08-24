CREATE TABLE `wealthgroupinterview` (
  `WGIID` varchar(32) NOT NULL,
  `WGAverageNumberInHH` int(11) DEFAULT NULL,
  `WGFemaleIVees` int(11) DEFAULT NULL,
  `WGInterviewDate` datetime DEFAULT NULL,
  `WGInterviewNumber` int(11) NOT NULL,
  `WGIntervieweesCount` int(11) NOT NULL,
  `WGInterviewers` varchar(255) NOT NULL,
  `WGMaleIVees` int(11) DEFAULT NULL,
  `WGYearType` varchar(255) DEFAULT NULL,
  `WGID` varchar(32) NOT NULL,
  `WGIStatus` int(11) DEFAULT NULL,
  `WGISpreadsheet` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`WGIID`),
  KEY `FK_jh8uxqbt3bi76knbc0gthr9sb` (`WGID`),
  CONSTRAINT `FK_jh8uxqbt3bi76knbc0gthr9sb` FOREIGN KEY (`WGID`) REFERENCES `wealthgroup` (`WealthGroupID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WealthGroupInterview_assetFoodStock` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `FoodName` varchar(50) NOT NULL,
  `wildFoodOrCropIndicator` int(11) DEFAULT NULL,
  `resourceType_IDResourceType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  KEY `FK_tnnqfbo5r1i975ywqsrq68lst` (`resourceType_IDResourceType`),
  KEY `FK_8u6y59hnvb5bi5g3y1cp6dh7x` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_8u6y59hnvb5bi5g3y1cp6dh7x` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `WealthGroupInterview` (`WGIID`),
  CONSTRAINT `FK_tnnqfbo5r1i975ywqsrq68lst` FOREIGN KEY (`resourceType_IDResourceType`) REFERENCES `ResourceType` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WealthGroupInterview_assetLand` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `LandArea` int(11) NOT NULL,
  `LandTypeEnteredName` varchar(50) DEFAULT NULL,
  `UnitEntered` varchar(50) DEFAULT NULL,
  `resourceType_IDResourceType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  KEY `FK_hdj97g9m4dn323o8ayy96dup2` (`resourceType_IDResourceType`),
  KEY `FK_8au1des2s77ncnoj0uxmi259i` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_8au1des2s77ncnoj0uxmi259i` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `WealthGroupInterview` (`WGIID`),
  CONSTRAINT `FK_hdj97g9m4dn323o8ayy96dup2` FOREIGN KEY (`resourceType_IDResourceType`) REFERENCES `ResourceType` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WealthGroupInterview_assetLiveStock` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `LiveStockTypeEnteredName` varchar(50) DEFAULT NULL,
  `NumberOwned` int(11) NOT NULL,
  `PricePerUnit` decimal(19,2) DEFAULT NULL,
  `UnitEntered` varchar(50) DEFAULT NULL,
  `resourceType_IDResourceType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  KEY `FK_qky1h8godcklkrv9vsia9grpd` (`resourceType_IDResourceType`),
  KEY `FK_jpg0rgp412kmfd1p19edn8f0b` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_jpg0rgp412kmfd1p19edn8f0b` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `WealthGroupInterview` (`WGIID`),
  CONSTRAINT `FK_qky1h8godcklkrv9vsia9grpd` FOREIGN KEY (`resourceType_IDResourceType`) REFERENCES `ResourceType` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WealthGroupInterview_assetTradeable` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `TradeableTypeEnteredName` varchar(50) DEFAULT NULL,
  `resourceType_IDResourceType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  KEY `FK_pskwl6yc2s0ame8tht7mbxjlh` (`resourceType_IDResourceType`),
  KEY `FK_mlvhkwmmqdx0t3c1wxrk41kj` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_mlvhkwmmqdx0t3c1wxrk41kj` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `WealthGroupInterview` (`WGIID`),
  CONSTRAINT `FK_pskwl6yc2s0ame8tht7mbxjlh` FOREIGN KEY (`resourceType_IDResourceType`) REFERENCES `ResourceType` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WealthGroupInterview_crop` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `AprQP` int(11) DEFAULT NULL,
  `AugQP` int(11) DEFAULT NULL,
  `CropName` varchar(50) DEFAULT NULL,
  `DecQP` int(11) DEFAULT NULL,
  `FebQP` int(11) DEFAULT NULL,
  `JanQP` int(11) DEFAULT NULL,
  `JulQP` int(11) DEFAULT NULL,
  `JunQP` int(11) DEFAULT NULL,
  `LocalUnit` varchar(45) DEFAULT NULL,
  `MarQP` int(11) DEFAULT NULL,
  `MayQP` int(11) DEFAULT NULL,
  `NovQP` int(11) DEFAULT NULL,
  `OctQP` int(11) DEFAULT NULL,
  `OtherUse` varchar(255) DEFAULT NULL,
  `PricePerUnit` decimal(10,2) DEFAULT NULL,
  `QuantityProduced` int(11) NOT NULL,
  `QuantitySold` int(11) DEFAULT NULL,
  `SepQP` int(11) DEFAULT NULL,
  `resourceType_IDResourceType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  KEY `FK_rx2gr7yjaprff8a5ecm0gm3rd` (`resourceType_IDResourceType`),
  KEY `FK_iqaewx6ugdf4vxyc8h4en15fv` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_iqaewx6ugdf4vxyc8h4en15fv` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `WealthGroupInterview` (`WGIID`),
  CONSTRAINT `FK_rx2gr7yjaprff8a5ecm0gm3rd` FOREIGN KEY (`resourceType_IDResourceType`) REFERENCES `ResourceType` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WealthGroupInterview_employment` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `CashPaymentPerUnit` decimal(10,2) DEFAULT NULL,
  `Duration` int(11) DEFAULT NULL,
  `EmploymentName` varchar(50) DEFAULT NULL,
  `FoodPaymentPerUnit` decimal(10,2) DEFAULT NULL,
  `Frequency` varchar(45) DEFAULT NULL,
  `PeopleCount` int(11) NOT NULL,
  `WorkUnit` varchar(45) DEFAULT NULL,
  `resourceType_IDResourceType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  KEY `FK_fnfdms248eusoflbkx0pylq74` (`resourceType_IDResourceType`),
  KEY `FK_2hxfvpov5xbln6n0qlc0hjh3q` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_2hxfvpov5xbln6n0qlc0hjh3q` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `WealthGroupInterview` (`WGIID`),
  CONSTRAINT `FK_fnfdms248eusoflbkx0pylq74` FOREIGN KEY (`resourceType_IDResourceType`) REFERENCES `ResourceType` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WealthGroupInterview_liveStockUse` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `LocalUnit` varchar(45) DEFAULT NULL,
  `LSIncomeType` int(11) DEFAULT NULL,
  `LSIncomeType2` varchar(255) DEFAULT NULL,
  `LSName` varchar(50) DEFAULT NULL,
  `OtherUse` varchar(255) DEFAULT NULL,
  `PricePerUnit` decimal(10,2) DEFAULT NULL,
  `QuantityProduced` int(11) NOT NULL,
  `QuantitySold` int(11) DEFAULT NULL,
  `resourceType_IDResourceType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  KEY `FK_hjjahkyty7rvj9q78xepjmdrf` (`resourceType_IDResourceType`),
  KEY `FK_8xlq9ref9yqct0i2fngcb9wrs` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_8xlq9ref9yqct0i2fngcb9wrs` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `WealthGroupInterview` (`WGIID`),
  CONSTRAINT `FK_hjjahkyty7rvj9q78xepjmdrf` FOREIGN KEY (`resourceType_IDResourceType`) REFERENCES `ResourceType` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WealthGroupInterview_transfer` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `LocalUnit` varchar(45) DEFAULT NULL,
  `OtherUse` varchar(255) DEFAULT NULL,
  `PricePerUnit` decimal(10,2) DEFAULT NULL,
  `QuantityReceived` int(11) NOT NULL,
  `QuantitySold` int(11) DEFAULT NULL,
  `TransferredResourceName` varchar(50) DEFAULT NULL,
  `resourceType_IDResourceType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  KEY `FK_hhphronxcbelvl3a7wky51fa5` (`resourceType_IDResourceType`),
  KEY `FK_6ui6qpma5gyfdmdb03vmkg3nv` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_6ui6qpma5gyfdmdb03vmkg3nv` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `WealthGroupInterview` (`WGIID`),
  CONSTRAINT `FK_hhphronxcbelvl3a7wky51fa5` FOREIGN KEY (`resourceType_IDResourceType`) REFERENCES `ResourceType` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WealthGroupInterview_wildFood` (
  `WealthGroupInterview_WGIID` varchar(32) NOT NULL,
  `LocalUnit` varchar(45) DEFAULT NULL,
  `OtherUse` varchar(255) DEFAULT NULL,
  `PricePerUnit` decimal(10,2) DEFAULT NULL,
  `QuantityProduced` int(11) DEFAULT NULL,
  `QuantitySold` int(11) DEFAULT NULL,
  `WildFoodName` varchar(50) DEFAULT NULL,
  `resourceType_IDResourceType` varchar(32) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  KEY `FK_jkwee2cr41v7ycxie9h8r3r9p` (`resourceType_IDResourceType`),
  KEY `FK_bp21nuxupo63nifhu9idgy51o` (`WealthGroupInterview_WGIID`),
  CONSTRAINT `FK_bp21nuxupo63nifhu9idgy51o` FOREIGN KEY (`WealthGroupInterview_WGIID`) REFERENCES `WealthGroupInterview` (`WGIID`),
  CONSTRAINT `FK_jkwee2cr41v7ycxie9h8r3r9p` FOREIGN KEY (`resourceType_IDResourceType`) REFERENCES `ResourceType` (`IDResourceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SELECT * FROM efd.WealthGroupInterview_assetLiveStock;