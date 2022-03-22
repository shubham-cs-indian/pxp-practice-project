/**
 * Author:  vallee
 * Created: Aug 25, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

-- PXON User views
_VIEW ( pxp.UserConfigPXON ) as
    select u.*, '[U>' || _TO_VARCHAR(userIID) || ' $name=''' || username || ''']' as csid from pxp.UserConfig u;

-- PXON Property views
_VIEW( pxp.PropertyConfigPXON ) as
    select p.*, e.label, '[' || _TO_PXONCODE(p.propertyCode) || ' $type=' || e.label || ']' as csid
    from pxp.PropertyConfig p
    join pxp.EnumCode e on e.type = _ENUMTYPE_PROPERTYTYPE and e.code = p.propertytype;

-- PXON Context views
_VIEW( pxp.ContextConfigPXON ) as
    select c.*, '[X>' || _TO_PXONCODE(c.contextCode) || ' $type=' || e.label || ']' as csid
    from pxp.ContextConfig c
    join pxp.EnumCode e on e.type = _ENUMTYPE_CONTEXTTYPE and e.code = c.contextType;

-- PXON Classifier views
_VIEW( pxp.ClassifierConfigPXON ) as 
    select c.*, e.label, '[c>' || _TO_PXONCODE(c.classifierCode) || ' $iid=' || _TO_VARCHAR(c.classifierIID) || ' $type=' || e.label || ']' as csid
    from pxp.classifierConfig c
    join pxp.EnumCode e on e.type = _ENUMTYPE_CLASSIFIERTYPE and e.code = c.classifierType;

-- PXON Tag value views
_VIEW( pxp.tagValueConfigPXON ) as 
    select t.*, '[T>' || _TO_PXONCODE(tagValueCode) || ' $property=' || _TO_VARCHAR(propertyIID) || ']' as csid
    from pxp.tagValueConfig t;
