/**
 * VIEW DEFINITIONs of ENTITY DATA
 * Author:  vallee
 * Created: Apr 5, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

/* This view is used to get all non-nature classifers for given baseentityIID*/
-- View baseentityOtherClassifiers
/*
_VIEW(pxp.baseentityOtherClassifiers) as 
select l.baseentityiid, cc.classifieriid, cc.classifierCode, cc.classifiertype
    from pxp.baseEntityClassifierLink l
    join pxp.classifierconfig cc on cc.classifieriid = l.otherClassifieriid;
*/

-- View baseentityAggregatedOtherClassifiers
/* This view is used with tracking base entity information in order to build BaseEntityDTO from a single request */
_VIEW(pxp.baseentityAggregatedOtherClassifiers) as
select l.baseentityiid, 
#ifdef PGSQL11
    string_agg(l.otherClassifierIID::varchar, ',') as classifierIIDs
#elif ORACLE12C
    listagg( l.otherClassifierIID, ',') within group (order by l.baseentityiid) as classifierIIDs
#endif
from pxp.baseEntityClassifierLink l
group by baseentityiid;


-- View BaseEntityTrackingFullContent = collect all elements to build up a full BaseEntityDTO
_VIEW(pxp.BaseEntityTrackingFullContent) as
    select e.*, u1.userName as createUserName, u2.userName as modifyUserName,oc.classifierIIDs,
           coo.contextCode, 
           lower(coo.cxttimerange) as cxtStartTime,
           upper(coo.cxttimerange) as cxtEndTime,
           coo.cxtTags
    from pxp.BaseEntity e
    join pxp.UserConfig u1 on u1.userIID = e.createUserIID
    join pxp.UserConfig u2 on u2.userIID = e.lastModifiedUserIID
    left join pxp.baseentityAggregatedOtherClassifiers oc on oc.baseEntityIID = e.baseEntityIID
    left join pxp.contextualObject coo on coo.contextualObjectIID = e.contextualObjectIID;

-- View BaseEntityTrackingFullContent + name in baseLocaleID
/* /!\ it is expected the name mandatory exists (even empty) in the baseLocaleID */
_VIEW(pxp.BaseEntityTrackingFullContentWithBaseName) as 
    select e.*, COALESCE (rv.value, 'extensionentity')  as baseentitybasename
    from pxp.BaseEntityTrackingFullContent e
    left join pxp.valuerecord rv on rv.entityIID = e.baseentityIID
    and rv.localeID = e.baseLocaleID and rv.propertyIID = _ARTICLE_NAME_PROPERTY_IID;   

_VIEW( pxp.valueRecordFullContent ) as
select  val.*, pc.propertyCode, pc.propertyType,
        coo.contextcode, 
        lower(coo.cxttimerange) as cxtStartTime,
        upper(coo.cxttimerange) as cxtEndTime,
        coo.cxtTags
    from pxp.ValueRecord val join pxp.PropertyConfig pc on val.propertyIID = pc.propertyIID
    left join pxp.contextualObject coo on coo.contextualObjectIID = val.contextualObjectIID;

-- View tagsRecordWithProperty
_VIEW( pxp.tagsRecordWithProperty ) as
select tr.*, pc.propertyCode, pc.propertyType
    from pxp.TagsRecord tr
    join pxp.PropertyConfig pc on tr.propertyIID = pc.propertyIID;

-- View to load full information for RelationsSet side 1 / side 2
/* used to load RelationsSet side 1 */
_VIEW( pxp.RelationsSetSide1FullContent ) as
select r.*, r.side1contextualobjectiid as contextualobjectiid,
    co.contextcode, 
    lower(co.cxttimerange) as cxtStartTime,
    upper(co.cxttimerange) as cxtEndTime,
 	co.cxtTags
from pxp.relation r 
left join pxp.contextualObject co on co.contextualobjectiid = r.side1contextualobjectiid;
/* used to load RelationsSet side 2 */
_VIEW( pxp.RelationsSetSide2FullContent ) as
select  r.*, r.side2contextualobjectiid as contextualobjectiid,
        co.contextcode,
    	lower(co.cxttimerange) as cxtStartTime,
        upper(co.cxttimerange) as cxtEndTime,
        co.cxtTags
