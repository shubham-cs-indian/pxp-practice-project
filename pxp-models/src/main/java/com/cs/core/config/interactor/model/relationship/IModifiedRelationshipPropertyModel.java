package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IModifiedRelationshipPropertyModel extends IModel {
  
  public static final String SIDE          = "side";
  public static final String ID            = "id";
  public static final String COUPLING_TYPE = "couplingType";
  
  public String getId();
  
  public void setId(String id);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
  
  public String getSide();
  
  public void setSide(String side);
}
