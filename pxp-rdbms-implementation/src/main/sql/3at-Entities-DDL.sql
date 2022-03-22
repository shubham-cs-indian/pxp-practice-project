/**
 * TRIGGER DEFINITIONs of ENTITY DATA
 * Author:  vallee
 * Created: Apr 5, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

/*    
    Limitations involved by PGSQL here:
    -> cannot have a FK pointing to a partitioned table => consistency should be resolved by trigger
*/

#if defined  PGSQL11 && defined WITH_PARTITIONS
    #define _TRIGGER_ACTION  after insert /* partitioned table cannot have a before insert trigger */
#else
    #define _TRIGGER_ACTION  before insert
#endif

--
--    Base Entity Triggers 
--
-- pxp.BaseEntityIIDCount: portable implementation of exists
_FUNCTION( pxp.BaseEntityIIDCount )( pBaseEntityIID _IID )
_RETURN _INT
_IMPLEMENT_AS
    vExistingCount     _INT;
begin
    if ( pBaseEntityIID is null ) then
        return -1; -- for null value the check is not provided
    end if;
    select count(*) into vExistingCount from pxp.BaseEntity where baseEntityIID = pBaseEntityIID;
    return vExistingCount;
end
_IMPLEMENT_END;

--
-- Trigger for below foreign key
-- _FOREIGN_KEY(BaseEntity, BaseEntity, parentIID, baseEntityIID);
--
_TRIGGER( BaseEntity_parentIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.BaseEntity )
    vExistingIID    _INT;
