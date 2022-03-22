package com.cs.core.config.interactor.entity.attribute;

public class CapacitanceAttribute extends AbstractUnitAttribute implements ICapacitanceAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.CAPACITANCE.name();
  }
}
