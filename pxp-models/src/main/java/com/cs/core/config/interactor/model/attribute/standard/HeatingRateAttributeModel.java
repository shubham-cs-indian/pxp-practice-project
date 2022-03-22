package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.HeatingRateAttribute;
import com.cs.core.config.interactor.entity.attribute.IHeatingRateAttribute;

public class HeatingRateAttributeModel extends AbstractUnitAttributeModel
    implements IHeatingRateAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public HeatingRateAttributeModel()
  {
    super(new HeatingRateAttribute(), Renderer.HEATING_RATE.toString());
  }
  
  public HeatingRateAttributeModel(IHeatingRateAttribute attribute)
  {
    super(attribute, Renderer.HEATING_RATE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
