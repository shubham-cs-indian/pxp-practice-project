package com.cs.di.workflow.tasks;

public interface ITransformationTask {
  
  public static final String EMBEDDED_VARIANTS                                 = "embeddedVariants";
  public static final String CONTENT_ID                                        = "contentId";
  public static final String VARIANT_ID                                        = "variantId";
  public static final String CONTEXT_ID                                        = "contextId";
  public static final String CONTEXT                                           = "context";
  public static final String CONTEXT_TAGS                                      = "contextTags";
  public static final String TYPE_IDS                                          = "typeIds";
  public static final String TAXONOMY_IDS                                      = "taxonomyIds";
  public static final String PROPERTIES                                        = "properties";
  public static final String ACTIONS                                           = "actions";
  public static final String ADD_ACTION                                        = "add";
  public static final String DELETE_ACTION                                     = "delete";
  public static final String TAXONOMIES                                        = "taxonomies";
  public static final String TYPES                                             = "types";
  public static final String ATTRIBUTES                                        = "attributes";
  public static final String TAGS                                              = "tags";
  public static final String TAG_VALUE_IDS                                     = "tagValueIds";
  public static final String ATTRIBUTE_VARIANTS                                = "attributeVariants";
  public static final String PARENT_ID                                         = "parentId";
  public static final String TIME_RANGE                                        = "timeRange";
  public static final String FROM                                              = "from";
  public static final String TO                                                = "to";
  public static final String FROM_URL                                          = "fromUrl";
  public static final String LANGUAGE_CODE                                     = "languageCode";
  public static final String TYPEIDS                                           = "typeIds";
  public static final String WIDTH                                             = "width";
  public static final String EXTENSION                                         = "extension";
  public static final String FILE_TYPE_EXTENSION                               = "File:FileTypeExtension";
  public static final String HEIGHT                                            = "height";
  public static final String PROPERTY_MAP                                      = "propertyMap";
  public static final String PRIORITY                                          = "priority";
  public static final String FILE_PATH                                         = "filePath";
  public static final String FILE_SOURCE                                       = "fileSource";
  public static final String IMAGE_ASSET                                       = "image_asset";
  public static final String VIDEO_ASSET                                       = "video_asset";
  public static final String DOCUMENT_ASSET                                    = "document_asset";
  public static final String STATUS                                            = "status";
  public static final String LABEL                                             = "label";
  public static final String ID_PROPERTY                                       = "id";
  public static final String EXCEL_FILES                                       = "EXCEL_FILES";
  public static final String JSON_FILES                                        = "JSON_FILES";
  public static final String PXON_FILES                                        = "PXON_FILES";
  public static final String RELATIONSHIPS                                     = "relationships";
  public static final String SIDE1_ID                                          = "side1Id";
  public static final String SIDE2_ID                                          = "side2Id";
  public static final String OPTIONAL                                          = "optional";
  public static final String RELATIONSHIP_ID                                   = "relationshipId";
  public static final String NAME_ATTRIBUTE                                    = "nameattribute";
  public static final String TYPE_PROPERTY                                     = "type";
  public static final String CODE_PROPERTY                                     = "code";
  public static final String LATEST_PRIORITY                                   = "latest";
  public static final String CALCULATED                                        = "CALCULATED";
  public static final String CONCATENATED                                      = "CONCATENATED";
  public static final String LAST_MODIFIED_BY                                  = "lastModifiedBy";
  public static final String LAST_MODIFIED                                     = "lastModified";
  public static final String CREATED_BY                                        = "createdBy";
  public static final String CREATED_ON                                        = "createdOn";
  
  // Sheet names
  public static final String ARTICLE_SHEET_NAME                                = "articles";
  public static final String ASSET_SHEET_NAME                                  = "assets";
  public static final String VARIANT_SHEET_NAME                                = "variants";
  public static final String RELATIONSHIP_SHEET_NAME                           = "relationships";
  public static final String REFERENCE_SHEET_NAME                              = "Reference";
  public static final String PARTNER_SHEET_NAME                                = "Partner";
  public static final String LANGUAGE_SHEET_NAME                               = "Language";
  
  public static final String MAIN_PERMISSIONS_SHEET_NAME                       = "MainPermissions";
  public static final String GENERALI_NFORMATION_PERMISSIONS_SHEET_NAME        = "GeneralInformation";
  public static final String PROPERTY_PERMISSIONS_SHEET_NAME                   = "Property";
  public static final String RELATIONSHIP_PERMISSIONS_SHEET_NAME               = "Relationship";
  
  // column names
  public static final String KLASS_COLUMN                                      = "klass";
  public static final String ID_COLUMN                                         = "id";
  public static final String TAXONOMIES_COLUMN                                 = "taxonomies";
  public static final String ASSIGN_TAXONOMIES_COLUMN                          = "assign_taxonomies";
  public static final String DELETE_TAXONOMIES_COLUMN                          = "delete_taxonomies";
  public static final String SECONDARY_KLASSES_COLUMN                          = "secondary_klasses";
  public static final String ASSIGN_NONNATUREKLASS_COLUMN                      = "assign_secondary_klasses";
  public static final String DELETE_NONNATUREKLASS_COLUMN                      = "delete_secondary_klasses";
  public static final String FILE_SOURCE_COLUMN                                = "file_source";
  public static final String FILE_PATH_COLUMN                                  = "file_path";
  public static final String RELATIONSHIP_CODES_COLUMN                         = "relationship_codes";
  public static final String SIDE1_ID_COLUMN                                   = "side1_id";
  public static final String SIDE2_ID_COLUMN                                   = "side2_id";
  public static final String CONTEXT_ID_COLUMN                                 = "context_id";
  public static final String FROM_DATE_COLUMN                                  = "from_date";
  public static final String TO_DATE_COLUMN                                    = "to_date";
  public static final String MASTER_ID_COLUMN                                  = "master_id";
  public static final String IS_ATTRIBUTE_VARIANT_COLUMN                       = "is_attribute_variant";
  public static final String ATTRIBUTE                                         = "ATTRIBUTE";
  public static final String TAG                                               = "TAG";
  
