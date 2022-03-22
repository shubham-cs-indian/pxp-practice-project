package com.cs.core.config.interactor.entity.standard.attribute;

public class FileSizeAttribute extends AbstractStandardAttribute implements IFileSizeAttribute {
  
  private static final long serialVersionUID = 1L;
  
  public FileSizeAttribute()
  {
  }
  
  public FileSizeAttribute(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getRendererType()
  {
    return Renderer.TEXT.name();
  }
}
