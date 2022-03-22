package com.cs.core.runtime.interactor.constants.application;

public class DiConstants {
  
  public static final String ATTRIBUTES                             = "attributes";
  public static final String TAGS                                   = "tags";
  public static final String ENTITY_TYPE                            = "Entity_type";
  public static final String GET_TAXONOMY_IDS                       = "getTaxonomyIds";
  public static final String ERROR_WHILE_SAVING                     = "Error while saving article for article Id : ";
  public static final String ID                                     = "id";
  public static final String TAG_ID                                 = "tagId";
  public static final String ARTILCE_CLASS_CODE                     = "article";
  public static final String NAME_ATTRIBUTE_CODE                    = "nameattribute";
  public static final String ERROR_FOR_CREATE                       = "Error occured while creating article.";
  public static final String DIEXCEPTION                            = "DiException";
  public static final String WRONG_ATTRIBUTE_OR_TAG_CODE            = "Wrong Attribute or Tag code given.";
  public static final String WRONG_CLASS_CODE                       = "Wrong class code given.";
  public static final String PRIMARY_KLASS_LIST                     = "primaryKlassList";
  public static final String SECONDARY_KLASS_LIST                   = "secondayKlassList";
  public static final String EXTRA_NATURE_KLASS_ERROR               = "Cannot create Article.More than 1 nature classes are given";
  public static final String ERROR_ADDING_NON_NATURE_CLASSES        = "Error adding Secondary Classes";
  public static final String SAVE_ARTIKLE                           = "Save_Artikle";
  // Taxonomy Import
  public static final String MAJOR_TAXONOMY                         = "majorTaxonomy";
  public static final String MINOR_TAXONOMY                         = "minorTaxonomy";
  // Common
  public static final String ADDED_ATTRIBUTES                       = "addedAttributes";
  public static final String MODIFIED_ATTRIBUTES                    = "modifiedAttributes";
  public static final String ADDED_TAGS                             = "addedTags";
  public static final String MODIFIED_TAGS                          = "modifiedTags";
  public static final String DATE_FORMAT                            = "yyyy-MM-dd";
  public static final String DATE_TIME_FORMAT                       = "yyyy-MM-dd hh:mm:ss.mmm";
  public static final String DATE_TIME_FORMAT_24HR                  = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String BODY                                   = "body";
  
  // Asset
  public static final String FILE_PATH                              = "filePath";
  public static final String FILE_SOURCE                            = "fileSource";
  public static final String JMS_LISTENER                           = "JMS Listener";
  public static final String JMS_SUPPLIER_IMPORT_LABEL              = "JMS Supplier Import";
  public static final String JMS_CONSUMER                           = "JMS Consumer";
  public static final String JMS_CONSUMER_ACKNOWLEDGEMENT           = "JMS Consumer Acknowledgement";
  public static final String JMS_PRODUCER                           = "JMS Producer";
  public static final String JMS_ARTICLE_IMPORT_LABEL               = "JMS Article Import";
  public static final String JMS_ASSET_IMPORT_LABEL                 = "JMS Asset Import";
  public static final String JMS_CHECK_EXPORT_ACKNOWLEDGEMENT_LABEL = "JMS Check Export Acknowledgement";
  public static final String JMS_NATURE_RELATIONSHIP_IMPORT_LABEL   = "JMS Nature Relationship Import";
  
  public static final String CONSUMER_IP                            = "consumerIp";
  public static final String CONSUMER_PORT                          = "consumerPort";
  public static final String CONSUMER_QUEUE_NAME                    = "consumerQueueName";
  public static final String CONSUMER_ACKNOWLEDGEMENT_QUEUE         = "consumerAcknowledgementQueue";
  public static final String PRODUCER_IP                            = "producerIp";
  public static final String PRODUCER_PORT                          = "producerPort";
  public static final String PRODUCER_QUEUE                         = "producerQueue";
  public static final String CONTEXT_TAB                            = "context_tab";
  public static final String RELATIONSHIP_TAB                       = "relationship_tab";
  public static final String BUT_EXPORT_COMPONENT_LABEL             = "But Product Export Component";
  public static final String JMS_RELATIONSHIP_IMPORT_LABEL          = "JMS Relationship Import";
  public static final String JMS_VARIANT_IMPORT_LABEL               = "JMS Variant Import";
  public static final String JMS_ATTRIBUTE_VARIANT_IMPORT_LABEL     = "JMS Attribute Variant Import";
  public static final String ERROR_MESSAGE                          = "errorMessage";
  
  public static final String LANGUAGE_CODE                          = "languageCode";
  
  public static final String STATUS_MODEL                           = "statusModel";
  
  // Controller names for DI
  /*  public static final String JMS_ARTICLE_IMPORT_CONTROLLER  = "articleJMSImportComponentController";
  public static final String JMS_VARIANT_IMPORT_CONTROLLER  = "articleVariantJMSImportComponentController";
  
  public static final List<String> JMS_IMPORT_CONTROLLERS = Arrays.asList(
      "${" + JMS_ARTICLE_IMPORT_CONTROLLER + "}" , "${" + JMS_VARIANT_IMPORT_CONTROLLER + "}",
      "${" + JMS_SUPPLIER_IMPORT_CONTROLLER + "}");*/
  public static final String JMS_SUPPLIER_IMPORT_CONTROLLER         = "supplierJMSImportComponentController";
  public static final String JMS_CONSUMER_CONTROLLERS               = "${JMSConsumerController}";
  public static final String ACK                                    = "Ack";
  public static final String PRODUCT                                = "Product";
  public static final String PIM                                    = "PIM";
  public static final String ENDPOINT_ID                            = "endpointId";
  public static final String ORGANIZATION_ID                        = "organizationId";
  public static final String PROPERTIES                             = "properties";
  public static final String NAME_ATTRIBUTE                         = "nameattribute";
  public static final String TAXONOMY_IDS                           = "taxonomyIds";
  public static final String PROPERTY_ATTRIBUTES                    = "attributes";
  public static final String ATTRIBUTE_VARIANTS                     = "attributeVariants";
  public static final String TYPEIDS                                = "typeIds";
  public static final String JMS_TARGET_IMPORT_LABEL                = "JMS Target Import";
  public static final String ESB                                    = "ESB";
  
  public static final String JMS                                    = "JMS";
  public static final String HOTFOLDER                              = "hotFolder";
  public static final String SOURCE_TYPE_PRODUCT                    = "product";
  public static final String SOURCE_TYPE_ASSET                      = "asset";
}
