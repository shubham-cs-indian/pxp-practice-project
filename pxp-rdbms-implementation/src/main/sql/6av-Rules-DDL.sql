/**
 * Author:  vallee
 * Created: Sep 22, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

-- View to get all violations
_VIEW(pxp.DQRule) as
 SELECT beqrl.qualityflag, count(be.baseentityiid) AS count,
#ifdef PGSQL11
    array_agg(be.baseentityiid) as baseentityiids
#elif ORACLE12C
    listagg(be.baseentityiid) within group (order by baseentityiid) as baseentityiids
#endif
from pxp.baseentityqualityrulelink beqrl join pxp.baseentity be on beqrl.baseentityiid = be.baseentityiid
group by beqrl.qualityflag;
  

_VIEW(pxp.KPI) as
 SELECT  re.ruleCode,cc.hierarchyiids,cc.classifiercode, avg(berl.kpi) as KPI, berl.localeid,
 berl.baseentityiid, be.catalogcode, be.organizationcode, resultset.tagvaluecode
 FROM pxp.baseentitykpirulelink berl
 JOIN pxp.ruleexpression re ON re.ruleexpressioniid = berl.ruleexpressioniid
 LEFT JOIN pxp.baseentity be on be.baseentityiid = berl.baseentityiid
 LEFT JOIN pxp.baseentityclassifierlink becl on becl.baseentityiid = berl.baseentityiid
 JOIN pxp.classifierconfig cc on (((cc.classifieriid = be.classifieriid) or (cc.classifieriid = becl.otherclassifieriid)))
 LEFT JOIN (SELECT entityiid , unnest(akeys(usrtags)) as tagvaluecode, unnest(avals(usrtags)) as relevance
 FROM pxp.tagsrecord)  resultset ON be.baseentityiid = resultset.entityiid
 group by re.ruleCode, cc.hierarchyiids, cc.classifiercode, berl.localeid, be.catalogcode, be.organizationcode, 
 resultset.tagvaluecode, berl.baseentityiid;
 
_VIEW(pxp.KPIGETALL) as
 SELECT  re.ruleCode,cc.hierarchyiids,cc.classifiercode, avg(berl.kpi) as KPI, berl.localeid,
 berl.baseentityiid, be.catalogcode, be.organizationcode
 FROM pxp.baseentitykpirulelink berl
 JOIN pxp.ruleexpression re ON re.ruleexpressioniid = berl.ruleexpressioniid
 LEFT JOIN pxp.baseentity be on be.baseentityiid = berl.baseentityiid
 LEFT JOIN pxp.baseentityclassifierlink becl on becl.baseentityiid = berl.baseentityiid
 JOIN pxp.classifierconfig cc on (((cc.classifieriid = be.classifieriid) or (cc.classifieriid = becl.otherclassifieriid)))
 group by berl.baseentityiid,cc.hierarchyiids, cc.classifiercode,re.ruleCode, berl.localeid, be.catalogcode, be.organizationcode;
 
 
 