package com.cs.core.config.interactor.entity.attribute;

public class PowerAttribute extends AbstractUnitAttribute implements IPowerAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.POWER.name();
  }
}
