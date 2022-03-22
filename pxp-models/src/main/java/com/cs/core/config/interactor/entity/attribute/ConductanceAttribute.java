package com.cs.core.config.interactor.entity.attribute;

public class ConductanceAttribute extends AbstractUnitAttribute implements IConductanceAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.CONDUCTANCE.name();
  }
}
