package com.cs.core.runtime.interactor.model.taskexecutor;

public class GetImportedEntityStatusRequestModel implements IGetImportedEntityStatusRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          processInstanceId;
  protected String          componentId;
  protected Long            importedEntityCount;
  
  @Override
  public String getProcessInstanceId()
  {
    return this.processInstanceId;
  }
  
  @Override
  public void setProcessInstanceId(String processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
  
  @Override
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
  
  @Override
  public Long getImportedEntityCount()
  {
    return importedEntityCount;
  }
  
  @Override
  public void setImportedEntityCount(Long importedEntityCount)
  {
    this.importedEntityCount = importedEntityCount;
  }
}
