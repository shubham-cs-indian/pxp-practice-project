package com.cs.core.config.interactor.entity.attribute;

public class ForceAttribute extends AbstractUnitAttribute implements IForceAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.FORCE.name();
  }
}
