package com.cs.core.config.interactor.entity.attribute;

public class EnergyAttribute extends AbstractUnitAttribute implements IEnergyAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.ENERGY.name();
  }
}
