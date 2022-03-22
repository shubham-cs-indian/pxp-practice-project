/**
 * Author:  Vannya Kalani
 * Created: Mar 17, 2020
 */

#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

_CREATE_SEQUENCE(pxp.DownloadLogInformationTablePrimaryKey, 1);

_TABLE( pxp.DownloadLogInformation) (
	primaryKey					_IID default _NEXTVAL(DownloadLogInformationTablePrimaryKey) primary key,
	assetInstanceIId			_LONG,
	assetInstanceName			_VARCHAR,
	assetFileName				_VARCHAR,
	assetInstanceClassId		_VARCHAR,
	assetInstanceClassCode		_VARCHAR,
	renditionInstanceIId		_LONG default 0,
	renditionInstanceName		_VARCHAR,
	renditionFileName			_VARCHAR,
	renditionInstanceClassId	_VARCHAR,
	renditionInstanceClassCode	_VARCHAR,
	userId						_VARCHAR,
	timeStamp 					_LONG,
	comment						_VARCHAR,
	downloadId 					_VARCHAR,
	userName					_VARCHAR,
	isReset						_BOOLEAN
);
