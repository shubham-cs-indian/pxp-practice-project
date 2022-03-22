package com.cs.core.config.strategy.constants;

public class ConfigRequestMappingConstants {
  
  /**
   * ************************* CONFIG *********************
   */
  public static final String CREATE_VERTEX_TYPES                                                     = "CreateVertexTypes";
  
  /**
   * ************************* TASK ************************
   */
  public static final String CREATE_TASK                                                             = "CreateTask";
  
  public static final String GET_ALL_TASKS                                                           = "GetAllTask";
  public static final String GET_TASK                                                                = "GetTask";
  public static final String DELETE_TASKS                                                            = "DeleteTasks";
  public static final String SAVE_TASK                                                               = "SaveTask";
  public static final String GET_TASKS_CONFIG_DETAILS                                                = "GetConfigDetailsForTasksTab";
  public static final String GET_CONFIG_DETAILS_BY_TASK_TYPE                                         = "GetConfigDetailsByTaskType";
  public static final String GET_CONFIG_DETAILS_FOR_TASK_DASHBOARD                                   = "GetConfigDetailsForTasksDashboard";
  public static final String GET_GRID_TASKS                                                          = "GetGridTasks";
  public static final String GET_KLASS_INFO_OF_LINKED_CONTENT                                        = "GetKlassInfoOfLinkedContent";
  
  /**
   * *************************GOVERNANCE TASK ************************
   */
  public static final String CREATE_GOVERNANCE_TASK                                                  = "CreateGovernanceTask";
  
  public static final String GET_ALL_GOVERNANCE_TASKS                                                = "GetAllGovernanceTask";
  public static final String GET_GOVERNANCE_TASK                                                     = "GetGovernanceTask";
  public static final String DELETE_GOVERNANCE_TASKS                                                 = "DeleteGovernanceTasks";
  public static final String SAVE_GOVERNANCE_TASK                                                    = "SaveGovernanceTask";
  public static final String GET_CONFIG_DETAILS_BY_GOVERNANCE_TASK_TYPE                              = "GetConfigDetailsByGovernanceTaskType";
  public static final String GET_GRID_GOVERNANCE_TASKS                                               = "GetGridGovernanceTasks";
  
  /**
   * ************************* ATTRIBUTES *******************
   */
  public static final String CREATE_ATTRIBUTE                                                        = "CreateAttribute";
  
  public static final String DELETE_ATTRIBUTE                                                        = "DeleteAttribute";
  public static final String GET_ATTRIBUTES_BY_ID                                                    = "GetAttributesById";
  public static final String GET_ATTRIBUTES                                                          = "GetAttributes";
  public static final String GET_ATTRIBUTE_LIST                                                      = "GetAttributeList";
  public static final String GET_ATTRIBUTE                                                           = "GetAttribute";
  public static final String SAVE_ATTRIBUTE                                                          = "SaveAttribute";
  public static final String CREATE_STANDARD_ATTRIBUTE                                               = "CreateStandardAttribute";
  public static final String GET_OR_CREATE_STANDARD_ATTRIBUTE                                        = "GetOrCreateStandardAttribute";
  public static final String BULK_CREATE_ATTRIBUTES                                                  = "BulkCreateAttributes";
  public static final String GET_GRID_ATTRIBUTES                                                     = "GetGridAttributes";
  public static final String CHECK_FOR_DUPLICATE_CODE                                                = "CheckForDuplicateCode";
  public static final String GET_DEPENDENT_ATTRIBUTE_IDS                                             = "GetDependentAttributeIds";
  public static final String GET_TRANSLATABLE_ATTRIBUTES                                             = "GetTranslatableAttributes";
  public static final String BULK_CHECK_FOR_DUPLICATE_CODES                                          = "BulkCheckForDuplicateCodes";
  
  /**
   * ************************* TAGS *************************
   */
  public static final String CREATE_TAG                                                              = "CreateTag";
  
  public static final String GET_TAG                                                                 = "GetTag";
  public static final String SAVE_TAG                                                                = "SaveTag";
  public static final String GET_TAGS_BY_IDS                                                         = "GetTagsById";
  public static final String DELETE_TAGS                                                             = "DeleteTags";
  public static final String GET_OR_CREATE_TAGS                                                      = "GetOrCreateTags";
  public static final String GET_ALL_DIMENTIONAL_TAG_IDS                                             = "GetAllDimensionalTagIds";
  public static final String GET_ALL_DISPLAY_AND_RELEVANCE_TAG_IDS                                   = "GetAllDisplayAndRelevanceTagIds";
  public static final String GET_ALL_DISPLAY_TAG_IDS                                                 = "GetAllDisplayTagIds";
  public static final String GET_ALL_SINGLE_SELECT_TAG_IDS                                           = "GetAllSingleSelectTagIds";
  public static final String GET_TAG_TYPES                                                           = "GetTagTypes";
  public static final String GET_OR_CREATE_TAG_TYPES                                                 = "GetOrCreateTagTypes";
  public static final String GET_GRID_TAGS                                                           = "GetGridTags";
  public static final String BULK_CREATE_TAG_VALUES                                                  = "BulkCreateTagValues";
  public static final String GET_TAGVALUES_BY_TAG_IDS                                                = "GetTagValuesByTagIds";
  
  /**
   * ************************* ROLES *************************
   */
  public static final String CREATE_ROLE                                                             = "CreateRole";
  public static final String CREATE_ROLE_CLONE                                                       = "CreateRoleClone";
  
  public static final String GET_ROLES                                                               = "GetRoles";
  public static final String GET_ROLE                                                                = "GetRole";
  public static final String SAVE_ROLE                                                               = "SaveRole";
  public static final String DELETE_ROLES                                                            = "DeleteRoles";
  public static final String GET_ROLE_IDS_FOR_CURRENT_USER                                           = "GetRoleIdsForCurrentUser";
  public static final String GET_ROLES_AND_KLASSES_FOR_USER                                          = "GetRolesAndKlassesForUser";
  public static final String GET_OR_CREATE_STANDARD_ROLE                                             = "GetOrCreateStandardRole";
  public static final String GET_ROLES_BY_IDS                                                        = "GetRolesById";
  public static final String GET_ALLOWED_TARGETS_FOR_ROLE                                            = "GetAllowedTargetsForOrganizationAndRole";
  public static final String GET_ROLE_BY_USER_ID                                                     = "GetRoleByUserId";
  
  /**
   * ************************* RELATIONSHIPS *************************
   */
  public static final String CREATE_RELATIONSHIP                                                     = "CreateRelationship";
  
  public static final String GET_KLASSES_FOR_RELATIONSHIP_SIDE                                       = "GetKlassesForRelationshipSide";
  public static final String GET_KLASSES_FOR_RELATIONSHIP                                            = "GetKlassesForRelationship";
  public static final String GET_RELATIONSHIP                                                        = "GetRelationship";
  public static final String GET_ALL_RELATIONSHIPS                                                   = "GetAllRelationships";
  public static final String SAVE_RELATIONSHIP                                                       = "SaveRelationship";
  public static final String DELETE_RELATIONSHIP                                                     = "DeleteRelationship";
  public static final String GET_RELATIONSHIP_BY_ID                                                  = "GetRelationshipsById";
  public static final String BULK_CREATE_RELATIONSHIPS                                               = "BulkCreateRelationships";
  public static final String GET_CONTENT_RELATIONSHIP_INSTANCES                                      = "GetContentRelationshipInstances";
  public static final String GET_UNLINKED_RELATIONSHIP_BY_KLASS_IDS                                  = "GetUnlinkedRelationshipsByKlassIds";
  public static final String GET_RELATIONSHIP_INFORMATION                                            = "GetRelationshipInformation";
  public static final String GET_RELATIONSHIP_INFORMATION_BY_KLASS_ID                                = "GetRelationshipInformationByKlassId";
  public static final String GET_ROOT_RELATIONSHIP                                                   = "GetRootRelationship";
  public static final String GET_NATURE_RELATIONSHIP_BY_TYPE                                         = "GetNatureRelationshipByType";
  public static final String GET_ROOT_RELATIONSHIPS_BY_IDS_FOR_DI_IMPORT                             = "GetRootRelationshipsByIdsForDiImport";
  public static final String GET_ROOT_RELATIONSHIP_ID_BY_CODE                                        = "GetRootRelationshipIdByCode";
  public static final String GET_SIDE2_NATURE_KLASS_FROM_NATURE_RELATIONSHIP                         = "GetSide2NatureKlassFromNatureRelationship";
  public static final String GET_ALL_SIDE2_NATURE_KLASSES_FROM_NATURE_RELATIONSHIP                   = "GetAllSide2NatureKlassesFromNatureRelationship";
  public static final String GET_ALL_NATURE_RELATIONSHIP_IDS                                         = "GetAllNatureRelationshipIds";
  public static final String GET_ROOT_RELATIONSHIP_SIDE_INFO                                         = "GetRootRelationshipSideInfo";

