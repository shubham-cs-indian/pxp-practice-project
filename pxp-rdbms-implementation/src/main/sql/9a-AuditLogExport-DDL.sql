/**
 * Author:  Faizan Siddique
 * Created: March 18, 2020
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

--  Audit Log Export Tracker table
/*  -----------------------
    The auditLogExportTracker table is used to track the information of auditlog files that user has exported. It stores information about the status of file exported along with some other information like username, filename, starttime etc.  
*/
_CREATE_SEQUENCE( pxp.auditLogExportSequence, 1);

_TABLE( pxp.auditlogexporttracker ) (
	exportIID	         	_IID     default _NEXTVAL(pxp.auditLogExportSequence) primary key,
	assetId                 _VARCHAR UNIQUE,
	fileName	            _VARCHAR not null,
	userName	            _VARCHAR not null,
	startTime               _LONG    not null,
	endTime                 _LONG,
	status		            _SHORT   not null
)