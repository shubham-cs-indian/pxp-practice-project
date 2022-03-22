/**
 * Author:  vallee
 * Created: Aug 27, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

-- View to list the tagcodes and range with respect to contextualobjectiid
#ifdef PGSQL11
_VIEW (pxp.CxtTagValueCodesLTag) as 
    with cxt as
        (select contextualobjectiid, concat( skeys(cxtTags), '[', svals(cxtTags), ']') as cxt_codes
	     from pxp.contextualobject)
	     select contextualobjectiid, cxt_codes from cxt;
#elif ORACLE12C
_VIEW (pxp.CxtTagValueCodesLTag) as
    with
        ctxrange(rnum, contextualobjectiid, ranges)  as (select rownum, contextualobjectiid, column_value  from pxp.contextualobject, table(cxtranges)),
        ctxcodes(rnum, contextualobjectiid, codes) as (select rownum, contextualobjectiid, column_value from pxp.contextualobject, table(cxttagvaluecodes))
    (select co.contextualobjectiid, concat('{ "$tag": "' || cc.codes || '", ' , '"range": ' || cr.ranges || '}') as cxt_codes
     from pxp.contextualobject co
     join ctxrange cr on cr.contextualobjectiid = co.contextualobjectiid
     join ctxcodes cc on cc.contextualobjectiid = co.contextualobjectiid and cc.rnum = cr.rnum);
#endif

-- PXON Contextual Object aggregate
_VIEW (pxp.CxtTagValueCodesPXONAGG) as
    select lt.contextualobjectiid,
#ifdef PGSQL11
    string_agg(lt.cxt_codes, ',') ltag
#elif ORACLE12C
    listagg(lt.cxt_codes, ',') within group (order by lt.contextualobjectiid) as ltag
#endif
    from pxp.CxtTagValueCodesLTag lt group by contextualobjectiid;

-- Entity Link PXON a reusable view for every entity link information like parent, origin, extension, side1, side2
_VIEW (pxp.EntityLinkPXON) as
    select e.baseentityIID,
        '[e>' || _TO_VARCHAR(e.baseEntityID) || ' $ctlg=' || e.catalogCode || ' $org=' || e.organizationcode || ' $type=' ||  ec.label || ']' as entity
	from pxp.BaseEntity e
    join pxp.enumcode ec on ec.type = _ENUMTYPE_BASETYPE and ec.code = e.basetype;

-- PXON Contextual Object
_VIEW (pxp.ContextualObjectPXON) as
    select co.contextualobjectiid,
        '[x>' || co.contextcode || 
       (case when ca.ltag is not null then' $tag=' || ca.ltag else '' end )||
       (case when lower(co.cxttimerange) is not null then ' $start=' || lower(co.cxttimerange) else '' end)||  
       (case when upper(co.cxttimerange) is not null then ' $end=' || upper(co.cxttimerange) else '' end)||  
        ' $type=' || ec.label || ']' as csid,
        co.contextcode
    from pxp.contextualobject co
    join pxp.contextconfig cc on cc.contextcode = co.contextcode
    join pxp.enumcode ec on ec.type = _ENUMTYPE_CONTEXTTYPE and ec.code = cc.contexttype
    left join pxp.CxtTagValueCodesPXONAGG ca on ca.contextualobjectiid = co.contextualobjectiid;

-- PXON Base Entity views
_VIEW ( pxp.BaseEntityPXON ) as
    select e.baseentityIID,
        '[e>' || e.baseEntityID ||' $ctlg=' || e.catalogCode || ' $org='	|| e.organizationcode ||' $type=' || ec.label || ']' as csid,
        cl.csid as Nature,
        e.childlevel as childlevel,
        e.baselocaleID as baselocale,
        e1.entity as parent,
        e2.entity as top,
        e3.entity as origin,
        e4.entity as Img,
        co.csid as cxt,
        e.parentIID,
        e.embeddedtype,
        e.entityExtension,
        e.hashCode,
        uc1.username as createdusername,
        e.createtime,
        uc2.username as lastusername,
        e.lastmodifiedtime,
        e.isExpired
    from pxp.baseentity e
    join pxp.classifierConfigPXON cl on cl.classifierIID = e.classifierIID
    join pxp.enumCode ec on ec.type = _ENUMTYPE_BASETYPE and ec.code = e.basetype
    join pxp.userconfig uc1 on uc1.userIID = e.createuserIID
    join pxp.userconfig uc2 on uc2.userIID = e.lastmodifieduserIID
    left join pxp.EntityLinkPXON e1 on e1.baseentityIID = e.parentiid
    left join pxp.EntityLinkPXON e2 on e2.baseentityIID = e.topparentiid
    left join pxp.EntityLinkPXON e3 on e3.baseentityIID = e.originbaseentityiid
    left join pxp.EntityLinkPXON e4 on e4.baseentityIID = e.defaultimageiid
    left join pxp.ContextualObjectPXON co on co.contextualobjectiid = e.contextualobjectiid;

_VIEW (pxp.BaseEntityClassifierLinkPXON) as
    select bcl.baseentityiid,
        '[c>' ||  cc.classifiercode || ' $type=' || ec.label || ']' as csid
    from pxp.baseentityclassifierlink bcl
    join pxp.classifierconfig cc on cc.classifieriid = bcl.otherclassifieriid
    join pxp.enumcode ec on ec.type = _ENUMTYPE_CLASSIFIERTYPE and ec.code = cc.classifiertype;

-- View to list the tagcodes and range with respect to contextualobjectiid
#ifdef PGSQL11
_VIEW (pxp.TagsRecordPXONAGGLTag) as
    with tr as
        (select entityiid, propertyiid,
                concat( '{ "$tag": "', skeys (usrtags), '" , "range": ', svals(usrtags), '}') as code_range
	     from pxp.tagsrecord)
	     select entityiid, propertyiid, code_range from tr;
#elif ORACLE12C
_VIEW (pxp.TagsRecordPXONAGGLTag) as
    with
        trrange(rnum, entityiid, propertyiid, ranges)  as (select rownum, entityiid, propertyiid, column_value  from pxp.tagsrecord, table(tagranges)),
        trcodes(rnum, entityiid, propertyiid, codes) as (select rownum, entityiid, propertyiid, column_value from pxp.tagsrecord, table(tagvaluecodes))
    (select r.entityiid, r.propertyiid, concat('{ "$tag": "' || tc.codes || '", ' , '"range": ' || tr.ranges || '}') as code_range
     from pxp.tagsrecord r
     join trrange tr on tr.entityiid = r.entityiid and tr.propertyiid = r.propertyiid
     join trcodes tc on tc.entityiid = r.entityiid and tc.propertyiid = r.propertyiid and tc.rnum = tr.rnum);
#endif

-- PXON Tag Record aggregate
_VIEW (pxp.TagsRecordPXONAGG) as
    select lt.entityiid, lt.propertyiid,
#ifdef PGSQL11
    string_agg(lt.code_range, ',') ltag
#elif ORACLE12C
    listagg(lt.code_range, ',') within group (order by lt.entityiid, lt.propertyiid) as ltag
#endif
    from pxp.TagsRecordPXONAGGLTag lt group by entityiid, propertyiid;

-- PXON Tags Record
_VIEW (pxp.TagsRecordPXON) as
    select tr.entityIID,
        tr.propertyIID,
        '[t>' || _TO_PXONCODE(p.propertyCode) || ' $type=' || p.label ||']' as csid,
        p.csid as Prop,
        coalesce(ec1.label, 'NONE') as cpl,
        coalesce(ec2.label, 'DIRECT') as status,
        cu.LTag
    from pxp.tagsrecord tr
    join pxp.TagsRecordPXONAGG cu on cu.entityIID = tr.entityIID and cu.propertyIID = tr.propertyIID
    join pxp.PropertyConfigPXON p on p.propertyIID = tr.propertyIID
    left join pxp.coupledrecord cr on cr.entityIID = tr.entityIID and cr.propertyIID = tr.propertyIID
    left join pxp.enumcode ec1 on ec1.type = _ENUMTYPE_COUPLINGTYPE and ec1.code = cr.couplingtype
    left join pxp.enumcode ec2 on ec2.type = _ENUMTYPE_RECORDSTATUS and ec2.code = cr.recordstatus;

-- View to list all tagcodes and range with respect to contextualobjectiid
#ifdef PGSQL11
_VIEW (pxp.AllTagsRecordPXONAGGLTag) as
    with tr as
        (select entityiid, propertyiid,
                concat( '{ "$tag": "', skeys (usrtags), '" , "range": ', svals(usrtags), '}') as code_range
         from pxp.alltagsrecord)
         select entityiid, propertyiid, code_range from tr;
#elif ORACLE12C
_VIEW (pxp.AllTagsRecordPXONAGGLTag) as
    with
        trrange(rnum, entityiid, propertyiid, ranges)  as (select rownum, entityiid, propertyiid, column_value  from pxp.alltagsrecord, table(tagranges)),
        trcodes(rnum, entityiid, propertyiid, codes) as (select rownum, entityiid, propertyiid, column_value from pxp.alltagsrecord, table(tagvaluecodes))
    (select r.entityiid, r.propertyiid, concat('{ "$tag": "' || tc.codes || '", ' , '"range": ' || tr.ranges || '}') as code_range
     from pxp.alltagsrecord r
     join trrange tr on tr.entityiid = r.entityiid and tr.propertyiid = r.propertyiid
     join trcodes tc on tc.entityiid = r.entityiid and tc.propertyiid = r.propertyiid and tc.rnum = tr.rnum);
#endif

-- PXON Tag Record aggregate
_VIEW (pxp.AllTagsRecordPXONAGG) as
    select lt.entityiid, lt.propertyiid,
#ifdef PGSQL11
    string_agg(lt.code_range, ',') ltag
#elif ORACLE12C
    listagg(lt.code_range, ',') within group (order by lt.entityiid, lt.propertyiid) as ltag
#endif
    from pxp.AllTagsRecordPXONAGGLTag lt group by entityiid, propertyiid;

-- PXON All Tags Record
_VIEW (pxp.AllTagsRecordPXON) as
    select tr.entityIID,
        tr.propertyIID,
        '[t>' || _TO_PXONCODE(p.propertyCode) || ' $type=' || p.label ||']' as csid,
        p.csid as Prop,
        (case when ec1.label = 'undefined' THEN 'UNDEFINED'
                   else coalesce(ec1.label, 'NONE') end) as cpl,
        coalesce(ec2.label, 'DIRECT') as status,
        cu.LTag
    from pxp.alltagsrecord tr
    join pxp.AllTagsRecordPXONAGG cu on cu.entityIID = tr.entityIID and cu.propertyIID = tr.propertyIID
    join pxp.PropertyConfigPXON p on p.propertyIID = tr.propertyIID
    left join pxp.coupledrecord cr on cr.entityIID = tr.entityIID and cr.propertyIID = tr.propertyIID
    left join pxp.enumcode ec1 on ec1.type = _ENUMTYPE_COUPLINGTYPE and ec1.code = cr.couplingtype
    left join pxp.enumcode ec2 on ec2.type = _ENUMTYPE_RECORDSTATUS and ec2.code = cr.recordstatus;

-- PXON Value Record
_VIEW (pxp.ValueRecordPXON) as
    select vr.entityIID,
        vr.propertyIID,
        vr.localeid,
        '[v>' || _TO_PXONCODE(p.propertyCode) ||
        (case when vr.localeid is not null then ' $locale=' || vr.localeid else '' end) ||
        ' $type=' || p.label ||
        ']' as csid,
        p.csid as prop,
        coalesce(ec2.label, 'NONE') as cpl,
        ec1.label as status,
        vr.value,
        vr.asnumber as asnumber,
        vr.unitsymbol as unit,
        vr.ashtml,
        co.csid as cxt
    from pxp.ValueRecord vr
    join pxp.PropertyConfigPXON p on p.propertyiid = vr.propertyiid
    join pxp.enumcode ec1 on ec1.type = _ENUMTYPE_RECORDSTATUS and ec1.code = vr.recordstatus
    left join pxp.ContextualObjectPXON co on co.contextualobjectIID = vr.contextualobjectIID
    left join pxp.coupledrecord cr on cr.entityIID = vr.entityIID and cr.propertyIID = vr.propertyIID
    left join pxp.enumcode ec2 on ec2.type = _ENUMTYPE_COUPLINGTYPE and ec2.code = cr.couplingtype;

-- PXON Value Record
_VIEW (pxp.AllValueRecordPXON) as
     SELECT vr.entityiid,
    vr.propertyiid,
    vr.localeid,
    (((('[v>'::text ||
        CASE
            WHEN p.propertycode::text ~ '^[0-9\-]'::text THEN ((''''::text || p.propertycode::text) || ''''::text)::character varying
            ELSE p.propertycode
        END::text) ||
        CASE
            WHEN vr.localeid IS NOT NULL THEN ' $locale='::text || vr.localeid::text
            ELSE ''::text
        END) || ' $type='::text) || p.label::text) || ']'::text AS csid,
    p.csid AS prop,
    (CASE WHEN ec2.label = 'undefined' THEN 'UNDEFINED'
          ELSE COALESCE(ec2.label, 'NONE'::character varying)
          END) AS cpl,
    ec1.label AS status,
    vr.value,
    vr.asnumber,
    vr.unitsymbol AS unit,
    vr.ashtml,
    co.csid AS cxt
   FROM allvaluerecord vr
     JOIN propertyconfigpxon p ON p.propertyiid = vr.propertyiid
     JOIN enumcode ec1 ON ec1.type = _ENUMTYPE_RECORDSTATUS AND ec1.code = vr.recordstatus
     LEFT JOIN contextualobjectpxon co ON co.contextualobjectiid = vr.contextualobjectiid
     LEFT JOIN coupledrecord cr ON cr.entityiid = vr.entityiid AND cr.propertyiid = vr.propertyiid
     LEFT JOIN enumcode ec2 ON ec2.type = _ENUMTYPE_COUPLINGTYPE AND ec2.code = cr.couplingtype;

-- PXON FULL Relation Side1
_VIEW (pxp.RelationSide1PXON) as
    select r.side1entityIID,
        '[r>' || _TO_PXONCODE(p.propertyCode) || ' $side=1 $type=' || p.label || ']' as csid,
        p.csid as prop,
        coalesce(ec1.label, 'NONE') as cpl,
        coalesce(ec2.label, 'NONE') as status,
        el.entity as entitylink,
        co.csid as cxt
    from pxp.Relation r
    join pxp.PropertyConfigPXON p on p.propertyiid = r.propertyiid
    join pxp.EntityLinkPXON el on el.baseentityIID = r.side2entityIID
    left join pxp.coupledrecord cr on cr.entityIID = r.side1entityIID and cr.propertyIID = r.propertyIID
    left join pxp.enumcode ec1 on ec1.type = _ENUMTYPE_COUPLINGTYPE and ec1.code = cr.couplingtype
    left join pxp.enumcode ec2 on ec2.type = _ENUMTYPE_RECORDSTATUS and ec2.code = cr.recordstatus
    left join pxp.ContextualObjectPXON co on co.contextualobjectiid = r.side1contextualobjectiid;

-- PXON FULL Relation Side2
_VIEW (pxp.RelationSide2PXON) as
    select r.side2entityIID,
        '[r>' || _TO_PXONCODE(p.propertyCode) || ' $side=2 $type=' || p.label || ']' as csid,
        p.csid as prop,
        coalesce(ec1.label, 'NONE') as cpl,
        coalesce(ec2.label, 'NONE') as status,
        el.entity as entitylink,
        co.csid as cxt
    from pxp.Relation r
    join pxp.PropertyConfigPXON p on p.propertyiid = r.propertyiid
    join pxp.EntityLinkPXON el on el.baseentityIID = r.side1entityIID
    left join pxp.coupledrecord cr on cr.entityIID = r.side2entityIID and cr.propertyIID = r.propertyIID
    left join pxp.enumcode ec1 on ec1.type = _ENUMTYPE_COUPLINGTYPE and ec1.code = cr.couplingtype
    left join pxp.enumcode ec2 on ec2.type = _ENUMTYPE_RECORDSTATUS and ec2.code = cr.recordstatus
    left join pxp.ContextualObjectPXON co on co.contextualobjectiid = r.side2contextualobjectiid;
