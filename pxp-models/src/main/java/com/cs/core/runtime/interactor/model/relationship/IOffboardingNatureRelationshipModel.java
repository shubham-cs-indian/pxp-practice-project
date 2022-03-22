package com.cs.core.runtime.interactor.model.relationship;

public interface IOffboardingNatureRelationshipModel extends IOffboardingRelationshipModel {
  
  public static final String COUNT = "count";
  
  public String getCount();
  
  public void setCount(String count);
}
