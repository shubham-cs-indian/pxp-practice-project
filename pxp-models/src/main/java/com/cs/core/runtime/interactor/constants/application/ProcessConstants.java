package com.cs.core.runtime.interactor.constants.application;

import java.util.Arrays;
import java.util.List;

public class ProcessConstants {
  
  public static final String       FILE_NAME_FOR_EXPORT                                  = "fileNameForExport";
  public static final String       FILE_INSTANCE_ID_FOR_EXPORT                           = "fileInstanceIdForExport";
  public static final String       PROCESS_TYPE                                          = "processType";
  public static final String       HEADER_ROW_NUMBER                                     = "headerRowNumber";
  public static final String       DATA_ROW_NUMBER                                       = "dataRowNumber";
  public static final String       SHEET                                                 = "sheet";
  public static final String       PRIMARY_KEY_COLUMN                                    = "primaryKeyColumn";
  public static final String       SECONDARY_CLASSES                                     = "secondaryClasses";
  public static final String       CLASS_INFO                                            = "classInfo";
  public static final String       SECONDARY_CLASS_COLUMN_NAME                           = "secondaryClassColumnName";
  public static final String       TYPE                                                  = "type";
  public static final String       TAXONOMY_COLUMNS                                      = "taxonomycolumns";
  public static final String       TAXONOMY_COLUMN                                       = "taxonomyColumn";
  public static final String       TAXONOMY_ID                                           = "taxonomyId";
  public static final String       TAXONOMIES                                            = "taxonomies";
  public static final String       RELATIONSHIP_TYPE                                     = "relationshipType";
  public static final String       INPUT_RELATIONSHIP_ID                                 = "Input_relationshipId";
  public static final String       RELATIONSHIP_COLUMN_NAME                              = "relationshipColumnName";
  public static final String       SOURCE_ID_COLUMN_NAME                                 = "sourceIdColumnName";
  public static final String       TARGET_ID_COLUMN_NAME                                 = "targetIdColumnName";
  public static final String       CONTEXT_ID_COLUMN                                     = "contextIdColumn";
  public static final String       CONTEXT_TAGS_COLUMN                                   = "contextTagsColumn";
  public static final String       FROM_DATE_COLUMN                                      = "fromDateColumn";
  public static final String       TO_DATE_COLUMN                                        = "toDateColumn";
  public static final String       KLASS_ID                                              = "klassId";
  public static final String       KLASS_COLUMN                                          = "klassColumn";
  public static final String       IDS                                                   = "ids";
  public static final String       MASTER_INSTANCE_ID                                    = "masterInstanceId";
  public static final String       TAG_COLUMN_NAME                                       = "tagColumnName";
  public static final String       IS_ATTRIBUTE_VARIANT                                  = "isAttributeVariant";
  public static final String       BODY                                                  = "body";
  public static final String       REQUEST_MAP                                           = "requestMap";
  public static final String       CATALOG_ID_DATAINTEGRATION                            = "dataIntegration";
  public static final String       CATALOG_ID_PIM                                        = "pim";
  public static final String       FILE_ID                                               = "fileId";
  public static final String       DESTINATION_CATALOG_ID                                = "destinationCatalogId";
  public static final String       DESTINATION_ENDPOINT_ID                               = "destinationEndpointId";
  public static final String       FILE                                                  = "file";
  public static final String       HEADER_ROW_NUMBER_1                                   = "1";
  public static final String       DATA_ROW_NUMBER_2                                     = "2";
  public static final String       FILE_SOURCE                                           = "fileSource";
  public static final String       IMAGE_FOLDER_PATH                                     = "imageFolderPath";
  public static final String       FILE_PATH_COLUMN_NAME                                 = "filePathColumnName";
  public static final String       CONTEXT_ID                                            = "contextId";
  public static final String       FROM_DATE_COLUMN_NAME                                 = "fromDateColumnName";
  public static final String       TO_DATE_COLUMN_NAME                                   = "toDateColumnName";
  public static final String       ATTRIBUTE_COLUMN                                      = "attributeColumn";
  public static final String       ATTRIBUTE_COLUMN_NAME                                 = "attributeValueColumn";
  public static final String       MASTER_VARIANT_INSTANCE_COLUMN                        = "masterVariantInstanceColumn";
  public static final String       PARENT_CONTEXT_ID                                     = "parentContextId";
  public static final String       VARIANT_TYPE                                          = "variantType";
  public static final String       DESTINATION_ORGANIZATION_ID                           = "destinationOrganizationId";
  public static final String       IDS_TO_TRANSFER                                       = "idsToTransfer";
  public static final String       KLASS_INSTANCE_ID                                     = "klassInstanceId";
  public static final String       CURRENT_KLASS_INSTANCE_ID                             = "CurrentKlassInstanceId";
  public static final String       MANUAL_TRANSFER                                       = "ManualTransfer";
  public static final String       TRANSFER_SOURCE                                       = "TransferSource";
  public static final String       TRANSFER_SOURCE_KLASS_INSTANCE_ID                     = "TransferSourceKlassInstanceId";
  public static final String       REFERENCE_TYPE                                        = "referenceType";
  public static final String       REFERENCE_ID                                          = "referenceId";
  public static final String       REFERENCE_COLUMN_NAME                                 = "referenceColumnName";
  
