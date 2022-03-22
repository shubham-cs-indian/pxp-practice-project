package com.cs.core.runtime.interactor.model.goldenrecord;

public class PropertyInfoModel implements IPropertyInfoModel {
  
  private static final long serialVersionUID = 1L;
  private String            propertyId;
  private String            type;
  private Boolean           isTranslatable   = false;
  
  @Override
  public String getPropertyId()
  {
    return propertyId;
  }
  
  @Override
  public void setPropertyId(String propertyId)
  {
    this.propertyId = propertyId;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Boolean getIsTranslatable()
  {
    return isTranslatable;
  }
  
  @Override
  public void setIsTranslatable(Boolean isTranslatable)
  {
    this.isTranslatable = isTranslatable;
  }
}
