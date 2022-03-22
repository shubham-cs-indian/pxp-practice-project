package com.cs.constants;

import java.util.Arrays;
import java.util.List;

/**
 * The exact replica of this class is in Neo4j project (Plugin) inside package "com.cs.config.strategy.plugin.constants"
 * Any changes in this class need to be done in the above mentioned class as well
 *
 * @author CS31
 */
public class SystemLevelIds {
  
  public static final String       ADMIN                                                                    = "admin";
  
  public static final String       IMAGE_COVERFLOW_ATTRIBUTE_FOR_MAM                                        = "IMAGE_COVERFLOW_ATTRIBUTE_FOR_MAM";
  public static final String       IMAGE_COVERFLOW_ATTRIBUTE_FOR_OTHERS                                     = "IMAGE_COVERFLOW_ATTRIBUTE_FOR_OTHERS";
  
  public static final String       PROJECT_KLASS                                                            = "com.cs.core.config.interactor.entity.klass.ProjectKlass";
  public static final String       TASK_KLASS_KLASS                                                         = "com.cs.core.config.interactor.entity.task.Task";
  public static final String       ASSET_KLASS                                                              = "com.cs.core.config.interactor.entity.klass.Asset";
  public static final String       TARGET_KLASS                                                             = "com.cs.config.interactor.entity.concrete.klass.Target";
  public static final String       MARKET_KLASS                                                             = "com.cs.core.config.interactor.entity.klass.Market";
  public static final String       RELATIONSHIP                                                             = "com.cs.core.config.interactor.entity.relationship.Relationship";
  
  public static final String       PROPERTY_TYPE_ATTRIBUTE                                                  = "attribute";
  public static final String       PROPERTY_TYPE_ROLE                                                       = "role";
  public static final String       PROPERTY_TYPE_TAG                                                        = "tag";
  public static final String       PROPERTY_TYPE_RELATIONSHIP                                               = "relationship";
  public static final String       PROPERTY_TYPE_TAXONOMY                                                   = "taxonomy";
  public static final String       PROPERTY_TYPE_REFERENCE                                                  = "reference";
  
  // Attributes
  public static final String       NAME_ATTRIBUTE                                                           = "nameattribute";
  public static final String       FIRST_NAME_ATTRIBUTE                                                     = "firstnameattribute";
  public static final String       LAST_NAME_ATTRIBUTE                                                      = "lastnameattribute";
  public static final String       DUE_DATE_ATTRIBUTE                                                       = "duedateattribute";
  public static final String       START_DATE_ATTRIBUTE                                                     = "schedulestartattribute";
  public static final String       END_DATE_ATTRIBUTE                                                       = "scheduleendattribute";
  public static final String       CREATED_ON_ATTRIBUTE                                                     = "createdonattribute";
  public static final String       ASSET_COVERFLOW_ATTRIBUTE                                                = "assetcoverflowattribute";
  public static final String       LIST_PRICE_ATTRIBUTE                                                     = "listpriceattribute";
  public static final String       SELLING_PRICE_ATTRIBUTE                                                  = "sellingpriceattribute";
  public static final String       PIN_CODE_ATTRIBUTE                                                       = "pincodeattribute";
  public static final String       FILE_NAME_ATTRIBUTE                                                      = "fileNameattribute";
  public static final String       LAST_MODIFIED_ATTRIBUTE                                                  = "lastmodifiedattribute";
  public static final String       LAST_MODIFIED_BY_ATTRIBUTE                                               = "lastmodifiedbyattribute";
  public static final String       MINIMUM_PRICE_ATTRIBUTE                                                  = "minimumpriceattribute";
  public static final String       MAXIMUM_PRICE_ATTRIBUTE                                                  = "maximumpriceattribute";
  public static final String       CREATED_BY_ATTRIBUTE                                                     = "createdbyattribute";
  public static final String       TELEPHONE_NUMBER_ATTRIBUTE                                               = "telephonenumberattribute";
  public static final String       DISCOUNT_ATTRIBUTE                                                       = "discountattribute";
  public static final String       ASSET_DOWNLOAD_COUNT_ATTRIBUTE                                           = "assetdownloadcountattribute";
  
  // Content class
  public static final String       ARTICLE_KLASS                                                            = "article";
  public static final String       COLLECTION_KLASS                                                         = "collection";
  public static final String       SET_KLASS                                                                = "set";
  
  public static final String       GTIN_ATTRIBUTE                                                           = "gtin_attribute";
  public static final String       PID_ATTRIBUTE                                                            = "pid_attribute";
  public static final String       SKU_ATTRIBUTE                                                            = "sku_attribute";
  
  public static final String       LONG_DESCRIPTION_ATTRIBUTE                                               = "longdescriptionattribute";
  public static final String       SHORT_DESCRIPTION_ATTRIBUTE                                              = "shortdescriptionattribute";
  public static final String       ADDRESS_ATTRIBUTE                                                        = "addressattribute";
  public static final String       DESCRIPTION_ATTRIBUTE                                                    = "descriptionattribute";
  
