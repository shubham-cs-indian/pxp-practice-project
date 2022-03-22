package com.cs.core.runtime.interactor.constants;

import com.cs.constants.SystemLevelIds;

/**
 * The exact replica of this class is in Database projects (Plugin) inside
 * package "com.cs.config.strategy.plugin.constants" Any changes in this class
 * need to be done in the above mentioned class as well
 *
 * @author CS31
 */
public class ImportSystemLevelIds extends SystemLevelIds {
  
  public static final String MASTER_ARTICLE_CLASS                = "masterarticle";
  public static final String IMPORT_SYSTEM_ARTICLE_CLASS         = "systemarticle";
  public static final String SYSTEM_ARTICLE_CLASS                = "systemarticle";
  public static final String EXCEL_FILE_SOURCE_CLASS_ID_POSTFIX  = "__excel";
  public static final String CSV_FILE_SOURCE_CLASS_ID_POSTFIX    = "__csv";
  public static final String MANUAL_SOURCE_CLASS_ID_POSTFIX      = "__manual";
  public static final String IGNORED_PROPERTY_ATTRIBUTE          = "ignoredpropertyattribute";
  public static final String SIZE_ATTRIBUTE                      = "sizeattribute";
  public static final String STATUS_ATTRIBUTE                    = "statusattribute";
  public static final String TAT_UPLOAD_ATTRIBUTE                = "tatuploadattribute";
  
  public static final String FILE_STATUS_TAG                     = "fileStatusTag";
  public static final String FILE_STATUS_GREY_TAG                = "greyfileStatusTag";
  public static final String FILE_STATUS_RED_TAG                 = "redfileStatusTag";
  public static final String FILE_STATUS_GREEN_TAG               = "greenfileStatusTag";
  public static final String FILE_STATUS_ORANGE_TAG              = "orangefileStatusTag";
  public static final String FILE_STATUS_YELLOW_TAG              = "yellowfileStatusTag";
  
  public static final String IMPORT_ARTICLE_STATUS_TAG           = "importArticleStatusTag";
  public static final String IMPORT_ARTICLE_STATUS_AVAILABLE_TAG = "availableImportArticleStatusTag";
  public static final String IMPORT_ARTICLE_STATUS_PROCESSED_TAG = "processedImportArticleStatusTag";
  public static final String IMPORT_ARTICLE_STATUS_READY_TAG     = "readyImportArticleStatusTag";
  
  public static final String MASTER_ARTICLE_STATUS_TAG           = "masterArticleStatusTag";
  public static final String MASTER_ARTICLE_STATUS_READY_TAG     = "readyMasterArticleStatusTag";
  public static final String MASTER_ARTICLE_STATUS_REJECTED_TAG  = "rejectedMasterArticleStatusTag";
  public static final String MASTER_ARTICLE_STATUS_PARTIAL_TAG   = "partialMasterArticleStatusTag";
  public static final String MASTER_ARTICLE_STATUS_WARNING_TAG   = "warningMasterArticleStatusTag";
  public static final String MASTER_ARTICLE_STATUS_IMPORTED_TAG  = "importedMasterArticleStatusTag";
  public static final String MASTER_ARTICLE_STATUS_CONFLICT_TAG  = "conflictMasterArticleStatusTag";
  
  public static final String GTIN_ATTRIBUTE                      = "gtin_attribute";
  public static final String PID_ATTRIBUTE                       = "pid_attribute";
  public static final String SKU_ATTRIBUTE                       = "sku_attribute";
  public static final String IMPORT_SYSTEM_ATTRIBUTE             = "importsystem_attribute";
  
  /**
   * *********************************** STANDARD_ATTRIBUTE
   * ********************************
   */
  public static final String FILE_NAME_ATTRIBUTE                 = "filenameattribute";
  
  public static final String FILE_SIZE_ATTRIBUTE                 = "filesizeattribute";
  public static final String FILE_TYPE_ATTRIBUTE                 = "filetypeattribute";
  public static final String FILE_SUPPLIER_ATTRIBUTE             = "filesupplierattribute";
  public static final String FILE_MAPPED_TO_KLASS_ATTRIBUTE      = "filemappedtoklassattribute";
  
  /**
   * *********************************** STANDARD_TAG
   * ********************************
   */
  public static final String IMPORT_FILE_STATUS_TAG              = "importfilestatus";
  
  public static final String IMPORT_FILE_MAPPED_STATUS_TAG       = "mappedtag";
  public static final String IMPORT_FILE_UNMAPPED_STATUS_TAG     = "unmappedtag";
}
