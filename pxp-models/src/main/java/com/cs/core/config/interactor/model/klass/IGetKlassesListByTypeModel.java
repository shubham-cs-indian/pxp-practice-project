package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetKlassesListByTypeModel extends IModel {
  
  public static final String ID          = "id";
  public static final String LABEL       = "label";
  public static final String TYPE        = "type";
  public static final String ICON        = "icon";
  public static final String NATURE_TYPE = "natureType";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getType();
  
  public void setType(String type);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public String getNatureType();
  
  public void setNatureType(String natureType);
}
