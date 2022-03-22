package com.cs.core.runtime.interactor.model.offboarding;

import java.util.HashMap;
import java.util.Map;

public class OffboardingKlassInstancesRequestModel extends InstancesOffboardingModel
    implements IOffboardingKlassInstancesRequestModel {
  
  private static final long     serialVersionUID = 1L;
  protected String              sourceIndexName;
  protected String              targetIndexName;
  protected String              tagGroupId;
  protected String              tagValueId;
  protected Map<String, Object> manualDataRules  = new HashMap<>();
  protected String              componentId;
  
  @Override
  public String getSourceIndexName()
  {
    return sourceIndexName;
  }
  
  @Override
  public void setSourceIndexName(String sourceIndexName)
  {
    this.sourceIndexName = sourceIndexName;
  }
  
  @Override
  public String getTargetIndexName()
  {
    return targetIndexName;
  }
  
  @Override
  public void setTargetIndexName(String targetIndexName)
  {
    this.targetIndexName = targetIndexName;
  }
  
  /*@Override
  public String getTagGroupId()
  {
    return tagGroupId;
  }
  
  @Override
  public void setTagGroupId(String tagGroupId)
  {
    this.tagGroupId = tagGroupId;
  }
  
  @Override
  public String getTagValueId()
  {
    return tagValueId;
  }
  
  @Override
  public void setTagValueId(String tagValueId)
  {
    this.tagValueId = tagValueId;
  }
  
  @Override
  public Map<String, Object> getManualDataRules()
  {
    return manualDataRules;
  }
  
  @Override
  public void setManualDataRules(Map<String, Object> manualDataRules)
  {
    this.manualDataRules = manualDataRules;
  }
  
  @Override
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }*/
  
}
