package com.cs.core.config.interactor.entity.processevent;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

import java.util.List;
import java.util.Map;

public interface IProcessEvent extends IConfigMasterEntity {
  
  public static final String EVENT_TYPE            = "eventType";
  public static final String PROCESS_FLOW_GRAPH    = "processFlowGraph";
  public static final String PROCESS_TYPE          = "processType";
  public static final String PROCESS_DEFINITION    = "processDefinition";
  public static final String PROCESS_DEFINITION_ID = "processDefinitionId";
  public static final String KLASS_IDS             = "klassIds";
  public static final String TAXONOMY_IDS          = "taxonomyIds";
  public static final String TRIGGERING_TYPE       = "triggeringType";
  public static final String IS_XML_MODIFIED       = "isXMLModified";
  public static final String IS_EXECUTABLE         = "isExecutable";
  public static final String ATTRIBUTE_IDS         = "attributeIds";
  public static final String TAG_IDS               = "tagIds";
  public static final String ENDPOINT_IDS          = "endpointIds";
  public static final String PHYSICAL_CATALOG_IDS  = "physicalCatalogIds";
  public static final String TIMER_DEFINITION_TYPE  = "timerDefinitionType";
  public static final String TIMER_START_EXPRESSION = "timerStartExpression";
  public static final String WORKFLOW_TYPE          = "workflowType";
  public static final String ENTITY_TYPE            = "entityType";
  public static final String TO_SCHEDULE            = "toSchedule";
  public static final String FROM_SCHEDULE          = "fromSchedule";
  public static final String NON_NATURE_KLASS_IDS   = "nonNatureKlassIds";
  public static final String TRIGGER_TYPE           = "triggerType";
  public static final String ACTION_SUB_TYPE        = "actionSubType";
  public static final String MAPPINGS               = "mappings";
  public static final String AUTHORIZATION_MAPPING  = "authorizationMapping";
  public static final String IP                     = "ip";
  public static final String PORT                   = "port";
  public static final String QUEUE                  = "queue";
  public static final String FREQUENCY_ACTIVE_TAB_ID  = "frequencyActiveTabId";
  public static final String ORGANIZATIONS_IDS       = "organizationsIds";
  public static final String USECASES                = "usecases";
  
  public String getEventType();
  
  public void setEventType(String eventType);
  
  public Map<String, Object> getProcessFlowGraph();
  
  public void setProcessFlowGraph(Map<String, Object> processFlowGraph);
  
  public String getProcessType();
  
  public void setProcessType(String processType);
  
  public String getProcessDefinition();
  
  public void setProcessDefinition(String processDefinition);
  
  public String getProcessDefinitionId();
  
  public void setProcessDefinitionId(String processDefinitionId);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public String getTriggeringType();
  
  public void setTriggeringType(String triggeringType);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public Boolean getIsXMLModified();
  
  public void setIsXMLModified(Boolean isXMLModified);
  
  public boolean getIsExecutable();
  
  public void setIsExecutable(boolean isExecutable);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
  
  public List<String> getEndpointIds();
  
  public void setEndpointIds(List<String> endpointIds);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public String getTimerDefinitionType();
  public void setTimerDefinitionType(String timerDefinitionType);

  public String getTimerStartExpression();
  public void setTimerStartExpression(String timerStartExpression);
  
  public String getWorkflowType();
  public void setWorkflowType(String workflowType);

  public String getEntityType();
  public void setEntityType(String entityType);

  public Long getToSchedule();
  public void setToSchedule(Long toSchedule);

  public Long getFromSchedule();
  public void setFromSchedule(Long fromSchedule);

  public List<String> getNonNatureKlassIds();
  public void setNonNatureKlassIds(List<String> nonNatureKlassIds);

  public List<String> getActionSubType();
  public void setActionSubType(List<String> actionSubType);
  
  public List<String> getMappings();
  public void setMappings(List<String> mappings);
  
  public List<String> getAuthorizationMapping();
  public void setAuthorizationMapping(List<String> authorizationMapping);

  public String getIp();
  public void setIp(String ip);
  
  public String getPort();
  public void setPort(String port);
  
  public String getQueue();
  public void setQueue(String queue);
  
  public Boolean getIsTemplate();
  public void setIsTemplate(Boolean isTemplate);
  
  public String getFrequencyActiveTabId();
  public void setFrequencyActiveTabId(String frequencyActiveTabId);
  

  public List<String> getOrganizationsIds();
  public void setOrganizationsIds(List<String> organizationsIds);
  
  public List<String> getUsecases();
  public void setUsecases(List<String> usecases);
}
