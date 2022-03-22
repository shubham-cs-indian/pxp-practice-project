/**
 * LOADER Script for test data
 * Author:  vallee
 * Created: Apr 5, 2019
 */
-- C:>psql -h localhost -p 5433 -d pxp -U pxp -f postgres-load.sql
-- pass pxp123

SET CLIENT_ENCODING TO 'utf8';

delete from pxp.eventqueue;
delete from pxp.objectrevision;
delete from pxp.usersession;
delete from pxp.ContextTagRecord;
delete from pxp.objectTracking;
delete from pxp.ValueRecord;
delete from pxp.TagRecord;
delete from pxp.PropertyRecord;
delete from pxp.Relation;
delete from pxp.BaseEntityClassifierLink;
delete from pxp.BaseEntity;
delete from pxp.ContextualObject;

-- Reinit the database
delete from pxp.classifierConfig;
delete from pxp.contextConfig;
delete from pxp.tagValueConfig;
delete from pxp.propertyConfig;
delete from pxp.CatalogConfig;
delete from pxp.UserConfig;
delete from pxp.localeconfig;

-- load configuration data
\copy pxp.localeconfig from 'csv/pxp.localeConfig.csv' csv header 
\copy pxp.UserConfig from 'csv/pxp.UserConfig.csv' csv header 
\copy pxp.CatalogConfig from 'csv/pxp.CatalogConfig.csv' csv header 
\copy pxp.propertyConfig from 'csv/pxp.propertyConfig.csv' csv header 
\copy pxp.tagValueConfig from 'csv/pxp.tagValueConfig.csv' csv header
\copy pxp.contextConfig from 'csv/pxp.contextConfig.csv' csv header 
\copy pxp.classifierConfig from 'csv/pxp.classifierConfig.csv' csv header 
-- end of configuration data

-- load entity data
\copy pxp.ContextualObject from 'csv/pxp.ContextualObject.csv' csv header
\copy pxp.BaseEntity from 'csv/pxp.BaseEntity.csv' csv header 
\copy pxp.BaseEntityClassifierLink from 'csv/pxp.BaseEntityClassifierLink.csv' csv header 
\copy pxp.Relation from 'csv/pxp.Relation.csv' csv header 
\copy pxp.PropertyRecord from 'csv/pxp.PropertyRecord.csv' csv header
\copy pxp.PropertyRecord from 'csv/pxp.PropertyRecord_2.csv' csv header  
\copy pxp.TagRecord from 'csv/pxp.TagRecord.csv' csv header 
\copy pxp.ValueRecord from 'csv/pxp.valuerecord.csv' csv header 
\copy pxp.objectTracking from 'csv/pxp.objectTracking.csv' csv header 
\copy pxp.ContextTagRecord from 'csv/pxp.ContextTagRecord.csv' csv header 
-- end of entity data

-- /!\ IMPORTANT Update sequence start at 5000000 to avoid duplicate keys
drop sequence if exists pxp.seqIID cascade; create sequence pxp.seqIID start 5000000;
