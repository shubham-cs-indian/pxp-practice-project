/**
 * Author:  vallee
 * Created: Sep 25, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

#undef _NB_PARTITIONS
#define _NB_PARTITIONS 4

_TABLE( pxp.Task ) (
	taskIID				_IID primary key,
	taskCode			_VARCHAR not null,
	entityIID			_IID not null,
	propertyIID			_IID,
	statusCode			_VARCHAR,
	priorityCode		_VARCHAR,
	parentTaskIID		_IID,
	taskName			_VARCHAR,
	createdTime			_LONG,
	startDate			_LONG,
	dueDate				_LONG,
	overdueDate			_LONG,
	wfCreated			_BOOL(wfCreated),
	wfProcessID			_VARCHAR,
	wfTaskInstanceID	_VARCHAR,
	wfProcessInstanceID _VARCHAR,
	attachments         _IIDARRAY,
	description         _VARCHAR,
    position            _JSONBCOL(position),
	comments			_JSONBCOL(comments),
	formFields          _JSONBCOL(formFields)
) 
#ifdef ORACLE12C
nested table attachments store as attachmentsTAB 
#endif
_PARTITION_BY_HASH(entityIID);
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.Task);

_CREATE_INDEX1(Task, entityIID);

_TABLE( pxp.TaskRoleLink ) (
	taskIID				_LONG,
	roleCode			_VARCHAR,
	RCI					_INT,
	primary key( taskIID, roleCode, RCI)
) _PARTITION_BY_HASH(taskIID);
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.TaskRoleLink);

_CREATE_INDEX1(TaskRoleLink, taskIID);

_TABLE( pxp.TaskUserLink ) (
	taskIID				_LONG,
	userIID				_LONG,
	RACIVS				_INT,
	primary key( taskIID, userIID, RACIVS)
) _PARTITION_BY_HASH(taskIID);
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.TaskUserLink);

_CREATE_INDEX1(TaskUserLink, taskIID);


