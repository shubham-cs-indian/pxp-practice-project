package com.cs.core.config.interactor.entity.attribute;

public class DateAttribute extends AbstractAttribute implements IDateAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.DATE.name();
  }
}