  /**
   * ************************* RELATIONSHIP INHERITANCE
   * *************************
   */
  public static final String GET_ELIGIBLE_RELATIONSHIPS_FOR_RELATIONSHIP_INHERITANCE                 = "GetEligibleRelationshipsForRelationshipInheritance";
  
  public static final String GET_NATURE_RELATIONSHIP_INFO                                            = "GetNatureRelationshipInfoForRelationshipInheritance";
  public static final String GET_MERGED_COUPLING_TYPE                                                = "GetMergedCouplingTypeForRelationshipInheritance";
  public static final String GET_NATURE_RELATIONSHIP_INFO_ON_NATURE_CHANGE                           = "GetRelationshipInheritanceInfoOnNatureRelationshipChange";
  public static final String GET_RELATIONSHIP_INFO_ON_CONFIG_CHANGE                                  = "GetRelationshipInfoOnConfigChange";
  public static final String GET_CONFIG_DETAILS_FOR_REL_INHERITANCE_ON_TYPE_SWITCH                   = "GetConfigDetailsForRelationshipInheritanceOnTypeSwitch";
  public static final String GET_RELATIONSHIP_INHERITANCE_INFO_ON_ROLLBACK                           = "GetRelationshipInheritanceInfoOnRollback";
  
  /**
   * ************************* PROPERTY_COLLECTIONS *************************
   */
  public static final String CREATE_PROPERTY_COLLECTION                                              = "CreatePropertyCollection";
  
  public static final String DELETE_PROPERTY_COLLECTION                                              = "DeletePropertyCollection";
  public static final String GET_ALL_PROPERTY_COLLECTION                                             = "GetAllPropertyCollection";
  public static final String GET_OR_CREATE_PROPERTY_COLLECTIONS                                      = "GetOrCreatePropertyCollections";
  public static final String GET_PROPERTY_COLLECTION                                                 = "GetPropertyCollection";
  public static final String SAVE_PROPERTY_COLLECTION                                                = "SavePropertyCollection";
  
  /**
   * ************************* USERS *************************
   */
  public static final String CREATE_USER                                                             = "CreateUser";
  
  public static final String GET_USERS                                                               = "GetUsers";
  public static final String GET_USER                                                                = "GetUser";
  public static final String SAVE_USERS                                                              = "SaveUsers";
  public static final String RESET_PASSWORD                                                          = "ResetPassword";
  public static final String GET_USER_BY_USERNAME                                                    = "GetUserByUsername";
  public static final String GET_USER_BY_USER_NAME_SSO                                               = "GetUserByUserNameSSO";
  public static final String GET_USER_BY_EMAIL_ID                                                    = "GetUserByEmailId";
  public static final String GET_VALIDATE_USER                                                       = "GetValidateUser";
  public static final String AUTHENTICATE_USER                                                       = "AuthenticateUser";
  public static final String GET_CURRENT_USER                                                        = "GetCurrentUser";
  public static final String DELETE_USERS                                                            = "DeleteUsers";
  public static final String GET_OR_CREATE_STANDARD_USER                                             = "GetOrCreateStandardUser";
  public static final String BULK_CREATE_USERS                                                       = "BulkCreateUsers";
  public static final String GET_ALLOWED_USERS                                                       = "GetAllowedUsers";
  public static final String GET_ALL_OFFBOARDING_ENDPOINT_FOR_USER                                   = "GetAllOffboardingEndpointsForUser";
  public static final String GET_GRID_USERS                                                          = "GetGridUsers";
  public static final String GET_USERS_BY_ROLE                                                       = "GetUsersByRole";
  public static final String GET_LANGUAGE_FOR_USER                                                   = "GetLanguageForUser";
  
  /**
   * ************************* KLASS *************************
   */
  public static final String CREATE_KLASS                                                            = "CreateKlass";
  
  public static final String GET_KLASS_WITH_GLOBAL_PERMISSION                                        = "GetKlassWithGlobalPermission";
  public static final String GET_KLASS_WITHOUT_KP                                                    = "GetKlassWithoutKP";
  public static final String SAVE_KLASS                                                              = "SaveKlass";
  public static final String GET_OR_CREATE_KLASS                                                     = "GetOrCreateKlass";
  public static final String DELETE_KLASSES                                                          = "DeleteKlasses";
  public static final String GET_ALL_KLASSES_TREE                                                    = "GetAllKlassesTree";
  public static final String GET_KLASSES_TREE                                                        = "GetKlassesTree";
  public static final String GET_TAXONOMY_TREE                                                       = "GetTaxonomiesTree";
  public static final String GET_DEFAULT_KLASSES_FOR_MODULES                                         = "GetDefaultKlassesForModules";
  public static final String GET_KLASS_WITH_LINKED_KLASSES                                           = "GetKlassWithLinkedKlasses";
  public static final String GET_ALLOWED_TYPES                                                       = "GetAllowedTypes";
  public static final String GET_DEFAULT_KLASSES                                                     = "GetDefaultKlasses";
  public static final String GET_FILTER_AND_SORT_DATA                                                = "GetFilterAndSortDataForKlass";
  public static final String GET_KLASSES_BY_ID                                                       = "GetKlassesById";
  public static final String GET_KLASS_FOR_MODULE                                                    = "GetKlassForModule";
  public static final String BULK_CREATE_KLASSES                                                     = "BulkCreateKlasses";
  public static final String BULK_SAVE_KLASSES                                                       = "BulkSaveKlasses";
  public static final String GET_ARTICLE_TREE                                                        = "GetArticleTree";
  public static final String GET_KLASS                                                               = "GetKlass";
  public static final String GET_TARGET_KLASSES                                                      = "GetTargetKlasses";
  public static final String GET_FILTER_AND_SORT_DATA_FOR_ARTICLE                                    = "GetFilterAndSortDataForArticle";
  public static final String GET_TARGET                                                              = "GetTarget";
  public static final String GET_MARKET_TREE                                                         = "GetMarketTree";
  public static final String GET_ALL_ASSET_RELATIONSHIPS_BY_KLASS_IDS                                = "GetAllAssetRelationshipsByKlassIds";
  public static final String GET_KLASSES_LIST_BY_NATURE_TYPE                                         = "GetKlassesListByNatureType";
  public static final String GET_KLASSES_LIST_BY_TYPES                                               = "GetKlassesListByTypes";
  public static final String GET_KLASSES_LIST_BY_BASE_TYPE                                           = "GetKlassesListByBaseType";
  public static final String GET_CONFIG_DETAILS_FOR_CONTENT_GRID_VIEW                                = "GetConfigDetailsForContentGridView";
  public static final String GET_SECTION_INFO_FOR_KLASS                                              = "GetSectionInfoForKlass";
  public static final String GET_SECTION_INFO_FOR_RELATIONSHIP_EXPORT                                = "GetSectionInfoForRelationshipExport";
  public static final String GET_CONFIG_DATA_FOR_RELATIONSHIP_EXPORT                                 = "GetConfigDataForRelationshipExport";
  public static final String GET_SECTION_INFO_FOR_COLLECTION                                         = "GetSectionInfoForCollection";
  public static final String GET_SECTION_INFO_FOR_TAXONOMY                                           = "GetSectionInfoForTaxonomy";
  public static final String GET_CHILD_KLASS_IDS_IN_HIERARCHY                                        = "GetChildKlassIdsInHierarchy";
  public static final String GET_DEFAULT_TYPES                                                       = "GetDefaultTypes";
  /**
   * ************************ CONTEXT ************************
   */
  public static final String CREATE_VARIANT_CONTEXT                                                  = "CreateVariantContext";
  
