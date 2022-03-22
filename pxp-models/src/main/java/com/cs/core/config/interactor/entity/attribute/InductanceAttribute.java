package com.cs.core.config.interactor.entity.attribute;

public class InductanceAttribute extends AbstractUnitAttribute implements IInductanceAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.INDUCTANCE.name();
  }
}
