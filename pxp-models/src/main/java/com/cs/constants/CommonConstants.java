package com.cs.constants;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.runtime.interactor.constants.application.CoreConstant;

import java.util.Arrays;
import java.util.List;

public class CommonConstants extends CoreConstant {
  
  public static final String       SWIFT_CONTAINER_IMAGE                                = "Image";
  
  public static final String       SWIFT_CONTAINER_VIDEO                                = "Video";
  
  public static final String       SWIFT_CONTAINER_DOCUMENT                             = "Document";
  
  public static final String       SWIFT_CONTAINER_ICONS                                = "Icons";
  
  public static final String       SWIFT_CONTAINER_ATTACHMENT                           = "Attachment";
  
  public static final String       MODE_CONFIG                                          = "config";
  
  public static final String       MODE_ATTACHMENT                                      = "Attachment";
  
  public static final String       SECTIONS_PROPERTY                                    = "sections";
  
  public static final String       ELEMENTS_PROPERTY                                    = "elements";

  public static final String       STATUS_TAG_TYPE_ID                                   = "tag_type_lifecycle_status";
  public static final String       MASTER_TAG_TYPE_ID                                   = "tag_type_master";
  public static final String       STATUS_TYPE_ID                                       = "tag_type_status";
  public static final String       LISTING_TYPE_ID                                      = "tag_type_listing_status";
  
  public static final String       ELEMENT_IDS_PROPERTY                                 = "elementIds";
  
  public static final String       RELATIONSHIP                                         = "relationship";
  public static final String       RELATIONSHIPS_ELEMENT                                = "Relationships";
  public static final String       RELATIONSHIP_KLASS                                   = "Relationship";
  public static final String       RELATIONSHIPS                                        = "relationships";
  public static final String       NATURE_RELATIONSHIP                                  = "natureRelationship";
  public static final String       NATURE_RELATIONSHIPS                                 = "natureRelationships";
  
  public static final String       ENTITY_KLASS_TYPE                                    = "klass";
  
  public static final String       REFERENCE_KLASS                                      = "Reference";
  
  public static final String       REFERENCES                                           = "References";
  
  public static final String       RELATIONSHIP_SIDE_1                                  = "side1";
  
  public static final String       SIDE_ID                                              = "sideId";
  
  public static final String       RELATIONSHIP_SIDE_2                                  = "side2";
  
  public static final String       RELATIONSHIP_SIDE_KLASSID                            = "klassId";
  
  public static final String       RELATIONSHIP_SIDE_ISVISIBLE                          = "isVisible";
  
  public static final String       RELATIONSHIP_SECTION_TYPE                            = "com.cs.core.config.interactor.entity.relationship.RelationshipSection";
  
  public static final String       RELATIONSHIP_PROPERTY                                = "relationship";
  
  public static final String       RELATIONSHIP_MAPPING_ID_PROPERTY                     = "relationshipMappingId";
  
  public static final String       RELATIONSHIP_SIDE_PROPERTY                           = "relationshipSide";
  
  public static final String       PROPERTY_IID                                         = "propertyIID";
  
  public static final String       CODE_PROPERTY                                        = "code";
  
  public static final String       IS_INHERITED_PROPERTY                                = "isInherited";
  
  public static final String       IS_CUTOFF_PROPERTY                                   = "isCutoff";
  
  public static final String       ADDED_ELEMENTS_PROPERTY                              = "addedElements";
  
  public static final String       MODIFIED_ELEMENTS_PROPERTY                           = "modifiedElements";
  
  public static final String       DELETED_ELEMENTS_PROPERTY                            = "deletedElements";
  
  public static final String       UTILIZING_KLASS_IDS_PROPERTY                         = "utilizingKlassIds";
  
  public static final String       OWNING_KLASS_ID_PROPERTY                             = "owningKlass";
  
  public static final String       ID_PROPERTY                                          = "id";
  public static final String       INCLUDE_CHILD_TAXONOMIES                             = "includeChildTaxonomies"; 
  
  public static final String       IID_PROPERTY                                         = "iid";
  
  public static final String       PHYSICAL_CATALOG_IDS                                 = "availablePhysicalCatalogIds";
  
  public static final String       TYPE_PROPERTY                                        = "type";
  
  public static final String       CHILDREN_PROPERTY                                    = "children";
  
  public static final String       START_POSITION_PROPERTY                              = "startPosition";
  
  public static final String       END_POSITION_PROPERTY                                = "endPosition";
  
  public static final String       VALIDATOR_PROPERTY                                   = "validator";
  
  public static final String       PREVIOUS_SECTION_ID_PROPERTY                         = "previousSectionId";
  
  public static final String       USER_NAME_PROPERTY                                   = "userName";
  
  public static final String       DEFAULT_VALUE_PROPERTY                               = "defaultValue";
  
  public static final String       STRUCTURE_CHILDREN_PROPERTY                          = "structureChildren";
  
  public static final String       KLASS_VIEW_SETTING_PROPERTY                          = "klassViewSetting";
  
  public static final String       ROLES_PROPERTY                                       = "roles";
  
  public static final String       ROLE_PROPERTY                                        = "role";
  
  public static final String       TAXONOMY_PROPERTY                                    = "taxonomy";
  
  public static final String       MAJOR_TAXONOMY                                       = "majorTaxonomy";
  
  public static final String       MINOR_TAXONOMY                                       = "minorTaxonomy";
  
  public static final String       TAG_PROPERTY                                         = "tag";
  
  public static final String       TAG_KLASS_TYPE                                       = "Tag";
  
  public static final String       VIEWS_PROPERTY                                       = "views";
  
  public static final String       STRUCTURES_PROPERTY                                  = "structures";
  
  public static final String       EDITOR_PROPERTY                                      = "editor";
  
  public static final String       TREE_PROPERTY                                        = "tree";
  
  public static final String       PROJECT_KLASS_TYPE                                   = "com.cs.core.config.interactor.entity.klass.ProjectKlass";
  
  public static final String       CLASS_FRAME_STRUCTURE_TYPE                           = "com.cs.core.config.interactor.entity.visualattribute.ClassFrameStructure";
  
  public static final String       IMAGE_VISUAL_ATTRIBUTE_FRAME_STRUCTURE_TYPE          = "com.cs.core.config.interactor.entity.visualattribute.ImageVisualAttributeFrameStructure";
  
  public static final String       PROJECT_SET_KLASS_TYPE                               = "com.cs.core.config.interactor.entity.klass.SetKlass";
  
  public static final String       COLLECTION_KLASS_TYPE                                = "com.cs.core.config.interactor.entity.klass.CollectionKlass";
  
  public static final String       ASSET_COLLECTION_KLASS_TYPE                          = "com.cs.core.config.interactor.entity.klass.AssetCollectionKlass";
  
  public static final String       TARGET_COLLECTION_KLASS_TYPE                         = "com.cs.core.config.interactor.entity.klass.TargetCollectionKlass";
  
  public static final String       HTML_VISUAL_ATTRIBUTE_FRAME_STRUCTURE_TYPE           = "com.cs.core.config.interactor.entity.visualattribute.HTMLVisualAttributeFrameStructure";
  
  public static final String       CONTAINER_FRAME_STRUCTURE_TYPE                       = "com.cs.core.config.interactor.entity.visualattribute.ContainerFrameStructure";
  
  public static final String       UNLINKED_CLASS_FRAME_STRUCTURE_TYPE                  = "com.cs.core.config.interactor.entity.visualattribute.UnlinkedClassFrameStructure";
  
  public static final String       REFERENCE_ID_PROPERTY                                = "referenceId";
  
  public static final String       POSITION_PROPERTY                                    = "position";
  
  public static final String       DATA_PROPERTY                                        = "data";
  
  public static final String       ATTRIBUTE_PROPERTY                                   = "attribute";
  
  public static final String       PROPERTY                                             = "property";
  
  public static final String       PROPERTIES                                           = "Properties";
  
  public static final String       PROPERTY_COLLECTION_TYPE                             = "Property_Collection";
  
  public static final String       PROPERTY_GROUP                                       = "PropertyGroup";
  
  public static final String       VIEW_SETTINGS_PROPERTY                               = "viewSettings";
  
  public static final String       LINK_PATH_PROPERTY                                   = "linkPath";
  
