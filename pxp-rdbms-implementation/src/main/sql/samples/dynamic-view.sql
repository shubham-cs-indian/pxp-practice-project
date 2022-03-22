/**
 * Author:  vallee
 * Created: Apr 6, 2019
 */
/*
    Basically, the with syntax (a.k.a common table expression = CTE) is like creating a temporary table
    with myNewTable (
        select * from ...
    )
    select * from somewherelse s
    join myNewTable m on m.id = s.id;
    ---
    A recursive CTE has been evaluated, but it appears no adapted to the research a value by locale ID order
*/
-- Version 2 with a stored function, a simple POC in postgresql shows the feasibility
create or replace function GetValueRecord( pbaseEntityIID bigint, inheritanceSchema varchar[]) returns pxp.ValueRecord%rowtype
as $body$ declare
	vContent pxp.ValueRecord%rowtype;
begin
	for vContent in 
		with locales(localeID,no) as ( select * from unnest( inheritanceSchema) with ordinality )
		select v.* from pxp.ValueRecord v
		join locales l on l.localeID = v.localeid
		where v.baseEntityIID = pbaseEntityIID 
		order by locales.no
	loop
		return vContent;
	end loop;
	return null;
end
$body$ language plpgsql;

-- Version 2: for searching operation, dynamic views remain applicable Example:

select b.entityiid, b.propertyiid, 
coalesce( v0.localeID, v1.localeID, v2.localeID, null) as localeID,
coalesce( v0.value, v1.value, v2.value, null) as value,
coalesce( v0.valueiid, v1.valueiid, v2.valueiid, null) as valueiid
from pxp.allvaluerecord b
left join pxp.allvaluerecord v0 on v0.entityiid = b.entityiid and v0.propertyiid = b.propertyiid and v0.localeID is null
left join pxp.allvaluerecord v1 on v1.entityiid = b.entityiid and v1.propertyiid = b.propertyiid and v1.localeID = 'fr_FR'
left join pxp.allvaluerecord v2 on v2.entityiid = b.entityiid and v2.propertyiid = b.propertyiid and v2.localeID = 'fr_CA'
;


-- Previous version
create or replace view pxp.valfr_FRfr_CAen_CAen_US as
select distinct b.baseentityiid, b.propertyrecordiid, b.contextiid, b.propertyiid, b.propertyid, b.propertyCode,
coalesce( v0.localeID, v1.localeID, v2.localeID, v3.localeID, v4.localeID, null) as localeID,
coalesce( v0.value, v1.value, v2.value, v3.value, v4.value, null) as value
from pxp.BaseEntityApplicableValueRecord b
left join pxp.BaseEntityApplicableValueRecord v0 on v0.baseentityiid = b.baseentityiid and v0.propertyiid = b.propertyiid and v0.localeID is null
left join pxp.BaseEntityApplicableValueRecord v1 on v1.baseentityiid = b.baseentityiid and v1.propertyiid = b.propertyiid and v1.localeID = 'fr_FR'
left join pxp.BaseEntityApplicableValueRecord v2 on v2.baseentityiid = b.baseentityiid and v2.propertyiid = b.propertyiid and v2.localeID = 'fr_CA'
left join pxp.BaseEntityApplicableValueRecord v3 on v3.baseentityiid = b.baseentityiid and v3.propertyiid = b.propertyiid and v3.localeID = 'en_CA'
left join pxp.BaseEntityApplicableValueRecord v4 on v4.baseentityiid = b.baseentityiid and v4.propertyiid = b.propertyiid and v4.localeID = 'en_US'
;

-- Final integration with base entity tracking
select e.*, coalesce( v.value, e.baseentityname) as localName from
pxp.BaseEntityTrackingWithName e
join pxp.valfr_FRfr_CAen_CAen_US v on v.baseEntityIID = e.baseEntityIID and v.propertyIID = 200
where e.baseEntityIID = 100002;

-- Gathering all information for a value record in locale hierarchy schema:
select v.baseEntityIID, v.propertyIID, v.propertyRecordIID, 
val.value, val.asHTML, val.asExpression, val.asNumber, val.localeID,
val.unitSymbol,
c.contextIID, c.contextID, c.contextID, t.tagValueiids, t.tagValueIDs
from pxp.valfr_FRfr_CAen_CAen_US v
join pxp.ValueRecord val on val.propertyRecordIID = v.propertyRecordIID
left join pxp.ContextualObject co on co.contextualObjectIID = v.propertyRecordIID
left join pxp.ContextConfig c on c.contextIID = co.contextIID
left join pxp.ContextTagRecordAggregated t on t.contextualObjectIID = val.propertyRecordIID
where v.baseEntityIID = 100002

-- Gathering all information for a tags record:
select p.baseEntityIID, p.propertyIID, t.propertyRecordIID, t.tagValueiids, t.tagValueIDs
from pxp.propertyRecord p
join pxp.TagRecordAggregated t on t.propertyRecordIID = p.propertyRecordIID
where p.baseEntityIID = 100002
