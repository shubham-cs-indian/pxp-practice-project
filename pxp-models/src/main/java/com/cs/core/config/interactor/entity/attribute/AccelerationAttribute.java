package com.cs.core.config.interactor.entity.attribute;

public class AccelerationAttribute extends AbstractUnitAttribute implements IAccelerationAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.ACCELERATION.name();
  }
}