  // JMS CONSTANTS
  public static final String       PRODUCER_IP                                           = "producerIP";
  public static final String       PRODUCER_PORT                                         = "producerPort";
  public static final String       PRODUCER_QUEUE                                        = "producerQueue";
  public static final String       PRODUCER_ACKNOWLEDGEMENT_QUEUE                        = "producerAcknowledgementQueue";
  public static final String       CONSUMER_QUEUE                                        = "consumerQueue";
  public static final String       CONSUMER_PORT                                         = "consumerPort";
  public static final String       CONSUMER_IP                                           = "consumerIP";
  public static final String       CONSUMER_ACKNOWLEDGEMENT_QUEUE                        = "acknowledgementQueue";
  public static final String       JMS_CONSUMER                                          = "Jms_Consumer";
  public static final String       JMS_PRODUCER                                          = "jmsProducerDiComponent";
  public static final String       GET_JMS_EXPORT_ACK                                    = "getJMSExportAckDiComponent";
  public static final String       ACKNOWLEDGEMENT_QUEUE                                 = "acknowledgementQueue";
  public static final String       DATA_INTEGRATION_CATALOG_IDS                          = "dataIntegration";
  public static final String       DI_ONBOARDING_PROCESS                                 = "diOnboardingProcess";
  public static final String       PROCESS_DATA                                          = "processData";
  public static final String       BOARDING_TYPE                                         = "boardingType";
  public static final String       ENDPOINT_ID                                           = "endpointId";
  public static final String       ENDPOINTS                                             = "endpoints";
  public static final String       LISTENER_TYPE                                         = "listenerType";
  public static final String       DATA                                                  = "data";
  public static final String       TRANSFER_COMPONENT_DATA                               = "transferComponentData";
  public static final String       PHYSICAL_CATALOG_ID                                   = "physicalCatalogId";
  public static final String       ORGANIZATION_ID                                       = "organizationId";
  public static final String       LOGICAL_CATALOG_ID                                    = "logicalCatalogId";
  public static final String       USER_ID                                               = "userId";
  public static final String       MINUTES_BEFORE_RETRY                                  = "minutesBeforeRetries";
  public static final String       NUMBER_OF_RETRIES                                     = "numberOfRetries";
  public static final String       MINUTES_BEFORE_RETRY_AFTER_ACK                        = "minutesBeforeRetriesAfterAck";
  public static final String       TIMER_DURATION                                        = "timerDuration";
  
  public static final String       PROCESS_IDS                                           = "processIds";
  public static final String       PROCESS_ID                                            = "processId";
  public static final String       USECASE                                               = "usecase";
  public static final String       PRODUCT_ID                                            = "ProductId";
  public static final String       TALEND_EXECUTABLE                                     = "talendExecutable";
  public static final String       TALEND_JOB                                            = "talendJob";
  public static final String       PROCESS_ENTITY_ID                                     = "processEntityId";
  
