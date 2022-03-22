package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;

public interface IImportedEntityStatus extends IRuntimeEntity {
  
  public static final String PROCESS_INSTANCE_ID = "processInstanceId";
  public static final String COMPONENT_ID        = "componentId";
  public static final String KLASS_INSTANCE_ID   = "klassInstanceId";
  public static final String IMPORT_STATUS       = "importStatus";
  public static final String EXCEPTION_MESSAGE   = "exceptionMessage";
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getImportStatus();
  
  public void setImportStatus(String importStatus);
  
  public String getExceptionMessage();
  
  public void setExceptionMessage(String exceptionMessage); // getExceptionMessage
}
