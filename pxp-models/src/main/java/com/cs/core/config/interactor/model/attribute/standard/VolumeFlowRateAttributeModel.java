package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IVolumeFlowRateAttribute;
import com.cs.core.config.interactor.entity.attribute.VolumeFlowRateAttribute;

public class VolumeFlowRateAttributeModel extends AbstractUnitAttributeModel
    implements IVolumeFlowRateAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public VolumeFlowRateAttributeModel()
  {
    super(new VolumeFlowRateAttribute(), Renderer.VOLUME_FLOW_RATE.toString());
  }
  
  public VolumeFlowRateAttributeModel(IVolumeFlowRateAttribute attribute)
  {
    super(attribute, Renderer.VOLUME_FLOW_RATE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