  public static final String       JMS_ARTICLE_IMPORT_CONTROLLER                         = "articleDiImportComponentController";
  public static final String       JMS_ASSET_IMPORT_CONTROLLER                           = "assetDiImportComponentController";
  public static final String       JMS_VARIANT_IMPORT_CONTROLLER                         = "articleVariantDiImportComponentController";
  public static final String       JMS_ATTRIBUTE_IMPORT_CONTROLLER                       = "attributeVariantDiImportComponentController";
  public static final String       BUT_PRODUCT_EXPORT_CONTROLLER                         = "butProductExportController";
  public static final String       JMS_TALEND_CONTROLLER                                 = "talendDiComponentController";
  public static final String       JMS_CONSUMER_CONTROLLER                               = "jMSConsumerDiComponentController";
  public static final String       JMS_PRODUCER_CONTROLLER                               = "jmsProducerDiComponentController";
  public static final String       JMS_GET_EXPORT_ACK_CONTROLLER                         = "JmsGetExportAcknowledgementDiComponentController";
  public static final String       JMS_ACKNOWLEDGEMENT_CONTROLLER                        = "jmsAcknowledgementDiComponentController";
  public static final String       JMS_CONSUMER_CONTROLLERS                              = "${"
      + JMS_CONSUMER_CONTROLLER + "}";
  public static final String       JMS_PRODUCER_CONTROLLERS                              = "${"
      + JMS_PRODUCER_CONTROLLER + "}";
  
  public static final String       JMS_GET_ARTICLE_BY_ID_COMPONENT                       = "jmsGetArticleByIdDiComponent";
  
  // tags and attribute
  public static final String       TAG_GROUP                                             = "tagGroup";
  public static final String       TAG_ID                                                = "tagId";
  public static final String       TAG_VALUE_ID                                          = "tagValueId";
  public static final String       TAG_GROUP_ID                                          = "tagGroupId";
  public static final String       INPUT_ATTRIBUTES_VALUES_TO_FETCH                      = "Input_attributesValuesToFetch";
  public static final String       INPUT_TAGS_VALUES_TO_FETCH                            = "Input_tagsValuesToFetch";
  public static final String       EXECUTION_PROPERTIES_TO_SET                           = "executionPropertiesToSet";
  public static final String       ATTRIBUTES_VALUES_TO_SET                              = "attributesValuesToSet";
  public static final String       INPUT_ATTRIBUTES_VALUES_MAP                           = "Input_attributesValueMap";
  public static final String       INPUT_ATTRIBUTES_TYPES_MAP                            = "Input_attributesTypeMap";
  public static final String       INPUT_TAG_VALUES_MAP                                  = "Input_tagsMap";
  public static final String       TAXONOMIES_LIST                                       = "taxonomiesList";
  public static final String       TRIGGER_EVENT                                         = "triggerEvent";
  public static final String       RESULT_VARIABLE                                       = "resultVariable";
  public static final String       INSTANCE_RELATIONSHIPS                                = "instanceRelationships";
  public static final String       OUTPUT_INSTANCE_RELATIONSHIPS                         = "Output_instanceRelationships";
  public static final String       FILTER_MODEL                                          = "filterModel";
  public static final String       INPUT_FILTER_MODEL                                    = "Input_filterModel";
  public static final String       DATA_LANGUAGE                                         = "dataLanguage";
  public static final String       INPUT_DATA_LANGUAGE                                   = "Input_dataLanguage";
  public static final String       OUTPUT_CONTENTIDS                                     = "Output_contentIds";
  public static final String       DEFAULT_RESULT_VARIABLE_FOR_SEARCH_COMPONENT          = "contentIds";
  public static final String       OUTPUT_ATTRIBUTES_AND_TAGS                            = "Output_attributesAndTags";
  public static final String       INPUT_EXECUTION_STATUS_TABLE_NAMES                    = "Input_executionStatusTableNames";
  public static final String       EXECUTION_STATUS_TABLES                               = "executionStatusTables";
  public static final String       HEADER_INFORMATION                                    = "Header Information";
  public static final String       MESSAGE_TYPE                                          = "Message Type";
  public static final String       OBJECT_CODE                                           = "Object Code";
  public static final String       MESSAGE_CODE                                          = "Message Code";
  public static final String       MESSAGE_TEXT                                          = "Message Text";
  public static final String       EXECUTION_STATUS_ZIP_NAME                             = "ExecutionStatus.zip";
  
