-- PROCEDURE: staging.sp_migrateallentitiesfromestordbms()

-- DROP PROCEDURE staging.sp_migrateallentitiesfromestordbms();

CREATE OR REPLACE PROCEDURE staging.sp_migrateallentitiesfromestordbms()
LANGUAGE 'plpgsql'

AS $BODY$
declare
begin
	
	raise notice 'Start: %', clock_timestamp();
	
	raise notice 'Start Insert into staging.baseentityid: %', clock_timestamp();
	PERFORM nextval('pxp.uniqueiid');

	-- Mapping for baseentityid	
	INSERT INTO staging.baseentityid(id, baseentityid)
	Select DISTINCT entity1.id,
	CASE 
	WHEN entity1.basetype = 'com.cs.runtime.interactor.entity.AssetInstance' 
	THEN 'ASS'|| nextval('pxp.uniqueiid')
	WHEN 
	entity1.basetype = 'com.cs.runtime.interactor.entity.textassetinstance.TextAssetInstance' 
	THEN 'TXT'||nextval('pxp.uniqueiid')
	WHEN 
	entity1.basetype = 'com.cs.runtime.interactor.entity.MarketInstance' 
	THEN 'TGT'|| nextval('pxp.uniqueiid')
	WHEN entity1.basetype = 'com.cs.runtime.interactor.entity.ArticleInstance' 
	THEN 'ART'|| nextval('pxp.uniqueiid')
	WHEN 
	entity1.basetype = 'com.cs.runtime.interactor.entity.virtualcataloginstance.VirtualCatalogInstance' 
	THEN 'VCT'||nextval('pxp.uniqueiid')
	WHEN entity1.basetype = 'com.cs.runtime.interactor.entity.supplierinstance.SupplierInstance' 
	THEN 'SUP'||nextval('pxp.uniqueiid')
	END
	from staging.baseentity entity1 
	Where entity1.id NOT IN(Select id from staging.baseentityid);
	raise notice 'End Insert into staging.baseentityid: %', clock_timestamp();
	
	raise notice 'Start Insert into pxp.contextualobject: %', clock_timestamp();
	-- contextualobject table
	INSERT INTO pxp.contextualobject(
		contextualobjectiid, contextcode, cxttimerange, cxttags)
	Select contextiid, contextid, int8range(starttime::int, endtime::int), tagvalues::hstore
	FROM (
		Select ctx.contextiid, 
		-- TODO: contextcode - contextcode should be present in the contextconfig table
		helper_config.code contextid,
		-- cxttimerange : from
		(
			Case  
			WHEN timerange ->'timeRange'->>'from' IS NULL THEN '0'
			ELSE timerange ->'timeRange'->>'from' 
			END
		) starttime,
		-- cxttimerange : to
		(
			Case 
			WHEN timerange ->'timeRange'->>'to' IS NULL THEN '1'
			ELSE timerange ->'timeRange'->>'to' 
			END
		) endtime,
		(
			Select string_agg(hstore(helper_config.code,tagrecord.value):: text, ',') 
			From (Select trec.id, (each(trec."tagvalues")).* From staging.tagrecord trec) tagrecord
			JOIN staging.helper_config helper_config ON tagrecord.key = helper_config.cid
			Where tagrecord.id = Any(ctx.taginstanceids)
		)tagvalues
		from staging.context ctx
		LEFT JOIN staging.helper_config helper_config ON helper_config.cid = ctx.contextid
		Where ctx.contextiid NOT IN (Select contextualobjectiid from pxp.contextualobject)
		AND helper_config.code IS NOT NULL
	) tt;
	raise notice 'End Insert into pxp.contextualobject: %', clock_timestamp();

	raise notice 'Start Insert into pxp.baseentity: %', clock_timestamp();
	-- Dump All type of base entities	
	INSERT INTO pxp.baseentity(
		baseentityiid, baseentityid, catalogcode,
		organizationcode, classifieriid, basetype, childlevel,
		parentiid, embeddedtype, topparentiid, sourcecatalogcode, baselocaleid, hashcode, 
		originbaseentityiid, defaultimageiid, entityextension, contextualobjectiid, createuseriid, 
		lastmodifieduseriid, createtime, lastmodifiedtime, isclone, endpointcode)	
	Select DISTINCT
		baseentityiid, baseentityid, physicalcatalogid, organizationcode, classifieriid,
		basetype, COALESCE(childlevel,1), null::bigint parentiid, embeddedtype, null::bigint topparentiid, sourcecatalogcode, 
		creationlanguage, hash, originbaseentityiid::bigint, defaultassetinstanceid::bigint, 
		assetinformation::jsonb #- '{variantOf}' #- '{lastModifiedBy}',
		contextualobjectiid, createdby, lastmodifiedby, createdon, lastmodified, isclone, endpointcode
	from (
	Select 
		staging_entity.baseentityiid,
		-- baseentityid
		baseentityid.baseentityid baseentityid,
		physicalcatalogid,
		case when helper_configoc.code = '-1' then 'stdo' else helper_configoc.code end organizationcode,
		-- TODO: classifieriid : should be present in the classifier config
		(Select "classifierIId" from staging.helper_config stag
		 where stag.cid= s.type and stag."isNature" = true
		 LIMIT 1) classifieriid,
		 -- basetype
		 helper_basetype.basetypevsenumvalue basetype,
		-- childlevel
		( With RECURSIVE temp as(
			Select id, parentid, 1 AS level from staging.baseentity
			where id = staging_entity.id
			UNION 
			Select entity2.id, entity2.parentid, temp.level+1 from staging.baseentity entity2
			JOIN temp ON temp.parentid = entity2.id)			
			Select level from temp where parentid is NULL) childlevel,
		 -- embeddedtype
		 Case WHEN staging_entity.isembedded = true THEN 1 END embeddedtype ,
		 null sourcecatalogcode, 
		 -- TODO: sourcecatalogcode
		 -- baselocaleid
		 staging_entity.creationLanguage, 		 
		 -- hashcode
		 staging_entity.assetinformation->>'hash' hash,		 
		 -- originbaseentityiid
		null originbaseentityiid,
		 -- defaultimageiid
		 -- entityextension
		 null defaultassetinstanceid,
		staging_entity.assetinformation::json ->> 'assetInformation' assetinformation, 
		-- contextualobjectiid
		Case WHEN staging_entity.contextiid = 0 THEN NULL ELSE staging_entity.contextiid END contextualobjectiid,
		-- createuseriid 
		helper_userconfigcb.useriid createdby, 
		-- lastmodifieduseriid
		helper_userconfigmb.useriid lastmodifiedby,
		 -- createtime
		staging_entity.createdOn,		
		-- lastmodifiedtime
		staging_entity.lastModified,
		-- isclone
		case when staging_entity.branchof != '-1' then true else false end isclone,
		-- endpointcode
		CASE WHEN helper_endpointconfig.cid IS NULL THEN '-1' ELSE helper_endpointconfig.code END endpointcode
	from staging.baseentity staging_entity 
		LEFT JOIN staging.baseentityid baseentityid USING(id) 
		LEFT JOIN staging.helper_basetype helper_basetype USING(basetype)
		LEFT JOIN staging.helper_userconfig helper_userconfigcb ON helper_userconfigcb.userid = staging_entity.createdBy
		LEFT JOIN staging.helper_userconfig helper_userconfigmb ON helper_userconfigmb.userid = staging_entity.lastmodifiedby
		LEFT JOIN staging.helper_config helper_configoc ON helper_configoc.cid = staging_entity.organizationid
		LEFT JOIN staging.helper_endpointconfig helper_endpointconfig ON helper_endpointconfig.cid = staging_entity.endpointId
		,unnest(types) s(type)) baseentity
	Where baseentity.baseentityid NOT IN(Select baseentityid from pxp.baseentity)
	AND baseentity.classifieriid IS NOT NULL
	AND baseentity.organizationcode IS NOT NULL
	AND (baseentity.endpointcode IS NULL OR baseentity.endpointcode != '-1');
	raise notice 'End Insert into pxp.baseentity: %', clock_timestamp();
	
	raise notice 'Start Update for defaultimageiid: %', clock_timestamp();
	-- Update defaultimageiid of pxp.baseentity
	Update pxp.baseentity entity1
	Set defaultimageiid = entity2.defaultassetinstanceid
	From(
		Select staging_entity.baseentityiid, 
		(
			Select baseentityiid from staging.baseentity innertemp
			where innertemp.id = staging_entity.defaultassetinstanceid LIMIT 1
		)
		defaultassetinstanceid from staging.baseentity staging_entity
	) entity2
	Where entity1.baseentityiid = entity2.baseentityiid and entity2.defaultassetinstanceid IS NOT NULL;
	raise notice 'End Update for defaultimageiid: %', clock_timestamp();
	
	raise notice 'Start Update for parentiid: %', clock_timestamp();
	-- Update parentiid and topparentiids
	Update pxp.baseentity entitytoupdate
	Set parentiid = handleparent.parentiid 
	from
	(Select staging_entity.baseentityiid,
	-- parentiid
			 (Select parentid.baseentityiid from (Select baseentityiid from staging.baseentity innerstaging_entity
				 where innerstaging_entity.id = staging_entity.parentid
				 AND innerstaging_entity.baseentityiid IN (Select baseentityiid From pxp.baseentity) LIMIT 1) parentid
			 ) parentiid
	from staging.baseentity staging_entity 
	Where staging_entity.parentid IS NOT NULL
	) handleparent
	where entitytoupdate.baseentityiid = handleparent.baseentityiid;
	raise notice 'End Update for parentiid: %', clock_timestamp();
	
	raise notice 'Start Update for topparentiids: %', clock_timestamp();
	-- Update parentiid and topparentiids
	Update pxp.baseentity entitytoupdate
	Set topparentiid = handleparent.topparentiid
	from
	(Select staging_entity.baseentityiid,
	-- topparentiid
			  (Select topparentid.baseentityiid from(Select baseentityiid from staging.baseentity innerstaging_entity
				where innerstaging_entity.id = staging_entity.klassinstanceid 
				AND innerstaging_entity.baseentityiid IN (Select baseentityiid From pxp.baseentity) LIMIT 1) topparentid
			  ) topparentiid
	from staging.baseentity staging_entity
	) handleparent
	where entitytoupdate.baseentityiid = handleparent.baseentityiid;
	raise notice 'End Update for topparentiids: %', clock_timestamp();
	
	raise notice 'Start Update for originbaseentityiid: %', clock_timestamp();
	-- Update query to add originbaseentityiid
	Update pxp.baseentity entitytochange
	Set originbaseentityiid = entity.originalinstanceId
	from
	(Select baseentityiid,
	 (
		 Select baseentityiid from staging.baseentity temp
		 where temp.id = staging_entity.originalinstanceId
	 ) originalinstanceId
	 from staging.baseentity staging_entity) entity
	where entitytochange.baseentityiid = entity.baseentityiid;
	raise notice 'End Update for originbaseentityiid: %', clock_timestamp();
	
	raise notice 'Start Insert into pxp.baseentitylocaleidlink: %', clock_timestamp();
	-- Map baseentityiids with localid	
	INSERT INTO pxp.baseentitylocaleidlink(baseentityiid, localeid)
	SELECT staging_entity.baseentityiid, s.token
	FROM staging.baseentity staging_entity, unnest(staging_entity.languagecodes) s(token);
	raise notice 'End Insert into pxp.baseentitylocaleidlink: %', clock_timestamp();
	
	raise notice 'Start Insert into pxp.baseentityclassifierlink: %', clock_timestamp();
	-- Map baseentityiids with classifieriids
	INSERT INTO pxp.baseentityclassifierlink(baseentityiid, otherclassifieriid)
	With temp_table AS(
	Select baseentityiid, s.token classifierid  from staging.baseentity, unnest(types) s(token)
	UNION
	Select baseentityiid, s.token classifierid from staging.baseentity, unnest(selectedtaxonomyids) s(token))
	Select * From (
		Select DISTINCT baseentityiid, 
		-- classifierid
		(Select helper."classifierIId" from staging.helper_config helper
		 Where classifierid = helper.cid and helper."isNature" = false ) classifierid 
		from temp_table 
		JOIN pxp.baseentity USING(baseentityiid)
	)temp_baseentityclassifierlink
	Where temp_baseentityclassifierlink.classifierid IS NOT NULL;
	raise notice 'End Insert into pxp.baseentityclassifierlink: %', clock_timestamp();
	
	raise notice 'End: %', clock_timestamp();
	
	DROP TABLE IF EXISTS staging.temp_baseentity;
	Create TABLE staging.temp_baseentity AS (
	Select pxpbaseentity.baseentityiid, stagingbaseentity.id  from pxp.baseentity pxpbaseentity 
	JOIN staging.baseentity stagingbaseentity USING(baseentityiid)
	);
	
	CREATE INDEX idx_temp_baseentity_id
	ON staging.temp_baseentity(id);

end
$BODY$;