  public static final String       PERMISSION_PROPERTY                                  = "permission";
  
  public static final String       ROLE_PERMISSION_PROPERTY                             = "rolePermission";
  
  public static final String       SECTION_PERMISSION_PROPERTY                          = "sectionPermission";
  
  public static final String       DISABLED_ELEMENTS_PROPERTY                           = "disabledElements";
  
  public static final String       IS_DISABLED_PROPERTY                                 = "isDisabled";
  
  public static final String       TASK_KLASS_TYPE                                      = "com.cs.core.config.interactor.entity.task.Task";
  
  public static final String       IS_COLLAPSED_PROPERTY                                = "isCollapsed";
  
  public static final String       IS_HIDDEN_PROPERTY                                   = "isHidden";
  
  // not changed the value
  public static final String       ASSIGNEE_ATTRIBUTE_TYPE                              = "com.cs.config.interactor.entity.concrete.attribute.mandatory.AssigneeAttribute";
  
  public static final String       OWNER_ATTRIBUTE_TYPE                                 = "com.cs.config.interactor.entity.concrete.attribute.mandatory.OwnerAttribute";
  
  public static final String       ICON_PROPERTY                                        = "icon";
  
  public static final String       LABEL_PROPERTY                                       = "label";
  
  public static final String       TAG_VALUES                                           = "tagValues";
  
  public static final String       TAG_TYPE_PROPERTY                                    = "tagType";
  
  public static final String       YES_NEUTRAL_TAG_TYPE_ID                              = "tag_type_yes_neutral";
  
  public static final String       YES_NEUTRAL_NO_TAG_TYPE_ID                           = "tag_type_yes_neutral_no";
  
  public static final String       RANGE_TAG_TYPE_ID                                    = "tag_type_range";
  
  public static final String       RULER_TAG_TYPE_ID                                    = "tag_type_ruler";
  
  public static final String       CUSTOM_TAG_TYPE_ID                                   = "tag_type_custom";
  
  public static final String       ASSET_KLASS_TYPE                                     = "com.cs.core.config.interactor.entity.klass.Asset";
  
  public static final String       MARKET_KLASS_TYPE                                    = "com.cs.core.config.interactor.entity.klass.Market";
  
  public static final String       CONTENT_KLASS_TYPE                                   = "com.cs.core.config.interactor.entity.klass.ContentKlass";
  
  public static final String       IS_NOTIFICATION_ENABLED                              = "isNotificationEnabled";
  
  public static final String       NOTIFICATION_SETTINGS                                = "notificationSettings";
  
  public static final String       IS_DIMENSIONAL                                       = "isDimensional";
  
  public static final String       IS_ENFORCED_TAXONOMY_PROPERTY                        = "isEnforcedTaxonomy";
  
  public static final String       ATTRIBUTION_TAXONOMY_KLASS                           = "Attribution_Taxonomy";
  
  public static final String       KLASS_TAXONOMY_KLASS                                 = "Klass_Taxonomy";

  public static final String       SEQUENCE                                             = "sequence";
  
  public static final String       ENTITY_KLASS                                         = "Klass";
  
  public static final String       CAN_READ_PROPERTY                                    = "canRead";
  
  public static final String       CAN_EDIT_PROPERTY                                    = "canEdit";
  
  public static final String       ORIGINAL_ID                                          = "originalId";
  
  public static final String       BRANCH_NO                                            = "branchNo";
  
  public static final String       BRANCH_OF                                            = "branchOf";
  
  public static final String       VARIANT_ID                                           = "variantId";
  
  public static final String       MAX                                                  = "max";
  
  public static final String       GLOBAL_PERMISSION_PROPERTY                           = "globalPermission";
  
  public static final String       ALLOWED_TYPES_PROPERTY                               = "allowedTypes";
  
  public static final String       HOT_FOLDER_PATH                                      = "hotFolderPath";
  
  public static final String       DATA_RULES                                           = "dataRules";
  
  public static final String       CONDITION                                            = "condition";
  
  public static final String       CAUSE_EFFECT_RULE                                    = "causeEffectRule";
  
  public static final String       STATE                                                = "state";
  
  public static final String       RULE_LIST                                            = "ruleList";
  
  public static final String       RULE_LISTS                                           = "Rule Lists";
  
  public static final String       RETAILER                                             = "retailer";
  
  // Not Changed the values
  public static final String       MASTER_IMPORT_ARTICLE_KLASS_TYPE                     = "com.cs.imprt.config.interactor.entity.concrete.klass.MasterImportArticle";
  
  public static final String       IMPORT_SYSTEM_FILE_ARTICLE_KLASS_TYPE                = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportSystemFileArticle";
  
  public static final String       IMPORT_SYSTEM_INSTANCE_KLASS_TYPE                    = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportSystem";
  
  public static final String       IMPORT_SYSTEM_FILE_KLASS_TYPE                        = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportFile";
  
  public static final String       IMPORT_ARTICLE_KLASS_TYPE                            = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportArticle";
  
  public static final String       TREE_TYPE_OPTION_PROPERTY                            = "treeTypeOption";
  
  public static final String       PARENT_PROPERTY                                      = "parent";
  
  public static final String       FOLDER_PROPERTY                                      = "folder";
  
  public static final String       ALL_PROPERTY                                         = "all";
  
  public static final String       FILE_PROPERTY                                        = "file";
  
  public static final String       CHILDREN_SECTION_TYPE                                = "com.cs.core.config.interactor.entity.propertycollection.ChildrenSection";
  
  public static final String       CAN_CREATE_PROPERTY                                  = "canCreate";
  
  public static final String       CAN_DELETE_PROPERTY                                  = "canDelete";
  
  public static final String       ORIENTDB_CLASS_PROPERTY                              = "@class";
  
  public static final String       IS_FILTERABLE_PROPERTY                               = "isFilterable";
  
  public static final String       IS_SORTABLE_PROPERTY                                 = "isSortable";
  
  public static final String       IS_IGNORED_PROPERTY                                  = "isIgnored";
  
  public static final String       PROPERTY_COLLECTION                                  = "propertyCollection";
  
  public static final String       X_RAY                                                = "xray";
  
  public static final String       UTILIZING_SECTION_IDS_PROPERTY                       = "utilizingSectionIds";
  
  public static final String       ROLE_ID_PROPERY                                      = "roleId";
  
  public static final String       TEMPLATE_ID_PROPERY                                  = "templateId";
  
  public static final String       IS_RELATIONSHIP_PROPERTY_COLLECTION                  = "isRelationshipPropertyCollection";
  
  public static final String       TO_PROPERTY                                          = "to";
  
  public static final String       FROM_PROPERTY                                        = "from";
  
  public static final String       ENTITY_ID_PROPERTY                                   = "entityId";
  
  public static final String       VALUES_PROPERTY                                      = "values";
  
  public static final String       VALUE_PROPERTY                                       = "value";
  
  public static final String       COLOR_PROPERTY                                       = "color";
  
  public static final String       DESCRIPTION_PROPERTY                                 = "description";
  
  public static final String       RULES_PROPERTY                                       = "rules";
  
  public static final String       RULES                                                = "Rules";
  
  public static final String       RULE_PROPERTY                                        = "rule";
  
  public static final String       RULE_LIST_LINK_ID_PROPERTY                           = "ruleListLinkId";
  
  public static final String       ATTRIBUTE_LINK_ID_PROPERTY                           = "attributeLinkId";
  
  public static final String       LIST_PROPERTY                                        = "list";
  
  public static final String       IDS_PROPERTY                                         = "ids";
  
  public static final String       TARGET_RELATIONSHIP_MAPPING_ID_PROPERTY              = "targetRelationshipMappingId";
  
  public static final String       CARDINALITY                                          = "cardinality";
  
  public static final String       SOURCE_CARDINALITY                                   = "sourceCardinality";
  
  public static final String       ARCHIVE                                              = "archive";
  
  public static final String       SUPPLIER_STAGEING_INDEX_NAME                         = "SupplierStaging";
  public static final String       CENTRAL_STAGEING_INDEX_NAME                          = "CentralStaging";
  public static final String       MDM_STAGEING_INDEX_NAME                              = "MDM";
  
