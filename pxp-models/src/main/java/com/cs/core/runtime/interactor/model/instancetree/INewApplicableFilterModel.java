package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface INewApplicableFilterModel extends IModel {
  
  public static final String ID            = "id";
  public static final String LABEL         = "label";
  public static final String TYPE          = "type";
  public static final String CODE          = "code";
  public static final String PROPERTY_TYPE = "propertyType";
  public static final String ICON          = "icon";
  public static final String ICON_KEY 	   = "iconKey";
  public static final String DEFAULT_UNIT   = "defaultUnit";
  
  
  public String getDefaultUnit();
  public void setDefaultUnit(String defaultUnit);
  
  public String getType();
  public void setType(String type);
  
  public void setId(String id);
  public String getId();
  
  public String getCode();
  public void setCode(String code);
  
  public void setLabel(String label);
  public String getLabel();
  
  public String getPropertyType();
  public void setPropertyType(String propertyType);
  
  public String getIcon();
  public void setIcon(String icon);
  
  public String getIconKey();
  public void setIconKey(String iconKey);
}
