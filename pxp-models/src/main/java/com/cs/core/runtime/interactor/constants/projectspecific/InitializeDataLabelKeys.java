package com.cs.core.runtime.interactor.constants.projectspecific;

import java.util.HashMap;
import java.util.Map;

import com.cs.constants.SystemLevelIds;
import com.cs.core.runtime.interactor.constants.application.Constants;

public class InitializeDataLabelKeys {
  
  public String              ADMIN;
  
  public String              IMAGE_COVERFLOW_ATTRIBUTE_FOR_MAM;
  public String              IMAGE_COVERFLOW_ATTRIBUTE_FOR_OTHERS;
  
  public String              PROJECT_KLASS;
  public String              TASK_KLASS_KLASS;
  public String              ASSET_KLASS;
  public String              TARGET_KLASS;
  public String              MARKET_KLASS;
  
  public String              PROPERTY_TYPE_ATTRIBUTE;
  public String              PROPERTY_TYPE_ROLE;
  public String              PROPERTY_TYPE_TAG;
  public String              PROPERTY_TYPE_RELATIONSHIP;
  public String              PROPERTY_TYPE_TAXONOMY;;
  // Attributes
  public String              NAME_ATTRIBUTE;
  public String              FIRST_NAME_ATTRIBUTE;
  public String              LAST_NAME_ATTRIBUTE;
  public String              DUE_DATE_ATTRIBUTE;
  public String              CREATED_ON_ATTRIBUTE;
  public String              ASSET_COVERFLOW_ATTRIBUTE;
  public String              LIST_PRICE_ATTRIBUTE;
  public String              SELLING_PRICE_ATTRIBUTE;
  public String              PIN_CODE_ATTRIBUTE;
  public String              FILE_NAME_ATTRIBUTE;
  public String              LAST_MODIFIED_ATTRIBUTE;
  public String              LAST_MODIFIED_BY_ATTRIBUTE;
  public String              MINIMUM_PRICE_ATTRIBUTE;
  public String              MAXIMUM_PRICE_ATTRIBUTE;
  public String              CREATED_BY_ATTRIBUTE;
  public String              TELEPHONE_NUMBER_ATTRIBUTE;
  public String              DISCOUNT_ATTRIBUTE;
  
  // Content class ;
  public String              ARTICLE_KLASS;
  public String              COLLECTION_KLASS;
  public String              SET_KLASS;
  
  public String              GTIN_ATTRIBUTE;
  public String              PID_ATTRIBUTE;
  public String              SKU_ATTRIBUTE;
  
  public String              DEFAULT_DASHBOARD_TAB;
  public String              DEFAULT_DATA_INTEGRATION_TAB;
  
  public String              LONG_DESCRIPTION_ATTRIBUTE;
  public String              SHORT_DESCRIPTION_ATTRIBUTE;
  public String              ADDRESS_ATTRIBUTE;
  public String              DESCRIPTION_ATTRIBUTE;
  
  // DTP Labels
  public String              STANDARD_DTP_ABSTRACT_DOCUMENT_TEMPLATE_LABEL;
  public String              STANDARD_DTP_ABSTRACT_PRODUCT_TEMPLATE_LABEL;
  public String              STANDARD_DTP_ABSTRACT_PUBLICATION_TEMPLATE_LABEL;
  public String              STANDARD_DTP_ABSTRACT_PUBLICATION_LABEL;
  public String              STANDARD_DTP_DOCUMENT_TEMPLATE_LABEL;
  public String              STANDARD_DTP_PRODUCT_TEMPLATE_LABEL;
  public String              STANDARD_DTP_PUBLICATION_TEMPLATE_LABEL;
  public String              STANDARD_DTP_PUBLICATION_LABEL;
  public String              STANDARD_DTP_ABSTRACT_DOCUMENT_LABEL;
  public String              STANDARD_DTP_DOCUMENT_LABEL;
  public String              STANDARD_DTP_DOCUMENT_VARIANT_LABEL;
  
  public String              STANDARD_DTP_PUBLICATION_DTP_DOCUMENT_TEMPLATE_RELATIONSHIP_LABEL;
  public String              STANDARD_DTP_PUBLICATION_DTP_PRODUCT_TEMPLATE_RELATIONSHIP_LABEL;
  public String              STANDARD_DTP_PUBLICATION_DTP_PUBLICATION_TEMPLATE_RELATIONSHIP_LABEL;
  public String              STANDARD_DTP_DOCUMENT_TEMPLATE_LINKED_BUNDLE_RELATIONSHIP_LABEL;
  
  public String              STANDARD_DTP_DOCUMENT_TEMPLATE_ASSET_RELATIONSHIP_LABEL;
  public String              STANDARD_DTP_PRODUCT_TEMPLATE_ASSET_RELATIONSHIP_LABEL;
  public String              STANDARD_DTP_PUBLICATION_TEMPLATE_ASSET_RELATIONSHIP_LABEL;
  public String              STANDARD_DTP_PUBLICATION_TEMPLATE_DTP_DOCUMENT_TEMPLATE_LABEL;
  public String              STANDARD_DTP_DOCUMENT_TEMPLATE_PAGE_PREVIEW_RELATIONSHIP_LABEL;
  
  public String              STANDARD_DTP_DOCUMENT_TEMPLATE_LINKED_MARKETING_ARTICLES_RELATIONSHIP_LABEL;
  public String              STANDARD_DTP_DOCUMENT_TEMPLATE_LINKED_MARKETING_BUNDLES_RELATIONSHIP_LABEL;
  public String              STANDARD_DTP_PUBLICATION_TEMPLATE_DTP_DOCUMENT_TEMPLATE_RELATIONSHIP_LABEL;
  
  public String              DTP_TEMPLATE_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL;
  public String              DTP_DOCUMENT_TEMPLATE_INFORMATION_PROPERTY_COLLECTION_LABEL;
  public String              DTP_TEMPLATE_FILE_INFOMATION_PROPERTY_COLLECTION_LABEL;
  
  public String              DTP_ERROR_MARGIN_LABEL;
  public String              DTP_MAX_NO_OF_SLOTS_ATTRIBUTE_LABEL;
  public String              DTP_MINIMUM_SLOT_HEIGHT_ATTRIBUTE_LABEL;
  public String              DTP_MINIMUM_SLOT_WIDTH_ATTRIBUTE_LABEL;
  public String              DTP_DELTA_ATTRIBUTE_LABEL;
  
  public String[]            DTP_KLASS_LABELS             = new String[] {
      STANDARD_DTP_ABSTRACT_DOCUMENT_TEMPLATE_LABEL, STANDARD_DTP_DOCUMENT_TEMPLATE_LABEL,
      STANDARD_DTP_ABSTRACT_PRODUCT_TEMPLATE_LABEL, STANDARD_DTP_PRODUCT_TEMPLATE_LABEL,
      STANDARD_DTP_ABSTRACT_PUBLICATION_TEMPLATE_LABEL, STANDARD_DTP_PUBLICATION_TEMPLATE_LABEL };
  
