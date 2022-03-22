package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.Map;

public class KlassInstanceStructureMergeModel implements IKlassInstanceStructureMergeModel {
  
  protected String              id;
  
  protected Boolean             isRemoved;
  
  protected String              label;
  
  protected String              data;
  
  protected Map<String, String> attributes;
  
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
  public Boolean getIsRemoved()
  {
    return isRemoved;
  }
  
  @Override
  public void setIsRemoved(Boolean isRemoved)
  {
    this.isRemoved = isRemoved;
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
  public String getData()
  {
    return data;
  }
  
  @Override
  public void setData(String data)
  {
    this.data = data;
  }
  
  @Override
  public Map<String, String> getAttributes()
  {
    return this.attributes;
  }
  
  @Override
  public void setAttributes(Map<String, String> attributes)
  {
    this.attributes = attributes;
  }
}
