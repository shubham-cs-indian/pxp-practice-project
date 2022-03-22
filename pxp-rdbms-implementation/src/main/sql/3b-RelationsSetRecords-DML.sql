/**
 * Author:  vallee
 * Created: Jul 29, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"
#include "./include/coupling-defs.sql"

---------------------------------------------------------------------------------
-- Read relations set record
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_readRelationsSetRecord ) ( p_entityiid _IID, p_propertyiids _IIDARRAY, side _INT)
_RETURN _CURSOR( pxp.AllRelationSide1)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.AllRelationSide1);
begin
    if ( side = 1 ) then
        _IMPLEMENT_CURSOR( vCursor,
            with 
                propertyIIDs( propertyIID) as  _ARRAY_TABLE( p_propertyiids)
            (select v.* from pxp.AllRelationSide1 v
            join propertyIIDs p on p.propertyIID = v.propertyIID
            where v.EntityIID = p_entityiid)
        );
    else
        _IMPLEMENT_CURSOR( vCursor,
            with 
                propertyIIDs( propertyIID) as  _ARRAY_TABLE( p_propertyiids)
            (select v.* from pxp.AllRelationSide2 v
            join propertyIIDs p on p.propertyIID = v.propertyIID
            where v.EntityIID = p_entityiid)
        );
    end if;
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create separated target relations from a source of coupling
---------------------------------------------------------------------------------
-- Side 1 version
_PROCEDURE( pxp.sp_CloneRelationsSide1 )( p_masterEntityIID _IID, p_masterPropertyIID _IID, p_entityIID _IID, p_propertyIID _IID)
_IMPLEMENT_AS
	vRelationSource _RECORD(pxp.Relation);
begin
	for vRelationSource in ( select * from pxp.relation
		where side1entityIID = p_masterEntityIID and propertyIID = p_masterPropertyIID )
	loop
		insert into pxp.Relation (propertyIID, side1entityIID, side2entityIID, side1contextualObjectIID, side2contextualObjectIID)
		values ( p_propertyIID, p_entityIID, vRelationSource.side2entityIID, 
			pxp.fn_CloneContextualObject( vRelationSource.side1contextualObjectIID), pxp.fn_CloneContextualObject( vRelationSource.side2contextualObjectIID));
	end loop;
end
_IMPLEMENT_END;
-- Side 2 version
_PROCEDURE( pxp.sp_CloneRelationsSide2 )( p_masterEntityIID _IID, p_masterPropertyIID _IID, p_entityIID _IID, p_propertyIID _IID)
_IMPLEMENT_AS
	vRelationSource _RECORD(pxp.Relation);
begin
	for vRelationSource in ( select * from pxp.relation
		where side2entityIID = p_masterEntityIID and propertyIID = p_masterPropertyIID )
	loop
		insert into pxp.Relation (propertyIID, side1entityIID, side2entityIID, side1contextualObjectIID, side2contextualObjectIID)
		values ( p_propertyIID, vRelationSource.side1entityIID, p_entityIID,
			pxp.fn_CloneContextualObject( vRelationSource.side1contextualObjectIID), pxp.fn_CloneContextualObject( vRelationSource.side2contextualObjectIID));
	end loop;
end
_IMPLEMENT_END;
-- Entry call
_PROCEDURE( pxp.sp_CreateSeparatedRelationsTargets )( p_masterNodeID _STRING, p_side _INT)
_IMPLEMENT_AS
	vCouplingTarget		_RECORD(pxp.CoupledRecord);
begin
	for vCouplingTarget in ( select * from pxp.CoupledRecord 
		where masternodeid = p_masterNodeID 
		and couplingType in _COUPLING_WITH_SEPARABLE_RECORDS
		and recordStatus <> _RecordStatus_NOTIFIED )
	loop
		if ( p_side = 1 ) then
			_CALL pxp.sp_CloneRelationsSide1( vCouplingTarget.masterEntityIID, vCouplingTarget.masterPropertyIID, vCouplingTarget.entityIID, vCouplingTarget.propertyIID);
		else
			_CALL pxp.sp_CloneRelationsSide2( vCouplingTarget.masterEntityIID, vCouplingTarget.masterPropertyIID, vCouplingTarget.entityIID, vCouplingTarget.propertyIID);
		end if;
	end loop;
	_CALL pxp.sp_ResolveCouplingTargets(p_masterNodeID);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create separated target relation records when a source of coupling is deleted
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_CreateRelationsFromAllTargetsAndDelete )( p_masterNodeID _STRING, p_side _INT)
_IMPLEMENT_AS
	vCouplingTarget		_RECORD(pxp.CoupledRecord);
begin
	for vCouplingTarget in ( select * from pxp.CoupledRecord 
		where masternodeid = p_masterNodeID )
	loop
		if ( p_side = 1 ) then
			_CALL pxp.sp_CloneRelationsSide1( vCouplingTarget.masterEntityIID, vCouplingTarget.masterPropertyIID, vCouplingTarget.entityIID, vCouplingTarget.propertyIID);
		else
			_CALL pxp.sp_CloneRelationsSide2( vCouplingTarget.masterEntityIID, vCouplingTarget.masterPropertyIID, vCouplingTarget.entityIID, vCouplingTarget.propertyIID);
		end if;
	end loop;
	delete from pxp.CoupledRecord where masternodeid = p_masterNodeID;
end
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Create a Set of relation records attached to an existing relations Set
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_createRelations) ( p_basentityiid _IID, p_propertyiid _IID, p_side _INT, p_contextCode _STRING, p_otherContextCode _STRING,
         p_sideEntityIIDs _IIDARRAY, p_baseLocaleID _STRING, p_catalogCode _STRING, p_organizationCode _STRING)
_RETURN _STRING
_IMPLEMENT_AS
    vSideContextualObjectIID _IID;
    vSideEntityIIDIdx        _INT;
    vSideEntityIID           _IID;
    vCombinedIIDs            _VARCHAR;
    vOtherSideContextualObjectIID _IID;

begin
    for vSideEntityIIDIdx in 1 .. _COUNT(p_sideEntityIIDs) loop
         if ( p_contextCode is not null ) then 
           vSideContextualObjectIID := _NEXTVAL( pxp.seqIID);
           insert into pxp.contextualObject ( contextualObjectIID, contextCode ) values ( vSideContextualObjectIID, p_contextCode );
       else
           vSideContextualObjectIID := null;
        end if; 
        if ( p_otherContextCode is not null ) then
            vOtherSideContextualObjectIID := _NEXTVAL( pxp.seqIID);
            insert into pxp.contextualObject ( contextualObjectIID, contextCode ) values ( vOtherSideContextualObjectIID, p_contextCode );
        else
            vOtherSideContextualObjectIID := null;
        end if;
        vSideEntityIID := _ARRAY( p_sideEntityIIDs, vSideEntityIIDIdx);
        if ( p_side = 1 ) then
          insert into pxp.Relation values( p_propertyiid, p_basentityiid, vSideEntityIID, vSideContextualObjectIID, vOtherSideContextualObjectIID);
        elsif ( p_side = 2 ) then
          insert into pxp.Relation values(p_propertyiid,  vSideEntityIID, p_basentityiid, vOtherSideContextualObjectIID, vSideContextualObjectIID);
        end if;

         if ( vSideContextualObjectIID is null ) then
            vSideContextualObjectIID := 0;
         end if;
         if ( vOtherSideContextualObjectIID is null ) then
            vOtherSideContextualObjectIID := 0;
         end if;
        if( vCombinedIIDs is null ) then 
           vCombinedIIDs :=  _TO_VARCHAR(vSideContextualObjectIID) || ':'|| _TO_VARCHAR(vOtherSideContextualObjectIID) ;
        else 
           vCombinedIIDs := vCombinedIIDs || ',' || _TO_VARCHAR( vSideContextualObjectIID)|| ':' || _TO_VARCHAR(vOtherSideContextualObjectIID) ;
        end if;
        end loop;
    return vCombinedIIDs;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Update a Set of coupled relations Set records with new relations
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_updateCoupledRelationsSetRecord )( p_basentityiid _IID, p_propertyiid _IID, 
         p_side _INT, p_contextCode _STRING, p_sideEntityIIDs _IIDARRAY, p_baseLocaleID _STRING, p_catlogCode _STRING
         , p_organizationCode _STRING)
_RETURN _STRING
_IMPLEMENT_AS
begin
    _CALL pxp.sp_ResolveCoupling( p_basentityiid, p_propertyiid);
	return pxp.fn_createRelations( p_basentityiid, p_propertyiid, p_side, 
		p_contextCode, p_sideEntityIIDs, p_baseLocaleID, p_catlogCode, p_organizationCode);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Resolve tight coupling notification
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_resolveRelationsRecordNotification )( p_entityiid _IID, p_propertyiid _IID, p_sideentityiids _IIDARRAY, p_side _INT, p_recordStatus _INT)
_IMPLEMENT_AS
begin
	if (p_recordStatus = _RecordStatus_DIRECT) then
		delete from pxp.coupledRecord  where entityiid = p_entityiid and propertyIID = p_propertyiid and recordstatus = _RecordStatus_NOTIFIED;
	elsif (p_recordStatus = _RecordStatus_FORKED) then
     	call pxp.sp_deleteRelations(p_propertyiid, p_entityiid, p_side, p_sideEntityIIDs);
	    update pxp.coupledRecord set recordstatus = _RecordStatus_COUPLED where entityiid = p_entityiid and propertyIID= p_propertyiid and recordstatus = _RecordStatus_NOTIFIED;
	end if;
end
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Delete a Set of relation records attached to an existing relations Set
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_deleteRelations) ( p_propertyiid _IID, p_entityIID _IID, p_side _INT, p_sideEntityIIDs _IIDARRAY)
_IMPLEMENT_AS
vSide1ContextualObjectIID _IID;
vSide2ContextualObjectIID _IID;
vSideEntityIIDIdx         _INT;
vSideEntityIID            _IID;

begin
	for vSideEntityIIDIdx in 1 .. _COUNT(p_sideEntityIIDs) loop 
	vSideEntityIID := _ARRAY( p_sideEntityIIDs, vSideEntityIIDIdx);
	if ( p_side = 1 ) then
	select side1contextualobjectiid , side2contextualobjectiid into vSide1ContextualObjectIID, vSide2ContextualObjectIID from pxp.relation
      r  where r.side1entityIID = p_entityIID and r.propertyiid = p_propertyiid and r.side2entityIID = vSideEntityIID;
         
    delete from pxp.Relation r where r.side1entityIID = p_entityIID
       and r.propertyiid = p_propertyiid and r.side2entityIID = vSideEntityIID;

    elsif ( p_side = 2 ) then
	select side1contextualobjectiid , side2contextualobjectiid into vSide1ContextualObjectIID, vSide2ContextualObjectIID from pxp.relation
      r  where r.side1entityIID = vSideEntityIID and r.propertyiid = p_propertyiid and r.side2entityIID = p_entityIID;
    
    delete from pxp.Relation r where r.side2entityIID = p_entityIID
         and r.propertyiid = p_propertyiid and r.side1entityIID = vSideEntityIID;
    end if;
         if(vSide1ContextualObjectIID is not null) then 
            delete from pxp.contextualobject where contextualobjectiid = vSide1ContextualObjectIID;
         end if;
	
         if(vSide2ContextualObjectIID is not null) then
            delete from pxp.contextualobject where contextualobjectiid = vSide2ContextualObjectIID;
         end if;
		 
    end loop;
    delete from pxp.coupledRecord where propertyIID = p_propertyiid and entityIID = p_entityiid;
end
_IMPLEMENT_END;
---------------------------------------------------------------------------------
-- Delete contextualObject and remove them from relationship
---------------------------------------------------------------------------------
_PROCEDURE(pxp.sp_removeContextFromRelationship) (p_propertyiid _IID, p_side1ContextId _STRING, p_side2ContextId _STRING)
_IMPLEMENT_AS
v_contextualObjectIds _IIDARRAY;
begin
	
	v_contextualObjectIds := ARRAY(select co.contextualobjectiid from pxp.relation r join pxp.contextualobject co on 
	r.side1contextualobjectiid = co.contextualobjectiid where r.propertyiid = p_propertyiid and co.contextcode = p_side1ContextId 
	UNION select co.contextualobjectiid from pxp.relation r join pxp.contextualobject co on r.side2contextualobjectiid 
	= co.contextualobjectiid where r.propertyiid = p_propertyiid and co.contextcode = p_side2ContextId);
	if (p_side1ContextId != '' AND p_side2ContextId != '') then
  		update pxp.relation set side1contextualobjectiid = NULL, side2contextualobjectiid = null
  		where side1contextualobjectiid = any(v_contextualObjectIds) OR side2contextualobjectiid = any(v_contextualObjectIds);
  	elsif( p_side2ContextId != '') then
  		update pxp.relation set side2contextualobjectiid = null where side2contextualobjectiid = any(v_contextualObjectIds);
 	else
  		update pxp.relation set side1contextualobjectiid = null where side1contextualobjectiid = any(v_contextualObjectIds);
 	end if;
 	delete from pxp.contextualobject where contextualobjectiid = any(v_contextualObjectIds);
end
_IMPLEMENT_END;

#ifdef PGSQL11
-- end of PGSQL Relations Set Procedure/Function declarations
#elif ORACLE12C
-- end of ORACLE Relations Set Procedure/Function declarations
#endif