  // metadata attributes ids
  public static final String       I_META_FILE_NAME                                                         = "I_META_FILE_NAME";
  public static final String       I_META_DOCUMENT_TYPE                                                     = "I_META_DOCUMENT_TYPE";
  public static final String       I_APPLICATION                                                            = "I_APPLICATION";
  public static final String       I_CREATE_DATE                                                            = "I_CREATE_DATE";
  public static final String       I_MODIFICATION_DATE                                                      = "I_MODIFICATION_DATE";
  public static final String       I_META_FILE_SIZE                                                         = "I_META_FILE_SIZE";
  public static final String       I_DIMENSIONS                                                             = "I_DIMENSIONS";
  public static final String       I_DIMENSION_IN_INCH                                                      = "I_DIMENSION_IN_INCH";
  public static final String       I_RESOLUTION                                                             = "I_RESOLUTION";
  public static final String       I_BIT_DEPTH                                                              = "I_BIT_DEPTH";
  public static final String       I_COLOR_MODE                                                             = "I_COLOR_MODE";
  public static final String       I_COLOR_PROFILE                                                          = "I_COLOR_PROFILE";
  public static final String       I_META_CREATOR_AUTHOR                                                    = "I_META_CREATOR_AUTHOR";
  public static final String       I_META_CREATOR_STREET                                                    = "I_META_CREATOR_STREET";
  public static final String       I_META_CREATOR_LOCATION                                                  = "I_META_CREATOR_LOCATION";
  public static final String       I_META_CREATOR_STATE                                                     = "I_META_CREATOR_STATE";
  public static final String       I_META_CREATOR_POSTAL_CODE                                               = "I_META_CREATOR_POSTAL_CODE";
  public static final String       I_META_CREATOR_COUNTRY                                                   = "I_META_CREATOR_COUNTRY";
  public static final String       I_META_CREATOR_TEL                                                       = "I_META_CREATOR_TEL";
  public static final String       I_META_CREATOR_EMAIL                                                     = "I_META_CREATOR_EMAIL";
  public static final String       I_META_CREATOR_WWW                                                       = "I_META_CREATOR_WWW";
  public static final String       I_META_HEADING                                                           = "I_META_HEADING";
  public static final String       I_META_DESCRIPTION                                                       = "I_META_DESCRIPTION";
  public static final String       I_META_KEYWORDS                                                          = "I_META_KEYWORDS";
  public static final String       I_META_LOCATION_DETAIL                                                   = "I_META_LOCATION_DETAIL";
  public static final String       I_META_LOCATION                                                          = "I_META_LOCATION";
  public static final String       I_META_STATE                                                             = "I_META_STATE";
  public static final String       I_META_COUNTRY                                                           = "I_META_COUNTRY";
  public static final String       I_META_TITEL                                                             = "I_META_TITEL";
  public static final String       I_META_COPYRIGHT                                                         = "I_META_COPYRIGHT";
  public static final String       I_META_COPYRIGHT_STATUS                                                  = "I_META_COPYRIGHT_STATUS";
  public static final String       I_META_EXPOSURE_INDEX                                                    = "I_META_EXPOSURE_INDEX";
  public static final String       I_META_FOCAL_RANGE                                                       = "I_META_FOCAL_RANGE";
  public static final String       I_META_MAX_BLINDING_VALUE                                                = "I_META_MAX_BLINDING_VALUE";
  public static final String       I_META_RECORDING_DATE                                                    = "I_META_RECORDING_DATE";
  public static final String       I_META_EXPOSURE_CONTROLS                                                 = "I_META_EXPOSURE_CONTROLS";
  public static final String       I_META_LIGHT_SOURCE                                                      = "I_META_LIGHT_SOURCE";
  public static final String       I_META_SENSOR_TYPE                                                       = "I_META_SENSOR_TYPE";
  public static final String       I_META_BRAND                                                             = "I_META_BRAND";
  public static final String       I_META_MODELL                                                            = "I_META_MODELL";
  public static final String       I_META_SERIAL_NUMBER                                                     = "I_META_SERIAL_NUMBER";
  public static final String       I_META_PIXEL_X_DIMENSION                                                 = "I_META_PIXEL_X_DIMENSION";
  public static final String       I_META_PIXEL_Y_DIMENSION                                                 = "I_META_PIXEL_Y_DIMENSION";
  public static final String       I_META_HIGH_RESOLUTION                                                   = "I_META_HIGH_RESOLUTION";
  public static final String       I_META_WIDTH_RESOLUTION                                                  = "I_META_WIDTH_RESOLUTION";
  public static final String       I_META_X_RESOLUTION                                                      = "I_META_X_RESOLUTION";
  public static final String       I_META_Y_RESOLUTION                                                      = "I_META_Y_RESOLUTION";
  
  public static final String[]     METADATA_ATTRIBUTES_IDS                                                  = new String[] {
      I_META_FILE_NAME, I_META_DOCUMENT_TYPE, I_APPLICATION, I_CREATE_DATE, I_MODIFICATION_DATE, I_META_FILE_SIZE, I_DIMENSIONS,
      I_DIMENSION_IN_INCH, I_RESOLUTION, I_BIT_DEPTH, I_COLOR_MODE, I_COLOR_PROFILE, I_META_CREATOR_AUTHOR, I_META_CREATOR_STREET,
      I_META_CREATOR_LOCATION, I_META_CREATOR_STATE, I_META_CREATOR_POSTAL_CODE, I_META_CREATOR_COUNTRY, I_META_CREATOR_TEL,
      I_META_CREATOR_EMAIL, I_META_CREATOR_WWW, I_META_HEADING, I_META_DESCRIPTION, I_META_KEYWORDS, I_META_LOCATION_DETAIL,
      I_META_LOCATION, I_META_STATE, I_META_COUNTRY, I_META_TITEL, I_META_COPYRIGHT, I_META_COPYRIGHT_STATUS, I_META_EXPOSURE_INDEX,
      I_META_FOCAL_RANGE, I_META_MAX_BLINDING_VALUE, I_META_RECORDING_DATE, I_META_EXPOSURE_CONTROLS, I_META_LIGHT_SOURCE,
      I_META_SENSOR_TYPE, I_META_BRAND, I_META_MODELL, I_META_SERIAL_NUMBER, I_META_PIXEL_X_DIMENSION, I_META_PIXEL_Y_DIMENSION,
      I_META_HIGH_RESOLUTION, I_META_WIDTH_RESOLUTION, I_META_X_RESOLUTION, I_META_Y_RESOLUTION };
  
