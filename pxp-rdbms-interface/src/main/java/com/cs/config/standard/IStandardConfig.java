package com.cs.config.standard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The place to define all standard configuration /!\ This file is the source of information to generate SQL file 1-ac that contains the
 * insert commands of standard configuration objects.
 *
 * @author vallee
 */
public interface IStandardConfig {

  public final String FALSE = "false";
  public final String TRUE = "true";
  public final String GLOBAL_LOCALE = "en_US";
  
  public final String STANDARD_ORGANIZATION_CODE = "-1";
  public final String STANDARD_ORGANIZATION_RCODE = "stdo";


  public static boolean isStandardOrganization( String organizationCode) {
    return ( organizationCode.equals(STANDARD_ORGANIZATION_CODE) ||
        organizationCode.equals(STANDARD_ORGANIZATION_RCODE));
  }


  /**
   * @param iid iid of a property
   * @param code of a property
   * @return true when the iid, or by default the code, of the property corresponds to a tracking property
   */
  public static boolean isTrackingProperty(long iid, String code) {
    if (iid > 0L) {
      return StandardProperty.TrackingPropertyIIDs.contains(iid);
    } // else
    return StandardProperty.TrackingPropertyCodes.contains(code);
  }

  public enum StandardUser {

    admin;

    public long getIID() {
      return 10 + this.ordinal();
    }
  }

  public enum StandardClassifier { // Only Class here => important for

    article, product_types, attribute_classes, single_article, fileklass,
    golden_article_klass, marker, attachment_asset, asset_asset, image_asset, video_asset,
    document_asset, smartdocument_asset, market, text_asset, supplier,
    suppliers, marketplaces, distributors, wholesalers, translation_agency,
    content_enrichment_agency, digital_asset_agency, virtual_catalog;

    public long getIID() {
      return 20 + this.ordinal();
    }
  }

  public enum StandardCatalog {

    pim, onboarding, offboarding;

    public static boolean isStandardCatalog(String code) {
      return code.equals(pim.toString()) || code.equals(onboarding.toString())
              || code.equals(offboarding.toString());
    }

    public String getCode() {
      return toString();
    }
  }

  public enum StandardContext {
    productVariant, attributeVariantContext, relationshipVariant, gtinVariant,
    contextualVariant, priceContext, imageVariant, attributeVariant,
    promotionContext;

  }
  
  public enum StandardTaskType {
    personal, shared;
  }

  public enum StandardProperty {

    nameattribute, createdbyattribute, createdonattribute, lastmodifiedbyattribute,
    lastmodifiedattribute, addressattribute, telephonenumberattribute, descriptionattribute,
    shortdescriptionattribute, discountattribute, duedateattribute, firstnameattribute,
    lastnameattribute, gtin_attribute, sku_attribute, listpriceattribute, maximumpriceattribute,
    minimumpriceattribute, sellingpriceattribute, pid_attribute, pincodeattribute,
    isbaseunittag, isordertag, issalestag, // asset
    // standard
    // attributes:
    I_APPLICATION, I_BIT_DEPTH, I_COLOR_MODE, I_COLOR_PROFILE, I_CREATE_DATE, I_DIMENSION_IN_INCH,
    I_DIMENSIONS, I_META_BRAND, I_META_COPYRIGHT, I_META_COPYRIGHT_STATUS, I_META_COUNTRY,
    I_META_CREATOR_AUTHOR, I_META_CREATOR_COUNTRY, I_META_CREATOR_EMAIL, I_META_CREATOR_LOCATION,
    I_META_CREATOR_POSTAL_CODE, I_META_CREATOR_STATE, I_META_CREATOR_STREET, I_META_CREATOR_TEL,
    I_META_CREATOR_WWW, I_META_DESCRIPTION, I_META_DOCUMENT_TYPE, I_META_EXPOSURE_CONTROLS,
    I_META_EXPOSURE_INDEX, I_META_FILE_NAME, I_META_FILE_SIZE, I_META_FOCAL_RANGE, I_META_HEADING,
    I_META_HIGH_RESOLUTION, I_META_KEYWORDS, I_META_LIGHT_SOURCE, I_META_LOCATION,
    I_META_LOCATION_DETAIL, I_META_MAX_BLINDING_VALUE, I_META_MODELL, I_META_PIXEL_X_DIMENSION,
    I_META_PIXEL_Y_DIMENSION, I_META_RECORDING_DATE, I_META_SENSOR_TYPE, I_META_SERIAL_NUMBER,
    I_META_STATE, I_META_TITEL, I_META_WIDTH_RESOLUTION, I_META_X_RESOLUTION, I_META_Y_RESOLUTION,
    I_MODIFICATION_DATE, I_RESOLUTION, // status and tags
    lifestatustag, listingstatustag, taskstatustag, T_STATUS_SYSTEM,
    T_STATUS_PROJECT, languagetag, availabilitytag, statustag,
    imageextensiontag, resolutiontag, // standard relationships:
    standardArticleAssetRelationship, standardArticleGoldenArticleRelationship,
    standardArticleMarketRelationship,
    standardMarketAssetRelationship,
    standardPromotionAssetRelationship,
    standardSupplierAssetRelationship,
    standardTextAssetAssetRelationship, standardVirtualCatalogAssetRelationship,
    standardDocumentAssetReference, standardDocumentMarketingArticleReference,
    standardArticleSmartDocumentReference,
    standardMarketingArticleSmartDocumentReference,
    longdescriptionattribute, assetcoverflowattribute, filenameattribute, schedulestartattribute, scheduleendattribute,
    assetdownloadcountattribute;
    
