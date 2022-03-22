package com.cs.core.config.interactor.model.endpoint;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CloneEndpointModel implements ICloneEndpointModel {
  
  private static final long serialVersionUID = 1L;
  
  public String                        fromEndPointId;
  public String                        toEndPointName;
  public String                        toEndPointCode;
  public List<ICopyWorkflowModel> workflowsToCopy;

  
  @Override
  public String getFromEndPointId()
  {
    return fromEndPointId;
  }
  
  @Override
  public void setFromEndPointId(String fromEndPointId)
  {
    this.fromEndPointId = fromEndPointId;
  }
  
  @Override
  public String getToEndPointName()
  {
    return toEndPointName;
  }
  
  @Override
  public void setToEndPointName(String toEndPointName)
  {
    this.toEndPointName = toEndPointName;
  }
  
  @Override
  public String getToEndPointCode()
  {
    return toEndPointCode;
  }
  
  @Override
  public void setToEndPointCode(String toEndPointCode)
  {
    this.toEndPointCode = toEndPointCode;
  }
  
  @Override
  public List<ICopyWorkflowModel> getWorkflowsToCopy()
  {
   return workflowsToCopy; 
  }
  
  @JsonDeserialize(contentAs = CopyWorkflowModel.class)
  @Override
  public void setWorkflowsToCopy(List<ICopyWorkflowModel> workflowsToCopy)
  {
    this.workflowsToCopy =  workflowsToCopy;
    
  }

}
