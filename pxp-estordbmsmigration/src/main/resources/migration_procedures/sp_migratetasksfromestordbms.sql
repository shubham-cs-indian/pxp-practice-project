-- PROCEDUREa: staging.sp_migratetasksfromestordbms()

-- DROP PROCEDURE staging.sp_migratetasksfromestordbms();

CREATE OR REPLACE PROCEDURE staging.sp_migratetasksfromestordbms()
LANGUAGE 'plpgsql'

AS $BODY$
declare
begin
	INSERT INTO pxp.task(
		taskiid, taskcode, entityiid, propertyiid, statuscode, prioritycode, parenttaskiid, taskname, createdtime, 
		startdate, duedate, overduedate, wfcreated, attachments, description, position, comments)
	Select DISTINCT
		taskiid, taskcode, entityiid, null::bigint propertyiid, statuscode, prioritycode, null::bigint parenttaskiid, 
		name, createdtime, startdate, null::bigint duedate, null::bigint overduedate, wfcreated, 
		attachments, description, position, comments
 	from (
 		Select staging_task.taskiid,
 		--type
 		(Select code From staging.helper_endpointconfig Where cid = staging_task.type) taskcode, 
 		--contentiid 		
 		(Select baseentityiid from staging.baseentity innertemp where innertemp.id = staging_task.contentId ) entityiid,
 		--later propid
 		(Select propertyiid from pxp.propertyconfig where propertycode = staging_task.propertyid) propertyiid,
 			
 		--statuscode
 		(Select code From staging.helper_config Where cid = staging_task.status) statuscode,
 		--prioritycode
 		(Select code From staging.helper_config Where cid = staging_task.priority) prioritycode,
 		
 		--parentasktiid
 		(Select innerstaging_task.taskiid	
		 from staging.task innerstaging_task
		 Where staging_task.parenttaskid IS NOT NULL AND innerstaging_task.id = staging_task.parenttaskid) parenttaskiid,
 		staging_task.name,
 		staging_task.createdtime,
 		staging_task.startdate,
 		staging_task.duedate,
 		staging_task.overduedate,
 		staging_task.wfcreated,
 		staging_task.attachments,
 		staging_task.description,
 		staging_task.position,
 		staging_task.comments

 		from staging.task staging_task Where staging_task.wfcreated = false OR staging_task.wftaskinstanceid NOT IN (Select id from staging.inprogresstaskinstances)) task
		Where task.taskiid NOT IN(Select taskiid from pxp.task) AND task.taskcode IS NOT NULL;


		--Insert into taskuserlink
		INSERT INTO pxp.taskuserlink(
			taskiid, useriid, racivs)
		Select DISTINCT
			taskiid, useriid, racivs
		from(
			--taskiid
			 Select (Select taskiid from staging.task innertemp where innertemp.id = staging_taskuserlink.taskid ) taskiid,
			--useriid
			(Select useriid From staging.helper_userconfig Where userid = staging_taskuserlink.userid) useriid, 
			staging_taskuserlink.racivs
			from staging.taskuserlink staging_taskuserlink) taskuserlink
		Where taskuserlink.taskiid IN(Select taskiid from pxp.task) AND taskuserlink.useriid IS NOT NULL;


		--Insert into taskrolelink
		INSERT INTO pxp.taskrolelink(
			taskiid, rolecode, rci)
		Select DISTINCT
			taskiid, rolecode, racivs
		from(
			--taskiid
			Select (Select taskiid from staging.task innertemp where innertemp.id = staging_taskuserlink.taskid ) taskiid,
			--useriid
			(Select code From staging.helper_endpointconfig Where cid = staging_taskuserlink.roleid) rolecode,
			staging_taskuserlink.racivs
			from staging.taskrolelink staging_taskuserlink) taskrolelink
		Where taskrolelink.taskiid IN(Select taskiid from pxp.task) AND taskrolelink.rolecode IS NOT NULL;
end
$BODY$;
