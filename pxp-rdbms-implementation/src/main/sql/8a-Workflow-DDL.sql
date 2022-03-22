/**
 * TABLE DEFINITIONs of Workflow and Task DATA
 * Author:  Mayuri Wankhade
 * Created: Mar 05, 2020
 */

#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

_CREATE_SEQUENCE( pxp.workflowTaskStatusSequence, 1);

_TABLE( pxp.workflowTaskStatus ) (
	    instanceIID         _IID default _NEXTVAL(pxp.workflowTaskStatusSequence) primary key, /* internal key: unique */
        processInstanceID   _INT not null, 
        definitionID        _VARCHAR default null,
        taskInstanceId      _VARCHAR default null,
        parentIID           _IID,
        jobID               _INT default null,
        status              _INT,
        label               _VARCHAR not null,
        endpointId          _VARCHAR default null,
        physicalCatalogId   _VARCHAR default null,
        success             _JSONBCOL(success),
        error               _JSONBCOL(success),
        information         _JSONBCOL(success),
        summary             _JSONBCOL(success),
        warning             _JSONBCOL(success),
        createUserID        _IID not null,
        startTime           _LONG not null,
        endTime             _LONG default null)