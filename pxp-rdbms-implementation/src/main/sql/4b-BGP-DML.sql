/**
 * Author:  vallee
 * Created: May 9, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

-- PROCEDURES RUN in BGP
#include "./include/sql-defs.sql"

----------------------------------------------------------------------------
-- Delete a contextual object with its related tags
----------------------------------------------------------------------------
_PROCEDURE(pxp.sp_deleteContextualObjectByIID)( p_contextualObjectIID _IID )
_IMPLEMENT_AS
begin
    if ( p_contextualObjectIID is not null ) then
        delete from pxp.ContextualObject where contextualObjectIID = p_contextualObjectIID;
    end if;
end
_IMPLEMENT_END;

----------------------------------------------------------------------------
-- Purge the event queue before the input date (BIGINT)
----------------------------------------------------------------------------
_PROCEDURE(pxp.sp_purgeEventQueueByDate)( p_date _LONG )
_IMPLEMENT_AS
begin
    delete from pxp.eventQueue where posted < p_date;
end
_IMPLEMENT_END;


----------------------------------------------------------------------------
-- When attribute or tag gets deleted from OrientDB we must delete the property
-- by PropertyIID from config and record table. 
--			Delete property entry from property record table 
--			Delete property config entry from property config table 
----------------------------------------------------------------------------
_PROCEDURE(pxp.sp_deleteProperty)( p_propIID _IIDARRAY )
_IMPLEMENT_AS
begin
	delete from pxp.propertyConfig where propertyIID in
#ifdef PGSQL11
            ( select unnest(p_propIID) );
#elif ORACLE12C
            ( select * from TABLE(p_propIID) );
#endif
end
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- detect and delete orphan value records:
-- detect & delete the value records which are no more referenced in property records 
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_deleteOrphanValueRecords ) ( p_batchSize _INT )
_RETURN _INT
_IMPLEMENT_AS
	vDeletedPropertyRecordCount _INT;
begin
    -- The inner most sub query lists all the records matching ValueRecords and PropertyRecords
    -- The outer sub query lists property records not listed in the inner sub query.
    -- The delete query will purge all the records from value record listed by the sub query and return the IIDs of purged record.
    -- Based on the purged records we generate the count and return
#ifdef PGSQL11
	with deleted as (
#endif
		delete from pxp.ValueRecord where _ROWID in
			(select vr._ROWID from pxp.ValueRecord vr where
	    		not exists
	    		(select be.baseEntityIID from pxp.baseEntity be where be.baseEntityIID = vr.entityIID)
	    	_LIMIT p_batchSize
#ifdef PGSQL11
	    ) returning entityIID, propertyIID)
	select count(1) into vDeletedPropertyRecordCount from deleted;
#elif ORACLE12C
		);
	vDeletedPropertyRecordCount := sql%rowcount;
#endif
	return vDeletedPropertyRecordCount;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- detect and delete orphan tag records:
-- delete the tag records which are no more referenced in property records
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_deleteOrphanTagRecords ) ( p_batchSize _INT )
_RETURN _INT
_IMPLEMENT_AS
	vDeletedPropertyRecordCount _INT;
begin
    -- The inner most sub query lists all the records matching TagRecords and PropertyRecords
    -- The outer sub query lists property records not listed in the inner sub query.
    -- The delete query will purge all the records from tag record listed by the sub query and return the IIDs of purged record.
    -- Based on the purged records we generate the count and return

#ifdef PGSQL11
	with deleted as (
#endif
		delete from pxp.TagsRecord where _ROWID in
			(select tr._ROWID from pxp.TagsRecord tr where
	    		not exists
	    		(select be.baseEntityIID from pxp.baseEntity be where be.baseEntityIID = tr.entityIID)
	    	_LIMIT p_batchSize
#ifdef PGSQL11
	    ) returning entityIID, propertyIID)
	select count(1) into vDeletedPropertyRecordCount from deleted;
#elif ORACLE12C
		);
	vDeletedPropertyRecordCount := sql%rowcount;
#endif
	return vDeletedPropertyRecordCount;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- detect and delete orphan side1 relationship records:
-- delete the side1 records from relation which are no more referenced in property records
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_deleteOrphanSide1Records ) ( p_batchSize _INT )
_RETURN _INT
_IMPLEMENT_AS
	vDeletedPropertyRecordCount _INT;
begin
    -- The inner most sub query lists all the records matching Relation and PropertyRecords
    -- The outer sub query lists property records not listed in the inner sub query.
    -- The delete query will purge all the records from value record listed by the sub query and return the IIDs of purged record.
    -- Based on the purged records we generate the count and return

#ifdef PGSQL11
	with deleted as (
#endif
		delete from pxp.Relation where _ROWID in
			(select r._ROWID from pxp.Relation r where
	    		NOT EXISTS
	    		(select be.baseEntityIID from pxp.baseEntity be where be.baseEntityIID = r.side1EntityIID)
	    	_LIMIT p_batchSize
#ifdef PGSQL11
	    ) returning side1EntityIID)
	select count(1) into vDeletedPropertyRecordCount from deleted;
#elif ORACLE12C
		);
	vDeletedPropertyRecordCount := sql%rowcount;
#endif
	return vDeletedPropertyRecordCount;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- detect and delete orphan side2 relationship records:
-- delete the side2 records from relation which are no more referenced in property records
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_deleteOrphanSide2Records ) ( p_batchSize _INT )
_RETURN _INT
_IMPLEMENT_AS
	vDeletedPropertyRecordCount _INT;
begin
    -- The inner most sub query lists all the records matching Relation and PropertyRecords
    -- The outer sub query lists property records not listed in the inner sub query.
    -- The delete query will purge all the records from value record listed by the sub query and return the IIDs of purged record.
    -- Based on the purged records we generate the count and return
    
#ifdef PGSQL11
	with deleted as (
#endif
		delete from pxp.Relation where _ROWID in
			(select r._ROWID from pxp.Relation r where
	    		NOT EXISTS
	    		(select be.baseEntityIID from pxp.baseEntity be where be.baseEntityIID = r.side2EntityIID)
	    	_LIMIT p_batchSize
#ifdef PGSQL11
	    ) returning side2EntityIID)
	select count(1) into vDeletedPropertyRecordCount from deleted;
#elif ORACLE12C
		);
	vDeletedPropertyRecordCount := sql%rowcount;
#endif
	return vDeletedPropertyRecordCount;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- detect and delete orphan baseEntityIIDs from collectionbaseentitylink:
-- delete the baseEntityIIDs collectionbaseentitylink which are no more referenced.
---------------------------------------------------------------------------------

_FUNCTION( pxp.fn_deleteOrphanCollectionBaseEntityLink ) ( p_batchSize _INT )
_RETURN _INT
_IMPLEMENT_AS
	vDeletedPropertyRecordCount _INT;
begin

 #ifdef PGSQL11
	with deleted as (
#endif
  delete from pxp.collectionbaseentitylink where _ROWID in
   (select vr._ROWID from pxp.collectionbaseentitylink vr where
       not exists
       (select be.collectioniid from pxp.collection be where be.collectioniid = vr.collectioniid)
       --TO DO: LIMIT needs to be added.
#ifdef PGSQL11
     ) returning baseentityiid)
 select count(1) into vDeletedPropertyRecordCount from deleted;
#elif ORACLE12C
		);
	vDeletedPropertyRecordCount := sql%rowcount;
#endif
	return vDeletedPropertyRecordCount;
end
_IMPLEMENT_END;	

---------------------------------------------------------------------------------
-- Remove relationship property
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_removeRelationProperty )( p_propertyIID _IIDARRAY)
_IMPLEMENT_AS
begin
    create temporary table deleterecord on commit drop as
    (select propertyiid, side1contextualobjectiid, side2contextualobjectiid 
    	from pxp.relation where propertyiid in 
    	#ifdef PGSQL11
            ( select unnest(p_propertyIID) )
		#elif ORACLE12C
            ( select * from table(p_propertyIID) )
		#endif
    );
    
    --delete property relation records from relation table
    delete from pxp.relation rel
    using deleterecord 
    where rel.propertyiid = deleterecord.propertyiid;
    
    --delete relationship side contextualobject from contextualobject table
    delete from pxp.contextualobject cxt 
    using deleterecord
    where deleterecord.side1contextualobjectiid = cxt.contextualobjectiid or deleterecord.side2contextualobjectiid = cxt.contextualobjectiid;

    --delete relationship extension from baseentity table
    --select array(select extendedentityiid from deleterecord where extendedentityiid is not null) into vExtendedEntityIID;
    --_CALL pxp.sp_deletebaseentitybyiids( vExtendedEntityIID );
    
    --delete taxonomy conflict from baseentitytaxonomyconflictlink table
    delete from pxp.baseentitytaxonomyconflictlink tc
    using deleterecord
    where tc.propertyiid = deleterecord.propertyiid;
    
    --delete couplings from relationcouplerecord 
    delete from pxp.relationcouplerecord rcr
    using deleterecord
    where rcr.naturerelationshipid = deleterecord.propertyiid;
    
    --delete property from propertyconfig table
    _CALL pxp.sp_deleteproperty( p_propertyIID );
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Remove attribute property
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_removeAttributeProperty )( p_propertyIID _IIDARRAY)
_IMPLEMENT_AS
begin
    --delete property records from valuerecord table
	with delsubquery as (
    	delete from pxp.valuerecord where propertyiid in
    	
    	#ifdef PGSQL11
            ( select unnest(p_propertyIID) )
		#elif ORACLE12C
            ( select * from table(p_propertyIID) )
		#endif
    	
    	returning contextualobjectiid
	)
	
	--delete property context records from contextualobject table
	delete from pxp.contextualobject cxt
	using delsubquery
	where delsubquery.contextualobjectiid is not null 
	and cxt.contextualobjectiid = delsubquery.contextualobjectiid; 
    
	--delete property couple records from coupledrecord table
	delete from pxp.coupledrecord where propertyiid in 
		#ifdef PGSQL11
            ( select unnest(p_propertyIID) );
		#elif ORACLE12C
            ( select * from table(p_propertyIID) );
		#endif
		
	--delete task on property from task table
	delete from pxp.task where propertyiid in 
	#ifdef PGSQL11
            ( select unnest(p_propertyIID) );
		#elif ORACLE12C
            ( select * from table(p_propertyIID) );
		#endif
	
	--delete property from propertyconfig table
    _CALL pxp.sp_deleteproperty( p_propertyIID );
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Remove tag property
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_removeTagProperty )( p_propertyIID _IIDARRAY)
_IMPLEMENT_AS
begin
	
	create temporary table deleterecord on commit drop as
    (select tagvaluecode from pxp.tagvalueconfig where propertyiid in 
    	#ifdef PGSQL11
            ( select unnest(p_propertyIID) )
		#elif ORACLE12C
            ( select * from table(p_propertyIID) )
		#endif
    );
	
  	--delete tag values from contextualobject table
	--pending
	
	--delete tag from task (priority) table
	update pxp.task set prioritycode = null 
	where prioritycode in (select tagvaluecode from deleterecord);  
	
	--delete property tag records from tagsrecord table
	delete from pxp.tagsrecord where propertyiid in
		#ifdef PGSQL11
            ( select unnest(p_propertyIID) );
		#elif ORACLE12C
            ( select * from table(p_propertyIID) );
		#endif
	
		
	--delete property couple records from coupledrecord table
	delete from pxp.coupledrecord where propertyiid in
		#ifdef PGSQL11
            ( select unnest(p_propertyIID) );
		#elif ORACLE12C
            ( select * from table(p_propertyIID) );
		#endif
	
	--delete task on property from task table
	delete from pxp.task where propertyiid in 
	#ifdef PGSQL11
            ( select unnest(p_propertyIID) );
		#elif ORACLE12C
            ( select * from table(p_propertyIID) );
		#endif
	
	--delete property tag values from tagvalueconfig table
	delete from pxp.tagvalueconfig where propertyiid in 
		#ifdef PGSQL11
            ( select unnest(p_propertyIID) );
		#elif ORACLE12C
            ( select * from table(p_propertyIID) );
		#endif
	
	--delete property from propertyconfig table
    _CALL pxp.sp_deleteproperty( p_propertyIID );
end
_IMPLEMENT_END;
