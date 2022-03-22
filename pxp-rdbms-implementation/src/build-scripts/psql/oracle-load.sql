/**
 * LOADER Script for test data
 * Author:  vallee
 * Created: May 2, 2019
 * ---
 * There is no obvious method to load CSV files into ORACLE.
 * for test data, an approach here consists in using insert commands prepared by EXCEL formulas.
 */
--
-- C:>SET ORACLE_SID=PXP
-- C:>sqlplus pxp/pxp123 @oracle-load.sql
-- 
set autocommit on;
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
delete from pxp.contextualObject;
delete from pxp.eventqueue;
delete from pxp.objectRevision;
delete from pxp.objectTracking;
delete from pxp.TaskRoleLink;
delete from pxp.TaskUserLink;
delete from pxp.task;
delete from pxp.baseentityqualityrulelink;

-- Reinit the database
delete from pxp.userSession;
delete from pxp.classifierConfig where classifierIID >= 1000;
delete from pxp.contextConfig;
delete from pxp.tagValueConfig where propertyIID >= 1000;
delete from pxp.propertyConfig where propertyIID >= 1000;
delete from pxp.CatalogConfig where catalogCode not in ('pim','onboarding','offboarding');
delete from pxp.UserConfig where userIID >= 1000;
delete from pxp.taskConfig;
delete from pxp.ruleConfig;
delete from pxp.ruleExpression;



-- preparing the files implies to use the formulas prepared in last column 
-- of every EXCEL sheet and to copy/paste here the produced insert commands:
-- load configuration data
@@ ../../../src/test/data/Test-Data/sql/pxp.UserConfig.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.CatalogConfig.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.propertyConfig.sql 
@@ ../../../src/test/data/Test-Data/sql/pxp.contextConfig.sql 
@@ ../../../src/test/data/Test-Data/sql/pxp.classifierConfig.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.tagValueConfig.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.taskConfig.SQL
@@ ../../../src/test/data/Test-Data/sql/pxp.ruleConfig.SQL
@@ ../../../src/test/data/Test-Data/sql/pxp.ruleExpression.SQL
--
-- load entity data
@@ ../../../src/test/data/Test-Data/sql/pxp.ContextualObject.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.Graph.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.BaseEntity.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.BaseEntityClassifierLink.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.CoupledRecord.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.Relation.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.valuerecord.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.TagsRecord.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.objectTracking.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.ContextBaseEntityLink.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.Task.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.TaskUserLink.sql
@@ ../../../src/test/data/Test-Data/sql/pxp.TaskRoleLink.SQL
@@ ../../../src/test/data/Test-Data/sql/pxp.baseentityqualityrulelink.sql


-- end of entity data
-- 
quit;
