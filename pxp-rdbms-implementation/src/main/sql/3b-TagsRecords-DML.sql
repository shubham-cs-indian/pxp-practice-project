/**
 * Author:  vallee
 * Created: Jul 29, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"
#include "./include/coupling-defs.sql"

---------------------------------------------------------------------------------
-- Read tags record
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_readTagsRecord ) ( p_entityiid _IID, p_propertyiids _IIDARRAY)
_RETURN _CURSOR( pxp.directtagsrecord)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.directtagsrecord);
begin
    _IMPLEMENT_CURSOR( vCursor,
        select tr.* from pxp.AllTagsRecord tr where tr.entityIID = p_entityiid and tr.propertyiid in
#ifdef PGSQL11
        (select * from unnest(p_propertyiids))
#elif ORACLE12C
        (select * from table(p_propertyiids))
#endif
        );
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Clone tags records from an entity to another one
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_cloneTagsRecords )( p_sourceEntityIID _IID, p_targetEntityIID _IID)
_IMPLEMENT_AS
begin
	insert into pxp.TagsRecord 
		(select a.propertyIID, p_targetEntityIID, a.recordStatus, a.usrTags 
			from pxp.TagsRecord a where a.entityIID = p_sourceEntityIID);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create separated target tags records when a source of coupling is updated
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_CreateSeparatedTagsTargets )( p_masterNodeID _STRING)
_IMPLEMENT_AS
	vCouplingTarget		_RECORD(pxp.CoupledRecord);
	vRecordStatus		_INT;
begin
	for vCouplingTarget in ( select * from pxp.CoupledRecord 
		where masternodeid = p_masterNodeID 
		and couplingType in _COUPLING_WITH_SEPARABLE_RECORDS
		and recordStatus <> _RecordStatus_NOTIFIED )
	loop
			if ( vCouplingTarget.recordStatus = _RecordStatus_CLONED ) then
				vRecordStatus := _RecordStatus_DIRECT;
			else
				vRecordStatus := _RecordStatus_FORKED ;
			end if;
		insert into pxp.tagsRecord ( propertyIID, entityIID, recordStatus, usrTags) 
			( select vCouplingTarget.propertyIID, vCouplingTarget.entityIID, vRecordStatus, usrTags
			from pxp.tagsRecord
			where entityIID = vCouplingTarget.masterEntityIID and propertyIID = vCouplingTarget.masterPropertyIID );
	end loop;
	_CALL pxp.sp_ResolveCouplingTargets(p_masterNodeID);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create separated target tags records when a source of coupling is deleted
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_CreateTagsFromAllTargetsAndDelete )( p_masterNodeID _STRING)
_IMPLEMENT_AS
	vCouplingTarget		_RECORD(pxp.CoupledRecord);
begin
	for vCouplingTarget in ( select * from pxp.CoupledRecord 
		where masternodeid = p_masterNodeID )
	loop
		insert into pxp.tagsRecord ( propertyIID, entityIID, recordStatus, usrTags) 
			( select vCouplingTarget.propertyIID, vCouplingTarget.entityIID, _RecordStatus_DIRECT, usrTags
			from pxp.tagsRecord
			where entityIID = vCouplingTarget.masterEntityIID and propertyIID = vCouplingTarget.masterPropertyIID );
	end loop;
	delete from pxp.CoupledRecord where masternodeid = p_masterNodeID;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Separate target tags records from a source of coupling before source deletion
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_CreateUniqueTagsTargets )( p_masterNodeID _STRING)
_IMPLEMENT_AS
	vCouplingTarget		_RECORD(pxp.CoupledRecord);
begin
	for vCouplingTarget in ( select * from pxp.CoupledRecord 
		where masternodeid = p_masterNodeID 
		and recordStatus <> _RecordStatus_NOTIFIED )
	loop
		insert into pxp.tagsRecord ( propertyIID, entityIID, recordStatus, usrTags) 
			( select vCouplingTarget.propertyIID, vCouplingTarget.entityIID, _RecordStatus_DIRECT, usrTags
			from pxp.tagsRecord
			where entityIID = vCouplingTarget.masterEntityIID and propertyIID = vCouplingTarget.masterPropertyIID );
	end loop;
end
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Only create the tags record part (directly used for a default value)
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_createTagsRecordPart ) (p_propertyIID _IID, p_entityIID _IID, p_recordStatus _INT, p_usrTags _STRING)
_IMPLEMENT_AS
begin
    insert into pxp.tagsRecord (propertyIID, entityIID, recordStatus, usrTags)
    values( p_propertyIID, p_entityIID, p_recordStatus, p_usrTags :: hstore);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create a Tags record
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_createTagsRecord )( p_entityIID _IID, p_propertyIID _IID, p_status _INT, p_usrTags _STRING)
_IMPLEMENT_AS
begin
	insert into pxp.tagsRecord
            (entityIID, propertyIID, recordstatus, usrTags)
        values(p_entityIID, p_propertyIID, p_status, p_usrTags :: hstore);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create a tags record by default value rule
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_createDefaultTagsRecord )(p_baseEntityIID _IID, p_propertyIID _IID, 
			p_couplingBehavior _INT, p_CouplingType _INT, p_CouplingExpression _STRING,
            p_usrTags _STRING, p_masterEntityIID _IID, p_masterPropertyIID _IID)
_IMPLEMENT_AS
	vExistingRecord			_INT;
	vMasterNodeID			_VARCHAR;
begin
	select count(*) into vExistingRecord from pxp.TagsRecord where entityIID = p_masterEntityIID and propertyIID = p_masterPropertyIID;
	if ( vExistingRecord = 0 ) then
		insert into pxp.TagsRecord
			(propertyIID, entityiid, recordStatus, usrTags)
		values( p_masterPropertyIID, p_masterEntityIID, _RecordStatus_DEFAULT_VALUE, p_usrTags :: hstore);
	end if;
	vMasterNodeID := _TO_VARCHAR(p_masterEntityIID) || ':' || _TO_VARCHAR( p_masterPropertyIID);
	_CALL pxp.sp_CreateCoupling( p_baseEntityIID, p_propertyIID,
            _RecordStatus_COUPLED, p_couplingBehavior, p_couplingType, vMasterNodeID, p_CouplingExpression, p_masterEntityIID, p_masterPropertyIID);
end
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Update a coupled tags record following change in coupling conditions
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_updateCoupledTagsRecord ) ( p_basentityiid _IID, p_propertyiid _IID, p_status _INT, 
														 p_usrTags _STRING)
_IMPLEMENT_AS
 begin
    _CALL pxp.sp_ResolveCoupling( p_basentityiid, p_propertyiid);
   	_CALL pxp.sp_createTagsRecord( p_basentityiid, p_propertyiid, p_status, p_usrTags); 
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Update a tags record
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_updateTagsRecord )( p_entityiid _IID, p_propertyiid _IID, p_usrTags _STRING)
_IMPLEMENT_AS
begin
	update pxp.tagsRecord set usrTags = p_usrTags :: hstore
	where EntityIID = p_entityiid and propertyIID = p_propertyiid;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Resolve tight coupling notification
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_resolveTagsRecordNotification )( p_entityiid _IID, p_propertyiid _IID, p_recordStatus _INT)
_IMPLEMENT_AS
begin
	if(p_recordStatus = _RecordStatus_DIRECT) then
		delete from pxp.coupledRecord  where entityiid = p_entityiid and propertyIID= p_propertyiid and recordstatus = _RecordStatus_NOTIFIED;
		update pxp.tagsRecord set recordStatus = _RecordStatus_DIRECT where entityiid = p_entityiid and propertyIID= p_propertyiid and recordstatus = _RecordStatus_FORKED;
	elsif(p_recordStatus = _RecordStatus_FORKED) then
		delete from pxp.tagsRecord where entityiid = p_entityiid and propertyIID= p_propertyiid and recordstatus = _RecordStatus_FORKED;
		update pxp.coupledRecord set recordstatus = _RecordStatus_COUPLED where entityiid = p_entityiid and propertyIID= p_propertyiid and recordstatus = _RecordStatus_NOTIFIED;
	end if;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Delete a tags record
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_deleteTagsRecord )( p_entityiid _IID, p_propertyiid _IID)
_IMPLEMENT_AS
begin
	
	delete from pxp.tagsRecord where propertyIID = p_propertyiid and entityIID = p_entityiid;
	delete from pxp.coupledRecord where propertyIID = p_propertyiid and entityIID = p_entityiid;
	-- to do => manage the dependency graph
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Remove tag value code
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_removeTagValueCode )( p_tagValueCodes _VARARRAY)
_IMPLEMENT_AS
    vTagValueCodeIdx    _INT;
    vTagValueCode       _VARCHAR;
begin
      
    for vTagValueCodeIdx in 1 .. _COUNT(p_tagValueCodes)
    loop
        vTagValueCode := _ARRAY( p_tagValueCodes, vTagValueCodeIdx);
        
        update pxp.tagsRecord set usrtags = delete(usrtags, vTagValueCode);

        --remove tag value from contextualobject table
         update pxp.contextualobject set cxtTags = delete(cxtTags, vTagValueCode);
    
         -- remove tag value from task table
  		update pxp.task set prioritycode = null 
  		where prioritycode = vTagValueCode; 
        
		--delete tag value from tagvalueconfig
		delete from pxp.tagvalueconfig where tagvaluecode = vTagValueCode;
	end loop;
end
_IMPLEMENT_END;

#ifdef PGSQL11
-- end of PGSQL Tags Records Procedure/Function declarations
#elif ORACLE12C
-- end of ORACLE Tags Records Procedure/Function declarations
#endif