  public static final String SAVE_VARIANT_CONTEXT                                                    = "SaveVariantContext";
  public static final String GET_VARIANT_CONTEXT                                                     = "GetVariantContext";
  public static final String GET_ALL_VARIANT_CONTEXT                                                 = "GetAllVariantContext";
  public static final String DELETE_VARIANT_CONTEXT                                                  = "DeleteVariantContext";
  public static final String GET_OR_CREATE_VARIANT_CONTEXTS                                          = "GetOrCreateVariantContexts";
  public static final String GET_CONFIG_DETAILS_FOR_DUPLICATE_LINKED_VARIANT                         = "GetConfigtDetailsForDuplicateLinkedVariant";
  
  /**
   * ************************* RULE_LIST ******************************
   */
  public static final String CREATE_RULE_LIST                                                        = "CreateRuleList";
  
  public static final String DELETE_RULE_LIST                                                        = "DeleteRuleList";
  public static final String GET_ALL_RULE_LIST                                                       = "GetAllRuleList";
  public static final String GET_RULE_LIST                                                           = "GetRuleList";
  public static final String SAVE_RULE_LIST                                                          = "SaveRuleList";
  
  /**
   * ************************* GLOBAL PERMISSIONS *********************
   */
  public static final String GET_GLOBAL_PERMISSIONS_FOR_ROLE                                         = "GetGlobalPermissionsForRole";
  
  public static final String GET_PROPERTY_COLLECTIONS_FOR_ENTITY                                     = "GetPropertyCollectionsForEntity";
  public static final String GET_PROPERTIES_OF_PROPERTY_COLLECTION                                   = "GetPropertiesOfPropertyCollection";
  public static final String GET_GLOBAL_PERMISSION_FOR_ENTITIES                                      = "GetGlobalPermissionForEntities";
  public static final String CREATE_OR_SAVE_GLOBAL_PERMISSION                                        = "CreateOrSaveGlobalPermission";
  public static final String GET_GLOBAL_PERMISSION_WITH_ALLOWED_TEMPLATES                            = "GetGlobalPermissionWithAllowedTemplates";
  public static final String GET_GLOBAL_PERMISSION_FOR_RUNTIME_ENTITIES                              = "GetGlobalPermissionForRuntimeEntities";
  public static final String GET_GLOBAL_PERMISSION_FOR_SINGLE_ENTITY                                 = "GetGlobalPermissionForSingleEntity";
  public static final String GET_GLOBAL_PERMISSION_FOR_SINGLE_ENTITY_FOR_ROLE                        = "GetGlobalPermissionForSingleEntityForRole";
  public static final String GET_GLOBAL_PERMISSION_FOR_MULTIPLE_INSTANCES                            = "GetGlobalPermissionForMultipleInstances";
  public static final String GET_GLOBAL_PERMISSION_FOR_MULTIPLE_NATURE_TYPES                         = "GetGlobalPermissionForMultipleNatureTypes";
  public static final String GET_NATURE_KLASS_PERMISSION                                             = "GetNatureKlassPermission";
  public static final String GET_COVERFLOW_FOR_NATURE_KLASS_WITH_DOWNLOAD_PERMISSION                 = "GetCoverFlowForNatureKlassWithDownloadPermission";
  
  /**
   * ************************* TEMPLATES *****************************
   */
  public static final String GET_ALL_TEMPLATES                                                       = "GetAllTemplates";
  
  public static final String BULK_SAVE_TEMPLATE                                                      = "BulkSaveTemplate";
  
  /**
   * ************************* DATA_RULES *****************************
   */
  public static final String CREATE_DATA_RULE                                                        = "CreateDataRule";
  
  public static final String DELETE_DATA_RULES                                                       = "DeleteDataRules";
  public static final String SAVE_DATA_RULE                                                          = "SaveDataRule";
  public static final String GET_DATA_RULE                                                           = "GetDataRule";
  public static final String GET_DATA_RULES                                                          = "GetDataRules";
  public static final String BULK_SAVE_DATA_RULE                                                     = "BulkSaveDataRule";
  
  /**
   * ************************* MODULES *******************************
   */
  public static final String GET_ALLOWED_MODULE_ENTITIES                                             = "GetAllowedModuleEntities";
  
  /**
   * ************************* ASSET *********************************
   */
  public static final String CREATE_ASSET                                                            = "CreateAsset";
  
  public static final String GET_ASSET_WITH_GLOBAL_PERMISSION                                        = "GetAssetWithGlobalPermission";
  public static final String GET_OR_CREATE_ASSET                                                     = "GetOrCreateAsset";
  public static final String SAVE_ASSET                                                              = "SaveAsset";
  public static final String DELETE_ASSETS                                                           = "DeleteAssets";
  public static final String BULK_CREATE_ASSETS                                                      = "BulkCreateAssets";
  public static final String BULK_SAVE_ASSETS                                                        = "BulkSaveAssets";
  public static final String GET_ASSETS                                                              = "GetAssets";
  public static final String GET_ALL_MASTER_ASSETS                                                   = "GetAllMasterAssets";
  public static final String GET_ASSETS_BY_IDS                                                       = "GetAssetsByIds";
  public static final String GET_ASSET                                                               = "GetAsset";
  public static final String GET_ASSET_TREE                                                          = "GetAssetTree";
  public static final String GET_FILTER_AND_SORT_DATA_FOR_ASSET                                      = "GetFilterAndSortDataForAsset";
  public static final String GET_ASSET_WITHOUT_KP                                                    = "GetAssetWithoutKP";
  public static final String FETCH_ASSET_CONFIGURATION_DETAILS                                       = "FetchAssetConfigurationDetails";
  public static final String FETCH_DAM_CONFIGURATION_DETAILS                                         = "FetchDAMConfigurationDetails";
  public static final String GET_ALL_RENDITION_KLASSES_WITH_PERMISSION                               = "GetAllRenditionKlassesWithPermission";
  public static final String GET_ALL_ASSET_EXTENSIONS                                                = "GetAllAssetExtensions";
  public static final String GET_ASSET_PARENT_ID                                                     = "GetAssetParentId";
  public static final String GET_ASSET_SHARE_DIALOG_INFORMATION                                      = "GetAssetShareDialogInformation";
  public static final String GET_DOWNLOAD_TRACKER_CONFIGURATION                                      = "GetDownloadTrackerConfiguration";
  public static final String GET_ALL_ICONS                                                           = "GetAllIcons";
  public static final String SAVE_OR_REPLACE_ICON                                                    = "SaveOrReplaceIcon";
  public static final String BULK_DELETE_ICONS                                                       = "BulkDeleteIcons";
  public static final String GET_DUPLICATE_DETECTION_STATUS                                          = "GetDuplicateDetectionStatus";
  public static final String SAVE_DAM_CONFIGURATION                                                  = "SaveDAMConfiguration";
  public static final String GET_CONFIG_DETAILS_FOR_ASSET_EXPORT_API                                 = "GetConfigDetailsForAssetExportAPI";
  public static final String GET_LABELS_BY_IDS                                                       = "GetLabelsByIds";
  
  /**
   * ************************* SUPPLIER ******************************
   */
  public static final String CREATE_SUPPLIER                                                         = "CreateSupplier";
  
