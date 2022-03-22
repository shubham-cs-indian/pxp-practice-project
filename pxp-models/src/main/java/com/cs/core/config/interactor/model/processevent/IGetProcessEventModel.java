package com.cs.core.config.interactor.model.processevent;
import java.util.Map;

public interface IGetProcessEventModel extends IProcessEventModel, IGetValidationInformationModel {
  
  public static final String PROCESS_FLOW     = "processFlow";
  public static final String CONFIG_DETAILS   = "configDetails";
  
  public Map<String, Object> getProcessFlow();
  
  public void setProcessFlow(Map<String, Object> processFlow);
  
  public IGetConfigDetailsModelForProcess getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsModelForProcess configDetails);
  

}