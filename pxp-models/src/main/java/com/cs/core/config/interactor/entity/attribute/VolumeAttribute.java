package com.cs.core.config.interactor.entity.attribute;

public class VolumeAttribute extends AbstractUnitAttribute implements IVolumeAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.VOLUME.name();
  }
}
