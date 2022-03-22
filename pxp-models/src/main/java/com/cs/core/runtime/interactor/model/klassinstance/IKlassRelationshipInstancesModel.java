package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IKlassRelationshipInstancesModel extends IModel {
  
  public static final String ID    = "id";
  public static final String TYPES = "types";
  public static final String SIZE  = "size";
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public String getId();
  
  public void setId(String id);
  
  public Integer getSize();
  
  public void setSize(Integer size);
}
