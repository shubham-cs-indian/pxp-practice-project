package com.cs.core.config.interactor.entity.attribute;

public class ProportionAttribute extends AbstractUnitAttribute implements IProportionAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.PROPORTION.name();
  }
}
