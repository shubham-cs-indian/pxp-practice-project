package com.cs.core.config.interactor.entity.standard.attribute;

public class FileSupplierAttribute extends AbstractStandardAttribute
    implements IFileSupplierAttribute {
  
  private static final long serialVersionUID = 1L;
  
  public FileSupplierAttribute()
  {
  }
  
  public FileSupplierAttribute(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getRendererType()
  {
    return Renderer.TEXT.name();
  }
}
