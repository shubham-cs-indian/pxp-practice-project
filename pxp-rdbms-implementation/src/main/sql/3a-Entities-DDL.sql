/**
 * TABLE DEFINITIONs of ENTITY DATA
 * Author:  vallee
 * Created: Mar 25, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

#undef _NB_PARTITIONS
#define _NB_PARTITIONS 4 /* when paritioning is active, 4 partitions are advised here to every entity tables */

--
--   Contextual Object including array/nested tags
--
#if defined WITH_PARTITIONS && defined ORACLE12C
alter table pxp.ContextualObject drop column cxtTagValueCodes;
alter table pxp.ContextualObject drop column cxtRanges;
#endif
_TABLE( pxp.ContextualObject) (
        contextualObjectIID     _IID primary key,
        contextCode             _VARCHAR not null,
        cxtTimeRange			_INT8RANGE,
        cxtTags				    _HSTORE
) 
#ifdef ORACLE12C
nested table cxtTagValueCodes store as cxtTagValueCodesTAB 
nested table cxtRanges store as cxtRangesTAB
#endif
_PARTITION_BY_HASH( contextualObjectIID );
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.ContextualObject);

#ifdef PGSQL11 /* index on  hstore usrTags */
_CREATE_HSTORE_INDEX(ContextualObject, cxtTags);
#elif ORACLE12C
create bitmap index cxtTagValueCodes_idx on cxtTagValueCodesTAB(column_value);
#endif

_FOREIGN_KEY(ContextualObject, contextConfig, contextCode, contextCode);

--
--    Base Entity
--
_TABLE( pxp.BaseEntity ) (
	baseEntityIID       _IID primary key, /* internal key: unique */
        baseEntityID        _VARCHAR not null, /* used as external key across catalogs */
        catalogCode         _VARCHAR not null, /* it is the current catalog of creation */
        organizationCode    _VARCHAR not null, /* it is the current organization of creation */
        classifierIID       _IID not null, /* the nature classifier of the entity */
        baseType            _SHORT not null,
        childLevel          _INT not null, /* represents the parent/child level 1 = top level, 2 is first embedded child level, etc.*/
        parentIID           _IID, /* can be null for top level and embeddedType = UNDEFINED */
        embeddedType        _INT,
        topParentIID        _IID, /* can be null for top level, corresponds to the top most base entity IID for children levels */ 
        sourceCatalogCode   _VARCHAR,
        baseLocaleID        _LOCALEID not null, /* it is by default the context in which it has been created */
        hashCode            _VARCHAR,
        originBaseEntityIID _IID,
        defaultImageIID     _IID,
        entityExtension     _JSONBCOL(entityExtension),
        contextualObjectIID _IID, /* equals baseEntityIID when context is attached to this entity */
#if FOREIGN_KEY_WITHOUT_PARTITION
        unique( baseEntityID, catalogCode, organizationCode, endpointCode),
#endif
        /* tracking information */
        createUserIID       _IID not null,
        lastModifiedUserIID _IID not null,
        createTime          _LONG not null,
        lastModifiedTime    _LONG not null,
		isExpired 			_BOOLEAN default false,
		isClone            _BOOLEAN default false,
		endpointCode       _VARCHAR,
		isDuplicate 		_BOOLEAN default false, /* true if asset instance's hashcode is same as any other instance's hashcode */
		isMerged            _BOOLEAN default false, /* true if entity is merged in golden record*/
		sourceOrganizationCode _VARCHAR /* Actual origin from which the content came from*/
) _PARTITION_BY_HASH( baseEntityIID );
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.BaseEntity);

