/**
 * Author:  vallee
 * Created: Sep 25, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

---------------------------------------------------------------------------------
-- Create task record
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_createTaskRecord )( p_baseentityiid _IID, p_taskcode _STRING, p_propertyiid _IID,
            p_statuscode _STRING, p_prioritycode _STRING, p_parenttaskiid _IID, p_taskname _STRING, p_createdtime _LONG,
            p_startdate _LONG, p_duedate _LONG, p_overduedate _LONG, p_wfcreated _BOOLEAN, p_wfprocessid _STRING,
            p_wftaskinstanceid _STRING, p_wfprocessinstanceid _STRING, p_attachments _IIDARRAY, p_description _STRING, p_position _JSON, p_comments _JSON, p_formFields _JSON)
_RETURN _IID
_IMPLEMENT_AS
    vTaskIID               _IID;
begin
       vTaskIID := _NEXTVAL( pxp.seqIID);
        insert into pxp.task ( taskIID, taskCode, entityIID, propertyIID, statusCode, priorityCode, parentTaskIID,
                 taskName, createdTime, startDate, dueDate, overdueDate, wfCreated, wfProcessID, wfTaskInstanceID,
                 wfProcessInstanceID, attachments, description, position, comments, formFields)
        values ( vTaskIID, p_taskcode, p_baseentityiid, p_propertyiid, p_statuscode, p_prioritycode, p_parenttaskiid,
                 p_taskname, p_createdtime, p_startdate, p_duedate, p_overduedate, p_wfcreated, p_wfprocessid,
                 p_wftaskinstanceid, p_wfprocessinstanceid, p_attachments, p_description, p_position, p_comments, p_formFields);
    return vTaskIID;
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- update task record
---------------------------------------------------------------------------------
_PROCEDURE( pxp.fn_updateTaskRecord )( p_taskiid _IID,
            p_statuscode _STRING, p_prioritycode _STRING, p_taskname _STRING,
            p_startdate _LONG, p_duedate _LONG, p_overduedate _LONG, p_attachments _IIDARRAY, p_description _STRING, p_comments _JSON, p_formFields _JSON)
_IMPLEMENT_AS
    vTaskIID               _IID;
begin
      update pxp.task set statusCode = p_statuscode, priorityCode = p_prioritycode, taskName = p_taskname, 
      startDate = p_startdate, dueDate = p_duedate, overdueDate = p_overduedate, attachments = p_attachments, description = p_description ,comments = p_comments ,formFields = p_formFields 
      where taskiid = p_taskiid ;
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create users and roles for task record
---------------------------------------------------------------------------------
_PROCEDURE( pxp.fn_createUsersAndRolesForTask )( p_taskIID _IID, p_userRACIVSs _INTARRAY, p_userIIDs _IIDARRAY,
            p_roleRCIs _INTARRAY, p_roleCodes _VARARRAY)
_IMPLEMENT_AS
    vuserRACIVSIdx	_INT;
    vroleRCIIdx  	_INT;
    vuserRACIVS     _INT;
    vuserIID        _IID;
    vroleRCI        _INT;
    vroleCode       _VARCHAR;
begin
       for vuserRACIVSIdx IN 1 .. _COUNT( p_userRACIVSs) loop
        vuserRACIVS := _ARRAY( p_userRACIVSs, vuserRACIVSIdx);
        vuserIID := _ARRAY( p_userIIDs, vuserRACIVSIdx);
        insert into pxp.TaskUserLink
            (taskIID, userIID, RACIVS)
        values( p_taskIID, vuserIID, vuserRACIVS);
       end loop;
       
       for vroleRCIIdx IN 1 .. _COUNT( p_roleRCIs) loop
        vroleRCI := _ARRAY( p_roleRCIs, vroleRCIIdx);
        vroleCode := _ARRAY( p_roleCodes, vroleRCIIdx);
        insert into pxp.TaskRoleLink
            (taskIID, roleCode, RCI)
        values( p_taskIID, vroleCode, vroleRCI);
       end loop;
   
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- update users and roles for task record
---------------------------------------------------------------------------------
_PROCEDURE( pxp.fn_updateUsersAndRolesForTask )( p_taskIID _IID, p_userRACIVSs _INTARRAY, p_userIIDs _IIDARRAY,
            p_roleRCIs _INTARRAY, p_roleCodes _VARARRAY)
_IMPLEMENT_AS
 
begin
    delete from pxp.TaskUserLink where taskIID = p_taskIID;
    delete from pxp.TaskRoleLink where taskIID = p_taskIID;
   _CALL pxp.fn_createUsersAndRolesForTask( p_taskIID, p_userRACIVSs, p_userIIDs, p_roleRCIs, p_roleCodes);
end 
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- delete users,roles and task record from baseentity
---------------------------------------------------------------------------------
_PROCEDURE( pxp.fn_deleteTaskRecord )( p_taskIID _IID)
_IMPLEMENT_AS
 
begin
    delete from pxp.TaskUserLink where taskIID = p_taskIID;
    delete from pxp.TaskRoleLink where taskIID = p_taskIID;
    delete from pxp.Task where taskIID = p_taskIID;
end 
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- delete users,roles and task record from baseentities
---------------------------------------------------------------------------------
_PROCEDURE( pxp.fn_deleteTaskRecords )( p_taskIIDs bigint[])
_IMPLEMENT_AS
 
begin
    for index in 1 .. coalesce( array_upper(p_taskIIDs, 1), 0)
    loop
    _CALL pxp.fn_deleteTaskRecord(p_taskIIDs[index]);
    end loop;
end 
_IMPLEMENT_END;
#ifdef PGSQL11
-- end of PGSQL task Records Procedure/Function declarations
#elif ORACLE12C
-- end of ORACLE task Records Procedure/Function declarations
#endif