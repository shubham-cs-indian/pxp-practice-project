package com.cs.core.runtime.interactor.entity.propertyinstance;

public interface ITagInstance extends IContentTagInstance {
  
  public static final String IS_MATCH_AND_MERGE    = "isMatchAndMerge";
  public static final String CONTEXT_INSTANCE_ID   = "contextInstanceId";
  public static final String IS_MANDATORY_VIOLATED = "isMandatoryViolated";
  public static final String IS_SHOULD_VIOLATED    = "isShouldViolated";
  public static final String IS_CONFLICT_RESOLVED  = "isConflictResolved";
  
  public Boolean getIsMatchAndMerge();
  
  public void setIsMatchAndMerge(Boolean isMatchAndMerge);
  
  public void setContextInstanceId(String contextInstanceId);
  
  public String getContextInstanceId();
  
  public Boolean getIsMandatoryViolated();
  
  public void setIsMandatoryViolated(Boolean isMandatoryViolated);
  
  public Boolean getIsShouldViolated();
  
  public void setIsShouldViolated(Boolean isShouldViolated);
  
  public Boolean getIsConflictResolved();
  
  public void setIsConflictResolved(Boolean isConflictResolved);
}
