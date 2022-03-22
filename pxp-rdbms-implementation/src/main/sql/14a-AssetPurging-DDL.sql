/**
 * Author:  Jamil Ahmad
 * Created: Nov 17, 2020
 */

 

#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"


_TABLE( pxp.AssetsToBePurged) (
    assetobjectkey       _VARCHAR,
    thumbkey             _VARCHAR,
    previewimagekey      _VARCHAR,
    type            _VARCHAR
);