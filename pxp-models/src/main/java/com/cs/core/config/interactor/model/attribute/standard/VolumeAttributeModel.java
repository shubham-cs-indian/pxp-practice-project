package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IVolumeAttribute;
import com.cs.core.config.interactor.entity.attribute.VolumeAttribute;

public class VolumeAttributeModel extends AbstractUnitAttributeModel
    implements IVolumeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public VolumeAttributeModel()
  {
    super(new VolumeAttribute(), Renderer.VOLUME.toString());
  }
  
  public VolumeAttributeModel(IVolumeAttribute attribute)
  {
    super(attribute, Renderer.VOLUME.toString());
  }
}
