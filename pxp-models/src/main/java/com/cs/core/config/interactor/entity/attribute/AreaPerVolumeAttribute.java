package com.cs.core.config.interactor.entity.attribute;

public class AreaPerVolumeAttribute extends AbstractUnitAttribute
    implements IAreaPerVolumeAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.AREA_PER_VOLUME.name();
  }
}
