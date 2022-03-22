/**
 * Author:  vallee
 * Created: Jul 29, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"
#include "./include/coupling-defs.sql"

#ifdef PGSQL11
#define INHERITANCE_SCHEMA(schema) (select * from unnest(schema) with ordinality)
#elif ORACLE12C
#define INHERITANCE_SCHEMA(schema) (select column_value, rownum from table(schema))
#endif

---------------------------------------------------------------------------------
-- Read value record with locale inheritance schema 
-- (the entity name in base locale is returned on condition)
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_readValueRecord ) ( p_entityiid _IID, p_withBaseLocaleName _INT, p_propertyiids _IIDARRAY, p_localesSchema _VARARRAY)
_RETURN _CURSOR( pxp.directvaluerecord)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.directvaluerecord);
begin
	if ( p_withBaseLocaleName = 0 ) then
		_IMPLEMENT_CURSOR(vCursor,
			with 
				locales(localeID, rnum) as INHERITANCE_SCHEMA(p_localesSchema),
				propertyIIDs(propertyIID) as  _ARRAY_TABLE(p_propertyiids)
			select v.* from pxp.AllValueRecord v   
			join propertyIIDs p on p.propertyIID = v.propertyIID
			join locales l on l.localeID = coalesce( v.localeid, _ARRAY( p_localesSchema,1)) /* if localeID is null, considers the first entry */
			where v.EntityIID = p_entityiid 
		order by l.rnum, v.contextualobjectIID
		);
	else
		_IMPLEMENT_CURSOR(vCursor,
			with 
				locales(localeID, rnum) as INHERITANCE_SCHEMA(p_localesSchema),
				propertyIIDs(propertyIID) as  _ARRAY_TABLE(p_propertyiids)
			select v.*, l.rnum as localeOrder from pxp.AllValueRecord v   
			join propertyIIDs p on p.propertyIID = v.propertyIID
			join locales l on l.localeID = coalesce( v.localeid, _ARRAY( p_localesSchema,1)) /* same as above */
			where v.EntityIID = p_entityiid
			union
			select v.*, 100000 as localeOrder from pxp.AllValueRecord v /* the value in base locale ID comes in last extend */
			join pxp.baseentity e on e.baseentityiid = v.entityIID and e.baseLocaleID = v.localeID 
			where v.propertyIID = _ARTICLE_NAME_PROPERTY_IID and v.EntityIID = p_entityiid

		order by propertyIID, localeOrder, contextualobjectIID
		);
	end if;
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Clone the value records from an entity to another one
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_cloneValueRecords )( p_sourceEntityIID _IID, p_targetEntityIID _IID)
_IMPLEMENT_AS
	vSourceValueRecord	_RECORD(pxp.ValueRecord);
	vValueIID			_IID;
	vContextualObjectIID _IID;
begin
	for vSourceValueRecord in ( select * from pxp.ValueRecord 
			where entityIID = p_sourceEntityIID )
	loop
		vValueIID := _NEXTVAL( pxp.seqIID);
		if ( vSourceValueRecord.contextualObjectIID is not null ) then
			vContextualObjectIID := vValueIID;
			_CALL pxp.sp_CloneContextualObject( vSourceValueRecord.contextualObjectIID, vContextualObjectIID);
		else
			vContextualObjectIID := null;
		end if;
		insert into pxp.valueRecord 
			values
				( vSourceValueRecord.propertyIID, p_targetEntityIID, vSourceValueRecord.recordStatus, vValueIID, 
					vSourceValueRecord.localeID, vContextualObjectIID, vSourceValueRecord.value, vSourceValueRecord.ashtml, 
					vSourceValueRecord.asnumber, vSourceValueRecord.unitsymbol, vSourceValueRecord.calculation );
	end loop;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create separated target value records from a source of coupling
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_CreateSeparatedValueTargets )
( p_propertyIID _IID,
p_masterEntityIID _IID,
p_localeid _STRING)

