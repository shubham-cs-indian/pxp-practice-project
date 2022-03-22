/**
 * Author:  Roshani Waghmare
 * Created: OCT 15, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

---------------------------------------------------------------------------------
-- Read BaseEntity by batch to export
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_exportBaseEntityPXON ) ( p_baseEntityiids _IIDARRAY)
_RETURN _CURSOR( pxp.baseEntityPXON)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.baseEntityPXON);
begin
    _IMPLEMENT_CURSOR( vCursor,
    with
        entityIIDs(entityIID) as _ARRAY_TABLE( p_baseEntityiids)
        (select be.* from pxp.baseEntityPXON be
         join entityIIDs e on e.entityIID = be.baseEntityIID)
        );
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Read Value record by batch to export
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_exportValueRecordPXON ) ( p_entityiids _IIDARRAY, p_localeID _STRING)
_RETURN _CURSOR( pxp.allValueRecordPXON)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.allValueRecordPXON);
begin
    _IMPLEMENT_CURSOR( vCursor,
    with
        entityIIDs(entityIID) as _ARRAY_TABLE( p_entityiids)
        (select vr.* from pxp.allValueRecordPXON vr
         join entityIIDs e on e.entityIID = vr.entityIID
         where (vr.localeID = p_localeID or vr.localeID is null ))
        );
end 
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- read Tags record by batch to export
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_exportTagsRecordPXON ) ( p_entityiids _IIDARRAY)
_RETURN _CURSOR( pxp.alltagsRecordPXON)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.alltagsRecordPXON);
begin
    _IMPLEMENT_CURSOR( vCursor,
    with
        entityIIDs(entityIID) as _ARRAY_TABLE( p_entityiids)
        (select tr.* from pxp.allTagsRecordPXON tr
         join entityIIDs e on e.entityIID = tr.entityIID)
        );
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- read RelationSide1PXON record by batch to export
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_exportRelationSide1PXON ) ( p_entityiids _IIDARRAY)
_RETURN _CURSOR( pxp.relationside1pxon)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.relationSide1PXON);
begin
    _IMPLEMENT_CURSOR( vCursor,
    with
        entityIIDs(entityIID) as _ARRAY_TABLE( p_entityiids)
        (select r1.* from pxp.relationSide1PXON r1
         join entityIIDs e on e.entityIID = r1.side1entityIID)
        );
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- read RelationSide2PXON record by batch to export
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_exportRelationSide2PXON ) ( p_entityiids _IIDARRAY)
_RETURN _CURSOR( pxp.relationside2pxon)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.relationside2pxon);
begin
    _IMPLEMENT_CURSOR( vCursor,
    with
        entityIIDs(entityIID) as _ARRAY_TABLE( p_entityiids)
        (select r2.* from pxp.relationSide2PXON r2
         join entityIIDs e on e.entityIID = r2.side2entityIID)
        );
end 
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- read RelationSide2PXON record by batch to export
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_exportEmbdEntitiesPXON ) ( p_baseEntityiids _IIDARRAY)
_RETURN _CURSOR( pxp.baseEntityPXON)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.baseEntityPXON);
begin
    _IMPLEMENT_CURSOR( vCursor,
    with
        entityIIDs(entityIID) as _ARRAY_TABLE(p_baseEntityiids)
        (select be.* from pxp.baseEntityPXON be
         join entityIIDs e on e.entityIID = be.parentIID)
        );
end 
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- read RelationSide2PXON record by batch to export
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_exportOtherClassifierPXON ) ( p_baseEntityiids _IIDARRAY)
_RETURN _CURSOR( pxp.baseentityclassifierlinkpxon)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.baseentityclassifierlinkpxon);
begin
    _IMPLEMENT_CURSOR( vCursor,
    with
        entityIIDs(entityIID) as _ARRAY_TABLE(p_baseEntityiids)
        (select bc.* from pxp.baseEntityClassifierLinkPXON bc
         join entityIIDs e on e.entityIID = bc.baseEntityIID)
        );
end 
_IMPLEMENT_END;
