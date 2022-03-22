package com.cs.di.runtime.model.processinstance;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.di.workflow.constants.MessageType;

public interface IGetProcessInstanceModel extends IModel {
  
  public static final String FROM                 = "from";
  public static final String TO                   = "to";
  public static final String PROCESS_EVENT_IDS    = "processEventIds";
  public static final String ENDPOINT_IDS         = "endpointIds";
  public static final String MESSAGE_TYPES        = "messageTypes";
  public static final String USER_IDS             = "userIds";
  public static final String INSTANCE_IIDS = "instanceIIDs";
  public static final String PHYSICAL_CATALOGS    = "physicalCatalogs";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getTo();
  
  public void setTo(Long to);
  
  public List<String> getProcessEventIds();
  
  public void setProcessEventIds(List<String> processEventIds);
  
  public List<String> getEndpointIds();
  
  public void setEndpointIds(List<String> endpoint);
  
  public List<MessageType> getMessageTypes();
  
  public void setMessageTypes(List<MessageType> messageTypes);
  
  public List<Long> getUserIds();
  
  public void setUserIds(List<Long> userIds);
  
  public List<String> getInstanceIIDs();
  
  public void setInstanceIIDs(List<String> instanceIIDs);
//PXPFDEV-15368 : added for getting task details from workflow
  /** STARTS **/
  public Long getParentIID();
  
  public void setParentIID(Long parentIID);
  
  public String getProcessDefinition();
  
  public void setProcessDefinition(String definitionXml);
  /** ENDS **/
  
  public List<String> getPhysicalCatalogs();
  
  public void setPhysicalCatalogs(List<String> physicalCatalogs);
}