  // Components List
  public static final String       ARTICLE_IMPORT_COMPONENT_ID                           = "Article_Import";
  public static final String       ARTICLE_EXPORT_COMPONENT_ID                           = "Article_Export";
  public static final String       ASSET_IMPORT_COMPONENT_ID                             = "Asset_Import";
  public static final String       ASSET_EXPORT_COMPONENT_ID                             = "Asset_Export";
  public static final String       MARKET_IMPORT_COMPONENT_ID                            = "Market_Import";
  public static final String       MARKET_EXPORT_COMPONENT_ID                            = "Market_Export";
  public static final String       TEXT_ASSET_IMPORT_COMPONENT_ID                        = "Text_Asset_Import";
  public static final String       TEXT_ASSET_EXPORT_COMPONENT_ID                        = "Text_Asset_Export";
  public static final String       SUPPLIER_IMPORT_COMPONENT_ID                          = "Supplier_Import";
  public static final String       SUPPLIER_EXPORT_COMPONENT_ID                          = "Supplier_Export";
  
  public static final String       PROMOTION_EXPORT_COMPONENT_ID                         = "Promotion_Export";
  
  public static final String       EXTENDED_ARTICLE_IMPORT_COMPONENT_ID                  = "Extended_Article_Import";
  public static final String       EXTENDED_ARTICLE_EXPORT_COMPONENT_ID                  = "Extended_Article_Export";
  public static final String       EXTENDED_ASSET_IMPORT_COMPONENT_ID                    = "Extended_Asset_Import";
  public static final String       EXTENDED_ASSET_EXPORT_COMPONENT_ID                    = "Extended_Asset_Export";
  public static final String       EXTENDED_MARKET_IMPORT_COMPONENT_ID                   = "Extended_Market_Import";
  public static final String       EXTENDED_MARKET_EXPORT_COMPONENT_ID                   = "Extended_Market_Export";
  public static final String       EXTENDED_TEXT_ASSET_IMPORT_COMPONENT_ID               = "Extended_Text_Asset_Import";
  public static final String       EXTENDED_TEXT_ASSET_EXPORT_COMPONENT_ID               = "Extended_Text_Asset_Export";
  public static final String       EXTENDED_SUPPLIER_IMPORT_COMPONENT_ID                 = "Extended_Supplier_Import";
  public static final String       EXTENDED_SUPPLIER_EXPORT_COMPONENT_ID                 = "Extended_Supplier_Export";
  
  public static final String       EXTENDED_PROMOTION_EXPORT_COMPONENT_ID                = "Extended_Promotion_Export";
  
  public static final String       RELATIONSHIP_EXPORT_COMPONENT_ID                      = "Relationship_Export";
  public static final String       RELATIONSHIP_IMPORT_COMPONENT_ID                      = "Relationship_Import";
  public static final String       NATURE_RELATIONSHIP_EXPORT_COMPONENT_ID               = "Nature_Relationship_Export";
  public static final String       NATURE_RELATIONSHIP_IMPORT_COMPONENT_ID               = "Nature_Relationship_Import";
  
  public static final String       REFERENCE_IMPORT_COMPONENT_ID                         = "Reference_Import";
  public static final String       REFERENCE_EXPORT_COMPONENT_ID                         = "Reference_Export";
  
  public static final String       TRANSFER_COMPONENT_ID                                 = "Transfer";
  
  public static final String       ARTICLE_VARIANT_IMPORT_COMPONENT_ID                   = "Article_Variant_Import";
  public static final String       ARTICLE_VARIANT_EXPORT_COMPONENT_ID                   = "Article_Variant_Export";
  
  public static final String       JMS_ARTICLE_IMPORT_COMPONENT                          = "JMSImportArticleComponent";
  public static final String       JMS_ASSET_IMPORT_COMPONENT                            = "JMSImportAssetComponent";
  public static final String       JMS_ARTICLE_VARIANT_IMPORT_COMPONENT                  = "JMSImportArticleVariantComponent";
  public static final String       JMS_ACKNOWLEDGEMENT_COMPONENT                         = "JMSAcknowledgementComponent";
  public static final String       TALEND_COMPONENT                                      = "TalendComponent";
  public static final String       JMS_EXPORT_ACKNOWLEDGEMENT_COMPONENT                  = "JMSExportAcknowledgementComponent";
  public static final String       OUTBOUND_MESSAGE_RETRY_COUNT                          = "outboundMessageRetryCount";
  