  public String[]            DTP_PUBLICATION_LABELS       = new String[] {
      STANDARD_DTP_ABSTRACT_PUBLICATION_LABEL, STANDARD_DTP_PUBLICATION_LABEL };
  public String[]            DTP_DOCUMENT_LABELS          = new String[] {
      STANDARD_DTP_ABSTRACT_DOCUMENT_LABEL, STANDARD_DTP_DOCUMENT_LABEL,
      STANDARD_DTP_DOCUMENT_VARIANT_LABEL };
  
  // metadata attributes ids ;
  public String              I_META_FILE_NAME;
  public String              I_META_DOCUMENT_TYPE;
  public String              I_APPLICATION;
  public String              I_CREATE_DATE;
  public String              I_MODIFICATION_DATE;
  public String              I_META_FILE_SIZE;
  public String              I_DIMENSIONS;
  public String              I_DIMENSION_IN_INCH;
  public String              I_RESOLUTION;
  public String              I_BIT_DEPTH;
  public String              I_COLOR_MODE;
  public String              I_COLOR_PROFILE;
  public String              I_META_CREATOR_AUTHOR;
  public String              I_META_CREATOR_STREET;
  public String              I_META_CREATOR_LOCATION;
  public String              I_META_CREATOR_STATE;
  public String              I_META_CREATOR_POSTAL_CODE;
  public String              I_META_CREATOR_COUNTRY;
  public String              I_META_CREATOR_TEL;
  public String              I_META_CREATOR_EMAIL;
  public String              I_META_CREATOR_WWW;
  public String              I_META_HEADING;
  public String              I_META_DESCRIPTION;
  public String              I_META_KEYWORDS;
  public String              I_META_LOCATION_DETAIL;
  public String              I_META_LOCATION;
  public String              I_META_STATE;
  public String              I_META_COUNTRY;
  public String              I_META_TITEL;
  public String              I_META_COPYRIGHT;
  public String              I_META_COPYRIGHT_STATUS;
  public String              I_META_EXPOSURE_INDEX;
  public String              I_META_FOCAL_RANGE;
  public String              I_META_MAX_BLINDING_VALUE;
  public String              I_META_RECORDING_DATE;
  public String              I_META_EXPOSURE_CONTROLS;
  public String              I_META_LIGHT_SOURCE;
  public String              I_META_SENSOR_TYPE;
  public String              I_META_BRAND;
  public String              I_META_MODELL;
  public String              I_META_SERIAL_NUMBER;
  public String              I_META_PIXEL_X_DIMENSION;
  public String              I_META_PIXEL_Y_DIMENSION;
  public String              I_META_HIGH_RESOLUTION;
  public String              I_META_WIDTH_RESOLUTION;
  public String              I_META_X_RESOLUTION;
  public String              I_META_Y_RESOLUTION;
  
  public String[]            METADATA_ATTRIBUTES_LABELS   = new String[] { I_META_FILE_NAME,
      I_META_DOCUMENT_TYPE, I_APPLICATION, I_CREATE_DATE, I_MODIFICATION_DATE, I_META_FILE_SIZE,
      I_DIMENSIONS, I_DIMENSION_IN_INCH, I_RESOLUTION, I_BIT_DEPTH, I_COLOR_MODE, I_COLOR_PROFILE,
      I_META_CREATOR_AUTHOR, I_META_CREATOR_STREET, I_META_CREATOR_LOCATION, I_META_CREATOR_STATE,
      I_META_CREATOR_POSTAL_CODE, I_META_CREATOR_COUNTRY, I_META_CREATOR_TEL, I_META_CREATOR_EMAIL,
      I_META_CREATOR_WWW, I_META_HEADING, I_META_DESCRIPTION, I_META_KEYWORDS,
      I_META_LOCATION_DETAIL, I_META_LOCATION, I_META_STATE, I_META_COUNTRY, I_META_TITEL,
      I_META_COPYRIGHT, I_META_COPYRIGHT_STATUS, I_META_EXPOSURE_INDEX, I_META_FOCAL_RANGE,
      I_META_MAX_BLINDING_VALUE, I_META_RECORDING_DATE, I_META_EXPOSURE_CONTROLS,
      I_META_LIGHT_SOURCE, I_META_SENSOR_TYPE, I_META_BRAND, I_META_MODELL, I_META_SERIAL_NUMBER,
      I_META_PIXEL_X_DIMENSION, I_META_PIXEL_Y_DIMENSION, I_META_HIGH_RESOLUTION,
      I_META_WIDTH_RESOLUTION, I_META_X_RESOLUTION, I_META_Y_RESOLUTION };
  
  public String              ASSET_COVERFLOW_ATTRIBUTE_LABEL;
  public String              ADDRESS_ATTRIBUTE_LABEL;
  public String              DESCRIPTION_ATTRIBUTE_LABEL;
  public String              DUE_DATE_ATTRIBUTE_LABEL;
  public String              FIRST_NAME_ATTRIBUTE_LABEL;
  public String              LIST_PRICE_ATTRIBUTE_LABEL;
  public String              LONG_DESCRIPTION_ATTRIBUTE_LABEL;
  public String              MINIMUM_PRICE_ATTRIBUTE_LABEL;
  public String              PID_ATTRIBUTE_LABEL;
  public String              PIN_CODE_ATTRIBUTE_LABEL;
  public String              SELLING_PRICE_ATTRIBUTE_LABEL;
  public String              SHORT_DESCRIPTION_ATTRIBUTE_LABEL;
  public String              SKU_ATTRIBUTE_LABEL;
  public String              TELEPHONE_NUMBER_ATTRIBUTE_LABEL;
  public String              DISCOUNT_ATTRIBUTE_LABEL;
  public String              GTIN_ATTRIBUTE_LABEL;
  public String              LAST_NAME_ATTRIBUTE_LABEL;
  public String              MAXIMUM_PRICE_ATTRIBUTE_LABEL;
  
  public String[]            ATTRIBUTE_LABELS             = new String[] {
      ASSET_COVERFLOW_ATTRIBUTE_LABEL, ADDRESS_ATTRIBUTE_LABEL, DESCRIPTION_ATTRIBUTE_LABEL,
      DUE_DATE_ATTRIBUTE_LABEL, FIRST_NAME_ATTRIBUTE_LABEL, LIST_PRICE_ATTRIBUTE_LABEL,
      LONG_DESCRIPTION_ATTRIBUTE_LABEL, MINIMUM_PRICE_ATTRIBUTE_LABEL, PID_ATTRIBUTE_LABEL,
      PIN_CODE_ATTRIBUTE_LABEL, SELLING_PRICE_ATTRIBUTE_LABEL, SHORT_DESCRIPTION_ATTRIBUTE_LABEL,
      SKU_ATTRIBUTE_LABEL, TELEPHONE_NUMBER_ATTRIBUTE_LABEL, DISCOUNT_ATTRIBUTE_LABEL,
      GTIN_ATTRIBUTE_LABEL, LAST_NAME_ATTRIBUTE_LABEL, MAXIMUM_PRICE_ATTRIBUTE_LABEL,
      CREATED_BY_ATTRIBUTE, CREATED_ON_ATTRIBUTE, LAST_MODIFIED_ATTRIBUTE,
      LAST_MODIFIED_BY_ATTRIBUTE, NAME_ATTRIBUTE, DTP_MAX_NO_OF_SLOTS_ATTRIBUTE_LABEL,
      DTP_MINIMUM_SLOT_WIDTH_ATTRIBUTE_LABEL, DTP_MINIMUM_SLOT_HEIGHT_ATTRIBUTE_LABEL,
      DTP_ERROR_MARGIN_LABEL, DTP_DELTA_ATTRIBUTE_LABEL };
  
