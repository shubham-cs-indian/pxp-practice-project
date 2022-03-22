/**
 * Author:  vallee
 * Created: Jun 4, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

-- user session with user details
_VIEW( pxp.userSessionWithUser ) as 
    select us.*, u.userName from pxp.usersession us
    join pxp.userConfig u on u.userIID = us.userIID;

-- object tracking with user details
_VIEW( pxp.objectTrackingWithUser ) as
    select ot.*, u.userName from pxp.objectTracking ot
    join pxp.userConfig u on u.userIID = ot.userIID;

-- event queue tracking with object tracking details
_VIEW( pxp.eventQueueWithData ) as
    select eq.topic, eq.eventType, eq.consumed, eq.flagged, eq.localeid, ot.*
    from pxp.eventQueue eq
    join pxp.objectTrackingWithUser ot on ot.trackIID = eq.trackIID;
 
-- last object tracking by user (based on sequential trackIID)
_VIEW( pxp.lastUserObjectTracking ) as
    select ot.*
    from pxp.Objecttracking ot
    join ( select objectIID, userIID, max(trackIID) as trackIID from pxp.Objecttracking group by objectIID, userIID ) last
    on last.objectIID = ot.objectIID and last.userIID = ot.userIID and last.trackIID = ot.trackIID;

-- object revision joined with objectTrackingWithUser in order to get full DTO information
_VIEW( pxp.objectrevisionfullcontent ) as
    select r.created, r.revisionNo, r.revisionComment, r.revisionTimeline, r.objectArchive, r.isarchived, ot.*
    from pxp.ObjectRevision r
    join pxp.objectTrackingWithUser ot on ot.trackIID = r.trackIID;
    
#ifdef PGSQL11
-- end of PGSQL Tracking views
#elif ORACLE12C
-- end of ORACLE Tracking views
#endif
