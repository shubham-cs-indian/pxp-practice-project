package com.cs.constants;

import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
  
  public static final String       PARAMETERS                                            = "parameters";
  public static final String       DATASOURCES                                           = "dataSources";
  public static final String       DATARULES                                             = "dataRules";
  public static final String       SHEET                                                 = "sheet";
  public static final String       PRIMARYKEYCOLUMN                                      = "primaryKeyColumn";
  public static final String       CLASSINFO                                             = "classInfo";
  
  public static final String       SINGLECLASS                                           = "singleClass";
  public static final String       SINGLERELATIONSHIP                                    = "singleRelationship";
  public static final String       KLASSID                                               = "klassId";
  public static final String       KLASSCOLUMN                                           = "klassColumn";
  public static final String       FILEPATH                                              = "FilePath";
  public static final String       DEV_TEMPLATE                                          = "DEV_TEMPLATE";
  public static final String       IS_MULTI_CLASSIFICATION_ENABLED                       = "isMultiClassificationEnabled";
  public static final String       IS_TAXONOMY_ENABLED                                   = "isTaxonomyEnabled";
  
  public static final String       SECONDARY_CLASS_TYPE                                  = "secondaryClassType";
  public static final String       CLASS_COLUMN                                          = "classColumn";
  public static final String       KLASS_COLUMN                                          = "klassColumn";
  public static final String       SECONDARY_CLASS_COLUMN_NAME                           = "secondaryClassColumnName";
  public static final String       SECONDARY_CLASSES                                     = "secondaryClasses";
  public static final String       TAXONOMY_COLUMN                                       = "taxonomyColumn";
  public static final String       TAXONOMY_ID                                           = "taxonomyId";
  
  public static final String       TREE_ELEMENT_TYPE_ALL                                 = "all";
  public static final String       TREE_ELEMENT_TYPE_FOLDER                              = "folder";
  public static final String       TREE_ELEMENT_TYPE_FILE                                = "file";
  
  public static final String       KLASS_INSTANCE_DIFF_MODELS_EXECUTION_CONTEXT_KEY      = "klassInstanceDiffModels";
  
  public static final String       SUMMARY_COLLECTION                                    = "summarycollection";
  public static final String       SUPPLIER_ATTRIBUTE                                    = "supplierattribute";
  public static final String       STATUS_TAG                                            = "statustag";
  public static final String       REGION_TAG                                            = "regiontag";
  public static final String       TYPE                                                  = "type";
  public static final String       LISTING_STATUS_TAG                                    = "listingstatustag";
  public static final String       LIFE_SATUS_TAG                                        = "lifestatustag";
  public static final String       LIFE_STATUS_INBOX                                     = "life_status_inbox";
  
  public static final String[]     DASHBOARD_SUMMARY_IDS                                 = {
      SUMMARY_COLLECTION };
  public static final String[]     INVENTORY_SUMMARY_IDS                                 = {
      "supplierattribute", "statustag", "regiontag", "type" };
  
  public static final String[]     QUALITY_SUMMARY_IDS                                   = { "red",
      "orange", "yellow", "green" };
  
  public static final String       SUMMARY_COLLECTION_PARENT_ID                          = "summaryCollectionParentId";
  
  public static final String       ALL_MODE                                              = "allmodule";
  public static final String       PIM_MODE                                              = "klass";
  public static final String       ASSET_MODE                                            = "asset";
  public static final String       TARGET_MODE                                           = "target";
  public static final String       TEXT_ASSET_MODE                                       = "textasset";
  public static final String       PROMOTION_MODE                                        = "promotion";
  public static final String       SUPPLIER_MODE                                         = "supplier";
  public static final String       FILES_MODUlE                                          = "filesmodule";
  public static final String       PIM_MODULE                                            = "pimmodule";
  public static final String       MAM_MODULE                                            = "mammodule";
  public static final String       TARGET_MODULE                                         = "targetmodule";
  public static final String       TEXT_ASSET_MODULE                                     = "textassetmodule";
  public static final String       VIRTUAL_CATALOG_MODULE                                = "virtualcatalogmodule";
  public static final String       SUPPLIER_MODULE                                       = "suppliermodule";
  public static final String       ALL_MODULE                                            = "allmodule";
  
  public static final String       PIM                                                   = "pim";
  public static final String       ASSET                                                 = "mam";
  public static final String       TARGET                                                = "target";
  
  public static final String       BASE_KLASS                                            = "baseKlass";
  public static final String       REFERENCED_KLASSES                                    = "referencedKlasses";
  
  public static final String       CHILDREN_MODE                                         = "children";
  public static final String       LINKED_MODE                                           = "linked";
  public static final String       MATCH_MODE                                            = "match";
  
  public static final List<String> TAG_TYPES                                             = Arrays
      .asList(SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID, SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID,
          SystemLevelIds.RANGE_TAG_TYPE_ID, SystemLevelIds.RULER_TAG_TYPE_ID,
          SystemLevelIds.LIFECYCLE_STATUS_TAG_TYPE_ID, SystemLevelIds.LISTING_STATUS_TAG_TYPE_ID,
          SystemLevelIds.STATUS_TAG_TYPE_ID,
          SystemLevelIds.MASTER_TAG_TYPE_ID, SystemLevelIds.BOOLEAN_TAG_TYPE_ID);
  
  public static final int          MAX_COUNT_FOR_INNER_HIT                               = 1000;
  
  public static final String       UNIT_ATTRIBUTE_BASE_TYPE                              = "com.cs.core.config.interactor.entity.attribute.IUnitAttribute";
  
  public static final String       DYNAMIC_COLLECTION_TYPE                               = "dynamicCollection";
  public static final String       STATIC_COLLECTION_TYPE                                = "staticCollection";
  
  public static final String       PROJECT_KLASS_TYPE                                    = "com.cs.core.config.interactor.entity.klass.ProjectKlass";
  public static final String       PROJECT_SET_KLASS_TYPE                                = "com.cs.core.config.interactor.entity.klass.SetKlass";
  public static final String       ASSET_KLASS_TYPE                                      = "com.cs.core.config.interactor.entity.klass.Asset";
  public static final String       MARKET_KLASS_TYPE                                     = "com.cs.core.config.interactor.entity.klass.Market";
  public static final String       TEXT_ASSET_KLASS_TYPE                                 = "com.cs.core.config.interactor.entity.textasset.TextAsset";
  public static final String       SUPPLIER_KLASS_TYPE                                   = "com.cs.core.config.interactor.entity.supplier.Supplier";
  public static final String       ARTICLE_INSTANCE_MODULE_ENTITY                        = "ArticleInstance";
  public static final String       ASSET_INSTANCE_MODULE_ENTITY                          = "AssetInstance";
  public static final String       MARKET_INSTANCE_MODULE_ENTITY                         = "MarketInstance";
  public static final String       TEXT_ASSET_INSTANCE_MODULE_ENTITY                     = "TextAssetInstance";
  public static final String       SUPPLIER_INSTANCE_MODULE_ENTITY                       = "SupplierInstance";
  public static final String       FILE_INSTANCE_MODULE_ENTITY                           = "FileInstance";
  
  public static final String       REQUEST_HEADER_AUTH_TOKEN                             = "X-Auth-Token";
  
  public static final String       REQUEST_HEADER_OBJECT_META_FORMAT                     = "X-Object-Meta-Format";
  
  public static final String       REQUEST_HEADER_OBJECT_META_THUMB                      = "X-Object-Meta-Thumb";
  
  // constants from elasticsearch
  public static final String       INDEX_NAME                                            = "cs";
  public static final String       ARCHIVE_INDEX_NAME                                    = "csarchive";
  public static final String       IMPORT_INDEX_NAME                                     = "staging1";
  
  public static final String       MASTER_ARTICLE_INSTANCE_DOC_TYPE                      = "masterarticleinstance";
  public static final String       MASTER_ARTICLE_INSTANCE_VERSION_DOC_TYPE              = "masterarticleinstanceversion";
  
  public static final String       IMPORT_SYSTEM_ARTICLE_INSTANCE_DOC_TYPE               = "importsysteminstance";
  public static final String       IMPORT_SYSTEM_ARTICLE_INSTANCE_VERSION_DOC_TYPE       = "importsysteminstanceversion";
  
  public static final String       IMPORT_SYSTEM_INSTANCE_DOC_TYPE                       = "importsysteminstance";
  
  public static final String       ARTICLE_INSTANCE_SEARCHABLE_DOC_TYPE                  = "articleinstancesearchablecache";
  public static final String       ASSET_INSTANCE_SEARCHABLE_DOC_TYPE                    = "assetinstancesearchablecache";
  public static final String       MARKET_INSTANCE_SEARCHABLE_DOC_TYPE                   = "marketinstancesearchablecache";
  public static final String       SUPPLIER_INSTANCE_SEARCHABLE_DOC_TYPE                 = "supplierinstancesearchablecache";
  public static final String       TEXTASSET_INSTANCE_SEARCHABLE_DOC_TYPE                = "textassetinstancesearchablecache";
  
  public static final String       ATTACHMENT_INSTANCE_SEARCHABLE_DOC_TYPE               = "attachmentinstancesearchablecache";
  public static final String       FILE_INSTANCE_SEARCHABLE_DOC_TYPE                     = "fileinstancesearchablecache";
  public static final String       ARTICLE_KLASS_INSTANCE_DOC_TYPE                       = "klassinstancecache";
  // public static final String ARTICLE_KLASS_INSTANCE_VERSION_DOC_TYPE =
  // "klassinstancecache";
  public static final String       SET_KLASS_INSTANCE_DOC_TYPE                           = "klassinstancesetcache";
  public static final String       SET_KLASS_INSTANCE_VERSION_DOC_TYPE                   = "klassinstancesetversioncache";
  public static final String       ASSET_INSTANCE_DOC_TYPE                               = "assetinstancecache";
  // public static final String ASSET_INSTANCE_VERSION_DOC_TYPE =
  // "assetinstancecache";
  public static final String       GOLDEN_RECORD_RULE_BUCKET_DOC_TYPE                    = "goldenrecordrulebucketcache";
  
  public static final String       ATTRIBUTE_DOC_TYPE                                    = "attributecache";
  // public static final String ATTRIBUTE_VERSION_DOC_TYPE =
  // "attributecache";
  public static final String       TAG_DOC_TYPE                                          = "tagcache";
  public static final String       TAG_VERSION_DOC_TYPE                                  = "tagversioncache";
  public static final String       ROLE_VERSION_DOC_TYPE                                 = "roleversioncache";
  public static final String       ROLE_DOC_TYPE                                         = "rolecache";
  public static final String       AUTO_GENERATED_ATTRIBUTE_INFO_DOC_TYPE                = "autogeneratedattributeinfocache";
  
  public static final String       ARTICLE_KLASS_STRUCTURE_DOC_TYPE                      = "structure";
  public static final String       ARTICLE_KLASS_STRUCTURE_VERSION_DOC_TYPE              = "structureVersion";
  public static final String       SET_KLASS_PROPERTY_VERSION_DOC_TYPE                   = "setklasspropertyversion";
  public static final String       TASK_KLASS_PROPERTY_VERSION_DOC_TYPE                  = "taskklasspropertyversion";
  public static final String       MARKET_TARGET_INSTANCE_DOC_TYPE                       = "targetinstancemarketcache";
  // public static final String MARKET_TARGET_INSTANCE_VERSION_DOC_TYPE =
  // "targetinstancemarketcache";
  public static final String       ATTACHMENT_INSTANCE_VERSION_DOC_TYPE                  = "attachmentinstanceversioncache";

  public static final String       TASK_INSTANCE_DOC_TYPE                                = "taskinstancecache";
  // public static final String TASK_INSTANCE_VERSION_CACHE =
  // "taskinstanceversioncache";
  public static final String       RELATIONSHIP_INSTANCE_DOC_TYPE                        = "relationshipinstancecache";
  // public static final String RELATIONSHIP_INSTANCE_VERSION_DOC_TYPE =
  // "relationshipinstancecache";
  public static final String       NATURE_RELATIONSHIP_INSTANCE_DOC_TYPE                 = "naturerelationshipinstancecache";
  // public static final String NATURE_RELATIONSHIP_INSTANCE_VERSION_DOC_TYPE =
  // "naturerelationshipinstancecache";
  public static final String       RELATIONSHIP_OBJECT_DOC_TYPE                          = "relationshipobjectcache";
  // public static final String RELATIONSHIP_OBJECT_VERSION_DOC_TYPE =
  // "relationshipobjectcache";
  
  public static final List<String> RELATIONSHIP_DOC_TYPES_LIST                           = Arrays
      .asList(Constants.RELATIONSHIP_INSTANCE_DOC_TYPE,
          Constants.NATURE_RELATIONSHIP_INSTANCE_DOC_TYPE);
  
  public static final List<String> DOC_TYPES_LIST                                        = Arrays
      .asList(Constants.ARTICLE_KLASS_INSTANCE_DOC_TYPE, Constants.ASSET_INSTANCE_DOC_TYPE,
          Constants.MARKET_TARGET_INSTANCE_DOC_TYPE,
          Constants.IMPORT_SYSTEM_ARTICLE_INSTANCE_DOC_TYPE, Constants.TEXTASSET_INSTANCE_DOC_TYPE,
          Constants.SUPPLIER_INSTANCE_DOC_TYPE, Constants.PROMOTION_INSTANCE_DOC_TYPE);
  
  public static final String       TEXTASSET_INSTANCE_DOC_TYPE                           = "textassetinstancecache";
  // public static final String TEXTASSET_INSTANCE_VERSION_DOC_TYPE =
  // "textassetinstancecache";
  
  // public static final String VIRTUAL_CATALOG_INSTANCE_VERSION_DOC_TYPE =
  // "virtualcataloginstancecache";
  
  public static final String       PROMOTION_INSTANCE_DOC_TYPE                           = "promotioninstancecache";
  // public static final String PROMOTION_INSTANCE_VERSION_DOC_TYPE =
  // "promotioninstancecache";
  
  public static final String       SUPPLIER_INSTANCE_DOC_TYPE                            = "supplierinstancecache";
  public static final String       FILE_INSTANCE_DOC_TYPE                                = "fileinstancecache";
  // public static final String SUPPLIER_INSTANCE_VERSION_DOC_TYPE =
  // "supplierinstancecache";
  // public static final String FILE_INSTANCE_VERSION_DOC_TYPE =
  // "fileinstancecache";
  
  public static final String       ASSET_STATUS_DOC_TYPE                                 = "assetuploadstatus";
  public static final String       PROCESS_INSTANCE_CACHE                                = "processinstancecache";
  public static final String       PROCESS_INSTANCE_DASHBOARD_CACHE                      = "processinstancedashboardcache";
  public static final String       PROCESS_INSTANCE_DASHBOARD_VERSION_DOC_TYPE           = "processinstancedashboardversioncache";
  
  /**
   * ****************************************************** BASE_TYPE
   * ************************************************************
   */
  public static final String       EVENT_INSTANCE_BASE_TYPE                              = "com.cs.core.runtime.interactor.entity.eventinstance.EventInstance";
  
  public static final String       ARTICLE_INSTANCE_BASE_TYPE                            = "com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance";
  public static final String       SET_INSTANCE_BASE_TYPE                                = "com.cs.runtime.interactor.entity.KlassInstanceSet";
  public static final String       ASSET_INSTANCE_BASE_TYPE                              = "com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance";
  public static final String       MARKET_INSTANCE_BASE_TYPE                             = "com.cs.core.runtime.interactor.entity.klassinstance.MarketInstance";
  
  public static final String       TEXTASSET_INSTANCE_BASE_TYPE                          = "com.cs.core.runtime.interactor.entity.textassetinstance.TextAssetInstance";
  public static final String       SUPPLIER_INSTANCE_BASE_TYPE                           = "com.cs.core.runtime.interactor.entity.supplierinstance.SupplierInstance";
  public static final String       FILE_INSTANCE_BASE_TYPE                               = "com.cs.core.runtime.interactor.entity.fileinstance.OnboardingFileInstance";
  
  public static final String       IMAGE_ATTRIBUTE_INSTANCE_TYPE                         = "com.cs.core.runtime.interactor.entity.propertyinstance.ImageAttributeInstance";
  public static final String       ASSET_ATTRIBUTE_INSTANCE_TYPE                         = "com.cs.core.runtime.interactor.entity.klassinstance.AssetAttributeInstance";
  public static final String       CLASS_FRAME_STRUCTURE_TYPE                            = "com.cs.core.config.interactor.entity.visualattribute.ClassFrameStructure";
  public static final String       ATTRIBUTE_INSTANCE_PROPERTY_TYPE                      = "com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance";
  public static final String       TAG_INSTANCE_PROPERTY_TYPE                            = "com.cs.core.runtime.interactor.entity.tag.TagInstance";
  public static final String       ROLE_INSTANCE_PROPERTY_TYPE                           = "com.cs.core.runtime.interactor.entity.role.RoleInstance";
  public static final String       OWNER_ATTRIBUTE_TYPE                                  = "com.cs.config.interactor.entity.concrete.attribute.mandatory.OwnerAttribute";
  public static final String       NAME_ATTRIBUTE_TYPE                                   = "com.cs.core.config.interactor.entity.standard.attribute.NameAttribute";
  public static final String       SET_ATTRIBUTE_INSTANCE_PROPERTY_TYPE                  = "com.cs.runtime.interactor.entity.SetAttributeInstance";
  public static final String       USER_BASE_TYPE                                        = "com.cs.core.config.interactor.entity.user.User";
  public static final String       ASSET_METADATA_ATTRIBUTE_TYPE                         = "com.cs.core.config.interactor.entity.standard.attribute.AssetMetadataAttribute";
  
  public static final String       COVER_FLOW_IMAGE_ATTRIBUTE_ID                         = "imageCoverflowattribute";
  public static final String       TYPE_ATTRIBUTE                                        = "typeattribute";
  public static final String       LIST_PRICE_ATTRIBUTE                                  = "listpriceattribute";
  public static final String       SELLING_PRICE_ATTRIBUTE                               = "sellingpriceattribute";
  public static final String       DESCRIPTION_ATTRIBUTE                                 = "longdescriptionattribute";
  
  public static final String       FILTER_MATCH_MODEL_BASE_TYPE                          = "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel";
  public static final String       FILTER_RANGE_MODEL_BASE_TYPE                          = "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel";
  public static final String       EVENTINSTANCE_CACHE                                   = "eventinstancecache";
  
  public static final String       MASTER_IMPORT_ARTICLE_KLASS_TYPE                      = "com.cs.imprt.config.interactor.entity.concrete.klass.MasterImportArticle";
  public static final String       IMPORT_SYSTEM_FILE_ARTICLE_KLASS_TYPE                 = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportSystemFileArticle";
  public static final String       IMPORT_SYSTEM_INSTANCE_KLASS_TYPE                     = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportSystem";
  public static final String       IMPORT_SYSTEM_FILE_KLASS_TYPE                         = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportFile";
  public static final String       IMPORT_ARTICLE_KLASS_TYPE                             = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportArticle";
  public static final String       IMPORT_SYSTEM_INSTANCE_BASE_TYPE                      = "com.cs.imprt.runtime.interactor.entity.importsystem.ImportSystemInstance";
  public static final String       MASTER_ARTICLE_INSTANCE_BASE_TYPE                     = "com.cs.imprt.runtime.interactor.entity.importsystem.MasterArticleInstance";
  
  public static final String[]     TREE_FIELDS_TO_FETCH                                  = new String[] {
      "id", "name", "type", "owner", "versionId", "roles", "parentId", "klassInstanceIds",
      "setInstanceIds", "isFolder" };
  
  public static final String[]     CHILD_INFORMATION_FIELDS_TO_FETCH                     = new String[] {
      "id", "name", "roles", "tags", "types", "code", "taxonomyIds", "owner", "createdBy",
      "createdOn", "branchNo", "branchOf", "variantId", "variantOf", "versionId", "baseType",
      "versionTimestamp", "lastModifiedBy", "assets", "lastModified", "isFolder", "parentId",
      "attributes", "messages", "defaultAssetInstanceId", "versionOf", "languageCodes",
      "klassInstanceId", "originalInstanceId", "creationLanguage" };
  
  // TODO: Use System Level Id's
  @Deprecated
  // TODO: Use System Level Id's
  public static final String       IMAGE_COVERFLOW_ATTRIBUTE_ID                          = "imageCoverflowattribute";
  
  public static final String       ASSET_COVERFLOW_ATTRIBUTE_ID                          = "assetcoverflowattribute";
  public static final Object       SET_TAG_INSTANCE_PROPERTY_TYPE                        = "com.cs.runtime.interactor.entity.SetTagInstance";
  
  public static final String       DASHBOARD_SUMMARY_DOC_TYPE                            = "dashboardsummary";
  public static final String       DASHBOARD_SUMMARY_ID                                  = "1";
  public static final String       RULE_VIOLATION_SUMMARY_KEY                            = "summaryForRuleViolation";
  public static final String       RULES                                                 = "rules";
  
  public static final String       DATE_ATTRIBUTE_BASE_TYPE                              = "com.cs.core.config.interactor.entity.attribute.DateAttribute";
  public static final String       CREATED_ON_ATTRIBUTE_BASE_TYPE                        = "com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute";
  public static final String       DUE_DATE_ATTRIBUTE_BASE_TYPE                          = "com.cs.config.interactor.entity.concrete.attribute.standard.DueDateAttribute";
  public static final String       LAST_MODIFIED_ATTRIBUTE_BASE_TYPE                     = "com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute";
  public static final String       IMAGE_COVER_FLOW_ATTRIBUTE_BASE_TYPE                  = "com.cs.core.config.interactor.entity.standard.attribute.ImageCoverflowAttribute";
  public static final String       TEXT_ATTRIBUTE_BASE_TYPE                              = "com.cs.core.config.interactor.entity.attribute.TextAttribute";
  public static final String       COVERFLOW_ATTRIBUTE_BASE_TYPE                         = "com.cs.core.config.interactor.entity.attribute.CoverflowAttribute";
  
  public static final String       QUICK_LIST_MODE_CHILDREN                              = "children";
  public static final String       QUICK_LIST_MODE_LINKED                                = "linked";
  public static final String       QUICK_LIST_MODE_MATCH                                 = "match";
  
  public static final String       INSTANCE_TAG                                          = "instance_tag";
  public static final String       RELEVANCE                                             = "relevance";
  public static final String       TAGS                                                  = "tags";
  public static final String       INNER_TAG                                             = "innerTag";
  public static final String       TAG_VALUES                                            = "tagValues";
  public static final String       INSTANCE_TAXONOMIES                                   = "instance_taxonomies";
  public static final String       TAXONOMIES                                            = "taxonomies";
  public static final String       TAG                                                   = "tag";
  
  public static final String       INSTANCE_ATTRIBUTE                                    = "instance_attribute";
  public static final String       ATTRIBUTE                                             = "attribute";
  public static final String       RANGE_ATTRIBUTE                                       = "range_attribute";
  public static final String       ATTRIBUTES                                            = "attributes";
  public static final String       CURRENT_USER_ID                                       = "currentUserId";
  public static final String       EXTENDED_STATS                                        = "extended_stats";
  
  public static final String       SET_OF_PRODUCTS                                       = "setOfProducts";
  public static final String       FIXED_BUNDLE                                          = "fixedBundle";
  public static final String       PID                                                   = "pidSku";
  public static final String       STANDARD_IDENTIFIER                                   = "gtin";
  public static final String       EMBEDDED                                              = "embedded";
  public static final String       LANGUAGE                                              = "language";
  public static final String       INDIVIDUAL_ARTICLE                                    = "singleArticle";
  
  public static final String       LINKED_VARIANT                                        = "productVariant";
  public static final String       ATTRIBUTE_CONTEXT                                     = "attributeVariantContext";
  public static final String       RELATIONSHIP_VARIANT                                  = "relationshipVariant";
  public static final String       GTIN_CONTEXT                                          = "gtinVariant";
  public static final String       EMBEDDED_CONTEXT                                      = "contextualVariant";
  public static final String       LANGUAGE_CONTEXT                                      = "languageVariant";
  
  public static final List<String> CONTEXTS_TYPES_LIST                                   = Arrays
      .asList(LINKED_VARIANT, ATTRIBUTE_CONTEXT, RELATIONSHIP_VARIANT, GTIN_CONTEXT,
          EMBEDDED_CONTEXT, LANGUAGE_CONTEXT);
  // image resolution mapping
  public static final String       RESOLUTION_300dpi                                     = "300";
  public static final String       RESOLUTION_150dpi                                     = "150";
  public static final String       RESOLUTION_72dpi                                      = "72";
  // Have to remove these entries
  public static final String       RESOLUTION_720pHD                                     = "1280x720";
  public static final String       RESOLUTION_1080pFHD                                   = "1920x1080";
  public static final String       RESOLUTION_1440pQHD                                   = "2560x1440";
  public static final String       RESOLUTION_2160p4K                                    = "3840x2160";
  public static final String       RESOLUTION_4380p8K                                    = "7680x4380";
  
  public static final List<String> dateTypeConstants                                     = new ArrayList<String>(
      Arrays.asList(DATE_ATTRIBUTE_BASE_TYPE, CREATED_ON_ATTRIBUTE_BASE_TYPE,
          DUE_DATE_ATTRIBUTE_BASE_TYPE, LAST_MODIFIED_ATTRIBUTE_BASE_TYPE));
  
  public static final List<String> propertiesToExcludeForPermissionCheck                 = new ArrayList<>();
  
  public static final String       CALCULATED_ATTRIBUTE_TYPE                             = "com.cs.core.config.interactor.entity.attribute.CalculatedAttribute";
  
  public static final List<String> DEFAULT_ATTRIBUTE_IDS                                 = Arrays
      .asList(SystemLevelIds.NAME_ATTRIBUTE, SystemLevelIds.CREATED_BY_ATTRIBUTE,
          SystemLevelIds.CREATED_ON_ATTRIBUTE, SystemLevelIds.LAST_MODIFIED_ATTRIBUTE,
          SystemLevelIds.LAST_MODIFIED_BY_ATTRIBUTE, SystemLevelIds.ASSET_COVERFLOW_ATTRIBUTE);
  
  public static final String       TAG_FILTERS                                           = "tagFilters";
  
  public static final String       CONCATENATED_ATTRIBUTE_TYPE                           = "com.cs.core.config.interactor.entity.attribute.ConcatenatedAttribute";
  
  public static final String       ARTICLE_NATURE_TYPE                                   = "singleArticle";
  
  public static final List<String> DEFAULT_TAG_IDS                                       = Arrays
      .asList(SystemLevelIds.LIFE_STATUS_TAG_ID, SystemLevelIds.LISTING_STATUS_TAG_ID);
  
  public static final String       RUNTIME_MIGRATION_DOC_TYPE                            = "runtimemigrationcache";
  public static final String       KLASS_INSTANCE_STATISTICS_DOCTYPE                     = "klassinstancestatisticscache";
  public static final String       ASSET_INSTANCE_STATISTICS_DOCTYPE                     = "assetinstancestatisticscache";
  public static final String       MARKET_INSTANCE_STATISTICS_DOCTYPE                    = "marketinstancestatisticscache";
  public static final String       TEXTASSET_INSTANCE_STATISTICS_DOCTYPE                 = "textassetinstancestatisticscache";
  public static final String       SUPPLIER_INSTANCE_STATISTICS_DOCTYPE                  = "supplierinstancestatisticscache";
  public static final String       ORAGANIZATION_INTERNAL_TYPE                           = "internal";
  
  public static final String       ONBOARDING_CATALOG_IDS                                = "onboarding";
  public static final String       OFFBOARDING_CATALOG_IDS                               = "offboarding";
  public static final String       PIM_CATALOG_IDS                                       = "pim";
  public static final String       DATA_INTEGRATION_CATALOG_IDS                          = "dataIntegration";
  
  public static final String       PIM_PORTAL_ID                                         = "pim";
  
  public static final String       PORTALS                                               = "portals";
  
  public static final List<String> PHYSICAL_CATALOG_IDS                                  = Arrays
      .asList(Constants.ONBOARDING_CATALOG_IDS, Constants.OFFBOARDING_CATALOG_IDS,
          Constants.PIM_CATALOG_IDS, Constants.DATA_INTEGRATION_CATALOG_IDS);
  
  public static final List<String> PORTALS_IDS                                           = Arrays
      .asList(Constants.PIM_PORTAL_ID);
  
  public static final String       ENGLISH_US                                            = "en_US";
  public static final String       ENGLISH_UK                                            = "en_UK";
  public static final String       GERMAN                                                = "de_DE";
  public static final String       FRENCH                                                = "fr_FR";
  public static final String       SPANISH                                               = "es_ES";
  
  public static final List<String> SUPPORTED_LANGUAGES                                   = Arrays
      .asList(ENGLISH_US, GERMAN, FRENCH, SPANISH);
  
  public static final String       GLOBAL_PERMISSION_INDEX                               = "globalPermissionIndex";
  public static final String       PROPERTY_COLLECTION_CAN_READ_PERMISSION_INDEX         = "propertyCollectionCanReadPermissionIndex";
  public static final String       PROPERTY_COLLECTION_CAN_EDIT_PERMISSION_INDEX         = "propertyCollectionCanEditPermissionIndex";
  public static final String       PROPERTY_CAN_READ_PERMISSION_INDEX                    = "propertyCanReadPermissionIndex";
  public static final String       PROPERTY_CAN_EDIT_PERMISSION_INDEX                    = "propertyCanEditPermissionIndex";
  public static final String       RELATIONSHIP_CAN_READ_PERMISSION_INDEX                = "relationshipCanReadPermissionIndex";
  public static final String       RELATIONSHIP_CAN_ADD_PERMISSION_INDEX                 = "relationshipCanAddPermissionIndex";
  public static final String       RELATIONSHIP_CAN_DELETE_PERMISSION_INDEX              = "relationshipCanDeletePermissionIndex";
  public static final String       HEADER_PERMISSION_INDEX                               = "headerPermissionIndex";
  public static final String       GLOBAL_CAN_CREATE_PERMISSIONS_INDEX                   = "globalCanCreatePermissionsIndex";
  public static final String       GLOBAL_CAN_DELETE_PERMISSIONS_INDEX                   = "globalCanDeletePermissionsIndex";
  public static final String       GLOBAL_CAN_EDIT_PERMISSIONS_INDEX                     = "globalCanEditPermissionsIndex";
  public static final String       GLOBAL_CAN_READ_PERMISSIONS_INDEX                     = "globalCanReadPermissionsIndex";
  public static final String       TEMPLATE_PERMISSION_INDEX                             = "templatePermissionIndex";
  public static final String       ASSET_CAN_DOWNLOAD_PERMISSIONS_INDEX                  = "assetCanDownloadPermissionsIndex";
  
  public static final List<String> DEFAULT_RUNTIME_TABS                                  = Arrays
      .asList(SystemLevelIds.TASK_TAB, SystemLevelIds.TIMELINE_TAB,
          SystemLevelIds.OVERVIEW_TAB);
  
  public static final String       UPLOAD_SUMMARY                                        = "uploadSummary";
  public static final String       ALL_UPLOADS                                           = "allUploads";
  public static final String       LAST_UPLOAD                                           = "lastUpload";
  public static final String       INBOUND_SUMMARY                                       = "inboundSummary";
  public static final String       INBOUND_LAST_UPLOAD                                   = "inboundLastUpload";
  public static final String       OUTBOUND                                              = "outbound";
  
  public static final String       SAVEARTIKLE                                           = "SAVEARTIKLE";
  
  public static final String       HEIRARCHY_TAXONOMY                                    = "Heirarchy_Taxonomy";
  public static final String       MASTER_TAXONOMY                                       = "Master_Taxonomy";
  
  public static final String       MAJOR_TAXONOMY                                        = "majorTaxonomy";
  public static final String       MINOR_TAXONOMY                                        = "minorTaxonomy";
  
  public static final String       TEMPLATE_MODULE                                       = "templatemodule";
  public static final String       DATE_ATTRIBUTE                                        = "DATE";
  public static final String       MODIFY_ATTRIBUTE                                      = "modifytime";
  
  /**
   * ************************************************************************************************************************
   */
  public static final String       OUTBOUND_ENDPOINT                                     = "offboardingendpoint";
  
  public static final String       INBOUND_ENDPOINT                                      = "onboardingendpoint";
  
  public static final List<String> ENDPOINT_TYPE_LIST                                    = Arrays
      .asList(OUTBOUND_ENDPOINT, INBOUND_ENDPOINT);
  
  public static final String       CLASSIFICATION                                        = "classification";
  public static final String       STANDARD_AND_NORMALIZATION                            = "standard_and_normalization";
  public static final String       VIOLATION                                             = "violation";
  
  public static final List<String> Rule_TYPE_LIST                                        = Arrays
      .asList(CLASSIFICATION, STANDARD_AND_NORMALIZATION, VIOLATION);
  
  public static final String       SHARED                                                = "shared";
  public static final String       PERSONAL                                              = "personal";
  
  public static final List<String> TASK_TYPE_LIST                                        = Arrays
      .asList(SHARED, PERSONAL);
  
  public static final String       RANGE                                                 = "range";
  public static final String       JUST_IN_TIME                                          = "justInTime";
  
  public static final String       COLLECTION_DOCTYPE                                    = "collectioncache";
  
  public static final String       SUPPLIERS_ORGANIZATION                                = "suppliers";
  public static final String       MARKETPLACES_ORGANIZATION                             = "marketplaces";
  public static final String       DISTRIBUTORS_ORGANIZATION                             = "distributors";
  public static final String       WHOLESALERS_ORGANIZATION                              = "wholesalers";
  public static final String       TRANSLATION_AGENCY_ORGANIZATION                       = "translation_agency";
  public static final String       CONTENT_ENRICHMENT_AGENCY_ORGANIZATION                = "content_enrichment_agency";
  public static final String       DIGITAL_ASSET_AGENCY_ORGANIZATION                     = "digital_asset_agency";
  
  public static final List<String> ORGANISATION_TYPE_LIST                                = Arrays
      .asList(SUPPLIERS_ORGANIZATION, MARKETPLACES_ORGANIZATION, DISTRIBUTORS_ORGANIZATION,
          WHOLESALERS_ORGANIZATION, TRANSLATION_AGENCY_ORGANIZATION,
          CONTENT_ENRICHMENT_AGENCY_ORGANIZATION, DIGITAL_ASSET_AGENCY_ORGANIZATION);
  
  public static final String       STATISTICS_SUFFIX                                     = "__Stats";
  public static final String       LAST_MODIFIED                                         = "lastModified";
  
  public static final String       ATTACHMENTINSTANCE_CACHE                              = "attachmentinstancecache";
  // public static final String ATTACHMENTINSTANCEVERSION_CACHE =
  // "attachmentinstancecache";
  
  public static final List<String> DOC_TYPES_LIST_CONTAINS_ATTRIBUTES                    = Arrays
      .asList(FILE_INSTANCE_DOC_TYPE, ASSET_INSTANCE_DOC_TYPE, ARTICLE_KLASS_INSTANCE_DOC_TYPE,
          ATTACHMENTINSTANCE_CACHE,
          GOLDEN_RECORD_RULE_BUCKET_DOC_TYPE, IMPORT_SYSTEM_INSTANCE_DOC_TYPE,
          MARKET_TARGET_INSTANCE_DOC_TYPE,
          PROMOTION_INSTANCE_DOC_TYPE, RELATIONSHIP_OBJECT_DOC_TYPE,
          SUPPLIER_INSTANCE_DOC_TYPE,
          TEXTASSET_INSTANCE_DOC_TYPE);
  
  public static final List<String> SERCHABLE_TYPES_LIST_CONTAINS_ATTRIBUTES              = Arrays
      .asList(FILE_INSTANCE_SEARCHABLE_DOC_TYPE, ASSET_INSTANCE_SEARCHABLE_DOC_TYPE,
          ARTICLE_INSTANCE_SEARCHABLE_DOC_TYPE, ATTACHMENT_INSTANCE_SEARCHABLE_DOC_TYPE,
          MARKET_INSTANCE_SEARCHABLE_DOC_TYPE,
          SUPPLIER_INSTANCE_SEARCHABLE_DOC_TYPE, TEXTASSET_INSTANCE_SEARCHABLE_DOC_TYPE);
  
  public static final List<String> STATISTICS_DOCTYPES_LIST                              = Arrays
      .asList(KLASS_INSTANCE_STATISTICS_DOCTYPE, ASSET_INSTANCE_STATISTICS_DOCTYPE,
          MARKET_INSTANCE_STATISTICS_DOCTYPE,
          SUPPLIER_INSTANCE_STATISTICS_DOCTYPE,
          TEXTASSET_INSTANCE_STATISTICS_DOCTYPE);
  
  public static final String       SEARCHABLE_SUFFIX                                     = "__Search";
  
  public static final List<String> NATURE_TYPES_LIST_FOR_KLASS                           = Arrays
      .asList(INDIVIDUAL_ARTICLE, FIXED_BUNDLE,
          STANDARD_IDENTIFIER, SET_OF_PRODUCTS, PID, EMBEDDED, LANGUAGE);
  
  public static final List<String> TAXONOMY_TYPE_LIST_FOR_TAXONOMY                       = Arrays
      .asList(MAJOR_TAXONOMY, MINOR_TAXONOMY);
  
  public static final Integer      EXTREME_SIZE_LIMIT                                    = 999999;
  
  public static final List<String> PIM_PORTAL_BASE_TYPES                                 = Arrays
      .asList(Constants.ARTICLE_INSTANCE_BASE_TYPE, Constants.ASSET_INSTANCE_BASE_TYPE,
          Constants.TEXTASSET_INSTANCE_BASE_TYPE, Constants.SUPPLIER_INSTANCE_BASE_TYPE,
          Constants.MARKET_INSTANCE_BASE_TYPE);
  
  public static final String       TASK                                                  = "task";
  
  public static final String       HTML_FRAME_STRUCTURE                                  = "com.cs.core.config.interactor.entity.visualattribute.HTMLFrameStructure";
  public static final String       IMAGE_FRAME_STRUCTURE                                 = "com.cs.core.config.interactor.entity.visualattribute.ImageFrameStructure";
  public static final String       CONTAINER_FRAME_STRUCTURE                             = "com.cs.core.config.interactor.entity.visualattribute.ContainerFrameStructure";
  public static final String       HTML_VISUAL_ATTRIBUTE_FRAME_STRUCTURE                 = "com.cs.core.config.interactor.entity.visualattribute.HTMLVisualAttributeFrameStructure";
  public static final String       IMAGE_VISUAL_ATTRIBUTE_FRAME_STRUCTURE                = "com.cs.core.config.interactor.entity.visualattribute.ImageVisualAttributeFrameStructure";
  public static final String       STANDARD_ORGANIZATION                                 = "-1";

  public static final List<String>      partnerTypes = Arrays.asList(
      Constants.SUPPLIERS_ORGANIZATION, Constants.MARKETPLACES_ORGANIZATION,
      Constants.DISTRIBUTORS_ORGANIZATION, Constants.WHOLESALERS_ORGANIZATION,
      Constants.TRANSLATION_AGENCY_ORGANIZATION, Constants.CONTENT_ENRICHMENT_AGENCY_ORGANIZATION,
      Constants.DIGITAL_ASSET_AGENCY_ORGANIZATION);

  public static final List<String>       standardTags = List.of(Constants.LIFE_SATUS_TAG, Constants.LISTING_STATUS_TAG);
  public static final List<PropertyType> relationType = List.of(PropertyType.RELATIONSHIP, PropertyType.NATURE_RELATIONSHIP);

}
