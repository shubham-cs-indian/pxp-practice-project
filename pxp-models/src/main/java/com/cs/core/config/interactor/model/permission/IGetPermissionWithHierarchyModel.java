package com.cs.core.config.interactor.model.permission;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPermissionWithHierarchyModel extends IModel {
  
  public static final String ID          = "id";
  public static final String CODE        = "code";
  public static final String LABEL       = "label";
  public static final String IS_NATURE   = "isNature";
  public static final String TYPE        = "type";
  public static final String NATURE_TYPE = "natureType";
  public static final String PERMISSION  = "permission";
  
  public String getId();
  
  public void setId(String id);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Boolean getIsNature();
  
  public void setIsNature(Boolean isNature);
  
  public IPermissionModel getPermission();
  
  public void setPermission(IPermissionModel permission);
  
  public String getType();
  
  public void setType(String type);
  
  public String getNatureType();
  
  public void setNatureType(String natureType);
}
