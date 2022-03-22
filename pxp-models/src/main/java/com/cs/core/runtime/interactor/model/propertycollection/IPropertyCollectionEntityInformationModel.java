package com.cs.core.runtime.interactor.model.propertycollection;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.io.Serializable;

public interface IPropertyCollectionEntityInformationModel extends IModel, Serializable {
  
  public static final String ID                   = "id";
  public static final String LABEL                = "label";
  public static final String ICON                 = "icon";
  public static final String ICON_KEY             = "iconKey";
  public static final String IS_STANDARD          = "isStandard";
  public static final String IS_FOR_X_RAY         = "isForXRay";
  public static final String IS_DEFAULT_FOR_X_RAY = "isDefaultForXRay";
  public static final String CODE                 = "code";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public Boolean getIsForXRay();
  
  public void setIsForXRay(Boolean isForXRay);
  
  public Boolean getIsDefaultForXRay();
  
  public void setIsDefaultForXRay(Boolean isDefaultForXRay);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
 public String getIconKey();
  
  public void setIconKey(String iconKey);
  
  public String getCode();
  
  public void setCode(String code);
}
