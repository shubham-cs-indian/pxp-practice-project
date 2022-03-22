package com.cs.core.config.interactor.model.tabs;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTabEntityModel extends IModel {
  
  public static final String ID                     = "id";
  public static final String LABEL                  = "label";
  public static final String ICON                   = "icon";
  public static final String ICON_KEY               = "iconKey";
  public static final String PROPERTY_SEQUENCE_LIST = "propertySequenceList";
  public static final String SEQUENCE               = "sequence";
  public static final String CODE                   = "code";
  public static final String IS_STANDARD            = "isStandard";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
 public String getIconKey();
  
  public void setIconKey(String iconKey);
  
  public List<String> getPropertySequenceList();
  
  public void setPropertySequenceList(List<String> propertySequenceList);
  
  public Integer getSequence();
  
  public void setSequence(Integer sequence);
  
  public String getCode();
  
  public void setCode(String code);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
}
