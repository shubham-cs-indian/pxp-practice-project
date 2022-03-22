/**
 * Author:  vallee
 * Created: Jul 3, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

#undef _NB_PARTITIONS
#define _NB_PARTITIONS 16 /* when paritioning is active, 16 partitions are advised here for the dependency graph */

--  Graph table
/*  -----------------------
    The graph table is a flexible structure that can resolve mulitple graph representations of varchar Keys of various forms 
    It's primary use is for dependencies for coupling and calculation where the most dependent
    targets are leaf in the graph tree while the least dependent sources are root.
    The graphCase column permits to differentiate multiple graph trees so the table and its 
    associated stored procedures and triggers can serve multiple purposes in the future.    
*/
_TABLE( pxp.Graph ) (
	nodeID          _VARCHAR not null,
	parentNodeID	_VARCHAR not null,
	graphCase		_INT,
	primary key( nodeID, parentNodeID, graphCase)
) _PARTITION_BY_HASH( nodeID );
_CREATE_INDEX2(Graph, nodeID, graphCase);
_CREATE_INDEX2(Graph, parentNodeID, graphCase);