  // Content class
  public String              ARTICLE;
  public String              FILE;
  public String              COLLECTION;
  public String              SET;
  public String              CHANNEL_CLASSES;
  public String              PRODUCT_TYPES;
  public String              SINGLE_ARTICLE;
  public String              ATTRIBUTION_CLASSES;
  public String              CATLOGUE_CLASS;
  public String              WERBESET_CLASS;;
  // Task class ;
  public String              TASK_KLASS;;
  // Asset class ;
  public String              ASSET;
  public String              IMAGE;
  public String              VIDEO;
  public String              DOCUMENT;
  public String              JPEG;
  public String              PNG;
  public String              ICO;
  public String              EPS;
  public String              AI;
  public String              PSD;
  public String              WMV;
  public String              AVI;
  public String              MOV;
  public String              FLV;
  public String              MPEG;
  public String              PDF;
  public String              INDD;
  public String              PPT;
  public String              WORD;
  public String              ATTACHMENT;;
  // Target class ;
  public String              MARKET;
  // Tag types ;
  public String              YES_NEUTRAL_TAG_TYPE_ID;
  public String              YES_NEUTRAL_NO_TAG_TYPE_ID;
  public String              RANGE_TAG_TYPE_ID;
  public String              CUSTOM_TAG_TYPE_ID;
  public String              RULER_TAG_TYPE_ID;
  public String              LIFECYCLE_STATUS_TAG_TYPE_ID;
  public String              LISTING_STATUS_TAG_TYPE_ID;
  public String              STATUS_TAG_TYPE_ID;
  public String              BOOLEAN_TAG_TYPE_ID;
  public String              MASTER_TAG_TYPE_ID;;
  // Tags ;
  public String              STATUS_TAG;
  public String              REGION_TAG;
  public String              AVAILABILITY_TAG;
  public String              LANGUAGE_TAG;
  public String              ENRICHMENT_TAG;
  public String              QUALITY_CHECK_TAG;
  public String              TRANSLATION_TAG;
  public String              APPROVAL_TAG;
  public String              PRODUCTIVE_TAG;
  public String              TASK_STATUS_TAG;
  public String              RESOLUTION_TAG;
  public String              LIFE_STATUS_TAG_ID;
  public String              LISTING_STATUS_TAG_ID;
  public String              IS_ORDER_TAG_ID;
  public String              IS_SALES_TAG_ID;
  public String              IS_BASEUNIT_TAG_ID;
  
  public String              LISTING_STATUS_TAG_LABEL;
  public String              LISTING_STATUS_CATLOG_LABEL;
  public String              LISTING_STATUS_IGNORED_LABEL;
  public String              LISTING_STATUS_REJECTED_LABEL;
  public String              LISTING_STATUS_ONBOARDING_POOL_LABEL;
  public String              LISTING_STATUS_RECOMMENDED_LABEL;
  public String              LISTING_STATUS_SHADOW_ASSORTMENT_LABEL;
  public String              LISTING_STATUS_LISTED_LABEL;
  public String              LIFE_STATUS_TAG_LABEL;
  public String              LIFE_STATUS_INBOX_LABEL;
  public String              LIFE_STATUS_NO_INTEREST_LABEL;
  public String              LIFE_STATUS_PREPARATION_LABEL;
  public String              LIFE_STATUS_ACTIVE_LABEL;
  public String              LIFE_STATUS_ACTIVE_BLOCKED_LABEL;
  public String              LIFE_STATUS_ACTIVE_BANNED_LABEL;
  public String              LIFE_STATUS_RETIRED_LABEL;
  public String              LIFE_STATUS_ARCHIVED_LABEL;
  public String              LIFE_STATUS_DELETED_LABEL;
  public String              STATUS_TAG_LABEL;
  public String              ENRICHMENT_TAG_LABEL;
  public String              QUALITY_CHECK_TAG_LABEL;
  public String              TRANSLATION_TAG_LABEL;
  public String              APPROVAL_TAG_LABEL;
  public String              PRODUCTIVE_TAG_LABEL;
  public String              BLOCKED_FROM_PLANNING_LABEL;
  public String              AVAILABILITY_TAG_LABEL;
  public String              ON_REQUEST_LABEL;
  public String              ON_STOCK_LABEL;
  public String              REGULARLY_AVAILABLE_LABEL;
  public String              DROP_SHIPPING_LABEL;
  public String              LANGUAGE_TAG_LABEL;
  public String              ENGLISH_LABEL;
  public String              GERMAN_LABEL;
  public String              SPANISH_LABEL;
  public String              ITALIAN_LABEL;
  public String              FRENCH_LABEL;
  public String              PORTUGESE_LABEL;
  public String              SWEDISH_LABEL;
  
  public String              SUPPORTED_LANGUAGES;
  public String              EN;
  public String              DE;
  public String              FR;
  public String              ES;
  // other 4
  
