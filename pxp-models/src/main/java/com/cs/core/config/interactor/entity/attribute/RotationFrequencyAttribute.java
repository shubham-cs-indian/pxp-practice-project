package com.cs.core.config.interactor.entity.attribute;

public class RotationFrequencyAttribute extends AbstractUnitAttribute
    implements IRotationFrequencyAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.ROTATION_FREQUENCY.name();
  }
}
