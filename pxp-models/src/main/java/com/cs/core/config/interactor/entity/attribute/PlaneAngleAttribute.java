package com.cs.core.config.interactor.entity.attribute;

public class PlaneAngleAttribute extends AbstractUnitAttribute implements IPlaneAngleAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.PLANE_ANGLE.name();
  }
}
