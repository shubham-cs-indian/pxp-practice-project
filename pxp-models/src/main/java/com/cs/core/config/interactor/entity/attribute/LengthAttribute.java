package com.cs.core.config.interactor.entity.attribute;

public class LengthAttribute extends AbstractUnitAttribute implements ILengthAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.LENGTH.name();
  }
}
