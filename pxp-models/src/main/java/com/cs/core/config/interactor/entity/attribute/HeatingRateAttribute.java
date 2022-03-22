package com.cs.core.config.interactor.entity.attribute;

public class HeatingRateAttribute extends AbstractUnitAttribute implements IHeatingRateAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.HEATING_RATE.name();
  }
}
