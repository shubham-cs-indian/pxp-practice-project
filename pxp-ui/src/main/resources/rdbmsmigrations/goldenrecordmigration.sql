/*DDL*/

ALTER TABLE pxp.baseentity add column if not exists isMerged boolean DEFAULT false not null;
ALTER TABLE pxp.baseentity add column if not exists sourceorganizationcode varchar DEFAULT 'stdo' not null;

drop table if exists pxp.goldenrecordbucket cascade; 
create table pxp.goldenrecordbucket (
 bucketid bigint not null,
 organisationCode varchar not null,
 issearchable boolean not null,
 catalogCode varchar not null,
 endpointCode varchar not null,
 ruleId varchar not null,
 createdTime bigint not null,
 lastModifiedTime bigint not null,
 primary key (bucketid)
);

drop table if exists pxp.goldenrecordbucketattributelink cascade;
create table pxp.goldenrecordbucketattributelink (
 bucketid bigint not null,
 attributeid varchar not null,
 value varchar not null
);

alter table pxp.goldenrecordbucketattributelink add constraint goldenrecordbucketattributelink_bucketid_fk foreign key(bucketid) references pxp.goldenrecordbucket (bucketid);

drop table if exists pxp.goldenrecordbuckettaglink cascade;
create table pxp.goldenrecordbuckettaglink (
 bucketid bigint not null,
 tagid varchar not null,
 value hstore default null
);

alter table pxp.goldenrecordbuckettaglink add constraint goldenrecordbuckettaglink_bucketid_fk foreign key(bucketid) references pxp.goldenrecordbucket (bucketid);
drop table if exists pxp.goldenrecordbucketbaseentitylink cascade;
create table pxp.goldenrecordbucketbaseentitylink (
 bucketid bigint not null,
 baseentityIID bigint not null
);

alter table pxp.goldenrecordbucketbaseentitylink add constraint goldenrecordbucketbaseentitylink_bucketid_fk foreign key(bucketid) references pxp.goldenrecordbucket (bucketid);

drop sequence if exists pxp.seqBucketID cascade; create sequence pxp.seqBucketID start 100;

/*DML*/
UPDATE pxp.baseentity set isMerged = false;
UPDATE pxp.baseentity set sourceorganizationcode = organizationCode;

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
	p_endpointcode character varying)
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
   p_createUserIID, p_createUserIID, p_createTime, p_createTime, p_isClone, p_endpointCode, p_organizationCode);
end
$BODY$;

create or replace procedure pxp.sp_deleteGoldenRecordBuckets (v_bucketIds bigint[])
as $body$ declare
begin
     delete from pxp.goldenrecordbucketattributelink where bucketid = any(v_bucketIds);
    delete from pxp.goldenrecordbuckettaglink where bucketid = any(v_bucketIds);
    delete from pxp.goldenrecordbucketbaseentitylink where bucketid = any(v_bucketIds);
    delete from pxp.goldenrecordbucket where bucketid = any(v_bucketIds);
end
$body$ language plpgsql;

CREATE OR REPLACE VIEW pxp.baseentitytrackingfullcontent
 AS
 SELECT e.baseentityiid,
    e.baseentityid,
    e.catalogcode,
    e.organizationcode,
    e.classifieriid,
    e.basetype,
    e.childlevel,
    e.parentiid,
    e.embeddedtype,
    e.topparentiid,
    e.sourcecatalogcode,
    e.baselocaleid,
    e.hashcode,
    e.originbaseentityiid,
    e.defaultimageiid,
    e.entityextension,
    e.contextualobjectiid,
    e.createuseriid,
    e.lastmodifieduseriid,
    e.createtime,
    e.lastmodifiedtime,
    e.isexpired,
    e.isclone,
    e.endpointcode,
    e.isduplicate,
    u1.username AS createusername,
    u2.username AS modifyusername,
    oc.classifieriids,
    coo.contextcode,
    lower(coo.cxttimerange) AS cxtstarttime,
    upper(coo.cxttimerange) AS cxtendtime,
    coo.cxttags,
    e.ismerged
   FROM pxp.baseentity e
     JOIN pxp.userconfig u1 ON u1.useriid = e.createuseriid
     JOIN pxp.userconfig u2 ON u2.useriid = e.lastmodifieduseriid
     LEFT JOIN pxp.baseentityaggregatedotherclassifiers oc ON oc.baseentityiid = e.baseentityiid
     LEFT JOIN pxp.contextualobject coo ON coo.contextualobjectiid = e.contextualobjectiid;

ALTER TABLE pxp.baseentitytrackingfullcontent
    OWNER TO pxp;
    
/* baseentitytrackingfullcontentwithbasename */
CREATE OR REPLACE VIEW pxp.baseentitytrackingfullcontentwithbasename
 AS
 SELECT e.baseentityiid,
    e.baseentityid,
    e.catalogcode,
    e.organizationcode,
    e.classifieriid,
    e.basetype,
    e.childlevel,
    e.parentiid,
    e.embeddedtype,
    e.topparentiid,
    e.sourcecatalogcode,
    e.baselocaleid,
    e.hashcode,
    e.originbaseentityiid,
    e.defaultimageiid,
    e.entityextension,
    e.contextualobjectiid,
    e.createuseriid,
    e.lastmodifieduseriid,
    e.createtime,
    e.lastmodifiedtime,
    e.isexpired,
    e.isclone,
    e.endpointcode,
    e.isduplicate,
    e.createusername,
    e.modifyusername,
    e.classifieriids,
    e.contextcode,
    e.cxtstarttime,
    e.cxtendtime,
    e.cxttags,
    COALESCE(rv.value, 'extensionentity'::character varying) AS baseentitybasename,
    e.ismerged
   FROM pxp.baseentitytrackingfullcontent e
     LEFT JOIN pxp.valuerecord rv ON rv.entityiid = e.baseentityiid AND rv.localeid::text = e.baselocaleid::text AND rv.propertyiid = (200 + 0);

ALTER TABLE pxp.baseentitytrackingfullcontentwithbasename
    OWNER TO pxp;
    
/* baseentitywithcontextualdata */
CREATE OR REPLACE VIEW pxp.baseentitywithcontextualdata
 AS
 SELECT e.baseentityiid,
    e.baseentityid,
    e.catalogcode,
    e.organizationcode,
    e.classifieriid,
    e.basetype,
    e.childlevel,
    e.parentiid,
    e.embeddedtype,
    e.topparentiid,
    e.sourcecatalogcode,
    e.baselocaleid,
    e.hashcode,
    e.originbaseentityiid,
    e.defaultimageiid,
    e.entityextension,
    e.contextualobjectiid,
    e.createuseriid,
    e.lastmodifieduseriid,
    e.createtime,
    e.lastmodifiedtime,
    e.isexpired,
    e.isclone,
    e.endpointcode,
    e.isduplicate,
    lower(coo.cxttimerange) AS cxtstarttime,
    upper(coo.cxttimerange) AS cxtendtime,
    coo.contextcode,
    coo.cxttags,
    coo.cxtlinkedbaseentitiyiids,
    e.ismerged
   FROM pxp.baseentity e
     JOIN pxp.contextualobjectaggregatedlinkedentities coo ON coo.contextualobjectiid = e.contextualobjectiid;

ALTER TABLE pxp.baseentitywithcontextualdata
    OWNER TO pxp;
