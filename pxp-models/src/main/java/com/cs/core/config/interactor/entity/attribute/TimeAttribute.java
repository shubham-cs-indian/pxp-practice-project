package com.cs.core.config.interactor.entity.attribute;

public class TimeAttribute extends AbstractUnitAttribute implements ITimeAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.TIME.name();
  }
}