  public static final String       SECTION_TYPE                                         = "com.cs.core.config.interactor.entity.propertycollection.Section";
  public static final String       KPI                                                  = "KPIs";
  public static final String       HIERARCHIES                                          = "Hierarchies";
  public static final String       MASTER_TAXONOMIES                                    = "Master Taxonomies";
  public static final String       LINKED_INSTANCE_SECTION                              = "com.cs.core.config.interactor.entity.propertycollection.LinkedInstancesSection";
  public static final String       MATCH_AND_MERGE_SECTION                              = "com.cs.core.config.interactor.entity.matchandmerge.MatchAndMergeSection";
  
  public static final String       PARTNERS                                             = "Partners";
  
  public static final String       USERS                                                = "Users";
  
  public static final String       LANGUAGE_TREE                                        = "Language Tree";
  
  // public static final String TIGHTLY_COUPLED = "tightCoupling";
  
  public static final String       ID_TRANSCACTION                                      = "IDtransaction";
  
  public static final String       RELATIONSHIP_OPPOSITE_SIDE_ISVISIBLE                 = "isOppositeVisible";
  
  public static final String       IS_STANDARD                                          = "isStandard";
  
  public static final String       NOT_EMPTY_PROPERTY                                   = "notempty";
  
  public static final String       EMPTY_PROPERTY                                       = "empty";
  
  public static final String       KLASSID                                              = "klassId";
  public static final String       CLASSES                                              = "Classes";
  
  public static final String       TEXT_ASSET_KLASS_TYPE                                = "com.cs.core.config.interactor.entity.textasset.TextAsset";
  public static final String       SUPPLIER_KLASS_TYPE                                  = "com.cs.core.config.interactor.entity.supplier.Supplier";
  
  public static final String       ARTICLE_INSTANCE_MODULE_ENTITY                       = "ArticleInstance";
  public static final String       ASSET_INSTANCE_MODULE_ENTITY                         = "AssetInstance";
  public static final String       MARKET_INSTANCE_MODULE_ENTITY                        = "MarketInstance";
  public static final String       TEXT_ASSET_INSTANCE_MODULE_ENTITY                    = "TextAssetInstance";
  public static final String       SUPPLIER_INSTANCE_MODULE_ENTITY                      = "SupplierInstance";
  public static final String       FILE_INSTANCE_MODULE_ENTITY                          = "FileInstance";
  
  public static final String       NON_NATURE_TYPE                                      = "NON_NATURE";
  public static final String       NATURE_TYPE                                          = "NATURE";
  
  public static final String       BASE_TYPE                                            = "baseType";
  
  // metadata constants
  public static final String       PROPERTY_MAP                                         = "propertyMap";
  public static final String       PRIORITY                                             = "priority";
  public static final String       XMP                                                  = "xmp";
  public static final String       IPTC                                                 = "iptc";
  public static final String       EXIF                                                 = "exif";
  public static final String       OTHER                                                = "other";
  
  public static final String       STANDARD_ATTRIBUTE_KLASS_TYPE                        = "Standard_Attribute";
  
  //all standard attribute base types
  public static final List<String> STANDARD_ATTRIBUTE_TYPES                                = Arrays
      .asList("com.cs.config.interactor.entity.concrete.attribute.standard.CreatedOnAttribute",
          "com.cs.config.interactor.entity.concrete.attribute.standard.LastModifiedAttribute",
          "com.cs.config.interactor.entity.concrete.attribute.standard.CreatedByAttribute"
          );
  // all unit attributes base types
  public static final List<String> UNIT_ATTRIBUTE_TYPES                                 = Arrays
      .asList(AREA_ATTRIBUTE_TYPE, CURRENCY_ATTRIBUTE_TYPE, PRICE_ATTRIBUTE_TYPE,
          CURRENT_ATTRIBUTE_TYPE, CURRENT_ATTRIBUTE_TYPE, ENERGY_ATTRIBUTE_TYPE,
          FREQUENCY_ATTRIBUTE_TYPE, LENGTH_ATTRIBUTE_TYPE, MASS_ATTRIBUTE_TYPE,
          PLANE_ANGLE_ATTRIBUTE_TYPE, POTENTIAL_ATTRIBUTE_TYPE, PRESSURE_ATTRIBUTE_TYPE,
          SPEED_ATTRIBUTE_TYPE, TEMPERATURE_ATTRIBUTE_TYPE, TIME_ATTRIBUTE_TYPE,
          VOLUME_ATTRIBUTE_TYPE, DIGITAL_STORAGE_ATTRIBUTE_TYPE, SELLING_PRICE_ATTRIBUTE_TYPE,
          LIST_PRICE_ATTRIBUTE_TYPE, MAXIMUM_PRICE_ATTRIBUTE_TYPE, MINIMUM_PRICE_ATTRIBUTE_TYPE,
          POWER_ATTRIBUTE_TYPE, LUMINOSITY_ATTRIBUTE_TYPE, RADIATION_ATTRIBUTE_TYPE,
          ILLUMINANCE_ATTRIBUTE_TYPE, FORCE_ATTRIBUTE_TYPE, ACCELERATION_ATTRIBUTE_TYPE,
          CAPACITANCE_ATTRIBUTE_TYPE, VISCOCITY_ATTRIBUTE_TYPE, INDUCTANCE_ATTRIBUTE_TYPE,
          RESISTANCE_ATTRIBUTE_TYPE, MAGNETISM_ATTRIBUTE_TYPE, CHARGE_ATTRIBUTE_TYPE,
          CONDUCTANCE_ATTRIBUTE_TYPE, SUBSTANCE_ATTRIBUTE_TYPE, WEIGHT_PER_AREA_ATTRIBUTE_TYPE,
          THERMAL_INSULATION_ATTRIBUTE_TYPE, PROPORTION_ATTRIBUTE_TYPE, HEATING_RATE_ATTRIBUTE_TYPE,
          DENSITY_ATTRIBUTE_TYPE, WEIGHT_PER_TIME_ATTRIBUTE_TYPE, VOLUME_FLOW_RATE_ATTRIBUTE_TYPE,
          AREA_PER_VOLUME_ATTRIBUTE_TYPE, ROTATION_FREQUENCY_ATTRIBUTE_TYPE,
          CUSTOM_UNIT_ATTRIBUTE_TYPE);
  
  public static final List<String> VERSION_DOC_TYPES                                    = Arrays
      .asList(ElasticDocTypeConstants.ATTRIBUTE_DOC_TYPE, ElasticDocTypeConstants.TAG_DOC_TYPE,
          ElasticDocTypeConstants.ROLE_DOC_TYPE,
          ElasticDocTypeConstants.ARTICLE_KLASS_INSTANCE_DOC_TYPE,
          ElasticDocTypeConstants.ASSET_INSTANCE_DOC_TYPE,
          ElasticDocTypeConstants.ATTACHMENTINSTANCE_CACHE,
          ElasticDocTypeConstants.ARTICLE_KLASS_STRUCTURE_DOC_TYPE,
          ElasticDocTypeConstants.MARKET_TARGET_INSTANCE_DOC_TYPE,
          ElasticDocTypeConstants.PROMOTION_INSTANCE_DOC_TYPE,
          ElasticDocTypeConstants.SUPPLIER_INSTANCE_DOC_TYPE,
          ElasticDocTypeConstants.TEXTASSET_INSTANCE_DOC_TYPE,
          ElasticDocTypeConstants.RELATIONSHIP_OBJECT_DOC_TYPE,
          ElasticDocTypeConstants.FILE_INSTANCE_DOC_TYPE);
  
  public static final List<String> RELATIONSHIP_VERSION_DOC_TYPES                       = Arrays
      .asList(ElasticDocTypeConstants.RELATIONSHIP_INSTANCE_DOC_TYPE,
          ElasticDocTypeConstants.NATURE_RELATIONSHIP_INSTANCE_DOC_TYPE);
  