  // Roles
  public static final String       OWNER_ROLE                                                               = "ownerrole";
  public static final String       ASSIGNEE_ROLE                                                            = "assigneerole";
  public static final String       APPROVER_ROLE                                                            = "approverrole";
  public static final String       CONTRIBUTOR_ROLE                                                         = "contributorrole";
  public static final String       REVIEWER_ROLE                                                            = "reviewerrole";
  public static final String       SENIOR_MANAGER_ROLE                                                      = "seniormanagerrole";
  public static final String       SUBSCRIBER_ROLE                                                          = "subscriberrole";
  
  public static final String       RESPONSIBLE_ROLE                                                         = "responsiblerole";
  public static final String       ACCOUNTABLE_ROLE                                                         = "accountablerole";
  public static final String       CONSULTED_ROLE                                                           = "consultedrole";
  public static final String       INFORMED_ROLE                                                            = "informedrole";
  public static final String       VERIFY_ROLE                                                              = "verifyrole";
  public static final String       SIGN_OFF_ROLE                                                            = "signoffrole";
  public static final String       ADMIN_ROLE                                                               = "adminrole";
  
  public static final String       RESPONSIBLE                                                              = "responsible";
  public static final String       ACCOUNTABLE                                                              = "accountable";
  public static final String       CONSULTED                                                                = "consulted";
  public static final String       INFORMED                                                                 = "informed";
  public static final String       VERIFY                                                                   = "verify";
  public static final String       SIGN_OFF                                                                 = "signoff";
  
  public static final String[]     RACIVS_ROLE_IDS                                                          = new String[] {
      RESPONSIBLE_ROLE, ACCOUNTABLE_ROLE, CONSULTED_ROLE, INFORMED_ROLE, VERIFY_ROLE, SIGN_OFF_ROLE, ADMIN_ROLE };
  
  // Content class
  public static final String       ARTICLE                                                                  = "article";
  public static final String       GOLDEN_ARTICLE_KLASS                                                     = "golden_article_klass";
  public static final String       FILE                                                                     = "fileklass";
  public static final String       COLLECTION                                                               = "collection";
  public static final String       SET                                                                      = "set";
  public static final String       PRODUCT_TYPES                                                            = "product_types";
  public static final String       SINGLE_ARTICLE                                                           = "single_article";
  public static final String       ATTRIBUTION_CLASSES                                                      = "attribute_classes";
  public static final String       CATLOGUE_CLASS                                                           = "catlogue_class";
  public static final String       WERBESET_CLASS                                                           = "werbeset_class";
  public static final String       MARKER                                                                   = "marker";
  
  // Task class
  public static final String       TASK_KLASS                                                               = "task";
  
  // Asset class
  public static final String       ASSET                                                                    = "asset_asset";
  public static final String       IMAGE                                                                    = "image_asset";
  public static final String       VIDEO                                                                    = "video_asset";
  public static final String       DOCUMENT                                                                 = "document_asset";
  public static final String       JPEG                                                                     = "jpeg_asset";
  public static final String       PNG                                                                      = "png_asset";
  public static final String       ICO                                                                      = "ico_asset";
  public static final String       EPS                                                                      = "eps_asset";
  public static final String       AI                                                                       = "ai_asset";
  public static final String       PSD                                                                      = "psd_asset";
  public static final String       WMV                                                                      = "wmv_asset";
  public static final String       AVI                                                                      = "avi_asset";
  public static final String       MOV                                                                      = "mov_asset";
  public static final String       FLV                                                                      = "flv_asset";
  public static final String       MPEG                                                                     = "mpeg_asset";
  public static final String       PDF                                                                      = "pdf_asset";
  public static final String       INDD                                                                     = "indd_asset";
  public static final String       PPT                                                                      = "ppt_asset";
  public static final String       WORD                                                                     = "word_asset";
  public static final String       ATTACHMENT                                                               = "attachment_asset";
  public static final String       SMARTDOCUMENT_ASSET                                                      = "smartdocument_asset";
  
  // Target class
  public static final String       MARKET                                                                   = "market";
  // Tag types
  public static final String       YES_NEUTRAL_TAG_TYPE_ID                                                  = "tag_type_yes_neutral";
  public static final String       YES_NEUTRAL_NO_TAG_TYPE_ID                                               = "tag_type_yes_neutral_no";
  public static final String       RANGE_TAG_TYPE_ID                                                        = "tag_type_range";
  public static final String       CUSTOM_TAG_TYPE_ID                                                       = "tag_type_custom";
  public static final String       RULER_TAG_TYPE_ID                                                        = "tag_type_ruler";
  public static final String       LIFECYCLE_STATUS_TAG_TYPE_ID                                             = "tag_type_lifecycle_status";
  public static final String       LISTING_STATUS_TAG_TYPE_ID                                               = "tag_type_listing_status";
  public static final String       STATUS_TAG_TYPE_ID                                                       = "tag_type_status";
  public static final String       BOOLEAN_TAG_TYPE_ID                                                      = "tag_type_boolean";
  public static final String       MASTER_TAG_TYPE_ID                                                       = "tag_type_master";
  public static final String       LANGUAGE_TAG_TYPE_ID                                                     = "tag_type_language";
  
