
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"


_TABLE( pxp.goldenrecordbucket ) (
	bucketid                     _LONG    not null,
	organisationCode 			 _VARCHAR not null,
	issearchable                 _BOOLEAN not null,
	catalogCode 		  		 _VARCHAR not null,
	endpointCode 		 		 _VARCHAR not null,
	ruleId 		   		 		 _VARCHAR not null,
	createdTime 		   		 _LONG not null,
	lastModifiedTime 		   	 _LONG not null,
	primary key (bucketid)
);


_TABLE( pxp.goldenrecordbucketattributelink ) (
	bucketid                     _LONG    not null,
	attributeid 				 _VARCHAR not null,
	value   		 			 _VARCHAR not null
);

_FOREIGN_KEY(goldenrecordbucketattributelink, goldenrecordbucket, bucketid, bucketid);

_TABLE( pxp.goldenrecordbuckettaglink ) (
	bucketid                    _LONG    not null,
	tagid 				 		_VARCHAR not null,
	value   					_HSTORE
);

_FOREIGN_KEY(goldenrecordbuckettaglink, goldenrecordbucket, bucketid, bucketid);



_TABLE( pxp.goldenrecordbucketbaseentitylink ) (
	bucketid                    _LONG    not null,
	baseentityIID               _IID     not null
);

_FOREIGN_KEY(goldenrecordbucketbaseentitylink, goldenrecordbucket, bucketid, bucketid);

_CREATE_SEQUENCE( pxp.seqBucketID, 100);



