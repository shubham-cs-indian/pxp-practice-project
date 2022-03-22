/**
 * Author:  vallee
 * Created: Mar 23, 2019
 *
 * IMPORTANT NOTICE REGARDING USAGE OF _INT or _SHORT for enum values:
 * Because, PGSQL takes integer as default type for numbers, use _INT instead 
 * of _SHORT to pass enum values through functions.
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

/*----------------------------------------------------------------------------*/
--    Create tables for hash partitioning for PGSQL only
/*----------------------------------------------------------------------------*/
#ifdef PGSQL11
_PROCEDURE( pxp.CreateHashPartitions )( tablename _STRING,  nbPartitions _INT)
_IMPLEMENT_AS
    no          _INT;
    subtable    _VARCHAR;
begin
    for no in 0..nbPartitions-1 loop
        subtable := tablename || no;
        execute format(
            'create table if not exists %s partition of %s for values with (modulus %s, remainder %s)',
            subtable, tablename, nbPartitions, no
        );
    end loop;
end
_IMPLEMENT_END;
#endif

/*----------------------------------------------------------------------------*/
--    Create a unique ID from a prefix
/*----------------------------------------------------------------------------*/
_FUNCTION( pxp.fn_createUniqueID ) ( p_prefix _STRING)
_RETURN varchar
_IMPLEMENT_AS
    vNextID     _IID;
begin
    vNextID := _NEXTVAL( pxp.uniqueIID);
    return p_prefix || _TO_VARCHAR( vNextID);
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--    Pull a series of new data IID from sequence
/*----------------------------------------------------------------------------*/
_FUNCTION( pxp.fn_acquireIIDs ) ( p_number _INT, p_sequence _STRING)
_RETURN _IIDARRAY
_IMPLEMENT_AS
    vSequenceIIDs     _IIDARRAY;
	vCounter		  _INT;
begin
 	if ( p_sequence = 'seqIID' ) then
		for vCounter in 1 .. p_number loop
			_ARRAY( vSequenceIIDs, vCounter) := _NEXTVAL( pxp.seqIID);
		end loop;
	elsif ( p_sequence = 'seqObjectTrackIID' ) then
		for vCounter in 1 .. p_number loop
			_ARRAY( vSequenceIIDs, vCounter) := _NEXTVAL( pxp.seqObjectTrackIID);
		end loop;
	else /* default */
		for vCounter in 1 .. p_number loop
			_ARRAY( vSequenceIIDs, vCounter) := _NEXTVAL( pxp.uniqueIID);
		end loop;
	end if;
    return vSequenceIIDs;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--    Lazy synchronization of Users
/*----------------------------------------------------------------------------*/
_FUNCTION( pxp.fn_userConfig )( p_userName _STRING)
_RETURN _LONG
_IMPLEMENT_AS
    vUserIID        _LONG; -- corresponding to the iid
begin
    -- check vUseriid existence
    begin
        select userIID into vUserIID from pxp.userConfig where userName iLIKE p_userName;
    exception
        when no_data_found then vUserIID := null;
    end;
    -- lazy initialization of config tables
    if ( vUserIID is null ) then
        vUserIID := _NEXTVAL( pxp.seqIID);
        insert into pxp.userConfig( userIID, userName) values ( vUserIID, p_userName);
    end if;
    return vUserIID;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--  synchronization of standard Users
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.sp_standardUserConfig )(p_userIID _LONG, p_userName _STRING)
_IMPLEMENT_AS
    vUserIID _LONG; -- corresponding to the iid
begin
    -- check vUseriid existence
    begin
        select userIID into vUserIID from pxp.userConfig where userName iLIKE p_userName;
    exception
        when no_data_found then vUserIID := null;
    end;
    -- lazy initialization of config tables
    if ( vUserIID is null ) then
        insert into pxp.userConfig( userIID, userName) values ( p_userIID, p_userName);
    else 
      if (p_userIID != vUserIID) then
      _RAISE( 'Mismatch user IID: %', _TO_VARCHAR(p_userIID));  
      end if;
    end if;
end
_IMPLEMENT_END;


/*----------------------------------------------------------------------------*/
--  synchronization of Properties by Code
/*----------------------------------------------------------------------------*/
_FUNCTION( pxp.fn_propertyConfig )( p_propertyCode _STRING, p_propertyType _INT, p_superType _INT)
_RETURN _LONG
_IMPLEMENT_AS
    vPropertyIID        _LONG; -- corresponding to the iid
begin
    -- check vPropertyIID existence
    begin
        select propertyIID into vPropertyIID from pxp.propertyConfig 
        where propertyCode = p_propertyCode;
    exception
        when no_data_found then vPropertyIID := null;
    end;
    -- lazy initialization of config tables
    if ( vPropertyIID is null ) then
        vPropertyIID := _NEXTVAL( pxp.seqIID);
        insert into pxp.propertyConfig(propertyIID, propertyCode, propertyType, superType) 
                values (vPropertyIID, p_propertyCode, p_propertyType, p_superType);
                
    end if;
    return vPropertyIID;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