from pxp.relation r 
left join pxp.contextualObject co on co.contextualobjectiid = r.side2contextualobjectiid;

-- View of contextual linked entities
_VIEW(pxp.BaseEntityContextualLinkedEntities) as 
     select c.contextualobjectiid, e.baseEntityIID, e.baseEntityID, e.baseType, e.classifierIID, e.catalogCode, e.organizationCode, e.baseLocaleID, 
            r.value as baseEntityName 
     from pxp.contextBaseEntityLink c 
     join pxp.baseEntity e on c.baseEntityIID = e.baseEntityIID 
     join pxp.valuerecord r on r.entityIID = e.baseentityIID and r.localeID = e.baseLocaleID and r.propertyIID = _ARTICLE_NAME_PROPERTY_IID;
    
-- View contextaul object with linked entities IIDs
/* This view is used when contextual object with linked entities */
_VIEW(pxp.ContextualObjectAggregatedLinkedEntities) as
    select co.*, cl.cxtlinkedBaseEntitiyIIDs
    from pxp.contextualObject co
    left join ( select
        contextualobjectiid, 
#ifdef PGSQL11
        string_agg(baseentityiid::varchar, ',') as cxtlinkedBaseEntitiyIIDs
#elif ORACLE12C
        listagg(baseentityiid, ',') within group (order by contextualobjectiid) as cxtlinkedBaseEntitiyIIDs
#endif
        from pxp.contextbaseentitylink group by contextualobjectiid
    ) cl on cl.contextualobjectiid = co.contextualobjectiid;
     
-- View of baseentity with context information
_VIEW(pxp.BaseEntityWithContextualData) as 
    select e.*, lower(coo.cxttimerange)as cxtStartTime,
          upper(coo.cxttimerange) as cxtEndTime,
           coo.contextcode, coo.cxtTags, coo.cxtlinkedBaseEntitiyIIDs
    from pxp.baseentity e  
    join pxp.ContextualObjectAggregatedLinkedEntities coo on coo.contextualObjectIID = e.contextualObjectIID;

-- View of coupled/direct tags record
_VIEW(pxp.CoupledTagsRecord) as
	select * from pxp.fn_getAllTagsRecordFromCoupledRecords() as f(
		propertyiid _IID,
    	entityiid _IID,
    	recordstatus _SHORT,
    	couplingtype _SHORT,
    	couplingbehavior _SHORT,
    	coupling _VARCHAR,
    	masterentityiid _IID,
    	masterpropertyiid _IID,
		usrTags			hstore
);    

_VIEW(pxp.DirectTagsRecord) as
    select t.propertyIID, t.entityIID, t.recordStatus, 
    _CouplingType_NONE as couplingtype, _CouplingBehavior_NONE as couplingbehavior, null as coupling,
    _TO_LONG(null) as masterEntityIID, _TO_LONG(null) as masterPropertyIID,
    t.usrTags
    from pxp.tagsrecord t where t.recordStatus = _RecordStatus_DIRECT
	union all
    select t.propertyIID, t.entityIID, t.recordStatus, 
    c.couplingType, c.couplingbehavior, c.coupling,
    c.masterEntityIID, c.masterPropertyIID,
    t.usrTags
	from pxp.tagsrecord t 
	join pxp.CoupledRecord c on c.entityIID = t.entityIID and c.propertyIID = t.propertyIID and c.recordStatus = _RecordStatus_NOTIFIED
    where t.recordStatus = _RecordStatus_FORKED;
_VIEW(pxp.AllTagsRecord) as
    select * from pxp.CoupledTagsRecord where recordStatus <> _RecordStatus_NOTIFIED
    union all
    select * from pxp.DirectTagsRecord;

