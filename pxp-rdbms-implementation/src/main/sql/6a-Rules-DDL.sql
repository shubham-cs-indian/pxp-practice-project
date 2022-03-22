/**
 * Author:  vallee
 * Created: Sep 22, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

_TABLE( pxp.RuleConfig ) (
	ruleCode			_VARCHAR  primary key,
	ruleType			_INT not null,
	lastEvaluatedByBGP	_LONG
);

_TABLE( pxp.RuleExpression ) (
	ruleExpressionIID	_IID  primary key,
	ruleCode			_VARCHAR not null,
	expression			_VARCHAR not null,
	localeIDs			_VARARRAY,
	catalogCodes		_VARARRAY,
	organizationCodes   _VARARRAY,
	forPropertyIIDs		_IIDARRAY,
	whenPropertyIIDs	_IIDARRAY
)
#ifdef ORACLE12C
nested table localeIDs store as localeIDsTAB 
nested table catalogCodes store as catalogCodesTAB 
nested table organizationCodes store as organizationCodesTAB
nested table forPropertyIIDs store as forPropertyIIDsTAB
nested table whenPropertyIIDs store as whenPropertyIIDsTAB
#endif
;

#ifdef PGSQL11
create index RuleExpression_localeIDs_idx on pxp.RuleExpression using gin(localeIDs);
create index RuleExpression_catalogCodes_idx on pxp.RuleExpression using gin(catalogCodes);
create index RuleExpression_organizationCodes_idx on pxp.RuleExpression using gin(organizationCodes);
create index RuleExpression_forPropertyIIDs_idx on pxp.RuleExpression using gin(forPropertyIIDs);
create index RuleExpression_whenPropertyIIDs_idx on pxp.RuleExpression using gin(whenPropertyIIDs);
#elif ORACLE12C
create bitmap index RuleExpression_localeIDs_idx on localeIDsTAB(column_value);
create bitmap index RuleExpression_catalogCodes_idx on catalogCodesTAB(column_value); 
create index RuleExpression_organizationCodes_idx on pxp.RuleExpression using gin(organizationCodes);
create bitmap index RuleExpression_forPropertyIIDs_idx on forPropertyIIDsTAB(column_value); 
create bitmap index RuleExpression_whenPropertyIIDs_idx on whenPropertyIIDsTAB(column_value);
#endif

_TABLE( pxp.BaseEntityKpiRuleLink ) (
	ruleExpressionIID	_IID not null,
	baseEntityIID		_IID not null,
	lastEvaluated		_LONG,
	localeID			_LOCALEID,
	kpi					_FLOAT,
	primary key( ruleExpressionIID, baseEntityIID, localeID)
);
_CREATE_INDEX1(BaseEntityKpiRuleLink, baseEntityIID);

_TABLE( pxp.BaseEntityQualityRuleLink ) (
	ruleExpressionIID	_IID not null,
	baseEntityIID		_IID not null,
	propertyIID			_IID not null,
	localeID			_LOCALEID,
	processID			_VARCHAR,
	qualityFlag			_INT,
	lastEvaluated		_LONG,
	message  		    _VARCHAR,
	primary key( ruleExpressionIID, baseEntityIID, propertyIID, localeID)
);
_CREATE_INDEX1(BaseEntityQualityRuleLink, baseEntityIID);
_CREATE_INDEX1(BaseEntityQualityRuleLink, qualityFlag);


 
 