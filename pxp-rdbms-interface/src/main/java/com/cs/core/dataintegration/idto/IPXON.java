package com.cs.core.dataintegration.idto;

/**
 * PXON keywords
 *
 * @author vallee
 */
public interface IPXON {

  // JSON tags used in PXON (please keep order by alphabetic to make easier
  // visual check)
  public enum PXONTag {

    allowedstyles, //html attribute styles
    attributevariantcontextcode, //context code through class
    availability, //property availability for entity type
    baselocale, // base locale ID of an entity
    calc, // calculation
    callback, // callback URI
    cardinality, //relationship side cardinality
    catalog, // catalog reference
    changed, // any changed information
    children, //tag children
    classcode, //any referred class code
    classifier, // classifier reference
    classifieriid, //any classifier iid
    code, //any code
    color, //any color reference
    columns, //columns in property collection
    contextcode, //any context code
    couplingtype, //any coupling type
    couplings, //list of coupling info
    cpl, // coupling type
    cplrule, // coupling rule
    created, // creation data
    csid, // used for CSE Identities
    pxon, // any embedded PXON content
    cxt, // context
    cxtual, // contextual object
    date, // any date specification
    defaultendtime, //any default time in event
    defaultstarttime, //default start time in event
    defaultunit, //measurement default unit
    defaultvalue, //any default value
    description, //any description in config property
    dupl, // allow duplicate in contextual object
    elements, //element sin property collection
    embd, // any embedded object or data
    embdtype, // type of embedded objects
    embeddedclasses, //embedded classes
    end, // ending data
    entities, //any list of entity e.g in context
    entity, // entity pointer
    entry, // entry parameters
    event, // type of event
    events, //list of events
    exp, // JSON expression
    ext, // JSON extension
    extclass, // extension class
    flag, // any flag
    hash, // hash code
    holdersidecxtual, // side 1 contextual object in relationship
    html, // html definition
    id, // any ID
    img, // image reference
    isabstract, //classifier property
    isautocreate, //context auto create
    iscurrenttime, //context time option
    iscutoff, //classifier inheritance cutoff
    isdefault, //classifier default property
    isdefaultforxray, //default x-ray property collection identifier
    isdisabled, //any disabled field
    isduplicatevariantallowed, //context configuration
    isexpired, //asset expiry check
    isDuplicate, //duplicate asset check
    isfilterable, //property filter option
    isforxray, //x-ray property collection identifier
    isgrideditable, //any grid editable property
    isidentifier, //any unique identifier
    isinherited, //any inheritance info e.g sections in class
    islimitedobject, //context limited configuration
    ismandatory, //any mandatory field
    ismultiselect, //flag for multi select option
    isnature, //flag for nature identifier of classifer
    issearchable, //flag for searchable property
    isshould, //flag for should property
    isskipped, //flag for any skipped property
    issortable, //flag for any sortable field
    isstandard, //any standard property
    istimeenabled, //time option for context
    istranslatable, //any multilingual property
    isversionable, //any versionable property
    isvisible, //visible toggle property
    label, //referenced label
    level, // level in a hierarchy
    link, // linked data
    linkedmastertag, //linked master tag info
    locale, // locale
    login, // login time
    logout, // logout time
    logouttype, // logout type
    modified, // last modification data
    msg, // any kind of message
    name, // referenced name
    nature, // nature class of an entity
    sourceorganizationcode, // source organization of an entity
    naturetype, //nature type of class
    no, // number (of line, object, etc.)
    number, // number definition
    numberofversionstomaintain, //count of versions
    object, // generic object
    order, // order in a sequence
    origin, // origin of an entity
    othersidecxtual, // side 2 contextual object in relationship
    parent, // reference to parent object
    parentcode, //any referring parent
    placeholder, //attribute placeholder
    precision, //measurement precision
    prefix, //attribute prefix
    priority, // any level of priority
    productvariantcontextcode, //context code for product variant
    progress, // progress data
    promotionalversioncontextcode, //context code for promo version
    prop, // property reference
    propertyiid, //property iid
    range, // range information attached to a tag
    read, // any read information
    record, // reference to property records
    relationshiptype, //relationship type
    relationships, //list of relationships
    report, // report data
    rows, //rows of property collection
    runtime, // runtime data
    sections, //list of sections linked to class
    selectedtagvalues, //default tag values in class
    session, // session identified
    side, // side of a relation
    side1, //relationship side
    side2, //relationship side
    src, // source or source of coupling (master)
    start, // starting data
    status, // status of objects
    statustags, //lifecycle | listing status tag
    steps, // process steps
    subtype, //any subtype e.g measurement length attribute
    suffix, //attribute suffix
    tab, //tab info
    tag, // any tag data
    tagcodes, //any linked tab code
    tagtype, //any tag type
    tagvaluesequence, //sequence of tag children
    tasks, //list of tasks linked to classifier
    text, // any free text
    tooltip, //tooltip information of property
    top, // reference to top entity of a hierarchy
    type, // any type that doesn't belong to identity
    unit, // unit definition
    uniqueselectors, //Unique selector of tags in context for auto create
    user, // user definition
    value, // value definition
    valueashtml, //default value of HTML attribute
    viid, // value IID
    when, // event time
    write, // any write information (end of enum)
    levelCode, // level codes of taxonomy
    parentCode, //parent code of classes
    masterParentTag, //parent tag of classes
    firstname, // first name of user
    lastname, // last name of user
    username, // username of user
    gender, // gender of user
    email, // email of user
    contact, // contact number of user
    birthdate, // birthdate of user
    icon, // file icon of user
    password,// password of user
    useriid, // useriid
    isbackgrounduser, // isbackgrounduser
    isemaillog, // isemaillog
    prioritytag,// prioritytag of task
    statustag, // status tag of task
    levelLabels, // level label of taxonomy
    taxonomytype, // type of taxonomy
    islanguagedependent, // language dependent or not
    attributes,// list of attribute
    types, // list of type
    tags, // list of tag
    ruleviolations, // list of rule violation
    normalizations, // list of normalization
    taxonomies, // list of taxonomy
    physicalcatalogids, // list of physical catalog id
    organizations, // list of organization
    languages, // list of language
    entityid, // entity id 
    rules, // list of rules
    from, // from
    to, // to
    values, // values
    rulelistlinkid, // rulelist link id
    attributelinkid, // attribute link id
    klasslinkids, // list of klasslink
    comparewithsysdate, // compare with current system date
    tagvalues, // list of tag values
    attributeoperatorlist, // list of attribute operator 
    calcattributeunit, // calculated attribute unit
    calcattributeunitashtml, // calculated attribute unit as html
    transformationtype, // transformation type
    basetype, // base type
    valueattributeid, // value attribute id
    attributeconcatenatedlist, // attribute concatenated list
    findtext, // fine text for rule
    replacetext, // replace text for rule
    startindex, // start index
    endindex, // end index
    klassids, // list of klasss ids
    taxonomyids, // list of taxonomy ids
    mergeeffect, // merge effect of golde record rule
    naturerelationships, // list of nature relationship ids
    entitytype, // type of entity
    supplierids, // list of supplier iids 
    propertySequenceList, // list of property sequence
    propertyCollectionCodes, // list of property collection code
    variantContextCodes, // list of variant context code
    relationshipCodes, // list of relationship code
    sequence, // sequence
    iseditable, 
    previewimage,
    physicalCatalogs, // physical catalogs for Organization and Role
    portals, // portal for Organization and Role
    roles,// roles for Organization
    endpointIds,// endpoint Ids for Organization
    systems,// systems for Organization and Role
    endpoints,// endpoints for Role
    kpis,// kpis for Role
    targetKlasses,// target classes for Role
    targetTaxonomies,// target taxonomies for Role
    roleType,// roletype for Role
    isDashboardEnable,// is dashboard enabled option for Role
    users,// users for Role
    isBackgroundRole,// field for Role
    enableaftersave,// Relationship after save event enable
    languagetranslation, // language translation
    imageresolution, // tag image resolution
    imageextension, extensionconfiguration, // tag image extension
    abbreviation,// abbreviation for Language
    localeId,// localeId for Language
    dateFormat,// dateFormat for Language
    numberFormat,// numberFormat for Language
    isDataLanguage,// isDataLanguage for Language
    isDefaultLanguage,// isDefaultLanguage for Language
    isUserInterfaceLanguage,// isUserInterfaceLanguage for Language
    isStandard, // isStandard for Language
    index, //property collection property sequence index
    endpoint,  // data integration endpoint code
    calculatedattributetype, // calculated Attribute type 
    iscodevisible, // for attribute show tag
    hideseparator, //for calculated attribute
    entityattributetype, // sub type of attributes or tags
    isnewlycreatedlevel, 
    levelindex, 
    detectduplicate, 
    extractzip, 
    trackdownloads,
    localeIds,
    isReadOnly, 
    isLite,
    isTimeEnabled,// is Readonly  enabled option for Role; 
    taxonomyInheritanceSetting, // for nature relationship taxonomy setting
    canEdit, // canEdit for permission
    canAdd, // canAdd for permission
    canDelete, // canDelete for permission
    viewName, // viewName for permission
    canEditName, // canEditName for permission
    viewIcon, // viewIcon for permission
    canChangeIcon,   // canChangeIcon for permission
    viewPrimaryType,   // viewPrimaryType for permission
    canEditPrimaryType,  // canEditPrimaryType for permission
    viewAdditionalClasses,  // viewAdditionalClasses for permission
    canAddClasses,  // canAddClasses for permission
    canDeleteClasses,  // canDeleteClasses for permission
    viewTaxonomies,  //     viewTaxonomies for permission
    canDeleteTaxonomy,  // canDeleteTaxonomy for permission
    canAddTaxonomy,  // canAddTaxonomy for permission
    viewStatusTags,   // viewStatusTags for permission
    canEditStatusTag,   // canEditStatusTag for permission
    viewCreatedOn,   // viewCreatedOn for permission
    viewLastModifiedBy,  // viewLastModifiedBy for permission
    canRead,// canRead for Global Permission
    canCreate,//canCreate for Global Permission
    propertyPermissions,// property Permission
    relationshipPermissions,// relationship Permission
    globalPermission,// global Permission
    headerPermission,// header  Permission 
    roleId,// roleId for permission
    relationshipId,//relationshipId for permission
    propertyId,  // propertyId for Permission 
    canDownload, // canDownload for Permission 
    permissionType, // permission Type for permission
    propertytype, // // property type for permission
    isMerged, // is baseEntity merged to the golden record
    preferreddatalanguage, // user data language
    preferreduilanguage,   //user ui language
    rendererType, // property rendererType,
    attribute,
    ; 
    
