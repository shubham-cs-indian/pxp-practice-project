-- PROCEDURE: staging.sp_migratealltagrecordsfromestordbms_without_loop()

-- DROP PROCEDURE staging.sp_migratealltagrecordsfromestordbms_without_loop();

CREATE OR REPLACE PROCEDURE staging.sp_migratealltagrecordsfromestordbms_without_loop()
LANGUAGE 'plpgsql'

AS $BODY$
declare
begin
	raise notice 'Start %', clock_timestamp();
	
CREATE TEMP TABLE temp_staging_tagrec AS ( Select tagiid, tagid, helper_config.code tagkey, temp_tagvalues.tagvalue,
	-- Instanceid
	CASE WHEN variantinstanceid = '' THEN klassinstanceid ELSE variantinstanceid END instanceid,
	tagvalues, notification From staging.tagrecord, unnest(avals(tagvalues)) temp_tagvalues(tagvalue),
	unnest(akeys(tagvalues)) temp_tagkeys(tagkey)
	JOIN staging.helper_config helper_config ON temp_tagkeys.tagkey = helper_config.cid
	Where contextinstanceid IS NULL
	GROUP BY tagiid, tagid, helper_config.code, temp_tagvalues.tagvalue, instanceid, tagvalues, notification);

CREATE TEMP TABLE temp_source_column AS (
	Select staging_tag_conflicting.klassinstanceid, staging_tag_conflicting.tagid, MIN(source::text) as source,
	staging_tag_conflicting.couplingtype from staging.tagconflictingvalues staging_tag_conflicting
	LEFT JOIN temp_staging_tagrec staging_tagrec
	ON staging_tag_conflicting.tagid = staging_tagrec.tagid 
	AND staging_tag_conflicting.klassinstanceid = staging_tagrec.instanceid 
	GROUP BY staging_tag_conflicting.klassinstanceid, staging_tag_conflicting.tagid, staging_tag_conflicting.couplingtype);

CREATE INDEX idx_temp_staging_tagrec_tagid
ON temp_staging_tagrec(tagid);

CREATE INDEX idx_temp_staging_tagrec_instanceid
ON temp_staging_tagrec(instanceid);
	
	raise notice 'Start Insert into pxp.tagsrecord: %', clock_timestamp();