  public static final String VARIANT_TYPE_COLUMN                               = "variant_type";
  public static final String PARENT_ID_COLUMN                                  = "parent_id";
  
  public static final String IS_NATURE_RELATIONSHIP_COLUMN                     = "is_nature_relationship";
  public static final String LANGUAGE_CODE_COLUMN                              = "language_code";
  
  public static final String FILE_DELETE_LOGGER                                = "File deleted successfully";
  public static final String FILE_DELETE_FAILED_LOGGER                         = "Failed to delete the file";
  public static final String CONTEXT_TAG_PREFIX                                = "context_tag_";
  public static final String LAST_MODIFIED_BY_COLUMN                           = "last_modified_by";
  public static final String LAST_MODIFIED_COLUMN                              = "last_modified";
  public static final String CREATED_BY_COLUMN                                 = "created_by";
  public static final String CREATED_ON_COLUMN                                 = "created_on";
  
  public static final String CODE                                              = "CODE";
  public static final String TYPE                                              = "TYPE";
  public static final String SUB_TYPE                                          = "SUB_TYPE";
  public static final String NAME                                              = "NAME";
  public static final String ALLOWED_STYLES                                    = "ALLOWED_STYLES";
  public static final String AVAILABILITY                                      = "AVAILABILITY";
  public static final String DEFAULT_UNIT                                      = "DEFAULT_UNIT";
  public static final String DEFAULT_VALUE                                     = "DEFAULT_VALUE";
  public static final String DEFAULT_VALUE_AS_HTML                             = "DEFAULT_VALUE_AS_HTML";
  public static final String DESCRIPTION                                       = "DESCRIPTION";
  public static final String FORMULA                                           = "FORMULA";
  public static final String DISABLED                                          = "DISABLED";
  public static final String FILTERABLE                                        = "FILTERABLE";
  public static final String GRID_EDITABLE                                     = "GRID_EDITABLE";
  public static final String READ_ONLY_COLUMN                                  = "READ_ONLY";
  public static final String TYPE_OF_PROPERTY_CONCATENATED                     = "TYPE_OF_PROPERTY_CONCATENATED";
  public static final String ATTRIBUTE_CODE_OF_PROPERTY_CONCATENATED           = "ATTRIBUTE_CODE_OF_PROPERTY_CONCATENATED";
  public static final String VALUE_OF_PROPERTY_CONCATENATED                    = "VALUE_OF_PROPERTY_CONCATENATED";
  public static final String CALCULATED_ATTRIBUTE_TYPE                         = "CALCULATED_ATTRIBUTE_TYPE";
  public static final String CONCATENATED_ATTRIBUTE_TYPE                       = "CONCATENATED_ATTRIBUTE_TYPE";
  public static final String CALCULATED_ATTRIBUTE_UNIT                         = "CALCULATED_ATTRIBUTE_UNIT";
  public static final String TYPE_OF_PROPERTY_CALCULATED                       = "TYPE_OF_PROPERTY_CALCULATED";
  public static final String ATTRIBUTE_CODE_OF_PROPERTY_CALCULATED             = "ATTRIBUTE_CODE_OF_PROPERTY_CALCULATED";
  public static final String VALUE_OF_PROPERTY_CALCULATED                      = "VALUE_OF_PROPERTY_CALCULATED";
  public static final String READONLY                                          = "READONLY";
  public static final String SHOWCODETAG                                       = "SHOWCODETAG";
  public static final String MANDATORY                                         = "MANDATORY";
  public static final String SEARCHABLE                                        = "SEARCHABLE";
  public static final String SORTABLE                                          = "SORTABLE";
  public static final String STANDARD                                          = "STANDARD";
  public static final String TRANSLATABLE                                      = "TRANSLATABLE";
  public static final String REVISIONABLE                                      = "REVISIONABLE";
  public static final String PLACEHOLDER                                       = "PLACEHOLDER";
  public static final String PRECISION                                         = "PRECISION";
  public static final String TOOLTIP                                           = "TOOLTIP";
  public static final String ACTION                                            = "ACTION";
  public static final String  HIDE_SEPARATOR                                   = "HIDE_SEPARATOR";
  // Column names for Taxonomy config
  public static final String ID_COLUMN_CONFIG                                  = "Id";
  public static final String CODE_COLUMN_CONFIG                                = "Code";
  public static final String LABEL_COLUMN_CONFIG                               = "Label";
  public static final String PARENT_CODE_COLUMN                                = "Parent_Code";
  public static final String TYPE_COLUMN                                       = "Type";
  public static final String LEVELS                                            = "Levels";
  public static final String LEVEL_CODE                                        = "Level_Code";
  public static final String LEVEL_CODES_COLUMN                                = "Level_Codes";
  public static final String LEVEL_LABEL_COLUMN                                = "Level_Label";
  public static final String LEVEL_INDEX_COLUMN                                = "Level_Index";
  public static final String MASTER_TAG_PARENT_CODE_COLUMN                     = "MasterTagParentCode";
  public static final String EVENTS_COLUMN                                     = "Events";
  public static final String TASK_COLUMN                                       = "Task";
  public static final String PROPERTY_COLLECTION_COLUMN                        = "Property_Collection";
  public static final String EMBEDDED_CLASS_COLUMN                             = "Embedded_Class";
  public static final String IS_NEWLY_CREATED_LEVEL_COLUMN                     = "IsNewlyCreatedLevel";
  public static final String MASTER_TAG_PARENT_CODE_COLUMN_2                   = "Master_Tag_Parent_Code";
  
