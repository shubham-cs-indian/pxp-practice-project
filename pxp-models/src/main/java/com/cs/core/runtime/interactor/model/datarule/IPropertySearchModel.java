package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPropertySearchModel extends IModel {
  
  public static final String ATTRIBUTE_ID          = "attributeId";
  public static final String ATTRIBUTE_SEARCH_TEXT = "attributeSearchText";
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public String getAttributeSearchText();
  
  public void setAttributeSearchText(String attributeSearchText);
}
