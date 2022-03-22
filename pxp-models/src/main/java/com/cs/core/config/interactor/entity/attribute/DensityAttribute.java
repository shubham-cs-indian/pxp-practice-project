package com.cs.core.config.interactor.entity.attribute;

public class DensityAttribute extends AbstractUnitAttribute implements IDensityAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.DENSITY.name();
  }
}
