/**
 * Author:  vallee
 * Created: Mar 25, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"
#include "./include/coupling-defs.sql"

---------------------------------------------------------------------------------
-- Create a coupled record
---------------------------------------------------------------------------------


_FUNCTION( pxp.fn_CreateCoupling )(
	p_entityiid _IID,
	p_propertyiid _IID,
	p_status _INT,
	p_couplingbehavior _INT,
	p_couplingtype _INT,
	p_masternodeid _STRING,
	p_coupling _STRING,
	p_masterentityiid _IID,
	p_masterpropertyiid _IID,
	p_couplingsourceiid _IID,
	p_localeiid _IID)
	
_RETURN _INT
_IMPLEMENT_AS
 vMasterEntityiid _IID;
 vMasterValueRecord _RECORD(pxp.allvaluerecord);
 vTargetValueRecord _RECORD(pxp.allvaluerecord);
 vMasterTagRecord _RECORD(pxp.alltagsrecord);
 vTargetTagRecord _RECORD(pxp.alltagsrecord);
 vPropertyType _INT;
 vValueiid _INT;
 vLocaleid _STRING;
 vParentLocaleid _STRING;
 vIsCoupledCreated _INT := 0;
begin
	
	select propertytype INTO vPropertyType from pxp.propertyconfig where propertyiid = p_propertyiid;
  if(vPropertyType = _PropertyType_TAG)THEN
  
 	select * INTO vTargetTagRecord from pxp.alltagsrecord where entityiid = p_entityiid and
   	propertyiid = p_propertyiid;
   	
   	select * INTO vMasterTagRecord from pxp.alltagsrecord where entityiid = p_masterentityiid and
   	propertyiid = p_propertyiid;
  	
  ELSE
  
   	select languagecode into vLocaleid from pxp.languageconfig where languageiid = p_localeiid;
   	
   	if(vLocaleid is null) then
		select * INTO vTargetValueRecord from pxp.allvaluerecord where entityiid = p_entityiid and
    	propertyiid = p_propertyiid and localeid is null;
    	
    	select * INTO vMasterValueRecord from pxp.allvaluerecord where entityiid = p_masterentityiid and
     	propertyiid = p_propertyiid and localeid is null;
	ELSE 
	
    	select * INTO vTargetValueRecord from pxp.allvaluerecord where entityiid = p_entityiid and
    	propertyiid = p_propertyiid and localeid = vLocaleid;
    	
    	
    	if(p_couplingtype = _CouplingType_LANG_INHERITANCE) THEN
	 	
			select languagecode into vParentLocaleid from pxp.languageconfig where languageiid = p_couplingsourceiid;
		
	 		select * INTO vMasterValueRecord from pxp.allvaluerecord where entityiid = p_masterentityiid and
     		propertyiid = p_propertyiid and localeid = vParentLocaleid;
     		
	 	ELSE
	 
	 		select * INTO vMasterValueRecord from pxp.allvaluerecord where entityiid = p_masterentityiid and
     		propertyiid = p_propertyiid and localeid = vLocaleid;
     		
		END IF;
	END IF;
    	
  END IF;
 
 select masterentityiid into vMasterEntityiid from pxp.coupledrecord where propertyiid = p_propertyiid
 and entityiid = p_entityiid and localeiid = p_localeiid;
  
 if(vMasterEntityiid is not null)THEN
  update pxp.conflictingvalues set recordstatus = p_status where targetentityiid = p_entityiid and propertyiid = p_propertyiid
  and sourceentityiid = vMasterEntityiid and localeiid = p_localeiid;
  INSERT INTO pxp.conflictingvalues(
 	targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid)
 	VALUES (p_entityiid, p_propertyiid, p_masterentityiid, p_couplingbehavior, p_couplingsourceiid, p_status, p_couplingtype, p_localeiid);
  
 ELSE
 	
 	select sourceentityiid into vMasterEntityiid from pxp.conflictingvalues where propertyiid = p_propertyiid
 	and targetentityiid = p_entityiid and localeiid = p_localeiid;
 	
 	if(vMasterEntityiid is null)THEN
 		if(vPropertyType = _PropertyType_TAG)THEN
			if(vTargetTagRecord is null)THEN
     			p_status := _RecordStatus_COUPLED;
   			ELSE
     			IF(vTargetTagRecord.usrtags = vMasterTagRecord.usrtags) THEN
      				p_status := _RecordStatus_COUPLED;
      				delete from pxp.tagsrecord where entityiid = p_entityiid and
       				propertyiid = p_propertyiid;
     			END IF;
   			END IF;
		ELSE
			if(vTargetValueRecord is null)THEN
    			p_status := _RecordStatus_COUPLED;
   	 		ELSE IF(vTargetValueRecord.value = vMasterValueRecord.value) THEN
  				p_status := _RecordStatus_COUPLED;
  				if(vLocaleid is null)THEN
   					delete from pxp.valuerecord where entityiid = p_entityiid and
       				propertyiid = p_propertyiid and localeid is null;
  				ELSE
    				delete from pxp.valuerecord where entityiid = p_entityiid and
        			propertyiid = p_propertyiid and localeid = vLocaleid;
  				END IF;
 			END IF;
   		END IF;
	END IF;
END IF;
	
 	
	if(p_status = _RecordStatus_COUPLED ) THEN
		if(p_couplingbehavior != _CouplingBehavior_TIGHTLY) THEN
			INSERT INTO pxp.coupledrecord(
 				propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
  				masterentityiid, masterpropertyiid, localeiid)
 			VALUES (p_propertyiid, p_entityiid, p_status, p_couplingbehavior, p_couplingType,p_masternodeid,
   				'', p_masterentityiid, p_propertyiid, p_localeiid);
   		END IF;
   		vIsCoupledCreated := 1;
	END IF;
    if(p_couplingbehavior = _CouplingBehavior_TIGHTLY) THEN 
    		update pxp.conflictingvalues set recordstatus = _RecordStatus_NOTIFIED where targetentityiid = p_entityiid and propertyiid = p_propertyiid
    			and localeiid = p_localeiid;
    END IF;
 	INSERT INTO pxp.conflictingvalues(
 	targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid)
 	VALUES (p_entityiid, p_propertyiid, p_masterentityiid, p_couplingbehavior, p_couplingsourceiid, p_status, p_couplingtype, p_localeiid);
 END IF;
 
 return vIsCoupledCreated;
end
_IMPLEMENT_END;



---------------------------------------------------------------------------------
-- Update target coupled records after update
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_ResolveCouplingTargets )( p_masterNodeID _STRING)
_IMPLEMENT_AS
begin
	-- update tightly coupling to notification
	update pxp.CoupledRecord set recordstatus = _RecordStatus_NOTIFIED
		where masternodeid = p_masterNodeID 
		and couplingType in _COUPLING_WITH_NOTIFICATION
		and recordStatus <> _RecordStatus_NOTIFIED;

end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Update a coupled record after update
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_ResolveCoupling )( p_entityIID _IID, p_propertyiid _IID)
_IMPLEMENT_AS
begin
	-- update tightly coupling to notification
	update pxp.CoupledRecord set recordstatus = _RecordStatus_NOTIFIED
		where entityIID = p_entityIID and propertyIID =  p_propertyiid
		and couplingType in _COUPLING_WITH_NOTIFICATION
		and recordStatus <> _RecordStatus_NOTIFIED;
end
_IMPLEMENT_END;


_PROCEDURE( pxp.sp_resolvedconflicts )(
	p_sourceentityiid _IID,
	p_targetentityiid _IID,
	p_couplingtype _INT,
	p_couplingsourcetype _INT,
	p_couplingsourceiid _IID,
	p_propertyiid _IID,
	p_masternodeid _STRING,
	p_lacaleiid _IID)

_IMPLEMENT_AS
 p_languagecode _STRING;
begin
    update pxp.conflictingvalues set recordstatus = _RecordStatus_FORKED where targetentityiid = p_targetEntityiid and
 	propertyiid = p_propertyiid and recordstatus in (_RecordStatus_NOTIFIED, _RecordStatus_COUPLED)  and localeiid = p_lacaleiid;
 	
	update pxp.conflictingvalues set recordstatus = _RecordStatus_COUPLED where targetentityiid = p_targetEntityiid and
 	sourceentityiid = p_sourceEntityiid and propertyiid = p_propertyiid
 	and couplingsourceiid = p_couplingsourceiid and localeiid = p_lacaleiid;
 	
    if(p_couplingtype != _CouplingBehavior_TIGHTLY) then
	    DELETE FROM pxp.coupledrecord where propertyiid = p_propertyiid and entityiid = p_targetEntityiid and localeiid = p_lacaleiid;
		INSERT INTO pxp.coupledrecord(
 			propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,
  			masterentityiid, masterpropertyiid, localeiid)
 		VALUES (p_propertyiid, p_targetEntityiid, _RecordStatus_COUPLED, p_couplingType, p_couplingSourceType, p_masternodeid,
   			'', p_sourceEntityiid, p_propertyiid, p_lacaleiid);
   
   END IF;
	select languagecode into p_languagecode from pxp.languageconfig where  languageiid = p_lacaleiid;
 
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- coupling type = Tightly coupling by inheritance/relationship or Dynamic default value
-- the record content does not equal to the source record content (=> propertyRecordIID differ)
---------------------------------------------------------------------------------


_FUNCTION( pxp.fn_loadNotificationStatus )(
	p_entityiid _IID,
	p_propertyiids _IIDARRAY)
RETURNS _IIDARRAY
_IMPLEMENT_AS

    vPropertyIID _IID;
    vPropertyIIDIdx _INT;
    vNotificationIIDs _IIDARRAY;
    vRecordStatus _INT;
begin
    for vPropertyIIDIdx IN 1 .. _COUNT( p_propertyIIDs)
    loop
        vPropertyIID := _ARRAY( p_propertyIIDs, vPropertyIIDIdx);
  		select recordStatus into vRecordStatus from pxp.conflictingvalues where targetentityIID = p_entityIID 
  		and propertyIID = vPropertyIID and recordStatus <> _RecordStatus_FORKED;
        if ( vRecordStatus = _RecordStatus_NOTIFIED OR vRecordStatus = _RecordStatus_COUPLED)
        then
            _ARRAY_APPEND(vNotificationIIDs, vPropertyIID);
        end if;
    end loop;
    return vNotificationIIDs;
end
_IMPLEMENT_END;

_PROCEDURE( pxp.sp_updateSourceCoupledRecord )(
	p_targetentityiid _IID,
	p_sourceentityiid _IID,
	p_propertyiid _IID,
	p_couplingsourceiid _IID,
	p_localeiid _IID)
_IMPLEMENT_AS

 vSourceValueRecord _RECORD(pxp.valuerecord);
 vSourceTagRecord _RECORD(pxp.tagsrecord);
 vValueiid _INT;
 vPropertyType _INT;

begin
  update pxp.conflictingvalues set recordstatus = _RecordStatus_NOTIFIED where targetentityiid = p_targetentityiid and
  propertyiid = p_propertyiid  and localeiid = p_localeiid;
  update pxp.conflictingvalues set recordstatus = _RecordStatus_NOTIFIED where sourceentityiid = p_sourceentityiid and
  propertyiid = p_propertyiid and couplingsourceiid = p_couplingsourceiid and localeiid = p_localeiid;
  delete from pxp.coupledrecord where entityiid = p_targetentityiid and
  propertyiid = p_propertyiid and localeiid = p_localeiid; 
  
end
_IMPLEMENT_END;


_PROCEDURE( pxp.sp_updateSourceCoupledRecordForDynamicCoupling )(
	p_targetentityiid _IID,
	p_sourceentityiid _IID,
	p_propertyiid _IID,
	p_couplingsourceiid _IID,
	p_localeiid bigint)
	
_IMPLEMENT_AS

 vCount _INT;
 vSourceValueRecord _RECORD(pxp.valuerecord);
 vSourceTagRecord _RECORD(pxp.tagsrecord);
 vValueiid _INT;
 vPropertyType _INT;

begin
  Select count(*) into vCount from pxp.coupledrecord where entityiid = p_targetentityiid and masterentityiid = p_sourceentityiid and
  propertyiid = p_propertyiid and localeiid = p_localeiid;
	
  if(vCount != 1) THEN
    update pxp.conflictingvalues set recordstatus = _RecordStatus_NOTIFIED where sourceentityiid = p_sourceentityiid and
    propertyiid = p_propertyiid and couplingsourceiid = p_couplingsourceiid and localeiid = p_localeiid;
  	
    update pxp.conflictingvalues set recordstatus = _RecordStatus_NOTIFIED where targetentityiid = p_targetentityiid and
    propertyiid = p_propertyiid and recordstatus = _RecordStatus_COUPLED and localeiid = p_localeiid;
  	
    delete from pxp.coupledrecord where entityiid = p_targetentityiid and propertyiid = p_propertyiid and localeiid = p_localeiid;
  
    
  END IF;
end
_IMPLEMENT_END;


_FUNCTION ( pxp.fn_createConflictingValuesForDynamicCoupling)(
	p_entityiid _IID,
	p_propertyiid _IID,
	p_couplingbehavior _INT,
	p_couplingtype _INT,
	p_masternodeid _STRING,
	p_coupling _STRING,
	p_masterentityiid _IID,
	p_masterpropertyiid _IID,
	p_couplingsourceiid _IID,
	p_localeiid _IID)
	
_RETURN _INT
_IMPLEMENT_AS

 vCouplingTarget _RECORD(pxp.conflictingvalues);
 vSourceValueRecord _RECORD(pxp.valuerecord);
 vValueiid _INT;
 vCount _INT;
 vrecordStatus _INT;
 vIsCoupledCreated _INT;
begin
	
	update pxp.conflictingvalues set recordstatus = _RecordStatus_FORKED where targetentityiid = p_entityiid and propertyiid = p_propertyiid
	and couplingtype = _CouplingBehavior_TIGHTLY and recordstatus = _RecordStatus_COUPLED;
	
	update pxp.conflictingvalues set recordstatus = _RecordStatus_FORKED where targetentityiid = p_entityiid and propertyiid = p_propertyiid
	and couplingtype = _CouplingBehavior_TIGHTLY and recordstatus = _RecordStatus_NOTIFIED;
	
	Select count(*) into vCount from pxp.conflictingvalues where targetentityiid = p_entityiid and 
	propertyiid = p_propertyiid and couplingtype = _CouplingBehavior_DYNAMIC and recordstatus <> _RecordStatus_FORKED and localeiid = p_localeiid;
 	
	vrecordStatus = _RecordStatus_COUPLED;
 	if(vCount != 0) THEN
 		vrecordStatus = _RecordStatus_NOTIFIED;
 	END IF;
 		
 	if(p_masternodeid != '') THEN
 		vIsCoupledCreated := pxp.fn_CreateCoupling( p_entityiid, p_propertyiid, vrecordStatus, 
 		p_couplingbehavior, p_couplingtype, p_masternodeid, p_coupling, p_masterentityiid , 
 		p_masterpropertyiid, p_couplingsourceiid, p_localeiid);
 	END IF;
 	
 	if(vCount > 0) THEN
 		update pxp.conflictingvalues set recordstatus = _RecordStatus_NOTIFIED where targetentityiid = p_entityiid and
		propertyiid = p_masterpropertyiid  and localeiid = p_localeiid;
		update pxp.conflictingvalues set recordstatus = _RecordStatus_NOTIFIED where sourceentityiid = p_masterentityiid and
		propertyiid = p_masterpropertyiid and couplingsourceiid = p_couplingsourceiid and localeiid = p_localeiid;
 	ELSE
 		delete from pxp.valuerecord where entityiid = p_entityiid and propertyiid = p_propertyiid;
 		delete from pxp.tagsrecord where entityiid = p_entityiid and propertyiid = p_propertyiid;
 	END IF;
 	
 	return vIsCoupledCreated;
end
_IMPLEMENT_END;




_PROCEDURE( pxp.sp_createconflictingvaluesfortightlycoupling )(
	p_entityiid _IID,
	p_propertyiid _IID,
	p_status _INT,
	p_couplingbehavior _INT,
	p_couplingtype _INT,
	p_masternodeid _STRING,
	p_coupling _STRING,
	p_masterentityiid _IID,
	p_masterpropertyiid _IID,
	p_couplingsourceiid _IID,
	p_localeiid _IID)

_IMPLEMENT_AS
 vCount _INT;
begin
	
	select count(*) INTO vCount from pxp.conflictingvalues where targetentityiid = p_entityiid and
	propertyiid = p_propertyiid and couplingtype = _CouplingBehavior_DYNAMIC and localeiid = p_localeiid;
	
	
	if(vCount = 0) THEN
		PERFORM pxp.fn_CreateCoupling( p_entityiid, p_propertyiid, p_status, 
		p_couplingbehavior, p_couplingtype, p_masternodeid, p_coupling, p_masterentityiid , 
		p_masterpropertyiid, p_couplingsourceiid, p_localeiid);
		
	ELSE
		
		INSERT INTO pxp.conflictingvalues(
  		targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid)
  		VALUES (p_entityiid, p_propertyiid, p_masterentityiid, p_couplingbehavior, p_couplingsourceiid, _RecordStatus_FORKED, p_couplingtype, p_localeiid);
  	END IF;
 
end
_IMPLEMENT_END;




_PROCEDURE( pxp.sp_updateValueCoupledRecord )(
	
	p_entityiid _IID,
	p_propertyiid _IID,
	p_masterentityiid _IID,
	p_value _STRING,
	p_localeid _STRING,
	p_ashtml _TEXT,
	p_asnumber _FLOAT,
	p_unitsymbol _STRING,
	p_calculation _STRING)
	
_IMPLEMENT_AS
 vValueIID _IID;
 vLocaleIID _IID;
begin
	
	select languageiid into  vLocaleIID from pxp.languageconfig where languagecode = p_localeid;
	
	if vLocaleIID is null THEN
		vLocaleIID = 0;
	END IF;

 	delete from pxp.coupledrecord where entityiid = p_entityiid and propertyiid = p_propertyiid and localeiid = vLocaleIID;
 
  	vValueIID := nextval('pxp.seqIID');
    insert into pxp.ValueRecord values (p_propertyiid, p_entityiid, _RecordStatus_DIRECT,
  	vValueIID, p_localeid, null,
    p_value, p_ashtml, p_asnumber, p_unitsymbol, p_calculation);
	 
 	update pxp.conflictingvalues set recordstatus = 2 where targetentityiid = p_entityiid
 	and propertyiid = p_propertyiid and recordstatus = 4 and localeiid = vLocaleIID;
 
end
_IMPLEMENT_END;



_PROCEDURE( pxp.sp_updateTagCoupledRecord )(
	
	p_entityiid _IID,
	p_propertyiid _IID,
	p_masterentityiid _IID,
	p_value _STRING)

_IMPLEMENT_AS
 vSourceTagRecord _RECORD(pxp.tagsrecord);
begin
	
	delete from pxp.coupledrecord where entityiid = p_entityiid and propertyiid = p_propertyiid;
	
    INSERT INTO pxp.tagsrecord(
    propertyiid, entityiid, recordstatus, usrtags)
    VALUES (p_propertyiid, p_entityiid, _RecordStatus_DIRECT, p_usrTags :: p_value);
				
	update pxp.conflictingvalues set recordstatus = _RecordStatus_FORKED where targetentityiid = p_entityiid 
	and propertyiid = p_propertyiid and recordstatus = _RecordStatus_COUPLED;
 
end
_IMPLEMENT_END;