  public static final String DELETE_SUPPLIER                                                         = "DeleteSuppliers";
  public static final String GET_OR_CREATE_SUPPLIER                                                  = "GetOrCreateSupplier";
  public static final String GET_SUPPLIER_WITH_GLOBAL_PERMISSION                                     = "GetSupplierWithGlobalPermission";
  public static final String SAVE_SUPPLIER                                                           = "SaveSupplier";
  public static final String GET_FILTER_AND_SORT_DATA_FOR_SUPPLIER                                   = "GetFilterAndSortDataForSupplier";
  public static final String GET_MULTI_CLASSIFICATION_SUPPLIER_DETAILS                               = "GetMultiClassificationSupplierDetails";
  public static final String GET_SUPPLIER                                                            = "GetSupplier";
  public static final String GET_SUPPLIER_WITHOUT_KP                                                 = "GetSupplierWithoutKP";
  /**
   * ************************* TARGET ******************************
   */
  public static final String CREATE_TARGET                                                           = "CreateTarget";
  
  public static final String DELETE_TARGETS                                                          = "DeleteTargets";
  public static final String GET_OR_CREATE_TARGET                                                    = "GetOrCreateTarget";
  public static final String GET_TARGET_WITH_GLOBAL_PERMISSSION                                      = "GetTargetWithGlobalPermission";
  public static final String SAVE_TARGET                                                             = "SaveTarget";
  public static final String GET_FILTER_AND_SORT_DATA_FOR_TARGET                                     = "GetFilterAndSortDataForTarget";
  public static final String GET_SUPPLIER_TREE                                                       = "GetSupplierTree";
  public static final String GET_TARGET_WITHOUT_KP                                                   = "GetTargetWithoutKP";
  
  /**
   * ************************* Taxonomy ******************************
   */
  
  public static final String SAVE_ARTICLE_TAXONOMY                                                   = "SaveHierarchyTaxonomy";
  public static final String GET_ARTICLE_TAXONOMY_LIST                                               = "GetArticleTaxonomyList";
  public static final String GET_FILTER_AND_SORT_DATA_FOR_SELECTED_TAXONOMIES                        = "GetFilterAndSortDataForSelectedTaxonomies";
  public static final String GET_HIERARCHY_TAXONOMY_IDS                                              = "GetHierarchyTaxonomyIds";
  /**
   * ************************* Attribution Taxonomy ***********************
   */
  public static final String GET_MASTER_TAXONOMY                                                     = "GetMasterTaxonomy";
  
  public static final String CREATE_MASTER_TAXONOMY                                                  = "CreateMasterTaxonomy";
  public static final String SAVE_MASTER_TAXONOMY                                                    = "SaveMasterTaxonomy";
  public static final String GET_AVAILABLE_MASTER_TAGS                                               = "GetAvailableMasterTags";
  public static final String BULK_CREATE_MASTER_TAXONOMY                                             = "BulkCreateMasterTaxonomy";
  public static final String DELETE_MASTER_TAXONOMY                                                  = "DeleteMasterTaxonomy";
  public static final String GET_TAXONOMIES                                                          = "GetTaxonomies";
  public static final String GET_ALLOWED_TAG_VALUES                                                  = "GetAllowedTagValuesForMasterTaxonomy";
  public static final String GET_KLASS_AND_ATTRIBUTION_TAXONOMY                                      = "GetKlassAndAttributionTaxonomy";
  public static final String GET_ALL_MASTER_TAGS                                                     = "GetAllMasterTags";
  public static final String GET_AVAILABLE_MASTER_TAGS_FOR_ARTICLE_TAXONOMY                          = "GetAvailableMasterTagsForArticleTaxonomy";
  public static final String GET_MASTER_TAXONOMY_IDS                                                 = "GetMasterTaxonomyIds";
  public static final String GET_CHILD_HIERARCHY_TAXONOMIES_FOR_TAXONOMY_LEVEL                       = "GetChildHierarchyTaxonomiesForTaxonomyLevel";
  public static final String GET_CHILD_MASTER_TAXONOMIES_FOR_TAXONOMY_LEVEL                          = "GetChildMasterTaxonomiesForTaxonomyLevel";
  
  /**
   * ************************* Language Taxonomy ***********************
   */
  public static final String GET_LANGUAGE                                                            = "GetLanguage";
  public static final String GET_LANGUAGE_BY_LOCALE_ID                                               = "GetLanguageByLocaleId";

  public static final String CREATE_LANGUAGE                                                         = "CreateLanguage";
  public static final String SAVE_LANGUAGE                                                           = "SaveLanguage";
  public static final String DELETE_LANGUAGE                                                         = "DeleteLanguage";
  public static final String GET_OR_CREATE_LANGUAGE                                                  = "GetOrCreateLanguage";
  public static final String UPDATE_SCHEMA_ON_CREATE_LANGUAGE                                        = "UpdateSchemaOnCreateLanguage";
  public static final String GET_LANGUAGES                                                           = "GetLanguages";
  public static final String GET_AVAILABLE_LANGUAGE_TAGS                                             = "GetAvailableLanguageTags";
  public static final String GET_ALL_LANGUAGE_CODES                                                  = "GetAllLanguageCodes";
  public static final String GET_CURRENT_DEFAULT_LANGUAGE                                            = "GetCurrentDefaultLanguage";
  public static final String GET_CHILD_LANGUAGE_CODE_VERSUS_LANGUAGE_ID                              = "GetChildLanguageCodeVersusLanguageId";
  public static final String GET_ALL_AUDIT_LOG_LABEL                                                 = "GetAllAuditLogLabel";
  
  /**
   * ************************* TextAsset *****************************
   */
  public static final String CREATE_TEXT_ASSET                                                       = "CreateTextAsset";
  
  public static final String DELETE_TEXT_ASSETS                                                      = "DeleteTextAssets";
  public static final String GET_OR_CREATE_TEXT_ASSET                                                = "GetOrCreateTextAsset";
  public static final String GET_TEXT_ASSET_WITH_GLOBAL_PERMISSION                                   = "GetTextAssetWithGlobalPermission";
  public static final String SAVE_TEXT_ASSET                                                         = "SaveTextAsset";
  public static final String GET_ALL_TEXT_ASSETS                                                     = "GetAllTextAssets";
  public static final String GET_FILTER_AND_SORT_DATA_FOR_TEXT_ASSET                                 = "GetFilterAndSortDataForTextAsset";
  public static final String GET_TEXT_ASSET_TREE                                                     = "GetTextAssetTree";
  public static final String GET_TEXT_ASSET                                                          = "GetTextAsset";
  public static final String GET_TEXT_ASSET_WITHOUT_KP                                               = "GetTextAssetWithoutKP";
  
  /**
   * ************************* Variants **********************************
   */
  public static final String GET_ALL_VARIANTS_BY_ARTICLE                                             = "GetAllVariantsByArticle";
  
  /**
   * ************************* Branch *****************************
   */
  public static final String GET_RESOLVED_CONFLICTING_DEFAULT_VALUES                                 = "GetResolvedConflictingDefaultValues";
  
  /**
   * ************************* MULTICLASSIFICATION
   * *********************************
   */
  public static final String GET_MULTICLASSIFICATION_ARTICLE_DETAILS                                 = "GetMultiClassificationArticleDetails";
  
  public static final String GET_MULTICLASSIFICATION_ASSET_DETAILS                                   = "GetMultiClassificationAssetDetails";
  public static final String GET_MULTICLASSIFICATION_PROMOTION_DETAILS                               = "GetMultiClassificationPromotionDetails";
  public static final String GET_MULTICLASSIFICATION_TARGET_DETAILS                                  = "GetMultiClassificationTargetDetails";
  public static final String GET_MULTICLASSIFICATION_TESTASSET_DETAILS                               = "GetMultiClassificationTextAssetDetails";
  
  /**
   * ************************* InboundEndpoint *************************
   */
  public static final String GET_ONBOARDING_ROLE_IDS_FOR_CURRENT_USER                                = "GetOnboardingRoleIdsForCurrentUser";
  
  public static final String GET_ONBOARDING_ROLES_AND_KLASSES_FOR_USER                               = "GetOnboardingRolesAndKlassesForUser";
  public static final String GET_KLASSES_FOR_MAPPING                                                 = "GetKlassesForMapping";
  
  /**
   * ************************* Mapping *************************
   */
  public static final String GET_ALL_MAPPINGS                                                        = "GetAllMappings";
  