  // Tags
  public static final String       STATUS_TAG                                                               = "statustag";
  public static final String       REGION_TAG                                                               = "regiontag";
  public static final String       AVAILABILITY_TAG                                                         = "availabilitytag";
  public static final String       LANGUAGE_TAG                                                             = "languagetag";
  public static final String       ENRICHMENT_TAG                                                           = "enrichmenttag";
  public static final String       QUALITY_CHECK_TAG                                                        = "qualitychecktag";
  public static final String       TRANSLATION_TAG                                                          = "translationtag";
  public static final String       APPROVAL_TAG                                                             = "approvaltag";
  public static final String       PRODUCTIVE_TAG                                                           = "productivetag";
  public static final String       TASK_STATUS_TAG                                                          = "taskstatustag";
  public static final String       RESOLUTION_TAG                                                           = "resolutiontag";
  public static final String       IMAGE_EXTENSION_TAG                                                      = "imageextensiontag";
  public static final String       LIFE_STATUS_TAG_ID                                                       = "lifestatustag";
  public static final String       LISTING_STATUS_TAG_ID                                                    = "listingstatustag";
  public static final String       IS_ORDER_TAG_ID                                                          = "isordertag";
  public static final String       IS_SALES_TAG_ID                                                          = "issalestag";
  public static final String       IS_BASEUNIT_TAG_ID                                                       = "isbaseunittag";
  public static final String       SUPPORTED_LANGUAGES                                                      = "supportedLanguages";
  public static final String       EN                                                                       = "en";
  public static final String       DE                                                                       = "dr";
  public static final String       FR                                                                       = "fr";
  public static final String       ES                                                                       = "es";
  public static final List<String> MANDATORY_TAGS                                                           = Arrays
      .asList(LIFE_STATUS_TAG_ID, LISTING_STATUS_TAG_ID);
  
  public static final String       T_STATUS_PROJECT                                                         = "T_STATUS_PROJECT";
  // Project status tag values
  public static final String       T_STATUS_PROJEKT_IN_PLANUNG                                              = "T_STATUS_PROJECT_IN_PLANNING";
  public static final String       T_STATUS_PROJEKT_ABGESCHLOSSEN                                           = "T_STATUS_PROJECT_COMPLETE";
  public static final String       T_STATUS_PROJEKT_ZUR_UBERARBEITUNG                                       = "T_STATUS_PROJECT_REWORK";
  public static final String       T_STATUS_PROJEKT_QUALITAT_SICHERN                                        = "T_STATUS_PROJECT_QUALITY_CHECK";
  public static final String       T_STATUS_PROJEKT_FEHLER_BEARBEITEN                                       = "T_STATUS_PROJECT_ERROR_PROCESSING";
  public static final String       T_STATUS_PROJEKT_VERWENDBAR                                              = "T_STATUS_PROJECT_USING ALLOWED";
  public static final String       T_STATUS_PROJEKT_LIZENZ_FEHLT                                            = "T_STATUS_PROJECT_LICENSE_MISSING";
  public static final String       T_STATUS_PROJEKT_ABGEBROCHEN                                             = "T_STATUS_PROJECT_CANCELED";
  
  public static final String       T_STATUS_SYSTEM                                                          = "T_STATUS_SYSTEM";
  // System Status Tag Values
  public static final String       T_STATUS_SYSTEM_GELOESCHT                                                = "T_STATUS_SYSTEM_DELETED";
  public static final String       T_STATUS_SYSTEM_ARCHIVIERT                                               = "T_STATUS_SYSTEM_ARCHIVED";
  public static final String       T_STATUS_SYSTEM_AKTIV                                                    = "T_STATUS_SYSTEM_ACTIV";
  public static final String       T_STATUS_SYSTEM_INAKTIV                                                  = "T_STATUS_SYSTEM_INACTIV";
  public static final String       T_STATUS_SYSTEM_PREPARE                                                  = "T_STATUS_SYSTEM_PREPARE";
  public static final String       T_STATUS_SYSTEM_BLOCKED                                                  = "T_STATUS_SYSTEM_BLOCKED";
  public static final String       T_STATUS_SYSTEM_NEU                                                      = "T_STATUS_SYSTEM_NEW";
  
