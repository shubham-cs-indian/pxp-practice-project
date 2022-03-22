
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"


-- This sql file was created as we wanted to use the below mentioned functions 
-- in views which are declared in 3av-Entities-DDL.sql 

---------------------------------------------------------------------------------
-- Get All value Record of master entityiid from coupled record
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getAllValueRecordFromCoupledRecords ) ()
_RETURN _CURSOR( record)
_IMPLEMENT_AS
    vCoupledRecord _RECORD(pxp.coupledRecord);
	vValueRecord _RECORD(pxp.valuerecord);
	vRecord record;
	vPropertyType _SHORT;
	vLocaleid _VARCHAR;
begin
	for vCoupledRecord in (select c.* from pxp.coupledrecord c 
		join pxp.propertyconfig pc on c.propertyiid = pc.propertyiid and pc.supertype = _SuperType_ATTRIBUTE) loop 
		
			select  * into vValueRecord from pxp.fn_getValueRecordFromCoupledRecord(vCoupledRecord);
			
			SELECT languagecode into vLocaleid
        	FROM pxp.languageconfig
        	WHERE languageiid = vCoupledRecord.localeiid;
    
    		if(vLocaleid is null) then
      			vLocaleid = vValueRecord.localeid;
    		END IF;
    
        	SELECT vCoupledRecord.propertyiid,
    		vCoupledRecord.entityiid,
    		vCoupledRecord.recordstatus,
    		vCoupledRecord.couplingtype,
    		vCoupledRecord.couplingbehavior,
    		vCoupledRecord.coupling,
    		vCoupledRecord.masterentityiid,
    		vCoupledRecord.masterpropertyiid,
    		vValueRecord.valueiid,
    		vLocaleid,
    		vValueRecord.contextualobjectiid,
    		vValueRecord.value,
    		vValueRecord.ashtml,
    		vValueRecord.asnumber,
    		vValueRecord.unitsymbol,
    		vValueRecord.calculation into vRecord;
        	return next vRecord;
	end loop;
end 
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Get Value record from coupled record
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getValueRecordFromCoupledRecord)( p_coupledRecord pxp.coupledRecord )
_RETURN pxp.valuerecord
_IMPLEMENT_AS
	vValueRecord _RECORD(pxp.valueRecord);
	vCoupledRecord _RECORD(pxp.coupledRecord);
	vConflictRecord _RECORD(pxp.conflictingValues);
 	vConflictingValue _RECORD(pxp.conflictingValues);
begin
	IF(p_coupledRecord.couplingtype <> _CouplingType_LANG_INHERITANCE) THEN
	
		select * into vValueRecord from pxp.valuerecord where entityiid = p_coupledRecord.masterentityiid and 
		propertyiid = p_coupledRecord.propertyiid
		and (localeid = ( SELECT languagecode
        FROM pxp.languageconfig
        WHERE languageiid = p_coupledRecord.localeiid) OR localeid IS NULL);
	
		if(vValueRecord is null) THEN
			select * into vCoupledRecord from pxp.coupledrecord where entityiid = p_coupledRecord.masterentityiid and 
			propertyiid = p_coupledRecord.propertyiid
			and localeiid = p_coupledRecord.localeiid;
		
			if(vCoupledRecord is not null )THEN
				select * into vValueRecord from pxp.fn_getValueRecordFromCoupledRecord(vCoupledRecord);
			END IF;
		END IF;
		
	ELSE
 	
		select * INTO vConflictRecord from pxp.conflictingvalues where sourceentityiid = p_coupledRecord.masterentityiid
		and targetentityiid = p_coupledRecord.entityiid and propertyiid = p_coupledRecord.propertyiid 
		and localeiid = p_coupledRecord.localeiid
		and recordstatus = _RecordStatus_COUPLED;
	
		select * into vValueRecord from pxp.valuerecord where entityiid = vConflictRecord.sourceentityiid and
		propertyiid = vConflictRecord.propertyiid 
 		and localeid = ( SELECT languagecode
        FROM pxp.languageconfig
        WHERE languageiid = vConflictRecord.couplingsourceiid);
		 
		if(vValueRecord is null) THEN
  			
			select * into vCoupledRecord from pxp.coupledrecord where entityiid = vConflictRecord.targetentityiid
	  		and masterentityiid = vConflictRecord.sourceentityiid and propertyiid = vConflictRecord.propertyiid 
	  		and localeiid = vConflictRecord.couplingsourceiid and couplingtype = _CouplingType_LANG_INHERITANCE;
       		
   			if(vCoupledRecord is not null )THEN
   				select * into vValueRecord from pxp.fn_getValueRecordFromCoupledRecord(vCoupledRecord);
 			END IF;
  		END IF;
 	END IF;
	
	return vValueRecord;

