package com.cs.core.config.interactor.entity.attribute;

public class TextAttribute extends AbstractAttribute implements ITextAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.TEXT.name();
  }
}