  // Goals Tags
  public static final String       T_GOALS_BRAND_PROMOTION                                                  = "T_GOALS_BRAND_PROMOTION";
  public static final String       T_GOALS_SEASONAL_PROMOTION                                               = "T_GOALS_SEASONAL_PROMOTION";
  public static final String       T_GOALS_NEW_ARRIVALS                                                     = "T_GOALS_NEW_ARRIVALS";
  public static final String       T_GOALS_PROMOTION_HIGH_MARGIN                                            = "T_GOALS_PROMOTION_HIGH_MARGIN";
  public static final String       T_GOALS_INVENTORY_CLEARANCE                                              = "T_GOALS_INVENTORY_CLEARANCE";
  public static final String       T_GOALS_SPECIAL_TOPIC                                                    = "T_GOALS_SPECIAL_TOPIC";
  public static final String       T_GOALS_LOYALTY_POINTS                                                   = "T_GOALS_LOYALTY_POINTS";
  public static final String       T_GOALS_RETAIN_EXISTING_CUSTOMERS                                        = "T_GOALS_RETAIN_EXISTING_CUSTOMERS";
  public static final String       T_GOALS_ACQUIRE_NEW_CUSTOMERS                                            = "T_GOALS_ACQUIRE_NEW_CUSTOMERS";
  // property collection
  public static final String       ARTICLE_GENERAL_INFORMATION_PROPERTY_COLLECTION                          = "articlegeneralInformationPropertyCollection";
  public static final String       MARKET_GENERAL_INFORMATION_PROPERTY_COLLECTION                           = "targetGeneralInformationPropertyCollection";
  public static final String       ASSET_GENERAL_INFORMATION_PROPERTY_COLLETION                             = "assetInformationPropertyCollection";
  public static final String       PRICING_PROPERTY_COLLECTION                                              = "pricingPropertyCollection";
  public static final String       PROMOTION_GENERAL_INFORMATION_PROPERTY_COLLECTION                        = "promotionGeneralInformationPropertyCollection";
  public static final String       TEXTASSET_GENERAL_INFORMATION_PROPERTY_COLLECTION                        = "textassetGeneralInformationPropertyCollection";
  public static final String       SUPPLIER_GENERAL_INFORMATION_PROPERTY_COLLECTION                         = "supplierGeneralInformationPropertyCollection";
  
  public static final String       DEFAULT_X_RAY_PROPERTY_COLLECTION                                        = "defaultXRayPropertyCollection";
  
  /*
   * public static final String MEDIA_METADATA_COLLECTION =
   * "mediaMetadataCollection";
   */
  public static final String       METADATA_COLLECTION                                                      = "metadataCollection";
  
  // LanguageTag
  public static final String       ENGLISH                                                                  = "english";
  public static final String       GERMAN                                                                   = "german";
  public static final String       SPANISH                                                                  = "spanish";
  public static final String       ITALIAN                                                                  = "italian";
  public static final String       FRENCH                                                                   = "french";
  public static final String       PORTUGESE                                                                = "portugese";
  public static final String       SWEDISH                                                                  = "swedish";
  
  // AvailabilityTag
  public static final String       BLOCKED_FROM_PLANNING                                                    = "blockedfromplanning";
  public static final String       ON_STOCK                                                                 = "onstock";
  public static final String       REGULARLY_AVAILABLE                                                      = "regularlyavailable";
  public static final String       ON_REQUEST                                                               = "onrequest";
  public static final String       DROP_SHIPPING                                                            = "dropshipping";
  public static final String       PRO_SPECIFIC_TAG                                                         = "projectSpecific";
  public static final String       PRO_SPECIFIC_TAG1                                                        = "projectSpecificTest";
  
  // Task-Status Tag
  public static final String       PLANNED                                                                  = "taskplanned";
  public static final String       READY                                                                    = "taskready";
  public static final String       DECLINED                                                                 = "taskdeclined";
  public static final String       IN_PROGRESS                                                              = "taskinprogress";
  public static final String       DONE                                                                     = "taskdone";
  public static final String       VERIFIED                                                                 = "taskverified";
  public static final String       SIGNED_OFF                                                               = "tasksignedoff";
  
  // ResolutionTag
  public static final String       RESOLUTION_300P                                                          = "resolution_300P";
  public static final String       RESOLUTION_150P                                                          = "resolution_150P";
  public static final String       RESOLUTION_72P                                                           = "resolution_72P";
  public static final String       RESOLUTION_720P                                                          = "resolution_720P";
  public static final String       RESOLUTION_1080P                                                         = "resolution_1080P";
  public static final String       RESOLUTION_1440P                                                         = "resolution_1440P";
  public static final String       RESOLUTION_2160P                                                         = "resolution_2160P";
  public static final String       RESOLUTION_4380P                                                         = "resolution_4380P";
  
  // ImageExtensionTag
  public static final String       IMAGE_EXTENSION_TAG_FORMAT_JPG                                           = "image_extension_format_jpg";
  public static final String       IMAGE_EXTENSION_TAG_FORMAT_PNG                                           = "image_extension_format_png";
  public static final String       IMAGE_EXTENSION_TAG_FORMAT_ORIGINAL                                      = "image_extension_format_original";
  
  // LifeStatusTags
  public static final String       LIFE_STATUS_INBOX                                                        = "life_status_inbox";
  public static final String       LIFE_STATUS_NO_INTEREST                                                  = "life_status_nointerest";
  public static final String       LIFE_STATUS_PREPARATION                                                  = "life_status_preparation";
  public static final String       LIFE_STATUS_ACTIVE                                                       = "life_status_active";
  public static final String       LIFE_STATUS_ACTIVE_BLOCKED                                               = "life_status_activeblocked";
  public static final String       LIFE_STATUS_ACTIVE_BANNED                                                = "life_status_activebanned";
  public static final String       LIFE_STATUS_RETIRED                                                      = "life_status_retired";
  public static final String       LIFE_STATUS_ARCHIVED                                                     = "life_status_archived";
  public static final String       LIFE_STATUS_DELETED                                                      = "life_status_deleted";
  
