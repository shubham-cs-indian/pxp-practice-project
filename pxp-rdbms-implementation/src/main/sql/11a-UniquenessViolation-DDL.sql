
/**
 * Author:  Harsh Shivhare
 * Created: April 7, 2020
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"
--
--   Product Identifier
--

_TABLE( pxp.UniquenessViolation) (
        sourceIID         _IID not null,
        targetIID         _IID not null,
        propertyIID       _IID not null,
        classifierIID     _IID not null,
        primary key ( sourceIID, targetIID, propertyIID, classifierIID)
);
_CREATE_INDEX1(UniquenessViolation, sourceIID);
_CREATE_INDEX1(UniquenessViolation, targetIID);


_TABLE( pxp.KpiUniquenessViolation) (
		ruleExpressionIID _IID not null,
        sourceIID         _IID not null,
        targetIID         _IID not null,
        primary key ( ruleExpressionIID, sourceIID, targetIID)
);
_CREATE_INDEX1(KpiUniquenessViolation, ruleExpressionIID);
_CREATE_INDEX1(KpiUniquenessViolation, sourceIID);
_CREATE_INDEX1(KpiUniquenessViolation, targetIID);