  public static final String       JMS_VARIANT_DI_IMPORT_CONTROLLER                      = "JMSVariantDiImportController";
  public static final String       JMS_VARIANT_DI_IMPORT_COMPONENT                       = "JMSVariantDiImportComponent";
  public static final String       JMS_RELATIONSHIP_IMPORT_COMPONENT                     = "JMSRelationshipImportComponent";
  public static final String       JMS_RELATIONSHIP_IMPORT_CONTROLLER                    = "JMSRelationshipImportController";
  public static final String       JMS_NATURE_RELATIONSHIP_IMPORT_CONTROLLER             = "JMSNatureRelationshipImportController";
  public static final String       JMS_NATURE_RELATIONSHIP_IMPORT_COMPONENT              = "JMSNatureRelationshipImportComponent";
  public static final String       JMS_REFERENCE_IMPORT_COMPONENT                        = "JMSReferenceImportComponent";
  public static final String       JMS_REFERENCE_IMPORT_CONTROLLER                       = "JMSReferenceImportController";
  public static final String       JMS_EMBEDDED_VARIANT_IMPORT_CONTROLLER                = "JMSEmbeddedVariantImportController";
  public static final String       JMS_ATTRIBUTE_VARIANT_IMPORT_CONTROLLER               = "JMSAttributeVariantImportController";
  public static final String       JMS_EMBEDDED_VARIANT_IMPORT_COMPONENT                 = "JMSEmbeddedVariantImportComponent";
  public static final String       JMS_ATTRIBUTE_VARIANT_IMPORT_COMPONENT                = "JMSAttributeVariantImportComponent";
  public static final String       JMS_TAXONOMY_IMPORT_COMPONENT                         = "jmsTaxonomyImportComponent";
  
  public static final String       MASTER_TAXONOMY                                       = "master";
  public static final String       HIERARCHY_TAXONOMY                                    = "hierarchy"; //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies -- Remove it after usage checking
  public static final String       JMS_SUPPLIER_DI_IMPORT_CONTROLLER                     = "JMSSuppliersDiImportController";
  public static final String       JMS_SUPPLIER_DI_IMPORT_COMPONENT                      = "JMSSuppliersDiImportComponent";
  public static final String       JMS_MARKET_IMPORT_CONTROLLER                          = "marketDiImportComponentController";
  public static final String       JMS_MARKET_IMPORT_COMPONENT                           = "JMSImportMarketComponent";
  
  public static final String       AFTER_SAVE                                            = "afterSave";
  public static final String       AFTER_CREATE                                          = "afterCreate";
  public static final String       TRANSFER                                              = "transfer";
  public static final String       AFTER_IMPORT                                          = "afterImport";
  public static final String       AFTER_SAVE_RELATIONSHIP                               = "afterSaveRelationship";
  public static final String       AFTER_SAVE_REFERENCE                                  = "afterSaveReference";
  public static final String       AFTER_SAVE_EMBEDDED                                   = "afterSaveEmbedded";
  
  public static final String       JMS_TAXONOMY_EXPORT_CONTROLLER                        = "jmsTaxonomyConfigExportController";
  public static final String       TAXONOMY_IDS                                          = "taxonomyIds";
  public static final String       ACTION                                                = "action";
  public static final String       TAXONOMY_CONFIG_EXPORT_COMPONENT                      = "jmsTaxonomyConfigExportComponent";
  
  public static final String       ID_CODE                                               = "idCode";
  
  public static final String       TASK_STATUS                                           = "Task-Status";
  public static final String       EXPIRED_ASSET_IDS                                     = "expiredAssetIds";
  public static final String       SUCCESS_ARTICLE_IDS                                   = "success_article_ids";
  public static final String       SUCCESS_VARIANT_IDS                                   = "success_variant_ids";
  public static final String       RELATIONSHIP_UPDATE_MODEL                             = "relationshipUpdateModel";
  public static final String       SOURCE_TYPE_PRODUCT                                   = "product";
  public static final String       SOURCE_TYPE_ASSET                                     = "asset";
  
  public static final String       DELETE_OPERATION_FOR_SWITCH_TYPE                      = "delete";
  public static final String       ADD_OPERATION_FOR_SWITCH_TYPE                         = "add";
  
  public static final String       CONTEXT_CODES                                         = "contextCodes";
  public static final String       RELATIONSHIP_CODES                                    = "relationshipCodes";
  public static final String       REFERENCE_CODES                                       = "referenceCodes";
  public static final String       CONTEXT                                               = "context";
  public static final String       COUNT                                                 = "count";
  public static final String       CONTEXT_TAGS_MAP                                      = "contextTagsMap";
  public static final String       CONTEXT_TIME_RANGE_MAP                                = "contextTimeRangeMap";
  public static final String       CONTEXT_CODE                                          = "contextCode";
  
