/**
 * Author:  vallee
 * Created: Nov 16, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

/*----------------------------------------------------------------------------*/
-- create a bulk of properties in order to prepare performance tests
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.createPerfProperties) ( pNbAttributes _INT, pNbTags _INT, pNbRelationships _INT)
_IMPLEMENT_AS
	no			_INT;
	propIID		_LONG;
begin
	-- create the requested attributes with codes starting ATTR#1
	for no in 1..pNbAttributes loop
		propIID := pxp.fn_propertyConfig( 'ATTR#' || no, _PropertyType_TEXT, _SuperType_ATTRIBUTE);
	end loop;
	-- create the requested tags with codes starting TAGS#1
	for no in 1..pNbTags loop
		propIID := pxp.fn_propertyConfig( 'TAG#' || no, _PropertyType_TAG, _SuperType_TAGS);
	end loop;
	-- create the requested relations with codes starting REL#1
	for no in 1..pNbRelationships loop
		propIID := pxp.fn_propertyConfig( 'REL#' || no, _PropertyType_RELATIONSHIP, _SuperType_RELATION_SIDE);
	end loop;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
-- create a bulk of entities in order to prepare performance tests
-- /!\ This only works from an empty database (no test data)
-- attributes are created in locale en_US
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.createPerfEntities) ( pNbEntities _INT, pstartWith _INT, pNbValueRec _INT, pNbTagsRec _INT, pNbRelations _INT)
_IMPLEMENT_AS
	no			_INT;
	noRec		_INT;
	entityIID	_IID;
	firstEntityIID	_IID;
	propIID		_IID;
	recordIID	_IID;
	combIID		_VARCHAR;
	systime		_LONG;
	value		_VARCHAR;
	trackiid	_IID;
begin
	-- create the requested entities with IDs starting ENT#1
	for no in 1..pNbEntities loop
		_NUMTIMESTAMP_INTO( systime);
		entityIID := pxp.fn_createBaseEntity( 'ENT#' || no  + pstartWith, null, 'pim', 
                20, _BaseType_ARTICLE, 1, null,
                'en_US', null, null, null, null, null, 
				'{}', systime, _ADMIN_USER_IID, false);
		recordIID := pxp.fn_createValueRecord( entityIID, _ARTICLE_NAME_PROPERTY_IID, null, 0,
				_RecordStatus_DIRECT, 'en_US', 'ENT#' || no  + pstartWith, null, '', 0, null);
		for noRec in 1..pNbValueRec loop
			select propertyIID into propIID from pxp.propertyConfig where propertyCode = 'ATTR#' || noRec;
			value := 'ENT#' || no + pstartWith || '::ATTR#' || noRec || '::en_US';
			recordIID := pxp.fn_createValueRecord( entityIID, propIID, null, 0,
				_RecordStatus_DIRECT, 'en_US', value, null, '', 0, null);
		end loop;
		for noRec in 1..pNbTagsRec loop
			select propertyIID into propIID from pxp.propertyConfig where propertyCode = 'TAG#' || noRec;
#ifdef PGSQL11
			_CALL pxp.sp_createTagsRecord( entityIID, propIID, _RecordStatus_DIRECT,
				ARRAY['T1','T2','T3','T4','T5'], ARRAY[100, 100, 50, 50, 25]);
#elif ORACLE12C
			_CALL pxp.sp_createTagsRecord( entityIID, propIID, _RecordStatus_DIRECT,
				pxp.varcharArray('T1','T2','T3','T4','T5'), pxp.iidArray(100, 100, 50, 50, 25));
#endif
		end loop;
		if ( no > 1 ) then
			for noRec in 1..pNbRelations loop
				select propertyIID into propIID from pxp.propertyConfig where propertyCode = 'REL#' || noRec;
#ifdef PGSQL11
				combIID := pxp.fn_createRelations( entityIID, propIID, 1, null,
					ARRAY[firstEntityIID], null, null, null);
#elif ORACLE12C
				combIID := pxp.fn_createRelations( entityIID, propIID, 1, null,
					pxp.iidArray(firstEntityIID), null, null, null);
#endif
			end loop;
		else
			firstEntityIID := entityIID;
		end if;
		trackiid := pxp.fn_postTrackingEvent( 
			_EventTopic_ENTITY, _EventType_OBJECT_CREATION, _ADMIN_USER_IID, 
			entityIID, '{}', null, systime);
	end loop;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
-- duplicate value records into another new locale ID
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.createPerfLocaleValues) ( pNbEntities _INT, pLocaleID _STRING)
_IMPLEMENT_AS
	no _INT;
	vEntityIID _IID;
	vValueiid _IID;
	vValue _VARCHAR;
	vValueRecord _RECORD(pxp.valuerecord);
