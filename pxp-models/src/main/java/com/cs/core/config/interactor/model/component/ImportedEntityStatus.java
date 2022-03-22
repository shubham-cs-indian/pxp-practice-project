package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.entity.configuration.base.AbstractRuntimeEntity;

public class ImportedEntityStatus extends AbstractRuntimeEntity implements IImportedEntityStatus {
  
  private static final long serialVersionUID = 1L;
  
  protected String          processInstanceId;
  protected String          componentId;
  protected String          klassInstanceId;
  protected String          importStatus;
  protected String          exceptionMessage;
  
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
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getImportStatus()
  {
    return importStatus;
  }
  
  @Override
  public void setImportStatus(String importStatus)
  {
    this.importStatus = importStatus;
  }
  
  @Override
  public String getExceptionMessage()
  {
    return this.exceptionMessage;
  }
  
  @Override
  public void setExceptionMessage(String exceptionMessage)
  {
    this.exceptionMessage = exceptionMessage;
  }
}
