CREATE OR REPLACE 
    ALGORITHM = UNDEFINED 
    DEFINER = `efd`@`%` 
    SQL SECURITY DEFINER
VIEW `wginterview_details` AS
    SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_assetcash`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_assetcash`.`Unit` AS `unit`,
        `wealthgroupinterview_assetcash`.`Status` AS `status`,
         `wealthgroupinterview_assetcash`.`Amount` as `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_assetcash`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_assetcash`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_assetcash`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_assetfoodstock`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_assetfoodstock`.`Unit` AS `unit`,
        `wealthgroupinterview_assetfoodstock`.`Status` AS `status`,
		`wealthgroupinterview_assetfoodstock`.`Quantity` as `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_assetfoodstock`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_assetfoodstock`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_assetfoodstock`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_assetland`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_assetland`.`Unit` AS `unit`,
        `wealthgroupinterview_assetland`.`Status` AS `status`,
        `wealthgroupinterview_assetland`.`NumberofUnits` as `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_assetland`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_assetland`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_assetland`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_assetlivestock`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_assetlivestock`.`Unit` AS `unit`,
        `wealthgroupinterview_assetlivestock`.`Status` AS `status`,
		`wealthgroupinterview_assetlivestock`.`NumberofUnits` as `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_assetlivestock`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_assetlivestock`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_assetlivestock`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_assettradeable`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_assettradeable`.`Unit` AS `unit`,
        `wealthgroupinterview_assettradeable`.`Status` AS `status`,
		`wealthgroupinterview_assettradeable`.`NumberOwned` as `x`
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_assettradeable`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_assettradeable`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_assettradeable`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_assettree`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_assettree`.`Unit` AS `unit`,
        `wealthgroupinterview_assettree`.`Status` AS `status`,
			 `wealthgroupinterview_assettree`.`NumberOwned` as `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_assettree`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_assettree`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_assettree`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_crop`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_crop`.`Unit` AS `unit`,
        `wealthgroupinterview_crop`.`Status` AS `status`,
						  `wealthgroupinterview_crop`.`UnitsProduced` as `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_crop`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_crop`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_crop`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_employment`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_employment`.`Unit` AS `unit`,
        `wealthgroupinterview_employment`.`Status` AS `status`,
        `wealthgroupinterview_employment`.`PeopleCount` AS `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_employment`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_employment`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_employment`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_foodpurchase`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_foodpurchase`.`Unit` AS `unit`,
        `wealthgroupinterview_foodpurchase`.`Status` AS `status`,
        `wealthgroupinterview_foodpurchase`.`UnitsPurchased` AS `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_foodpurchase`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_foodpurchase`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_foodpurchase`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_nonfoodpurchase`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_nonfoodpurchase`.`Unit` AS `unit`,
        `wealthgroupinterview_nonfoodpurchase`.`Status` AS `status`,
         `wealthgroupinterview_nonfoodpurchase`.`ItemPurchased` AS `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_nonfoodpurchase`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_nonfoodpurchase`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_nonfoodpurchase`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_livestockproducts`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_livestockproducts`.`Unit` AS `unit`,
        `wealthgroupinterview_livestockproducts`.`Status` AS `status`,
        `wealthgroupinterview_livestockproducts`.`UnitsProduced` AS `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_livestockproducts`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_livestockproducts`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_livestockproducts`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_livestocksales`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_livestocksales`.`Unit` AS `unit`,
        `wealthgroupinterview_livestocksales`.`Status` AS `status`,
        `wealthgroupinterview_livestocksales`.`UnitsSold` AS `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_livestocksales`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_livestocksales`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_livestocksales`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_transfer`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_transfer`.`Unit` AS `unit`,
        `wealthgroupinterview_transfer`.`Status` AS `status`,
        `wealthgroupinterview_transfer`.`UnitesTransferred` AS `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_transfer`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_transfer`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_transfer`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`)) 
    UNION SELECT 
        `project`.`ProjectTitle` AS `Project`,
        DATE_FORMAT(`project`.`PDate`, `%d/%m/%y`) AS `ProjectDate`,
        `project`.`altExchangeRate` AS `ExchangeRate`,
        `livelihoodzone`.`LZName` AS `LZName`,
        `country`.`CountryName` AS `Country`,
        `site`.`LocationDistrict` AS `District`,
        `site`.`SubDistrict` AS `SubDistrict`,
        `wealthgroup`.`WGName_Eng` AS `WGNameEng`,
        `wealthgroup`.`WGHHSize` AS `WGNameLoc`,
        `wealthgroup`.`WGOrder` AS `WGOrder`,
        `wealthgroup`.`WGPercent` AS `WGPercent`,
        `wealthgroup`.`WGWives` AS `WGWives`,
        `wealthgroup`.`CommunityID` AS `CommunityID`,
        `wealthgroupinterview`.`WGID` AS `WGID`,
        `wealthgroupinterview_wildfood`.`WealthGroupInterview_WGIID` AS `WGIIID`,
        DATE_FORMAT(`wealthgroupinterview`.`WGInterviewDate`,
                `%d/%m/%y`) AS `WGInterviewDate`,
        `wealthgroupinterview`.`WGIntervieweesCount` AS `WGIntervieweesCount`,
        `wealthgroupinterview`.`WGInterviewers` AS `WGInterviewers`,
        `wealthgroupinterview`.`WGInterviewNumber` AS `WGInterviewNumber`,
        `wealthgroupinterview`.`WGISpreadsheet` AS `WGISpreadsheet`,
        `wealthgroupinterview`.`WGIStatus` AS `WGIStatus`,
        `wealthgroupinterview`.`WGMaleIVees` AS `WGMaleIVees`,
        `wealthgroupinterview`.`WGFemaleIVees` AS `WGFemaleIVees`,
        `wealthgroupinterview`.`WGYearType` AS `WGYearType`,
        `resourcetype`.`ResourceTypeName` AS `ResourceType`,
        `resourcesubtype`.`ResourceTypeName` AS `ResourceSubTypeName`,
        `resourcesubtype`.`ResourceSubTypeUnit` AS `ResourceUnit`,
        `resourcesubtype`.`ResourceSubTypeKCal` AS `ResourceKCal`,
        `wealthgroupinterview_wildfood`.`Unit` AS `unit`,
        `wealthgroupinterview_wildfood`.`Status` AS `status`,
          `wealthgroupinterview_wildfood`.`UnitsConsumed` AS `x`
    FROM
        ((((((((((`project`
        JOIN `country`)
        JOIN `livelihoodzone`)
        JOIN `projectlz`)
        JOIN `site`)
        JOIN `community`)
        JOIN `wealthgroup`)
        JOIN `wealthgroupinterview`)
        JOIN `resourcetype`)
        JOIN `resourcesubtype`)
        JOIN `wealthgroupinterview_wildfood`)
    WHERE
        ((`project`.`ProjectID` = `projectlz`.`Project`)
            AND (`livelihoodzone`.`LZID` = `projectlz`.`LZ`)
            AND (`livelihoodzone`.`LZID` = `site`.`LZ`)
            AND (`country`.`IDCountry` = `livelihoodzone`.`LZCountry`)
            AND (`community`.`CLocation` = `site`.`LocationID`)
            AND (`community`.`CID` = `wealthgroup`.`CommunityID`)
            AND (`wealthgroup`.`WealthGroupID` = `wealthgroupinterview`.`WGID`)
            AND (`community`.`CProject` = `project`.`ProjectID`)
            AND (`wealthgroupinterview_wildfood`.`WealthGroupInterview_WGIID` = `wealthgroupinterview`.`WGIID`)
            AND (`wealthgroupinterview_wildfood`.`ResourceSubType` = `resourcesubtype`.`IDResourceSubType`)
            AND (`resourcesubtype`.`ReourceType` = `resourcetype`.`IDResourceType`));
