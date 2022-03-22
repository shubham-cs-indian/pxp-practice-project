/**
 * Author:  Meetali saxena
 * Created: June 11, 2020
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"
--  Relation Couple Record table
/*  -----------------------
    The relationcouplerecord table is used to track the information of Relationship Couple  Information generated on Relationshipinheritance.
*/

_TABLE( pxp.relationcouplerecord ) (
	targetentityid				 _LONG    not null, 
	sourceentityid         		 _LONG    not null,
	couplingtype                 _INT     not null,
	naturerelationshipid         _LONG    not null,
	propagablerelationshipid     _LONG    not null,
	propagablerelationshipsideid _VARCHAR not null,
	isresolved                   _BOOLEAN not null,
	primary key (targetentityid, naturerelationshipid, propagablerelationshipsideid )
);

_CREATE_INDEX1(relationcouplerecord, targetentityid);


