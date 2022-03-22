/**
 * SQL and PL/SQL snippets (should not be loaded in PXP database)
 * Author:  vallee
 * Created: Mar 23, 2019
 */
#include "./include/sql-defs.sql"

/* To return the full config object information */

_FUNCTION( pxp.fn_catalogConfig )( p_catalogCode _STRING, p_catalogType _INT)
_RETURN record -- specific to POSTGRES
_IMPLEMENT_AS
    vCatalog       _RECORD( pxp.CatalogConfig);
begin
    -- check vCatalogIID existence
    begin
        select * into vCatalog from pxp.CatalogConfig 
        where catalogCode = p_catalogCode;
    exception
        when no_data_found then vCatalog := null;
    end;
    -- lazy initialization of config tables
    if ( vCatalog is null ) then
        insert into pxp.CatalogConfig( catalogCode, catalogType) 
                values ( p_catalogCode, p_catalogType);
		vCatalog.catalogCode = p_catalogCode;
		vCatalog.catalogType = p_catalogType;
    end if;
	return vCatalog;
end
_IMPLEMENT_END;

/*
    To call a procedure from PGAdmin (use transtyping if required):
    $> call pxp.sp_timeoutsession(1::smallint);

    To call a procedure from SQL Developer
    $>  begin
            pxp.SP_TIMEOUTSESSION(1);
        end;
        /
*/

/* 
    Example of procedure instead of a function 
    This example also shows how to query across a result set (record)
    example: automatically closing sessions older than a given perion in hours
*/
_PROCEDURE( pxp.sp_timeoutsession )(pHours _SHORT)
_IMPLEMENT_AS
    vSessionData    _RECORD(pxp.UserSession); -- only for select * from pxp.UserSession
    vNow            _TIMESTAMP;
begin
    for vSessionData in (select * from pxp.UserSession where 
        logouttime is null and logintime < (_SYSTIMESTAMP - _INTERVAL( pHours, hour)) )
    loop
        update pxp.UserSession 
            set logouttime = _SYSTIMESTAMP, logouttype = 3, remarks = 'sp_timeoutsession'
            where sessionID = vSessionData.sessionID;
    end loop;
end
_IMPLEMENT_END;

/*
    Example that demonstrates how to have an array as parameter and to loop across that array

    The simplest way to test that kind of function that returns an array in PGADMIN4 consists in converting it into json
    ex: 
        select json_agg(pxp.fn_getValueRecord( 200001, '#pxp#200', ARRAY ['en_CA','en_US']));
        -- when the fields are explicitly declared in the select query of the function, then it is possible to directly get
        select pxp.fn_getValueRecord( 200001, '#pxp#200', ARRAY ['en_CA','en_US']);

    To test in SQL Developer is more cumbersome
    Set serveroutput on 
    declare 
            v   pxp.ValueRecord%rowtype;
            lc  pxp.varcharArray := pxp.varcharArray();
    begin
            lc.extend(2);
            lc(1) := 'en_CA';
            lc(2) := 'en_US';
            v := pxp.fn_getValueRecord( 200001, '#pxp#200', lc);
            dbms_output.put_line( 'returned value = ' || v.value);
    end;
    /    
*/
_FUNCTION( pxp.fn_getValueRecord )( p_baseEntityID varchar, p_propertyID varchar, p_localeSchema _VARARRAY)
_RETURN _RECORD( pxp.ValueRecord)
_IMPLEMENT_AS
    vLocaleID   _VARCHAR;
    vLocalIdx   _INT;
    vRecord     _RECORD( pxp.ValueRecord);
begin
    for vLocalIdx IN 1 .. _COUNT( p_localeSchema) loop
        vLocaleID := _ARRAY( p_localeSchema, vLocalIdx);

    end loop;
    return _NULL(vRecord);
end
_IMPLEMENT_END;

/**
 Example of a dynamic view
*/
 SELECT DISTINCT b.entityiid,
    b.contextualobjectiid,
    b.propertyiid,
    COALESCE(v0.valueiid, v1.valueiid, v2.valueiid, v3.valueiid, vf.valueiid) AS valueiid,
    COALESCE(v0.value, v1.value, v2.value, v3.value, vf.value) AS value,
    COALESCE(v0.localeid, v1.localeid, v2.localeid, v3.localeid, NULL::varchar) AS localeid
   FROM pxp.allvaluerecord b
     LEFT JOIN pxp.allvaluerecord v0 ON v0.entityiid = b.entityiid AND v0.propertyiid = b.propertyiid AND v0.localeid = 'fr_FR'
     LEFT JOIN pxp.allvaluerecord v1 ON v1.entityiid = b.entityiid AND v1.propertyiid = b.propertyiid AND v1.localeid = 'fr_CA'
     LEFT JOIN pxp.allvaluerecord v2 ON v2.entityiid = b.entityiid AND v2.propertyiid = b.propertyiid AND v2.localeid = 'en_CA'
     LEFT JOIN pxp.allvaluerecord v3 ON v3.entityiid = b.entityiid AND v3.propertyiid = b.propertyiid AND v3.localeid = 'en_US'
     LEFT JOIN pxp.allvaluerecord vf ON vf.entityiid = b.entityiid AND vf.propertyiid = b.propertyiid AND vf.localeid IS NULL;


_EOF