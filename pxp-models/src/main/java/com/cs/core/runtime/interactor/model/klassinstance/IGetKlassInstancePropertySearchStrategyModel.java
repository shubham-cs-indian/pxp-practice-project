package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.runtime.interactor.model.datarule.IPropertySearchModel;

public interface IGetKlassInstancePropertySearchStrategyModel
    extends IGetKlassInstanceTreeStrategyModel {
  
  String ATTRIBUTE_PROPERTY_SEARCH = "attributePropertySearch";
  String ATTRIBUTE                 = "attribute";
  
  public IPropertySearchModel getAttributePropertySearch();
  
  public void setAttributePropertySearch(IPropertySearchModel propertySearchModel);
  
  public IAttributeModel getAttribute();
  
  public void setAttribute(IAttributeModel attribute);
}