  public String              T_STATUS_SYSTEM_LABEL;
  public String              T_STATUS_SYSTEM_NEU_LABEL;
  public String              T_STATUS_SYSTEM_INAKTIV_LABEL;
  public String              T_STATUS_SYSTEM_AKTIV_LABEL;
  public String              T_STATUS_SYSTEM_ARCHIVIERT_LABEL;
  public String              T_STATUS_SYSTEM_GELOESCHT_LABEL;
  public String              T_STATUS_SYSTEM_PREPARE_LABEL;
  public String              T_STATUS_SYSTEM_BLOCKED_LABEL;
  public String              T_STATUS_PROJECT_LABEL;
  public String              T_CAMPAIGN_GOALS_LABEL;
  public String              T_GOALS_BRAND_PROMOTION_LABEL;
  public String              T_GOALS_SEASONAL_PROMOTION_LABEL;
  public String              T_GOALS_NEW_ARRIVALS_LABEL;
  public String              T_GOALS_PROMOTION_HIGH_MARGIN_LABEL;
  public String              T_GOALS_INVENTORY_CLEARANCE_LABEL;
  public String              T_GOALS_LOYALTY_POINTS_LABEL;
  public String              T_GOALS_SPECIAL_TOPIC_LABEL;
  public String              T_GOALS_ACQUIRE_NEW_CUSTOMERS_LABEL;
  public String              T_GOALS_RETAIN_EXISTING_CUSTOMERS_LABEL;
  public String              T_CAMP_TYPE_LABEL;
  public String              T_CAMP_TYPE_NATIONAL_LABEL;
  public String              T_CAMP_TYPE_REGIONAL_LABEL;
  public String              T_CAMP_TYPE_CATALINA_LABEL;
  public String              T_CAMP_TYPE_DEUTSCHLANDCARD_LABEL;
  public String              T_CAMP_TYPE_DCARD_LABEL;
  public String              T_CAMP_TYPE_LIEFERANT_LABEL;
  public String              TASK_STATUS_TAG_LABEL;
  public String              PLANNED_LABEL;
  public String              READY_LABEL;
  public String              DECLINED_LABEL;
  public String              IN_PROGRESS_LABEL;
  public String              DONE_LABEL;
  public String              VERIFIED_LABEL;
  public String              SIGNED_OFF_LABEL;
  public String              RESOLUTION_TAG_LABEL;
  public String              RESOLUTION_300P_LABEL;
  public String              RESOLUTION_150P_LABEL;
  public String              RESOLUTION_72P_LABEL;
  public String              IMAGE_EXTENSION_LABEL;
  public String              IMAGE_FORMAT_JPG_LABEL;
  public String              IMAGE_FORMAT_PNG_LABEL;
  public String              IMAGE_FORMAT_ORIGINAL_LABEL;
  public String[]            TAG_LABELS                   = new String[] { LISTING_STATUS_TAG_LABEL,
      LISTING_STATUS_CATLOG_LABEL, LISTING_STATUS_IGNORED_LABEL, LISTING_STATUS_REJECTED_LABEL,
      LISTING_STATUS_ONBOARDING_POOL_LABEL, LISTING_STATUS_RECOMMENDED_LABEL,
      LISTING_STATUS_SHADOW_ASSORTMENT_LABEL, LISTING_STATUS_LISTED_LABEL, LIFE_STATUS_TAG_LABEL,
      LIFE_STATUS_INBOX_LABEL, LIFE_STATUS_NO_INTEREST_LABEL, LIFE_STATUS_PREPARATION_LABEL,
      LIFE_STATUS_ACTIVE_LABEL, LIFE_STATUS_ACTIVE_BLOCKED_LABEL, LIFE_STATUS_ACTIVE_BANNED_LABEL,
      LIFE_STATUS_RETIRED_LABEL, LIFE_STATUS_ARCHIVED_LABEL, LIFE_STATUS_DELETED_LABEL,
      STATUS_TAG_LABEL, ENRICHMENT_TAG_LABEL, QUALITY_CHECK_TAG_LABEL, TRANSLATION_TAG_LABEL,
      APPROVAL_TAG_LABEL, PRODUCTIVE_TAG_LABEL, BLOCKED_FROM_PLANNING_LABEL, AVAILABILITY_TAG_LABEL,
      ON_REQUEST_LABEL, ON_STOCK_LABEL, REGULARLY_AVAILABLE_LABEL, DROP_SHIPPING_LABEL,
      LANGUAGE_TAG_LABEL, ENGLISH_LABEL, GERMAN_LABEL, SPANISH_LABEL, ITALIAN_LABEL, FRENCH_LABEL,
      PORTUGESE_LABEL, SWEDISH_LABEL };
  // event
  public String              CREATED_EVENT_LABEL;
  
  // Lang taxonomy
  public String              LANGUAGE_TAXONOMY_LABEL;
  
  public String              PRICING_PROPERTY_COLLECTION_LABEL;
  public String              ARTICLE_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL;
  public String              MARKET_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL;
  public String              ASSET_GENERAL_INFORMATION_PROPERTY_COLLETION_LABEL;
  public String              CAMPAIGN_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL;
  public String              PROMOTION_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL;
  public String              SUPPLIER_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL;
  public String              TEXTASSET_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL;
  public String              DEFAULT_X_RAY_PROPERTY_COLLECTION_LABEL;
  public String              METADATA_COLLECTION_LABEL;
  public String              UNIT_DEFAULT_PROPERTYCOLLECTION_LABEL;
  
  public String[]            PROPERTY_COLLECTION_LABELS   = new String[] {
      PRICING_PROPERTY_COLLECTION_LABEL, ARTICLE_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL,
      MARKET_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL,
      ASSET_GENERAL_INFORMATION_PROPERTY_COLLETION_LABEL,
      CAMPAIGN_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL,
      PROMOTION_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL,
      SUPPLIER_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL,
      TEXTASSET_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL,
      DEFAULT_X_RAY_PROPERTY_COLLECTION_LABEL, METADATA_COLLECTION_LABEL,
      UNIT_DEFAULT_PROPERTYCOLLECTION_LABEL,
      DTP_TEMPLATE_GENERAL_INFORMATION_PROPERTY_COLLECTION_LABEL,
      DTP_DOCUMENT_TEMPLATE_INFORMATION_PROPERTY_COLLECTION_LABEL,
      DTP_TEMPLATE_FILE_INFOMATION_PROPERTY_COLLECTION_LABEL };
  
  public String              METADATA_COLLECTION;
  
  // Tags
  public String              IS_ORDER_TAG_LABEL;
  public String              IS_SALES_TAG_LABEL;
  public String              IS_BASEUNIT_TAG_LABEL;
  
  // LanguageTag ;
  public String              ENGLISH;
  public String              GERMAN;
  public String              SPANISH;
  public String              ITALIAN;
  public String              FRENCH;
  public String              PORTUGESE;
  public String              SWEDISH;;
  // AvailabilityTag ;
  public String              BLOCKED_FROM_PLANNING;
  public String              ON_STOCK;
  public String              REGULARLY_AVAILABLE;
  public String              ON_REQUEST;
  public String              DROP_SHIPPING;
  public String              PRO_SPECIFIC_TAG;
  public String              PRO_SPECIFIC_TAG1;
  
  // LifeStatusTags ;
  public String              LIFE_STATUS_INBOX;
  public String              LIFE_STATUS_NO_INTEREST;
  public String              LIFE_STATUS_PREPARATION;
  public String              LIFE_STATUS_ACTIVE;
  public String              LIFE_STATUS_ACTIVE_BLOCKED;
  public String              LIFE_STATUS_ACTIVE_BANNED;
  public String              LIFE_STATUS_RETIRED;
  public String              LIFE_STATUS_ARCHIVED;
  public String              LIFE_STATUS_DELETED;;
  // ListingStatus Tags ;
  public String              LISTING_STATUS_CATLOG;
  public String              LISTING_STATUS_IGNORED;
  public String              LISTING_STATUS_REJECTED;
  public String              LISTING_STATUS_ONBOARDING_POOL;
  public String              LISTING_STATUS_RECOMMENDED;
  public String              LISTING_STATUS_SHADOW_ASSORTMENT;
  public String              LISTING_STATUS_LISTED;;
  // isSales Boolean Tag ;
  public String              IS_SALES_TAG_VALUE;;
  // isOrder Boolean Tag ;
  public String              IS_ORDER_TAG_VALUE;;
  // isPromotion Boolean Tag ;
  public String              IS_BASEUNIT_TAG_VALUE;
  
  // project status
  
