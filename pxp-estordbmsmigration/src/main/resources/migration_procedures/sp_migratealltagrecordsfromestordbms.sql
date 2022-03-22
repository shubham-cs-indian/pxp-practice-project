-- PROCEDURE: staging.sp_migratealltagrecordsfromestordbms()

-- DROP PROCEDURE staging.sp_migratealltagrecordsfromestordbms();

CREATE OR REPLACE PROCEDURE staging.sp_migratealltagrecordsfromestordbms()
LANGUAGE 'plpgsql'

AS $BODY$
declare
recordrow staging.tagrecord%rowtype;
conflictingrecord staging.tagconflictingvalues%rowtype;
counter_for_tightly_coupled_records INTEGER;
counter_for_dynamic_coupled_records INTEGER;
begin
	counter_for_tightly_coupled_records := 1;
	counter_for_dynamic_coupled_records := 1;
--Inserting all coupling/non coupling records from staging to pxp in loop
FOR recordrow IN
   select * FROM staging.tagrecord
LOOP
	--Tightly coupled case one (noOfRecordsInConflictingValuesTable==1 && notification.isEmpty)
	IF (Select count(*) from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled') = 1 
	AND (recordrow.notification is null OR recordrow.notification = '{}') AND recordrow.tagvalues is not null 
	THEN
	--Inserting data into pxp conflictingvalues table
	INSERT INTO pxp.conflictingvalues(
		targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
		localeiid)
	VALUES (
		(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
		(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
		(Select baseentityiid from staging.baseentity where id = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled')::json ->> 'contentId'),
		4,
		(Select propertyiid from pxp.propertyconfig where propertycode = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled')::json ->> 'id'),
		4,
		3,
		0
		);
	--Inserting data into pxp coupledrecord table
	INSERT INTO pxp.coupledrecord(
		propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
		masterentityiid, masterpropertyiid, localeiid)
	VALUES (
		(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
		(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
		4,
		4,
		3,
		concat ((Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)), ':', (Select "classifierIId" From staging.helper_config where cid = recordrow.tagid)),
		'',
		(Select baseentityiid from staging.baseentity where id = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled')::json ->> 'contentId'),
		(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
		0
	);
	--Condition: noOfRecordsInConflictingValuesTable==1 && notification.isNotEmpty
	ELSEIF (Select count(*) from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled') = 1 
	AND recordrow.notification is not null AND recordrow.notification <> '{}'
	THEN
	--Condition: (recordrow.tagvalues).is null
	 IF recordrow.tagvalues is null
		THEN 
		--Inserting data into pxp tagsrecord table
		INSERT INTO pxp.tagsrecord(
			propertyiid, entityiid, recordstatus, usrtags) 
		 VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid), 
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			6, 
			null
			);
		--Inserting data into pxp conflictingvalues table
		INSERT INTO pxp.conflictingvalues(
			targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
			localeiid)
		VALUES (
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled')::json ->> 'contentId'),
			4,
			(Select propertyiid from pxp.propertyconfig where propertycode = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled')::json ->> 'id'),
			6,
			3,
			0
			);
		
	ELSE 
		--Inserting data into pxp tagsrecord table
		INSERT INTO pxp.tagsrecord(
			propertyiid, entityiid, recordstatus, usrtags) 
		 VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid), 
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			6, 
			recordrow.tagvalues
			);
		--Inserting data into pxp conflictingvalues table
		INSERT INTO pxp.conflictingvalues(
			targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
			localeiid)
		VALUES (
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled')::json ->> 'contentId'),
			4,
			(Select propertyiid from pxp.propertyconfig where propertycode = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled')::json ->> 'id'),
			6,
			3,
			0
			);
		END IF;
	--Tightly coupled case two (noOfRecordsInConflictingValuesTable > 1 && valueInValueRecordTable.isEmpty)
    ELSIF (Select count(*) from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled') > 1  
    THEN 
    IF recordrow.tagvalues is null
    THEN 	
    	--Inserting data into pxp tagsrecord table
		INSERT INTO pxp.tagsrecord(
			propertyiid, entityiid, recordstatus, usrtags) 
		 VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid), 
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			6, 
			null
			);
		--Inserting all conflicting records from staging to pxp in loop
	    FOR conflictingrecord IN
	    select * FROM staging.tagconflictingvalues WHERE  tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled'
	    LOOP
		--Inserting data into pxp conflictingvalues table
	    INSERT INTO pxp.conflictingvalues(
			targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
			localeiid)
		VALUES (
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
			(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
			4,
			(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
			6,
			3,
			0
			);
		END LOOP;
	ELSE
		--Inserting all conflicting records from staging to pxp in loop
		FOR conflictingrecord IN
	   	select * FROM staging.tagconflictingvalues WHERE  tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled'
		LOOP
			IF conflictingrecord.tagvalues = recordrow.tagvalues AND counter_for_tightly_coupled_records = 1
			THEN
				--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					4,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					4,
					3,
					0
					);
					counter_for_tightly_coupled_records := 2;
			ELSE 
				--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					4,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					2,
					3,
					0
					);
				END IF;
		END LOOP;
			counter_for_tightly_coupled_records := 1;
		--Inserting data into pxp coupledrecord table
		INSERT INTO pxp.coupledrecord(
			propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
			masterentityiid, masterpropertyiid, localeiid)
		VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			4,
			3,
			2,
			concat ((Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)), ':', (Select "classifierIId" From staging.helper_config where cid = recordrow.tagid)),
			'',
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'tightlyCoupled' AND tagvalues = recordrow.tagvalues LIMIT 1)::json ->> 'contentId'),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
			0
		);
		END IF;
		
	--Dynamic coupled case one (noOfRecordsInConflictingValuesTable = 1)
	ELSIF (Select count(*) from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)  AND couplingtype = 'dynamicCoupled') = 1 
	AND recordrow.tagvalues is not null
	THEN
		--Inserting data into pxp conflictingvalues table
		INSERT INTO pxp.conflictingvalues(
			targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
			localeiid)
		VALUES (
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled')::json ->> 'contentId'),
			4,
			(Select propertyiid from pxp.propertyconfig where propertycode = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled')::json ->> 'id'),
			4,
			3,
			0
			);
		--Inserting data into pxp coupledrecord table
		INSERT INTO pxp.coupledrecord(
			propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
			masterentityiid, masterpropertyiid, localeiid)
		VALUES (
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
			(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
			4,
			3,
			2,
			concat ((Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)), ':', (Select "classifierIId" From staging.helper_config where cid = recordrow.tagid)),
			'',
			(Select baseentityiid from staging.baseentity where id = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled')::json ->> 'contentId'),
			(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
			0
		);
		
	--Dynamic coupled case one (noOfRecordsInConflictingValuesTable > 1)
	ELSIF (Select count(*) from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled') > 1 
	THEN
		IF recordrow.tagvalues is not null
		THEN
			--Inserting all conflicting records from staging to pxp in loop
			FOR conflictingrecord IN
		   	select * FROM staging.tagconflictingvalues WHERE tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled'
			LOOP
				IF conflictingrecord.tagvalues = recordrow.tagvalues AND counter_for_dynamic_coupled_records = 1
				THEN
				--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					3,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					4,
					2,
					0
					);
					counter_for_dynamic_coupled_records := 2;
				ELSE 
					--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					3,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					2,
					2,
					0
					);
				END IF;
			END LOOP;
			counter_for_dynamic_coupled_records := 1;
			--Inserting data into pxp coupledrecord table
			INSERT INTO pxp.coupledrecord(
				propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
				masterentityiid, masterpropertyiid, localeiid)
			VALUES (
				(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
				(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
				4,
				3,
				2,
				concat ((Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)), ':', (Select "classifierIId" From staging.helper_config where cid = recordrow.tagid)),
				'',
				(Select baseentityiid from staging.baseentity where id = (Select source from staging.tagconflictingvalues where tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled' AND tagvalues = recordrow.tagvalues LIMIT 1)::json ->> 'contentId'),
				(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
				0
			);
		ELSE
			--Inserting data into pxp tagsrecord table
			INSERT INTO pxp.tagsrecord(
				propertyiid, entityiid, recordstatus, usrtags) 
			 VALUES (
				(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid), 
				(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
				6, 
				null
				);
			--Inserting all conflicting records from staging to pxp in loop
			FOR conflictingrecord IN
		   	select * FROM staging.tagconflictingvalues WHERE tagid = recordrow.tagid AND klassinstanceId = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END) AND couplingtype = 'dynamicCoupled'
			LOOP
				--Inserting data into pxp conflictingvalues table
				INSERT INTO pxp.conflictingvalues(
					targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype,
					localeiid)
				VALUES (
					(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
					(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid),
					(Select baseentityiid from staging.baseentity where id = conflictingrecord.source::json ->> 'contentId'),
					3,
					(Select propertyiid from pxp.propertyconfig where propertycode = conflictingrecord.source::json ->> 'id'),
					6,
					2,
					0
					);
			END LOOP;
		END IF;
			
	--Loosely coupled cases
	ELSE 
	--Inserting data into pxp tagsrecord table
	INSERT INTO pxp.tagsrecord(
		propertyiid, entityiid, recordstatus, usrtags) 
	 VALUES (
		(Select "classifierIId" From staging.helper_config where cid = recordrow.tagid), 
		(Select baseentityiid from staging.baseentity where id = (CASE WHEN recordrow.variantinstanceid = '' THEN recordrow.klassinstanceid ELSE recordrow.variantinstanceid END)),
		1, 
		recordrow.tagvalues
		);
	END if;
END LOOP;
return;
end
$BODY$;

