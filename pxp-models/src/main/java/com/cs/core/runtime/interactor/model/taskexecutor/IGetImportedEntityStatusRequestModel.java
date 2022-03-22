package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetImportedEntityStatusRequestModel extends IModel {
  
  public static final String PROCESS_INSTANCE_ID   = "processInstanceId";
  public static final String COMPONENT_ID          = "componentId";
  public static final String IMPORTED_ENTITY_COUNT = "importedEntityCount";
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public Long getImportedEntityCount();
  
  public void setImportedEntityCount(Long importedEntityCount);
}
