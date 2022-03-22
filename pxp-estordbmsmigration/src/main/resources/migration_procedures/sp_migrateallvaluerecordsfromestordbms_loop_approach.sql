-- PROCEDURE: staging.sp_migrateallvaluerecordsfromestordbms_loop_approach()

-- DROP PROCEDURE staging.sp_migrateallvaluerecordsfromestordbms_loop_approach();

CREATE OR REPLACE PROCEDURE staging.sp_migrateallvaluerecordsfromestordbms_loop_approach(
	)
LANGUAGE 'plpgsql'

AS $BODY$
declare
recordrow staging.valuerecord%rowtype;
conflictingrecord staging.attributeconflictingvalues%rowtype;
counter_for_tightly_coupled_records INTEGER;
counter_for_dynamic_coupled_records INTEGER;
begin
	counter_for_tightly_coupled_records := 1;
	counter_for_dynamic_coupled_records := 1;
--Inserting all coupling/non coupling records from staging to pxp in loop
FOR recordrow IN
   select * FROM staging.valuerecord
LOOP
	--Tightly coupled case one (noOfRecordsInConflictingValuesTable==1 && notification.isEmpty)
	IF (Select count(*) from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language) = 1 
	AND (recordrow.notification is null OR recordrow.notification = '{}') AND recordrow.value <> '' 
	THEN
	--Inserting data into pxp conflictingvalues table
	INSERT INTO pxp.conflictingvalues(
		targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
		localeiid)
	VALUES (
		(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
		(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
		(Select baseentityiid from staging.baseentity where id = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language)::json ->> 'contentId'),
		4,
		(Select propertyiid from pxp.propertyconfig where propertycode = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language)::json ->> 'id'),
		4,
		3,
		(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
		);
	--Inserting data into pxp coupledrecord table
	INSERT INTO pxp.coupledrecord(
		propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
		masterentityiid, masterpropertyiid, localeiid)
	VALUES (
		(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
		(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
		4,
		4,
		3,
		concat ((Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)), ':', (Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid)),
		'',
		(Select baseentityiid from staging.baseentity where id = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language)::json ->> 'contentId'),
		(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
		(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
	);
	--Condition: noOfRecordsInConflictingValuesTable==1 && notification.isNotEmpty
	ELSEIF (Select count(*) from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language) = 1 
	AND recordrow.notification is not null AND recordrow.notification <> '{}'
	THEN
	--Condition: (recordrow.value).isEmpty
	 IF recordrow.value = ''
		THEN 
		--Inserting data into pxp valuerecord table
		INSERT INTO pxp.valuerecord(
			propertyiid, entityiid, recordstatus, valueiid, localeid, 
			contextualobjectiid, value, ashtml, asnumber, unitsymbol,
			calculation) 
		 VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid), 
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			6, 
			recordrow.valueiid,
			recordrow.language, 
			(CASE When recordrow.contextiid = 0 THEN NULL ELSE recordrow.contextiid end), 
			'', 
			recordrow.valueashtml, 
			recordrow.valueasnumber,
			recordrow.unitsymbol, 
			recordrow.calculation
			);
		--Inserting data into pxp conflictingvalues table
		INSERT INTO pxp.conflictingvalues(
			targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
			localeiid)
		VALUES (
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language)::json ->> 'contentId'),
			4,
			(Select propertyiid from pxp.propertyconfig where propertycode = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language)::json ->> 'id'),
			6,
			3,
			(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
			);
		
	ELSE 
		--Inserting data into pxp valuerecord table
		INSERT INTO pxp.valuerecord(
			propertyiid, entityiid, recordstatus, valueiid, localeid, 
			contextualobjectiid, value, ashtml, asnumber, unitsymbol,
			calculation) 
		 VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid), 
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			6, 
			recordrow.valueiid,
			recordrow.language, 
			(CASE When recordrow.contextiid = 0 THEN NULL ELSE recordrow.contextiid end), 
			recordrow.value, 
			recordrow.valueashtml, 
			recordrow.valueasnumber,
			recordrow.unitsymbol, 
			recordrow.calculation
			);
		--Inserting data into pxp conflictingvalues table
		INSERT INTO pxp.conflictingvalues(
			targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
			localeiid)
		VALUES (
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language)::json ->> 'contentId'),
			4,
			(Select propertyiid from pxp.propertyconfig where propertycode = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language)::json ->> 'id'),
			6,
			3,
			(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
			);
		END IF;
	--Tightly coupled case two (noOfRecordsInConflictingValuesTable > 1 && valueInValueRecordTable.isEmpty)
    ELSIF (Select count(*) from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language) > 1  
    THEN 
    IF recordrow.value = ''
    THEN 	
    	--Inserting data into pxp valuerecord table
		INSERT INTO pxp.valuerecord(
			propertyiid, entityiid, recordstatus, valueiid, localeid, 
			contextualobjectiid, value, ashtml, asnumber, unitsymbol,
			calculation) 
		 VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid), 
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			6, 
			recordrow.valueiid,
			recordrow.language, 
			(CASE When recordrow.contextiid = 0 THEN NULL ELSE recordrow.contextiid end), 
			'', 
			recordrow.valueashtml, 
			recordrow.valueasnumber,
			recordrow.unitsymbol, 
			recordrow.calculation
			);
		--Inserting all conflicting records from staging to pxp in loop
	    FOR conflictingrecord IN
	    select * FROM staging.attributeconflictingvalues WHERE  attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language
	    LOOP
		--Inserting data into pxp conflictingvalues table
	    INSERT INTO pxp.conflictingvalues(
			targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
			localeiid)
		VALUES (
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
			(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
			4,
			(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
			6,
			3,
			(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
			);
		END LOOP;
	ELSE
		--Inserting all conflicting records from staging to pxp in loop
		FOR conflictingrecord IN
	   	select * FROM staging.attributeconflictingvalues WHERE  attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language
		LOOP
			IF conflictingrecord.value = recordrow.value AND counter_for_tightly_coupled_records = 1
			THEN
				--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					4,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					4,
					3,
					(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
					);
					counter_for_tightly_coupled_records := 2;
			ELSE 
				--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					4,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					2,
					3,
					(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
					);
				END IF;
		END LOOP;
			counter_for_tightly_coupled_records := 1;
		--Inserting data into pxp coupledrecord table
		INSERT INTO pxp.coupledrecord(
			propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
			masterentityiid, masterpropertyiid, localeiid)
		VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			4,
			3,
			2,
			concat ((Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)), ':', (Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid)),
			'',
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND language = recordrow.language AND value = recordrow.value LIMIT 1)::json ->> 'contentId'),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
			(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
		);
		END IF;
		
	--Dynamic coupled case one (noOfRecordsInConflictingValuesTable = 1)
	ELSIF (Select count(*) from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)  AND couplingtype = 'dynamicCoupled' AND language = recordrow.language) = 1 
	AND recordrow.value <> '' 
	THEN
		--Inserting data into pxp conflictingvalues table
		INSERT INTO pxp.conflictingvalues(
			targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
			localeiid)
		VALUES (
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled' AND language = recordrow.language)::json ->> 'contentId'),
			4,
			(Select propertyiid from pxp.propertyconfig where propertycode = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled' AND language = recordrow.language)::json ->> 'id'),
			4,
			3,
			(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
			);
		--Inserting data into pxp coupledrecord table
		INSERT INTO pxp.coupledrecord(
			propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
			masterentityiid, masterpropertyiid, localeiid)
		VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			4,
			3,
			2,
			concat ((Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)), ':', (Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid)),
			'',
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled' AND language = recordrow.language)::json ->> 'contentId'),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
			(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
		);
		
	--Dynamic coupled case one (noOfRecordsInConflictingValuesTable > 1)
	ELSIF (Select count(*) from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled' AND language = recordrow.language) > 1 
	THEN
		IF recordrow.value <> ''
		THEN
			--Inserting all conflicting records from staging to pxp in loop
			FOR conflictingrecord IN
		   	select * FROM staging.attributeconflictingvalues WHERE attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled' AND language = recordrow.language
			LOOP
				IF conflictrecord.value = recordrow.value AND counter_for_dynamic_coupled_records = 1
				THEN
				--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					3,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					4,
					2,
					(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
					);
					counter_for_dynamic_coupled_records := 2;
				ELSE 
					--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					3,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					2,
					2,
					(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
					);
				END IF;
			END LOOP;
			counter_for_dynamic_coupled_records := 1;
			--Inserting data into pxp coupledrecord table
			INSERT INTO pxp.coupledrecord(
				propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
				masterentityiid, masterpropertyiid, localeiid)
			VALUES (
				(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
				(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
				4,
				3,
				2,
				concat ((Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)), ':', (Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid)),
				'',
				(Select baseentityiid from staging.baseentity where id = (Select source from staging.attributeconflictingvalues where attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled' AND value = recordrow.value AND language = recordrow.language LIMIT 1)::json ->> 'contentId'),
				(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
				(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
			);
		ELSE
			--Inserting data into pxp valuerecord table
			INSERT INTO pxp.valuerecord(
				propertyiid, entityiid, recordstatus, valueiid, localeid, 
				contextualobjectiid, value, ashtml, asnumber, unitsymbol,
				calculation) 
			 VALUES (
				(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid), 
				(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
				6, 
				recordrow.valueiid,
				recordrow.language, 
				(CASE When recordrow.contextiid = 0 THEN NULL ELSE recordrow.contextiid end), 
				'', 
				recordrow.valueashtml, 
				recordrow.valueasnumber,
				recordrow.unitsymbol, 
				recordrow.calculation
				);
			--Inserting all conflicting records from staging to pxp in loop
			FOR conflictingrecord IN
		   	select * FROM staging.attributeconflictingvalues WHERE attributeid = recordrow.attributeid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled' AND language = recordrow.language
			LOOP
				--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					3,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					6,
					2,
					(CASE when recordrow.language IS NOT NULL THEN (Select languageiid from pxp.languageconfig where languagecode = recordrow.language) ELSE 0 END)
					);
			END LOOP;
		END IF;
			
	--Loosely coupled cases
	ELSE 
	--Inserting data into pxp valuerecord table
	INSERT INTO pxp.valuerecord(
		propertyiid, entityiid, recordstatus, valueiid, localeid, 
		contextualobjectiid, value, ashtml, asnumber, unitsymbol,
		calculation) 
	 VALUES (
		(Select "classifierIId" From staging.helper_config where cid = recordrow.attributeid), 
		(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
		1, 
		recordrow.valueiid,
		recordrow.language, 
		(CASE When recordrow.contextiid = 0 THEN NULL ELSE recordrow.contextiid end), 
		recordrow.value, 
		recordrow.valueashtml, 
		recordrow.valueasnumber,
		recordrow.unitsymbol, 
		recordrow.calculation
		);
	END if;
END LOOP;
return;
end
$BODY$;
