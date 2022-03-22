package com.cs.core.config.interactor.model.tabs;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveTabModel extends IModel {
  
  public static final String ID                         = "id";
  public static final String LABEL                      = "label";
  public static final String ICON                       = "icon";
  public static final String MODIFIED_PROPERTY_SEQUENCE = "modifiedPropertySequence";
  public static final String CODE                       = "code";
  public static final String MODIFIED_TAB_SEQUENCE      = "modifiedTabSequence";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public List<IModifiedTabPropertySequenceModel> getModifiedPropertySequence();
  
  public void setModifiedPropertySequence(
      List<IModifiedTabPropertySequenceModel> modifiedPropertySequence);
  
  public Integer getModifiedTabSequence();
  
  public void setModifiedTabSequence(Integer newTabSequence);
}
