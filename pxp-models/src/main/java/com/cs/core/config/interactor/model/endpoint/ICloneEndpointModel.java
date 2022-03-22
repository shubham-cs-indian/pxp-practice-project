package com.cs.core.config.interactor.model.endpoint;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICloneEndpointModel extends IModel {
  
  public static final String FROM_ENDPOINT_ID          = "fromEndPointId";
  public static final String TO_ENDPOINT_NAME          = "toEndPointName";
  public static final String TO_ENDPOINT_CODE          = "toEndPointCode";
  public static final String WORKFLOWS_TO_COPY         = "workflowsToCopy";
  
  public String getFromEndPointId();
  
  public void setFromEndPointId(String fromEndPointId);
  
  public String getToEndPointName();
  
  public void setToEndPointName(String toEndPointName);
  
  public String getToEndPointCode();
  
  public void setToEndPointCode(String toEndPointCode);
  
  public List<ICopyWorkflowModel> getWorkflowsToCopy();
  
  public void setWorkflowsToCopy(List<ICopyWorkflowModel> workflowsToCopy);
}
