package com.cs.di.config.model.initializeevent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IInitializeWorkflowEventModel extends IModel{
  public String getEventType();
  public void setEventType(String eventType);

  public String getId();
  public void setId(String id);
  
  public String getLabel();
  public void setLabel(String label);
  
  public String getProcessType();
  public void setProcessType(String processType);
  
  public String getFileName();
  public void setFileName(String fileName);
  
  public Boolean getIsXMLModified();
  public void setIsXMLModified(Boolean isXMLModified);
}