  public static final String       FAILURE                                              = "failure";
  public static final String       MAPPING                                              = "mapping";
  public static final String       SUCCESS                                              = "success";
  public static final String       INDEX_NAME                                           = "indexName";
  public static final String       VERSION_INDEX_NAME                                   = "versionIndexName";
  public static final String       DOC_TYPE                                             = "docType";
  public static final String       VERSION_DOC_TYPE                                     = "versionDocType";
  public static final String       IS_ROLLBACK                                          = "isRollback";
  public static final String       GET_ALL_CHILDREN                                     = "getAllChildren";
  public static final String       IS_LOAD_MORE                                         = "isLoadMore";
  public static final String       TREE_GET_LEAF                                        = "treeGetLeaf";
  public static final String       TAG_VALUES_TO_REMOVE                                 = "tagValuesToRemove";
  public static final String       CREATED_ON_PROPERTY                                  = "createdOn";
  public static final String       VARIANT_OF                                           = "variantOf";
  public static final String       NAME_PROPERTY                                        = "name";
  public static final String       PARENT_ID_PROPERTY                                   = "parentId";
  public static final String       VERSION_LIST                                         = "versionList";
  public static final String       VARIANT_LIST                                         = "variantList";
  public static final String       CONTAINS                                             = "contains";
  public static final String       LESS_THAN                                            = "lt";
  public static final String       GREATER_THAN                                         = "gt";
  public static final String       EXACT                                                = "exact";
  public static final String       END                                                  = "end";
  public static final String       START                                                = "start";
  public static final String       EMPTY                                                = "empty";
  public static final String       NOT_EMPTY                                            = "notempty";
  public static final String       RANGE                                                = "range";
  public static final String       OLD_VALUE                                            = "oldValue";
  public static final String       CANDIDATES                                           = "candidates";
  public static final String       ATTRIBUTE                                            = "attribute";
  
  public static final String       ATTRIBUTE_KLASS_TYPE                                 = "Attribute";
  
  public static final String       TIGHTLY_COUPLED                                      = "tightlyCoupled";
  public static final String       LOOSELY_COUPLED                                      = "looselyCoupled";
  public static final String       DYNAMIC_COUPLED                                      = "dynamicCoupled";
  public static final String       READ_ONLY_COUPLED                                    = "readOnlyCoupled";
  
  public static final String       TAG                                                  = "tag";
  public static final String       TAGS                                                 = "tags";
  public static final String       ROLE                                                 = "role";
  public static final String       NAME_ATTRIBUTE                                       = "nameattribute";
  public static final String       ATTRIBUTE_VERSION_TIGHT                              = "attributeVersionTight";
  public static final String       ATTRIBUTE_VERSION_DYNAMIC                            = "attributeVersionDynamic";
  public static final String       ATTRIBUTE_VARIANT_TIGHT                              = "attributeVariantTight";
  public static final String       ATTRIBUTE_VARIANT_DYNAMIC                            = "attributeVariantDynamic";
  public static final String       TAG_VERSION_TIGHT                                    = "tagVersionTight";
  public static final String       TAG_VERSION_DYNAMIC                                  = "tagVersionDynamic";
  public static final String       TAG_VARIANT_TIGHT                                    = "tagVariantTight";
  public static final String       TAG_VARIANT_DYNAMIC                                  = "tagVariantDynamic";
  public static final String       ROLE_VERSION_TIGHT                                   = "roleVersionTight";
  public static final String       ROLE_VERSION_DYNAMIC                                 = "roleVersionDynamic";
  public static final String       ROLE_VARIANT_TIGHT                                   = "roleVariantTight";
  public static final String       ROLE_VARIANT_DYNAMIC                                 = "roleVariantDynamic";
  public static final String       SORT_FIELD                                           = "sortField";
  public static final String       SORT_ORDER                                           = "sortOrder";
  public static final String       SORTORDER_ASC                                        = "asc";
  public static final String       SORTORDER_DESC                                       = "desc";
  public static final String       FROM                                                 = "from";
  public static final String       SIZE                                                 = "size";
  public static final String       SEARCH_STRING                                        = "searchString";
  public static final String       DOC_TYPES                                            = "docTypes";
  public static final String       IS_FOLDER_PROPERTY                                   = "isFolder";
  public static final String       ATTRIBUTES                                           = "attributes";
  public static final String       PROPERTY_VERSION_TYPE                                = "propertyVersionType";
  public static final String       LOWER_THRESHOLD                                      = "lowerThreshold";
  public static final String       UPPER_THRESHOLD                                      = "upperThreshold";
  public static final String       IS_GET_CHILDREN                                      = "isGetChildren";
  
  public static final String       CREATE_BRANCH_NAME_SUFFIX                            = "_v";
  public static final String       CREATE_VARIANT_NAME_PREFIX                           = "Copy of ";
  public static final String       VERSION_ID_SEPERATOR                                 = "_";
  public static final String       NEW_ID                                               = "newId";
  public static final String       USER_ID                                              = "userId";
  public static final String       ASSET_INSTANCE_DOC_TYPE                              = "assetinstancecache";
  public static final String       ATTACHMENT_INSTANCE_DOC_TYPE                         = "attachmentinstancecache";
  public static final String       FILTER_IDS_PROPERTY                                  = "filterIds";
  public static final String       IDS_TO_EXCLUDE_PROPERTY                              = "idsToExclude";
  public static final String       KLASS_INSTANCE_IDS                                   = "klassInstanceIds";
  public static final String       IS_HTML_ATTRIBUTE                                    = "isHtmlAttribute";
  public static final String       IS_FOLDER                                            = "isFolder";
  public static final String       QUICK_LIST_MODE                                      = "mode";
  public static final String       TYPE                                                 = "type";
  public static final String       SECONDARY_TYPES                                      = "secondaryTypes";
  
  // table pproperties
  public static final String       TABLE_ATTRIBUTES                                     = "tableAttributes";
  public static final String       TABLE_TAGS                                           = "tableTags";
  public static final String       TABLE_ASSETS                                         = "tableAssets";
  
  public static final String       ATTRIBUTEID_OBJECT_MAP                               = "attributeIdObjectMap";
  public static final String       TAGID_OBJECT_MAP                                     = "tagIdObjectMap";
  public static final String       ASSETID_OBJECT_MAP                                   = "assetIdObjectMap";
  
  public static final String       SORT_FIELD_RELEVANCE                                 = "relevance";
  public static final String       COUNT                                                = "count";
  
  // relationships
  public static final String       RELATIONSHIP_ID                                      = "relationshipId";
  public static final String       ADMIN_USER_ID                                        = "admin";
  public static final String       RELATIONSHIP_MAPPING_ID                              = "relationshipMappingId";
  public static final String       ELEMENT_IDS                                          = "elementIds";
  public static final String       DELETED_ELEMENTS                                     = "deletedElements";
  public static final String       ADDED_ELEMENTS                                       = "addedElements";
  public static final String       TARGET_RELATIONSHIP_MAPPING_ID                       = "targetRelationshipMappingId";
  public static final String       CARDINALITY_ONE                                      = "One";
  public static final String       CARDINALITY_MANY                                     = "Many";
  public static final String       SIDE_1_INSTANCE_ID                                   = "side1InstanceId";
  public static final String       SIDE_2_INSTANCE_ID                                   = "side2InstanceId";
  public static final String       SIDE_1_ID                                            = "side1Id";
  public static final String       SIDE_2_ID                                            = "side2Id";
  public static final String       RELATIONSHIP_SIDE                                    = "relationshipSide";
  public static final String       SIDE_PROPERTY                                        = "side";
  public static final String       RELATIONSHIP_OBJECT_ID                               = "relationship"
      + "ObjectId";
  
  public static final String       TARGET_TYPE                                          = "targetType";
  // public static final String RELATIONSHIP_SECTION_TYPE =
  // "com.cs.config.interactor.entity.RelationshipSection";
  
  public static final String       SIDE_1_INSTANCE_VERSION_ID                           = "side1InstanceVersionId";
  public static final String       SIDE_2_INSTANCE_VERSION_ID                           = "side2InstanceVersionId";
  public static final String       VERSION_ID                                           = "versionId";
  public static final String       AUTO_UPDATE                                          = "autoUpdate";
  
  public static final String       ATTRIBUTE_VALUE_PROPERTY                             = "attributeValue";
  public static final String       TAG_VALUE_PROPERTY                                   = "tagValue";
  
  public static final String       CONTEXTUAL_VARIANT                                   = "contextualVariant";
  public static final String       GTIN_VARIANT                                         = "gtinVariant";
  public static final String       PRODUCT_VARIANT                                      = "productVariant";
  public static final String       LANGUAGE_VARIANT                                     = "languageVariant";
  public static final String       PROMOTIONAL_VERSION                                  = "promotionalVersion";
  public static final String       IMAGE_VARIANT                                        = "imageVariant";
  public static final String       ATTRIBUTE_VARIANT                                    = "attributeVariant";
  public static final String       ATTRIBUTE_VARIANT_CONTEXT                            = "attributeVariantContext";
  public static final String       RELATIONSHIP_VARIANT                                 = "relationshipVariant";
  public static final String       PROMOTION_CONTEXT                                    = "promotionContext";
  
