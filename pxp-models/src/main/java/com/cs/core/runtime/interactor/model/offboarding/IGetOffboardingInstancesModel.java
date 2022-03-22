package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

import java.util.List;

public interface IGetOffboardingInstancesModel extends IIdsListParameterModel {
  
  public static final String DOCTYPE             = "docType";
  public static final String SIDE_1_INSTANCE_IDS = "side1InstanceIds";
  
  public String getDocType();
  
  public void setDocType(String docType);
  
  public List<String> getSide1InstanceIds();
  
  public void setSide1InstanceIds(List<String> list);
}
