SELECT 
    project.ProjectTitle,
    livelihoodzone.LZName,
    site.LocationDistrict,
    site.SubDistrict,
    wealthgroup.WGName_Eng,
    resourcetype.ResourceTypeName,
    ResourceSubType.ResourceTypeName
FROM
    project,
    livelihoodzone,
    projectlz,
    site,
    community,
    wealthgroup,
    wgcharacteristicsresource,
    ResourceSubType,
    resourcetype
WHERE
    project.ProjectID = projectlz.Project
        AND livelihoodzone.LZID = projectlz.LZ
        AND livelihoodzone.LZID = site.LZ
        AND community.CLocation = site.LocationID
        AND community.CID = wealthgroup.CommunityID
        AND wgcharacteristicsresource.WGID = wealthgroup.WealthGroupID
        AND wgcharacteristicsresource.WGResourceSubType = ResourceSubType.IDResourceSubType
        AND resourcetype.IDResourceType = ResourceSubType.ReourceType
        AND community.CProject = project.ProjectID
ORDER BY 1 , 2 , 3