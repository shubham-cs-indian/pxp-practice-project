package com.cs.core.config.strategy.model.attribute;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForCalculatedAttributeRequestModel extends IModel {

  public static final String PROPERTY_CODES_OF_CALCULATED_ATTRIBUTES = "propertyCodesOfCalculatedAttributes";

  public List<String> getPropertyCodesOfCalculatedAttributes();
  
  public void setPropertyCodesOfCalculatedAttributes(List<String> propertyCodesOfCalculatedAttributes);
  
}
