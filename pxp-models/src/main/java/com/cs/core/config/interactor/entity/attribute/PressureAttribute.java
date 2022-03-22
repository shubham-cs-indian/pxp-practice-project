package com.cs.core.config.interactor.entity.attribute;

public class PressureAttribute extends AbstractUnitAttribute implements IPressureAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.PRESSURE.name();
  }
}
