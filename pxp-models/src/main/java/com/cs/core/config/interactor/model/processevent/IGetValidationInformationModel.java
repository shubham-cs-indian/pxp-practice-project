package com.cs.core.config.interactor.model.processevent;

public interface IGetValidationInformationModel extends IProcessEventModel {
  
  public static final String VALIDATION_INFO = "validationInfo";
  
  public void setValidationInfo(IValidationInformationModel validationInfo);
  
  public IValidationInformationModel getValidationInfo();
}