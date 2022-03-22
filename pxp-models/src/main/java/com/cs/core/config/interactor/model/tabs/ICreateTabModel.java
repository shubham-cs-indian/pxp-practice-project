package com.cs.core.config.interactor.model.tabs;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateTabModel extends IModel {
  
  public static final String ID          = "id";
  public static final String LABEL       = "label";
  public static final String ICON        = "icon";
  public static final String CODE        = "code";
  public static final String IS_STANDARD = "isStandard";
  public static final String SEQUENCE    = "sequence";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getCode();
  
  public void setCode(String code);
  
  public Boolean getIsStandard();
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public void setIsStandard(Boolean isStandard);
  
  public Integer getSequence();
  
  public void setSequence(Integer sequence);
}
