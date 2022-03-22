package com.cs.core.runtime.interactor.model.offboarding;

public interface IOffboardingKlassInstancesRequestModel extends IInstancesOffboardingModel {
  
  public static final String SOURCE_INDEX_NAME = "sourceIndexName";
  public static final String TARGET_INDEX_NAME = "targetIndexName";
  /*public static final String TAG_GROUP_ID      = "tagGroupId";
  public static final String TAG_VALUE_ID      = "tagValueId";
  public static final String MANUAL_DATA_RULES = "manualDataRules";
  public static final String COMPONENT_ID      = "componentId";*/
  
  public String getSourceIndexName();
  
  public void setSourceIndexName(String sourceIndexName);
  
  public String getTargetIndexName();
  
  public void setTargetIndexName(String targetIndexName);
  
  /*public String getTagGroupId();
  
  public void setTagGroupId(String tagGroupId);
  
  public String getTagValueId();
  
  public void setTagValueId(String tagValueId);
  
  public Map<String, Object> getManualDataRules();
  public void setManualDataRules(Map<String, Object> manualDataRules);
  
  public String getComponentId();
  public void setComponentId(String componentId);*/
  
}
