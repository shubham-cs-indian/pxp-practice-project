package com.cs.core.config.interactor.model.processevent;

import java.util.List;
import java.util.Map;

public class ValidationInformationModel implements IValidationInformationModel {
  
  private static final long serialVersionUID = 1L;
  Boolean                   isWorkflowValid;
  Map<String, List<String>> validationDetails;
  
  public ValidationInformationModel()
  {
  }
  public ValidationInformationModel(Boolean isWorkflowValid, Map<String, List<String>> validationDetails)
  {
    this.isWorkflowValid = isWorkflowValid;
    this.validationDetails = validationDetails;
  }
  
  @Override
  public Boolean getIsWorkflowValid()
  {
    return isWorkflowValid;
  }
  
  @Override
  public void setIsWorkflowValid(Boolean isWorkflowValid)
  {
    this.isWorkflowValid = isWorkflowValid;
  }
  
  public void setValidationDetails(Map<String, List<String>> validationDetails)
  {
    this.validationDetails = validationDetails;
  }
  
  @Override
  public Map<String, List<String>> getValidationDetails()
  {
    return validationDetails;
  }
}
