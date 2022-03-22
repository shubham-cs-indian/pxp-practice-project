package com.cs.core.config.strategy.model.attribute;

import java.util.ArrayList;
import java.util.List;

public class ConfigDetailsForCalculatedAttributeRequestModel implements IConfigDetailsForCalculatedAttributeRequestModel {

  protected List<String> propertyCodesOfCalculatedAttributes = new ArrayList<>();
  
  @Override
  public List<String> getPropertyCodesOfCalculatedAttributes()
  {
    return propertyCodesOfCalculatedAttributes;
  }

  @Override
  public void setPropertyCodesOfCalculatedAttributes(List<String> propertyCodesOfCalculatedAttributes)
  {
    this.propertyCodesOfCalculatedAttributes = propertyCodesOfCalculatedAttributes;
  }
  
}
