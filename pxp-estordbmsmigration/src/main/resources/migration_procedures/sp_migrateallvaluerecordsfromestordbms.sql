-- PROCEDURE: staging.sp_migrateallvaluerecordsfromestordbms()

-- DROP PROCEDURE staging.sp_migrateallvaluerecordsfromestordbms();

CREATE OR REPLACE PROCEDURE staging.sp_migrateallvaluerecordsfromestordbms()
LANGUAGE 'plpgsql'

AS $BODY$
declare
begin
-- Fill All attributes --

	raise notice 'Start %', clock_timestamp();
	
	CREATE TEMP TABLE temp_staging_valrec AS ( Select 
	CASE WHEN variantinstanceid = '' THEN klassinstanceid ELSE variantinstanceid END instanceid,
	vrec.* From staging.valuerecord vrec );

	CREATE TEMP TABLE temp_attributeconflictingvalues AS (
	Select attributeconflictingvalues.klassinstanceid, attributeconflictingvalues.attributeid,
	MIN(source::text) as source, attributeconflictingvalues.couplingtype,
	count(attributeconflictingvalues.attributeid) count, attributeconflictingvalues.language
	from staging.attributeconflictingvalues attributeconflictingvalues LEFT JOIN temp_staging_valrec staging_valrec
	ON attributeconflictingvalues.attributeid = staging_valrec.attributeid 
	AND attributeconflictingvalues.klassinstanceid = staging_valrec.instanceid 
	AND attributeconflictingvalues.language = staging_valrec.language
	GROUP BY attributeconflictingvalues.klassinstanceid, attributeconflictingvalues.attributeid,
	attributeconflictingvalues.couplingtype, attributeconflictingvalues.language);

	CREATE INDEX idx_temp_staging_valrec_attributeid
	ON temp_staging_valrec(attributeid);

	CREATE INDEX idx_temp_staging_valrec_instanceid
	ON temp_staging_valrec(instanceid);
	
	CREATE INDEX idx_temp_staging_valrec_language
	ON temp_staging_valrec(language);
	
	raise notice 'Start Insert into pxp.valuerecord: %', clock_timestamp();
	
	-- Insert in valuerecord
	INSERT INTO pxp.valuerecord(propertyiid, entityiid, recordstatus, valueiid, localeid, contextualobjectiid, value, ashtml, asnumber, unitsymbol, calculation)
	Select * from (
		Select propertyiid , baseentityiid, 
		-- recordstatus
			CASE 
			When value = '' AND (notification is null OR notification = '{}') AND count = 1 THEN 1
			When count > 1 AND value = '' THEN 6	
			When (notification is null OR notification = '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 4
			When (notification is not null AND notification != '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 6		
			When count > 1 AND couplingtype = 'tightlyCoupled' AND value != '' THEN 4					
			WHEN count = 1 AND couplingtype = 'dynamicCoupled' THEN 4
			WHEN count > 1 AND couplingtype = 'dynamicCoupled' AND value != '' THEN 4
			ELSE 1
			END recordstatus, 
		valueiid, language, contextiid, value, valueAsHtml, valueAsNumber, unitsymbol, calculation 
		from (
			Select
			-- propertyiid
			helper_config."classifierIId" propertyiid, 
			-- Couplingtype
			attributeconflictingvalues.couplingtype couplingtype,
			-- entityiid
			baseentity.baseentityiid baseentityiid,
			-- no. of conflicting values
			attributeconflictingvalues.count count,
			staging_valrec.notification,
			staging_valrec.valueiid, staging_valrec.language,
			-- contextualobjectiid
			CASE WHEN staging_valrec.contextiid = 0 THEN NULL ELSE staging_valrec.contextiid END, 
			staging_valrec.value,
			staging_valrec.valueAsHtml,
			staging_valrec.valueAsNumber,
			staging_valrec.unitsymbol unitsymbol,
			staging_valrec.calculation calculation
			from temp_staging_valrec staging_valrec
			LEFT JOIN staging.helper_config helper_config ON helper_config.cid = staging_valrec.attributeid
			LEFT JOIN staging.temp_baseentity baseentity ON baseentity.id = staging_valrec.instanceid
			LEFT JOIN temp_attributeconflictingvalues attributeconflictingvalues 
				ON attributeconflictingvalues.attributeid = staging_valrec.attributeid 
				and attributeconflictingvalues.klassinstanceid = staging_valrec.instanceid
				and attributeconflictingvalues.language = staging_valrec.language
			WHERE helper_config."classifierIId" is not null 
			AND baseentity.baseentityiid IS NOT NULL
		)tt
	) valuerecords Where recordstatus != 4;		
		
	raise notice 'End Insert into pxp.valuerecord: %', clock_timestamp();
	
	raise notice 'Start Insert into pxp.conflictingvalues: %', clock_timestamp();
	-- Insert in conflictingvalues
	-- Looselycoupled not allowed
		INSERT INTO pxp.conflictingvalues(targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid)
		Select klassinstanceid, propertyiid, contentId, couplingtype, id, 
		-- recordstatus
		CASE WHEN adaptedValue IS NULL AND recordstatus = 4 THEN 2 ELSE recordstatus END,
		couplingtype, languageiid
		from ( 
			Select (Select value From staging.attributeconflictingvalues staging_val_conflicting 
				Where (rec.value IS NOT NULL AND rec.value != '')
				AND rec.value = staging_val_conflicting.value 
				AND rec.attributeid = staging_val_conflicting.attributeid 
				AND staging_val_conflicting.klassinstanceid = rec.instanceid 			 
				AND staging_val_conflicting.language = rec.language LIMIT 1) adaptedValue,
			-- contentId 
			baseentityouter.baseentityiid contentId,
			rec.propertyiid,
			-- klassinstanceid
			baseentityouter1.baseentityiid klassinstanceid,
			-- couplingtype
			CASE 
			WHEN rec.couplingtype = 'dynamicCoupled' THEN 3
			WHEN rec.couplingtype = 'tightlyCoupled' THEN 4
			END couplingtype,	
			propertyconfigouter.propertyiid id,
			-- recordstatus
			CASE 
			When value = '' AND (notification is null OR notification = '{}') AND count = 1 THEN 1
			When count > 1 AND value = '' THEN 6	
			When (notification is null OR notification = '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 4
			When (notification is not null AND notification != '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 6		
			When count > 1 AND couplingtype = 'tightlyCoupled' AND value != '' THEN 4					
			WHEN count = 1 AND couplingtype = 'dynamicCoupled' THEN 4
			WHEN count > 1 AND couplingtype = 'dynamicCoupled' AND value != '' THEN 4
			ELSE 1
			END recordstatus, 
			-- languageiid
			languageconfigouter.languageiid languageiid	
			from (Select staging_valrec.attributeid, 
				  -- propertyiid
				  helper_config."classifierIId" propertyiid,
				  -- entityiid
				  baseentity.baseentityiid baseentityiid,
				  -- No. of conflicting values
				  attributeconflictingvalues.count count,
				  -- Couplingtype
				  attributeconflictingvalues.couplingtype couplingtype,
				  -- source 
				  attributeconflictingvalues.source source, staging_valrec.instanceid,
				  staging_valrec.language, staging_valrec.notification, staging_valrec.value		
				from temp_staging_valrec staging_valrec
				LEFT JOIN staging.helper_config helper_config ON helper_config.cid = staging_valrec.attributeid
				LEFT JOIN staging.temp_baseentity baseentity ON baseentity.id = staging_valrec.instanceid
				LEFT JOIN temp_attributeconflictingvalues attributeconflictingvalues 
					ON attributeconflictingvalues.attributeid = staging_valrec.attributeid 
					and attributeconflictingvalues.klassinstanceid = staging_valrec.instanceid
					and attributeconflictingvalues.language = staging_valrec.language
				WHERE helper_config."classifierIId" is not null
			) rec
			LEFT JOIN staging.temp_baseentity baseentityouter ON baseentityouter.id = rec.source::json ->> 'contentId'
			LEFT JOIN staging.temp_baseentity baseentityouter1 ON baseentityouter1.id = rec.instanceid
			LEFT JOIN pxp.propertyconfig propertyconfigouter 
				ON propertyconfigouter.propertycode = rec.source::json ->> 'id'
			LEFT JOIN pxp.languageconfig languageconfigouter ON languageconfigouter.languagecode = rec.language
			Where couplingtype != 'looselyCoupled'
			AND baseentityouter.baseentityiid IS NOT NULL
			AND baseentityouter1.baseentityiid IS NOT NULL) tt
		Where recordstatus != 1 
		AND tt.propertyiid NOT IN (Select propertyiid From pxp.conflictingvalues 
								 Where tt.klassinstanceid = targetentityiid AND tt.contentId = sourceentityiid 
								 AND tt.id = couplingsourceiid AND tt.languageiid = localeiid);

								 
		raise notice 'End Insert into pxp.conflictingvalues: %', clock_timestamp();
		
		raise notice 'Start Insert into pxp.conflictingvalues: %', clock_timestamp();
		INSERT INTO pxp.conflictingvalues(targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid)
		Select klassinstanceid, propertyiid, contentId, couplingtype, id, 
		-- recordstatus
		CASE 
		WHEN recordstatus = 4 THEN 2
		ELSE recordstatus
		END,
		couplingtype, languageiid from (
			Select 			
			-- contentId 
			baseentityouter.baseentityiid contentId,
			rec.propertyiid,
			-- klassinstanceid
			baseentityouter1.baseentityiid klassinstanceid,
			-- couplingtype
			CASE 
			WHEN rec.couplingtype = 'dynamicCoupled' THEN 3
			WHEN rec.couplingtype = 'tightlyCoupled' THEN 4
			END couplingtype,	
			propertyconfigouter.propertyiid id,
			-- recordstatus
			CASE 
			When value = '' AND (notification is null OR notification = '{}') AND count = 1 THEN 1
			When count > 1 AND value = '' THEN 6	
			When (notification is null OR notification = '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 4
			When (notification is not null AND notification != '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 6		
			When count > 1 AND couplingtype = 'tightlyCoupled' AND value != '' THEN 4					
			WHEN count = 1 AND couplingtype = 'dynamicCoupled' THEN 4
			WHEN count > 1 AND couplingtype = 'dynamicCoupled' AND value != '' THEN 4
			ELSE 1
			END recordstatus, 
			-- languageiid
			languageconfigouter.languageiid languageiid	
			from (
				Select staging_valrec.attributeid, 
				-- propertyiid
				helper_config."classifierIId" propertyiid, 
				-- Couplingtype
				staging_val_conflict.couplingtype,
				-- entityiid
				baseentity.baseentityiid baseentityiid,
				-- no. of conflicting values
				count(staging_val_conflict.attributeid) count,
				-- source
				staging_val_conflict.source, staging_valrec.instanceid, staging_valrec.language,
				staging_valrec.notification, staging_valrec.value, staging_val_conflict.value conflictingvalue 		
				from (Select CASE WHEN variantinstanceid = '' THEN klassinstanceid ELSE variantinstanceid END instanceid,
					  vrec.* From staging.valuerecord vrec)staging_valrec
				LEFT JOIN staging.helper_config helper_config ON helper_config.cid = staging_valrec.attributeid
				LEFT JOIN staging.temp_baseentity baseentity ON baseentity.id = staging_valrec.instanceid
				LEFT JOIN staging.attributeconflictingvalues staging_val_conflict 
					ON staging_val_conflict.attributeid = staging_valrec.attributeid
					AND staging_val_conflict.klassinstanceid = staging_valrec.instanceid
					AND staging_val_conflict.language = staging_valrec.language
				WHERE helper_config."classifierIId" is not null 
				GROUP BY staging_valrec.attributeid, helper_config."classifierIId", baseentity.baseentityiid,
				staging_val_conflict.couplingtype, staging_val_conflict.source, staging_valrec.instanceid,
				staging_valrec.language, staging_valrec.notification, staging_valrec.value, staging_val_conflict.value
			) rec
			LEFT JOIN staging.temp_baseentity baseentityouter ON baseentityouter.id = rec.source::json ->> 'contentId'
			LEFT JOIN staging.temp_baseentity baseentityouter1 ON baseentityouter1.id = rec.instanceid
			LEFT JOIN pxp.propertyconfig propertyconfigouter 
				ON propertyconfigouter.propertycode = rec.source::json ->> 'id'
			LEFT JOIN pxp.languageconfig languageconfigouter ON languageconfigouter.languagecode = rec.language
			Where couplingtype != 'looselyCoupled'
			AND baseentityouter.baseentityiid IS NOT NULL
			AND baseentityouter1.baseentityiid IS NOT NULL) tt
		Where tt.propertyiid NOT IN (Select propertyiid From pxp.conflictingvalues 
								 Where tt.klassinstanceid = targetentityiid AND tt.contentId = sourceentityiid 
								 AND tt.id = couplingsourceiid AND tt.languageiid = localeiid)
								 AND recordstatus != 1;
		raise notice 'End Insert into pxp.conflictingvalues: %', clock_timestamp();
		
		raise notice 'Start Insert into pxp.coupledrecord: %', clock_timestamp();
	-- Insert Coupled record
		INSERT INTO pxp.coupledrecord(propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling, masterentityiid, masterpropertyiid, localeiid)
		Select * From ( 
			Select propertyiid, klassinstanceid, 
			CASE 
			WHEN adaptedValue IS NULL AND recordstatus = 4 THEN 2 ELSE recordstatus
			END recordstatus,
			couplingbehaviour, 
			CASE 
				WHEN couplingbehaviour = 4 and propertytype = 12 THEN 2
				WHEN couplingbehaviour = 3 and propertytype = 12 THEN 5	
			END couplingtype, valueiid||':'||propertyiid masternodeid, '',
			id, propertyiid masterpropertyiid, languageiid from (
				Select
				rec.valueiid,
				-- Adapted value
				(Select value From staging.attributeconflictingvalues staging_val_conflicting 
				Where (rec.value IS NOT NULL AND rec.value != '')
				AND rec.value = staging_val_conflicting.value 
				AND rec.attributeid = staging_val_conflicting.attributeid 
				AND staging_val_conflicting.klassinstanceid = rec.instanceid 			 
				AND staging_val_conflicting.language = rec.language LIMIT 1) adaptedValue,
				-- contentId 
				baseentityouter.baseentityiid contentId,
				rec.propertyiid,
				-- klassinstanceid
				baseentityouter1.baseentityiid klassinstanceid,
				-- couplingbehaviour
				CASE 
				WHEN rec.couplingtype = 'dynamicCoupled' THEN 4
				WHEN rec.couplingtype = 'tightlyCoupled' THEN 3
				END couplingbehaviour,
				-- propertytype
				propertyconfigouter.propertytype propertytype,
				-- source : propertyiid
				propertyconfigouter.propertyiid id,
				-- recordstatus
				CASE 
				When value = '' AND (notification is null OR notification = '{}') AND count = 1 THEN 1
				When count > 1 AND value = '' THEN 6	
				When (notification is null OR notification = '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 4
				When (notification is not null AND notification != '{}') AND count = 1 AND couplingtype = 'tightlyCoupled' THEN 6		
				When count > 1 AND couplingtype = 'tightlyCoupled' AND value != '' THEN 4					
				WHEN count = 1 AND couplingtype = 'dynamicCoupled' THEN 4
				WHEN count > 1 AND couplingtype = 'dynamicCoupled' AND value != '' THEN 4
				ELSE 1
				END recordstatus, 
				-- languageiid
				languageconfigouter.languageiid languageiid	
				from (
					Select  staging_valrec.attributeid,
					-- propertyiid
					helper_config."classifierIId" propertyiid,
					-- No. of conflicting values
					staging_val_conflict.count count,		
					-- Couplingtype
					staging_val_conflict.couplingtype couplingtype,
					-- source
					staging_val_conflict.source source,
					staging_valrec.instanceid, staging_valrec.language, staging_valrec.notification, 
					staging_valrec.value, staging_valrec.valueiid 		
					from temp_staging_valrec staging_valrec
					LEFT JOIN staging.helper_config helper_config ON helper_config.cid = staging_valrec.attributeid
					LEFT JOIN temp_attributeconflictingvalues staging_val_conflict 
						ON staging_val_conflict.attributeid = staging_valrec.attributeid
						AND staging_val_conflict.klassinstanceid = staging_valrec.instanceid
						AND staging_val_conflict.language = staging_valrec.language
					WHERE helper_config."classifierIId" is not null
				) rec
			LEFT JOIN staging.temp_baseentity baseentityouter ON baseentityouter.id = rec.source::json ->> 'contentId'
			LEFT JOIN staging.temp_baseentity baseentityouter1 ON baseentityouter1.id = rec.instanceid
			LEFT JOIN pxp.propertyconfig propertyconfigouter 
				ON propertyconfigouter.propertycode = rec.source::json ->> 'id'
			LEFT JOIN pxp.languageconfig languageconfigouter ON languageconfigouter.languagecode = rec.language
				Where rec.couplingtype != 'looselyCoupled'
				AND baseentityouter.baseentityiid IS NOT NULL
				AND baseentityouter1.baseentityiid IS NOT NULL
			) tt
		) coupledrec 
		Where recordstatus = 4
		AND propertyiid NOT IN(Select propertyiid From pxp.coupledrecord 
								Where coupledrec.klassinstanceid = entityiid 
								AND coupledrec.languageiid = localeiid);
		raise notice 'End Insert into pxp.coupledrecord: %', clock_timestamp();
		
		DROP TABLE temp_staging_valrec;
		DROP TABLE temp_attributeconflictingvalues;
		raise notice 'End: %', clock_timestamp();
end
$BODY$;