  public static final String CLONE_MAPPINGS                                                          = "CloneMappings";
  
  /**
   * ************************* Partner Authorization Mapping *************************
   */
  public static final String CREATE_PARTNER_AUTHORIZATION                                            = "CreatePartnerAuthorizationMappings";
  public static final String GET_PARTNER_AUTHORIZATION_MAPPING                                       = "GetPartnerAuthorizationMapping";
  public static final String GET_ALL_PARTNER_AUTHORIZATION_MAPPINGS                                  = "GetAllPartnerAuthorizationMappings";
  public static final String DELETE_PARTNER_AUTHORIZATION_MAPPING                                    = "DeletePartnerAuthorizationMapping";
  public static final String SAVE_PARTNER_AUTHORIZATION_MAPPING                                      = "SavePartnerAuthorizationMapping";
  public static final String BULK_SAVE_PARTNER_AUTHORIZATION_MAPPING                                 = "BulkSavePartnerAuthorizationMapping";
  
  /**
   * ************************* Endpoint *************************
   */
  public static final String GET_ALLOWED_ENDPOINTS_FOR_ROLE                                          = "GetAllowedEndpointsForRole";
  
  public static final String GET_GRID_ENDPOINTS                                                      = "GetGridEndpoints";
  public static final String SAVE_ENDPOINT                                                           = "SaveEndpoint";
  public static final String GET_ENDPOINTS_BY_SYSTEM                                                 = "GetEndpointsBySystem";
  public static final String GET_SELECTED_SYSTEMS_IN_ORGANIZATION                                    = "GetSelectedSystemsInOrganization";
  public static final String CLONE_ENDPOINTS                                                         = "CloneEndpoints";
  
  /**
   * ************************* COLLECTION ENTRY
   * ************************************
   */
  public static final String CREATE_COLLECTION_NODE                                                  = "CreateCollectionNode";
  
  public static final String DELETE_COLLECTION_NODE                                                  = "DeleteCollectionNode";
  public static final String MOVE_STATIC_COLLECTION                                                  = "MoveStaticCollectionHierarchy";
  public static final String GET_COLLECTION_HIERARCHY                                                = "GetStaticCollectionHierarchy";
  public static final String GET_COLLECTION_DETAILS                                                  = "GetStaticCollectionDetails";
  public static final String SAVE_COLLECTION_DETAILS                                                 = "SaveStaticCollectionDetails";
  public static final String SAVE_COLLECTION_NODE                                                    = "SaveStaticCollectionNode";
  public static final String GET_ADDED_PROPERTIES_DIFF                                               = "GetAddedPropertiesDiff";
  public static final String GET_CONFIG_DETAILS_FOR_BULK_PROPAGATION                                 = "GetConfigDetailsForBulkPropagation";
  
  /**
   * ************************* ProcessEvent *************************
   */
  public static final String CREATE_PROCESS_EVENT                                                    = "CreateProcessEvent";
  
  public static final String GET_PROCESS_EVENTS                                                      = "GetProcessEvents";
  public static final String GET_ALL_EXECUTABLE_PROCESS_EVENTS                                       = "GetAllExecutableProcessEvents";
  public static final String GET_PROCESS_EVENT                                                       = "GetProcessEvent";
  public static final String SAVE_PROCESS_EVENT                                                      = "SaveProcessEvent";
  public static final String DELETE_PROCESS_EVENT                                                    = "DeleteProcessEvent";
  public static final String GET_PROCESS_EVENTS_FOR_USER                                             = "GetProcessEventsForUser";
  public static final String GET_PROCESS_BY_CONFIG                                                   = "GetProcessByConfig";
  public static final String BULK_SAVE_PROCESS                                                       = "BulkSaveProcessEvent";
  public static final String GET_GRID_PROCESS_EVENTS                                                 = "GetGridProcessEvents";
  public static final String GET_PROCESS_EVENTS_FOR_DASHBOARD                                        = "GetProcessEventsForDashboard";
  public static final String GET_PROCESS_DEFINITION_BY_ID                                            = "GetProcessDefinitionById";
  public static final String GET_PROCESS_DEFINITION_BY_PROCESS_DEFINITION_ID                         = "GetProcessDefinitionByProcessDefinitionId";
  public static final String CLONE_PROCESS_EVENTS                                                    = "CloneProcessEvents";
  public static final String GET_PROCESS_EVENTS_BY_IDS                                               = "GetProcessEventsByIds";
  
  public static final String GET_PROCESS_EVENT_BY_ENDPOINT_ID                                        = "GetProcessEventByEndpointId";
  public static final String GET_ALL_DATA_QUALITY_RULES                                              = "GetAllDataQualityRules";
  public static final String GET_CONSUMER_COMPONENTS                                                 = "GetConsumerComponents";
  
  /**
   * ************************* OnboardingTaxonomy ******************************
   */
  
  public static final String GET_TAXONOMY_IDS_BY_CODE_AND_PARENT_ID_FOR_ONBOARDING                   = "GetTaxonomyIdsByCodeAndParentIdForOnboarding";
  public static final String GET_TAXONOMY_IDS_BY_CODE_ID_FOR_ONBOARDING                              = "GetTaxonomyIdsByCodeIdForOnboarding";
  public static final String GET_PARENT_VS_CHILDS_TAXONOMY_IDS                                       = "GetParentVsChildsTaxonomyIds";
  public static final String GET_CHILD_TAOXNOMIES_BY_PARENTID_WITH_SELECTED_TAXONOMIES               = "GetChildTaxonomiesByParentIdWithSelectedTaxonomies";
  
  /**
   * ************************** Notifications
   * ***************************************
   */
  public static final String GET_CONFIG_DETAILS_FOR_NOTIFICATION                                     = "GetConfigDetailsForNotification";
  
  /**
   * ************************** Versions ***************************************
   */
  public static final String GET_VERSION_COUNT                                                       = "GetVersionCount";
  
  /**
   * **************************** Governance Rules *****************************
   */
  public static final String GET_GOVERNANCE_RULES_BY_KLASS_AND_TAXONOMY_IDS                          = "GetGovernanceRulesByKlassAndTaxonomyIds";
  
  public static final String CREATE_KEY_PERFORMANCE_INDEX                                            = "CreateKeyPerformanceIndex";
  public static final String GET_KEY_PERFORMANCE_INDEX                                               = "GetKeyPerformanceIndex";
  public static final String SAVE_KEY_PERFORMANCE_INDEX                                              = "SaveKeyPerformanceIndex";
  public static final String GETAll_KEY_PERFORMANCE_INDEX                                            = "GetAllKeyPerformanceIndex";
  public static final String DELETE_KEY_PERFORMANCE_INDEX                                            = "DeleteKeyPerformanceIndex";
  public static final String GET_CONFIG_DETAILS_FOR_GET_STATISTICS                                   = "GetConfigDetailsForGetStatistics";
  public static final String GET_CONFIG_DETAILS_FOR_GET_ALL_STATISTICS                               = "GetConfigDetailsForGetAllStatistics";
  public static final String GET_CONFIG_DETAILS_FOR_GET__STATISTICS_FOR_CONTENT                      = "GetConfigDetailsForGetStatisticsForContent";
  public static final String BULK_SAVE_KPI_RULE                                                      = "BulkSaveKpiRule";
  
  /**
   * ************************** Translations ***********************************
   */
  public static final String SAVE_PROPERTIES_TRANSLATIONS                                            = "SavePropertiesTranslations";
  
