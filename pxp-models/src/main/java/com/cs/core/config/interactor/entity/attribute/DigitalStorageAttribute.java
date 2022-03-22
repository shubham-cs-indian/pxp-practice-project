package com.cs.core.config.interactor.entity.attribute;

public class DigitalStorageAttribute extends AbstractUnitAttribute
    implements IDigitalStorageAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.DIGITAL_STORAGE.name();
  }
}