-- View of coupled/direct value record
_VIEW(pxp.CoupledValueRecord) as
    select * from pxp.fn_getAllValueRecordFromCoupledRecords() as f(
	propertyiid _IID,
    entityiid _IID,
    recordstatus _SHORT,
    couplingtype _SHORT,
    couplingbehavior _SHORT,
    coupling _VARCHAR,
    masterentityiid _IID,
    masterpropertyiid _IID,
    valueiid _IID,
    localeid _VARCHAR,
    contextualobjectiid _IID,
    value _VARCHAR,
    ashtml _TEXT,
    asnumber _FLOAT,
    unitsymbol _VARCHAR,
    calculation _VARCHAR
);
    
_VIEW(pxp.DirectValueRecord) as
    select v.propertyIID, v.entityIID, v.recordStatus, 
    _CouplingType_NONE as couplingtype, _CouplingBehavior_NONE as couplingbehavior, null as coupling,
    _TO_LONG(null) as masterEntityIID, _TO_LONG(null) as masterPropertyIID,
    v.valueiid, v.localeid, v.contextualobjectiid, v.value, v.ashtml, v.asnumber, v.unitsymbol, v.calculation
    from pxp.valuerecord v where v.recordStatus = _RecordStatus_DIRECT
	union all
    select v.propertyIID, v.entityIID, v.recordStatus, 
    c.couplingType, c.couplingbehavior, c.coupling,
    c.masterEntityIID, c.masterPropertyIID,
    v.valueiid, v.localeid, v.contextualobjectiid, v.value, v.ashtml, v.asnumber, v.unitsymbol, v.calculation
	from pxp.Valuerecord v 
	join pxp.CoupledRecord c on c.entityIID = v.entityIID and c.propertyIID = v.propertyIID and c.recordStatus = _RecordStatus_NOTIFIED
    where v.recordStatus = _RecordStatus_FORKED;
_VIEW(pxp.AllValueRecord) as
    select * from pxp.CoupledValueRecord where recordStatus <> _RecordStatus_NOTIFIED
    union all
    select * from pxp.DirectValueRecord;

-- View of coupled/direct relation record side 1
_VIEW(pxp.CoupledRelationSide1) as
    select a.propertyIID, a.entityIID, a.recordStatus, a.couplingtype, a.couplingbehavior, a.coupling, 
    a.masterEntityIID, a.masterPropertyIID, r.side2contextualobjectiid as othersidecontextualobject,
    r.side1contextualobjectiid as sidecontextualobject, r.side2entityIID as sideentityIID, 1 as side
    from pxp.CoupledRecord a
    join pxp.relation r on r.side1entityIID = a.masterEntityiid and r.propertyiid = a.masterpropertyiid;
_VIEW(pxp.DirectRelationSide1) as
    select r.propertyIID, r.side1entityIID as entityIID, _RecordStatus_DIRECT as recordStatus, 
    _CouplingType_NONE as couplingtype, _CouplingBehavior_NONE as couplingbehavior, null as coupling,
    _TO_LONG(null) as masterEntityIID, _TO_LONG(null) as masterPropertyIID, r.side2contextualobjectiid as othersidecontextualobject,
    r.side1contextualobjectiid as sidecontextualobject, r.side2entityIID as sideentityIID, 1 as side
    from pxp.relation r where not exists(select 1 from pxp.coupledRecord c where c.entityIID = r.side1entityIID and c.propertyIID = r.propertyIID)
	union
    select r.propertyIID, r.side1entityIID as entityIID, _RecordStatus_FORKED as recordStatus, 
    c.couplingType, c.couplingbehavior, c.coupling,
    c.masterEntityIID, c.masterPropertyIID, r.side2contextualobjectiid as othersidecontextualobject,
	r.side1contextualobjectiid as sidecontextualobject, r.side2entityIID as sideentityIID, 1 as side
	from pxp.relation r
 	join pxp.CoupledRecord c on c.entityIID = r.side1entityIID and c.propertyIID = r.propertyIID and c.recordStatus = _RecordStatus_NOTIFIED;
_VIEW(pxp.AllRelationSide1) as
    select * from pxp.CoupledRelationSide1 where recordStatus <> _RecordStatus_NOTIFIED
    union
    select * from pxp.DirectRelationSide1;