  // Article natureTypes
  public static final String       PID_SKU                                              = "pidSku";
  public static final String       SINGLE_ARTICLE                                       = "singleArticle";
  public static final String       GTIN_KLASS_TYPE                                      = "gtin";
  public static final String       EMBEDDED_KLASS_TYPE                                  = "embedded";
  public static final String       TECHNICAL_IMAGE_VARIANT_KLASS_TYPE                   = "technicalImage";
  public static final String       LANGUAGE_KLASS_TYPE                                  = "language";
  
  public static final List<String> CONTEXTUAL_KLASSES                                   = Arrays
      .asList(GTIN_KLASS_TYPE, EMBEDDED_KLASS_TYPE,
          TECHNICAL_IMAGE_VARIANT_KLASS_TYPE, LANGUAGE_KLASS_TYPE);
  
  /** Promotion Nature Relationship Types */
  public static final String       SCOPE_PROMOTION_RELATIONSHIP                         = "scopePromotionRelationship";
  
  public static final String       TARGET_PROMOTION_RELATIONSHIP                        = "targetPromotionRelationship";
  
  // MAM natureTypes
  public static final String       MAM_NATURE_TYPE_IMAGE                                = "imageAsset";
  public static final String       MAM_NATURE_TYPE_VIDEO                                = "videoAsset";
  public static final String       MAM_NATURE_TYPE_DOCUMENT                             = "documentAsset";
  public static final String       MAM_NATURE_TYPE_FILE                                 = "fileAsset";
  
  // Nature relationship types..
  public static final String       PROMOTIONAL_VERSION_RELATIONSHIP                     = "promotionalVersionRelationship";
  public static final String       PRODUCT_VARIANT_RELATIONSHIP                         = "productVariantRelationship";
  public static final String       VERSION_CONTEXT_RELATIONSHIP                         = "versionContextRelationship";
  public static final String       FIXED_BUNDLE                                         = "fixedBundleRelationship";
  public static final String       SET_OF_PRODUCTS                                      = "setOfProductsRelationship";
  
  public static final List<String> NATURE_RELATIONSHIP_TYPES                            = Arrays
      .asList(PROMOTIONAL_VERSION_RELATIONSHIP, PRODUCT_VARIANT_RELATIONSHIP,
          FIXED_BUNDLE, SET_OF_PRODUCTS);
  
  public static final String       EVENT_INSTANCE_DOCTYPE                               = "eventinstancecache";
  public static final String       COLLECTION_DOCTYPE                                   = "collectioncache";
  /**
   * ******************************* Events constants
   * **********************************
   */
  public static final String       MONDAY                                               = "MON";
  
  public static final String       TUESDAY                                              = "TUE";
  public static final String       WEDNESDAY                                            = "WED";
  public static final String       THURSDAY                                             = "THU";
  public static final String       FRIDAY                                               = "FRI";
  public static final String       SATURDAY                                             = "SAT";
  public static final String       SUNDAY                                               = "SUN";
  
  public static final String       DAILY                                                = "DAILY";
  public static final String       WEEKLY                                               = "WEEKLY";
  public static final String       MONTHLY                                              = "MONTHLY";
  public static final String       YEARLY                                               = "YEARLY";
  public static final String       NONE                                                 = "NONE";
  
  public static final String       STARTTIME                                            = "startTime";
  public static final String       ENDTIME                                              = "endTime";
  
  
  public static final String       CONTEXT_INSTANCE_ID                                  = "contextInstanceId";
  public static final String       CONTEXT_ID                                           = "contextId";
  
  /**
   * ********************************** HTTP Properties Constants
   * ************************
   */
  public static final String       CONTENT_INLINE                                       = "inline;filename=\"";
  
  public static final String       CONTENT_ATTACHMENT                                   = "attachment;filename=\"";
  public static final String       CONTENT_TYPE_IMAGE                                   = "image/";
  public static final String       CONTENT_TYPE_VIDEO                                   = "video/";
  public static final String       CONTENT_TYPE_APPLICATION                             = "application/";
  
  public static final List<String> IMAGE_TYPES                                          = Arrays
      .asList("jpg", "jpeg", "png", "ico", "eps", "ai", "psd");
  public static final List<String> VIDEO_TYPES                                          = Arrays
      .asList("wmv", "avi", "mov", "flv", "mpeg", "mpg", "mp4");
  
  public static final String       HEADER_IF_MATCH                                      = "If-Match";
  public static final String       HEADER_RANGE                                         = "Range";
  public static final String       HEADER_ACCEPT_RANGES                                 = "Accept-Ranges";
  public static final String       HEADER_CONTENT_LENGTH                                = "Content-Length";
  public static final String       HEADER_CONTENT_RANGE                                 = "Content-Range";
  public static final String       HEADER_CONTENT_TYPE                                  = "Content-Type";
  public static final String       HEADER_ETAG                                          = "Etag";
  public static final String       HEADER_FILENAME                                      = "X-Object-Meta-Name";
  
  public static final String       ENDPOINT                                             = "endpoint";
  public static final String       INBOUND_ENDPOINT                                     = "inbound";
  public static final String       OUTBOUND_ENDPOINT                                    = "outbound";
  public static final String       INBOUND_OUTBOUND_ENDPOINT                            = "both";
  public static final String       ENDPOINT_OWNER                                       = "isOwner";
  public static final String       TEMPLATE_LANGUAGE_TAB_BASETYPE                       = "com.cs.config.interactor.entity.template.TemplateLanguageTab";
  public static final String       TEMPLATE_TASKS_TAB_BASETYPE                          = "com.cs.core.config.interactor.entity.template.TemplateTasksTab";
  public static final String       TEMPLATE_TIME_LINE_TAB_BASETYPE                      = "com.cs.core.config.interactor.entity.template.TemplateTimelineTab";
  public static final String       CUSTOM_TEMPLATE_TAB_BASETYPE                         = "com.cs.core.config.interactor.entity.template.CustomTemplateTab";
  
  public static final String       SEQUENCE_LIST                                        = "sequenceList";
  public static final Integer      SEQUENCE_LIST_LIMIT                                  = 25;
  
  // Template types constants
  public static final String       CONTEXTUAL_TEMPLATE                                  = "contextualTemplate";
  public static final String       TAXONOMY_TEMPLATE                                    = "taxonomyTemplate";
  public static final String       KLASS_TEMPLATE                                       = "klassTemplate";
  public static final String       CUSTOM_TEMPLATE                                      = "customTemplate";
  public static final String       COLLECTION_TEMPLATE                                  = "CollectionTemplate";
  
  public static final String       PRIMARY_TYPES                                        = "primaryTypes";
  public static final String       TAXONOMIES                                           = "taxonomies";
  public static final String       TAXONOMIES_ENTITY                                    = "Taxonomies";
  
  // Changes till here
  public static final String       KLASS_CONFLICTING_SOURCE_TYPE                        = "com.cs.core.runtime.interactor.entity.klassinstance.KlassConflictingValueSource";
  public static final String       COLLECTION_CONFLICTING_SOURCE_TYPE                   = "com.cs.core.runtime.interactor.entity.datarule.CollectionConflictingValueSource";
  public static final String       TAXONOMY_CONFLICTING_SOURCE_TYPE                     = "com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflictingValueSource";
  public static final String       RELATIONSHIP_CONFLICTING_SOURCE_TYPE                 = "com.cs.core.runtime.interactor.entity.relationship.RelationshipConflictingValueSource";
  public static final String       LANGUAGE_CONFLICTING_SOURCE_TYPE                     = "com.cs.core.runtime.interactor.entity.language.LanguageConflictingValueSource";
  
  public static final String       TAXONOMY_LEVEL                                       = "level";
  public static final String       CONTEXT_CONFLICTING_SOURCE_TYPE                      = "com.cs.core.runtime.interactor.entity.datarule.ContextConflictingValueSource";
  
  public static final String       ARCHIVE_INDEX_NAME                                   = "archiveIndexName";
  
