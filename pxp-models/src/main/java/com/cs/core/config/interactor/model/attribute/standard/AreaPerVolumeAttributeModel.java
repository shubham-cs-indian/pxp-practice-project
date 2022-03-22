package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.AreaPerVolumeAttribute;
import com.cs.core.config.interactor.entity.attribute.IAreaPerVolumeAttribute;

public class AreaPerVolumeAttributeModel extends AbstractUnitAttributeModel
    implements IAreaPerVolumeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public AreaPerVolumeAttributeModel()
  {
    super(new AreaPerVolumeAttribute(), Renderer.AREA_PER_VOLUME.toString());
  }
  
  public AreaPerVolumeAttributeModel(IAreaPerVolumeAttribute attribute)
  {
    super(attribute, Renderer.AREA_PER_VOLUME.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
