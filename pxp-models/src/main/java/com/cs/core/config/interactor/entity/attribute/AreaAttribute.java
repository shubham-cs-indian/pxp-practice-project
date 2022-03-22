package com.cs.core.config.interactor.entity.attribute;

public class AreaAttribute extends AbstractUnitAttribute implements IAreaAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.AREA.name();
  }
}
