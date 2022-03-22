package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IAddedRelationshipInstanceModel extends IModel {
  
  public static final String INSTANCE_ID         = "instanceId";
  public static final String INSTANCE_VERSION_ID = "instanceVersionId";
  public static final String INSTANCE            = "instance";
  
  public String getInstanceId();
  
  public void setInstanceId(String instanceId);
  
  public String getInstanceVersionId();
  
  public void setInstanceVersionId(String instanceVersionId);
  
  public Map<String, Object> getInstance();
  
  public void setInstance(Map<String, Object> instance);
}
