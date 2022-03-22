package com.cs.core.config.interactor.entity.attribute;

public class VolumeFlowRateAttribute extends AbstractUnitAttribute
    implements IVolumeFlowRateAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.VOLUME_FLOW_RATE.name();
  }
}
