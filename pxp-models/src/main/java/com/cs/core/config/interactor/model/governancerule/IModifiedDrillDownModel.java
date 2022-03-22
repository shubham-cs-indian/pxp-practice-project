package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedDrillDownModel extends IModel {
  
  public static final String ID            = "id";
  public static final String TYPE          = "type";
  public static final String ADDED_TYPES   = "addedTypes";
  public static final String DELETED_TYPES = "deletedTypes";
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getAddedTypes();
  
  public void setAddedTypes(List<String> addedTypes);
  
  public List<String> getDeletedTypes();
  
  public void setDeletedTypes(List<String> deletedTypes);
}