  public String              T_STATUS_PROJEKT_VERWENDBAR;
  public String              T_STATUS_PROJEKT_IN_PLANUNG;
  public String              T_STATUS_PROJEKT_ABGESCHLOSSEN;
  public String              T_STATUS_PROJEKT_ZUR_UBERARBEITUNG;
  public String              T_STATUS_PROJEKT_QUALITAT_SICHERN;
  public String              T_STATUS_PROJEKT_FEHLER_BEARBEITEN;
  public String              T_STATUS_PROJEKT_LIZENZ_FEHLT;
  public String              T_STATUS_PROJEKT_ABGEBROCHEN;;
  // Relationships ;
  public String              STANDARD_MARKET_ASSET_RELATIONSHIP_ID;
  public String              STANDARD_ARTICLE_ASSET_RELATIONSHIP_ID;
  public String              STANDARD_PROMOTION_ASSET_RELATIONSHIP_ID;
  public String              STANDARD_TEXTASSET_ASSET_RELATIONSHIP_ID;
  public String              STANDARD_CAMPAIGN_ASSET_RELATIONSHIP_ID;
  public String              STANDARD_SUPPLIER_ASSET_RELATIONSHIP_ID;
  public String              STANDARD_ARTICLE_MARKET_RELATIONSHIP_ID;
  public String              STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID;
  
  public String              STANDARD_ARTICLE_MARKET_RELATIONSHIP_LABEL;
  
  public String              STANDARD_ARTICLE_MARKETING_ARTICLE_RELATIONSHIP_LABEL;
  public String              STANDARD_MARKETING_ARTICLE_ASSET_RELATIONSHIP_LABEL;
  public String              STANDARD_MARKETING_BUNDLE_ASSET_RELATIONSHIP_LABEL;
  
  public String              STANDARD_TARGET_KLASS_MARKET_LABEL;;
  // ResolutionContext ;
  public String              RESOLUTION_CONTEXT;;
  public String              IMAGE_ATTRIBUTE_INSTANCE;;
  // Promotion ;
  public String              PROMOTION;;
  // TextAsset ;
  public String              TEXT_ASSET;;
  // Campaign ;
  public String              CAMPAIGN;;
  // Supplier Klass ;
  public String              SUPPLIER;;
  // Supplier Klass ;
  public String              TAXONOMY_ATTRIBUTION;;
  // Events ;
  public String              CREATED_EVENT_ID;;
  // template tab lables ;
  public String              TEMPLATE_HOME_TAB_LABEL;
  public String              TEMPLATE_RELATIONSHIP_TAB_LABEL;
  public String              TEMPLATE_CONTEXTUAL_TAB_LABEL;
  public String              TEMPLATE_LANGUAGE_TAB_LABEL;
  public String              TEMPLATE_PROMO_VERSION_TAB_LABEL;
  public String              TEMPLATE_TASKS_TAB_LABEL;
  /* public List<String> MANDATORY_ATTRIBUTES                                ;
  NAME_ATTRIBUTE, CREATED_ON_ATTRIBUTE, CREATED_BY_ATTRIBUTE, LAST_MODIFIED_ATTRIBUTE,  ;
  LAST_MODIFIED_BY_ATTRIBUTE);  ;*/
  ;
  /* public List<String> UNIT_DEFAULT_PC_TAGS                                ;
    .asList(IS_ORDER_TAG_ID, IS_SALES_TAG_ID, IS_BASEUNIT_TAG_ID);  ;
  ;*/
  public String              UNIT_DEFAULT_PC_ID;;
  public String              STANDARD_ORGANIZATION;
  public String              STANDARD_SYSTEM;
  
  // target klass
  
  public String              PROMOTION_LABEL;
  public String              SUPPLIER_LABEL;
  public String              MARKET_LABEL;              
  
  public String              STANDARD_CAMPAIGN_KLASS_LABEL;
  
  public String[]            TARGET_KLASS_LABELS          = new String[] { MARKET_LABEL };
  
  public String              ADMIN_LABEL;
  
  // Role
  public String              RESPONSIBLE_ROLE_LABEL;
  public String              ACCOUNTABLE_ROLE_LABEL;
  public String              CONSULTED_ROLE_LABEL;
  public String              INFORMED_ROLE_LABEL;
  public String              VERIFY_ROLE_LABEL;
  public String              SIGNOFF_ROLE_LABEL;
  public String              ADMIN_ROLE_LABEL;
  
  public String              APPROVER_ROLE_LABEL;
  public String              OWNER_ROLE_LABEL;
  public String              SUBSCRIBER_ROLE_LABEL;
  public String              CONTRIBUTOR_ROLE_LABEL;
  public String              REVIEWER_ROLE_LABEL;
  public String              SENIOR_MANAGER_ROLE_LABEL;
  public String              ASSIGNEE_ROLE_LABEL;
  
  public String[]            RACIVS_ROLE_LABELS           = new String[] { RESPONSIBLE_ROLE_LABEL,
      ACCOUNTABLE_ROLE_LABEL, CONSULTED_ROLE_LABEL, INFORMED_ROLE_LABEL, VERIFY_ROLE_LABEL,
      SIGNOFF_ROLE_LABEL, ADMIN_ROLE_LABEL };
  
  public String[]            ROLES                        = new String[] { APPROVER_ROLE_LABEL,
      OWNER_ROLE_LABEL, SUBSCRIBER_ROLE_LABEL, CONTRIBUTOR_ROLE_LABEL, REVIEWER_ROLE_LABEL,
      SENIOR_MANAGER_ROLE_LABEL, ASSIGNEE_ROLE_LABEL };
  
  public String              STANDARD_ARTICLE_ASSET_RELATIONSHIP_LABEL;
  public String              STANDARD_MARKET_ASSET_RELATIONSHIP_LABEL;
  public String              STANDARD_CAMPAIGN_ASSET_RELATIONSHIP_LABEL;
  public String              STANDARD_CAMPAIGN_CAMPAIGN_RELATIONSHIP_LABEL;
  public String              STANDARD_TEXTASSET_ASSET_RELATIONSHIP_LABEL;
  public String              STANDARD_SUPPLIER_ASSET_RELATIONSHIP_LABEL;
  public String              STANDARD_PROMOTION_ASSET_RELATIONSHIP_LABEL;
  
  public String              PAGE_PREVIEWS;
  public String              IMAGES;
  
  public String              STANDARD_DTP_DOCUMENT_TEMPLATE_LINKED_ARTICLES_RELATIONSHIP_LABEL;
  public String              STANDARD_DTP_DOCUMENT_TEMPLATE_LINKED_PRODUCT_TEMPLATES_RELATIONSHIP_LABEL;
  
  public String              ARTICLES_LABEL;
  public String              TARGETS_LABEL;
  public String              CAMPAIGN_LABEL;
  public String              TEXTASSET_LABEL;
  
  public String              STANDARD_VIDEO_KLASS_LABEL;
  public String              STANDARD_ASSET_KLASS_LABEL;
  public String              STANDARD_DOCUMENT_KLASS_LABEL;
  public String              STANDARD_IMAGE_KLASS_LABEL;
  public String              STANDARD_ATTACHMENT_KLASS_LABEL;
  public String              STANDARD_SMARTDOCUMENT_KLASS_LABEL;
  
  public String              STANDARD_ARTICLE_KLASS_LABEL;
  public String              STANDARD_SET_KLASS_LABEL;
  public String              STANDARD_COLLETION_KLASS_LABEL;
  public String              STANDARD_CHANNEL_CLASSES_LABEL;
  public String              STANDARD_PRODUCT_TYPES_LABEL;
  public String              STANDARD_SINGLE_ARTICLE;
  public String              STANDARD_ATTRIBUTION_CLASSES_LABEL;
  