end 
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Get All Tags Record of master entityiid from coupled record
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getAllTagsRecordFromCoupledRecords ) ()
_RETURN _CURSOR( record)
_IMPLEMENT_AS
    vCoupledRecord _RECORD(pxp.coupledRecord);
	vTagsRecord _RECORD(pxp.tagsrecord);
	vRecord record;
	vPropertyType _SHORT;
begin
	for vCoupledRecord in (select c.* from pxp.coupledrecord c 
		join pxp.propertyconfig pc on c.propertyiid = pc.propertyiid and pc.supertype = _SuperType_TAGS) loop 
		
			select  * into vTagsRecord from pxp.fn_getTagsRecordFromCoupledRecord(vCoupledRecord);
			SELECT vCoupledRecord.propertyiid,
    		vCoupledRecord.entityiid,
    		vCoupledRecord.recordstatus,
    		vCoupledRecord.couplingtype,
    		vCoupledRecord.couplingbehavior,
    		vCoupledRecord.coupling,
    		vCoupledRecord.masterentityiid,
    		vCoupledRecord.masterpropertyiid,
    		vTagsRecord.usrtags
			into vRecord;
        	return next vRecord;
		
	end loop;
end 
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Get Tag record from coupled record
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getTagsRecordFromCoupledRecord)( p_coupledRecord pxp.coupledRecord )
_RETURN pxp.tagsrecord
_IMPLEMENT_AS
	vTagsRecord _RECORD(pxp.tagsrecord);
	vCoupledRecord _RECORD(pxp.coupledRecord);
begin
	select * into vTagsRecord from pxp.tagsrecord where entityiid = p_coupledRecord.masterentityiid and 
	propertyiid = p_coupledRecord.propertyiid;
	
	if(vTagsRecord is null) THEN
		select * into vCoupledRecord from pxp.coupledrecord where entityiid = p_coupledRecord.masterentityiid and 
		propertyiid = p_coupledRecord.propertyiid;
		
		if(vCoupledRecord is not null )THEN
			select * into vTagsRecord from pxp.fn_getTagsRecordFromCoupledRecord(vCoupledRecord);
		END IF;
	END IF;
	
	return vTagsRecord;

end 
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Create TYPE FOR RETURN OF COUPLING
---------------------------------------------------------------------------------
drop type if exists recordrow cascade;
create type recordrow as (propertyiid bigint, entityiid bigint,
recordstatus smallint, couplingtype smallint, couplingbehavior smallint,
coupling character varying, masterentityiid bigint, masterpropertyiid bigint,
valueiid bigint, localeid character varying, contextualobjectiid bigint,
value character varying, ashtml text,
asnumber numeric, unitsymbol character varying, calculation character varying);

drop type if exists tagrow cascade;
create type tagrow as (propertyiid bigint, entityiid bigint,
recordstatus smallint, couplingtype smallint, couplingbehavior smallint,
coupling character varying, masterentityiid bigint, masterpropertyiid bigint,
usrTags hstore);

---------------------------------------------------------------------------------
-- Get COUPLED VALUES WITH CHAINING
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getCoupledValuesWithChaining ) (p_baseEntityIIDs _IIDARRAY)
_RETURN _CURSOR( recordrow)
_IMPLEMENT_AS
    vCoupledRecord _RECORD(pxp.coupledRecord);
	vValueRecord _RECORD(pxp.valuerecord);
	vRecord record;
	vPropertyType _SHORT;
	vLocaleid _VARCHAR;
begin
	for vCoupledRecord in (select c.* from pxp.coupledrecord c
		join pxp.propertyconfig pc on c.propertyiid = pc.propertyiid and pc.supertype = _SuperType_ATTRIBUTE
		where c.entityIID = ANY(p_baseEntityIIDS)) loop

			select  * into vValueRecord from pxp.fn_getValueRecordFromCoupledRecord(vCoupledRecord);
			
			SELECT languagecode into vLocaleid
        	FROM pxp.languageconfig
        	WHERE languageiid = vCoupledRecord.localeiid;
    
    		if(vLocaleid is null) then
      			vLocaleid = vValueRecord.localeid;
    		END IF;
    		
        	SELECT vCoupledRecord.propertyiid,
    		vCoupledRecord.entityiid,
    		vCoupledRecord.recordstatus,
    		vCoupledRecord.couplingtype,
    		vCoupledRecord.couplingbehavior,
    		vCoupledRecord.coupling,
    		vCoupledRecord.masterentityiid,
    		vCoupledRecord.masterpropertyiid,
    		vValueRecord.valueiid,
    		vLocaleid,
    		vValueRecord.contextualobjectiid,
    		vValueRecord.value,
    		vValueRecord.ashtml,
    		vValueRecord.asnumber,
    		vValueRecord.unitsymbol,
    		vValueRecord.calculation into vRecord;
        	return next vRecord;
	end loop;
