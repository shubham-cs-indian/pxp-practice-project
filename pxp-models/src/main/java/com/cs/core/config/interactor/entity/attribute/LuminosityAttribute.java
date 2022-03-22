package com.cs.core.config.interactor.entity.attribute;

public class LuminosityAttribute extends AbstractUnitAttribute implements ILuminosityAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.LUMINOSITY.name();
  }
}