  // -------------------------------Transfer Component ----------------------
  
  public static final String       TRANSFER_ARTICLES_TO_DESTINATION_COMPONENT            = "transferArticlesToDestinationComponent";
  public static final String       TRANSFER_ASSETS_TO_DESTINATION_COMPONENT              = "transferAssetsToDestinationComponent";
  public static final String       TRANSFER_ASSETSS_TO_DESTINATION_DISTRIBUTOR_COMPONENT = "transferAssetsToDestinationDistributorComponent";
  public static final String       TRANSFER_VARIANTS_TO_DESTINATION_COMPONENT            = "transferVariantsToDestinationComponent";
  public static final String       TRANSFER_RELATIONSHIPS_TO_DESTINATION_COMPONENT       = "transferRelationshipsToDestinationComponent";
  public static final String       TRANSFER_TASKS_TO_DESTINATION_COMPONENT               = "transferTasksToDestinationComponent";
  public static final String       TRANSFER_EVENTS_TO_DESTINATION_COMPONENT              = "transferEventsToDestinationComponent";
  public static final String       TRANSFER_REFERENCES_TO_DESTINATION_COMPONENT          = "transferReferencesToDestinationComponent";
  
  // Camunda JavaDelegate Controllers
  
  public static final String       SEARCH_CONTENT_CONTROLLER                             = "searchContentController";
  public static final String       SEARCH_CONTENT                                        = "Search Content";
  public static final String       CODE_ID                                               = "codeId";
  public static final String       ASSET_INSTANCE_ID                                     = "assetInstanceId";
  public static final String       SET_ATTRIBUTES_AND_TAGS_TASK                          = "setAttributesAndTagsTask";
  public static final String       SET_ATTRIBUTES_AND_TAGS                               = "Set Attributes And Tags";
  
  public static final String       IS_TRIGGERED_THROUGH_SCHEDULER                        = "isTriggeredThroughScheduler";
  public static final String       SET_DATA_LANGUAGE                                     = "setDataLanguage";
  public static final String       INPUT_SET_DATA_LANGUAGE                               = "Input_setDataLanguage";
  public static final String       RELATIVE_PATH_FOR_BPMN_FILES                          = "/dataIntegrationProperties/";
  
  public static final List<String> ALLOWED_TYPES_FOR_UPLOAD                              = Arrays
      .asList("jpg", "jpeg", "png", "ico", "eps", "ai", "psd", "tif", "tiff", "gif", "wmv", "avi",
          "mov", "flv", "mpeg", "mpg", "mp4", "pdf", "ppt", "pptx", "indd", "doc", "docx", "xls",
          "xlsx", "xtex");
  
  public static final String       SIDE1_LABEL                                           = "Side1Label";
  public static final String       SIDE2_LABEL                                           = "Side2Label";
  
  public static final String       DEFAULT_ACTION                                        = "-1";
  public static final String       CREATE                                                = "create";
  public static final String       UPDATE                                                = "update";
  public static final String       DELETE                                                = "delete";
  public static final String       IMPORT_ACTION                                         = "Action";
  
  public static final String       REFERENCE_IMPORT_COMPONENT_CONTROLLER                 = "referenceImportComponentController";
  public static final String       REFERENCE_IMPORT_COMPONENT                            = "referenceImportComponent";
  public static final String       REFERENCE_IMPORT                                      = "Reference Import";
  public static final String       REFERENCE_EXPORT_COMPONENT_CONTROLLER                 = "referenceExportComponentController";
  public static final String       REFERENCE_EXPORT_COMPONENT                            = "referenceExportComponent";
  public static final String       REFERENCE_EXPORT                                      = "Reference Export";
  
  public static final String       SUCCESS                                               = "success";
  public static final String       FAILED                                                = "failed";
  public static final String       NOT_APPLICABE                                         = "notApplicable";
  
  public static final String       AFTER_BULK_EDIT                                       = "afterBulkEdit";
  public static final String       AUTHORIZATION_MAPPING                                 = "authorizationMapping";
  
  public static final String       SAVED_IDS                                             = "savedIds";
  public static final String       CREATED_IDS                                           = "createdids";
  public static final String       SAVED                                                 = "saved";
  public static final String       CREATED                                               = "created";
  