end
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Get COUPLED TAGS WITH CHAINING
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getCoupledTagsWithChaining ) (p_baseEntityIIDs _IIDARRAY)
_RETURN _CURSOR( tagrow)
_IMPLEMENT_AS
    vCoupledRecord _RECORD(pxp.coupledRecord);
	vTagsRecord _RECORD(pxp.tagsrecord);
	vRecord record;
	vPropertyType _SHORT;
begin
	for vCoupledRecord in (select c.* from pxp.coupledrecord c
		join pxp.propertyconfig pc on c.propertyiid = pc.propertyiid and pc.supertype = _SuperType_TAGS
		where c.entityIID = ANY(p_baseEntityIIDS)) loop

			select  * into vTagsRecord from pxp.fn_getTagsRecordFromCoupledRecord(vCoupledRecord);
			SELECT vCoupledRecord.propertyiid,
    		vCoupledRecord.entityiid,
    		vCoupledRecord.recordstatus,
    		vCoupledRecord.couplingtype,
    		vCoupledRecord.couplingbehavior,
    		vCoupledRecord.coupling,
    		vCoupledRecord.masterentityiid,
    		vCoupledRecord.masterpropertyiid,
    		vTagsRecord.usrtags
			into vRecord;
        	return next vRecord;
	end loop;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Get ALL records WITH EntityIID check
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getallvaluerecord ) (p_baseEntityIIDs _IIDARRAY)
_RETURN _CURSOR( recordrow)
_IMPLEMENT_AS
begin
    return query select v.propertyIID, v.entityIID, v.recordStatus,
    _CouplingType_NONE::smallint as couplingtype, _CouplingBehavior_NONE::smallint as couplingbehavior, null as coupling,
    _TO_LONG(null) as masterEntityIID, _TO_LONG(null) as masterPropertyIID,
    v.valueiid, v.localeid, v.contextualobjectiid, v.value, v.ashtml, v.asnumber, v.unitsymbol, v.calculation
    from pxp.valuerecord v where v.recordStatus = _RecordStatus_DIRECT AND entityIID = ANY(p_baseEntityIIDs)
union all
select * from pxp.fn_getCoupledValuesWithChaining(p_baseEntityIIDs);
end;
_IMPLEMENT_END;

---------------------------------------------------------------------------------
--Get ALL records WITH EntityIID check
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getalltagsrecord ) (p_baseEntityIIDs _IIDARRAY)
_RETURN _CURSOR( tagrow)
_IMPLEMENT_AS
begin
    return query select t.propertyIID, t.entityIID, t.recordStatus,
    _CouplingType_NONE::smallint as couplingtype, _CouplingBehavior_NONE::smallint as couplingbehavior, null as coupling,
    _TO_LONG(null) as masterEntityIID, _TO_LONG(null) as masterPropertyIID,
    t.usrTags
    from pxp.tagsrecord t where t.recordStatus = _RecordStatus_DIRECT AND entityIID = ANY(p_baseEntityIIDs)
union all
select * from pxp.fn_getCoupledTagsWithChaining(p_baseEntityIIDs);
end;
_IMPLEMENT_END;

---------------------------------------------------------------------------------
--Get selectective tag records with chaining
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getselectivetagrecords ) (p_baseEntityIIDs _IIDARRAY, p_propertyIIDs _IIDARRAY)
_RETURN _CURSOR( tagrow)
_IMPLEMENT_AS
begin
    return query select t.propertyIID, t.entityIID, t.recordStatus,
    _CouplingType_NONE::smallint as couplingtype, _CouplingBehavior_NONE::smallint as couplingbehavior, null as coupling,
    _TO_LONG(null) as masterEntityIID, _TO_LONG(null) as masterPropertyIID,
    t.usrTags
    from pxp.tagsrecord t where t.recordStatus = _RecordStatus_DIRECT AND entityIID = ANY(p_baseEntityIIDs) AND propertyIID = ANY(p_propertyIIDs)
union all
select * from pxp.fn_getcoupledtagswithchainingforselectiveproperties(p_baseEntityIIDs, p_propertyIIDs);
end;
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Get COUPLED TAGS WITH CHAINING FOR SELECTIVE PROPERTIES
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getcoupledtagswithchainingforselectiveproperties ) (p_baseEntityIIDs _IIDARRAY,p_propertyIIDs _IIDARRAY)
_RETURN _CURSOR( tagrow)
_IMPLEMENT_AS
    vCoupledRecord _RECORD(pxp.coupledRecord);
	vTagsRecord _RECORD(pxp.tagsrecord);
	vRecord record;
	vPropertyType _SHORT;
