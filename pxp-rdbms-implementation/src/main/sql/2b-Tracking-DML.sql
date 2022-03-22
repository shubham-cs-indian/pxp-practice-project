/**
 * Author:  vallee
 * Created: Mar 16, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

-----------------------------------------------------------------
-- pxp.fn_opensession lazy initialization of user config
-----------------------------------------------------------------
_FUNCTION( pxp.fn_opensession )(p_sessionID varchar, p_hostname varchar, p_userName varchar)
_RETURN _LONG
_IMPLEMENT_AS
    vUserIID        _LONG; -- corresponding to sequence type in stored procedures
    vSessionID      _VARCHAR;
    vNow            _TIMESTAMP;
begin
    -- lazy initializaiton of user config
    vUserIID := pxp.fn_userConfig( p_userName);
    -- vNow = current system time (no TZ)
    _SYSTIMESTAMP_INTO( vNow);
    -- insert the new session information only if not already existing
    begin
        select sessionID into vSessionID from pxp.UserSession where sessionID = p_sessionID;
    exception
        when no_data_found then vSessionID := null;
    end;
    if ( vSessionID is null ) then
        insert into pxp.UserSession( sessionID, hostname, userIID, loginTime) values ( p_sessionID, p_hostname, vUserIID, vNow);
    end if;
    return vUserIID;
end
_IMPLEMENT_END;

-----------------------------------------------------------------
-- pxp.fn_convertEvent convert event type into tracking event
-----------------------------------------------------------------
_FUNCTION( pxp.fn_convertEvent ) ( p_eventType _INT)
_RETURN _INT 
_IMPLEMENT_AS
begin
    if ( p_eventType = _EventType_OBJECT_CREATION ) then
        return _TrackingEvent_CREATE;
    elsif ( p_eventType = _EventType_OBJECT_DELETE ) then
        return _TrackingEvent_DELETE;
    end if;
    return _TrackingEvent_MODIFY;
end
_IMPLEMENT_END;

-----------------------------------------------------------------
-- pxp.fn_trackObject insertion to track object
-----------------------------------------------------------------
_FUNCTION( pxp.fn_trackObject )( p_objectIID _IID, p_classifierIID _IID, p_userIID _LONG, p_event _INT, p_trackingData _JSON, p_pxon _BLOB, p_posted _LONG, p_transactionId _STRING)
_RETURN _IID
_IMPLEMENT_AS
    vObjectid     _VARCHAR;
    vCatalogCode  _VARCHAR;
    vOrganizationCode _VARCHAR;
    vTrackIID     _IID;
begin
    /* select the object ID from the topParentIID or by default from the object IID */
    begin
        select bp.baseEntityID, be.catalogCode, be.organizationCode into vObjectid, vCatalogCode, vOrganizationCode
        from pxp.baseEntity be 
        join pxp.baseEntity bp on bp.baseEntityIID = coalesce(be.topParentIID, be.baseEntityIID)
        where be.baseEntityIID = p_objectIID;
    exception
        when no_data_found then vObjectid := null;
    end;
    vTrackIID := _NEXTVAL(pxp.seqObjectTrackIID);
    if vObjectid is not null then
        insert into pxp.objectTracking
            values (p_transactionId, vTrackIID, p_objectIID, vObjectid,  vOrganizationCode, vCatalogCode, p_classifierIID, p_userIID, p_posted, p_event, p_trackingData, p_pxon );
    else
        insert into pxp.objectTracking
            values (p_transactionId, vTrackIID, p_objectIID, '', '', '', p_classifierIID, p_userIID, p_posted, p_event, p_trackingData, p_pxon );
    end if;
    return vTrackIID;
end
_IMPLEMENT_END;

-----------------------------------------------------------------
-- pxp.fn_postTrackingEvent insertion to track object and event queue tables
-----------------------------------------------------------------
_FUNCTION( pxp.fn_postTrackingEvent )( p_topic _INT, p_eventType _INT, p_classifierIID _IID, p_userIID _LONG, p_objectIID _IID, p_trackingData _JSON,
p_pxon _BLOB, p_posted _LONG, p_localeId _STRING, p_transactionId _STRING)
_RETURN _IID
_IMPLEMENT_AS
    vTrackIID   _IID;
    vEvent      _INT;
begin
    vEvent := pxp.fn_convertEvent( p_eventType);
    vTrackIID := pxp.fn_trackObject( p_objectIID, p_classifierIID, p_userIID, vEvent, p_trackingData, p_pxon, p_posted, p_transactionId);
    insert into pxp.eventqueue
        values( vTrackIID, p_topic, p_eventType, p_posted, 0, 0, p_localeId);
     return vTrackIID;
end
_IMPLEMENT_END;

-----------------------------------------------------------------
-- pxp.sp_postEventRedirection insertion of an event directed on existing trackIID
-----------------------------------------------------------------
_PROCEDURE( pxp.sp_postEventRedirection )( p_topic _INT, p_eventType _INT, p_trackIID _LONG, p_posted _LONG, p_localeId _STRING)
_IMPLEMENT_AS
begin
    insert into pxp.eventqueue
        values( p_trackIID, p_topic, p_eventType, p_posted, 0, 0, p_localeId);
