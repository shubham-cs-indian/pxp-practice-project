package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

public class KlassRelationshipInstancesModel implements IKlassRelationshipInstancesModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    types;
  protected String          id;
  protected Integer         size;
  
  @Override
  public List<String> getTypes()
  {
    if (types == null) {
      types = new ArrayList<>();
    }
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
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
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
}