begin
	for vCoupledRecord in (select c.* from pxp.coupledrecord c
		join pxp.propertyconfig pc on c.propertyiid = pc.propertyiid and pc.supertype = _SuperType_TAGS
		where c.entityIID = ANY(p_baseEntityIIDS) and c.propertyiid = ANY(p_propertyIIDs)) loop

			select  * into vTagsRecord from pxp.fn_getTagsRecordFromCoupledRecord(vCoupledRecord);
			SELECT vCoupledRecord.propertyiid,
    		vCoupledRecord.entityiid,
    		vCoupledRecord.recordstatus,
    		vCoupledRecord.couplingtype,
    		vCoupledRecord.couplingbehavior,
    		vCoupledRecord.coupling,
    		vCoupledRecord.masterentityiid,
    		vCoupledRecord.masterpropertyiid,
    		vTagsRecord.usrtags
			into vRecord;
        	return next vRecord;
	end loop;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Get selective records WITH EntityIID check
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getselectivevaluerecords ) (p_baseEntityIIDs _IIDARRAY, p_propertyIIDs _IIDARRAY, p_languageCode character varying)
_RETURN _CURSOR( recordrow)
_IMPLEMENT_AS
begin
    return query select v.propertyIID, v.entityIID, v.recordStatus,
    _CouplingType_NONE::smallint as couplingtype, _CouplingBehavior_NONE::smallint as couplingbehavior, null as coupling,
    _TO_LONG(null) as masterEntityIID, _TO_LONG(null) as masterPropertyIID,
    v.valueiid, v.localeid, v.contextualobjectiid, v.value, v.ashtml, v.asnumber, v.unitsymbol, v.calculation
    from pxp.valuerecord v where v.recordStatus = _RecordStatus_DIRECT AND entityIID = ANY(p_baseEntityIIDs) and propertyIID = ANY(p_propertyiids)
	and (v.localeid = p_languagecode or v.localeid is null)
union all
select * from pxp.fn_getcoupledvalueswithchainingforselectiveproperties(p_baseEntityIIDs,p_propertyIIDs, p_languageCode);
end;
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Get COUPLED VALUES WITH CHAINING
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getcoupledvalueswithchainingforselectiveproperties ) (p_baseEntityIIDs _IIDARRAY, p_propertyIIDs _IIDARRAY,
p_languageCode character varying)
_RETURN _CURSOR( recordrow)
_IMPLEMENT_AS
    vCoupledRecord _RECORD(pxp.coupledRecord);
	vValueRecord _RECORD(pxp.valuerecord);
	vRecord record;
	vPropertyType _SHORT;
	vLanguageiid _IID;
	vLocaleid _VARCHAR;
begin
	select languageiid into vLanguageiid from pxp.languageconfig where languagecode = p_languageCode;
	for vCoupledRecord in (select c.* from pxp.coupledrecord c
		join pxp.propertyconfig pc on c.propertyiid = pc.propertyiid and pc.supertype = _SuperType_ATTRIBUTE
		where c.entityIID = ANY(p_baseEntityIIDS) and c.propertyiid = ANY(p_propertyIIDs) 
					   and (c.localeiid = vLanguageiid or c.localeiid = 0)) loop

			select  * into vValueRecord from pxp.fn_getValueRecordFromCoupledRecord(vCoupledRecord);
			
			SELECT languagecode into vLocaleid
        	FROM pxp.languageconfig
        	WHERE languageiid = vCoupledRecord.localeiid;
    
    		if(vLocaleid is null) then
      			vLocaleid = vValueRecord.localeid;
    		END IF;
    		
        	SELECT vCoupledRecord.propertyiid,
    		vCoupledRecord.entityiid,
    		vCoupledRecord.recordstatus,
    		vCoupledRecord.couplingtype,
    		vCoupledRecord.couplingbehavior,
    		vCoupledRecord.coupling,
    		vCoupledRecord.masterentityiid,
    		vCoupledRecord.masterpropertyiid,
    		vValueRecord.valueiid,
    		vLocaleid,
    		vValueRecord.contextualobjectiid,
    		vValueRecord.value,
    		vValueRecord.ashtml,
    		vValueRecord.asnumber,
    		vValueRecord.unitsymbol,
    		vValueRecord.calculation into vRecord;
        	return next vRecord;
	end loop;
end
_IMPLEMENT_END;
---------------------------------------------------------------------------------
