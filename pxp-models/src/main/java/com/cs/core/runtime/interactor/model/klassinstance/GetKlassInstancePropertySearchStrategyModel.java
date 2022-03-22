package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.config.interactor.model.propertycollection.PropertySearchModel;
import com.cs.core.runtime.interactor.model.datarule.IPropertySearchModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassInstancePropertySearchStrategyModel extends GetKlassInstanceTreeStrategyModel
    implements IGetKlassInstancePropertySearchStrategyModel {
  
  /**
   *
   */
  private static final long      serialVersionUID = 1L;
  
  protected IPropertySearchModel propertySearchModel;
  protected IAttributeModel      attribute;
  
  @Override
  public IPropertySearchModel getAttributePropertySearch()
  {
    return propertySearchModel;
  }
  
  @JsonDeserialize(as = PropertySearchModel.class)
  @Override
  public void setAttributePropertySearch(IPropertySearchModel propertySearchModel)
  {
    this.propertySearchModel = propertySearchModel;
  }
  
  @Override
  public IAttributeModel getAttribute()
  {
    return attribute;
  }
  
  @JsonDeserialize(as = AbstractAttributeModel.class)
  @Override
  public void setAttribute(IAttributeModel attribute)
  {
    this.attribute = attribute;
  }
}