#if FOREIGN_KEY_WITH_PARTITION
_CREATE_INDEX2(BaseEntity, baseType, catalogCode);
_CREATE_INDEX2(BaseEntity, baseEntityID, catalogCode);
#endif
_CREATE_INDEX1(BaseEntity, baseEntityID);
_CREATE_INDEX1(BaseEntity, organizationCode);
_CREATE_INDEX1(BaseEntity, contextualObjectIID);
_CREATE_INDEX1(BaseEntity, parentIID);
_CREATE_INDEX1(BaseEntity, topParentIID);
_CREATE_INDEX1(BaseEntity, hashCode);
_CREATE_INDEX1(BaseEntity, createUserIID);
_CREATE_INDEX1(BaseEntity, lastModifiedUserIID);
_CREATE_INDEX1(BaseEntity, createTime);
_CREATE_INDEX1(BaseEntity, lastModifiedTime);
_CREATE_INDEX1(BaseEntity, endpointCode);
/* index on JSONB extension fields */
#ifdef PGSQL11
create index BaseEntity_entityExtension_idx on pxp.BaseEntity using gin(entityExtension);
#elif ORACLE12C
create index BaseEntity_entityExtension_idx on pxp.BaseEntity(entityExtension) 
    indextype is ctxsys.context parameters( 'section group CTXSYS.JSON_SECTION_GROUP sync(on commit)');
#endif

_FOREIGN_KEY(baseEntity, classifierConfig, classifierIID, classifierIID);
_FOREIGN_KEY(BaseEntity, UserConfig, createUserIID, userIID);
_FOREIGN_KEY(BaseEntity, UserConfig, lastModifiedUserIID, userIID);

#if FOREIGN_KEY_WITHOUT_PARTITION
_FOREIGN_KEY(baseEntity, ContextualObject, contextualObjectIID, contextualObjectIID);
#endif

--
--     Collection
--
_TABLE( pxp.Collection ) (
	collectionIID       _IID primary key, /* internal key: unique */
    collectionType      _INT not null,
    collectionCode      _VARCHAR not null, /* it is the current catalog of creation */
	parentIID           _IID,
	createUserIID       _IID not null,
    lastModifiedUserIID _IID not null,
    createTime          _LONG not null,
    lastModifiedTime    _LONG not null,
	searchCriteria	    _JSONCOL(searchCriteria),
	isPublic            _BOOL(isPublic),
	catalogCode         _VARCHAR not null,
	organizationCode    _VARCHAR not null
);
_CREATE_INDEX1(Collection, parentIID);
_CREATE_INDEX1(Collection, collectionType);
_CREATE_INDEX1(Collection, collectionCode);
_CREATE_INDEX1(Collection, createUserIID);
_CREATE_INDEX1(Collection, lastModifiedUserIID);
_CREATE_INDEX1(Collection, createTime);
_CREATE_INDEX1(Collection, lastModifiedTime);

_FOREIGN_KEY(Collection, Collection, parentIID, collectionIID) on delete cascade;
_FOREIGN_KEY(Collection, UserConfig, createUserIID, userIID);
_FOREIGN_KEY(Collection, UserConfig, lastModifiedUserIID, userIID);

--
--   Collection base entity link
--
_TABLE( pxp.CollectionBaseEntityLink) (
        collectionIID     _IID,
        baseEntityIID     _IID,
        primary key ( collectionIID, baseEntityIID) 
); 
_CREATE_INDEX1(CollectionBaseEntityLink, collectionIID);
_CREATE_INDEX1(CollectionBaseEntityLink, baseEntityIID);

--
--    Base Entity other classifiers link
--
_TABLE( pxp.BaseEntityClassifierLink ) (
    baseEntityIID       _IID,
    otherClassifierIID  _IID,
    primary key ( baseEntityIID, otherClassifierIID)
) _PARTITION_BY_HASH( baseEntityIID );
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.BaseEntityClassifierLink);

_CREATE_INDEX1(BaseEntityClassifierLink, baseEntityIID);
_CREATE_INDEX1(BaseEntityClassifierLink, otherClassifierIID);

_FOREIGN_KEY(BaseEntityClassifierLink, ClassifierConfig, otherClassifierIID, classifierIID);

--
--  Base Entity LocaleID Link
--
_TABLE( pxp.BaseEntityLocaleIdLink) (
        baseEntityIID     _IID,
		localeId	  _VARCHAR,
        primary key ( baseEntityIID, localeId) 
); 
_CREATE_INDEX1(BaseEntityLocaleIdLink, baseEntityIID);
_CREATE_INDEX1(BaseEntityLocaleIdLink, localeId);