  // Column names for property Collection config
  public static final String PROPERTY_CODE_COLUMN                              = "Property_Code";
  public static final String PROPERTY_TYPE_COLUMN                              = "Property_Type";
  public static final String ALLOWED_VALUES_COLUMN                             = "Allowed_Values";
  public static final String PRECISION_COLUMN                                  = "Precision";
  public static final String DEFAULT_VALUE_COLUMN                              = "Default_Value";
  public static final String COUPLING_COLUMN                                   = "Coupling";
  public static final String MANDATORY_COLUMN                                  = "Mandatory";
  public static final String SKIPPED_COLUMN                                    = "Skipped";
  public static final String MULTISELECT_COLUMN                                = "Multiselect";
  public static final String ATTRIBUTE_CONTEXT_COLUMN                          = "Attribute_Context";
  public static final String PREFIX_COLUMN                                     = "Prefix";
  public static final String SUFFIX_COLUMN                                     = "Suffix";
  public static final String PRODUCT_IDENTIFIER_COLUMN                         = "Product_Identifier";
  public static final String LANGUAGE_DEPENDENT_COLUMN                         = "LanguageDependent";
  public static final String REVISIONABLE_COLUMN                               = "Revisionable";
  public static final String HIDE_SEPARATOR_COLUMNN                            = "Hide Separator";
  // column name for tag
  public static final String PARENT_CODE                                       = "PARENT_CODE";
  public static final String COLOR                                             = "COLOR";
  public static final String LINKED_MASTER_TAG_CODE                            = "LINKED_MASTER_TAG_CODE";
  public static final String MULTISELECT                                       = "MULTISELECT";
  public static final String ICON                                              = "ICON";
  public static final String IMAGE_EXTENSION                                   = "IMAGE_EXTENSION";
  public static final String IMAGE_RESOLUTION                                  = "IMAGE_RESOLUTION";
  
  // column names for property collection
  public static final String TAB_COLUMN                                        = "Tab";
  public static final String ENTITY_CODE_COLUMN                                = "EntityCode";
  public static final String ENTITY_TYPE_COLUMN                                = "EntityType";
  public static final String IS_FOR_XRAY_COLUMN                                = "isForXRay";
  public static final String IS_DEFAULT_FOR_XRAY_COLUMN                        = "Default_for_X_RAY";
  public static final String IS_STANDARD_COLUMN                                = "Standard";
  public static final String INDEX_COLUMN                                      = "Index";
  public static final String INDEX_PROPERTY                                    = "index";
  
  // column names for context
  public static final String IS_TIME_ENABLED                                   = "EnableTime";
  public static final String ENABLE_TIME_FROM                                  = "EnableTimeFrom";
  public static final String ENABLE_TIME_TO                                    = "EnableTimeTo";
  public static final String USE_CURRENT_TIME                                  = "CurrentTime";
  public static final String ALLOW_DUPLICATES                                  = "AllowDuplicates";
  public static final String SELECTED_TAG                                      = "Selected_Tag";
  public static final String SELECTED_TAGS                                     = "Selected_Tags";
  public static final String SELECTED_TAG_VALUES                               = "Selected_Tag_Values";
  public static final String ENTITIES                                          = "Entities";
  public static final String ARTICLE_INSTANCE                                  = "ArticleInstance";
  public static final String ASSET_INSTANCE                                    = "AssetInstance";
  public static final String MARKET_INSTANCE                                   = "MarketInstance";
  public static final String TEXT_ASSET_INSTANCE                               = "TextAssetInstance";
  public static final String SUPPLIER_INSTANCE                                 = "SupplierInstance";
  public static final String VIRTUAL_CATALOG_INSTANCE                          = "VirtualCatalogInstance";
  public static final String ARTICLE_KEY                                       = "article";
  public static final String ASSET_KEY                                         = "asset";
  public static final String MARKET_KEY                                        = "market";
  public static final String TEXT_ASSET_KEY                                    = "textAsset";
  public static final String SUPPLIER_KEY                                      = "supplier";
  public static final String VIRTUAL_CATALOG_KEY                               = "virtualCatalog";
  public static final String TASK_KEY                                          = "task";
  public static final String ATTRIBUTE_VARIANT_CONTEXT                         = "attributeVariantContext";
  public static final String RELATIONSHIP_VARIANT_CONTEXT                      = "relationshipVariant";
  public static final String PRICE_CONTEXT                                     = "priceContext";
  public static final String PRODUCT_VARIANT_CONTEXT                           = "productVariant";
  
  // Column names for user import
  public static final String USERNAME                                          = "USERNAME";
  public static final String PASSWORD                                          = "PASSWORD";
  public static final String FIRSTNAME                                         = "FIRSTNAME";
  public static final String LASTNAME                                          = "LASTNAME";
  public static final String GENDER                                            = "GENDER";
  public static final String EMAIL                                             = "EMAIL";
  public static final String CONTACT                                           = "CONTACT";
  public static final String EMAIL_LOG_FILE                                    = "EMAIL_LOG_FILE";
  public static final String DATA_LANGUAGE                                      = "DATALANGUAGE";
  public static final String UI_LANGUAGE                                        = "UILANGUAGE";
  
  
  public static final String ICON_COLUMN                                       = "Icon";
  public static final String PRIORITY_TAG_COLUMN                               = "PriorityTag";
  public static final String STATUS_TAG_COLUMN                                 = "StatusTag";
  public static final String COLOR_COLUMN                                      = "Color";
  
  // Column names for Relationship
  public static final String LABEL_COLUMN                                      = "LABEL";
  public static final String TAB_CODE_COLUMN                                   = "TAB_CODE";
  public static final String ALLOW_AFTERSAVE_EVENT                             = "ALLOW_AFTERSAVE_EVENT_TO_BE_TRIGGERED_FOR_CHANGES_IN_DATA_TRANSFER_CONFIGURATION";
  public static final String IS_LITE                                           = "IS_LITE";
  
  public static final String S1_CLASS_TYPE                                     = "S1_CLASS_TYPE";
  public static final String S1_CLASS_CODE                                     = "S1_CLASS_CODE";
  public static final String S1_LABEL                                          = "S1_LABEL";
  public static final String S1_CARDINALITY                                    = "S1_CARDINALITY";
  public static final String S1_EDITABLE                                       = "S1_EDITABLE";
  public static final String S1_CONTEXT_CODE                                   = "S1_CONTEXT_CODE";
  public static final String S1_PROPERTY_TYPE                                  = "S1_PROPERTY_TYPE";
  public static final String S1_PROPERTY_CODE                                  = "S1_PROPERTY_CODE";
  public static final String S1_COUPLING_TYPE                                  = "S1_COUPLING_TYPE";
  
