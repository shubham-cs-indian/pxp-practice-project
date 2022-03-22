package com.cs.core.config.interactor.entity.processevent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessEvent implements IProcessEvent {
  
  private static final long     serialVersionUID   = 1L;
  protected String              id;
  protected String              label;
  protected String              type               = this.getClass().getName();
  protected String              eventType;//Business,Integration,Application Event
  protected Map<String, Object> processFlowGraph;
  protected String              processType;
  protected String              code;
  protected String              icon;
  protected String              iconKey;
  protected String              processDefinition;
  protected String              processDefinitionId;
  protected String              triggeringType;//actionType
  protected List<String>        klassIds;//natureType
  protected List<String>        taxonomyIds;//taxonomy
  protected Boolean             isXMLModified;
  protected boolean             isExecutable;
  protected List<String>        attributeIds;
  protected List<String>        tagIds;//tags
  protected List<String>        endpointIds;
  protected List<String>        physicalCatalogIds = new ArrayList<String>();
  protected String              timerDefinitionType;
  protected String              timerStartExpression;
  protected String              workflowType;                                // SCHEDULED/STANDARD/TASK
  protected String              entityType;
  protected Long                toSchedule;
  protected Long                fromSchedule;
  protected List<String>        authorizationMapping = new ArrayList<>();
  protected List<String>        actionSubType      = new ArrayList<String>();
  protected List<String>        nonNatureKlassIds  = new ArrayList<String>();// non-nature
  protected List<String>        mappings           = new ArrayList<>();
  protected String              ip;
  protected String              port;
  protected String              queue;
  protected String              acknowledgementQueue;
  protected Boolean             isTemplate;            
  protected String              frequencyActiveTabId;
  protected List<String>        organizationsIds = new ArrayList<>();
  protected List<String>        usecases      = new ArrayList<String>();
  
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return this.icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return this.iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  public String getEventType()
  {
    return eventType;
  }
  
  @Override
  public void setEventType(String eventType)
  {
    this.eventType = eventType;
  }
  
  @Override
  public Map<String, Object> getProcessFlowGraph()
  {
    return processFlowGraph;
  }
  
  @Override
  public void setProcessFlowGraph(Map<String, Object> processFlowGraph)
  {
    this.processFlowGraph = processFlowGraph;
  }
  
  @Override
  public String getProcessType()
  {
    return this.processType;
  }
  
  @Override
  public void setProcessType(String processType)
  {
    this.processType = processType;
  }
  
  @Override
  public String getProcessDefinition()
  {
    return processDefinition;
  }
  
  @Override
  public void setProcessDefinition(String processDefinition)
  {
    this.processDefinition = processDefinition;
  }
  
  @Override
  public String getProcessDefinitionId()
  {
    return processDefinitionId;
  }
  
  @Override
  public void setProcessDefinitionId(String processDefinitionId)
  {
    this.processDefinitionId = processDefinitionId;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return this.klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public String getTriggeringType()
  {
    return this.triggeringType;
  }
  
  @Override
  public void setTriggeringType(String triggeringType)
  {
    this.triggeringType = triggeringType;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return this.taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public Boolean getIsXMLModified()
  {
    return isXMLModified;
  }
  
  @Override
  public void setIsXMLModified(Boolean isXMLModified)
  {
    this.isXMLModified = isXMLModified;
  }
  
  @Override
  public boolean getIsExecutable()
  {
    return isExecutable;
  }
  
  @Override
  public void setIsExecutable(boolean isExecutable)
  {
    this.isExecutable = isExecutable;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> selectedAttributes)
  {
    this.attributeIds = selectedAttributes;
  }
  
  @Override
  public List<String> getTagIds()
  {
    return tagIds;
  }
  
  @Override
  public void setTagIds(List<String> selectedTags)
  {
    this.tagIds = selectedTags;
  }
  
  @Override
  public List<String> getEndpointIds()
  {
    return endpointIds;
  }
  
  @Override
  public void setEndpointIds(List<String> endpointIds)
  {
    this.endpointIds = endpointIds;
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return physicalCatalogIds;
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
  @Override
  public String getTimerDefinitionType()
  {
    return timerDefinitionType;
  }

  @Override
  public void setTimerDefinitionType(String timerDefinitionType)
  {
    this.timerDefinitionType = timerDefinitionType;
  }

  @Override
  public String getTimerStartExpression()
  {
    return timerStartExpression;
  }

  @Override
  public void setTimerStartExpression(String timerStartExpression)
  {
    this.timerStartExpression = timerStartExpression;
  }
  
  @Override
  public String getWorkflowType()
  {
    return workflowType;
  }

  @Override
  public void setWorkflowType(String workflowType)
  {
    this.workflowType = workflowType;
  }

  @Override
  public String getEntityType()
  {
    return entityType;
  }

  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }

  @Override
  public Long getToSchedule()
  {
    return toSchedule;
  }

  @Override
  public void setToSchedule(Long toSchedule)
  {
    this.toSchedule = toSchedule;
  }

  @Override
  public Long getFromSchedule()
  {
    return fromSchedule;
  }

  @Override
  public void setFromSchedule(Long fromSchedule)
  {
    this.fromSchedule = fromSchedule;
  }

  @Override
  public List<String> getNonNatureKlassIds()
  {
    return nonNatureKlassIds;
  }
  
  @Override
  public void setNonNatureKlassIds(List<String> nonNatureKlassIds)
  {
    this.nonNatureKlassIds = nonNatureKlassIds;
  }

  @Override
  public List<String> getActionSubType()
  {
    return actionSubType;
  }

  @Override
  public void setActionSubType(List<String> actionSubType)
  {
    this.actionSubType = actionSubType;
  }
  
  @Override
  public List<String> getMappings()
  {
    return mappings;
  }
  
  @Override
  public void setMappings(List<String> mappings)
  {
    this.mappings = mappings;
  }

  public List<String> getAuthorizationMapping()
  {
    return authorizationMapping;
  }

  @Override
  public void setAuthorizationMapping(List<String> authorizationMapping)
  {
   this.authorizationMapping = authorizationMapping;    
  }

  @Override
  public String getIp()
  {
    return ip;
  }

  @Override
  public void setIp(String ip)
  {
    this.ip = ip;
  }

  @Override
  public String getPort()
  {
    return port;
  }

  @Override
  public void setPort(String port)
  {
    this.port = port;
  }

  @Override
  public String getQueue()
  {
    return queue;
  }

  @Override
  public void setQueue(String queue)
  {
    this.queue = queue;
  }

  @Override
  public Boolean getIsTemplate()
  {
    return isTemplate;
  }

  @Override
  public void setIsTemplate(Boolean isTemplate)
  {
    this.isTemplate = isTemplate;
  }
    
  @Override
  public String getFrequencyActiveTabId()
  {
    return frequencyActiveTabId;
  }

  @Override
  public void setFrequencyActiveTabId(String frequencyActiveTabId)
  {
    this.frequencyActiveTabId = frequencyActiveTabId;
  }
  
  @Override
  public List<String> getOrganizationsIds()
  {
    return organizationsIds;
  }

  @Override
  public void setOrganizationsIds(List<String> organizationsIds)
  {
    this.organizationsIds = organizationsIds;
  }

  @Override
  public List<String> getUsecases()
  {
    return usecases;
  }

  @Override
  public void setUsecases(List<String> usecases)
  {
    this.usecases = usecases;
  }
}