  public static final String       DEFAULT_LABEL_FOR_SUB_PROMOTION                      = "Sub Promotion";
  
  public static final String       THUMBNAIL_VIEW                                       = "THUMBNAIL";
  public static final String       GRID_VIEW                                            = "GRID";
  
  public static final String       KLASS_IDS_HAVING_RP                                  = "klassIdsHavingRP";
  public static final String       TAXONOMY_IDS_HAVING_RP                               = "taxonomyIdsHavingRP";
  public static final String       ENTITIES                                             = "entities";
  
  public static final String       SEPERATOR                                            = "_-_-_";
  
  public static final String       TAXONOMY_HIERARCHY_ROOT                              = "taxonomyHierarchyRoot";
  
  public static final String       ONBOARDING_ENDPOINT                                  = "onboardingendpoint";
  
  public static final String       OFFBOARDING_ENDPOINT                                 = "offboardingendpoint";
  
  public static final String       OFFBOARDING                                          = "offBoarding";
  public static final String       ONBOARDING                                           = "onboarding";
  public static final String       DATA_INTEGRATION                                     = "dataIntegration";
  // Used to get task instances for all roles in dashboard
  public static final String       ID_ALL                                               = "all";
  
  // Global Permission Entity Types
  public static final String       GLOBAL_PERMISSION_ENTITY_KLASS                       = "klass";
  public static final String       GLOBAL_PERMISSION_ENTITY_TAXONOMY                    = "taxonomy";
  public static final String       GLOBAL_PERMISSION_ENTITY_CONTEXT                     = "context";
  public static final String       GLOBAL_PERMISSION_ENTITY_TASKS                       = "tasks";
  
  public static final String       TASK_TYPE_PERSONAL                                   = "personal";
  public static final String       TASK_TYPE_SHARED                                     = "shared";
  
  // Notification actions
  public static final String       ASSIGNED_YOU                                         = "assignedYou";
  public static final String       REMOVED_YOU                                          = "removedYou";
  public static final String       COMMENT                                              = "comment";
  public static final String       COMMENT_WITH_ATTACHMENT                              = "commentWithAttachment";
  public static final String       ATTACHMENT_UPLOADED                                  = "attachmentUploaded";
  public static final String       ATTACHMENT_DELETED                                   = "attachmentDeleted";
  public static final String       USERS_ADDED                                          = "usersAdded";
  public static final String       USERS_DELETED                                        = "usersDeleted";
  public static final String       STATUS_CHANGED                                       = "statusChanged";
  public static final String       PRIORITY_CHANGED                                     = "priorityChanged";
  public static final String       SUBTASK_ADDED                                        = "subtaskAdded";
  public static final String       SUBTASK_DELETED                                      = "subtaskDeleted";
  public static final String       TASK_MODIFIED                                        = "taskModified";
  public static final String       START_DATE_CHANGED                                   = "startDateChanged";
  public static final String       DUE_DATE_CHANGED                                     = "dueDateChanged";
  public static final String       OVER_DUE_DATE_CHANGED                                = "overDueDateChanged";
  public static final String       TASK_DELETED                                         = "taskDeleted";
  
  public static final Integer      MAX_VERSIONS_ALLOWED_FOR_PROPERTIES                  = 5;
  public static final String       IS_CONTEXTUAL                                        = "isContextual";
  
  public static final String       DATE_INSTANCE_BASE_TYPE                              = "com.cs.core.config.interactor.model.attribute.DateInstanceModel";
  public static final String       LINKED_INSTANCES_BASE_TYPE                           = "com.cs.core.config.interactor.model.asset.LinkedInstancesPropertyModel";
  
  public static final String       DATE_TYPE                                            = "date";
  public static final String       ARTICLE                                              = "Article";
  public static final String       ASSET                                                = "Asset";
  public static final String       MARKET                                               = "Market";
  public static final String       TEXT_ASSET                                           = "TextAsset";
  public static final String       SUPPLIER                                             = "Supplier";
  public static final List<String> KLASS_TYPES                                          = Arrays
      .asList(CommonConstants.ARTICLE, CommonConstants.ASSET, CommonConstants.MARKET,
          CommonConstants.TEXT_ASSET, CommonConstants.SUPPLIER);
  
  public static final String       ATTRIBUTE_VARIANT_TYPE                               = "attributeVariant";
  
  public static final String       PRIVACY_CHANGED                                      = "privacyChanged";
  public static final String       LABEL_CHANGED                                        = "labelChanged";
  public static final String       DESCRIPTION_CHANGED                                  = "descriptionChanged";
  public static final String       DUE_DATE_CROSSED                                     = "dueDateCrossed";
  public static final String       DUE_DATE_TODAY                                       = "dueDateToday";
  public static final String       DUE_DATE_APPROACHING                                 = "dueDateApproaching";
  
  public static final String       LEVEL                                                = "level";
  
  // Notification status
  public static final String       READ                                                 = "read";
  public static final String       UNREAD                                               = "unread";
  
  public static final String       ONBOARDING_PROCESS                                   = "onboardingProcess";
  public static final String       OFFBOARDING_PROCESS                                  = "offboardingProcess";
  public static final String       JMS_ONBOARDING_PROCESS                               = "jmsOnboardingProcess";
  public static final String       JMS_OFFBOARDING_PROCESS                              = "jmsOffboardingProcess";
  public static final String       DI_OFFBOARDING_PROCESS                               = "diOffboardingProcess";
  // MAM Validity Messages
  public static final String       VALIDITY_MESSAGE_EXPIRED                             = "Expired";
  
  public static final String       DUE_DATE_REMINDER                                    = "dueDateReminder";
  
  public static final List<String> CONTEXTUAL_KLASS_TYPES                               = Arrays
      .asList(CommonConstants.EMBEDDED_KLASS_TYPE,
          CommonConstants.GTIN_KLASS_TYPE, CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE,
          CommonConstants.LANGUAGE_KLASS_TYPE);
  
  public static final String       COMPLETENESS_BLOCK                                   = "completenessBlock";
  public static final String       ACCURACY_BLOCK                                       = "accuracyBlock";
  public static final String       UNIQUENESS_BLOCK                                     = "uniquenessBlock";
  public static final String       CONFORMITY_BLOCK                                     = "conformityBlock";
  public static final List<String> GOVERNANCE_RULE_BLOCK_TYPES                          = Arrays
      .asList(CommonConstants.COMPLETENESS_BLOCK, CommonConstants.ACCURACY_BLOCK,
          CommonConstants.CONFORMITY_BLOCK, CommonConstants.UNIQUENESS_BLOCK);
  
  public static final List<String> RACIVS_ROLE_IDS                                      = Arrays
      .asList(SystemLevelIds.RESPONSIBLE, SystemLevelIds.ACCOUNTABLE, SystemLevelIds.CONSULTED,
          SystemLevelIds.INFORMED, SystemLevelIds.VERIFY, SystemLevelIds.SIGN_OFF);
  
  public static final List<String> MODULE_ENTITIES                                      = Arrays
      .asList(Constants.ARTICLE_INSTANCE_MODULE_ENTITY, Constants.ASSET_INSTANCE_MODULE_ENTITY,
          Constants.MARKET_INSTANCE_MODULE_ENTITY,
          Constants.TEXT_ASSET_INSTANCE_MODULE_ENTITY,
          Constants.SUPPLIER_INSTANCE_MODULE_ENTITY);
  
  public static final String       PHYSICAL_CATALOG_ID                                  = "physicalCatalogId";
  public static final String       PORTAL_ID                                            = "portalId";
  public static final String       ORGANIZATION_ID                                      = "organizationId";
  public static final String       ENDPOINT_ID                                          = "endpointId";
  public static final String       LOGICAL_CATALOG_ID                                   = "logicalCatalogId";
  public static final String       SYSTEM_ID                                            = "systemId";
  public static final String       LOCALE_ID                                            = "localeId";
  
  public static final String       UI_LANGUAGE                                          = "uiLanguage";
  public static final String       SCREENS                                              = "screens";
  