  // -------------------------------List Operation Common
  // Constants----------------------
  public static final String       OUTPUT_EXECUTION_STATUS                               = "Output_executionStatus";
  public static final String       INPUT_LIST                                            = "Input_list";
  public static final String       INPUT_ITEM                                            = "Input_item";
  public static final String       LIST_INPUT_PARAMETERS                                 = "listInputParameters";
  public static final String       INPUT_POSITION                                        = "Input_Position";
  public static final String       ANY_INPUT_PARAMETER                                   = "Any Input Parameter";
  
  // -------------------------------List Append Operation
  // Constants----------------------
  public static final String       LIST_APPEND_CONTROLLER                                = "listAppendController";
  public static final String       OUTPUT_LISTAPPEND                                     = "Output_listAppend";
  public static final String       LIST_APPEND_LABEL                                     = "List Append";
  
  // -------------------------------List Remove All Occurrences Constants
  // ----------------------
  public static final String       LIST_REMOVE_ALL_OCCURRENCES_CONTROLLER                = "listRemoveAllOccurrencesController";
  public static final String       OUTPUT_LIST_REMOVE_ALL_OCCURRENCES                    = "Output_listRemoveAllOccurrences";
  public static final String       LIST_REMOVE_ALL_OCCURRENCES_COMPONENT                 = "listRemoveAllOccurrencesComponent";
  public static final String       LIST_REMOVE_ALL_OCCURRENCES_LABEL                     = "List Remove All Occurrences";
  
  // -------------------------------List Remove First Occurrence
  // Constants---------------
  public static final String       LIST_REMOVE_FIRST_OCCURRENCE_CONTROLLER               = "listRemoveFirstOccurrenceController";
  public static final String       OUTPUT_LIST_REMOVE_FIRST_OCCURRENCE                   = "Output_listRemoveFirstOccurrence";
  public static final String       LIST_REMOVE_FIRST_OCCURRENCE_COMPONENT                = "listRemoveFirstOccurrenceComponent";
  public static final String       LIST_REMOVE_FIRST_OCCURRENCE_LABEL                    = "List Remove First Occurrence";
  
  // -------------------------------List Reverse Operation
  // Constants----------------------
  public static final String       LIST_REVERSE_CONTROLLER                               = "listReverseController";
  public static final String       OUTPUT_LIST_REVERSE                                   = "Output_listReverse";
  public static final String       LIST_REVERSE_COMPONENT                                = "listReverseComponent";
  public static final String       LIST_REVERSE_LABEL                                    = "List Reverse";
  
  // -------------------------------List Sort Operation
  // Constants----------------------
  public static final String       LIST_SORT_CONTROLLER                                  = "listSortController";
  public static final String       OUTPUT_LIST_SORT                                      = "Output_listSort";
  public static final String       SORT_ORDER                                            = "sortOrder";
  public static final String       LIST_SORT_COMPONENT                                   = "listSortComponent";
  public static final String       LIST_SORT_LABEL                                       = "List Sort";
  
  // -------------------------------List Clear Operation
  // Constants------------------------
  public static final String       LIST_CLEAR_CONTROLLER                                 = "listClearController";
  public static final String       OUTPUT_LISTCLEAR                                      = "Output_listClear";
  public static final String       LIST_CLEAR_LABEL                                      = "List Clear";
  
  // -------------------------------List Extend Operation
  // Constants------------------------
  public static final String       LIST_EXTEND_CONTROLLER                                = "listExtendController";
  public static final String       INPUT_LIST_TOWHICH_APPEND                             = "Input_listToWhichAppend";
  public static final String       INPUT_LIST_TOAPPENDED                                 = "Input_listToAppended";
  public static final String       OUTPUT_LIST_APPENED                                   = "Output_listAppened";
  public static final String       LIST_EXTEND_LABEL                                     = "List Extend";
  
  // -------------------------------List Count Element Operation
  // Constants------------------------
  public static final String       LIST_COUNT_ELEMENT_CONTROLLER                         = "listCountElementController";
  public static final String       OUTPUT_LISTCOUNTELEMENT                               = "Output_listCountElement";
  public static final String       INPUT_SEARCH_ITEM                                     = "Input_searchItem";
  public static final String       LIST_COUNT_ELEMENT_LABEL                              = "List Count Element";
  
  // -------------------------------List Search Operation
  // Constants-------------------------
  public static final String       LIST_SEARCH_INDEX_CONTROLLER                          = "listSearchIndexComponentController";
  public static final String       OUTPUT_LIST_SEARCHINDEX                               = "Output_listSearchIndex";
  
