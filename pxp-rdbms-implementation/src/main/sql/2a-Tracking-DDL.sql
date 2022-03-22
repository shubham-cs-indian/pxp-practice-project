/**
 * Author:  vallee
 * Created: Mar 16, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

_TABLE( pxp.userSession ) (
	sessionID   _VARCHAR primary key,
    hostName    _VARCHAR not null,
	userIID     _IID not null,
	loginTime   _TIMESTAMP not null,
	logoutTime  _TIMESTAMP,
	logoutType  _SHORT,
	remarks     _VARCHAR
);
_FOREIGN_KEY(userSession, userConfig, userIID, userIID);

_CREATE_INDEX1( userSession, userIID);

#undef _NB_PARTITIONS
#define _NB_PARTITIONS 16 /* when paritioning is active, 16 partitions are advised here */
--
-- Object tracking
--
_TABLE( pxp.objectTracking ) (
	    transactionID       _VARCHAR,
        trackIID     _IID default _NEXTVAL(pxp.seqObjectTrackIID),
        objectIID    _IID not null,
        objectID     _VARCHAR not null,
        organizationCode _VARCHAR ,
        catalogCode  _VARCHAR not null,
        classifierIID    _IID not null,
	    userIID      _IID not null,
	    posted       _LONG not null,
        event        _SHORT not null,
        timelineData _JSONCOL(timelineData), /* contains all information for the revision timeline */
        pxonDelta    _BLOB, /* compressed PXON delta of changes */
        primary key (trackIID) /* PGSQL: components of partitioning must be part of the PK */
) _PARTITION_BY_HASH(trackIID);
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.objectTracking);

_CREATE_INDEX1(objectTracking, userIID);
_CREATE_INDEX1(objectTracking, objectIID);
_CREATE_INDEX2(objectTracking, objectID, catalogCode);
_FOREIGN_KEY(objectTracking, userConfig, userIID, userIID);

--
-- Object Event Queue
--
_TABLE( pxp.eventQueue ) (
        trackIID            _IID,
        topic               _SHORT not null,
        eventType           _SHORT not null,
	    posted              _LONG not null, /* replicated here for indexing */
        consumed            _LONG default 0,
        flagged             _LONG default 0,
	    localeId	        _VARCHAR,
        primary key (trackIID, eventType)
);

_CREATE_INDEX2(eventQueue, eventType, posted);
#if FOREIGN_KEY_WITHOUT_PARTITION
_FOREIGN_KEY(eventQueue, objectTracking, trackIID, trackIID);
#endif

--
-- Background Process
--
_TABLE( pxp.backgroundProcess ) (
        jobIID              _IID primary key,
        userIID             _IID not null,
        service             _VARCHAR not null,
        status              _SHORT not null,
        callbackURI         _VARCHAR,
        userPriority        _SHORT not null,
        entryData           _JSONCOL(entryData), /* Entry parameters */
        runtimeData         _JSONCOL(runtimeData), /* Runtime context (used in case of interruption/restart) */
        progressData        _JSONCOL(progressData), /* Progress information */
        summaryData         _JSONCOL(summaryData), /* Summary information */
        logData             _BLOB, /* compacted execution logs */
        runningTime         _LONG not null, /* time spent in running */
        created             _LONG not null,
        started             _LONG default 0,
        ended               _LONG default 0
);
_CREATE_INDEX2( backgroundProcess, service, created);
_FOREIGN_KEY(backgroundProcess, userConfig, userIID, userIID);

--
-- Object Revision
--
_TABLE( pxp.objectRevision )  (
        trackIID             _IID not null, /* the last tracking IID considered by this revision */ 
        objectIID            _IID not null, /* concerned base entity */
        classifierIID        _IID not null, /* classifieriid of base entity */
	    created              _LONG not null,
        revisionNo           _INT not null,
        revisionComment      _VARCHAR,
        revisionTimeline     _JSONCOL(revisionTimeline), /* aggregated timeline from object tracking */
        objectArchive        _BLOB, /* compacted ZIP PXON content of the object in that revision */
        isArchived			 _BOOLEAN,
        assetObjectKey       _VARCHAR,
        primary key ( objectIID, trackIID)
) _PARTITION_BY_HASH( objectIID);
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.objectRevision);

_CREATE_INDEX1(objectRevision, classifierIID);
_CREATE_UNIQUE_INDEX2( objectRevision, objectIID, revisionNo);
#if FOREIGN_KEY_WITHOUT_PARTITION
_FOREIGN_KEY(objectRevision, objectTracking, trackIID, trackIID);
#endif

--
-- Base Entity Archive
--
_TABLE( pxp.BaseEntityArchive )  (
        entityIID            _IID primary key, /* concerned base entity */
        objectArchive        _BLOB, /* compacted ZIP PXON content of the object in that revision */
		assetObjectKey       _VARCHAR
);
_CREATE_INDEX1(BaseEntityArchive, entityIID);



#ifdef PGSQL11
-- end of PGSQL Tracking declarations
#elif ORACLE12C
-- end of ORACLE Tracking declarations
#endif