  public static final String S2_CLASS_TYPE                                     = "S2_CLASS_TYPE";
  public static final String S2_CLASS_CODE                                     = "S2_CLASS_CODE";
  public static final String S2_LABEL                                          = "S2_LABEL";
  public static final String S2_CARDINALITY                                    = "S2_CARDINALITY";
  public static final String S2_EDITABLE                                       = "S2_EDITABLE";
  public static final String S2_CONTEXT_CODE                                   = "S2_CONTEXT_CODE";
  public static final String S2_PROPERTY_TYPE                                  = "S2_PROPERTY_TYPE";
  public static final String S2_PROPERTY_CODE                                  = "S2_PROPERTY_CODE";
  public static final String S2_COUPLING_TYPE                                  = "S2_COUPLING_TYPE";
  
//JSON Key names for Relationship
  public static final String CODE_KEY                                          = "Code";
  public static final String LABEL_KEY                                         = "Label";
  public static final String ICON_KEY                                          = "Icon";
  public static final String TAB_CODE_KEY                                      = "Tab_Code";
  public static final String ALLOW_AFTERSAVE_EVENT_KEY                         = "Allow_AfterSave_Event_To_Be_Triggered_For_Changes_In_Data_Transfer_Configuration";
  public static final String IS_LITE_KEY                                       = "isLite";
  
  public static final String S1_CLASS_TYPE_KEY                                 = "S1_Class_Type";
  public static final String S1_CLASS_CODE_KEY                                 = "S1_Class_Code";
  public static final String S1_LABEL_KEY                                      = "S1_Label";
  public static final String S1_CARDINALITY_KEY                                = "S1_Cardinality";
  public static final String S1_EDITABLE_KEY                                   = "S1_Editable";
  public static final String S1_CONTEXT_CODE_KEY                               = "S1_Context_Code";
  public static final String S1_PROPERTY_TYPE_KEY                              = "S1_Property_Type";
  public static final String S1_PROPERTY_CODE_KEY                              = "S1_Property_code";
  public static final String S1_COUPLING_TYPE_KEY                              = "S1_Coupling_Type";
  
  public static final String S2_CLASS_TYPE_KEY                                 = "S2_Class_Type";
  public static final String S2_CLASS_CODE_KEY                                 = "S2_Class_Code";
  public static final String S2_LABEL_KEY                                      = "S2_Label";
  public static final String S2_CARDINALITY_KEY                                = "S2_Cardinality";
  public static final String S2_EDITABLE_KEY                                   = "S2_Editable";
  public static final String S2_CONTEXT_CODE_KEY                               = "S2_Context_Code";
  public static final String S2_PROPERTY_TYPE_KEY                              = "S2_Property_Type";
  public static final String S2_PROPERTY_CODE_KEY                              = "S2_Property_Code";
  public static final String S2_COUPLING_TYPE_KEY                              = "S2_Coupling_Type";
  
  // column name for klass
  public static final String IS_NATURE                                         = "Is_Nature";
  public static final String NATURE_TYPE                                       = "Nature_Type";
  public static final String MAX_VERSION                                       = "Max_version";
  public static final String IS_ABSTRACT                                       = "Is_Abstract";
  public static final String LIFE_CYCLE_STATUS_TAG                             = "Life_Cycle_Status_Tag";
  public static final String IS_DEFAULT                                        = "Is_Default";
  public static final String EVENT_CODES                                       = "Event_Codes";
  public static final String TASK_CODES                                        = "Task_Codes";
  public static final String SECTION_SHEET_NAME                                = "Sections_Sheet_Name";
  public static final String VARIANT_SHEET_NAME_FOR_KLASS                      = "Variant_Sheet_Name";
  public static final String RELATIONSHIP_SHEET_NAME_FOR_KLASS                 = "Relationships_Sheet_Name";
  public static final String PROPERTY_COLLECTION_CODE                          = "Property_Collection_Code";
  public static final String PROPERTY_TYPE                                     = "Property_Type";
  public static final String PROPERTY_CODE                                     = "Property_Code";
  public static final String IS_INHERTIED                                      = "Inherited";
  public static final String COUPLING                                          = "Coupling";
  public static final String CLASS_TYPE                                        = "Class_Type";
  public static final String CLASS_CODE                                        = "Class_Code";
  public static final String RELATIONSHIP_TYPE                                 = "Relationship_Type";
  public static final String SIDE2_CLASS_CODE                                  = "Side2_Class_Code";
  public static final String TAB_CODE                                          = "Tab_Code";
  public static final String CONTEXT_CODE                                      = "Context_Code";
  public static final String MAXNOOFVERSION                                    = "MaxNoOfVersion";
  public static final String TRACKDOWNLOAD                                     = "Track_Download";
  
  public static final String ATTRIBUTES_SIDE1                                  = "Attributes_Side1";
  public static final String ATTRIBUTESCOUPLING_SIDE1                          = "AttributesCoupling_Side1";
  public static final String TAGS_SIDE1                                        = "Tags_Side1";
  public static final String TAGSCOUPLING_SIDE1                                = "TagsCoupling_Side1";
  public static final String ATTRIBUTES_SIDE2                                  = "Attributes_Side2";
  public static final String ATTRIBUTESCOUPLING_SIDE2                          = "AttributesCoupling_Side2";
  public static final String TAGS_SIDE2                                        = "Tags_Side2";
  public static final String TAGSCOUPLING_SIDE2                                = "TagsCoupling_Side2";
  public static final String PRODUCTVARIANTRELATIONSIP                         = "productVariantRelationship";
  public static final String PROMOTIONALVERSIONCONTEXTCODE                     = "promotionalVersionContextCode";
  
