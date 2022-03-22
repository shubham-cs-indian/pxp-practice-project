package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICopyWorkflowModel extends IModel {
  

  public static final String FROM_WORKFLOW_ID          = "fromWorkflowId";
  public static final String TO_WORKFLOW_NAME          = "toWorkflowName";
  public static final String TO_WORKFLOW_CODE          = "toWorkflowCode";
  public static final String CLONE_WORKFLOW_SAVE_MODEL = "cloneWorkflowSaveModel";

   
  public String getFromWorkflowId();
  
  public void setFromWorkflowId(String fromWorkflowId);
  
  public String getToWorkflowName();
  
  public void setToWorkflowName(String toWorkflowName);
  
  public String getToWorkflowCode();
  
  public void setToWorkflowCode(String toWorkflowCode);
  
  public IProcessEventModel getCloneWorkflowSaveModel();
  
  public void setCloneWorkflowSaveModel(IProcessEventModel cloneWorkflowSaveModel);
  
 
}
