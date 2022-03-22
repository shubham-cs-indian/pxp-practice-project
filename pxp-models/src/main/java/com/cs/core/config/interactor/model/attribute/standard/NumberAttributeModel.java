package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.INumberAttribute;
import com.cs.core.config.interactor.entity.attribute.NumberAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

public class NumberAttributeModel extends AbstractAttributeModel implements INumberAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public NumberAttributeModel()
  {
    super(new NumberAttribute(), Renderer.NUMBER.toString());
  }
  
  public NumberAttributeModel(INumberAttribute attribute)
  {
    super(attribute, Renderer.NUMBER.toString());
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public Integer getPrecision()
  {
    return ((INumberAttribute) attribute).getPrecision();
  }
  
  @Override
  public void setPrecision(Integer precision)
  {
    ((INumberAttribute) attribute).setPrecision(precision);
  }
  
  @Override
  public Boolean getHideSeparator()
  {
    return ((INumberAttribute) attribute).getHideSeparator();
  }
  
  @Override
  public void setHideSeparator(Boolean hideSeparator)
  {
    ((INumberAttribute) attribute).setHideSeparator(hideSeparator);
  }
}