  // ListingStatus Tags
  public static final String       LISTING_STATUS_CATLOG                                                    = "listing_status_catlog";
  public static final String       LISTING_STATUS_IGNORED                                                   = "listing_status_ignored";
  public static final String       LISTING_STATUS_REJECTED                                                  = "listing_status_rejected";
  public static final String       LISTING_STATUS_ONBOARDING_POOL                                           = "listing_status_onboardingpool";
  public static final String       LISTING_STATUS_RECOMMENDED                                               = "listing_status_recommended";
  public static final String       LISTING_STATUS_SHADOW_ASSORTMENT                                         = "listing_status_shadowassortment";
  public static final String       LISTING_STATUS_LISTED                                                    = "listing_status_listed";
  
  // isSales Boolean Tag
  public static final String       IS_SALES_TAG_VALUE                                                       = "issalestagvalue";
  
  // isOrder Boolean Tag
  public static final String       IS_ORDER_TAG_VALUE                                                       = "isordertagvalue";
  
  // isPromotion Boolean Tag
  public static final String       IS_BASEUNIT_TAG_VALUE                                                    = "isbaseunittagvalue";
  
  // Master Language Tas
  public static final String       LANGUAGE                                                                 = "language";
  public static final String       EN_US                                                                    = "en_US";
  public static final String       EN_UK                                                                    = "en_UK";
  public static final String       DE_DE                                                                    = "de_DE";
  public static final String       FR_FR                                                                    = "fr_FR";
  public static final String       ES_ES                                                                    = "es_ES";
  
  // Relationships
  public static final String       STANDARD_MARKET_ASSET_RELATIONSHIP_ID                                    = "standardMarketAssetRelationship";
  public static final String       STANDARD_ARTICLE_ASSET_RELATIONSHIP_ID                                   = "standardArticleAssetRelationship";
  public static final String       STANDARD_PROMOTION_ASSET_RELATIONSHIP_ID                                 = "standardPromotionAssetRelationship";
  public static final String       STANDARD_TEXTASSET_ASSET_RELATIONSHIP_ID                                 = "standardTextAsset-AssetRelationship";
  public static final String       STANDARD_SUPPLIER_ASSET_RELATIONSHIP_ID                                  = "standardSupplierAssetRelationship";
  public static final String       STANDARD_ARTICLE_MARKET_RELATIONSHIP_ID                                  = "standardArticleMarketRelationship";
  public static final String       STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID                          = "standardArticleGoldenArticleRelationship";
  // References
  public static final String       STANDARD_ARTICLE_SMART_DOCUMENT_REFRENCE_ID                              = "standardArticleSmartDocumentReference";
  public static final String       STANDARD_ARTICLE_SMART_DOCUMENT_REFRENCE_SIDE_1_ID                       = "standardArticleSmartDocumentReferenceSide1";
  public static final String       STANDARD_ARTICLE_SMART_DOCUMENT_REFRENCE_SIDE_2_ID                       = "standardArticleSmartDocumentReferenceSide2";
  
  // ResolutionContext
  public static final String       STANDARD_LOGO_CONFIGURATION_ID                                           = "standardlogoconfiguration";
  
  public static final String       RELATIONSHIP_APPENDER_FOR_SIDE_1_ELEMENT                                 = "_side1_element";
  public static final String       RELATIONSHIP_APPENDER_FOR_SIDE_2_ELEMENT                                 = "_side2_element";
  
  // ResolutionContext
  public static final List<String> STANDARD_ENTITY_ASSET_RELATIONSHIPS                                      = Arrays.asList(
      STANDARD_MARKET_ASSET_RELATIONSHIP_ID,
      STANDARD_ARTICLE_ASSET_RELATIONSHIP_ID, STANDARD_PROMOTION_ASSET_RELATIONSHIP_ID, STANDARD_TEXTASSET_ASSET_RELATIONSHIP_ID,
      STANDARD_SUPPLIER_ASSET_RELATIONSHIP_ID);
  
  // ResolutionContext
  public static final String       RESOLUTION_CONTEXT                                                       = "Resolution_Context";
  
  public static final String       IMAGE_ATTRIBUTE_INSTANCE                                                 = "com.cs.core.runtime.interactor.entity.propertyinstance.ImageAttributeInstance";
  // TextAsset
  public static final String       TEXT_ASSET                                                               = "text_asset";
  // Supplier Klass
  public static final String       SUPPLIER                                                                 = "supplier";
  public static final String       SUPPLIERS                                                                = "suppliers";
  public static final String       MARKETPLACES                                                             = "marketplaces";
  public static final String       DISTRIBUTORS                                                             = "distributors";
  public static final String       WHOLESALERS                                                              = "wholesalers";
  public static final String       TRANSLATION_AGENCY                                                       = "translation_agency";
  public static final String       CONTENT_ENRICHMENT_AGENCY                                                = "content_enrichment_agency";
  public static final String       DIGITAL_ASSET_AGENCY                                                     = "digital_asset_agency";
  
  // Attribution Taxonomies
  public static final String       TAXONOMY_ATTRIBUTION                                                     = "taxonomy_attribution";
  
  // Events
  public static final String       CREATED_EVENT_ID                                                         = "createdEvent";
  
  // template tab lables
  public static final String       TEMPLATE_HOME_TAB_LABEL                                                  = "HOME";
  public static final String       TEMPLATE_RELATIONSHIP_TAB_LABEL                                          = "Relationship";
  public static final String       TEMPLATE_CONTEXTUAL_TAB_LABEL                                            = "Contextual Variants";
  public static final String       TEMPLATE_LANGUAGE_TAB_LABEL                                              = "Language Variants";
  public static final String       TEMPLATE_PROMO_VERSION_TAB_LABEL                                         = "Promotional Version";
  public static final String       TEMPLATE_TASKS_TAB_LABEL                                                 = "Tasks";
  
