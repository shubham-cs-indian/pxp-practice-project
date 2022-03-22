package com.cs.core.config.interactor.entity.attribute;

public class RadiationAttribute extends AbstractUnitAttribute implements IRadiationAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.RADIATION.name();
  }
}
