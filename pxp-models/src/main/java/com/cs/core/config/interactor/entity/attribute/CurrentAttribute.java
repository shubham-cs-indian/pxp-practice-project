package com.cs.core.config.interactor.entity.attribute;

public class CurrentAttribute extends AbstractUnitAttribute implements ICurrentAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.CURRENT.name();
  }
}