  // language
  public static final String       STANDARD_DATE_FORMAT                                                     = "YYYY-MM-DD HH:MM:SS";
  public static final String       STANDARD_NUMBER_FORMAT                                                   = "#########.##";
  
  public static final List<String> MANDATORY_ATTRIBUTES                                                     = Arrays.asList(NAME_ATTRIBUTE,
      CREATED_ON_ATTRIBUTE, CREATED_BY_ATTRIBUTE, LAST_MODIFIED_ATTRIBUTE, LAST_MODIFIED_BY_ATTRIBUTE);
  
  public static final List<String> UNIT_DEFAULT_PC_TAGS                                                     = Arrays.asList(IS_ORDER_TAG_ID,
      IS_SALES_TAG_ID, IS_BASEUNIT_TAG_ID);
  
  public static final String       UNIT_DEFAULT_PC_ID                                                       = "UNIT_Default";
  
  public static final String       STANDARD_ORGANIZATION                                                    = "-1";
  public static final String       STANDARD_SYSTEM                                                          = "-1";
  public static final String       STANDARD_PARENT_ID                                                       = "-1";
  
  public static final String[]     ATTRIBUTE_IDS                                                            = new String[] {
      ASSET_COVERFLOW_ATTRIBUTE, ADDRESS_ATTRIBUTE, DESCRIPTION_ATTRIBUTE, DUE_DATE_ATTRIBUTE, FIRST_NAME_ATTRIBUTE, LIST_PRICE_ATTRIBUTE,
      LONG_DESCRIPTION_ATTRIBUTE, MINIMUM_PRICE_ATTRIBUTE, PID_ATTRIBUTE, PIN_CODE_ATTRIBUTE, SELLING_PRICE_ATTRIBUTE,
      SHORT_DESCRIPTION_ATTRIBUTE, SKU_ATTRIBUTE, TELEPHONE_NUMBER_ATTRIBUTE, DISCOUNT_ATTRIBUTE, GTIN_ATTRIBUTE, LAST_NAME_ATTRIBUTE,
      MAXIMUM_PRICE_ATTRIBUTE, CREATED_BY_ATTRIBUTE, CREATED_ON_ATTRIBUTE, LAST_MODIFIED_ATTRIBUTE, LAST_MODIFIED_BY_ATTRIBUTE,
      NAME_ATTRIBUTE };
  
  public static final String[]     TAG_IDS                                                                  = new String[] {
      LISTING_STATUS_TAG_ID, LISTING_STATUS_CATLOG, LISTING_STATUS_IGNORED, LISTING_STATUS_REJECTED, LISTING_STATUS_ONBOARDING_POOL,
      LISTING_STATUS_RECOMMENDED, LISTING_STATUS_SHADOW_ASSORTMENT, LISTING_STATUS_LISTED, LIFE_STATUS_TAG_ID, LIFE_STATUS_INBOX,
      LIFE_STATUS_NO_INTEREST, LIFE_STATUS_PREPARATION, LIFE_STATUS_ACTIVE, LIFE_STATUS_ACTIVE_BLOCKED, LIFE_STATUS_ACTIVE_BANNED,
      LIFE_STATUS_RETIRED, LIFE_STATUS_ARCHIVED, LIFE_STATUS_DELETED, STATUS_TAG, ENRICHMENT_TAG, QUALITY_CHECK_TAG, TRANSLATION_TAG,
      APPROVAL_TAG, PRODUCTIVE_TAG, BLOCKED_FROM_PLANNING, AVAILABILITY_TAG, ON_REQUEST, ON_STOCK, REGULARLY_AVAILABLE, DROP_SHIPPING,
      LANGUAGE_TAG, ENGLISH, GERMAN, SPANISH, ITALIAN, FRENCH, PORTUGESE, SWEDISH };
  
  public static final String[]     PROPERTY_COLLECTION_IDS                                                  = new String[] {
      PRICING_PROPERTY_COLLECTION, ARTICLE_GENERAL_INFORMATION_PROPERTY_COLLECTION, MARKET_GENERAL_INFORMATION_PROPERTY_COLLECTION,
      ASSET_GENERAL_INFORMATION_PROPERTY_COLLETION,
      PROMOTION_GENERAL_INFORMATION_PROPERTY_COLLECTION, SUPPLIER_GENERAL_INFORMATION_PROPERTY_COLLECTION,
      TEXTASSET_GENERAL_INFORMATION_PROPERTY_COLLECTION, DEFAULT_X_RAY_PROPERTY_COLLECTION, METADATA_COLLECTION, UNIT_DEFAULT_PC_ID };
  
  public static final String[]     TARGET_IDS                                                               = new String[] {        MARKET };  
  public static final String[]     ROLE_IDS                                                                 = new String[] { APPROVER_ROLE,
      OWNER_ROLE, SUBSCRIBER_ROLE, CONTRIBUTOR_ROLE, REVIEWER_ROLE, SENIOR_MANAGER_ROLE, ASSIGNEE_ROLE };
  
