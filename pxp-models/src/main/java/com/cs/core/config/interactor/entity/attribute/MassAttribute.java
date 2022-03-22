package com.cs.core.config.interactor.entity.attribute;

public class MassAttribute extends AbstractUnitAttribute implements IMassAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.MASS.name();
  }
}