  public String              STANDARD_FILE_KLASS_LABEL;
  public String              STANDARD_VERSION_KLASS_LABEL;
  public String              STANDARD_GOLDEN_ARTICLE_KLASS_LABEL;
  
  public String              STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_LABEL;
  
  // Marketing Article
  public String              MARKETING_PRODUCT_LABEL;
  
  // Marketing Bundle
  public String              MARKETING_BUNDLE_LABEL;
  
  public String              STANDARD_OVERVIEW_TAB_LABEL;
  public String              STANDARD_PROPERTY_COLLECTION_TAB_LABEL;
  public String              STANDARD_RELATIONSHIP_TAB_LABEL;
  public String              STANDARD_CONTEXT_TAB_LABEL;
  public String              STANDARD_TASK_TAB_LABEL;
  public String              STANDARD_TIMELINE_TAB_LABEL;
  public String              STANDARD_RENDITION_TAB_LABEL;
  public String              STANDARD_CONFIGURATION_TAB_LABEL;
  public String              STANDARD_MDM_TAB_LABEL;
  public String              STANDARD_DUPLICATE_TAB_LABEL;
  
  public String[]            RELATIONSHIP_LABELS          = new String[] {
      STANDARD_ARTICLE_ASSET_RELATIONSHIP_LABEL, STANDARD_MARKET_ASSET_RELATIONSHIP_LABEL,
      STANDARD_CAMPAIGN_ASSET_RELATIONSHIP_LABEL, STANDARD_CAMPAIGN_CAMPAIGN_RELATIONSHIP_LABEL,
      STANDARD_TEXTASSET_ASSET_RELATIONSHIP_LABEL, STANDARD_SUPPLIER_ASSET_RELATIONSHIP_LABEL,
      STANDARD_PROMOTION_ASSET_RELATIONSHIP_LABEL, STANDARD_ARTICLE_MARKET_RELATIONSHIP_LABEL,
      STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_LABEL,
      STANDARD_MARKETING_ARTICLE_ASSET_RELATIONSHIP_LABEL,
      STANDARD_MARKETING_BUNDLE_ASSET_RELATIONSHIP_LABEL,
      STANDARD_DTP_DOCUMENT_TEMPLATE_ASSET_RELATIONSHIP_LABEL,
      STANDARD_DTP_DOCUMENT_TEMPLATE_LINKED_MARKETING_ARTICLES_RELATIONSHIP_LABEL,
      STANDARD_DTP_DOCUMENT_TEMPLATE_LINKED_MARKETING_BUNDLES_RELATIONSHIP_LABEL,
      STANDARD_DTP_DOCUMENT_TEMPLATE_LINKED_PRODUCT_TEMPLATES_RELATIONSHIP_LABEL,
      STANDARD_DTP_DOCUMENT_TEMPLATE_PAGE_PREVIEW_RELATIONSHIP_LABEL,
      STANDARD_DTP_PRODUCT_TEMPLATE_ASSET_RELATIONSHIP_LABEL,
      STANDARD_DTP_PUBLICATION_DTP_DOCUMENT_TEMPLATE_RELATIONSHIP_LABEL,
      STANDARD_DTP_PUBLICATION_DTP_PRODUCT_TEMPLATE_RELATIONSHIP_LABEL,
      STANDARD_DTP_PUBLICATION_DTP_PUBLICATION_TEMPLATE_RELATIONSHIP_LABEL,
      STANDARD_DTP_PUBLICATION_TEMPLATE_ASSET_RELATIONSHIP_LABEL,
      STANDARD_DTP_PUBLICATION_TEMPLATE_DTP_DOCUMENT_TEMPLATE_RELATIONSHIP_LABEL };
  
  public String[]            RELATIONSHIP_SIDE_1_LABELS   = new String[] { ARTICLES_LABEL,
      MARKET_LABEL, CAMPAIGN_LABEL, CAMPAIGN_LABEL,
      TEXTASSET_LABEL, SUPPLIER_LABEL, PROMOTION_LABEL, ARTICLES_LABEL,
      ARTICLES_LABEL, MARKETING_PRODUCT_LABEL,
      MARKETING_BUNDLE_LABEL,
      STANDARD_DTP_DOCUMENT_TEMPLATE_LABEL,
      STANDARD_DTP_DOCUMENT_TEMPLATE_LABEL, STANDARD_DTP_DOCUMENT_TEMPLATE_LABEL,
      STANDARD_DTP_DOCUMENT_TEMPLATE_LABEL, STANDARD_DTP_DOCUMENT_TEMPLATE_LABEL,
      STANDARD_DTP_PRODUCT_TEMPLATE_LABEL, STANDARD_DTP_PUBLICATION_LABEL,
      STANDARD_DTP_PUBLICATION_LABEL, STANDARD_DTP_PUBLICATION_LABEL,
      STANDARD_DTP_PUBLICATION_TEMPLATE_LABEL, STANDARD_DTP_PUBLICATION_TEMPLATE_LABEL };
  
  public String[]            RELATIONSHIP_SIDE_2_LABELS   = new String[] { IMAGES, IMAGES, IMAGES,
      IMAGES, IMAGES, CAMPAIGN_LABEL, IMAGES, IMAGES, IMAGES, MARKET_LABEL, IMAGES,
      STANDARD_GOLDEN_ARTICLE_KLASS_LABEL, IMAGES, IMAGES, MARKET_LABEL,
      IMAGES, IMAGES, IMAGES, MARKETING_PRODUCT_LABEL, MARKETING_BUNDLE_LABEL,
      STANDARD_DTP_PRODUCT_TEMPLATE_LABEL, PAGE_PREVIEWS, IMAGES,
      STANDARD_DTP_DOCUMENT_TEMPLATE_LABEL, STANDARD_DTP_PRODUCT_TEMPLATE_LABEL,
      STANDARD_DTP_PUBLICATION_TEMPLATE_LABEL, IMAGES, STANDARD_DTP_DOCUMENT_TEMPLATE_LABEL };
  
  public String[]            PIM_KLASS_LABELS             = new String[] {
      STANDARD_ARTICLE_KLASS_LABEL, STANDARD_PRODUCT_TYPES_LABEL, STANDARD_CHANNEL_CLASSES_LABEL,
      STANDARD_ATTRIBUTION_CLASSES_LABEL, STANDARD_SINGLE_ARTICLE, STANDARD_FILE_KLASS_LABEL,
      STANDARD_VERSION_KLASS_LABEL, STANDARD_GOLDEN_ARTICLE_KLASS_LABEL };
  
  public String[]            MAM_KLASS_LABELS             = new String[] {
      STANDARD_ATTACHMENT_KLASS_LABEL, STANDARD_ASSET_KLASS_LABEL, STANDARD_IMAGE_KLASS_LABEL,
      STANDARD_VIDEO_KLASS_LABEL, STANDARD_DOCUMENT_KLASS_LABEL,
      STANDARD_SMARTDOCUMENT_KLASS_LABEL };
  
