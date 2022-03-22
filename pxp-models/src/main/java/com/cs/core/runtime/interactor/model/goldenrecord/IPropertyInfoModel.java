package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPropertyInfoModel extends IModel {
  
  public static final String PROPERTY_ID     = "propertyId";
  public static final String TYPE            = "type";
  public static final String IS_TRANSLATABLE = "isTranslatable";
  
  public String getPropertyId();
  
  public void setPropertyId(String propertyId);
  
  public String getType();
  
  public void setType(String type);
  
  public Boolean getIsTranslatable();
  
  public void setIsTranslatable(Boolean isTranslatable);
}
