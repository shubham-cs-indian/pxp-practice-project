package com.cs.di.config.model.initializeevent;

public class InitializeWorkflowEventModel implements IInitializeWorkflowEventModel {
  
  private static final long serialVersionUID = 1L;
  protected String          eventType;
  protected String          id;
  protected String          label;
  protected String          processType;
  protected String          fileName;
  protected Boolean         isXMLModified;

  @Override
  public String getEventType() {
    return eventType;
  }

  @Override
  public void setEventType(String eventType) {
    this.eventType = eventType;
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
  public String getProcessType()
  {
    return processType;
  }
  
  @Override
  public void setProcessType(String processType)
  {
    this.processType = processType;
  }
  
  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
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
}