_IMPLEMENT_AS
	
	vSourceValueRecord _RECORD(pxp.ValueRecord);
 	vSourceTagRecord _RECORD(pxp.tagsrecord);
 	vSourceCoupledRecord _RECORD(pxp.coupledrecord);
 	vSourceConflictingRecord _RECORD(pxp.conflictingvalues);
 	vSourceRecord _RECORD(pxp.conflictingvalues);
 	vPropertyType _INT;
 	vValueIID _IID;
 	vIsSource bool := false;
 	vLanguageCode _STRING;
 	vLanguageIID _IID;
  	vConflictingValuesForLanguageInh _RECORD(pxp.conflictingvalues);
 	
begin
	if(p_localeid is null) THEN
		vLanguageIID := 0;
	ELSE
		select languageiid into vLanguageIID from pxp.languageconfig where languagecode = p_localeid;
	END IF;
	
	for vSourceConflictingRecord IN 
 	select * from pxp.conflictingvalues where
  	sourceentityiid = p_masterEntityIID and propertyiid = p_propertyIID and localeiid = vLanguageIID
    loop
       continue when vSourceConflictingRecord.couplingsourcetype = _CouplingType_LANG_INHERITANCE OR vSourceConflictingRecord.couplingType = _CouplingBehavior_TIGHTLY;
       vIsSource := false;
       
	   select * into vSourceCoupledRecord from pxp.coupledrecord where 
	   masterentityiid = vSourceConflictingRecord.sourceentityiid and 
	   entityiid = vSourceConflictingRecord.targetentityiid and
 	   propertyiid = vSourceConflictingRecord.propertyiid and localeiid = vLanguageIID;
	   
	   if(vSourceCoupledRecord is null)THEN
	   		select * into vSourceRecord from pxp.conflictingvalues where propertyiid = p_propertyIID
   			and targetentityiid = vSourceConflictingRecord.targetentityiid and recordstatus = _RecordStatus_COUPLED
   			and localeiid = vLanguageIID;
			
			if(vSourceRecord is not null) THEN
				select * into vSourceCoupledRecord from pxp.coupledrecord where 
				masterentityiid = vSourceRecord.sourceentityiid and
   				propertyiid = vSourceRecord.propertyiid and localeiid = vLanguageIID;
    			vIsSource := true;
			END IF;
			
		ELSE IF(vSourceCoupledRecord.couplingBehavior = _CouplingBehavior_TIGHTLY) THEN
			vIsSource := true;
		END IF;
		END IF;
		
		
		if(vIsSource = true AND vSourceCoupledRecord.couplingbehavior != 3) THEN
  			select propertytype INTO vPropertyType from pxp.propertyconfig where propertyiid = p_propertyIID;
  			if(vPropertyType = 11) THEN
  				vSourceTagRecord = pxp.fn_getTagsRecordFromCoupledRecord(vSourceCoupledRecord);
  				
   				INSERT INTO pxp.tagsrecord(
     			propertyiid, entityiid, recordstatus, usrtags)
     			VALUES (p_propertyiid, vSourceCoupledRecord.entityiid, _RecordStatus_DIRECT, vSourceTagRecord.usrtags);
  			ELSE
  				vSourceValueRecord := pxp.fn_getValueRecordFromCoupledRecord(vSourceCoupledRecord);
  				
   				select languagecode into vLanguageCode from pxp.languageconfig where languageiid = vSourceCoupledRecord.localeiid;
  				vValueIID := nextval('pxp.seqIID');
      			insert into pxp.valueRecord ( propertyIID, entityIID, recordStatus, valueiid, localeid, contextualObjectIID, value, ashtml, asnumber, unitsymbol, calculation)
      			values
      			( p_propertyIID, vSourceCoupledRecord.entityiid, _RecordStatus_DIRECT, vValueIID,
       			vLanguageCode, vSourceValueRecord.contextualObjectIID,
    			vSourceValueRecord.value, vSourceValueRecord.ashtml,
       			vSourceValueRecord.asnumber, vSourceValueRecord.unitsymbol, vSourceValueRecord.calculation );
  			END IF;
 		END IF;
		
    end loop; 
    
    if(p_localeid is not null) THEN
		
		select languageiid into vLanguageIID from pxp.languageconfig where languagecode = p_localeid;
		
		for vConflictingValuesForLanguageInh IN
  			select * from pxp.conflictingvalues where
   			sourceentityiid = p_masterentityiid and propertyiid = p_propertyiid and couplingsourceiid = vLanguageIID
			and couplingsourcetype = _CouplingType_LANG_INHERITANCE and recordstatus = _RecordStatus_COUPLED
    		loop
				
    			select * into vSourceCoupledRecord from pxp.coupledrecord where masterentityiid = vConflictingValuesForLanguageInh.sourceentityiid 
    			and entityiid = vConflictingValuesForLanguageInh.targetentityiid and 
    			propertyiid = vConflictingValuesForLanguageInh.propertyiid 
    			and localeiid = vConflictingValuesForLanguageInh.localeiid and couplingtype = _CouplingType_LANG_INHERITANCE;
    			
    			vSourceValueRecord := pxp.fn_getValueRecordFromCoupledRecord(vSourceCoupledRecord);
    			
				select languagecode into vLanguageCode from pxp.languageconfig where languageiid = vConflictingValuesForLanguageInh.localeiid;
				
				vValueIID := nextval('pxp.seqIID');
         		insert into pxp.valueRecord ( propertyIID, entityIID, recordStatus, valueiid, localeid, 
				contextualObjectIID, value, ashtml, asnumber, unitsymbol, calculation)
         		values
         		( p_propertyIID, vConflictingValuesForLanguageInh.targetentityiid, _RecordStatus_DIRECT, vValueIID,
          		vLanguageCode, vSourceValueRecord.contextualObjectIID,
       			vSourceValueRecord.value, vSourceValueRecord.ashtml,
          		vSourceValueRecord.asnumber, vSourceValueRecord.unitsymbol, vSourceValueRecord.calculation );
			end loop;
			
		END IF;
		
		vSourceCoupledRecord := null;
  		select * INTO vSourceCoupledRecord from pxp.coupledrecord where entityiid = p_masterentityiid and 
  		propertyiid = p_propertyiid and localeiid = vLanguageIID;
  
  		if(vSourceCoupledRecord is not null) THEN
  			if(p_localeid is null) THEN
  				delete from pxp.valuerecord where entityiid = p_masterentityiid and propertyiid = p_propertyiid  
				and localeid is null;
		
				delete from pxp.tagsrecord where entityiid = p_masterentityiid and propertyiid = p_propertyiid;
			ELSE
				delete from pxp.valuerecord where entityiid = p_masterentityiid and propertyiid = p_propertyiid
				and localeid = p_localeid;
			END IF;
  		END IF;
		
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create separated target value records when a source of coupling is deleted
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_CreateValuesFromAllTargetsAndDelete )( p_masterNodeID _STRING)
_IMPLEMENT_AS
	vCouplingTarget		_RECORD(pxp.CoupledRecord);
	vSourceValueRecord	_RECORD(pxp.ValueRecord);
	vValueIID			_IID;
	vContextualObjectIID _IID;
