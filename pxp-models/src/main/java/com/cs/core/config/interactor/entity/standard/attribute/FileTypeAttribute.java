package com.cs.core.config.interactor.entity.standard.attribute;

public class FileTypeAttribute extends AbstractStandardAttribute implements IFileTypeAttribute {
  
  private static final long serialVersionUID = 1L;
  
  public FileTypeAttribute()
  {
  }
  
  public FileTypeAttribute(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getRendererType()
  {
    return Renderer.TEXT.name();
  }
}
