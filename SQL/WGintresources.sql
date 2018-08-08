CREATE OR REPLACE VIEW WGInterview_details AS
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
        wealthgroupinterview_assetcash.Unit unit,
        wealthgroupinterview_assetcash.Status status
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
        wealthgroupinterview_assetcash
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_assetcash.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_assetcash.ResourceSubType = ResourceSubType.IDResourceSubType
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
        wealthgroupinterview_assetfoodstock.Unit unit,
        wealthgroupinterview_assetfoodstock.Status status
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
        wealthgroupinterview_assetfoodstock
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_assetfoodstock.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_assetfoodstock.ResourceSubType = ResourceSubType.IDResourceSubType
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
        wealthgroupinterview_assetland.Unit unit,
        wealthgroupinterview_assetland.Status status
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
        wealthgroupinterview_assetland
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_assetland.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_assetland.ResourceSubType = ResourceSubType.IDResourceSubType
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
        wealthgroupinterview_assetlivestock.Unit unit,
        wealthgroupinterview_assetlivestock.Status status
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
        wealthgroupinterview_assetlivestock
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_assetlivestock.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_assetlivestock.ResourceSubType = ResourceSubType.IDResourceSubType
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
        wealthgroupinterview_assettradeable.Unit unit,
        wealthgroupinterview_assettradeable.Status status
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
        wealthgroupinterview_assettradeable
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_assettradeable.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_assettradeable.ResourceSubType = ResourceSubType.IDResourceSubType
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
        wealthgroupinterview_assettree.Unit unit,
        wealthgroupinterview_assettree.Status status
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
        wealthgroupinterview_assettree
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_assettree.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_assettree.ResourceSubType = ResourceSubType.IDResourceSubType
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
        wealthgroupinterview_crop.Unit unit,
        wealthgroupinterview_crop.Status status
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
        wealthgroupinterview_employment.Unit unit,
        wealthgroupinterview_employment.Status status
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
        wealthgroupinterview_employment
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_employment.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_employment.ResourceSubType = ResourceSubType.IDResourceSubType
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
        wealthgroupinterview_foodpurchase.Unit unit,
        wealthgroupinterview_foodpurchase.Status status
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
        wealthgroupinterview_foodpurchase
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_foodpurchase.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_foodpurchase.ResourceSubType = ResourceSubType.IDResourceSubType
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
        wealthgroupinterview_nonfoodpurchase.Unit unit,
        wealthgroupinterview_nonfoodpurchase.Status status
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
        wealthgroupinterview_livestockproducts.Status status
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
        wealthgroupinterview_livestocksales.Unit unit,
        wealthgroupinterview_livestocksales.Status status
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
        wealthgroupinterview_livestocksales
    WHERE
        project.ProjectID = projectlz.Project
            AND livelihoodzone.LZID = projectlz.LZ
            AND livelihoodzone.LZID = site.LZ
            AND country.IDCountry = livelihoodzone.LZCountry
            AND community.CLocation = site.LocationID
            AND community.CID = wealthgroup.CommunityID
            AND wealthgroup.WealthGroupID = wealthgroupinterview.WGID
            AND community.CProject = project.ProjectID
            AND wealthgroupinterview_livestocksales.WealthGroupInterview_WGIID = wealthgroupinterview.WGIID
            AND wealthgroupinterview_livestocksales.ResourceSubType = ResourceSubType.IDResourceSubType
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
        wealthgroupinterview_transfer.Status status
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
        wealthgroupinterview_wildfood.Unit unit,
        wealthgroupinterview_wildfood.Status status
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