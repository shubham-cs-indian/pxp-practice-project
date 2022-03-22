package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.List;
import java.util.Map;

public class TaskReferencedInstanceModel implements ITaskReferencedInstanceModel {
  
  private static final long     serialVersionUID = 1L;
  protected String              id;
  protected String              label;
  protected List<String>        types;
  protected Map<String, Object> assetInstance;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public List<String> getTypes()
  {
    
    return this.types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public Map<String, Object> getAssetInstance()
  {
    return assetInstance;
  }
  
  @Override
  public void setAssetInstance(Map<String, Object> assetInstance)
  {
    this.assetInstance = assetInstance;
  }
}