  public static final String CREATE_OR_SAVE_PROPERTIES_TRANSLATIONS                                  = "CreateOrSavePropertiesTranslations";
  public static final String GET_PROPERTIES_TRANSLATIONS                                             = "GetPropertiesTranslations";
  public static final String GET_TAG_TRANSLATIONS                                                    = "GetTagTranslations";
  public static final String SAVE_RELATIONSHIP_TRANSLATIONS                                          = "SaveRelationshipTranslations";
  public static final String CREATE_OR_SAVE_RELATIONSHIP_TRANSLATIONS                                = "CreateOrSaveRelationshipTranslations";
  public static final String GET_RELATIONSHIP_TRANSLATIONS                                           = "GetRelationshipTranslations";
  public static final String GET_OR_CREATE_STATIC_TRANSLATIONS                                       = "GetOrCreateStaticTranslations";
  public static final String SAVE_STATIC_TRANSLATIONS                                                = "SaveStaticTranslations";
  public static final String GET_STATIC_TRANSLATIONS                                                 = "GetStaticTranslations";
  public static final String EXPORT_SYSTEM_STATIC_TRANSLATION                                        = "ExportSystemStaticTranslation";
  public static final String GET_STATIC_TRANSLATIONS_FOR_RUNTIME                                     = "GetStaticTranslationsForRuntime";
  
  /**
   * **************************** Organization
   * **************************************
   */
  public static final String SAVE_ORGANIZATION                                                       = "SaveOrganization";
  
  public static final String GET_ORGANIZATION                                                        = "GetOrganization";
  public static final String GET_ALL_ORGANIZATIONS                                                   = "GetAllOrganizations";
  public static final String CREATE_ORGANIZATION                                                     = "CreateOrganization";
  public static final String DELETE_ORGANIZATION                                                     = "DeleteOrganizations";
  public static final String GET_OR_CREATE_ORGANIZATIONS                                             = "GetOrCreateOrganizations";
  
  /**
   * **************************** System **************************************
   */
  public static final String CREATE_SYSTEM                                                           = "CreateSystem";
  
  public static final String GET_PAGINATED_SYSTEMS                                                   = "GetPaginatedSystems";
  public static final String GET_OR_CREATE_SYSTEMS                                                   = "GetOrCreateSystems";
  
  /**
   * **************************** Tabs **************************************
   */
  public static final String CREATE_TAB                                                              = "CreateTab";
  
  public static final String GET_ALL_TABS                                                            = "GetAllTabs";
  public static final String GET_TAB                                                                 = "GetTab";
  public static final String SAVE_TAB                                                                = "SaveTab";
  public static final String DELETE_TABS                                                             = "DeleteTabs";
  public static final String GET_OR_CREATE_TABS                                                      = "GetOrCreateTab";
  public static final String BULK_SAVE_TAB                                                           = "BulkSaveTab";
  
  public static final String CREATE_DASHBOARD_TAB                                                    = "CreateDashboardTab";
  public static final String GET_OR_CREATE_DASHBOARD_TABS                                            = "GetOrCreateDashboardTab";
  public static final String GET_ENDPOINTS_FOR_DASHBOARD_BY_SYSTEM_ID                                = "GetEndpointsForDashboardBySystemId";
  public static final String GET_CONFIG_DETAILS_WITHOUT_PERMISSIONS                                  = "GetConfigDetailsWithoutPermissions";
  public static final String GET_CONFIG_DETAILS_FOR_TYPES_AND_TAXONOMIES_OF_CONTENT                  = "GetConfigDetailsForTypesAndTaxonomiesOfContent";
  public static final String GET_CONFIG_DETAILS_FOR_SAVE_RELATIONSHIP_INSTANCES                      = "GetConfigDetailsForSaveRelationshipInstance";
  public static final String GET_CONFIG_DETAILS_FOR_PREPARE_DATA_FOR_RELATIONSHIP_DATA_TRANSFER      = "GetConfigDetailsForPrepareDataForRelationshipDataTransfer";
  public static final String GET_CONFIG_DETAILS_TO_PREPARE_DATA_FOR_LANGUAGE_INHERITANCE             = "GetConfigDetailsToPrepareDataForLanguageInheritance";
  public static final String GET_CONFIG_DETAILS_FOR_RELATIONSHIP_RESTORE                             = "GetConfigDetailsForRelationshipRestore";
  public static final String GET_CONFIG_DETAILS_FOR_DASHBOARD_TILE_INFORMATION                       = "GetConfigDetailsForDashboardTileInformation";
  
  public static final String GET_ALL_DASHBOARD_TABS                                                  = "GetAllDashboardTabs";
  /**
   * *************************** Permission
   * **************************************
   */
  public static final String GET_PERMISSION                                                        = "GetPermission";
  public static final String GET_FUNCTION_PERMISSION_BY_USER_ID                                    = "GetFunctionPermissionByUserId";
  
  public static final String GET_CONFIG_DETAILS_FOR_GET_KLASS_INSTANCE_TO_COMPARE                    = "GetConfigDetailsForGetKlassInstancesToCompare";
  public static final String GET_CONFIG_DETAILS_FOR_CUSTOM_TAB                                       = "GetConfigDetailsForCustomTab";
  public static final String GET_CONFIG_DETAILS_FOR_QUICKLIST                                        = "GetConfigDetailsForQuicklist";
  public static final String GET_CONFIG_DETAILS_FOR_HIERARCHY_RELATIONSHIP_QUICKLIST                 = "GetConfigDetailsForKlassTaxonomyRelationshipQuicklist";
  public static final String GET_IDENTIFIER_ATTRIBUTES_FOR_TYPES                                     = "GetIdentifierAttributesForTypes";
  public static final String GET_ALLOWED_TYPES_BY_BASE_TYPE                                          = "GetAllowedTypesByBaseType";
  public static final String GET_CONFIG_DETAILS_FOR_AUTO_CREATE_VARIANT_INSTANCE                     = "GetConfigDetailsForAutoCreateVariantInstance";
  public static final String GET_CONFIG_DETAILS_FOR_AUTO_CREATE_TIV                                  = "GetConfigDetailsForAutoCreateTIV";
  public static final String GET_PERMISSION_WITH_HIERARCHY                                           = "GetPermissionWithHierarchy";
  public static final String GET_CONFIG_DETAILS_FOR_OVERVIEW_TAB                                     = "GetConfigDetailsForOverviewTab";
  public static final String GET_CONFIG_DETAILS_FOR_VERSION_ROLLBACK                                 = "GetConfigDetailsForVersionRollback";
  public static final String GET_CONFIG_DETAILS_FOR_MERGE_VIEW                                       = "GetConfigDetailsForMergeView";
  
  public static final String GET_CONFLICT_SOURCES_INFORMATION                                        = "GetConflictSourcesInformation";
  public static final String SAVE_ENTITY_PROPERTY                                                    = "SaveEntityProperty";
  public static final String GET_CONFIG_DETAILS_FOR_GET_VARIANT_INSTANCES_IN_TABLE_VIEW              = "GetConfigDetailsForGetVariantInstancesInTableView";
  public static final String GET_FUNCTION_PERMISSION_FOR_ROLE                                        = "GetFunctionPermissionForRole";
  
  /**
   * *************************** Golden Records
   * ***********************************
   */
  public static final String CREATE_GOLDEN_RECORD_RULE                                               = "CreateGoldenRecordRule";
  
  public static final String GET_GOLDEN_RECORD_RULE                                                  = "GetGoldenRecordRule";
  public static final String SAVE_GOLDEN_RECORD_RULE                                                 = "SaveGoldenRecordRule";
  public static final String DELETE_GOLDEN_RECORD_RULE                                               = "DeleteGoldenRecordRules";
  public static final String GET_ALL_GOLDEN_RECORD_RULES                                             = "GetAllGoldenRecordRules";
  public static final String BULK_SAVE_GOLDEN_RECORD_RULE                                            = "BulkSaveGoldenRecordRule";
  public static final String GET_GOLDEN_RECORD_RULES_ASSOCIATED_WITH_INSTANCE                        = "GetGoldenRecordRulesAssociatedWithInstance";
  public static final String GET_CONFIG_DETAILS_FOR_GOLDEN_RECORD_BUCKETS                            = "GetConfigDetailsForGoldenRecordBuckets";
  public static final String GET_CONFIG_DETAILS_FOR_GOLDEN_RECORD_MATCHED_RECORD_INSTANCES           = "GetConfigDetailsForGoldenRecordMatchedInstances";
  public static final String GET_ALL_LINKED_VARIANT_PROPERTY_CODES                                   = "GetAllLinkedVariantPropertyCodes";
  public static final String GET_CONFIG_DETAILS_FOR_GET_SOURCE_INFO_FOR_BUCKET                       = "GetConfigDetailsForGetSourceInfoForBucket";
  public static final String GET_CONFIG_DETAILS_FOR_GET_RELATIONSHIP_DATA                            = "GetConfigDetailsForGetRelationshipDataFromSources";
  public static final String GET_CONFIG_DETAILS_FOR_TYPE_INFO                                        = "GetConfigDetailsForTypeInfo";
  public static final String GET_CONFIG_DETAILS_FOR_GOLDEN_RECORD_FILTER_AND_SORT_DATA               = "GetConfigDetailsForGoldenRecordFilterAndSortData";
  public static final String GET_CONFIG_DETAILS_FOR_GET_GOLDEN_RECORD_FILTER_CHILDREN                = "GetConfigDetailsForGetGoldenRecordFilterChildren";
  public static final String GET_CONFIG_DETAILS_FOR_GET_KLASS_INSTANCES_TO_MERGE                   	 = "GetConfigDetailsForGetKlassInstancesToMerge";
  