end
_IMPLEMENT_END;

-----------------------------------------------------------------
-- pxp.sp_postSideEffectEvents insertion of an event by side effect
-----------------------------------------------------------------
_PROCEDURE( pxp.sp_postSideEffectEvents ) ( p_topic _INT, p_eventType _INT, p_classifierIID _IID, p_userIID _IID, p_objectIIDs _IIDARRAY, p_trackMsgs _VARARRAY, p_posted _LONG)
_IMPLEMENT_AS
    vObjectIIDIdx _INT;
    vObjectIID    _IID;
    vTrackData    _VARCHAR;
    vTrackIID     _IID;
begin
    for vObjectIIDIdx in 1 .. _COUNT(p_objectIIDs) loop
        vObjectIID := _ARRAY( p_objectIIDs, vObjectIIDIdx);
        vTrackData := _ARRAY( p_trackMsgs, vObjectIIDIdx);
        vTrackIID := pxp.fn_postTrackingEvent( p_topic, p_eventType, p_classifierIID, p_userIID, vObjectIID, vTrackData::json, null, p_posted);
    end loop;
end
_IMPLEMENT_END;

-----------------------------------------------------------------
-- pxp.sp_flagEventQueue flag events
-----------------------------------------------------------------
_PROCEDURE( pxp.sp_flagEventQueue )( p_timestamp _LONG, p_EventIIDs _IIDARRAY)
_IMPLEMENT_AS
begin
    /* tested and working in PG+ORA: */
    update pxp.eventQueue set flagged = p_timestamp 
        where flagged = 0 and trackIID in 
#ifdef PGSQL11
            ( select unnest(p_EventIIDs) );
#elif ORACLE12C
            ( select * from table(p_EventIIDs) );
#endif
end
_IMPLEMENT_END;

-----------------------------------------------------------------
-- pxp.sp_submitBackgroundprocess
-----------------------------------------------------------------
_FUNCTION( pxp.fn_submitBackgroundprocess ) ( p_userIID _IID, p_service varchar, p_callbackURI varchar, p_userPriority _INT, 
    p_entryData _JSON, p_created _LONG )
_RETURN _IID
_IMPLEMENT_AS
    vjobIID  _IID;
begin
    vjobIID := _NEXTVAL(pxp.seqObjectTrackIID);
    insert into pxp.backgroundprocess
        values( vjobIID, p_userIID, p_service, _BGPStatus_PENDING, p_callbackURI,
            p_userPriority, p_entryData, null, null, null, null, 0, p_created, 0, 0);
    return vjobIID;
end
_IMPLEMENT_END;

-----------------------------------------------------------------
-- pxp.sp_updateBackgroundprocess
-----------------------------------------------------------------
_PROCEDURE( pxp.sp_updateBackgroundprocess ) ( p_iid _IID, p_status _INT, p_runtimeData _JSON, p_progressData _JSON, 
	p_summaryData _JSON, p_logData _BLOB, p_runningTime _LONG, p_started _LONG, p_ended _LONG)
_IMPLEMENT_AS
begin
    update pxp.backgroundprocess set
        status = p_status, runtimedata = p_runtimeData, progressData = p_progressData, summaryData = p_summaryData,
        logData = p_logData, runningTime = p_runningTime, started = p_started, ended = p_ended
    where JOBIID = p_iid;
end
_IMPLEMENT_END;

-----------------------------------------------------------------
-- pxp.sp_createOjectRevision
-----------------------------------------------------------------
_FUNCTION( pxp.fn_createObjectRevision ) ( p_ObjectIID _IID, p_classifierIID _IID, p_created _LONG,
                                            p_comment varchar, p_trackIID _IID, p_timeline _JSON, p_archive _BLOB, p_isArchived _BOOLEAN, p_assetobjectkey varchar)
_RETURN _INT
_IMPLEMENT_AS
    vNewRevisionNo     _INT;
begin
    begin
        select max(revisionNo) + 1 into vNewRevisionNo from pxp.objectRevision 
        where objectIID =  p_ObjectIID and revisionNo >= 0;
    exception
        when no_data_found then vNewRevisionNo := null;
    end;
    if ( vNewRevisionNo is null ) then
        vNewRevisionNo := 0;
    end if;
    insert into pxp.ObjectRevision (objectIID, classifierIID, created, revisionNo, revisionComment, trackIID, revisionTimeline, objectArchive, isArchived, assetObjectkey)
        values ( p_ObjectIID, p_classifierIID, p_created, vNewRevisionNo, p_comment, p_trackIID, p_timeline, p_archive, p_isArchived, p_assetobjectkey);
    return vNewRevisionNo;
end
_IMPLEMENT_END;

#ifdef PGSQL11
-- end of PGSQL Tracking procedures
#elif ORACLE12C
-- end of ORACLE Tracking procedures
#endif
