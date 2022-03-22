package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.datarule.IAddedRelationshipInstanceModel;

import java.util.Map;

public class AddedRelationshipInstanceModel implements IAddedRelationshipInstanceModel {
  
  protected String              instanceId;
  protected String              instanceVersionId;
  protected Map<String, Object> instance;
  
  @Override
  public String getInstanceId()
  {
    return instanceId;
  }
  
  @Override
  public void setInstanceId(String instanceId)
  {
    this.instanceId = instanceId;
  }
  
  @Override
  public String getInstanceVersionId()
  {
    return instanceVersionId;
  }
  
  @Override
  public void setInstanceVersionId(String instanceVersionId)
  {
    this.instanceVersionId = instanceVersionId;
  }
  
  @Override
  public Map<String, Object> getInstance()
  {
    return instance;
  }
  
  @Override
  public void setInstance(Map<String, Object> instance)
  {
    this.instance = instance;
  }
}