  /**
   * *******************************ConfigDetails
   * *********************************************
   */
  public static final String GET_CONFIG_DETAILS_FOR_TIMELINE_TAB                                     = "GetConfigDetailsForTimelineTab";
  
  public static final String GET_CONFIG_DETAILS_FOR_GET_PROPERTIES_VARIANT_INSTANCES_IN_TABLE_VIEW   = "GetConfigDetailsForGetPropertiesVariantInstancesInTableView";
  public static final String GET_CONFIG_DETAILS_FOR_GET_PRICING_VARIANT_TABLE_VIEW_INSTANCES         = "GetConfigDetailsForGetPricingVariantTableViewInstances";
  public static final String GET_CONFIG_DETAILS_FOR_CREATE_BULK_CLONE                                = "GetConfigDetailsForCreateBulkClone";
  public static final String GET_CONFIG_DETAILS_FOR_CREATE_KLASS_INSTANCE_CLONE                      = "GetConfigDetailsForCreateKlassInstanceClone";
  
  public static final String GET_CONFIG_DETAILS_FOR_ASSET_INSTANCE_CUSTOM_TAB                        = "GetConfigDetailsForAssetInstanceCustomTab";
  public static final String GET_CONFIG_DETAILS_FOR_ASSET_INSTANCE_OVERVIEW_TAB                      = "GetConfigDetailsForAssetInstanceOverviewTab";
  public static final String GET_CONFIG_DETAILS_FOR_ASSET_INSTANCE_TASKS_TAB                         = "GetConfigDetailsForAssetInstanceTasksTab";
  public static final String GET_CONFIG_DETAILS_FOR_ASSET_INSTANCE_TIMELINE_TAB                      = "GetConfigDetailsForAssetInstanceTimelineTab";
  public static final String GET_CONFIG_INFORMATION_FOR_DOWNLOAD_TRACKER                             = "GetConfigInformationForDownloadTracker";
  public static final String GET_CONFIG_DETAILS_FOR_TAXONOMY_INHEERITANCE                            = "GetConfigDetailsForTaxonomyInheritance";

  public static final String GET_NUMBER_OF_VERSIONS_TO_MAINTAIN                                      = "GetNumberOfVersionsToMaintain";
  
  /************************************** Instance Tree *********************************************/
  public static final String      GET_CONFIG_DETAILS_FOR_GET_FILTER_CHILDREN                         = "GetConfigDetailsForGetFilterChildren";
  public static final String      GET_CONFIG_DETAILS_FOR_GET_GOLDEN_RECORD_BUCKET_INSTANCES          = "GetConfigDetailsForGetGoldenRecordBucketInstances";
  public static final String GET_DATA_LANGUAGE_FOR_KLASS_INSTANCE                                    = "GetDataLanguageForKlassInstance";
  public static final String GET_ALL_DATA_LANGUAGE                                                   = "GetAllDataLanguage";
  
  /**
   * *************************************Migration****************************************
   */
  public static final String CHECK_MIGRATION_EXECUTED                                                = "CheckMigrationExecuted";
  
  public static final String SAVE_MIGRATION                                                          = "SaveMigration";
  public static final String CONFIG_DUMMY_MIGRATION                                                  = "ConfigDummyMigration";
  public static final String ORIENT_MIGRATION_FOR_CODE_COLLATE                                       = "Orient_Migration_FOR_CODE_COLLATE";
  public static final String ORIENT_MIGRATION_TO_DELETE_UNLINKED_RELATIONSHIPS_AND_REFERENCES        = "Orient_Migration_To_Delete_Unlinked_Relationships_And_References";
  public static final String ORIENT_MIGRATION_FOR_THEME_CONFIGURATION                                = "Orient_Migration_Script_Theme_Configuration";
  public static final String ORIENT_MIGRATION_FOR_THEME_CONFIGURATION_19_1_SR                        = "Orient_Migration_Script_Theme_Configuration_For_19_1SR";
  public static final String ORIENT_MIGRATION_FOR_REMOVING_AUTO_GENERATED_ATTRIBUTES                 = "Orient_Migration_For_Removing_AutoGenerated_Attributes";
  
  /**
   * ************************************** Config Import Export
   * ****************************************
   */
  public static final String GET_CONFIG_ENTITY_IDS_BY_ENTITY_TYPE                                    = "GetConfigEntityIdsByEntityType";
  
  /**
   * ************************************** Theme Configuration
   * ****************************************
   */
  public static final String GET_OR_CREATE_THEME_CONFIGURATION                                       = "GetOrCreateThemeConfiguration";
  public static final String GET_ADMIN_CONFIGURATION                                                 = "GetAdminConfiguration";
  public static final String GET_OR_CREATE_VIEW_CONFIGURATION                                        = "GetOrCreateViewConfiguration";
  
  public static final String SAVE_DEFAULT_THEME_CONFIGURATION                                        = "SaveDefaultThemeConfiguration";
  public static final String SAVE_THEME_CONFIGURATION                                                = "SaveThemeConfiguration";
  public static final String GET_THEME_CONFIGURATION                                                 = "GetThemeConfiguration";
  public static final String GET_VIEW_CONFIGURATION                                                  = "GetViewConfiguration";
  public static final String SAVE_VIEW_CONFIGURATION                                                 = "SaveViewConfiguration";
  public static final String SAVE_DEFAULT_VIEW_CONFIGURATION                                         = "SaveDefaultViewConfiguration";
  
  /**
   * ************************************** Smart Document
   * ****************************************
   */
  public static final String GET_OR_CREATE_SMART_DOCUMENT                                            = "GetOrCreateSmartDocument";
  
  public static final String GET_SMART_DOCUMENT                                                      = "GetSmartDocument";
  public static final String SAVE_SMART_DOCUMENT                                                     = "SaveSmartDocument";
  public static final String GET_SMART_DOCUMENT_TEMPLATE                                             = "GetSmartDocumentTemplate";
  public static final String CREATE_SMART_DOCUMENT_TEMPLATE                                          = "CreateSmartDocumentTemplate";
  public static final String DELETE_SMART_DOCUMENT_TEMPLATE                                          = "DeleteSmartDocumentTemplate";
  public static final String SAVE_SMART_DOCUMENT_TEMPLATE                                            = "SaveSmartDocumentTemplate";
  public static final String GET_ALL_SMART_DOCUMENT_TEMPLATE                                         = "GetAllSmartDocumentTemplate";
  
  public static final String GET_SMART_DOCUMENT_PRESET                                               = "GetSmartDocumentPreset";
  public static final String CREATE_SMART_DOCUMENT_PRESET                                            = "CreateSmartDocumentPreset";
  public static final String SAVE_SMART_DOCUMENT_PRESET                                              = "SaveSmartDocumentPreset";
  public static final String DELETE_SMART_DOCUMENT_PRESET                                            = "DeleteSmartDocumentPreset";
  public static final String GET_ALL_SMART_DOCUMENT_PRESET_BY_TEMPLATE                               = "GetAllSmartDocumentPresetByTemplate";
  public static final String GET_SMART_DOCUMENT_CONFIG_INFO                                          = "GetSmartDocumentConfigInfo";
  public static final String GET_ALL_SMART_DOCUMENT_PRESET                                           = "GetAllSmartDocumentPreset";
  
