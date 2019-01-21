set foreign_key_checks=0;



insert into efdremote.community
select * from efd_remote_stage.community;

insert into efdremote.communityyearnotes
select * from efd_remote_stage.communityyearnotes;

insert into efdremote.country
select * from efd_remote_stage.country;

insert into efdremote.images
select * from efd_remote_stage.images;

insert into efdremote.livelihoodzone
select * from efd_remote_stage.livelihoodzone;

insert into efdremote.project
select * from efd_remote_stage.project;

insert into efdremote.projectlz
select * from efd_remote_stage.projectlz;

insert into efdremote.referencecode
select * from efd_remote_stage.referencecode;

insert into efdremote.resourcesubtype
select * from efd_remote_stage.resourcesubtype;

insert into efdremote.resourcetype
select * from efd_remote_stage.resourcetype;

insert into efdremote.site
select * from efd_remote_stage.site;

insert into efdremote.wealthgroup
select * from efd_remote_stage.wealthgroup;

insert into efdremote.wealthgroupinterview
select * from efd_remote_stage.wealthgroupinterview;

insert into efdremote.wealthgroupinterview_assetcash
select * from efd_remote_stage.wealthgroupinterview_assetcash;

insert into efdremote.wealthgroupinterview_assetfoodstock
select * from efd_remote_stage.wealthgroupinterview_assetfoodstock;

insert into efdremote.wealthgroupinterview_assetland
select * from efd_remote_stage.wealthgroupinterview_assetland;

insert into efdremote.wealthgroupinterview_assetlivestock
select * from efd_remote_stage.wealthgroupinterview_assetlivestock;

insert into efdremote.wealthgroupinterview_assettradeable
select * from efd_remote_stage.wealthgroupinterview_assettradeable;

insert into efdremote.wealthgroupinterview_assettree
select * from efd_remote_stage.wealthgroupinterview_assettree;

insert into efdremote.wealthgroupinterview_crop
select * from efd_remote_stage.wealthgroupinterview_crop;

insert into efdremote.wealthgroupinterview_employment
select * from efd_remote_stage.wealthgroupinterview_employment;

insert into efdremote.wealthgroupinterview_foodpurchase
select * from efd_remote_stage.wealthgroupinterview_foodpurchase;

insert into efdremote.wealthgroupinterview_livestockproducts
select * from efd_remote_stage.wealthgroupinterview_livestockproducts;

insert into efdremote.wealthgroupinterview_livestocksales
select * from efd_remote_stage.wealthgroupinterview_livestocksales;


insert into efdremote.wealthgroupinterview_nonfoodpurchase
select * from efd_remote_stage.wealthgroupinterview_nonfoodpurchase;

insert into efdremote.wealthgroupinterview_transfer
select * from efd_remote_stage.wealthgroupinterview_transfer;

insert into efdremote.wealthgroupinterview_wildfood
select * from efd_remote_stage.wealthgroupinterview_wildfood;

insert into efdremote.wgcharacteristicsresource
select * from efd_remote_stage.wgcharacteristicsresource;


set foreign_key_checks=1;