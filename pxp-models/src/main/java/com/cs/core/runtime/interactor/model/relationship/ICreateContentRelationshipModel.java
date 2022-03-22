package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateContentRelationshipModel extends IModel {
  
  public static final String ID   = "id";
  public static final String TYPE = "type";
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
}
