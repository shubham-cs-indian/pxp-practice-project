/**
 * Author:  Mrunali Dhenge
 * Created: Mar 09, 2020
 */

#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

_CREATE_SEQUENCE(pxp.AssetMiscTablePrimaryKey, 1);

_TABLE( pxp.AssetMisc) (
	primaryKey			 _IID default _NEXTVAL(AssetMiscTablePrimaryKey) primary key,
	assetInstanceIId	 _LONG,
	renditionInstanceIId _LONG default 0,
	downloadTimeStamp 	 _LONG default 0,
	downloadCount		 _LONG default 0,
	sharedObjectId 		 _VARCHAR,
	sharedTimeStamp		 _LONG default 0	
); 