  // -------------------------------List Insert Position Operation
  // Constants----------------
  public static final String       LIST_INSERT_POSITION_CONTROLLER                       = "listInsertPositionController";
  public static final String       OUTPUT_LIST_INSERT_POSITION                           = "Output_listInsertPosition";
  
  // -------------------------------List Pop Position Operation
  // Constants-------------------
  public static final String       LIST_POP_POSITION_CONTROLLER                          = "listPopPositionController";
  public static final String       OUTPUT_LIST_POP_POSITION                              = "Output_listPopPosition";
  
  // -------------------------------List Status Execution Check
  // Constants-------------------
  public static final String       STATUS_EXECUTION_CHECK_CONTROLLER                     = "statusExecutionCheckController";
  public static final String       INPUT_MESSAGE_TYPE                                    = "Input_messageType";
  public static final String       OUTPUT_VALUE_PRESENT                                  = "Output_valuePresent";
  
  // -------------------------------List Boolean Check Operation
  // Constants-------------------
  public static final String       BOOLEAN_CHECK_CONTROLLER                              = "booleanCheckController";
  public static final String       INPUT_CONDITION                                       = "Input_condition";
  public static final String       OUTPUT_BOOLEAN_CHECK                                  = "Output_booleanCheck";
  
  // -------------------------------Assign Variables
  // Constants----------------------
  public static final String       ASSIGN_VARIABLES_CONTROLLER                           = "assignVariablesController";
  public static final String       ASSIGN_VARIABLES_COMPONENT                            = "assignVariablesComponent";
  public static final String       INPUT_VARIABLES_MAP                                   = "Input_variablesMap";
  public static final String       ASSIGN_VARIABLES                                      = "Assign Variables";
  
  public static final String       GET_ATTRIBUTES_AND_TAGS                               = "Get Attributes And Tags";
  
  public static final String       CONTENT                                               = "content";
  public static final String       ASSET                                                 = "asset";
  public static final String       VARIANT                                               = "variant";
  public static final String       ATTRIBUTE_VARIANT                                     = "attribute variant";
  public static final String       RELATIONSHIP                                          = "relationship";
  public static final String       ERROR_MESSAGE                                         = "errorMessage";
  public static final String       CONTENT_ID                                            = "contentId";
  public static final String       ASSET_ID                                              = "assetId";
  public static final String       VARIANT_ID                                            = "variantId";
  public static final String       ATTRIBUTE_VARIANT_ID                                  = "attributeVariantId";
  public static final String       RELATIONSHIP_INSTANCE_ID                              = "relationshipInstanceId";
  
  public static final String       BACKGROUNDUSER_STANDARDORGANIZATION                   = "backgrounduserstandardorganization";
  
  public static final String       RECEIVER_TASK                                         = "receiverTask";
  public static final String       CLASS_PATH_IP                                         = "CLASS_PATH_IP";
  public static final String       PORT                                                  = "PORT";
  public static final String       QUEUE_NAME                                            = "QUEUE_NAME";
  public static final String       ACKNOWLEDGEMENT_QUEUE_NAME                            = "ACKNOWLEDGEMENT_QUEUE_NAME";
  public static final String       TASK_ID                                               = "taskId";
  public static final String       DELIVERY_TASK                                         = "deliveryTask";
  
  // Mapping Constants
  public static final String       KLASS_TAB_ID                                          = "klass";
  public static final String       TAXONOMY_TAB_ID                                       = "taxonomy";
  public static final String       PROPERTYCOLLECTION_TAB_ID                             = "propertyCollection";
  public static final String       RELATIONSHIP_TAB_ID                                   = "relationship";
  public static final String       CONTEXT_TAG_TAB_ID                                    = "contextTag";
  
  // Workflow Action Subtype
  public static final String       AFTER_PROPERTIES_SAVE                                 = "afterPropertiesSave";
  public static final String       AFTER_CLASSIFICATION_SAVE                             = "afterClassificationSave";
  public static final String       AFTER_RELATIONSHIP_SAVE                               = "afterRelationshipSave";
  public static final String       AFTER_CONTEXT_SAVE                                    = "afterContextSave";
  public static final String       AFTER_SAVE_UNDEFINED_SUB_TYPE                         = "undefined";
  
}
