package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.runtime.interactor.model.datarule.IPropertySearchModel;

public class PropertySearchModel implements IPropertySearchModel {
  
  private static final long serialVersionUID = 1L;
  protected String          attributeId;
  protected String          attributeSearchText;
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public String getAttributeSearchText()
  {
    return attributeSearchText;
  }
  
  @Override
  public void setAttributeSearchText(String attributeSearchText)
  {
    this.attributeSearchText = attributeSearchText;
  }
}
