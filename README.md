# efdrest

David Brown
Feb 2021

Provide API access to IDAPS_Livelihoods 

#Dev Notes

Currrently set to port 8081

Examples are documented using OpenAPI and Swagger http://server:8081/swagger-ui

Move to Intellij from Eclipse to support Spring5

Use OX jar files and copy over efd.model etc into intellij Watch for invalid FK column names - use JoinColumn to identify the column name in mysql db

DO NOT use spring dev tools in IDE - cache gets confused and errors all over in ModellingReports