  // For Config Export from PXON to EXCEL
  public static final String RELATIONSHIP_FILTER                               = "Relationship_Filter";
  public static final String ATTRIBUTE_FILTER                                  = "Attribute_Filter";
  public static final String TAG_FILTER                                        = "Tag_Filter";
  public static final String PREVIEW_IMAGE                                     = "Preview_Image";
  public static final String TECHNICAL_IMAGE_CLASSES                           = "Technical_Image_Classes";
  public static final String DETECT_DUPLICATE                                  = "Detect_Duplicate";
  public static final String EXTRACT_ZIP                                       = "Extract_Zip";
  public static final String EXTRACT_METADATA                                  = "Extract_Metadata";
  public static final String EXTRACT_RENDITIONS                                = "Extract_Renditions";
  public static final String AUTOCREATESETTING                                 = "AutoCreateSetting";
  public static final String RHYTHM                                            = "Rhythm";
  public static final String CONTEXTTAGS_CODES                                 = "ContextTags_Codes";
  public static final String RELATIONSHIPCODES_TO_INHERIT                      = "Relationshipcodes_To_Inherit";
  public static final String RELATIONSHIP_INHERITANCE_COUPLING                 = "Relationship_Inheritance_Coupling";
  public static final String TAXONOMY_INHERITANCE                              = "Taxonomy_Inheritance";
  public static final String ALLOW_AFTERSAVE_EVENT_TO_BE_TRIGGERED_FOR_CHANGES = "Allow_AfterSave_event_to_be_triggered_for_Changes";
  public static final String ATTRIBUTES_COUPLING                               = "attributesCoupling";
  public static final String TAGS_COUPLING                                     = "tagsCoupling";
  public static final String COUPLING_TYPE                                     = "couplingType";
  public static final String EMBEDDED                                          = "embedded";
  
  // column names for dataRules
  public static final String LANGUAGES                                         = "Languages";
  public static final String PARTNERS                                          = "Partners";
  public static final String PHYSICALCATALOGS                                  = "PhysicalCatalog";
  public static final String CAUSE_ENTITY_CODE                                 = "Cause_EntityCode";
  public static final String CAUSE_ENTITY_TYPE                                 = "Cause_EntityType";
  public static final String CAUSE_ENTITY_SUBTYPE                              = "Cause_EntitySubType";
  public static final String CAUSE_COMPARE_TYPE                                = "Cause_CompareType";
  public static final String CAUSE_ENTITY_VALUE_TYPE                           = "Cause_EntityValueType";
  public static final String CAUSE_ENTITY_VALUE                                = "Cause_EntityValue";
  public static final String CAUSE_FROM                                        = "Cause_From";
  public static final String CAUSE_TO                                          = "Cause_To";
  public static final String CAUSE_COMPARE_WITH_CURRENT_DATE                   = "Cause_CompareWithCurrentDate";
  public static final String CAUSE_KLASSES                                     = "Cause_Klasses";
  public static final String CAUSE_TAXONOMIES                                  = "Cause_Taxonomies";
  public static final String EFFECT_ENTITY_CODE                                = "Effect_EntityCode";
  public static final String EFFECT_ENTITY_TYPE                                = "Effect_EntityType";
  public static final String EFFECT_ENTITY_SUBTYPE                             = "Effect_EntitySubType";
  public static final String SAN_EFFECT_TRANSFORMATION_TYPE                    = "SAN_Effect_TransformationType";
  public static final String SAN_EFFECT_ENTITY_VALUE                           = "SAN_Effect_EntityValue";
  public static final String SAN_EFFECT_ENTITY_SPECIAL_VALUE                   = "SAN_Effect_EntitySpecialValue";
  public static final String SAN_EFFECT_FROM                                   = "SAN_Effect_From";
  public static final String SAN_EFFECT_TO                                     = "SAN_Effect_To";
  public static final String SAN_EFFECT_KLASSES                                = "SAN_Effect_Klasses";
  public static final String SAN_EFFECT_TAXONOMIES                             = "SAN_Effect_Taxonomies";
  public static final String VIOLATION_EFFECT_COLOR                            = "Violation_Effect_Color";
  public static final String VIOLATION_EFFECT_DESCRIPTION                      = "Violation_Effect_Description";
  public static final String CLASSIFICATION_CAUSE_KLASSES                      = "Classification_Cause_Klasses";
  public static final String CLASSIFICATION_CAUSE_TAXONOMIES                   = "Classification_Cause_Taxonomies";
  public static final String CLASSIFICATION_EFFECT_KLASSES                     = "Classification_Effect_Klasses";
  public static final String CLASSIFICATION_EFFECT_TAXONOMIES                  = "Classification_Effect_Taxonomies";
  
  // Sheets name for config transformation
  public static final String CONTEXT_SHEET_NAME                                = "Context";
  public static final String KLASS_SHEET_NAME                                  = "Klass";
  public static final String ATTRIBUTE_SHEET_NAME                              = "Attribute";
  public static final String HIERARCHY_SHEET_NAME                              = "TaxonomyHierarchy";
  public static final String USER_SHEET_NAME                                   = "User";
  public static final String TASK_SHEET_NAME                                   = "Task";
  public static final String MASTER_TAXONOMY_SHEET_NAME                        = "MasterTaxonomy";
  public static final String RELATIONSHIP_SHEET_NAME_FOR_CONFIG                = "Relationships";
  public static final String TAG_SHEET_NAME                                    = "Tags";
  public static final String PROPERTY_COLLECTION_SHEET_NAME                    = "PropertyCollection";
  public static final String DATA_RULES_SHEET_NAME                             = "Rule";
  public static final String GOLDEN_RECORD_RULES_SHEET_NAME                    = "GoldenRecordRule";
  
  // column names for Golden Record Rules
  public static final String MATCH_ATTRIBUTES                                  = "Match_Attributes";
  public static final String MATCH_TAGS                                        = "Match_Tags";
  public static final String MATCH_NON_NATURE_CLASSES                          = "Match_NonNatureClasses";
  public static final String MATCH_TAXONOMIES                                  = "Match_Taxonomies";
  public static final String AUTOCREATE                                        = "AutoCreate";
  public static final String MERGE_ATTRIBUTES                                  = "Merge_Attributes";
  public static final String MERGE_ATTRIBUTES_LATEST                           = "Merge_Attributes_Latest";
  public static final String MERGE_ATTRIBUTES_SUPPLIERS                        = "Merge_Attributes_Suppliers";
  public static final String MERGE_TAGS                                        = "Merge_Tags";
  public static final String MERGE_TAGS_LATEST                                 = "Merge_Tags_Latest";
  public static final String MERGE_TAGS_SUPPLIERS                              = "Merge_Tags_Suppliers";
  public static final String MERGE_RELATIONSHIPS                               = "Merge_Relationships";
  public static final String MERGE_RELATIONSHIPS_SUPPLIERS                     = "Merge_Relationships_Suppliers";
  public static final String MERGE_NATURE_RELATIONSHIPS                        = "Merge_NatureRelationships";
  public static final String MERGE_NATURE_RELATIONSHIPS_SUPPLIERS              = "Merge_NatureRelationships_Suppliers";
  