begin
	for vCouplingTarget in ( select * from pxp.CoupledRecord 
		where masternodeid = p_masterNodeID )
	loop
		for vSourceValueRecord in ( select * from pxp.ValueRecord 
			where entityIID = vCouplingTarget.masterEntityIID and propertyIID = vCouplingTarget.masterPropertyIID )
		loop
			vValueIID := _NEXTVAL( pxp.seqIID);
			if ( vSourceValueRecord.contextualObjectIID is not null ) then
				vContextualObjectIID := vValueIID;
				_CALL pxp.sp_CloneContextualObject( vSourceValueRecord.contextualObjectIID, vContextualObjectIID);
			else
				vContextualObjectIID := null;
			end if;
			insert into pxp.valueRecord ( propertyIID, entityIID, recordStatus, valueiid, localeid, contextualObjectIID, value, ashtml, asnumber, unitsymbol, calculation) 
			values
				( vCouplingTarget.propertyIID, vCouplingTarget.entityIID, _RecordStatus_DIRECT, vValueIID, 
					vSourceValueRecord.localeID, vContextualObjectIID, vSourceValueRecord.value, vSourceValueRecord.ashtml, 
					vSourceValueRecord.asnumber, vSourceValueRecord.unitsymbol, vSourceValueRecord.calculation );
		end loop;
	end loop;
	delete from pxp.CoupledRecord where masternodeid = p_masterNodeID;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Search an existing value record with context option
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_getValueRecord)( p_entityiid _IID, p_propertyiid _IID, p_localeID _STRING, p_contextCode _STRING )
_RETURN _IID
_IMPLEMENT_AS
 vExistingValueIID       _IID;
