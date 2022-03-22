package com.cs.core.config.interactor.entity.attribute;

public class MagnetismAttribute extends AbstractUnitAttribute implements IMagnetismAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.MAGNETISM.name();
  }
}