  // column names for config import of tabs
  public static final String SEQUENCE                                          = "Sequence";
  public static final String IS_STANDARD                                       = "IsStandard";
  public static final String S1_CLASS_TYPE_COLUMN                              = "S1_ClassType";
  public static final String S1_CLASS_CODE_COlUMN                              = "S1_ClassCode";
  public static final String S1_CLASS_LABEL_COlUMN                             = "S1_Label";
  public static final String S1_CLASS_CARDINALITY_COlUMN                       = "S1_Cardinality";
  public static final String S2_CLASS_TYPE_COLUMN                              = "S2_ClassType";
  public static final String S2_CLASS_CODE_COLUMN                              = "S2_ClassCode";
  public static final String S2_CLASS_LABEL_COLUMN                             = "S2_Label";
  public static final String S2_CLASS_CARDINALITY_COLUMN                       = "S2_Cardinality";
  public static final String TAB_SHEET_NAME                                    = "Tab";
  
  public static final String TYPE_SAN                                          = "standardization_and_normalization";
  public static final String TYPE_VIO                                         = "violation";
  public static final String TYPE_CLASSIFICATION                               = "classification";
  public static final String KLASS_TYPE                                        = "type";
  public static final String VIOLATION_EFFECT_DEFAULT_COLOR                    = "red";
  public static final String VIOLATION_EFFECT_DEFAULT_DESCRIPTION              = "";
  public static final String VALUE                                             = "value";
  public static final String ORDER                                             = "order";
  public static final String ATTRIBUTE_ID                                      = "attributeId";
  public static final String TRANSFORMATION_TYPE_REPLACE                       = "replace";
  public static final String TRANSFORMATION_TYPE_UPPERCASE                     = "uppercase";
  public static final String TRANSFORMATION_TYPE_LOWERCASE                     = "lowercase";
  public static final String TRANSFORMATION_TYPE_TRIM                          = "trim";
  public static final String TRANSFORMATION_TYPE_PROPERCASE                    = "propercase";
  public static final String TRANSFORMATION_TYPE_ATTRIBUTEVALUE                = "attributeValue";
  public static final String TRANSFORMATION_TYPE_SUBSTRING                     = "substring";
  public static final String TRANSFORMATION_TYPE_VALUE                         = "value";
  public static final String TRANSFORMATION_TYPE_VALUE_HTML                    = "HTML";
  public static final String TRANSFORMATION_TYPE_VALUE_HTML_PARAGRAPH_START    = "<p>";
  public static final String TRANSFORMATION_TYPE_VALUE_HTML_PARAGRAPH_END      = "</p>";
  public static final String TRANSFORMATION_TYPE_VALUE_HTML_NON_BREAKING_SPACE = "&nbsp;";
  public static final String TRANSFORMATION_TYPE_VALUE_DATE                    = "DATE";
  public static final String TRANSFORMATION_TYPE_CONCAT                        = "concat";
  public static final String ENTITY_SPECIAL_VALUE_HTML                         = "html";
  public static final String ENTITY_TAG                                        = "tag";
  public static final String TAG_TYPE_BOOL                                     = "tag_type_boolean";
  public static final String ENTITY_ATTRIBUTE                                  = "attribute";
  public static final String ONE                                               = "1";
  public static final String COMPARE_TYPE_EXACT                                = "exact";
  public static final String COMPARE_TYPE_CONTAINS                             = "contains";
  public static final String COMPARE_TYPE_START                                = "start";
  public static final String COMPARE_TYPE_END                                  = "end";
  public static final String COMPARE_TYPE_LT                                   = "length_lt";
  public static final String COMPARE_TYPE_GT                                   = "length_gt";
  public static final String COMPARE_TYPE_EQUAL                                = "length_equal";
  public static final String COMPARE_TYPE_RANGE                                = "length_range";
  public static final String COMPARE_TYPE_EMPTY                                = "empty";
  public static final String COMPARE_TYPE_NOT_EMPTY                            = "notempty";
  public static final String COMPARE_TYPE_REGEX                                = "regex";
  public static final String COMPARE_TYPE_IS_DUPLICATE                         = "isduplicate";
  public static final String COMPARE_TYPE_DATE_LT                              = "lt";
  public static final String COMPARE_TYPE_DATE_GT                              = "gt";
  public static final String COMPARE_TYPE_DATE_RANGE                           = "range";
  public static final String TAXONOMY                                          = "taxonomy";
  public static final String ENTITY_ID                                         = "entityId";
  public static final String ENTITY_TYPE                                       = "type";
  public static final String VIO_COLOR                                         = "color";
  public static final String VIO_DESCRIPTION                                   = "description";
  public static final String VERSION_ID                                        = "versionId";
  public static final String DATE_ATTRIBUTE_TYPE                               = "DATE_ATTRIBUTE_TYPE";
  