  public static final String GET_CONFIG_DETAILS_WITHOUT_PERMISSIONS_FOR_SMART_DOCUMENT               = "GetConfigDetailsWithoutPermissionsForSmartDocument";
  
  public static final String GET_DEPENDENT_ATTRIBUTES_AND_TECHNICAL_VARIANT_KLASS_IDS                = "GetDependentAttributesAndTechnicalVariantKlassIds";
  
  public static final String GET_CONFIG_DETAILS_TO_FETCH_DATA_SMART_DOCUMENT                         = "GetConfigDetailsToFetchDataForSmartDocument";
  
  /**
   * *********************************************************************************************
   */
  public static final String GET_ROLES_OR_USERS_DATA_BY_PARTNER_ID                                   = "GetRolesOrUsersDataByPartnerId";
  
  /**
   * ************************************** Grid Editable Property List
   * ****************************************
   */
  public static final String SAVE_GRID_EDITABLE_PROPERTY_LIST                                        = "SaveGridEditablePropertyList";
  
  public static final String GET_ALL_GRID_EDITABLE_PROPERTY_LIST_WITH_AVALIABLE_SEQUENCE             = "GetAllGridEditablePropertyListWithAvaliableSequence";
  public static final String GET_ALL_GRID_EDITABLE_PROPERTIES                                        = "GetAllGridEditableProperties";
  
  /**
   * ************************************* SSO Setting
   * ****************************************
   */
  public static final String CREATE_SSO_CONFIGURATION                                                = "CreateSSOConfiguration";
  
  public static final String GET_GRID_SSO_CONFIGURATION                                              = "GetGridSSOConfiguration";
  public static final String DELETE_SSO_CONFIGURATION                                                = "DeleteSSOConfiguration";
  public static final String SAVE_SSO_CONFIGURATION                                                  = "SaveSSOConfiguration";
  
  /**
   * ************************************** CONFIG ENTITY USAGES
   * ****************************************
   */
  public static final String GET_ATTRIBUTE_ENTITY_CONFIGURATION                                      = "GetAttributeEntityConfiguration";
  public static final String GET_TAG_ENTITY_CONFIGURATION                                            = "GetTagEntityConfigurationPlugin";
  public static final String GET_USER_ENTITY_CONFIGURATION                                           = "GetUserEntityConfigurationPlugin";
  public static final String GET_LANGUAGE_ENTITY_CONFIGURATION                                       = "GetLanguageEntityConfiguration";
  public static final String GET_KLASS_ENTITY_CONFIGURATION                                          = "GetKlassEntityConfiguration";
  public static final String GET_TAXONOMY_ENTITY_CONFIGURATION                                       = "GetTaxonomyEntityConfigurationPlugin";
  public static final String GET_CONTEXT_ENTITY_CONFIGURATION                                        = "GetContextEntityConfiguration";
  public static final String GET_PROPERTY_COLLECTION_ENTITY_CONFIGURATION                            = "GetPropertyCollectionEntityConfigurationPlugin";
  public static final String GET_MAPPING_ENTITY_CONFIGURATION                                        = "GetMappingEntityConfiguration";
  public static final String GET_TAB_ENTITY_CONFIGURATION                                            = "GetTabEntityConfiguration";
  public static final String GET_PARTNER_ENTITY_CONFIGURATION                                        = "GetPartnerEntityConfigurationPlugin";
  public static final String GET_RELATIONSHIP_ENTITY_CONFIGURATION                                   = "GetRelationshipEntityConfiguration";
  public static final String GET_RULELIST_ENTITY_CONFIGURATION                                       = "GetRuleListEntityConfiguration";
  public static final String GET_TASK_ENTITY_CONFIGURATION                                           = "GetTaskEntityConfiguration";
  public static final String GET_ICON_ENTITY_CONFIGURATION                                           = "GetIconEntityConfiguration";

  
  /**
   * ****************************** Linked Variant Migration
   * **********************************
   */
  public static final String MIGRATE_LV                                                              = "LinkedVarientCardinilityMigration";
  
  public static final String GET_LV_RELATIONSHIPIDS                                                  = "GetLinkedVarientRelationshipIds";
  

  /************************************** New Instance Tree *********************************************/
  public static final String      GET_CONFIG_DETAILS_FOR_GET_NEW_INSTANCE_TREE                            = "GetConfigDetailsForGetNewInstanceTree";
  public static final String      GET_POST_CONFIG_DETAILS_FOR_NEW_INSTANCE_TREE                           = "GetPostConfigDetailsForNewInstanceTree";
  public static final String      GET_CONFIG_DETAILS_FOR_FILTER_AND_SORT_DATA                             = "GetConfigDetailsForFilterAndSortData";
  
  public static final String      GET_CONFIG_DETAIL_FOR_KLASS_TAXONOMY_TREE                               = "GetConfigDetailForKlassTaxonomyTree";
  public static final String      GET_CONFIG_DETAIL_FOR_RELATIONSHIP_KLASS_TAXONOMY_TREE                  = "GetConfigDetailForRelationshipKlassTaxonomyTree";
  public static final String      GET_CONFIG_DETAIL_FOR_RELATIONSHIP_QUICKLIST                            = "GetConfigDetailsForRelationshipQuicklist";
  public static final String      GET_CONFIG_DETAILS_FOR_GET_RQ_FILTER_AND_SORT_DATA                      = "GetConfigDetailsForGetRQFilterAndSortData";
  public static final String      GET_CONFIG_DETAILS_FOR_GET_RQ_FILTER_CHILDREN                           = "GetConfigDetailsForGetRQFilterChildren";
  public static final String      GET_CONFIG_DETAIL_FOR_ORGANIZE_TREE_DATA                                = "GetConfigDetailForOrganizeTreeData";
  public static final String      GET_CONFIG_DETAILS_FOR_TABLE_VIEW                                       = "GetConfigDetailsForTableView";

  /*********************************
   * Outbound Mapping
   ***********************************/
  public static final String CREATE_OUTBOUND_MAPPING                                               = "CreateOutboundMapping";
  public static final String GET_OUTBOUND_MAPPING                                                  = "GetOutboundMapping";
  public static final String SAVE_OUTBOUND_MAPPING                                                 = "SaveOutboundMapping";
  public static final String GET_OUTBOUND_MAPPING_FOR_EXPORT                                       = "GetOutboundMappingForExport";
  public static final String GET_ATTRIBUTE_TAG_INFO                                                = "GetPropertyGroupInfo";
  

  /****************************** DTP InDesign Server *************************************/
  
  public static final String   GET_ALL_INDS_INSTANCES                                               = "GetAllInDesignServerInstances";
  public static final String   UPDATE_INDS_INSTANCE_DATA                                            = "UpdateInDesignServerInstanceData";
  
  /************************* Select Variant Configuration *******************/
  public static final String GET_VARIANT_CONFIGURATION                                             = "GetVariantConfiguration";
  public static final String GET_OR_CREATE_VARIANT_CONFIGURATION                                   = "GetOrCreateVariantConfiguration";
  public static final String SAVE_VARIANT_CONFIGURATION                                            = "SaveVariantConfiguration";

  /************************* default instace of taxonomy *******************/
  public static final String GET_CONFIG_DETAILS_FOR_DEFAULT_INSTANCE                               = "GetConfigDetailsForDefaultInstance";
  
  public static final String MIGRATE_DEPRECATE_VIRTUAL_CATALOG                                     = "MigrateDeprecateVirtualCatalog";
  
  public static final String GET_KLASSES_AND_TAXONOMY_BY_IDS                                       = "GetKlassesAndTaxonomyByIds";
 
  public static final String GET_ENTITY_COUNT                                                      = "GetEntityCount";
  
}