-- synchronization of standard Properties by Code
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.sp_standardPropertyConfig )( p_propertyIID _LONG, p_propertyCode _STRING, p_propertyType _INT, p_superType _INT)
_IMPLEMENT_AS
    vPropertyIID _LONG; -- corresponding to the iid
begin
    -- check vPropertyIID existence
    begin
        select propertyIID into vPropertyIID from pxp.propertyConfig
        where propertyCode = p_propertyCode;
    exception
        when no_data_found then vPropertyIID := null;
    end;
    -- lazy initialization of config tables
    if ( vPropertyIID is null ) then
        insert into pxp.propertyConfig(propertyIID, propertyCode, propertyType, superType)
                values (p_propertyIID, p_propertyCode, p_propertyType, p_superType);
     else 
      if (p_propertyIID != vPropertyIID) then
      _RAISE( 'Mismatch property IID: %', _TO_VARCHAR(p_propertyIID));  
      end if;            
    end if;
end
_IMPLEMENT_END;


/*----------------------------------------------------------------------------*/
--   synchronization of Contexts by Code
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.sp_contextConfig)( p_contextCode _STRING, p_contextType _INT)
_IMPLEMENT_AS
    vContextType        _INT; -- corresponding to the iid
begin
    -- check vContextIID existence
    begin
        select contextType into vContextType from pxp.contextConfig 
        where contextCode = p_contextCode;
    exception
        when no_data_found then vContextType := null;
    end;
    -- lazy initialization of config tables
    if ( vContextType is null ) then
        insert into pxp.contextConfig( contextCode, contextType) 
            values ( p_contextCode, p_contextType);
    end if;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--  synchronization of Classifiers by Code
/*----------------------------------------------------------------------------*/
_FUNCTION( pxp.fn_classifierConfig)( p_classifierCode _STRING, p_classifierType _INT)
_RETURN _LONG
_IMPLEMENT_AS
    vClassifierIID        _LONG; -- corresponding to the id
begin
    -- check vClassifierIID existence
    begin
        select classifierIID into vClassifierIID from pxp.classifierConfig 
        where classifierCode = p_classifierCode;
    exception
        when no_data_found then vClassifierIID := null;
    end;
    -- lazy initialization of config tables
    if ( vClassifierIID is null ) then
        vClassifierIID := _NEXTVAL( pxp.seqIID);
        insert into pxp.classifierConfig(classifierIID, classifierCode, classifierType) 
        values (vClassifierIID, p_classifierCode, p_classifierType);
    end if;
    return vClassifierIID;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
-- synchronization of standard Classifiers by Code
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.sp_standardClassifierConfig )( p_classifierIID _LONG, p_classifierCode _STRING, p_classifierType _INT)
_IMPLEMENT_AS
    vClassifierIID _LONG; -- corresponding to the id
begin
    -- check vClassifierIID existence
    begin
        select classifierIID into vClassifierIID from pxp.classifierConfig
        where classifierCode = p_classifierCode;
    exception
        when no_data_found then vClassifierIID := null;
    end;
    -- lazy initialization of config tables
    if ( vClassifierIID is null ) then
        insert into pxp.classifierConfig(classifierIID, classifierCode, classifierType)
        values (p_classifierIID, p_classifierCode, p_classifierType);
    else 
      if (p_classifierIID != vClassifierIID) then
      _RAISE( 'Mismatch classifier IID: %', _TO_VARCHAR(p_classifierIID));  
      end if;      
        
    end if;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--   synchronization of TagValues by Code
/*----------------------------------------------------------------------------*/
_FUNCTION( pxp.fn_tagValueConfig)( p_tagValueCode _STRING, p_propertyIID _LONG)
_RETURN _LONG
_IMPLEMENT_AS
    vPropertyIID        _LONG; 
begin
    -- check vTagValueIID existence
    begin
        select propertyIID into vPropertyIID from pxp.tagValueConfig 
        where tagValueCode = p_tagValueCode;
    exception
        when no_data_found then vPropertyIID := null;
    end;
    -- lazy initialization of config tables
    if ( vPropertyIID is null ) then
        insert into pxp.tagValueConfig( tagValueCode, propertyIID) 
        values ( p_tagValueCode, p_propertyIID);
        return p_propertyIID;
    end if;
    return vPropertyIID;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--  synchronization of task by Code
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.fn_taskConfig)( p_taskCode _STRING, p_taskType _INT)
_IMPLEMENT_AS
    vTaskCode        _VARCHAR;
begin
    -- check vTaskCode existence
    begin
        select taskCode into vTaskCode from pxp.taskConfig 
        where taskCode = p_taskCode;
    exception
        when no_data_found then vTaskCode := null;
    end;
    -- lazy initialization of config tables
    if ( vTaskCode is null ) then
        insert into pxp.taskConfig(taskCode, taskType) 
        values (p_taskCode, p_taskType);
    end if;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--  Checking a view exists
/*----------------------------------------------------------------------------*/
_FUNCTION(pxp.fn_checkDynamicView) ( p_viewName _STRING)
_RETURN _INT
_IMPLEMENT_AS
    vViewName      _VARCHAR;
    vOraViewName      _VARCHAR;