  // column names for config import of partners
  public static final String PHYSICAL_CATALOG                                  = "Physical_Catalog";
  public static final String PORTALS                                           = "Portals";
  public static final String TAXONOMY_CODE                                     = "Taxonomy_Code";
  public static final String TARGET_CLASS_CODE                                 = "Target_Class_Code";
  public static final String SYSTEM_CODE                                       = "System_Code";
  public static final String ENDPOINT_CODE                                     = "Endpoint_Code";
  public static final String ROLE                                              = "Role";
  public static final String ROLES                                             = "Roles";
  public static final String ROLE_CODE                                         = "Role_Code";
  public static final String ROLE_LABEL                                        = "Role_Label";
  public static final String ROLE_TYPE                                         = "Role_Type";
  public static final String ROLE_PHYSICAL_CATALOG                             = "Role_Physical_Catalog";
  public static final String ROLE_PORTALS                                      = "Role_Portals";
  public static final String ROLE_TAXONOMY_CODE                                = "Role_Taxonomy_Code";
  public static final String ROLE_TARGET_CLASS_CODE                            = "Role_Target_Class_Code";
  public static final String ROLE_USERS                                        = "Role_Users";
  public static final String ROLE_ENABLED_DASHBOARD                            = "Role_Enable_Dashboard";
  public static final String ROLE_KPI                                          = "Role_KPI";
  public static final String ROLE_ENTITIES                                     = "Role_Entities";
  public static final String ROLE_SYSTEM_CODE                                  = "Role_System_Code";
  public static final String ROLE_ENDPOINT_CODE                                = "Role_Endpoint_Code";
  public static final String ROLE_READONLY_USER                                = "Role_Read_only_User";
  
  // column names for config import of relationships
  public static final String DESCRIPTION_COLUMN_CONFIG                         = "Description";
  public static final String PLACEHOLDER_COLUMN_CONFIG                         = "PlaceHolder";
  public static final String TOOLTIP_COLUMN_CONFIG                             = "Tooltip";
  public static final String SIDE1_LABEL                                       = "Side1Label";
  public static final String SIDE2_LABEL                                       = "Side2Label";
  public static final String DESCRIPTION_VAL                                   = "description";
  public static final String PLACEHOLDER_VAL                                   = "placeholder";
  public static final String TOOLTIP_VAL                                       = "tooltip";
  public static final String SIDE1_LABEL_VAL                                   = "side1Label";
  public static final String SIDE2_LABEL_VAL                                   = "side2Label";
  
  // column names for config import of Languages
  public static final String PARENTCODE                                        = "PARENTCODE";
  public static final String ABBREVIATION                                      = "ABBREVIATION";
  public static final String LOCALE_ID                                         = "LOCALE_ID";
  public static final String NUMBER_FORMAT                                     = "NUMBER_FORMAT";
  public static final String DATE_FORMAT                                       = "DATE_FORMAT";
  public static final String IS_DATA_LANGUAGE                                  = "IS_DATA_LANGUAGE";
  public static final String IS_UI_LANGUAGE                                    = "IS_UI_LANGUAGE";
  public static final String IS_DEFULT_LANGUAGE                                = "IS_DEFULT_LANGUAGE";
  public static final String TRUE                                              = "1";
  public static final String FALSE                                             = "0";
  public static final String ABBREVIATION_CONFIG                               = "Abbreviation";
  public static final String LOCALE_ID_CONFIG                                  = "Locale_Id";
  public static final String NUMBER_FORMAT_CONFIG                              = "Number_Format";
  public static final String DATE_FORMAT_CONFIG                                = "Date_Format";
  public static final String IS_DATA_LANGUAGE_CONFIG                           = "Is_Data_Language";
  public static final String IS_UI_LANGUAGE_CONFIG                             = "Is_UI_Language";
  public static final String IS_DEFULT_LANGUAGE_CONFIG                         = "Is_Default_Language";
  
  // column names for config import of Languages
  public static final String ENTITY_CODE                                       = "Entity_Code";
  public static final String PLACEHOLDER_CONFIG                                = "Placeholder";

  //Taxonomy/Non Nature Class Action
  public static final String ASSIGN                                            = "ASSIGN";
  public static final String DELETE                                            = "DELETE";
  public static final String REPLACE                                           = "REPLACE";
  public static final String CREATE                                            = "CREATE";
  public static final String UPDATE                                            = "UPDATE";
  public static final String DEFAULT_ACTION                                    = "UPSERT";
  
  // Default tags for Article base type
  public static final String LISTING_STATUS_CATLOG                             = "listing_status_catlog";
  public static final String LIFE_STATUS_INBOX                                 = "life_status_inbox";
  public static final String LISTINGSTATUSTAG                                  = "listingstatustag";
  public static final String LIFESTATUSTAG                                     = "lifestatustag";
  
  //data rules json key
  public static final String CALCULATED_ATTRIBUTE_UNIT_AS_HTML                 = "CalculatedAttributeUnitAsHTML";
  public static final String COUPLINGS                                         = "Couplings";
  public static final String ATTRIBUTE_OPERATOT_LIST                           = "AttributeOperatorList";
  public static final String VALUE_AS_HTML                                     = "ValueAsHTML";
  public static final String VALUES                                            = "Values";
  public static final String CALUCLUATED_ATTRIBUTE_UNIT                        = "CalculatedAttributeUnit";
  public static final String TRANSFORMATION_TYPE                               = "TransformationType";
  public static final String TAG_VALUES                                        = "TagValues";
  public static final String BASE_TYPE                                         = "BaseType";
  public static final String MERGE_EFFECTS                                     = "Merge_Effects";
  public static final String MERGE_ATTRIBUTE_ENTITY_TYPE                       = "Merge_Attribute_Entity_Type";
  public static final String MERGE_RELATIONSHIP_ENTITY_TYPE                    = "Merge_Relationship_Entity_Type";
  public static final String MERGE_NON_NATURERELATIONSHIP_ENTITY_TYPE          = "Merge_NonNatureRelationship_Entity_Type";
  public static final String MERGE_TAG_ENTITY_TYPE                             = "Merge_Tag_Entity_Type";
  public static final String NON_NATURERELATIONSHIP                            = "Non_NatureRelationship";
  public static final String NORMALISATION                                     = "Normalisation";
  public static final String RULE_VIOLATION                                    = "Rule_Violation";
  public static final String KLASS_LINK_IDS                                    = "Klasslinkids";
  public static final String ATTRIBUTE_LINK_IDS                                = "Attribute_link_ids";
  public static final String RULES                                             = "Rules";
  public static final String VALUE_ATTRIBUTE_ID                                = "ValueAttributeId";
  public static final String ENTITY_ATTRIBUTE_TYPE                             = "EntityAttributeType";
  public static final String FIND_TEXT                                         = "FindText";
  public static final String REPLACE_TEXT                                      = "ReplaceText";
  public static final String START_INDEX                                       = "StartIndex";
  public static final String END_INDEX                                         = "EndIndex";
  public static final String ATTRIBUTE_CONCATENATED_LIST                       = "AttributeConcatenatedList";
  public static final String ENTITYTYPE                                        = "Entity_Type";
  public static final String COMPARE_WITH_SYS_DATE                             = "CompareWithSysDate";
  public static final String RULE_LIST_LINKID                                  = "RuleListLinkId";
  public static final String From                                              = "From";
  public static final String To                                                = "To";
  public static final String INNER_TAG_ID                                      = "Inner_Tag_Id";
  
