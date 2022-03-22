package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IIconModel extends IModel {
  
  public static final String ID          = "id";
  public static final String LABEL       = "label";
  public static final String ICON_KEY    = "iconKey";
  public static final String CODE        = "code";
  public static final String MODIFIED_ON = "modifiedOn";
  public static final String CREATED_ON  = "createdOn";
  
  public void setId(String id);
  
  public String getId();
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getIconKey();
  
  public void setIconKey(String iconKey);
  
  public Long getCreatedOn();
  
  public void setCreatedOn(Long createdOn);
  
  public Long getModifiedOn();
  
  public void setModifiedOn(Long modifiedOn);
  
  public String getVersionId();
  
  public void setVersionId(String versionId);;
}
