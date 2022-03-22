/**
 * Author:  vallee
 * Created: Mar 16, 2019
 * Model V update
 */
/*
    PostgreSQL 11 instructions:
    --------------------------
    for automation, the script is run on-line from
    $>psql -h localhost -p 5433 -d pxp -U pxp -f file.sql
    
    for manual setup, copy/paste the script into a query tools from PGAdmin4
    and execute on PXP database

    ORACLE 12c instructions:
    ------------------------
    for automation, the script is run on-line from
    $>set ORACLE_SID = PXP
    $>sqlplus pxp/pxp123 @file.sql

    for manual setup, copy/paste the script SQLDeveloper and execute
*/
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

#ifdef PGSQL11
    /* Schema creation required for Postgre only */
    create schema if not exists pxp;
    alter schema pxp owner TO pxp;
#elif ORACLE12C
    /* no need to create schema in ORACLE? */
    /* declare types (in schema PXP = mandatory) */
    drop type pxp.varcharArray force;
    create or replace type pxp.varcharArray as table of _VARCHAR;
    /
    drop type pxp.iidArray force;
    create or replace type pxp.iidArray as table of number;
    /
#endif

-- common sequence creation for business objects (e.g. base entity IIDs, classifiers IID, etc.)
_CREATE_SEQUENCE( pxp.seqIID, 1000000); 

-- sequence creation of unique IDs generation for custom usages and config codes
_CREATE_SEQUENCE( pxp.uniqueIID, 1000);
 
-- Sequence creation for oject tracking
_CREATE_SEQUENCE( pxp.seqObjectTrackIID, 1000000);

/* 
    Configuration tables definition
    -------------------------------
*/
_TABLE( pxp.UserConfig ) (
	userIID     _IID  primary key,
	userName    _VARCHAR not null
);
_CREATE_UNIQUE_INDEX1(UserConfig, userName);

_TABLE(pxp.PropertyConfig) (
	propertyIID  _IID  primary key,
	propertyCode _VARCHAR not null,
    superType    _SHORT not null,
	propertyType _SHORT not null
);
_CREATE_UNIQUE_INDEX1(PropertyConfig, propertyCode);
_CREATE_INDEX1(PropertyConfig, propertyType);

_TABLE( pxp.ContextConfig) (
	contextCode _VARCHAR  primary key,
	contextType _SHORT not null
);

_TABLE(pxp.LanguageConfig) (
	languageIID _IID  primary key,
	languageCode _VARCHAR not null,
    	parentIIDs _IIDARRAY
);

_TABLE(pxp.ClassifierConfig) (
	classifierIID _IID  primary key,
	classifierCode _VARCHAR not null,
	classifierType _SHORT not null,
    hierarchyIIDs _IIDARRAY
);

_CREATE_UNIQUE_INDEX1(ClassifierConfig, classifierCode);

_TABLE(pxp.tagValueConfig) (
        tagValueCode    _VARCHAR primary key,
        propertyIID     _IID references pxp.PropertyConfig( propertyIID)
);

_TABLE(pxp.taskConfig) (
        taskCode    _VARCHAR primary key,
        taskType    _INT
);

--
-- Table to hold localid language code as id which is the short form of language for the region
-- and language.  
--
_TABLE(pxp.localeIDLanguage) (
    id       _VARCHAR primary key,
    language _VARCHAR not null
);

-- User can add language and its code as and when required in the table.

insert into pxp.localeIDLanguage values('ar', 'arabic' );
insert into pxp.localeIDLanguage values('da', 'danish' );
insert into pxp.localeIDLanguage values('de', 'german' );
insert into pxp.localeIDLanguage values('el', 'greek' );
insert into pxp.localeIDLanguage values('es', 'spanish' );
insert into pxp.localeIDLanguage values('en', 'english' );
insert into pxp.localeIDLanguage values('fr', 'french');
insert into pxp.localeIDLanguage values('hi', 'hindi' );
insert into pxp.localeIDLanguage values('pt', 'portuguese' );
insert into pxp.localeIDLanguage values('pl', 'polish' );
insert into pxp.localeIDLanguage values('ru', 'russian' );
insert into pxp.localeIDLanguage values('sv', 'swedish' );

#ifdef PGSQL11
-- end of PGSQL Infrastructure declarations
#elif ORACLE12C
-- end of ORACLE Infrastructure declarations
#endif