-- View of coupled/direct relation record side 2
_VIEW(pxp.CoupledRelationSide2) as
    select a.propertyIID, a.entityIID, a.recordStatus, a.couplingtype, a.couplingbehavior, a.coupling, 
    a.masterEntityIID, a.masterPropertyIID, r.side1contextualobjectiid as othersidecontextualobject,
    r.side2contextualobjectiid as sidecontextualobject, r.side1entityIID as sideentityIID, 2 as side
    from pxp.CoupledRecord a
    join pxp.relation r on r.side2entityIID = a.masterEntityiid and r.propertyiid = a.masterpropertyiid;
_VIEW(pxp.DirectRelationSide2) as
    select r.propertyIID, r.side2entityIID as entityIID, _RecordStatus_DIRECT as recordStatus, 
    _CouplingType_NONE as couplingtype, _CouplingBehavior_NONE as couplingbehavior, null as coupling,
    _TO_LONG(null) as masterEntityIID, _TO_LONG(null) as masterPropertyIID, r.side1contextualobjectiid as othersidecontextualobject,
    r.side2contextualobjectiid as sidecontextualobject, r.side1entityIID as sideentityIID, 2 as side
    from pxp.relation r where not exists(select 1 from pxp.coupledRecord c where c.entityIID = r.side2entityIID and c.propertyIID = r.propertyIID)
	union
    select r.propertyIID, r.side2entityIID as entityIID, _RecordStatus_FORKED as recordStatus, 
    c.couplingType, c.couplingbehavior, c.coupling,
    c.masterEntityIID, c.masterPropertyIID, r.side1contextualobjectiid as othersidecontextualobject,
	r.side2contextualobjectiid as sidecontextualobject, r.side1entityIID as sideentityIID, 2 as side
	from pxp.relation r
 	join pxp.CoupledRecord c on c.entityIID = r.side1entityIID and c.propertyIID = r.propertyIID and c.recordStatus = _RecordStatus_NOTIFIED;
_VIEW(pxp.AllRelationSide2) as
    select * from pxp.CoupledRelationSide2 where recordStatus <> _RecordStatus_NOTIFIED
    union
    select * from pxp.DirectRelationSide2;

-- View all properties with record status
_VIEW(pxp.AllRecordWithStatus) as
select entityIID, propertyIID, recordStatus, couplingType, couplingbehavior, 0 as side from pxp.AllValueRecord
union
select entityIID, propertyIID, recordStatus, couplingType, couplingbehavior, 0 as side from pxp.AllTagsRecord
union
select entityIID, propertyIID, recordStatus, couplingType, couplingbehavior, side from pxp.AllRelationSide1
union
select entityIID, propertyIID, recordStatus, couplingType, couplingbehavior, side from pxp.AllRelationSide2;


_VIEW(pxp.allrelations) as
 SELECT allrelationside1.propertyiid,
    allrelationside1.entityiid,
    allrelationside1.recordstatus,
    allrelationside1.couplingtype,
    allrelationside1.couplingbehavior,
    allrelationside1.coupling,
    allrelationside1.masterentityiid,
    allrelationside1.masterpropertyiid,
    allrelationside1.othersidecontextualobject,
    allrelationside1.sidecontextualobject,
    allrelationside1.sideentityiid,
    allrelationside1.side
   FROM allrelationside1
UNION
 SELECT allrelationside2.propertyiid,
    allrelationside2.entityiid,
    allrelationside2.recordstatus,
    allrelationside2.couplingtype,
    allrelationside2.couplingbehavior,
    allrelationside2.coupling,
    allrelationside2.masterentityiid,
    allrelationside2.masterpropertyiid,
    allrelationside2.othersidecontextualobject,
    allrelationside2.sidecontextualobject,
    allrelationside2.sideentityiid,
    allrelationside2.side
   FROM allrelationside2;

ALTER TABLE pxp.allrelations
    OWNER TO pxp;

#ifdef PGSQL11
-- end of PGSQL Entity View declarations
#elif ORACLE12C
-- end of ORACLE Entity View declarations
#endif


    
    
    