    public static <E extends Enum> String toTag(E tag) {
      return tag.toString();
    }

    public static <E extends Enum> String toReadOnlyTag(E tag) {
      return "$" + tag;
    }

    public static <E extends Enum> String toPrivateTag(E tag) {
      return "_" + tag;
    }

    public static <E extends Enum> String toJSONContentTag(E tag) {
      return "J" + tag;
    }

    public static <E extends Enum> String toJSONArrayTag(E tag) {
      return "J" + tag;
    }

    // get the PXON tag (equivalent as toString)
    public String toTag() {
      return toString();
    }

    // get the readonly tag (prefixed by $)
    public String toReadOnlyTag() {
      return "$" + toTag();
    }

    // get the CSE tag (first letter uppercase)
    public String toCSETag() {
      return toTag().substring(0, 1)
              .toUpperCase() + toTag().substring(1);
    }

    // get the readonly CSE tag (prefixed by $ and first letter uppercase)
    public String toReadOnlyCSETag() {
      return "$" + toCSETag();
    }

    // get the private tag (prefixed by _)
    public String toPrivateTag() {
      return "_" + toTag();
    }

    // get the JSON content tag (prefixed by J)
    public String toJSONContentTag() {
      return "J" + toTag();
    }

    // get the JSON array tag (prefixed by L)
    public String toJSONArrayTag() {
      return "L" + toTag();
    }

    // get the CSE list tag (prefixed by L)
    public String toCSEListTag() {
      return "L" + toCSETag();
    }

  }

  // CSExpression Meta used in PXON
  public enum PXONMeta {
    Create, Delete, Init, Update, Use, Content;
  }
}
