/**
 * Author:  vallee
 * Created: Jun 4, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

-- pxp.TrackingIIDCount: portable implementation of exists
_FUNCTION( pxp.TrackingIIDCount )( pTrackingIID _IID )
_RETURN _INT
_IMPLEMENT_AS
    vExistingCount     _INT;
begin
    if ( pTrackingIID is null ) then
        return -1; -- for null value the check is not provided
    end if;
    select count(*) into vExistingCount from pxp.objectTracking where trackIID = pTrackingIID;
    exception
        when no_data_found then return 0;
    return vExistingCount;
end
_IMPLEMENT_END;

--
-- _FOREIGN_KEY(eventQueue, objectTracking, trackIID, trackIID);
--
#if FOREIGN_KEY_WITH_PARTITION
_FUNCTION( pxp.ObjectTracking_trackIID_fk )()
_RETURN trigger
_IMPLEMENT_AS
    vExistingIID    _IID;
begin
    vExistingIID := pxp.TrackingIIDCount( _NEW.trackingIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on trackingIID: %', _TO_VARCHAR(_NEW.trackingIID));
end
_IMPLEMENT_END;

_TRIGGER_DECLARE( EventQueue_trackingIID_fk, ObjectTracking_trackIID_fk, _TRIGGER_ACTION, pxp.EventQueue);
#endif

--
-- _FOREIGN_KEY(objectRevision, objectTracking, trackIID, trackIID);
--
#if FOREIGN_KEY_WITH_PARTITION
_TRIGGER_DECLARE( ObjectRevision_trackingIID_fk, ObjectTracking_trackIID_fk, _TRIGGER_ACTION, pxp.objectRevision);
#endif