--
--   Contextual Object base entity link
--
_TABLE( pxp.ContextBaseEntityLink) (
        contextualObjectIID     _IID,
        baseEntityIID       _IID,
        primary key ( contextualObjectIID, baseEntityIID) 
); /* this table is deemed to be rarely used -> no partition required here */
_CREATE_INDEX1(ContextBaseEntityLink, contextualObjectIID);
_CREATE_INDEX1(ContextBaseEntityLink, baseEntityIID);

#if FOREIGN_KEY_WITHOUT_PARTITION
_FOREIGN_KEY( ContextBaseEntityLink, ContextualObject, contextualObjectIID, contextualObjectIID);
_FOREIGN_KEY( ContextBaseEntityLink, BaseEntity, baseEntityIID, baseEntityIID);
#endif

#undef _NB_PARTITIONS
#define _NB_PARTITIONS 16 /* when paritioning is active, 16 partitions are advised here to every record tables */

--
--    Coupled record
--
_TABLE( pxp.CoupledRecord ) (
    propertyIID         _IID not null,
    entityIID           _IID not null, /* the base entity or the classifier that is owning this coupled record */
    recordStatus        _SHORT not null, /* defines if this record is the result of transfer/coupling/calculation or clone */
    couplingBehavior    _SHORT, /* the behavior of coupling: tight, dynamic, initial */
    couplingType        _SHORT, /* the type of coupling: default value, coupling by inheritance/relationship, clone/transfer */
    masterNodeID        _VARCHAR, /* when this property record if slaved to another object IID or property record IID (case dependent) */
    coupling            _VARCHAR, /* expression of the coupling rule */
    masterEntityIID		_IID not null,
    masterPropertyIID	_IID not null,
    localeiid			_IID not null,
    primary key ( propertyIID, entityIID, localeiid)
) _PARTITION_BY_HASH( entityIID );
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.CoupledRecord);

_CREATE_INDEX2(CoupledRecord, entityIID, recordStatus);
_CREATE_INDEX3(CoupledRecord, entityIID, propertyIID, recordStatus);
_CREATE_INDEX2(CoupledRecord, masterNodeID, recordStatus); 
_CREATE_INDEX2(CoupledRecord, masterEntityIID, masterPropertyIID);
_FOREIGN_KEY(CoupledRecord, propertyConfig, propertyIID, propertyIID);


_TABLE( pxp.ConflictingValues ) (
    targetEntityiid         _IID not null,
    propertyiid           	_IID not null, 
    sourceEntityiid        	_IID not null,
    couplingType        	_SHORT not null, 
    couplingSourceiid		_IID not null,
    recordStatus 			_SHORT not null,
    couplingSourceType		_SHORT not null,
    localeiid				_IID not null,
    primary key ( targetEntityiid, propertyiid, sourceEntityiid, couplingSourceiid, localeiid)
);

--
--    Tag record = type of property record with array/nested tags
--
#if defined WITH_PARTITIONS && defined ORACLE12C
alter table pxp.TagsRecord drop column ursTags;
#endif
_TABLE( pxp.TagsRecord ) (
    propertyIID         _IID not null,
    entityIID           _IID not null, /* the base entity or the classifier that is owning this coupled record */
    recordStatus        _SHORT not null, /* defines if this record is an entity or a classifier default value */
    usrTags				    _HSTORE,
    primary key ( propertyIID, entityIID)
) 
#ifdef ORACLE12C
nested table usrTags store as usrTagsTAB
#endif
_PARTITION_BY_HASH( entityIID );
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.TagsRecord);
_CREATE_INDEX3(TagsRecord, propertyIID, entityIID, recordStatus);

_CREATE_INDEX1( TagsRecord, entityIID);
_CREATE_INDEX2( TagsRecord, entityIID, propertyIID);
_CREATE_INDEX1( TagsRecord, recordStatus);

