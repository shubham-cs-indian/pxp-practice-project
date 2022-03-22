package com.cs.core.config.interactor.model.processevent;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetProcessEventModel extends ProcessEventModel implements IGetProcessEventModel {
  
  private static final long                  serialVersionUID = 1L;
  protected Map<String, Object>              processFlow      = new HashMap<>();
  protected IGetConfigDetailsModelForProcess configDetails;
  protected IValidationInformationModel      validationInfo;
  
  @Override
  public IGetConfigDetailsModelForProcess getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDetailsModelForProcess.class)
  public void setConfigDetails(IGetConfigDetailsModelForProcess configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Map<String, Object> getProcessFlow()
  {
    
    return processFlow;
  }
  
  @Override
  @JsonDeserialize(as = HashMap.class)
  public void setProcessFlow(Map<String, Object> processFlow)
  {
    this.processFlow = processFlow;
  }
  
  @Override
  public IValidationInformationModel getValidationInfo()
  {
    return validationInfo;
  }
  
  @JsonDeserialize(as = ValidationInformationModel.class)
  @Override
  public void setValidationInfo(IValidationInformationModel validationInfo)
  {
    this.validationInfo = validationInfo;
  }
}