  public static final String[]     RELATIONSHIP_IDS                                                         = new String[] {
      STANDARD_ARTICLE_ASSET_RELATIONSHIP_ID, STANDARD_MARKET_ASSET_RELATIONSHIP_ID,
      STANDARD_TEXTASSET_ASSET_RELATIONSHIP_ID, STANDARD_SUPPLIER_ASSET_RELATIONSHIP_ID, STANDARD_PROMOTION_ASSET_RELATIONSHIP_ID,
      STANDARD_ARTICLE_MARKET_RELATIONSHIP_ID, STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID };
  
  public static final String[]     REFERENCE_IDS                                                            = new String[] {
      STANDARD_ARTICLE_SMART_DOCUMENT_REFRENCE_ID };
  
  public static final String       DATE_ATTRIBUTE_BASE_TYPE                                                 = "com.cs.core.config.interactor.entity.attribute.DateAttribute";
  public static final String       CREATED_ON_ATTRIBUTE_BASE_TYPE                                           = "com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute";
  public static final String       DUE_DATE_ATTRIBUTE_BASE_TYPE                                             = "com.cs.config.interactor.entity.concrete.attribute.standard.DueDateAttribute";
  public static final String       LAST_MODIFIED_ATTRIBUTE_BASE_TYPE                                        = "com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute";
  public static final List<String> DATE_TYPE                                                                = Arrays
      .asList(DATE_ATTRIBUTE_BASE_TYPE, CREATED_ON_ATTRIBUTE_BASE_TYPE, DUE_DATE_ATTRIBUTE_BASE_TYPE, LAST_MODIFIED_ATTRIBUTE_BASE_TYPE);
  
  public static final String       PROPERTY_COLLECTION_TAB                                                  = "property_collection_tab";
  public static final String       RELATIONSHIP_TAB                                                         = "relationship_tab";
  public static final String       CONTEXT_TAB                                                              = "context_tab";
  public static final String       TAB_SEQUENCE_NODE_ID                                                     = "tabSequence";
  public static final String       TASK_TAB                                                                 = "task_tab";
  public static final String       TIMELINE_TAB                                                             = "timeline_tab";
  public static final String       OVERVIEW_TAB                                                             = "overview_tab";
  public static final String       RENDITION_TAB                                                            = "rendition_tab";
  public static final String       CONFIGURATION_TAB                                                        = "configurations_tab";
  public static final String       DUPLICATE_TAB                                                            = "duplicate_tab";
  
  public static final String[]     PIM_KLASS_IDS                                                            = new String[] { ARTICLE,
      PRODUCT_TYPES, ATTRIBUTION_CLASSES, SINGLE_ARTICLE, FILE, MARKER, GOLDEN_ARTICLE_KLASS };
  public static final String[]     MAM_KLASS_IDS                                                            = new String[] { ATTACHMENT,
      ASSET, IMAGE, VIDEO, DOCUMENT, SMARTDOCUMENT_ASSET };
  
  public static final String       DEFAULT_DASHBOARD_TAB_ID                                                 = "dashboardtab";
  public static final String       DEFAULT_DATA_INTEGRATION_TAB_ID                                          = "dataIntegrationTab";
  public static final List<String> KLASSES_TO_EXCLUDE_FROM_CONFIG_SCREEN                                    = Arrays.asList(MARKER,
      GOLDEN_ARTICLE_KLASS, FILE);
  
  public static final String       LANGUAGE_TREE                                                            = "language";
  
  public static final String[]     STANDARD_TABS                                                            = new String[] { OVERVIEW_TAB,
      PROPERTY_COLLECTION_TAB, RELATIONSHIP_TAB, CONTEXT_TAB, TASK_TAB, TIMELINE_TAB, RENDITION_TAB,
      CONFIGURATION_TAB, DUPLICATE_TAB };
  
  public static final List<String> RACVIS_ROLES                                                             = Arrays
      .asList(RESPONSIBLE_ROLE, ACCOUNTABLE_ROLE, CONSULTED_ROLE, VERIFY_ROLE, INFORMED_ROLE, SIGN_OFF_ROLE);
  
  // Image Extension formats
  public static final String       IMAGE_EXTENSION_JPG                                                      = "jpg";
  public static final String       IMAGE_EXTENSION_PNG                                                      = "png";
  public static final String       IMAGE_EXTENSION_ORIGINAL                                                 = "original";
  
  // Smart Document
  public static final String       SMART_DOCUMENT_ID                                                        = "smartdocument";
  // Standard Transfer Workflow
  public static final String       STANDARD_TRANSFER_PROCESS                                                = "standard_transfer_process";
  public static final List<String> WORKFLOWS_TO_EXCLUDE_FROM_CONFIG_SCREEN                                  = Arrays
      .asList(STANDARD_TRANSFER_PROCESS);
  
  // Standard BGP Workflow
  public static final String       STANDARD_BULK_UPDATE_PROCESS                                             = "BULK_UPDATE";
  // Grid Edit Sequnece
  public static final String       GRID_EDIT_SEQUENCE_NODE_ID                                               = "gridEditSequence";
  
  public static final String       ID_SEQUENCE                                                              = "idSequence";
  public static final String       VIEW_CODE                                                                = "standardviewconfiguration";
  public static final String       THEME_CODE                                                               = "standardlogoconfiguration";
  
  // Variant Configuration
  public static final String       STANDARD_VARIANT_CONFIGURATION                     = "standardVariantConfiguration";
  
  // Standard DI dashboard tabs
  public static final String       ONBOARD_TAB                                        = "onboardTab";
  public static final String       PUBLISH_TAB                                        = "publishTab";
}
