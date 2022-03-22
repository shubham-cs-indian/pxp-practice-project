package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetDefaultTypesResponseModel extends IModel {
  
  public static final String ID          = "id";
  public static final String TYPE        = "type";
  public static final String ICON        = "icon";
  public static final String ICON_KEY    = "iconKey";
  public static final String CODE        = "code";
  public static final String LABEL       = "label";
  public static final String NATURE_TYPE = "natureType";
  
  public String getId();
  public void setId(String id);
  
  public String getType();
  public void setType(String type);
  
  public String getIcon();
  public void setIcon(String icon);
  
  public String getIconKey();
  public void setIconKey(String iconKey);
  
  public String getCode();
  public void setCode(String code);
  
  public String getLabel();
  public void setLabel(String label);
  
  public String getNatureType();
  public void setNatureType(String natureType);

}