  // attribute/tag/propertyCollection json key
  public static final String SUBTYPE                                           = "Sub_Type";
  public static final String NAME_FOR_PROPERTY                                 = "Name";
  public static final String ALLOWEDSTYLES                                     = "Allowed_Styles";
  public static final String AVAILABILITY_FOR_PROPERTY                         = "Availability";
  public static final String DEFAULT_UNIT_FOR_PROPERTY                         = "Default_Unit";
  public static final String DEFAULT_VALUE_FOR_PROPERTY                        = "Default_Value";
  public static final String DEFAULT_VALUE_AS_HTML_FOR_PROPERTY                = "Default_Value_As_HTML";
  public static final String DESCRIPTION_FOR_PROPERTY                          = "Description";
  public static final String FORMULA_FOR_PROPERTY                              = "Formula";
  public static final String DISABLED_FOR_PROPERTY                             = "Disabled";
  public static final String FILTERABLE_FOR_PROPERTY                           = "Filterable";
  public static final String GRID_EDITABLE_FOR_PROPERTY                        = "Grid_Editable";
  public static final String READ_ONLY_FOR_PROPERTY                            = "Read_Only";
  public static final String TYPE_OF_PROPERTYCONCATENATED                      = "Type_Of_Property_Concatened";
  public static final String ATTRIBUTE_CODE_OF_PROPERTYCONCATENATED            = "Attribute_Code_Of_Property_Concatenated";
  public static final String VALUE_OF_PROPERTYCONCATENATED                     = "Value_Of_Property_Concatenated";
  public static final String CALCULATED_ATTRIBUTE_TYPE_FOR_PROPERTY            = "Calculated_Attribute_Type";
  public static final String CONCATENATED_ATTRIBUTE_TYPE_FOR_PROPEERTY         = "Concatenated_Attribute_Type";
  public static final String CALCULATED_ATTRIBUTE_UNIT_FOR_PROPERTY            = "Calculated_Attribute_Unit";
  public static final String TYPE_OF_PROPERTYCALCULATED                        = "Type_Of_Property_Calculated";
  public static final String ATTRIBUTE_CODE_OF_PROPERTYCALCULATED              = "Attribute_Code_Of_Property_Calculated";
  public static final String VALUE_OF_PROPERTYCALCULATED                       = "Value_Of_Property_Calculated";
  public static final String READ_ONLY                                         = "Readonly";
  public static final String SHOW_CODE_TAG                                     = "Show_Code_Tag";
  public static final String MANDATORY_FOR_PROPERTY                            = "Mandatory";
  public static final String SEARCHABLE_FOR_PROPERTY                           = "Searchable";
  public static final String SORTABLE_FOR_PROPERTY                             = "Sortable";
  public static final String STANDARD_FOR_PROPERTY                             = "Standard";
  public static final String TRANSLATABLE_FOR_PROPERTY                         = "Translatable";
  public static final String REVISIONABLE_FOR_PROPERTY                         = "Revisionable";
  public static final String PLACEHOLDER_FOR_PROPERTY                          = "Placeholder";
  public static final String PRECISION_FOR_PROPERTY                            = "Precision";
  public static final String TOOLTIP_FOR_PROPERTY                              = "Tooltip";
  public static final String ACTION_FOR_PROPERTY                               = "Action";
  public static final String HIDE_SEPARATOR_FOR_PROPERTY                       = "Hide_Separator";
  public static final String LINKED_MASTER_TAG_CODE_FOR_TAG                    = "Linked_Master_Tag_Code";
  public static final String ELEMENTS                                          = "Elements";
  public static final String IS_FOR_XRAYCOLUMN                                = "IsForXRay";
  
  // column name for Permission
  
  public static final String PERMISSION_TYPE                                   = "Permission_Type";
  public static final String KLASS_CODE                                        = "Klass_Code";
  public static final String PERMISSION_CREATE                                 = "Create";
  public static final String PERMISSION_DELETE                                 = "Delete";
  public static final String PERMISSION_DOWNLOAD                               = "Download";
  public static final String PERMISSION_READ                                   = "Read";
  public static final String PERMISSION_UPDATE                                 = "Update";
  
  
  public static final String GENERAL_INFO_LABEL                                = "General_Info_Label";
  public static final String VISIBLE                                           = "Visible";
  public static final String CAN_EDIT                                          = "CanEdit";
  public static final String CAN_ADD                                           = "CanAdd";
  public static final String CAN_REMOVE                                        = "CanRemove";
  public static final String ATTRIBUTE_TAG_CODE                                = "Attribute/Tag_Code";
  public static final String PERMISSION_RELATIONSHIPS_COLOUMN                  = "Relationships";
  public static final String PROPERTY_TYPE_COLOUMN                             = "Property_Type";
  
  // General info label constant for permissions
  public static final String GENERAL_INFO_IMAGE                                = "Image";
  public static final String GENERAL_INFO_NAME                                 = "Name";
  public static final String GENERAL_INFO_NATURE_TYPE                          = "Nature type";
  public static final String GENERAL_INFO_ADDITIONAL_CLASSES                   = "Additional classes";
  public static final String GENERAL_INFO_TAXONOMIES                           = "Taxonomies";
  public static final String GENERAL_INFO_LIFECYCLE_STATUS_TAGS                = "Lifecycle status tags";
  public static final String GENERAL_INFO_CREATED_AND_LAST_MODIFIED_INFO       = "Created & last modified info";
  
}
