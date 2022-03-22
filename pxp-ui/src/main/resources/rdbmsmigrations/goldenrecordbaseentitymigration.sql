/*Procedure*/
DROP procedure IF EXISTS pxp.sp_createbaseentity;
CREATE OR REPLACE PROCEDURE pxp.sp_createbaseentity(
    p_baseentityiid bigint,
    p_baseentityid character varying,
    p_contextcode character varying,
    p_catalogcode character varying,
    p_organizationcode character varying,
    p_classifieriid bigint,
    p_basetype integer,
    p_childlevel integer,
    p_sourcecatcode character varying,
    p_localeid character varying,
    p_parentiid bigint,
    p_topparentiid bigint,
    p_defaultimageiid bigint,
    p_sourcebeiid bigint,
    p_hashcode character varying,
    p_extension json,
    p_createtime bigint,
    p_createuseriid bigint,
    p_isclone boolean,
    p_endpointcode character varying,
    p_sourceorganizationcode character varying)
LANGUAGE 'plpgsql'

 

AS $BODY$ declare
    vContextualObjectIID bigint;
 vTopParentIID bigint;
 vChildLevel integer;
begin
 vTopParentIID := p_topParentIID;
 vChildLevel := p_childLevel;
 if ( p_parentIID is not null ) then
  begin
   select a.topParentIID, a.childLevel+1 into vTopParentIID, vChildLevel
   from pxp.BaseEntity a
   where a.baseentityIID = p_parentIID;
   if ( vTopParentIID is null ) then
    vTopParentIID := p_topParentIID;
   end if;
  exception
   when no_data_found then raise 'Unknown parent IID: %', p_parentIID::varchar;
  end;
 end if;
    if ( p_contextCode is not null ) then
        insert into pxp.contextualObject ( contextualObjectIID, contextCode )
        values ( p_baseentityiid, p_contextCode );
        vContextualObjectIID := p_baseentityiid;
    else
        vContextualObjectIID := null;
    end if;
    insert into pxp.baseentity
        ( baseentityiid, baseentityid, catalogcode, organizationCode, contextualObjectIID, classifieriid,
            basetype, childLevel, sourcecatalogcode, baselocaleid, parentIID, topParentIID, hashcode, defaultimageiid, originBaseEntityIID, entityExtension,
            createUserIID, lastModifiedUserIID, createTime, lastModifiedTime, isclone, endpointCode, sourceOrganizationCode)
    values( p_baseentityiid, p_baseentityid, p_catalogCode, p_organizationCode, vContextualObjectIID, p_classifieriid,
            p_basetype, vChildLevel, p_sourceCatCode, p_localeID, p_parentIID, vTopParentIID, p_hashcode, p_defaultimageiid, p_sourceBeIID, p_extension,
   p_createUserIID, p_createUserIID, p_createTime, p_createTime, p_isClone, p_endpointCode, p_sourceOrganizationCode);
end
$BODY$;

ALTER procedure pxp.sp_createbaseentity(bigint, character varying, character varying, character varying, character varying, 
	 bigint, integer, integer, character varying, character varying, bigint, bigint, bigint, bigint, character varying, json,
	 bigint, bigint, boolean, character varying, character varying)
    OWNER TO pxp;

    
/*Function*/
DROP FUNCTION pxp.fn_createbaseentity;

CREATE OR REPLACE FUNCTION pxp.fn_createbaseentity(
	p_baseentityiid bigint,
	p_baseentityid character varying,
	p_contextcode character varying,
	p_catalogcode character varying,
	p_organizationcode character varying,
	p_classifieriid bigint,
	p_basetype integer,
	p_childlevel integer,
	p_sourcecatcode character varying,
	p_localeid character varying,
	p_parentiid bigint,
	p_topparentiid bigint,
	p_defaultimageiid bigint,
	p_sourcebeiid bigint,
	p_hashcode character varying,
	p_extension json,
	p_createtime bigint,
	p_createuseriid bigint,
	p_isclone boolean,
	p_endpointcode character varying,
	p_sourceorganizationcode character varying)
    RETURNS bigint
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$ declare
begin
 if(p_baseentityiid = 0 ) then
     p_baseentityiid := nextval('pxp.seqIID');
    end if;
 call pxp.sp_createBaseEntity( p_baseentityiid, p_baseentityid, p_contextCode, p_catalogCode, p_organizationCode,
                p_classifieriid, p_basetype, p_childLevel, p_sourceCatCode,
                p_localeID, p_parentIID, p_topParentIID, p_defaultimageiid, p_sourceBeIID, p_hashcode,
    p_extension, p_createTime, p_createUserIID, p_isClone, p_endpointCode, p_sourceorganizationcode);
    return p_baseentityiid;
end
$BODY$;

ALTER FUNCTION pxp.fn_createbaseentity(bigint, character varying, character varying, character varying, character varying, bigint, integer, integer, character varying, character varying, bigint, bigint, bigint, bigint, character varying, json, bigint, bigint, boolean, character varying, character varying)
    OWNER TO pxp;
    