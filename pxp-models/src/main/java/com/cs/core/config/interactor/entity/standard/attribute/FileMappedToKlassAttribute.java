package com.cs.core.config.interactor.entity.standard.attribute;

public class FileMappedToKlassAttribute extends AbstractStandardAttribute
    implements IFileMappedToKlassAttribute {
  
  private static final long serialVersionUID = 1L;
  
  public FileMappedToKlassAttribute()
  {
  }
  
  public FileMappedToKlassAttribute(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getRendererType()
  {
    return Renderer.TEXT.name();
  }
}