-- Tag records 
INSERT INTO pxp.tagsrecord(propertyiid, entityiid, recordstatus, usrtags)
Select * From (
	Select classifieriid, baseentityiid, 
	-- recordstatus
	CASE 
	When usertags IS NULL AND (notification is null OR notification = '{}') AND count = 1 THEN 1
	When count > 1 AND usertags IS NULL THEN 6	
	When (notification is null OR notification = '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 4
	When (notification is not null AND notification != '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 6		
	When count > 1 AND couplingtype = 'tightlyCoupled' AND usertags IS NOT NULL THEN 4					
	WHEN count = 1 AND couplingtype = 'dynamicCoupled' THEN 4
	WHEN count > 1 AND couplingtype = 'dynamicCoupled' AND usertags IS NOT NULL THEN 4
	ELSE 1
	END recordstatus, 
	usertags
	From (
		Select classifieriid, baseentityiid, couplingtype, string_agg(hstore, ',')::hstore usertags, count, notification 
		From ( Select temp_staging_tagrec.notification,
		-- classifierIId
		helper_config."classifierIId" classifieriid,
		-- baseentityiid
		baseentity.baseentityiid,
		-- usertags
		hstore(tagkey, tagvalue):: text,
		-- Couplingtype
		tagconflictingvalues.couplingtype couplingtype,
		-- count
		count(tagconflictingvalues.tagid) count
		From temp_staging_tagrec
		LEFT JOIN staging.helper_config helper_config ON helper_config.cid = temp_staging_tagrec.tagid
		LEFT JOIN staging.temp_baseentity baseentity ON baseentity.id = temp_staging_tagrec.instanceid
		LEFT JOIN staging.tagconflictingvalues tagconflictingvalues ON tagconflictingvalues.tagid = temp_staging_tagrec.tagid 
			AND tagconflictingvalues.klassinstanceid = temp_staging_tagrec.instanceid 
		Where tagvalue = '100'
		AND baseentity.baseentityiid IS NOT NULL
		GROUP BY temp_staging_tagrec.notification, helper_config."classifierIId", baseentity.baseentityiid,
		tagconflictingvalues.couplingtype, temp_staging_tagrec.tagid, temp_staging_tagrec.instanceid, temp_staging_tagrec.tagkey, 
		temp_staging_tagrec.tagvalue
	) tag_record_with_count
	GROUP BY classifieriid, baseentityiid, notification, couplingtype, count
	) temp_tag_record	
) tagrecords
Where recordstatus != 4
AND tagrecords.classifieriid 
NOT IN (
	Select classifieriid From pxp.tagsrecord tagrec
	Where tagrec.entityiid = tagrecords.baseentityiid
);
raise notice 'End Insert into pxp.tagsrecord: %', clock_timestamp();

raise notice 'Start Insert into pxp.conflictingvalues: %', clock_timestamp();
-- Conflicting Values Part 1
INSERT INTO pxp.conflictingvalues(targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid)
Select baseentityiid, classifieriid, contentId, couplingtype, id, 
-- recordstatus
CASE WHEN adaptedValue IS NULL AND recordstatus = 4 THEN 2 ELSE recordstatus END,
couplingtype, 0
From (
	Select classifieriid, temp_tag_record.baseentityiid, 		
	-- couplingtype
	CASE 
	WHEN temp_tag_record.couplingtype = 'dynamicCoupled' THEN 3
	WHEN temp_tag_record.couplingtype = 'tightlyCoupled' THEN 4
	END couplingtype,
	-- contentId 
	baseentityouter.baseentityiid contentId,
	-- couplingsourceiid
	propertyconfig.propertyiid id,
	-- AdaptedValues
	Count(tag_conflict_val.tagid) adaptedvalue,
	-- recordstatus
	CASE 
	When usertags IS NULL AND (notification is null OR notification = '{}') AND count = 1 THEN 1
	When count > 1 AND usertags IS NULL THEN 6	
	When (notification is null OR notification = '{}') AND count = 1 AND temp_tag_record.couplingtype = 'tightlyCoupled' THEN 4
	When (notification is not null AND notification != '{}') AND count = 1 AND temp_tag_record.couplingtype = 'tightlyCoupled' THEN 6		
	When count > 1 AND temp_tag_record.couplingtype = 'tightlyCoupled' AND usertags IS NOT NULL THEN 4					
	WHEN count = 1 AND temp_tag_record.couplingtype = 'dynamicCoupled' THEN 4
	WHEN count > 1 AND temp_tag_record.couplingtype = 'dynamicCoupled' AND usertags IS NOT NULL THEN 4
	ELSE 1
	END recordstatus, 
	usertags
	From ( Select tagid, classifieriid, instanceid, baseentityiid, couplingtype, source,
		string_agg(hstore, ',')::hstore usertags, count, notification 
		From ( Select temp_staging_tagrec.tagid, instanceid, temp_staging_tagrec.notification,
		-- classifierIId
		helper_config."classifierIId" classifieriid,
		-- baseentityiid
		baseentity.baseentityiid,
		-- usertags
		hstore(tagkey, tagvalue):: text,
		-- Couplingtype
		temp_source_column.couplingtype couplingtype,
		-- source
		temp_source_column.source source,
		-- count
		COUNT(tagconflictingvalues.tagid)
		From temp_staging_tagrec
		LEFT JOIN staging.helper_config helper_config ON helper_config.cid = temp_staging_tagrec.tagid
		LEFT JOIN staging.temp_baseentity baseentity ON baseentity.id = temp_staging_tagrec.instanceid
		LEFT JOIN staging.tagconflictingvalues tagconflictingvalues ON tagconflictingvalues.tagid = temp_staging_tagrec.tagid 
			AND tagconflictingvalues.klassinstanceid = temp_staging_tagrec.instanceid 
		LEFT JOIN temp_source_column ON temp_source_column.tagid = temp_staging_tagrec.tagid 
			AND temp_source_column.klassinstanceid = temp_staging_tagrec.instanceid
		Where tagvalue = '100'
		AND baseentity.baseentityiid IS NOT NULL
		GROUP BY temp_staging_tagrec.tagid, instanceid, temp_staging_tagrec.notification, helper_config."classifierIId", 
		baseentity.baseentityiid, temp_staging_tagrec.tagkey, temp_staging_tagrec.tagvalue,
		temp_source_column.couplingtype, temp_source_column.source
	) tag_record_with_count
	GROUP BY tagid, classifieriid, instanceid, baseentityiid, notification, couplingtype, count, source
	) temp_tag_record
	LEFT JOIN staging.temp_baseentity baseentityouter ON baseentityouter.id = temp_tag_record.source::json ->> 'contentId'
	LEFT JOIN pxp.propertyconfig propertyconfig ON propertyconfig.propertycode = temp_tag_record.source::json ->> 'id'
	LEFT JOIN staging.tagconflictingvalues tag_conflict_val ON tag_conflict_val.tagid = temp_tag_record.tagid
	AND tag_conflict_val.klassinstanceid = temp_tag_record.instanceid
	AND tag_conflict_val.tagvalues = temp_tag_record.usertags
	AND temp_tag_record.usertags IS NOT NULL AND temp_tag_record.usertags !=''
	Where temp_tag_record.couplingtype != 'looselyCoupled'
	AND temp_tag_record.source::json ->> 'contentId' is not NULL
	AND baseentityouter.baseentityiid IS NOT NULL
	GROUP BY classifieriid, temp_tag_record.baseentityiid, temp_tag_record.couplingtype, baseentityouter.baseentityiid,
	propertyconfig.propertyiid, temp_tag_record.usertags, temp_tag_record.notification, temp_tag_record.count
) tagrecords
Where recordstatus != 1
AND classifieriid 
NOT IN (
	Select propertyiid From pxp.conflictingvalues 
	Where tagrecords.baseentityiid = targetentityiid 
	AND tagrecords.contentId = sourceentityiid 
	AND tagrecords.id = couplingsourceiid AND localeiid = 0
);
raise notice 'End Insert into pxp.conflictingvalues: %', clock_timestamp();

raise notice 'Start Insert into pxp.conflictingvalues: %', clock_timestamp();
-- Conflicting Values Part 2
INSERT INTO pxp.conflictingvalues(targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid)
Select  baseentityiid, classifieriid, contentId, couplingtype, id, 
-- recordstatus
CASE WHEN recordstatus = 4 THEN 2 ELSE recordstatus END,
couplingtype, 0 
From ( Select classifieriid, baseentityiid, contentId, couplingtype, id,
	CASE 
	When tagvalue IS NULL AND (notification is null OR notification = '{}') AND count = 1 THEN 1
	When count > 1 AND tagvalue IS NULL THEN 6	
	When (notification is null OR notification = '{}') AND count = 1 AND couplingtype = 4 THEN 4
	When (notification is not null AND notification != '{}') AND count = 1 AND couplingtype = 4 THEN 6		
	When count > 1 AND couplingtype = 4 AND tagvalue IS NOT NULL THEN 4					
	WHEN count = 1 AND couplingtype = 3 THEN 4
	WHEN count > 1 AND couplingtype = 3 AND tagvalue IS NOT NULL THEN 4
	ELSE 1
	END recordstatus
	From ( Select temp_staging_tagrec.tagid, temp_staging_tagrec.instanceid, temp_staging_tagrec.notification,
		-- classifierIId
		helper_config."classifierIId" classifieriid,
		-- baseentityiid
		baseentity.baseentityiid,
		-- no. of conflicting values
		COUNT(staging_tag_conflict.tagid),
		-- Couplingtype
		CASE 
		WHEN staging_tag_conflict.couplingtype = 'dynamicCoupled' THEN 3
		WHEN staging_tag_conflict.couplingtype = 'tightlyCoupled' THEN 4
		END couplingtype,
		-- contentId 
		baseentity1.baseentityiid contentId,
		-- couplingsourceiid
		propertyconfig.propertyiid id,
		tagvalue
		From temp_staging_tagrec
		LEFT JOIN staging.helper_config helper_config ON helper_config.cid = temp_staging_tagrec.tagid
		LEFT JOIN staging.temp_baseentity baseentity ON baseentity.id = temp_staging_tagrec.instanceid
		LEFT JOIN staging.tagconflictingvalues staging_tag_conflict
		ON staging_tag_conflict.tagid = temp_staging_tagrec.tagid
		AND staging_tag_conflict.klassinstanceid = temp_staging_tagrec.instanceid
		LEFT JOIN staging.temp_baseentity baseentity1 ON baseentity1.id = staging_tag_conflict.source::json ->> 'contentId'
		LEFT JOIN pxp.propertyconfig propertyconfig ON propertycode = staging_tag_conflict.source::json ->> 'id'
		Where tagvalue = '100' AND staging_tag_conflict.couplingtype != 'looselyCoupled'
		AND baseentity.baseentityiid IS NOT NULL
		GROUP BY temp_staging_tagrec.tagid, temp_staging_tagrec.instanceid, temp_staging_tagrec.notification,
		  helper_config."classifierIId", baseentity.baseentityiid, staging_tag_conflict.couplingtype,
		  baseentity1.baseentityiid, propertyconfig.propertyiid, temp_staging_tagrec.tagvalue
	) tag_rec_with_count 
	Where classifieriid 
	NOT IN (
		Select propertyiid From pxp.conflictingvalues 
		Where tag_rec_with_count.baseentityiid = targetentityiid 
		AND tag_rec_with_count.contentId = sourceentityiid 
		AND tag_rec_with_count.id = couplingsourceiid AND localeiid = 0)
) teg_rec_with_recordstatus
Where recordstatus != 1 AND contentid IS NOT NULL;
raise notice 'End Insert into pxp.conflictingvalues: %', clock_timestamp();

raise notice 'Start Insert into pxp.coupledrecord: %', clock_timestamp();
-- Coupled Record
INSERT INTO pxp.coupledrecord(propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling, masterentityiid, masterpropertyiid, localeiid)
Select classifieriid, baseentityiid, 
CASE WHEN adaptedvalue = 0 AND recordstatus = 4 THEN 2 ELSE recordstatus END recordstatus,
couplingbehaviour, 
CASE 
	WHEN couplingbehaviour = 4 and propertytype = 12 THEN 2
	WHEN couplingbehaviour = 3 and propertytype = 12 THEN 5	
END couplingtype, tagiid||':'||classifieriid masternodeid, '', sourceentityiid, classifieriid masterpropertyiid, 0 
From ( Select classifieriid, temp_tag_record.baseentityiid, tagiid,
	-- propertytype
	propertyconfig.propertytype propertytype,
	-- couplingbehaviour
	CASE 
	WHEN temp_tag_record.couplingtype = 'dynamicCoupled' THEN 3
	WHEN temp_tag_record.couplingtype = 'tightlyCoupled' THEN 4
	END couplingbehaviour,
	-- contentId 
	--baseentityouter.baseentityiid contentId,
	-- couplingsourceiid
	pxpconflictingvalues.sourceentityiid sourceentityiid,
	--propertyconfig.propertyiid id,
	-- AdaptedValues
	count(tag_conflict_val.tagid) adaptedvalue,
	-- recordstatus
	CASE 
	When usertags IS NULL AND (notification is null OR notification = '{}') AND count = 1 THEN 1
	When count > 1 AND usertags IS NULL THEN 6	
	When (notification is null OR notification = '{}') AND count = 1 AND temp_tag_record.couplingtype = 'tightlyCoupled' THEN 4
	When (notification is not null AND notification != '{}') AND count = 1 AND temp_tag_record.couplingtype = 'tightlyCoupled' THEN 6		
	When count > 1 AND temp_tag_record.couplingtype = 'tightlyCoupled' AND usertags IS NOT NULL THEN 4					
	WHEN count = 1 AND temp_tag_record.couplingtype = 'dynamicCoupled' THEN 4
	WHEN count > 1 AND temp_tag_record.couplingtype = 'dynamicCoupled' AND usertags IS NOT NULL THEN 4
	ELSE 1
	END recordstatus, 
	usertags
	From ( Select tagid, tagiid, classifieriid, instanceid, baseentityiid, couplingtype, source,
		string_agg(hstore, ',')::hstore usertags, count, notification 
		From ( Select temp_staging_tagrec.tagid, instanceid, tagiid, temp_staging_tagrec.notification,
		-- classifierIId
		helper_config."classifierIId" classifieriid,
		-- baseentityiid
		baseentity.baseentityiid,
		-- usertags
		hstore(tagkey, tagvalue):: text,
		-- Couplingtype
		temp_source_column.couplingtype couplingtype,
		-- source
		temp_source_column.source source,
		-- count
		COUNT(tagconflictingvalues.tagid)
		From temp_staging_tagrec
		LEFT JOIN staging.helper_config helper_config ON helper_config.cid = temp_staging_tagrec.tagid
		LEFT JOIN staging.temp_baseentity baseentity ON baseentity.id = temp_staging_tagrec.instanceid
		LEFT JOIN staging.tagconflictingvalues tagconflictingvalues ON tagconflictingvalues.tagid = temp_staging_tagrec.tagid 
			AND tagconflictingvalues.klassinstanceid = temp_staging_tagrec.instanceid
		LEFT JOIN temp_source_column ON temp_source_column.tagid = temp_staging_tagrec.tagid 
			AND temp_source_column.klassinstanceid = temp_staging_tagrec.instanceid Where tagvalue = '100'
			AND baseentity.baseentityiid IS NOT NULL
		GROUP BY temp_staging_tagrec.tagid, instanceid, tagiid, temp_staging_tagrec.notification, helper_config."classifierIId",
		baseentity.baseentityiid, hstore(tagkey, tagvalue):: text, temp_source_column.couplingtype, temp_source_column.source
	) tag_record_with_count
	GROUP BY tagiid, tagid, classifieriid, instanceid, baseentityiid, notification, couplingtype, count, source
	) temp_tag_record
	LEFT JOIN pxp.conflictingvalues pxpconflictingvalues 
	  ON pxpconflictingvalues.propertyiid = temp_tag_record.classifieriid
	  AND pxpconflictingvalues.targetentityiid = temp_tag_record.baseentityiid
	--LEFT JOIN staging.baseentity baseentityouter ON baseentityouter.id = temp_tag_record.source::json ->> 'contentId'
	LEFT JOIN pxp.propertyconfig propertyconfig ON propertyconfig.propertycode = temp_tag_record.source::json ->> 'id'
	LEFT JOIN staging.tagconflictingvalues tag_conflict_val ON tag_conflict_val.tagid = temp_tag_record.tagid
		AND temp_tag_record.usertags IS NOT NULL AND temp_tag_record.usertags !='' 
		AND tag_conflict_val.klassinstanceid = temp_tag_record.instanceid 
		AND tag_conflict_val.tagvalues = temp_tag_record.usertags
	Where temp_tag_record.couplingtype != 'looselyCoupled' AND pxpconflictingvalues.recordstatus = 4
	 --AND classifieriid = 1002321 AND temp_tag_record.baseentityiid = 20688845
	GROUP BY classifieriid, temp_tag_record.baseentityiid, tagiid, propertyconfig.propertytype, temp_tag_record.couplingtype,
	  propertyconfig.propertyiid, temp_tag_record.usertags, temp_tag_record.notification,
	  temp_tag_record.count, pxpconflictingvalues.sourceentityiid
) tagrecords
Where recordstatus = 4 AND classifieriid 
NOT IN (
	Select propertyiid From pxp.coupledrecord 
	Where tagrecords.baseentityiid = entityiid 
	AND tagrecords.sourceentityiid = masterentityiid 
	AND localeiid = 0
);
raise notice 'End Insert into pxp.coupledrecord: %', clock_timestamp();

 DROP TABLE temp_staging_tagrec;
 DROP TABLE temp_source_column;
raise notice 'End %', clock_timestamp();
end
$BODY$;
