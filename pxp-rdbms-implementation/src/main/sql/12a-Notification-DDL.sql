/**
 * TABLE DEFINITION of Notification DATA
 * Author:  Dhaval Bhadresha
 * Created: May 04, 2020
 */

#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

_CREATE_SEQUENCE( pxp.instanceIID, 1);

_TABLE( pxp.notification ) (
	instanceIID     _IID default _NEXTVAL(pxp.instanceIID) primary key, /* internal key: unique */
        actedFor        _LONG not null, 
        actedBy         _LONG not null,
        status          _VARCHAR,
        action          _VARCHAR,
        description     _VARCHAR not null,
        createdOn       _LONG not null)