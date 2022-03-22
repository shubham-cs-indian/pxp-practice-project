package com.cs.core.runtime.interactor.constants.application;

import java.util.Date;

public class TransferConstants {
  
  public static final String PROCESS_STATUS_TABLE                          = "process_status";
  // public static final String RELATIONSHIP_DETAILS_TABLE =
  // "realtionship_details";
  
  public static final String KLASS_INSTANCE_STATUS_TABLE                   = "klass_instance_status";
  public static final String VARIANT_STATUS_TABLE                          = "variant_status";
  public static final String ATTRIBUTE_VARIANT_STATUS_TABLE                = "attribute_variant_status";
  public static final String RELATIONSHIP_STATUS_TABLE                     = "relationship_status";
  public static final String NATURE_RELATIONSHIP_STATUS_TABLE              = "nature_relationship_status";
  public static final String SOURCE_DESTINATION_TABLE                      = "source_destination";
  
  public static final String COLUMN_NAME                                   = "columnName";
  public static final String COLUMN_TYPE                                   = "columnType";
  public static final String COLUMN_SIZE                                   = "columnSize";
  public static final String COLUMN_CONSTRAINTS                            = "columnConstraints";
  
  public static final String KLASS_INSTANCE_TRIGGER_FUNCTION               = "klassinstancefunction";
  public static final String KLASS_INSTANCE_TRIGGER_NAME                   = "klassinstancetrigger";
  
  public static final String VARIANT_INSTANCE_TRIGGER_FUNCTION             = "variantinstancefunction";
  public static final String VARIANT_INSTANCE_TRIGGER_NAME                 = "variantinstancetrigger";
  public static final String ATTRIBUTE_VARIANT_INSTANCE_TRIGGER_FUNCTION   = "attributevariantinstancefunction";
  public static final String ATTRIBUTE_VARIANT_INSTANCE_TRIGGER_NAME       = "attributevariantinstancetrigger";
  public static final String RELATIONSHIP_INSTANCE_TRIGGER_NAME            = "relationshipinstancetrigger";
  public static final String RELATIONSHIP_INSTANCE_TRIGGER_FUNCTION        = "relationshipinstancefunction";
  public static final String NATURE_RELATIONSHIP_INSTANCE_TRIGGER_NAME     = "naturerelationshipinstancetrigger";
  public static final String NATURE_RELATIONSHIP_INSTANCE_TRIGGER_FUNCTION = "naturerelationshipinstancefunction";
  
  /** **************** postgres sql constants ****************************** */
  public static final String VARCHAR                                       = "VARCHAR";
  
  public static final String BOOLEAN                                       = Boolean.class
      .getSimpleName()
      .toUpperCase();
  public static final String INTEGER                                       = Integer.class
      .getSimpleName()
      .toUpperCase();
  public static final String DATE                                          = Date.class
      .getSimpleName()
      .toUpperCase();
  public static final String NOT_NULL                                      = " NOT NULL ";
  public static final String DEFAULT_CURRENT_TIMESTAMP                     = " DEFAULT CURRENT_TIMESTAMP";
  public static final String PRIMARY_KEY                                   = " PRIMARY KEY ";
}
