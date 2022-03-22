package com.cs.core.config.interactor.entity.attribute;

public class IlluminanceAttribute extends AbstractUnitAttribute implements IIlluminanceAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.ILLUMINANCE.name();
  }
}
