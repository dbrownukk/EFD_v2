CREATE OR REPLACE VIEW WGInterview_food_consumed_details AS
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
        wealthgroupinterview_crop.Unit unit,
        wealthgroupinterview_crop.Status status,
        wealthgroupinterview_crop.UnitsProduced val1,
        wealthgroupinterview_crop.UnitsSold val2,
        wealthgroupinterview_crop.PricePerUnit val3,
        wealthgroupinterview_crop.UnitsConsumed * resourcesubtype.ResourceSubTypeKCal val4
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
        wealthgroupinterview_crop
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_crop.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_crop.ResourceSubType = ResourceSubType.IDResourceSubType
            AND ResourceSubType.ReourceType = resourcetype.IDResourceType 
    UNION SELECT 
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
        wealthgroupinterview_transfer.Unit unit,
        wealthgroupinterview_transfer.Status status,
        wealthgroupinterview_transfer.UnitesTransferred val1,
        wealthgroupinterview_transfer.UnitsSold val2,
        wealthgroupinterview_transfer.PricePerUnit val3,
        wealthgroupinterview_transfer.UnitsConsumed * resourcesubtype.ResourceSubTypeKCal val4
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
        wealthgroupinterview_transfer
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_transfer.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_transfer.ResourceSubType = ResourceSubType.IDResourceSubType
            AND ResourceSubType.ReourceType = resourcetype.IDResourceType 
    UNION SELECT 
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
        wealthgroupinterview_livestockproducts.Unit unit,
        wealthgroupinterview_livestockproducts.Status status,
        wealthgroupinterview_livestockproducts.UnitsProduced val1,
        wealthgroupinterview_livestockproducts.UnitsSold val2,
        wealthgroupinterview_livestockproducts.PricePerUnit val3,
        wealthgroupinterview_livestockproducts.UnitsConsumed * resourcesubtype.ResourceSubTypeKCal val4
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
        wealthgroupinterview_livestockproducts
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_livestockproducts.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_livestockproducts.ResourceSubType = ResourceSubType.IDResourceSubType
            AND ResourceSubType.ReourceType = resourcetype.IDResourceType 
    UNION SELECT 
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
        wealthgroupinterview_wildfood.Unit unit,
        wealthgroupinterview_wildfood.Status status,
        wealthgroupinterview_wildfood.UnitsProduced val1,
        wealthgroupinterview_wildfood.UnitsSold val2,
        wealthgroupinterview_wildfood.PricePerUnit val3,
        wealthgroupinterview_wildfood.UnitsConsumed * resourcesubtype.ResourceSubTypeKCal val4
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
        wealthgroupinterview_wildfood
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_wildfood.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_wildfood.ResourceSubType = ResourceSubType.IDResourceSubType
            AND ResourceSubType.ReourceType = resourcetype.IDResourceType