begin
    vExistingIID := pxp.BaseEntityIIDCount( _NEW.parentIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on parentIID: %', _TO_VARCHAR( _NEW.parentIID));
end
_TRIGGER_BODY_END(BaseEntity_parentIID_fk, _TRIGGER_ACTION, pxp.BaseEntity);

--
-- Trigger for below foreign key
--_FOREIGN_KEY(BaseEntity, BaseEntity, topParentIID, baseEntityIID);
--
_TRIGGER( BaseEntity_topParentIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.BaseEntity )
    vExistingIID    _INT;
begin
    vExistingIID := pxp.BaseEntityIIDCount( _NEW.topParentIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on top parentIID: %', _TO_VARCHAR( _NEW.topParentIID));
end
_TRIGGER_BODY_END(BaseEntity_topParentIID_fk, _TRIGGER_ACTION, pxp.BaseEntity);

--
-- Trigger for below foreign key
--_FOREIGN_KEY(BaseEntity, BaseEntity, originBaseEntityIID, baseEntityIID);
--
_TRIGGER( BaseEntity_originBaseEntityIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.BaseEntity )
    vExistingIID    _INT;
begin
    vExistingIID := pxp.BaseEntityIIDCount( _NEW.originBaseEntityIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on default origin entityIID: %', _TO_VARCHAR(_NEW.originBaseEntityIID));
end
_TRIGGER_BODY_END(BaseEntity_originBaseEntityIID_fk, _TRIGGER_ACTION, pxp.BaseEntity);

--
-- Trigger for below foreign key
--_FOREIGN_KEY(BaseEntity, BaseEntity, defaultImageIID, baseEntityIID);
--
_TRIGGER( BaseEntity_defaultImageIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.BaseEntity )
    vExistingAssetIID     _INT;
begin
    if ( _NEW.defaultImageIID is null ) then
        _RETURN_TRIGGER_NEW;
    end if;
begin
        select count(*) into vExistingAssetIID from pxp.BaseEntity
            where baseEntityIID = _NEW.defaultImageIID and basetype = _BaseType_ASSET;
        exception
            when no_data_found then vExistingAssetIID := 0;
    end;
    if ( vExistingAssetIID > 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on default image IID: %', _TO_VARCHAR(_NEW.defaultImageIID));
end
_TRIGGER_BODY_END(BaseEntity_defaultImageIID_fk, _TRIGGER_ACTION, pxp.BaseEntity);

--
-- Trigger for below foreign key
-- _FOREIGN_KEY( Relation, baseEntity, side1EntityIID, baseEntityIID);
--
_TRIGGER( Relation_side1EntityIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.Relation )
    vExistingIID    _INT;
begin
    vExistingIID := pxp.BaseEntityIIDCount( _NEW.side1EntityIID);
    if ( vExistingIID <> 0 ) then
    _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on side 1 base entity IID: %', _TO_VARCHAR(_NEW.side1EntityIID));
end
_TRIGGER_BODY_END(Relation_side1EntityIID_fk, _TRIGGER_ACTION, pxp.Relation);

--
-- Trigger for below foreign key
-- _FOREIGN_KEY( Relation, baseEntity, side2EntityIID, baseEntityIID);
--
_TRIGGER( Relation_side2EntityIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.Relation )
    vExistingIID    _INT;
begin
    vExistingIID := pxp.BaseEntityIIDCount( _NEW.side2EntityIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on side 2 base entity IID: %', _TO_VARCHAR(_NEW.side2EntityIID));
end
_TRIGGER_BODY_END(Relation_side2EntityIID_fk, _TRIGGER_ACTION, pxp.Relation);

--
-- Generic check for baseEntityIID in pxp.BaseEntity
--
_TRIGGER( BaseEntity_baseEntityIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.BaseEntity )
    vExistingIID    _IID;
begin
    vExistingIID := pxp.BaseEntityIIDCount( _NEW.baseEntityIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on baseEntityIID: %', _TO_VARCHAR(_NEW.baseEntityIID));
end
_IMPLEMENT_END;
--
-- BaseEntityClassifierLink triggers:
--
_TRIGGER_DECLARE( BaseEntityClassifierLink_baseEntityIID_fk, BaseEntity_BaseEntityIID_fk, _TRIGGER_ACTION, pxp.BaseEntityClassifierLink);

--
-- Check for TagsRecord entityIID in pxp.baseEntity
--
_TRIGGER( TagsRecord_entityIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.TagsRecord )
    vExistingIID    _IID;
begin
	if ( _NEW.recordstatus = _RecordStatus_DEFAULT_VALUE ) then
        _RETURN_TRIGGER_NEW;
    end if;
    
    vExistingIID := pxp.BaseEntityIIDCount( _NEW.entityIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on baseEntityIID: %', _TO_VARCHAR(_NEW.entityIID));
end
_IMPLEMENT_END;
--
--    Tags record triggers:
--
-- _FOREIGN_KEY( TagsRecord, baseEntity, entityIID, baseEntityIID);
_TRIGGER_DECLARE( TagsRecord_baseEntityIID_fk, TagsRecord_entityIID_fk, _TRIGGER_ACTION, pxp.TagsRecord);

--
-- Check for entityIID in pxp.ValueRecord
--
_TRIGGER( ValueRecord_entityIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.ValueRecord )
    vExistingIID    _IID;
begin
	if ( _NEW.recordStatus = _RecordStatus_DEFAULT_VALUE ) then
        _RETURN_TRIGGER_NEW;
    end if;

    vExistingIID := pxp.BaseEntityIIDCount( _NEW.entityIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on baseEntityIID: %', _TO_VARCHAR(_NEW.entityIID));
end
_IMPLEMENT_END;
--
--    Value record triggers:
--
-- _FOREIGN_KEY( ValueRecord, baseEntity, entityIID, baseEntityIID);
_TRIGGER_DECLARE( ValueRecord_baseEntityIID_fk, ValueRecord_entityIID_fk, _TRIGGER_ACTION, pxp.ValueRecord);

_TRIGGER( CoupledRecord_entityIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.CoupledRecord )
    vExistingIID    _IID;
begin
	if ( _NEW.recordStatus = _RecordStatus_DEFAULT_VALUE ) then
        _RETURN_TRIGGER_NEW;
    end if;

    vExistingIID := pxp.BaseEntityIIDCount( _NEW.entityIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on baseEntityIID: %', _TO_VARCHAR(_NEW.entityIID));
end
_IMPLEMENT_END;
--
--    Coupled record triggers:
--
--_FOREIGN_KEY( CoupledRecord, baseEntity, entityIID, baseEntityIID);
_TRIGGER_DECLARE( CoupledRecord_baseEntityIID_fk, CoupledRecord_entityIID_fk, _TRIGGER_ACTION, pxp.CoupledRecord);


#if FOREIGN_KEY_WITH_PARTITION
--
-- pxp.ContextualObjectIIDCount: portable implementation of exists
--
_FUNCTION( pxp.ContextualObjectIIDCount )( pContextualObjectIID _IID )
_RETURN _INT
_IMPLEMENT_AS
    vExistingCount     _INT;
begin
    if ( pContextualObjectIID is null ) then
        return -1; -- for null value the check is not provided
    end if;
    select count(*) into vExistingCount from pxp.ContextualObject where ContextualObjectIID = pContextualObjectIID;
    exception
        when no_data_found then return 0;
    return vExistingCount;
end
_IMPLEMENT_END;

--
-- Generic check for contextualObjectIID in pxp.ContextualObject
--
_FUNCTION( pxp.ContextualObject_contextualObjectIID_fk )()
_RETURN trigger
_IMPLEMENT_AS
    vExistingIID    _IID;
begin
    vExistingIID := pxp.ContextualObjectIIDCount( _NEW.contextualObjectIID);
    if ( vExistingIID <> 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Foreign Key violation on ContextualObjectIID: %', _TO_VARCHAR( _NEW.contextualObjectIID));
end
_IMPLEMENT_END;

--_FOREIGN_KEY(baseEntity, ContextualObject, contextualObjectIID, contextualObjectIID);
_TRIGGER_DECLARE( BaseEntity_contextualObjectIID_fk, ContextualObject_contextualObjectIID_fk, _TRIGGER_ACTION, pxp.BaseEntity);

--_FOREIGN_KEY(baseEntity, ContextualObject, contextualObjectIID, contextualObjectIID);
_TRIGGER_DECLARE( BaseEntity_contextualObjectIID_fk, ContextualObject_contextualObjectIID_fk, _TRIGGER_ACTION, pxp.BaseEntity);

-- UNIQUE BaseEntityID + CatalogCode
_TRIGGER( BaseEntity_baseEntityID_CatalogCode_Unq )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.BaseEntity )
    vExistingCase     _INT;
begin
    begin
        select count(*) into vExistingCase from pxp.BaseEntity
            where baseEntityID = _NEW.baseEntityID and catalogCode = _NEW.catalogCode;
        exception
            when no_data_found then vExistingCase := 0;
    end;
    if ( vExistingCase = 0 ) then
        _RETURN_TRIGGER_NEW;
    end if;
    _RAISE( 'Unicity violation on entity ID % in current Catalog %', _NEW.baseEntityID );
end
_TRIGGER_BODY_END(BaseEntity_defaultImageIID_fk, _TRIGGER_ACTION, pxp.BaseEntity);


--
-- ContextBaseEntityLink triggers:
--
-- _FOREIGN_KEY( ContextBaseEntityLink, BaseEntity, baseEntityIID, baseEntityIID);
_TRIGGER_DECLARE( ContextBaseEntityLink_baseEntityIID_fk, BaseEntity_baseEntityIID_fk, _TRIGGER_ACTION, pxp.ContextBaseEntityLink);

--
-- BaseEntityClassifierLink triggers:
--
-- _FOREIGN_KEY( ContextBaseEntityLink, ContextualObject, contextualObjectIID, contextualObjectIID);
_TRIGGER_DECLARE( BaseEntityClassifierLink_contextualObjectIID_fk, ContextualObject_contextualObjectIID_fk, _TRIGGER_ACTION, pxp.ContextBaseEntityLink);

--
--    Relation triggers:
--
-- _FOREIGN_KEY( Relation, ContextualObject, side1contextualObjectIID, contextualObjectIID);
_TRIGGER(Relation_side1ContextualObjectIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.Relation)
    vExistingIID     _INT;
begin
    vExistingIID := pxp.ContextualObjectIIDCount( _NEW.side1ContextualObjectIID);
    if ( vExistingIID <> 0 ) then
		_RETURN_TRIGGER_NEW;
	end if;
    _RAISE( 'Foreign Key violation on object side1ContextualObjectIID: %', _NEW.side1ContextualObjectIID);
end
_TRIGGER_BODY_END(Relation_side1ContextualObjectIID_fk, _TRIGGER_ACTION, pxp.Relation);

-- _FOREIGN_KEY( Relation, ContextualObject, side2contextualObjectIID, contextualObjectIID);
_TRIGGER(Relation_side2ContextualObjectIID_fk )
_TRIGGER_BODY_IS( _TRIGGER_ACTION, pxp.Relation)
    vExistingIID     _INT;
begin
    vExistingIID := pxp.ContextualObjectIIDCount( _NEW.side2ContextualObjectIID);
    if ( vExistingIID <> 0 ) then
		_RETURN_TRIGGER_NEW;
	end if;
    _RAISE( 'Foreign Key violation on object side2ContextualObjectIID: %', _NEW.side2ContextualObjectIID);
end
_TRIGGER_BODY_END(Relation_side2ContextualObjectIID_fk, _TRIGGER_ACTION, pxp.Relation);

#endif  /*FOREIGN_KEY_WITH_PARTITION*/
	
#ifdef PGSQL11
-- end of PGSQL Entity Table triggers
#elif ORACLE12C
-- end of ORACLE Entity Table triggers
#endif
