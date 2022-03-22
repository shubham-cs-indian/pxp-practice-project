/**
 * LOADER Script for test data
 * Author:  vallee
 * Created: Apr 5, 2019
 */
-- C:>SET PGPASSWORD=pxp123
-- C:>psql -h localhost -p 5433 -d pxp -U pxp -f postgres-load.sql
drop view pxp.valfr_CAen_CAen_US;

delete from pxp.ContextBaseEntityLink;
delete from pxp.backgroundprocess;
delete from pxp.eventqueue;
delete from pxp.TagsRecord;
delete from pxp.valuerecord;
delete from pxp.CoupledRecord;
delete from pxp.Relation;
delete from pxp.BaseEntityClassifierLink;
delete from pxp.BaseEntity;
delete from pxp.Graph;
delete from pxp.contextualObject;
delete from pxp.objectRevision;
delete from pxp.eventqueue;
delete from pxp.objectRevision;
delete from pxp.objectTracking;
delete from pxp.taskRoleLink;
delete from pxp.taskUserLink;
delete from pxp.task;
delete from pxp.baseentityqualityrulelink;

-- Reinit the database
delete from pxp.userSession;
delete from pxp.classifierConfig where classifierIID >= 1000;
delete from pxp.contextConfig;
delete from pxp.tagValueConfig where propertyIID >= 1000;
delete from pxp.propertyConfig where propertyIID >= 1000;
delete from pxp.UserConfig where userIID >= 1000;
delete from pxp.taskConfig;
delete from pxp.ruleConfig;
delete from pxp.ruleExpression;
delete from pxp.baseEntityLocaleIdLink;

-- load configuration data
\copy pxp.UserConfig from '../../test/data/Test-Data/csv/pxp.UserConfig.csv' csv header 
\copy pxp.propertyConfig from '../../test/data/Test-Data/csv/pxp.propertyConfig.csv' csv header 
\copy pxp.contextConfig from '../../test/data/Test-Data/csv/pxp.contextConfig.csv' csv header 
\copy pxp.classifierConfig from '../../test/data/Test-Data/csv/pxp.classifierConfig.csv' csv header 
\copy pxp.tagValueConfig from '../../test/data/Test-Data/csv/pxp.tagValueConfig.csv' csv header
\copy pxp.taskConfig from '../../test/data/Test-Data/csv/pxp.taskConfig.csv' csv header
\copy pxp.ruleConfig from '../../test/data/Test-Data/csv/pxp.ruleConfig.csv' csv header
\copy pxp.ruleExpression from '../../test/data/Test-Data/csv/pxp.ruleExpression.csv' csv header
\copy pxp.languageconfig from '../../test/data/Test-Data/csv/pxp.languageconfig.csv' csv header



-- end of configuration data
-- load entity data
\copy pxp.ContextualObject from '../../test/data/Test-Data/csv/pxp.contextualObject.csv' csv header 
\copy pxp.Graph from '../../test/data/Test-Data/csv/pxp.graph.csv' csv header 
\copy pxp.BaseEntity from '../../test/data/Test-Data/csv/pxp.BaseEntity.csv' csv header 
\copy pxp.BaseEntityClassifierLink from '../../test/data/Test-Data/csv/pxp.BaseEntityClassifierLink.csv' csv header 
\copy pxp.CoupledRecord from '../../test/data/Test-Data/csv/pxp.CoupledRecord.csv' csv header 
\copy pxp.Relation from '../../test/data/Test-Data/csv/pxp.Relation.csv' csv header 
\copy pxp.valuerecord from '../../test/data/Test-Data/csv/pxp.valuerecord.csv' csv header 
\copy pxp.TagsRecord from '../../test/data/Test-Data/csv/pxp.TagsRecord.csv' csv header 
\copy pxp.objectTracking from '../../test/data/Test-Data/csv/pxp.objectTracking.csv' csv header 
\copy pxp.ContextBaseEntityLink from '../../test/data/Test-Data/csv/pxp.ContextBaseEntityLink.csv' csv header 
\copy pxp.Task from '../../test/data/Test-Data/csv/pxp.Task.csv' csv header 
\copy pxp.TaskRoleLink from '../../test/data/Test-Data/csv/pxp.TaskRoleLink.csv' csv header 
\copy pxp.TaskUserLink from '../../test/data/Test-Data/csv/pxp.TaskUserLink.csv' csv header
\copy pxp.baseentityqualityrulelink from '../../test/data/Test-Data/csv/pxp.baseentityqualityrulelink.csv' csv header
\copy pxp.baseEntityLocaleIdLink from '../../test/data/Test-Data/csv/pxp.baseEntityLocaleIdLink.csv' csv header
\copy pxp.eventqueue from '../../test/data/Test-Data/csv/pxp.eventqueue.csv' csv header
-- end of entity data

