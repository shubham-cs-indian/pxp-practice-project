package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IKlassInformationModel extends IConfigModel {
  
  public static String NATURE_TYPE = "natureType";
  public static String LABEL       = "label";
  public static String TYPE        = "type";
  public static String ICON        = "icon";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public String getType();
  
  public void setType(String type);
  
  public String getNatureType();
  
  public void setNatureType(String natureType);
}
