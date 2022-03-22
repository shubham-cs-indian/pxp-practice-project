package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.DateAttribute;
import com.cs.core.config.interactor.entity.attribute.IDateAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

public class DateAttributeModel extends AbstractAttributeModel implements IDateAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public DateAttributeModel()
  {
    super(new DateAttribute(), Renderer.DATE.toString());
  }
  
  public DateAttributeModel(IDateAttribute attribute)
  {
    super(attribute, Renderer.DATE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
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
}
