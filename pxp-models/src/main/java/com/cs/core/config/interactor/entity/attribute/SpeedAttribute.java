package com.cs.core.config.interactor.entity.attribute;

public class SpeedAttribute extends AbstractUnitAttribute implements ISpeedAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.SPEED.name();
  }
}