#ifdef PGSQL11 /* index on  hstore usrTags */
_CREATE_HSTORE_INDEX(TagsRecord, usrTags);
#elif ORACLE12C
create bitmap index TagsRecord_tagValueCodes_idx on usrTagsTAB(column_value);
#endif

_FOREIGN_KEY( TagsRecord, propertyConfig, propertyIID, propertyIID);

--
--    ValueRecord = record of entity values
--
_TABLE( pxp.ValueRecord ) (
    propertyIID         _IID not null,
    entityIID           _IID not null, /* the base entity or the classifier that is owning this coupled record */
    recordStatus        _SHORT not null, /* defines if this record is an entity or a classifier default value */
    valueIID            _IID  default _NEXTVAL(pxp.seqIID), /* distinct key across locale IDs and contexts of same property */
    localeID             _LOCALEID, /* locale ID is null when the property is not language dependent */
    contextualObjectIID  _IID, /* equals valueIID when context is attached to this value */
    value                _VARCHAR,
    asHTML              _TEXT,
    asNumber            _FLOAT,
    unitSymbol          _VARCHAR,
    calculation         _VARCHAR,
    primary key( valueIID),
    unique ( propertyIID, entityIID, localeID, contextualObjectIID) /* Business PK: localeID and contextualObjectIID can be null */
) _PARTITION_BY_HASH( entityIID );
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.ValueRecord);

_CREATE_INDEX2(ValueRecord, entityIID, propertyIID);
_CREATE_INDEX2(ValueRecord, localeID, propertyIID);
_CREATE_INDEX2(ValueRecord, value, localeID);
_CREATE_INDEX1(ValueRecord, asNumber);
_CREATE_INDEX1(ValueRecord, recordStatus);
_CREATE_INDEX3(ValueRecord, propertyIID, entityIID, recordStatus);
/* Foreign key when the table has no partition */
#if !defined(PGSQL11) || !defined(WITH_PARTITIONS)
/* to be checked by trigger _FOREIGN_KEY(ValueRecord, PropertyRecord, propertyRecordIID, propertyRecordIID); */
_FOREIGN_KEY(ValueRecord, ContextualObject, contextualObjectIID, contextualObjectIID) on DELETE cascade;
#endif

--
--    Relation = link table many to many between side entities
--
_TABLE( pxp.Relation ) (
    propertyIID                 _IID not null,
    side1EntityIID              _IID not null,
    side2EntityIID              _IID not null,
    side1contextualObjectIID    _IID,
    side2contextualObjectIID    _IID,
    primary key ( propertyIID, side1EntityIID, side2EntityIID)
) _PARTITION_BY_HASH( side1BaseEntityIID );
_CREATE_PARTITIONS_BY_HASH_TABLES( pxp.Relation);

_CREATE_INDEX2( Relation, side1EntityIID, propertyIID);
_CREATE_INDEX2( Relation, side2EntityIID, propertyIID);

_FOREIGN_KEY( Relation, propertyConfig, propertyIID, propertyIID);
#if FOREIGN_KEY_WITHOUT_PARTITION
_FOREIGN_KEY( Relation, ContextualObject, side1contextualObjectIID, contextualObjectIID);
_FOREIGN_KEY( Relation, ContextualObject, side2contextualObjectIID, contextualObjectIID);
#endif

--
--   Base Entity Taxonomy Conflict Link
--
_TABLE( pxp.BaseEntityTaxonomyConflictLink) (   
        entityIID           _IID primary key,
		sourceEntityIID     _IID not null,
		propertyIID         _IID not null,
		isResolved          _BOOLEAN 
); 
_CREATE_INDEX1(BaseEntityTaxonomyConflictLink, entityIID);

_FOREIGN_KEY( BaseEntityTaxonomyConflictLink, BaseEntity, entityIID, baseEntityIID);
_FOREIGN_KEY( BaseEntityTaxonomyConflictLink, BaseEntity, sourceEntityIID, baseEntityIID);



#ifdef PGSQL11
-- end of PGSQL Entity Table declarations
#elif ORACLE12C
-- end of ORACLE Entity Table declarations
#endif
