package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.AreaPerVolumeAttribute;
import com.cs.core.config.interactor.entity.attribute.IAreaPerVolumeAttribute;

public class SaveAreaPerVolumeAttributeModel extends AbstractSaveUnitAttributeModel
    implements IAreaPerVolumeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveAreaPerVolumeAttributeModel()
  {
    super(new AreaPerVolumeAttribute(), Renderer.AREA_PER_VOLUME.toString());
  }
  
  public SaveAreaPerVolumeAttributeModel(IAreaPerVolumeAttribute attribute)
  {
    super(attribute, Renderer.AREA_PER_VOLUME.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
