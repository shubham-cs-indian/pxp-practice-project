package com.cs.core.config.interactor.entity.attribute;

public class ViscocityAttribute extends AbstractUnitAttribute implements IViscocityAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.VISCOCITY.name();
  }
}
