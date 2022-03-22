package com.cs.core.config.interactor.entity.attribute;

public class SubstanceAttribute extends AbstractUnitAttribute implements ISubstanceAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.SUBSTANCE.name();
  }
}