begin
	if (  p_contextCode is null ) then
		begin
            select v.valueIID into vExistingValueIID from pxp.valuerecord v
                where ( v.localeID = p_localeID or ( v.localeID is null and p_localeID is null ) )
                and v.contextualObjectIID is null
                and v.propertyIID = p_propertyiid 
                and v.entityIID = p_entityiid;
        exception 
            when no_data_found then vExistingValueIID := null;
        end;
	else
       begin
            select v.valueIID into vExistingValueIID from pxp.valuerecord v
			join pxp.contextualObject c on c.contextualObjectIID = v.contextualObjectIID
                where ( v.localeID = p_localeID or ( v.localeID is null and p_localeID is null ) )
				and c.contextCode = p_contextCode
                and v.propertyIID = p_propertyiid 
                and v.entityIID = p_entityiid;
        exception 
            when no_data_found then vExistingValueIID := null;
        end;
	end if;
	return vExistingValueIID;
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create a value record
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_createValueRecord )( p_entityiid _IID, p_propertyiid _IID, p_contextCode _STRING, p_contextDupl _INT,
            p_status _INT, p_localeID _STRING, p_value _STRING, p_asHtml _TEXT, p_calculation _STRING, p_asNumber _FLOAT, p_unitSymbol _STRING)
_RETURN _IID
_IMPLEMENT_AS
    vPropertyRecordIID      _IID;
    vValueIID               _IID;
    vContextualObjectIID    _IID;
    vExistingValueIID       _IID;
begin
	vExistingValueIID := null;
	/* if no context or duplicated context is not allowed, then check existnce of the value record */
	if ( p_contextCode is null or p_contextDupl = 0 ) then
		vExistingValueIID := pxp.fn_getValueRecord( p_entityiid, p_propertyiid, p_localeID, p_contextCode);
		/* if the record doesn't exist, then one is created */
	   if ( vExistingValueIID is null ) then
			vValueIID := _NEXTVAL( pxp.seqIID);
			if ( p_contextCode is not null ) then
				vContextualObjectIID := vValueIID;
			    insert into pxp.contextualObject ( contextualObjectIID, contextCode )
				values ( vContextualObjectIID, p_contextCode );
 			else
				vContextualObjectIID := null;
			end if;
			insert into pxp.valueRecord
				(valueIID, propertyIID, entityiid, localeID, contextualObjectIID, recordstatus, value, asHtml, asNumber, unitSymbol, calculation)
			values( vValueIID, p_propertyiid, p_entityiid, p_localeID, vContextualObjectIID, p_status, p_value, p_asHtml, p_asNumber, p_unitSymbol, p_calculation);
		else /* the value record is updated */
			vValueIID := vExistingValueIID;
			update pxp.ValueRecord set
				value = p_value, asHTML = p_asHtml, asNumber = p_asNumber, unitSymbol = p_unitSymbol, calculation = p_calculation 
				where valueIID = vValueIID;
		end if;
	else /* a new value record is systematically created with duplicated context */
		vValueIID := _NEXTVAL( pxp.seqIID);
		vContextualObjectIID := vValueIID;
        insert into pxp.contextualObject ( contextualObjectIID, contextCode )
        values ( vContextualObjectIID, p_contextCode );
 		insert into pxp.valueRecord
				(valueIID, propertyIID, entityiid, localeID, contextualObjectIID, recordstatus, value, asHtml, asNumber, unitSymbol, calculation)
		values( vValueIID, p_propertyiid, p_entityiid, p_localeID, vContextualObjectIID, p_status, p_value, p_asHtml, p_asNumber, p_unitSymbol, p_calculation);
	end if;
    return vValueIID;
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Clone and update a coupled value record following change in coupling conditions
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_cloneAndUpdateCoupledValueRecord ) ( p_entityiid _IID, p_propertyiid _IID, p_valueiid _IID, p_status _INT,
            p_value _STRING, p_asHtml _TEXT, p_asNumber _FLOAT, p_unitSymbol _STRING, p_masterEntityIID _IID, p_masterPropertyIID _IID)
