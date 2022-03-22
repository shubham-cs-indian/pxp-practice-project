/**
 * Author:  vallee
 * Created: Jul 4, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

/*----------------------------------------------------------------------------*/
-- create a new dependency when not existing
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.upsertDependency) ( pNodeID _STRING, pParentID _STRING, pGraphCase _INT)
_IMPLEMENT_AS
	vParentID		_VARCHAR;
begin
	begin
		select parentNodeID into vParentID from pxp.Graph
		where 
		nodeID = pNodeID and parentNodeID = pParentID and graphCase = pGraphCase;
	exception
		when no_data_found then vParentID := null;
	end;
	if ( vParentID is null ) then
		insert into pxp.Graph values ( pNodeID, pParentID, pGraphCase);
	end if;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
-- Create dependencies
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.sp_multiinsertgraph) ( pNodeID _STRING, pNewParentIDs _VARARRAY, pGraphCase _INT)
_IMPLEMENT_AS
	vFound			_INT;
    vParentIDIdx    _INT;
	vParentID		_VARCHAR;
begin
	for vParentIDIdx in 1 .. _COUNT(pNewParentIDs) loop
        vParentID := _ARRAY( pNewParentIDs, vParentIDIdx);	
		_CALL pxp.upsertDependency( pNodeID, vParentID, pGraphCase);
	end loop;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
-- Renew dependencies from an existing nodeID
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.sp_renewGraphDependencies) ( pNodeID _STRING, pNewParentIDs _VARARRAY, pGraphCase _INT)
_IMPLEMENT_AS
	vFound			_INT;
    vParentIDIdx    _INT;
	vParentID		_VARCHAR;
begin
	delete from pxp.Graph where nodeID = pNodeID and graphCase = pGraphCase;
	for vParentIDIdx in 1 .. _COUNT(pNewParentIDs) loop
        vParentID := _ARRAY( pNewParentIDs, vParentIDIdx);	
		insert into pxp.Graph values ( pNodeID, vParentID, pGraphCase);
	end loop;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--    Retrieve all dependencies from a nodeID
/* Notice: sources are on the side of parentNodeID and targets are nodeIDs
/*----------------------------------------------------------------------------*/
_FUNCTION( pxp.fn_getAllDependencyTargetNodes) ( pSourceNodeID _STRING, pGraphCase _INT)
_RETURN _CURSOR( pxp.graph)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.graph);
begin
    _IMPLEMENT_CURSOR( vCursor,
         with _RECURSIVE ChildrenGraph(nodeID, parentNodeID, graphCase) as (
		 	 select nodeID, parentNodeID, graphCase from pxp.Graph 
			 where parentNodeID = pSourceNodeID and graphCase = pGraphCase
			 union all
			 select g.nodeID, g.parentNodeID, g.graphCase from pxp.Graph g
			 join ChildrenGraph c on g.parentNodeID = c.nodeID
			 where g.graphCase = pGraphCase
		 ) select * from ChildrenGraph
	);
end 
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--    Retrieve direct dependencies from a nodeID
/* Notice: sources are on the side of parentNodeID and targets are nodeIDs
/*----------------------------------------------------------------------------*/
_FUNCTION( pxp.fn_getDirectDependencyTargetNodes) ( pSourceNodeID _STRING, pGraphCase _INT)
_RETURN _CURSOR( pxp.graph)
_IMPLEMENT_AS
    vCursor _RECORD_CURSOR( pxp.graph);
begin
    _IMPLEMENT_CURSOR( vCursor,
         select * from pxp.Graph 
			 where parentNodeID = pSourceNodeID and graphCase = pGraphCase
		 );
end 
_IMPLEMENT_END;
