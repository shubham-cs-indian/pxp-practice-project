package com.cs.core.config.interactor.entity.attribute;

public class PotentialAttribute extends AbstractUnitAttribute implements IPotentialAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.POTENTIAL.name();
  }
}
