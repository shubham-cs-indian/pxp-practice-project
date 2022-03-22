/**
 * Author:  vallee
 * Created: Sep 22, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

/*----------------------------------------------------------------------------*/
--   Create Rule
/*----------------------------------------------------------------------------*/
_FUNCTION( pxp.sp_ruleConfig )(p_ruleExpressionIID _IID, p_ruleCode _STRING, p_ruleType _INT, p_expression _STRING,
 p_localeIDs _VARARRAY, p_catalogCodes _VARARRAY, p_organizationCodes _VARARRAY, p_forPropertyIIDs _IIDARRAY, p_whenPropertyIIDs _IIDARRAY)
 _RETURN _IID
_IMPLEMENT_AS
	vRuleCode			_VARCHAR;
	vRuleExpressionIID  _IID;
begin
    -- check rule existence
    begin
        select ruleCode into vRuleCode from pxp.ruleConfig 
        where ruleCode = p_ruleCode;
    exception
        when no_data_found then vRuleCode := null;
    end;
    -- lazy initialization of config tables
    if (vRuleCode is null) then
        insert into pxp.RuleConfig (rulecode, ruletype) values (p_ruleCode, p_ruleType);
    end if;
    
    begin
        select ruleCode into vRuleCode from pxp.ruleexpression where ruleExpressionIID = p_ruleExpressionIID;
    exception
        when no_data_found then vRuleCode := null;
    end;
    
    if(vRuleCode is not null) then
        update pxp.ruleexpression
        set
        expression = p_expression,
        localeIDs = p_localeIDs,
        catalogCodes = p_catalogCodes,
        organizationCodes = p_organizationCodes,
        forPropertyIIDs = p_forPropertyIIDs,
        whenPropertyIIDs = p_whenPropertyIIDs
        where ruleExpressionIID = p_ruleExpressionIID;
    else
    	insert into 
    	pxp.ruleexpression(ruleExpressionIID, ruleCode, expression, localeIDs, catalogCodes, organizationCodes, forPropertyIIDs, whenPropertyIIDs)
    	values 
        (p_ruleExpressionIID, p_ruleCode, p_expression, p_localeIDs, p_catalogCodes, p_organizationCodes, p_forPropertyIIDs, p_whenPropertyIIDs);
    end if;
    vRuleExpressionIID := p_ruleExpressionIID;
    return vRuleExpressionIID;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--   Upsert KPI
/*----------------------------------------------------------------------------*/

_PROCEDURE( pxp.sp_kpiUpsert )(p_ruleExpressionIID _IID, p_baseentityiid _IID, p_KPI _FLOAT, p_currentTime _LONG, p_localeid _STRING)
_IMPLEMENT_AS
	vRuleExpressionIID  _IID;
begin
    -- check rule existence
    begin
        select ruleexpressioniid into vRuleExpressionIID from pxp.baseentitykpirulelink 
        where ruleexpressioniid = p_ruleExpressionIID and baseentityiid = p_baseentityiid and localeid = p_localeid;
    exception
        when no_data_found then vRuleExpressionIID := null;
    end;
    -- lazy initialization of config tables
    if (vRuleExpressionIID is not null) then
        update pxp.baseentitykpirulelink 
        set
        ruleexpressioniid = p_ruleExpressionIID,
        baseentityiid = p_baseentityiid,
        kpi = p_KPI,
        lastevaluated = p_currentTime, 
        localeid = p_localeid
       	where ruleexpressioniid = p_ruleExpressionIID and baseentityiid = p_baseentityiid and localeid = p_localeid;
    else
    	insert into pxp.baseentitykpirulelink(ruleexpressioniid, baseentityiid, kpi, lastevaluated, localeid) 
       values (p_ruleExpressionIID, p_baseentityiid, p_KPI, p_currentTime, p_localeid);
    end if;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
--   Upsert VIOLATION
/*----------------------------------------------------------------------------*/

_PROCEDURE( pxp.sp_violationUpsert )(p_ruleExpressionIID _IID, p_baseentityiid _IID, p_propertyiid _IID, p_localeid _STRING, p_qualityflag _INT, p_currentTime _LONG, p_message _STRING)
_IMPLEMENT_AS
	vRuleExpressionIID  _IID;
begin
    -- check rule existence
    begin
        select ruleexpressioniid INTO vRuleExpressionIID from pxp.baseentityqualityrulelink 
        where ruleexpressioniid = p_ruleExpressionIID and baseentityiid = p_baseentityiid and propertyiid = p_propertyiid and localeid = p_localeid;
    exception
        when no_data_found then vRuleExpressionIID := null;
    end;
    -- lazy initialization of config tables
    IF (vRuleExpressionIID is null) THEN
      	insert into pxp.baseentityqualityrulelink(ruleexpressioniid, baseentityiid, propertyiid, localeid, qualityflag, lastevaluated, message) 
    	values(p_ruleExpressionIID, p_baseentityiid, p_propertyiid, p_localeid, p_qualityflag, p_currentTime, p_message);
    ELSE
        update pxp.baseentityqualityrulelink 
        set
        ruleexpressioniid = p_ruleExpressionIID,
        baseentityiid = p_baseentityiid,
        propertyiid = p_propertyiid,
        localeid = p_localeid,
        qualityflag = p_qualityflag,
        lastevaluated = p_currentTime,
        message = p_message
       	where ruleexpressioniid = p_ruleExpressionIID and baseentityiid = p_baseentityiid and propertyiid = p_propertyiid;
  
    end if;
end
_IMPLEMENT_END;


/*-----------------------------------------------*/
--  Update instances on kpi delete
/*-----------------------------------------------*/
_PROCEDURE( pxp.sp_updateinstancesonkpidelete )( p_rulecodes _VARARRAY)
_IMPLEMENT_AS
	vRuleCodeIdx _INT;
    	vRuleCode _VARCHAR;
begin

	for vRuleCodeIdx in 1 .. _COUNT(p_rulecodes) 
	loop
        	vRuleCode := _ARRAY(p_rulecodes, vRuleCodeIdx);	
	
		delete FROM pxp.baseentitykpirulelink WHERE ruleexpressioniid IN 
		(select ruleexpressioniid  from pxp.ruleexpression where rulecode = vRuleCode);

		delete FROM pxp.kpiuniquenessviolation WHERE ruleexpressioniid IN 
		(select ruleexpressioniid  from pxp.ruleexpression where rulecode = vRuleCode);

		delete from pxp.ruleexpression where rulecode = vRuleCode;

		delete from pxp.ruleconfig where rulecode = vRuleCode;
	
	end loop;  
end
_IMPLEMENT_END;