/**
 * Author:  vallee
 * Created: Sep 8, 2019
 *
 * Notice : upper case must be reserved to gcc constants and macros 
 */
-- coupling types subject to record cloning when a source record is updated 
#define _COUPLING_WITH_SEPARABLE_RECORDS (_CouplingType_TIGHT_RELATIONSHIP, _CouplingType_TIGHT_INHERITANCE, _CouplingType_DYN_DEFAULT, _CouplingType_TIGHT_DEFAULT)

-- coupling types subject to notification
#define _COUPLING_WITH_NOTIFICATION (_CouplingType_TIGHT_RELATIONSHIP, _CouplingType_TIGHT_INHERITANCE, _CouplingType_DYN_DEFAULT)