begin
	for  no in 1..pNbEntities loop
		select baseentityiid into vEntityIID from pxp.baseentity where baseentityid = 'ENT#' || no;

		for vValueRecord in (select * from pxp.valuerecord  where entityiid = vEntityIID and localeID = 'en_US') loop
			vValueiid := _NEXTVAL( pxp.seqIID);
			vValue := vValueRecord.value || ':TO:' || pLocaleID;
			insert into pxp.valuerecord values( vValueRecord.propertyIID, vValueRecord.entityiid, 
				vValueRecord.recordstatus, vValueiid, pLocaleID, vValueRecord.contextualObjectIID,  
				vValue, vValueRecord.asHtml, vValueRecord.asNumber, vValueRecord.unitSymbol, 
				vValueRecord.calculation);
		end loop;
	end loop;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
-- Create coupled value records inside the same entity
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.createCoupledValues) ( pNbEntities _INT, pNbRecords _INT)
_IMPLEMENT_AS
	no	_INT;
	vEntityIID  _IID;
	norec _INT;
	propIID		_IID;
	sourceIID	_IID;
	masterNodeID _VARCHAR;
	coupling	_VARCHAR;
begin
	/* create the requested attributes with codes starting COUP#1 */
	for no in 1..pNbRecords loop
		propIID := pxp.fn_propertyConfig( 'COUP#' || no, _PropertyType_TEXT, _SuperType_ATTRIBUTE);
	end loop;
	/* create coupled records for the specified number of entities */
	for  no in 1..pNbEntities loop
		select baseentityiid into vEntityIID from pxp.baseentity where baseentityid = 'ENT#' || no;

		for norec in 1..pNbRecords loop
			select propertyIID into propIID from pxp.propertyconfig where propertycode like 'COUP#' || norec;
			select propertyIID into sourceIID from pxp.propertyconfig where propertycode like 'ATTR#' || norec;
			
			masterNodeID := _TO_VARCHAR(vEntityIID) || ':' || _TO_VARCHAR(sourceIID);
			coupling := '$entity&.[' || _TO_VARCHAR(sourceIID) || ']';
			insert into pxp.coupledrecord values(
				propIID, vEntityIID, _RecordStatus_COUPLED, _CouplingBehavior_DYNAMIC, _CouplingType_DYN_INHERITANCE,
				masterNodeID, coupling, vEntityIID, sourceIID
			);
		end loop;
	end loop;
end
_IMPLEMENT_END;


-- Sequence of generation to be copied/pasted into PG ADIMN / SQL DEVELOPER
-- 100,000 takes up to 3 to 10 mn for execution (depend on the number of sub-objects)
#define NB_ENTITIES 100000 
#define NB_ATTR	5
#define NB_TAGS	5
#define NB_REL	2

_CALL pxp.createPerfProperties( NB_ATTR, NB_TAGS, NB_REL);
-- generation of entities in 10 batches
_CALL pxp.createPerfEntities( NB_ENTITIES, 0, NB_ATTR, NB_TAGS, NB_REL);
_CALL pxp.createPerfEntities( NB_ENTITIES, NB_ENTITIES, NB_ATTR, NB_TAGS, NB_REL);
_CALL pxp.createPerfEntities( NB_ENTITIES, 2*NB_ENTITIES, NB_ATTR, NB_TAGS, NB_REL);
_CALL pxp.createPerfEntities( NB_ENTITIES, 3*NB_ENTITIES, NB_ATTR, NB_TAGS, NB_REL);
_CALL pxp.createPerfEntities( NB_ENTITIES, 4*NB_ENTITIES, NB_ATTR, NB_TAGS, NB_REL);
_CALL pxp.createPerfEntities( NB_ENTITIES, 5*NB_ENTITIES, NB_ATTR, NB_TAGS, NB_REL);
_CALL pxp.createPerfEntities( NB_ENTITIES, 6*NB_ENTITIES, NB_ATTR, NB_TAGS, NB_REL);
_CALL pxp.createPerfEntities( NB_ENTITIES, 7*NB_ENTITIES, NB_ATTR, NB_TAGS, NB_REL);
_CALL pxp.createPerfEntities( NB_ENTITIES, 8*NB_ENTITIES, NB_ATTR, NB_TAGS, NB_REL);
_CALL pxp.createPerfEntities( NB_ENTITIES, 9*NB_ENTITIES, NB_ATTR, NB_TAGS, NB_REL);

select 'entities: ' || count(*) as report from pxp.baseentity
union select 'values: ' || count(*) as report from pxp.valuerecord
union select 'tags: ' || count(*) as report from pxp.tagsrecord
union select 'relations: ' || count(*) as report from pxp.relation
union select 'couplings: ' || count(*) as report from pxp.coupledrecord
union select 'tracks: ' || count(*) as report from pxp.objecttracking;

-- generation of internationalized attributes
_CALL pxp.createPerfLocaleValues( NB_ENTITIES/2, 'en_CA');

-- geenration of coupled attributes
_CALL pxp.createCoupledValues( NB_ENTITIES/2, 2);