_RETURN _IID
_IMPLEMENT_AS
    vCurrentValueIID    _IID;
    vValueIID			_IID;
    vValueRecord        _RECORD(pxp.ValueRecord);
    vContextObjectIID   _IID;
begin
	for vValueRecord in (select * from pxp.ValueRecord where entityiid = p_masterEntityIID and  propertyiid = p_masterPropertyIID) loop
        vCurrentValueIID := _NEXTVAL( pxp.seqIID);
        if ( vValueRecord.contextualObjectIID is not null ) then
            _CALL pxp.sp_CloneContextualObject( vValueRecord.contextualObjectIID, vCurrentValueIID);
            vContextObjectIID := vCurrentValueIID;
        else
            vContextObjectIID := null;
        end if;
        if ( vValueRecord.valueIID = p_valueIID ) then
        	vValueIID := vCurrentValueIID;
            insert into pxp.ValueRecord values (p_propertyiid, p_entityiid, p_status, vCurrentValueIID, vValueRecord.localeID, vContextObjectIID,
                p_value, p_asHtml, p_asNumber, p_unitSymbol, vValueRecord.calculation);
        else
            insert into pxp.ValueRecord values (p_propertyiid, p_entityiid, p_status, vCurrentValueIID, vValueRecord.localeID, vContextObjectIID,
                vValueRecord.value, vValueRecord.asHTML, vValueRecord.asNumber, vValueRecord.unitSymbol, vValueRecord.calculation);
        end if;
    end loop;
    return vValueIID;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Update a coupled value record following change in coupling conditions
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_updateCoupledValueRecord ) ( p_entityiid _IID, p_propertyiid _IID, p_valueiid _IID, p_status _INT,
            p_value _STRING, p_asHtml _TEXT, p_asNumber _FLOAT, p_unitSymbol _STRING, p_masterEntityIID _IID, p_masterPropertyIID _IID)
_RETURN _IID
_IMPLEMENT_AS
    vValueIID	_IID;   
begin
	_CALL pxp.sp_ResolveCoupling( p_entityiid, p_propertyiid);
	return  pxp.fn_cloneAndUpdateCoupledValueRecord(p_entityiid, p_propertyiid, p_valueiid, p_status, 
						p_value, p_asHtml, p_asNumber, p_unitSymbol, p_masterEntityIID, p_masterPropertyIID);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Update a value record
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_updateValueRecord )( p_valueIID _IID, p_value _STRING, p_asHtml _TEXT, p_asNumber _FLOAT, p_unitSymbol _STRING, p_calculation _STRING)
_IMPLEMENT_AS
begin
    update pxp.Valuerecord set 
		value = p_value, ashtml = p_asHtml, asnumber = p_asNumber, unitSymbol = p_unitSymbol, calculation = p_calculation
	where valueIID = p_valueIID; 
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create a coupled value record by default value rule
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_createDefaultValueRecord )(p_baseEntityIID _IID, p_propertyIID _IID, 
			p_couplingBehavior _INT, p_CouplingType _INT, p_CouplingExpression _STRING,
            p_value _STRING, p_asHtml _TEXT, p_asNumber _FLOAT, p_unitSymbol _STRING, p_masterEntityIID _IID, p_masterPropertyIID _IID)
_RETURN _IID
_IMPLEMENT_AS
    vValueIID               _IID;
	vMasterNodeID			_VARCHAR;