  public String[]            STANDARD_TAB_LABELS          = new String[] {
      STANDARD_OVERVIEW_TAB_LABEL, STANDARD_PROPERTY_COLLECTION_TAB_LABEL,
      STANDARD_RELATIONSHIP_TAB_LABEL, STANDARD_CONTEXT_TAB_LABEL, STANDARD_TASK_TAB_LABEL,
      STANDARD_TIMELINE_TAB_LABEL, STANDARD_RENDITION_TAB_LABEL,
      STANDARD_CONFIGURATION_TAB_LABEL, STANDARD_MDM_TAB_LABEL, STANDARD_DUPLICATE_TAB_LABEL };
  
  public String              LANGUAGE;
  public String              EN_US;
  public String              EN_UK;
  public String              DE_DE;
  public String              FR_FR;
  public String              ES_ES;
  
  public Map<String, String> Language_Labels              = new HashMap<String, String>()
                                                          {
                                                            
                                                            {
                                                              put(Constants.ENGLISH_US,
                                                                  ENGLISH_LABEL);
                                                              put(Constants.FRENCH, FRENCH_LABEL);
                                                              put(Constants.SPANISH, SPANISH_LABEL);
                                                              put(Constants.GERMAN, GERMAN_LABEL);
                                                            }
                                                          };
  
  public Map<String, String> Tabs_Labels                  = new HashMap<String, String>()
                                                          {
                                                            
                                                            {
                                                              put(SystemLevelIds.DEFAULT_DASHBOARD_TAB_ID,
                                                                  DEFAULT_DASHBOARD_TAB);
                                                              put(SystemLevelIds.DEFAULT_DATA_INTEGRATION_TAB_ID,
                                                                  DEFAULT_DATA_INTEGRATION_TAB);
                                                            }
                                                          };
  
