
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

_PROCEDURE(pxp.sp_deleteGoldenRecordBuckets) (v_bucketIds _IIDARRAY)
_IMPLEMENT_AS
begin
   
     delete from pxp.goldenrecordbucketattributelink where bucketid = any(v_bucketIds);
    delete from pxp.goldenrecordbuckettaglink where bucketid = any(v_bucketIds);
    delete from pxp.goldenrecordbucketbaseentitylink where bucketid = any(v_bucketIds);
    delete from pxp.goldenrecordbucket where bucketid = any(v_bucketIds);
end
_IMPLEMENT_END;