    public static final List<Long>   TrackingPropertyIIDs    = new ArrayList<>();
    public static final List<String> TrackingPropertyCodes   = new ArrayList<>();
    public static final List<String> MandatoryAttributeCodes = new ArrayList<>();
    public static final List<String> DefaultTagCodes = new ArrayList<>();
    public static final List<String> MandatoryTagCodes = DefaultTagCodes;
    public static final List<String> AssetMetaAttributeCodes = new ArrayList<>();

    static {
      TrackingPropertyIIDs.addAll(
              Arrays.asList(new Long[]{createdbyattribute.getIID(), createdonattribute.getIID(),
        lastmodifiedbyattribute.getIID(), lastmodifiedattribute.getIID()}));
      TrackingPropertyCodes.addAll(
              Arrays.asList(new String[]{createdbyattribute.toString(), createdonattribute.toString(),
        lastmodifiedbyattribute.toString(), lastmodifiedattribute.toString()}));
    }

    static {
      MandatoryAttributeCodes.addAll(TrackingPropertyCodes);
      MandatoryAttributeCodes.add(nameattribute.toString());
    }

    static {
      DefaultTagCodes.addAll(
              Arrays.asList(new String[]{lifestatustag.toString(), listingstatustag.toString()}));
    }

    static {
      AssetMetaAttributeCodes.addAll(Arrays.asList(new String[]{I_META_FILE_NAME.toString(),
        I_META_DOCUMENT_TYPE.toString(), I_APPLICATION.toString(), I_CREATE_DATE.toString(),
        I_MODIFICATION_DATE.toString(), I_META_FILE_SIZE.toString(), I_DIMENSIONS.toString(),
        I_DIMENSION_IN_INCH.toString(), I_RESOLUTION.toString(), I_BIT_DEPTH.toString(),
        I_COLOR_MODE.toString(), I_COLOR_PROFILE.toString(), I_META_CREATOR_AUTHOR.toString(),
        I_META_CREATOR_STREET.toString(), I_META_CREATOR_LOCATION.toString(),
        I_META_CREATOR_STATE.toString(), I_META_CREATOR_POSTAL_CODE.toString(),
        I_META_CREATOR_COUNTRY.toString(), I_META_CREATOR_TEL.toString(),
        I_META_CREATOR_EMAIL.toString(), I_META_CREATOR_WWW.toString(), I_META_HEADING.toString(),
        I_META_DESCRIPTION.toString(), I_META_KEYWORDS.toString(),
        I_META_LOCATION_DETAIL.toString(), I_META_LOCATION.toString(), I_META_STATE.toString(),
        I_META_COUNTRY.toString(), I_META_TITEL.toString(), I_META_COPYRIGHT.toString(),
        I_META_COPYRIGHT_STATUS.toString(), I_META_EXPOSURE_INDEX.toString(),
        I_META_FOCAL_RANGE.toString(), I_META_MAX_BLINDING_VALUE.toString(),
        I_META_RECORDING_DATE.toString(), I_META_EXPOSURE_CONTROLS.toString(),
        I_META_LIGHT_SOURCE.toString(), I_META_SENSOR_TYPE.toString(), I_META_BRAND.toString(),
        I_META_MODELL.toString(), I_META_SERIAL_NUMBER.toString(),
        I_META_PIXEL_X_DIMENSION.toString(), I_META_PIXEL_Y_DIMENSION.toString(),
        I_META_HIGH_RESOLUTION.toString(), I_META_WIDTH_RESOLUTION.toString(),
        I_META_X_RESOLUTION.toString(), I_META_Y_RESOLUTION.toString()}));
    }

    public long getIID() {
      return 200 + this.ordinal();
    }
  }

  public enum TagType {

    tag_type_yes_neutral, tag_type_yes_neutral_no, tag_type_range, tag_type_ruler,
    tag_type_lifecycle_status, tag_type_listing_status, tag_type_status, tag_type_boolean,
    tag_type_master;

    public static final List<String> AllTagTypes = new ArrayList<>();
    public static final String BooleanTagCode = tag_type_boolean.toString();

    static {
      for (TagType type : values()) {
        AllTagTypes.add(type.toString());
      }
    }
  }

