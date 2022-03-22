/**
 * VIEW DEFINITIONs of Task Data for Base Entity
 * Author:  janak
 * Created: Oct 09, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

-- View of task aggregated with users
_VIEW(pxp.taskAggregatedUsers) as
select u.taskIID, 
#ifdef PGSQL11
    string_agg(u.userIID::varchar, ',') as userIIDs,
    string_agg(u.racivs::varchar, ',') as userRACIVSs,
    string_agg(uc.username::text, ','::text) AS usercodes
#elif ORACLE12C
    listagg(to_char(u.userIID), ',') within group (order by u.taskIID) as userIIDs,
    listagg(to_char(u.racivs), ',') within group (order by u.taskIID) as userRACIVSs,
    listagg(to_char(uc.username), ',') within group (order by u.taskIID) as usercodes
#endif
from pxp.taskuserlink u join pxp.userconfig uc on uc.useriid = u.useriid
group by taskIID;


-- View of task aggregated with roles
_VIEW(pxp.taskAggregatedRoles) as
select r.taskIID, 
#ifdef PGSQL11
    string_agg(r.roleCode::varchar, ',') as roleCodes,
    string_agg(r.rci::varchar, ',') as roleRCIs
#elif ORACLE12C
    listagg(r.roleCode, ',') within group (order by r.taskIID) as roleCodes,
    listagg(to_char(r.rci), ',') within group (order by r.taskIID) as roleRCIs
#endif
from pxp.taskrolelink r
group by taskIID;

-- View of task full information
_VIEW(pxp.taskfullrecord) as
select t.*, u.userIIDs, u.userRACIVSs, u.usercodes, r.roleCodes, r.roleRCIs
  from pxp.task t join pxp.taskAggregatedUsers u  on t.taskiid = u.taskiid left join pxp.taskAggregatedRoles r on r.taskiid = t.taskiid;

-- View of task entity information
_VIEW(pxp.taskentityrecord) as
select v.entityiid as baseentityiid, v.value as baseentityname, t.taskiid from pxp.task t
join pxp.baseentity b on b.baseentityiid = t.entityiid
join pxp.valuerecord v on t.entityiid = v.entityiid and v.localeid = b.baselocaleid and v.propertyiid = 200;

#ifdef PGSQL11
-- end of PGSQL Entity View declarations
#elif ORACLE12C
-- end of ORACLE Entity View declarations
#endif