package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDetermineSidesForRolledbackContentResponseModel extends IModel {
  
  String SIDE1_INSTANCE_ID = "side1InstanceId";
  String SIDE2_INSTANCE_ID = "side2InstanceId";
  
  public String getSide1InstanceId();
  
  public void setSide1InstanceId(String side1InstanceId);
  
  public String getSide2InstanceId();
  
  public void setSide2InstanceId(String side2InstanceId);
}