  // Tags MAP
  public Map<String, String> TAGS_LABELS                  = new HashMap<String, String>()
                                                          {
                                                            
                                                            {
                                                              // availability
                                                              put("availabilitytag",
                                                                  AVAILABILITY_TAG_LABEL);
                                                              put("onrequest", ON_REQUEST_LABEL);
                                                              put("onstock", ON_STOCK_LABEL);
                                                              put("regularlyavailable",
                                                                  REGULARLY_AVAILABLE_LABEL);
                                                              put("dropshipping",
                                                                  DROP_SHIPPING_LABEL);
                                                              
                                                              // lagnguage tag 8
                                                                                                                                                 /*put("languagetag", LANGUAGE_TAG_LABEL);
                                                                                                                                                 put("english", ENGLISH_LABEL);
                                                                                                                                                 put("german", GERMAN_LABEL);
                                                                                                                                                 put("spanish", SPANISH_LABEL);
                                                                                                                                                 put("italian", ITALIAN_LABEL);
                                                                                                                                                 put("french", FRENCH_LABEL);
                                                                                                                                                 put("portugese", PORTUGESE_LABEL);
                                                                                                                                                 put("swedish", SWEDISH_LABEL);*/
                                                                                                                                                 
                                                              // life cylcel
                                                              // status tag 10
                                                              put("lifestatustag",
                                                                  LIFE_STATUS_TAG_LABEL);
                                                              put("life_status_inbox",
                                                                  LIFE_STATUS_INBOX_LABEL);
                                                              put("life_status_nointerest",
                                                                  LIFE_STATUS_NO_INTEREST_LABEL);
                                                              put("life_status_preparation",
                                                                  LIFE_STATUS_PREPARATION_LABEL);
                                                              put("life_status_active",
                                                                  LIFE_STATUS_ACTIVE_LABEL);
                                                              put("life_status_activeblocked",
                                                                  LIFE_STATUS_ACTIVE_BLOCKED_LABEL);
                                                              put("life_status_activebanned",
                                                                  LIFE_STATUS_ACTIVE_BANNED_LABEL);
                                                              put("life_status_retired",
                                                                  LIFE_STATUS_RETIRED_LABEL);
                                                              put("life_status_archived",
                                                                  LIFE_STATUS_ARCHIVED_LABEL);
                                                              put("life_status_deleted",
                                                                  LIFE_STATUS_DELETED_LABEL);
                                                              
                                                              // listing status
                                                              // tag 8
                                                              put("listingstatustag",
                                                                  LISTING_STATUS_TAG_LABEL);
                                                              put("listing_status_catlog",
                                                                  LISTING_STATUS_CATLOG_LABEL);
                                                              put("listing_status_ignored",
                                                                  LISTING_STATUS_IGNORED_LABEL);
                                                              put("listing_status_rejected",
                                                                  LISTING_STATUS_REJECTED_LABEL);
                                                              put("listing_status_onboardingpool",
                                                                  LISTING_STATUS_ONBOARDING_POOL_LABEL);
                                                              put("listing_status_recommended",
                                                                  LISTING_STATUS_RECOMMENDED_LABEL);
                                                              put("listing_status_shadowassortment",
                                                                  LISTING_STATUS_SHADOW_ASSORTMENT_LABEL);
                                                              put("listing_status_listed",
                                                                  LISTING_STATUS_LISTED_LABEL);
                                                              
                                                              // system status
                                                              // tag
                                                              // 8
                                                              put("T_STATUS_SYSTEM",
                                                                  T_STATUS_SYSTEM_LABEL);
                                                              put("T_STATUS_SYSTEM_NEW",
                                                                  T_STATUS_SYSTEM_NEU_LABEL);
                                                              put("T_STATUS_SYSTEM_INACTIV",
                                                                  T_STATUS_SYSTEM_INAKTIV_LABEL);
                                                              put("T_STATUS_SYSTEM_ACTIV",
                                                                  T_STATUS_SYSTEM_AKTIV_LABEL);
                                                              put("T_STATUS_SYSTEM_ARCHIVED",
                                                                  T_STATUS_SYSTEM_ARCHIVIERT_LABEL);
                                                              put("T_STATUS_SYSTEM_DELETED",
                                                                  T_STATUS_SYSTEM_GELOESCHT_LABEL);
                                                              put("T_STATUS_SYSTEM_PREPARE",
                                                                  T_STATUS_SYSTEM_PREPARE_LABEL);
                                                              put("T_STATUS_SYSTEM_BLOCKED",
                                                                  T_STATUS_SYSTEM_BLOCKED_LABEL);
                                                              
                                                              // Campaign Goals
                                                              // tag
                                                              put("T_CAMPAIGN_GOALS",
                                                                  T_CAMPAIGN_GOALS_LABEL);
                                                              put("T_GOALS_BRAND_PROMOTION",
                                                                  T_GOALS_BRAND_PROMOTION_LABEL);
                                                              put("T_GOALS_SEASONAL_PROMOTION",
                                                                  T_GOALS_SEASONAL_PROMOTION_LABEL);
                                                              put("T_GOALS_NEW_ARRIVALS",
                                                                  T_GOALS_NEW_ARRIVALS_LABEL);
                                                              put("T_GOALS_PROMOTION_HIGH_MARGIN",
                                                                  T_GOALS_PROMOTION_HIGH_MARGIN_LABEL);
                                                              put("T_GOALS_INVENTORY_CLEARANCE",
                                                                  T_GOALS_INVENTORY_CLEARANCE_LABEL);
                                                              put("T_GOALS_LOYALTY_POINTS",
                                                                  T_GOALS_LOYALTY_POINTS_LABEL);
                                                              put("T_GOALS_SPECIAL_TOPIC",
                                                                  T_GOALS_SPECIAL_TOPIC_LABEL);
                                                              put("T_GOALS_ACQUIRE_NEW_CUSTOMERS",
                                                                  T_GOALS_ACQUIRE_NEW_CUSTOMERS_LABEL);
                                                              put("T_GOALS_RETAIN_EXISTING_CUSTOMERS",
                                                                  T_GOALS_RETAIN_EXISTING_CUSTOMERS_LABEL);
                                                              
                                                              // project status
                                                              // 9
                                                              
                                                              put("T_STATUS_PROJECT",
                                                                  T_STATUS_PROJECT_LABEL);
                                                              put("T_STATUS_PROJECT_IN_PLANNING",
                                                                  T_STATUS_PROJEKT_IN_PLANUNG);
                                                              put("T_STATUS_PROJECT_CANCELED",
                                                                  T_STATUS_PROJEKT_ABGEBROCHEN);
                                                              put("T_STATUS_PROJECT_COMPLETE",
                                                                  T_STATUS_PROJEKT_ABGESCHLOSSEN);
                                                              put("T_STATUS_PROJECT_ERROR_PROCESSING",
                                                                  T_STATUS_PROJEKT_FEHLER_BEARBEITEN);
                                                              put("T_STATUS_PROJECT_LICENSE_MISSING",
                                                                  T_STATUS_PROJEKT_LIZENZ_FEHLT);
                                                              put("T_STATUS_PROJECT_QUALITY_CHECK",
                                                                  T_STATUS_PROJEKT_QUALITAT_SICHERN);
                                                              // put("T_STATUS_PROJECT_USING_ALLOWED",
                                                              // T_STATUS_PROJEKT_VERWENDBAR);
                                                              put("T_STATUS_PROJECT_REWORK",
                                                                  T_STATUS_PROJEKT_ZUR_UBERARBEITUNG);
                                                              
                                                              // campaign type
                                                              // tag
                                                              // 6
                                                              put("T_CAMP_TYPE", T_CAMP_TYPE_LABEL);
                                                              put("T_CAMP_TYPE_NATIONAL",
                                                                  T_CAMP_TYPE_NATIONAL_LABEL);
                                                              put("T_CAMP_TYPE_REGIONAL",
                                                                  T_CAMP_TYPE_REGIONAL_LABEL);
                                                              put("T_CAMP_TYPE_CATALINA",
                                                                  T_CAMP_TYPE_CATALINA_LABEL);
                                                              put("T_CAMP_TYPE_DCARD",
                                                                  T_CAMP_TYPE_DEUTSCHLANDCARD_LABEL);
                                                              put("T_CAMP_TYPE_SUPPLIER",
                                                                  T_CAMP_TYPE_LIEFERANT_LABEL);
                                                              
                                                              // task status tag
                                                              // 8
                                                              put("taskstatustag",
                                                                  TASK_STATUS_TAG_LABEL);
                                                              put("taskplanned", PLANNED_LABEL);
                                                              put("taskready", READY_LABEL);
                                                              put("taskdeclined", DECLINED_LABEL);
                                                              put("taskinprogress",
                                                                  IN_PROGRESS_LABEL);
                                                              put("taskdone", DONE_LABEL);
                                                              put("taskverified", VERIFIED_LABEL);
                                                              put("tasksignedoff",
                                                                  SIGNED_OFF_LABEL);
                                                              
                                                              // resolution tag
                                                              // 4
                                                              put("resolutiontag",
                                                                  RESOLUTION_TAG_LABEL);
                                                              put("resolution_300P",
                                                                  RESOLUTION_300P_LABEL);
                                                              put("resolution_150P",
                                                                  RESOLUTION_150P_LABEL);
                                                              put("resolution_72P",
                                                                  RESOLUTION_72P_LABEL);
                                                              
                                                              // image extension
                                                              // tag 5
                                                              put("imageextensiontag",
                                                                  IMAGE_EXTENSION_LABEL);
                                                              put("image_extension_format_original",
                                                                  IMAGE_FORMAT_ORIGINAL_LABEL);
                                                              put("image_extension_format_jpg",
                                                                  IMAGE_FORMAT_JPG_LABEL);
                                                              put("image_extension_format_png",
                                                                  IMAGE_FORMAT_PNG_LABEL);
                                                              
                                                              // boolean tag 6
                                                              put("isordertag", IS_ORDER_TAG_LABEL);
                                                              put("isordertagvalue",
                                                                  IS_ORDER_TAG_LABEL);
                                                              put("issalestag", IS_SALES_TAG_LABEL);
                                                              put("issalestagvalue",
                                                                  IS_SALES_TAG_LABEL);
                                                              put("isbaseunittag",
                                                                  IS_BASEUNIT_TAG_LABEL);
                                                              put("isbaseunittagvalue",
                                                                  IS_BASEUNIT_TAG_LABEL);
                                                              
                                                              // suported
                                                              // languages
                                                                                                                                                 /*                                                            put("supportedLanguages",
                                                                                                                                                     SUPPORTED_LANGUAGES);
                                                                                                                                                 put("en", EN);
                                                                                                                                                 put("dr", DE);
                                                                                                                                                 put("es", ES);
                                                                                                                                                 put("fr", FR);
                                                                                                                                                 
                                                                                                                                                 put("language", LANGUAGE);
                                                                                                                                                 put("en_US", EN_US);
                                                                                                                                                 put("en_UK", EN_UK);
                                                                                                                                                 put("de_DE", DE_DE);
                                                                                                                                                 put("fr_FR", FR_FR);
                                                                                                                                                 put("es_ES", ES_ES);*/
                                                            }
                                                          };
  
  // Smart Document
  public String              SMART_DOCUMENT_LABEL;
  
  // supplier classes
  public String              SUPPLIERS_LABEL;
  public String              MARKETPLACES_LABEL;
  public String              DISTRIBUTORS_LABEL;
  public String              WHOLESALERS_LABEL;
  public String              TRANSLATION_AGENCY_LABEL;
  public String              CONTENT_ENRICHMENT_AGENCY_LABEL;
  public String              DIGITAL_ASSET_AGENCY_LABEL;
  // Tags MAP
  
  // References
  public String              STANDARD_ARTICLE_SMART_DOCUMENT_REFRENCE_ID;
  public String              STANDARD_MARKETING_ARTICLE_SMART_DOCUMENT_REFRENCE_ID;
  
  public String[]            REFERENCE_LABELS             = new String[] {
      STANDARD_ARTICLE_SMART_DOCUMENT_REFRENCE_ID,
      STANDARD_MARKETING_ARTICLE_SMART_DOCUMENT_REFRENCE_ID };
  
  public String[]            REFERENCE_SIDE_1_LABELS      = new String[] { ARTICLES_LABEL,
      MARKETING_PRODUCT_LABEL };
  
  public String[]            REFERENCE_SIDE_2_LABELS      = new String[] { SMART_DOCUMENT_LABEL,
      SMART_DOCUMENT_LABEL };
}
