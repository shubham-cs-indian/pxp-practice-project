package com.cs.core.config.interactor.model.processevent;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IValidationInformationModel extends IModel {
  
  public static final String IS_WORKFLOW_VALID  = "isWorkflowValid";
  public static final String VALIDATION_DETAILS = "validationDetails";
  
  public Boolean getIsWorkflowValid();
  
  public void setIsWorkflowValid(Boolean isWorkflowValid);
  
  public void setValidationDetails(Map<String, List<String>> validationDetails);
  
  public Map<String, List<String>> getValidationDetails();
}
