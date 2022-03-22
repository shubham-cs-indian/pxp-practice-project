/**
 * Author:  Sumit Bhingardive
 * Created: March 5, 2020
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

--  Audit Log table
/*  -----------------------
    The audit log table is repesenting the information of configuartion entities created by user. This table is used to track the information such as when the entity is created, who created the entity etc.   
*/
_CREATE_SEQUENCE( pxp.auditLogUniqueIID, 1);

_TABLE( pxp.auditlog ) (
	activityIID		_IID default _NEXTVAL(pxp.auditLogUniqueIID) primary key,
	activity        _VARCHAR,
	date			_LONG,
	entityType		_SHORT,
	element         _SHORT,
	elementCode     _VARCHAR,
	elementName		_VARCHAR,
	elementType		_VARCHAR,
	ipAddress		_VARCHAR,
	userName		_VARCHAR
)