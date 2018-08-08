CREATE OR REPLACE VIEW WGInterview_nonfoodpurchased_details AS
    SELECT 
        project.ProjectTitle Project,
        DATE_FORMAT(project.pdate, '%d/%m/%y') ProjectDate,
        project.altExchangeRate ExchangeRate,
        livelihoodzone.LZName LZName,
        country.CountryName Country,
        site.LocationDistrict District,
        site.SubDistrict SubDistrict,
        wealthgroup.WGName_Eng WGNameEng,
        wealthgroup.WGHHSize WGNameLoc,
        wealthgroup.WGOrder WGOrder,
        wealthgroup.WGPercent WGPercent,
        wealthgroup.WGWives WGWives,
        wealthgroup.CommunityID CommunityID,
        wealthgroupinterview.WGID WGID,
        WealthGroupInterview_WGIID WGIIID,
        DATE_FORMAT(wealthgroupinterview.WGInterviewDate,
                '%d/%m/%y') WGInterviewDate,
        wealthgroupinterview.WGIntervieweesCount WGIntervieweesCount,
        wealthgroupinterview.WGInterviewers WGInterviewers,
        wealthgroupinterview.WGInterviewNumber WGInterviewNumber,
        wealthgroupinterview.WGISpreadsheet WGISpreadsheet,
        wealthgroupinterview.WGIStatus WGIStatus,
        wealthgroupinterview.WGMaleIVees WGMaleIVees,
        wealthgroupinterview.WGFemaleIVees WGFemaleIVees,
        wealthgroupinterview.WGYearType WGYearType,
        resourcetype.ResourceTypeName ResourceType,
        ResourceSubType.ResourceTypeName ResourceSubTypeName,
        resourcesubtype.ResourceSubTypeUnit ResourceUnit,
        resourcesubtype.ResourceSubTypeKCal ResourceKCal,
        wealthgroupinterview_nonfoodpurchase.Unit unit,
        wealthgroupinterview_nonfoodpurchase.Status status,
        wealthgroupinterview_nonfoodpurchase.UnitsPurchased UnitsPurchased,
        wealthgroupinterview_nonfoodpurchase.PricePerUnit
    FROM
        project,
        country,
        livelihoodzone,
        projectlz,
        site,
        community,
        wealthgroup,
        wealthgroupinterview,
        resourcetype,
        ResourceSubType,
        wealthgroupinterview_nonfoodpurchase
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_nonfoodpurchase.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_nonfoodpurchase.ResourceSubType = ResourceSubType.IDResourceSubType
            AND ResourceSubType.ReourceType = resourcetype.IDResourceType