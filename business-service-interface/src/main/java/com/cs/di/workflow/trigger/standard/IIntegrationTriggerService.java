package com.cs.di.workflow.trigger.standard;

public interface IIntegrationTriggerService extends IWorkflowEventTriggerService<IIntegrationTriggerModel> {
  
  public static final String SEARCH_CRITERIA          = "searchCriteria";
  public static final String CONFIG_PROPERTY_TYPE     = "configPropertyType";
  public static final String CONFIG_ENTITY_CODES      = "configEntityCodes";
  public static final String EXPORT_TYPE              = "EXPORT_TYPE";
  public static final String ENTITY                   = "entity";
  public static final String NODE_TYPE                = "configtype";
  public static final String RECEIVED_DATA            = "data";
  public static final String MAPPING_ID               = "mappingId";
  public static final String PARTNER_AUTHORIZATION_ID = "PARTNER_AUTHORIZATION_ID";
  public static final String LANGUAGE_CODE            = "LANGUAGE_CODE";
  public static final String PERMISSION_TYPES         = "permissionTypes";
  public static final String ROLE_IDS                 = "roleIds";
}
