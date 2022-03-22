package com.cs.di.runtime.model.processinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.di.workflow.constants.MessageType;

public class GetProcessInstanceModel implements IGetProcessInstanceModel {
  
  private static final long   serialVersionUID = 1L;
  protected Long              from;
  protected Long              to;
  protected List<String>      processEventIds;
  protected List<MessageType> messageTypes;
  protected List<Long>        userIds;
  protected List<String>      endpointIds;
  protected List<String>      instanceIIDs;
  protected Long              parentIID;
  protected String            processDefinition;
  protected List<String>      physicalCatalogs;
  
  @Override
  public List<String> getProcessEventIds()
  {
    return processEventIds;
  }
  
  @Override
  public void setProcessEventIds(List<String> processEventIds)
  {
    this.processEventIds = processEventIds;
  }
  
  @Override
  public List<MessageType> getMessageTypes()
  {
    return messageTypes;
  }
  
  @Override
  public void setMessageTypes(List<MessageType> messageTypes)
  {
    this.messageTypes = messageTypes;
  }
  
  @Override
  public List<Long> getUserIds()
  {
    return userIds;
  }
  
  @Override
  public void setUserIds(List<Long> userIds)
  {
    this.userIds = userIds;
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
  public List<String> getInstanceIIDs()
  {
    if(instanceIIDs==null)
    {
      this.instanceIIDs = new ArrayList<String>();
    }
    return instanceIIDs;
  }
  
  @Override
  public void setInstanceIIDs(List<String> instanceIIDs)
  {
    this.instanceIIDs = instanceIIDs;
  }
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getTo()
  {
    return to;
  }
  
  @Override
  public void setTo(Long to)
  {
    this.to = to;
  }

  @Override
  public Long getParentIID()
  {
    return parentIID;
  }

  @Override
  public void setParentIID(Long parentIID)
  {
    this.parentIID = parentIID;
    
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
  public List<String> getPhysicalCatalogs()
  {
    return physicalCatalogs;
  }

  @Override
  public void setPhysicalCatalogs(List<String> physicalCatalogs)
  {
    this.physicalCatalogs = physicalCatalogs;
  }
}