  public static final String       SELECTED_TAG_VALUES_LIST                             = "selectedTagValuesList";
  public static final String       CONTEXT                                              = "context";
  public static final String       CONTEXT_ENTITY                                       = "Context";
  public static final String       VARIANT_CONTEXT                                      = "Variant_Context";
  public static final String       TASK                                                 = "task";
  public static final String       TEMPLATE                                             = "template";
  public static final String       TAXONOMY                                             = "taxonomy";
  public static final String       RULE                                                 = "rule";
  public static final String       TAXONOMY_MASTER_LIST                                 = "taxonomyMasterList";
  public static final String       USER                                                 = "user";
  public static final String       ADMIN                                                = "admin";
  public static final String       PROFILE                                              = "profile";
  public static final String       PROCESS                                              = "process";
  public static final String       ATTRIBUTION_TAXONOMY                                 = "attributionTaxonomy";
  public static final String       LANGUAGE                                             = "language";
  public static final String       TARGET                                               = "target";
  public static final String       TARGET_KLASS_TYPE                                    = "Target";
  public static final String       ARTICLE_ENTITY                                       = "article";
  public static final String       ASSET_ENTITY                                         = "asset";
  public static final String       MARKET_ENTITY                                        = "market";
  public static final String       TEXT_ASSET_ENTITY                                    = "textAsset";
  public static final String       SUPPLIER_ENTITY                                      = "supplier";
  
  public static final String       ORGANIZATION                                         = "organization";
  public static final String       SYSTEM                                               = "system";
  public static final String       DATA_GOVERNANCE                                      = "dataGovernance";
  public static final String       KEYPERFORMANCEINDEX                                  = "keyperformanceindex";
  public static final String       TAB                                                  = "tab";
  public static final String       TAB_KLASS_TYPE                                       = "Tab";
  public static final String       DASHBOARD_TAB                                        = "dashboardTab";
  public static final String       GOLDEN_RECORDS                                       = "goldenRecords";
  public static final String       GOLDEN_RECORD_RULE                                   = "Golden Record Rules";
  
  public static final String       SUB_STRING_NORMALIZATION_BASE_TYPE                   = "com.cs.core.config.interactor.model.datarule.SubStringNormalization";
  public static final String       FIND_REPLACE_NORMALIZATION_BASE_TYPE                 = "com.cs.core.config.interactor.model.datarule.FindReplaceNormalization";
  public static final String       NORMALIZATION_BASE_TYPE                              = "com.cs.core.config.interactor.model.datarule.Normalization";
  public static final String       ATTRIBUTE_VALUE_NORMALIZATION_BASE_TYPE              = "com.cs.core.config.interactor.model.attribute.AttributeValueNormalization";
  public static final String       CONCATENATED_NORMALIZATION_BASE_TYPE                 = "com.cs.core.config.interactor.model.datarule.ConcatenatedNormalization";
  
  public static final String       KLASS_ENTITY                                         = "class";
  public static final String       KLASS_PROPERTY_OREINT                                = "@class";
  
  public static final String       LISTENER_INIT_ERROR                                  = "Cannot Initialize Message Listener";
  
  public static final String       PROPERTY_ID                                          = "propertyId";
  
  public static final String       CLASSIFICATION                                       = "classification";
  
  public static final String       TEMPLATES                                            = "Templates";
  
  public static final List<String> PROPERTIES_TO_EXCLUDE_FOR_GRID                       = Arrays
      .asList(Constants.ASSET_COVERFLOW_ATTRIBUTE_ID);
  
  public static final String       ERRORLOG_CSV                                         = "ErrorLog.csv";
  public static final String       SUCCESSLOG_CSV                                       = "SuccessLog.csv";
  public static final String       SYSTEM_ADMIN_ROLE_TYPE                               = "SystemAdmin";
  
  public static final String       TALEND_JAR_CLASS_PATH                                = "classpath*:/talendjar/*.*";
  public static final String       EXTERNAL_JAR_CLASS_PATH                              = "classpath*:/externallibs/*.*";
  
  public static final String       IDS_USECASE                                          = "IDSusecase";
  public static final String       ACQUIRE_LOCK                                         = "acquire";
  public static final String       RELEASE_LOCK                                         = "release";
  
  public static final String       DASHBOARD                                            = "dashboard";
  
  public static final String       HIERARCHY_TAXONOMY                                   = "hierarchyTaxonomy"; //PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies -- Remove it after usage checking
  public static final String       MASTER_TAXONOMY                                      = "masterTaxonomy";
  public static final String       COLLECTION                                           = "collection";
  
  public static final String       SUPPLIER_NATURE_TYPE                                 = "supplier";
  public static final String       STATIC_TRANSLATION                                   = "staticTranslation";
  
  
  public static final List<String> ALL_CONFIG_ENTITIES                                  = Arrays
      .asList(TAG, ATTRIBUTE, CONTEXT, TASK, PROPERTY_COLLECTION, ARTICLE_ENTITY,
          ASSET_ENTITY, TARGET, TEXT_ASSET_ENTITY,
          SUPPLIER_ENTITY, KLASS_ENTITY, TEMPLATE, TAXONOMY, ROLE, RULE,
          TAXONOMY_MASTER_LIST, USER, PROFILE, MAPPING, PROCESS, ATTRIBUTION_TAXONOMY, RULE_LIST,
          RELATIONSHIP, ORGANIZATION, SYSTEM, DATA_GOVERNANCE, KEYPERFORMANCEINDEX, TAB,
          ENTITY_KLASS_TYPE, DASHBOARD_TAB);
  
  public static final String       SUPPLIER_PRIORITY                                    = "supplierPriority";
  public static final String       PROCESS_INSTANCE_ID                                  = "processInstanceId";
  public static final String       TAXONOMY_TYPE                                        = "taxonomyType";
  
  public static final String       DATA_LANGUAGE                                        = "dataLanguage";
  public static final String       ATTRIBUTE_VALUE_TRANFORMATION_TYPE                   = "attributeValue";
  public static final String       CONCAT_TRANSFORMATION_TYPE                           = "concat";
  
  public static final String       SHOULD_INHERIT_PROPERTY                              = "shouldInherit";
  
  public static final String       SHOULD_MAINTAIN_ARCHIVES                             = "shouldMaintainArchives";
  public static final String       REFERENCE                                            = "reference";
  
  // Smart Document
  public static final String       SMART_DOCUMENT_ENTITY                                = "smartDocument";
  public static final String       SMART_DOCUMENT_TEMPLATE                              = "smartDocumentTemplate";
  public static final String       SMART_DOCUMENT_PRESET                                = "smartDocumentPreset";
  
  public static final String       PROCESS_EVENT                                        = "Process_Event";
  
  public static final String       SSO_SETTING                                          = "ssoSetting";
  public static final String       SSO_LDAP                                             = "SAML";
  public static final String       SSO_SAML                                             = "LDAP";
  public static final String       AUTHORIZATION_MAPPING                                = "authorizationMapping";
  
  public static final List<String> RELATIONSHIP_TYPES_TO_TRANSFER                       = Arrays
      .asList(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP);
  
  public static final String       AUTH_FAILURE                                         = "authfailure";
  public static final String       USER_WITHOUT_ROLE                                    = "userwithoutrole";
  public static final String       INCORRECT_DATA_SIZE                                  = "incorrectdatasize";
  public static final String       NON_SSO_USER                                         = "nonSSOUser";

  
  /** -------------------------------EXCEL EXPORT------------------------- */
  public static final String       REQUEST_URI                                          = "requestUri";
  
  /**
   * ***************************** SECONDARY TYPES
   * ************************************
   */

  public static final String       CSV_FILE_EXTENTION                                   = ".csv";

  public static final String       CID_PROPERTY                                         = "cid";

  public static final String     TRANSACTION_DATA                                     = "transactionData";
  
  public static final List<String> AUDIT_KLASS_LIST                                     = Arrays
	      .asList(CommonConstants.ENTITY_KLASS, CommonConstants.ARTICLE, CommonConstants.ASSET,
	          CommonConstants.MARKET,
	          CommonConstants.TEXT_ASSET,
	          CommonConstants.SUPPLIER,
	          CommonConstants.TARGET_KLASS_TYPE);

  /************************************
   * SPECIAL USECASE FILTERS
   *******************************/
  public static final String       COLOR_VOILATION_FILTER                               = "colorVoilation";
  public static final String       RED_VOILATION_FILTER                                 = "red";
  public static final String       ORANGE_VOILATION_FILTER                              = "orange";
  public static final String       YELLOW_VOILATION_FILTER                              = "yellow";
  public static final String       GREEN_VOILATION_FILTER                               = "green";
  
