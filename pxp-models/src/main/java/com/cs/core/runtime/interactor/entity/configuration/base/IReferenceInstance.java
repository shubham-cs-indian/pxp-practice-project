package com.cs.core.runtime.interactor.entity.configuration.base;

public interface IReferenceInstance extends IRuntimeEntity {
  
  public static final String REFERENCE_ID         = "referenceId";
  public static final String SIDE1_INSTANCE_ID    = "side1InstanceId";
  public static final String SIDE2_INSTANCE_ID    = "side2InstanceId";
  public static final String ORIGINAL_INSTANCE_ID = "originalInstanceId";
  
  public String getSide1InstanceId();
  
  public void setSide1InstanceId(String side1Id);
  
  public String getSide2InstanceId();
  
  public void setSide2InstanceId(String side2Id);
  
  public String getReferenceId();
  
  public void setReferenceId(String referenceId);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
}