begin
	begin
		select valueIID into vValueIID from pxp.ValueRecord where entityIID = p_masterEntityIID and propertyIID = p_masterPropertyIID;
		exception
			when no_data_found then vValueIID := null;
	end;
	if ( vValueIID is null ) then
		vValueIID := _NEXTVAL( pxp.seqIID);
		insert into pxp.valueRecord
			(valueIID, propertyIID, entityiid, recordStatus, localeid, contextualObjectIID, value, asHtml, asNumber, unitSymbol, calculation)
		values( vValueIID, p_masterPropertyIID, p_masterEntityIID, _RecordStatus_DEFAULT_VALUE, null, null, p_value, p_asHtml, p_asNumber, p_unitSymbol, null);
	end if;
	vMasterNodeID := _TO_VARCHAR(p_masterEntityIID) || ':' || _TO_VARCHAR( p_masterPropertyIID);
	_CALL pxp.sp_CreateCoupling( p_baseEntityIID, p_propertyIID,
            _RecordStatus_COUPLED, p_couplingBehavior, p_couplingType, vMasterNodeID, p_CouplingExpression, p_masterEntityIID, p_masterPropertyIID);
	return vValueIID;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Resolve tight coupling notification
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_resolveValueRecordNotification )( p_entityiid _IID, p_propertyiid _IID, p_recordStatus _INT)
_IMPLEMENT_AS
	vContextualObjectIID _IID;
begin
	if(p_recordStatus = _RecordStatus_DIRECT) then
		delete from pxp.coupledRecord  where entityiid = p_entityiid and propertyIID= p_propertyiid and recordstatus = _RecordStatus_NOTIFIED;
		update pxp.valueRecord set recordStatus = _RecordStatus_DIRECT where entityiid = p_entityiid and propertyIID= p_propertyiid and recordstatus = _RecordStatus_FORKED;
	elsif(p_recordStatus = _RecordStatus_FORKED) then
		select contextualObjectIId into vContextualObjectIID from pxp.valueRecord where propertyIID = p_propertyiid and entityIID = p_entityiid and recordstatus = _RecordStatus_FORKED;
    	if ( vContextualObjectIID is not null ) then
            delete from pxp.ContextualObject where contextualObjectIID = vContextualObjectIID;
    	end if;
		delete from pxp.ValueRecord where entityiid = p_entityiid and propertyIID= p_propertyiid and recordstatus = _RecordStatus_FORKED;
		update pxp.coupledRecord set recordstatus = _RecordStatus_COUPLED where entityiid = p_entityiid and propertyIID= p_propertyiid and recordstatus = _RecordStatus_NOTIFIED;
	end if;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Delete a value record
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_deleteValueRecord )( p_entityiid _IID, p_propertyiid _IID, p_contextualobjectiid _IID)
_IMPLEMENT_AS
begin
    if ( p_contextualobjectiid is not null ) then
        delete from pxp.ContextualObject where contextualObjectIID = p_contextualobjectiid;
    else
    	 delete from pxp.ValueRecord where propertyIID = p_propertyiid and entityIID = p_entityiid;
    end if;
    /* delete from value record */
	delete from pxp.coupledRecord where propertyIID = p_propertyiid and entityIID = p_entityiid;
end
_IMPLEMENT_END;

-----------------------------------------------------------------------------
--Delete a value record as per locale id
-----------------------------------------------------------------------------
_PROCEDURE( pxp.sp_deleteLanguageTranslation )( p_valueiid _IID )
_IMPLEMENT_AS
	vContextualObjectIID _IID;
begin
	select contextualObjectIID into vContextualObjectIID from pxp.valueRecord where valueIID = p_valueiid;
--	delete any contextual reference
	if(vContextualObjectIID is not null) then
		delete from pxp.ContextualObject where contextualObjectIID = vContextualObjectIID;
	end if;
--	delete value record.
	delete from pxp.valueRecord where valueIID = p_valueiid;
end
_IMPLEMENT_END;



#ifdef PGSQL11
-- end of PGSQL Value Records Procedure/Function declarations
#elif ORACLE12C
-- end of ORACLE Value Records Procedure/Function declarations
#endif