  public static final String       CLASS_TAXONOMY_UI_TRANSLATION                        = "CLASS_TAXONOMY";
  public static final String       ASSET_EXPIRY_FILTER                                  = "assetExpiry";
  public static final String       EXPIRED_FILTER                                       = "expired";
  public static final String       NON_EXPIRED_FILTER                                   = "nonExpired";
  public static final String       DUPLICATE_ASSETS_FILTER                              = "duplicateAssets";
  public static final String       DUPLICATE                                            = "duplicate";
  public static final String       ENTITYTYPE                                           = "entityType";
  public static final String       ENTITYATTRIBUTETYPE                                  = "entityAttributeType";
  
  
  /*
   * ------------------------------Audit Log----------------------------------
   */ 
  public static final String AUDIT_LOG_ACIVITY_NUMBER = "ACTIVITY_NUMBER";
  public static final String AUDIT_LOG_USERNAME = "USERNAME";
  public static final String AUDIT_LOG_ACTIVITY = "ACTIVITY";
  public static final String AUDIT_LOG_ENTITY = "ENTITY";
  public static final String AUDIT_LOG_ELEMENT = "ELEMENT";
  public static final String AUDIT_LOG_TYPE = "TYPE";
  public static final String AUDIT_LOG_ELEMENT_NAME = "ELEMENT_NAME";
  public static final String AUDIT_LOG_CODE = "CODE";
  public static final String AUDIT_LOG_TIMESTAMP = "TIMESTAMP";
  public static final String AUDIT_LOG_IP_ADDRESS = "IP_ADDRESS";

  public static final List<String> AUDIT_LOG_UI_CODE_LIST = Arrays.asList("CLASSES", "CONTEXT", "PROPERTIES",
      "PROPERTY_GROUPS_MENU_ITEM_TITLE", "RELATIONSHIPS", "TAXONOMIES", "TEMPLATES", "PARTNERS", "USERS",
      "GOLDEN_RECORD_RULE", "KPI", "RULELIST", "RULES", "LANGUAGE_TREE", "ROLES", "SSO_SETTING", "ARTICLE",
      "ASSET", "SUPPLIER", "MARKET", "TEMPLATES",
      "TEXT_ASSET", "ATTRIBUTE", "TAGS", "PROPERTY_COLLECTION", "TABS_MENU_ITEM_TILE",
      "REFERENCES", "RELATIONSHIPS", "HIERARCHIES_TITLE", "MASTER_TAXONOMY_CONFIGURATION_TITLE",
      "ATTRIBUTE_TYPES_CALCULATED_ATTRIBUTE", "ATTRIBUTE_TYPES_CONCATENATED_ATTRIBUTE", "DATE",
      "ATTRIBUTE_TYPES_HTML", "ATTRIBUTE_TYPES_NUMBER", "ATTRIBUTE_TYPES_PRICE", "ATTRIBUTE_TYPES_TEXT",
      "ATTRIBUTE_TYPES_MEASUREMENT_ATTRIBUTE", "STANDARD_ATTRIBUTES", "TAG_TYPE_BOOLEAN", "TAG_TYPE_YES_NEUTRAL",
      "TAG_TYPE_YES_NEUTRAL_NO", "MASTER", "TAG_TYPE_STATUS", "TAG_TYPE_LIFECYCLE_STATUS",
      "TAG_TYPE_LISTING_STATUS", "CLASSIFICATION", "RANGE", "TAG_TYPE_RULER",
      "STANDARDIZATION_AND_NORMALIZATION", "VIOLATION", "FIXED_BUNDLE", "SET_OF_PRODUCTS",
      "BASE_ARTICLE", "SINGLE_ARTICLE", "MAJOR", "MINOR", "EMBEDDED", "GTIN",
      "NON_NATURE", "NATURE", "IMAGE", "TECHNICAL_IMAGE", "DOCUMENT", "FILE", "VIDEO",
      "TEXT_ASSET", "PRODUCT", "MEDIA",
      "ATTRIBUTE_CONTEXT_VARIANT", "CONTEXT_TYPES_PRODUCT_VARIANT",
      "CONTEXT_TYPES_RELATIONSHIP_VARIANT",
      "PROPERTY_COLLECTION", "X-RAY", "DOCUMENT",
      "MARKET", "TEXT_ASSET", "LDAP", "SAML",
      "CREATED", "UPDATED", "DELETED", CommonConstants.AUDIT_LOG_ACIVITY_NUMBER,
      CommonConstants.AUDIT_LOG_USERNAME, CommonConstants.AUDIT_LOG_ACTIVITY, CommonConstants.AUDIT_LOG_ENTITY,
      CommonConstants.AUDIT_LOG_ELEMENT, CommonConstants.AUDIT_LOG_TYPE, CommonConstants.AUDIT_LOG_ELEMENT_NAME,
      CommonConstants.AUDIT_LOG_CODE, CommonConstants.AUDIT_LOG_TIMESTAMP, CommonConstants.AUDIT_LOG_IP_ADDRESS,
      CommonConstants.CLASSES);

  public static final String ADDED   = "added";
  public static final String DELETED = "deleted";
  public static final String MODIFIED = "modified";

  /******************************* BACKGROUND USER/ROLE *************************************/
  public static final String       BACKGROUND_USER_PEFIX                                = "BackgroundUser_";
  public static final String       BACKGROUND_USER_ID_FOR_STD_ORG                       = "backgrounduserstandardorganization";
  public static final String       BACKGROUND_USER_ID_PEFIX                             = "backgrounduser";
  public static final String       BACKGROUND_USER_LAST_NAME                            = "Background User";
  public static final String       BACKGROUND_USER_PASSWORD                             = "Pass@123$";
  public static final String       BACKGROUND_ROLE_ID_PEFIX                             = "backgroundrole";
  public static final String       BACKGROUND_USER_EMAIL_ID                             = "dummy@dummymail.com";
  public static final String       BACKGROUND_ROLE_PEFIX                                = "BackgroundRole_";
  
  public static final String       USER_TYPE                                            ="com.cs.config.interactor.entity.User";
  public static final String       PROPERTY_SEQUENCE_IDS                      = "propertySequenceIds";

  public static final String       HTML                                                 = "html";
  public static final String       ATTRIBUTE_FORMULA_CODE_SIZE                          = "attributeFormulaCodeSize";

  public static final String       STANDARD_PARENT_CODE                                 = "-1";

  /******************************* RUNTIME CLEANUP *************************************/
  public static final List<String> MANDATORY_ATTRIBUTES_LIST                   = Arrays.asList(
      SystemLevelIds.NAME_ATTRIBUTE, SystemLevelIds.CREATED_BY_ATTRIBUTE,
      SystemLevelIds.CREATED_ON_ATTRIBUTE, SystemLevelIds.LAST_MODIFIED_ATTRIBUTE,
      SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE, SystemLevelIds.ASSET_COVERFLOW_ATTRIBUTE,
      SystemLevelIds.START_DATE_ATTRIBUTE, SystemLevelIds.END_DATE_ATTRIBUTE,
      SystemLevelIds.LAST_MODIFIED_BY_ATTRIBUTE);
  
  public static final List<String> MANDATORY_TAG_TYPE_LIST                     = Arrays
      .asList(STATUS_TAG_TYPE_ID, STATUS_TYPE_ID, LISTING_TYPE_ID);

  public static final String DEFAULT_KLASS_INSTANCE_CATALOG = "diff-"+ IStandardConfig.StandardCatalog.pim.toString();
  
  public static final String       DEFAULT_LABEL                                       = "defaultLabel";
  public static final String       LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY            = "#default#";
  
  public static final String       SYSTEM_MODE_DEVELOPMENT                     = "Development";
  public static final String       SYSTEM_MODE_QA                              = "Qa";
  public static final String       SYSTEM_MODE_PRODUCTION                      = "Production";
  public static final String       SYSTEM_MODE_OFF                             = "Off";
  
  public static final String       INBOUND_MAPPING                                  = "inboundmapping";
  
  public static final String       OUTBOUND_MAPPING                                 = "outboundmapping";

  // Baseentity Metadata constants
  public static final String       SOURCE_ORGANIZATION                    = "sourceOrganization";
}
