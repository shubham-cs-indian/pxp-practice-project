package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;

/**
 * @author apurva.dhakre
 * 
 *    Model to copy workflow linked with an endpoint.
 */
public class CopyWorkflowModel implements ICopyWorkflowModel {
  
  private static final long serialVersionUID = 1L;
  
  public String             fromWorkflowId;
  public String             toWorkflowName;
  public String             toWorkflowCode;
  public IProcessEventModel cloneWorkflowSaveModel;
  
  @Override
  public String getFromWorkflowId()
  {
    return fromWorkflowId;
  }
  
  @Override
  public void setFromWorkflowId(String fromWorkflowId)
  {
    this.fromWorkflowId = fromWorkflowId;
    
  }
  
  @Override
  public String getToWorkflowName()
  {
    
    return toWorkflowName;
  }
  
  @Override
  public void setToWorkflowName(String toWorkflowName)
  {
    this.toWorkflowName = toWorkflowName;
    
  }
  
  @Override
  public String getToWorkflowCode()
  {
    return toWorkflowCode;
  }
  
  @Override
  public void setToWorkflowCode(String toWorkflowCode)
  {
    this.toWorkflowCode = toWorkflowCode;
    
  }
  
  @Override
  public IProcessEventModel getCloneWorkflowSaveModel()
  {
    return cloneWorkflowSaveModel;
  }
  
  @Override
  public void setCloneWorkflowSaveModel(IProcessEventModel cloneWorkflowSaveModel)
  {
    this.cloneWorkflowSaveModel = cloneWorkflowSaveModel;
  }

  
}