  public enum StandardTagValue_availabilitytag {

    onrequest, onstock, regularlyavailable, dropshipping; // availabilitytag

    public String getCode() {
      return toString();
    }
  };

  public enum StandardTagValue_statustag {

    enrichmenttag, qualitychecktag, translationtag, approvaltag, productivetag, blockedfromplanning; // statustag

    public String getCode() {
      return toString();
    }
  };

  public enum StandardTagValue_languagetag {

    english, german, spanish, italian, french, portugese, swedish; // languagetag

    public String getCode() {
      return toString();
    }
  };

  public enum StandardTagValue_lifestatustag {

    life_status_inbox, life_status_nointerest, life_status_preparation, life_status_active,
    life_status_activeblocked, life_status_activebanned, life_status_retired, life_status_archived,
    life_status_deleted; // lifestatustag

    public String getCode() {
      return toString();
    }
  };

  public enum StandardTagValue_listingstatustag {

    listing_status_catlog, listing_status_ignored, listing_status_rejected,
    listing_status_onboardingpool, listing_status_recommended, listing_status_shadowassortment,
    listing_status_listed; // listingstatustag

    public String getCode() {
      return toString();
    }
  }

  public enum StandardTagValue_T_STATUS_SYSTEM {

    T_STATUS_SYSTEM_NEW, T_STATUS_SYSTEM_INACTIV, T_STATUS_SYSTEM_ACTIV, T_STATUS_SYSTEM_ARCHIVED,
    T_STATUS_SYSTEM_DELETED, T_STATUS_SYSTEM_PREPARE, T_STATUS_SYSTEM_BLOCKED; // T_STATUS_SYSTEM

    public String getCode() {
      return toString();
    }
  };

  public enum StandardTagValue_resolutiontag {

    resolution_300P, resolution_150P, resolution_72P; // resolutiontag

    public String getCode() {
      return toString();
    }
  };

  public enum StandardTagValue_imageextensiontag {

    image_extension_format_jpg, image_extension_format_png, image_extension_format_original; // imageextensiontag

    public String getCode() {
      return toString();
    }
  };

  public enum StandardTagValue_T_STATUS_PROJECT {

    T_STATUS_PROJECT_IN_PLANNING, T_STATUS_PROJECT_CANCELED, T_STATUS_PROJECT_COMPLETE,
    T_STATUS_PROJECT_REWORK, T_STATUS_PROJECT_QUALITY_CHECK, T_STATUS_PROJECT_ERROR_PROCESSING,
    T_STATUS_PROJECT_LICENSE_MISSING; // T_STATUS_PROJECT

    public String getCode() {
      return toString();
    }
  }

  public enum StandardTagValue_taskstatustag {

    taskplanned, taskready, taskdeclined, taskinprogress, taskdone, taskverified, tasksignedoff; // taskstatustag

    public String getCode() {
      return toString();
    }
  }

  public enum StandardTagValue_isordertag {

    isordertagvalue, issalestagvalue, isbaseunittagvalue; // isordertag

    public String getCode() {
      return toString();
    }
  }

  public enum StandardTab {

    task_tab, timeline_tab, overview_tab, property_collection_tab, relationship_tab,
    context_tab, tabSequence, rendition_tab,
    configurations_tab, usage_tab, promotional_products_tab, mdm_tab, duplicate_tab;

    public static final List<String> DefaultRuntimeTabs = new ArrayList<>();

    static {
      DefaultRuntimeTabs.addAll(Arrays.asList(new String[]{task_tab.toString(),
        timeline_tab.toString(), overview_tab.toString()}));
    }
  }

  public enum GraphCase {

    UNDEFINED, RECORD_DEPENDENCY, ENTITY_PARENT;

    private static final GraphCase[] values = values();

    public static GraphCase valueOf(int ordinal) {
      return values[ordinal];
    }
  }
  
  public enum UniquePrefix {
    ATTRIBTUE("ATT"), CLASS("CFR"), CONTEXT("CTX"), EVENT("EVT"),
    GOLDENRECORD("GOR"), HIERARCHY_TAXONOMY("HTX"), KPI("KPI"), LOCALE("LOC"), MAPPING("MPG"), PROPERTYCOLLECTION("PCN"), 
    RANDOM("RND"), RELATIONSHIP("REL"), RELATIONSHIP_SIDE("SDE"), ROLE("ROL"), RULE("RUL"), RULELIST("RLS"), 
    TAB("TAB"), TAG("TAG"), TASK("TSK"), TAXONOMY("TAX"), TEMPLATE("TPL"), USER("USR"), REFERENCE("REF"), ICON("ICO"), 
    SMART_DOCUMENT_TEMPLATE("SDT");
    
    private String prefix;
    UniquePrefix (String prefix) {
      this.prefix = prefix;
    } 
    public String getPrefix() {
      return this.prefix;
    }
  }
  
}