begin
   begin -- check view existence
#ifdef PGSQL11 
        select to_regclass(p_viewName) into vViewName;
#elif ORACLE12C
        vOraViewName := replace( upper(p_viewName), 'PXP.'); /* remove PXP. from the prefix */
        select view_name into vViewName from all_views where view_name = vOraViewName;
#endif
    exception
        when no_data_found then vViewName := null;
    end;
    if ( vViewName is not null ) then
        return 1;
    end if;
    return 0;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--  Checking a temporary table exists
/*----------------------------------------------------------------------------*/
_FUNCTION(pxp.fn_checkTemporaryTable) ( p_tableName _STRING)
_RETURN _INT
_IMPLEMENT_AS
    vTableName      _VARCHAR;
begin
   begin -- check table existence
#ifdef PGSQL11 
        select to_regclass(p_tableName) into vTableName;
#elif ORACLE12C
        select table_name into vTableName from all_tables where table_name = p_tableName;
#endif
    exception
        when no_data_found then vTableName := null;
    end;
    if ( vTableName is not null ) then
        return 1;
    end if;
    return 0;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--  Creating dynamically a views (e.g. to navigate across properties)
/*----------------------------------------------------------------------------*/
_PROCEDURE(pxp.sp_createDynamicView) ( p_viewName _STRING, p_selectStatement _STRING)
_IMPLEMENT_AS
    vTest                   _INT;
    vCreateViewStatement    _VARCHAR;
begin
    /* double check view existence (should have been previously checked from the JAVA side) */
    vTest := pxp.fn_checkDynamicView( p_viewName);
    if ( vTest = 0 ) then
        vCreateViewStatement := 'create view ' || p_viewName || ' as ' || p_selectStatement;
#ifdef PGSQL11
        execute vCreateViewStatement;
#elif ORACLE12C
        execute immediate ( vCreateViewStatement);
#endif        
    end if;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--  Create taxonomy
/*----------------------------------------------------------------------------*/

_FUNCTION(pxp.fn_createTaxonomyClassifier) ( p_classifierCode _STRING, p_classifierType _INT, p_parentIIDs _IIDARRAY)
_RETURN _IID
_IMPLEMENT_AS
    vClassifierIID _LONG; -- corresponding to the id

    begin
        -- check vClassifierIID existence
        begin
            select classifierIID into vClassifierIID from pxp.classifierConfig
            where classifierCode = p_classifierCode;
        exception
            when no_data_found then vClassifierIID := null;
        end;
        -- lazy initialization of config tables
        if ( vClassifierIID is null ) then
            vClassifierIID := _NEXTVAL( pxp.seqIID);

            insert into pxp.classifierConfig(classifierIID, classifierCode, classifierType, hierarchyIIDs)
            values (vClassifierIID, p_classifierCode, p_classifierType, array_append(p_parentIIDs, vClassifierIID));
        end if;
        return vClassifierIID;
    end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--  Evaluates the relates operator
/*----------------------------------------------------------------------------*/
_FUNCTION(pxp.relates) ( pLocaleID _STRING, pText _STRING, pLexeme _STRING)
_RETURN _INT
_IMPLEMENT_AS
    vID         _VARCHAR;
    vLanguage   _VARCHAR;
    vFound      _BOOLEAN;
begin
#ifdef PGSQL11
	vID := left(pLocaleID, 2); --extract the language code from the localeid.
#elif ORACLE12C
    select substr(pLocaleID, 1, 2) into vID from dual;
#endif
    begin
	    select language into vLanguage from pxp.localeIDLanguage where id = vID;
	    exception
            when no_data_found then vLanguage := 'english'; --default to english language
    end;
#ifdef PGSQL11
    vFound := (select to_tsvector(vLanguage::regconfig, pLexeme ) @@ to_tsquery(pText));
    if (vFound = true) then
        return 1;
    else
        return 0;
    end if;
    return 0;
#elif ORACLE12C
        -- TODO, certainly completely differently than with PGSQL
#endif        
end
_IMPLEMENT_END;

#ifdef PGSQL11
-- end of PGSQL Infrastructure functions
#elif ORACLE12C
-- end of ORACLE Infrastructure functions
#endif


/*----------------------------------------------------------------------------*/
--  language config creation
/*----------------------------------------------------------------------------*/
_PROCEDURE(pxp.sp_languageConfig) ( p_languagecode _STRING, p_parentcode _STRING)
_IMPLEMENT_AS
    vLanguageIID _LONG; -- corresponding to the id
    vParentLanguageIID _LONG DEFAULT -1;
    p_parentiids _IIDARRAY;
begin
   	vLanguageIID := _NEXTVAL(pxp.seqIID);
   	if (p_parentcode != '-1') then
 		select parentiids, languageiid  into p_parentiids, vParentLanguageIID from pxp.languageConfig
            where languagecode = p_parentcode;
		
	end if;
   
   	insert into pxp.languageConfig(languageiid, languagecode, parentiids)
   	values (vLanguageIID, p_languagecode, array_append(p_parentiids, vParentLanguageIID));
end
_IMPLEMENT_END;

