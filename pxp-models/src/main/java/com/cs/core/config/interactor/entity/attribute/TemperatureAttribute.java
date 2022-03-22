package com.cs.core.config.interactor.entity.attribute;

public class TemperatureAttribute extends AbstractUnitAttribute implements ITemperatureAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.TEMPERATURE.name();
  }
}
