package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IVolumeAttribute;
import com.cs.core.config.interactor.entity.attribute.VolumeAttribute;

public class SaveVolumeAttributeModel extends AbstractSaveUnitAttributeModel
    implements IVolumeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveVolumeAttributeModel()
  {
    super(new VolumeAttribute(), Renderer.VOLUME.toString());
  }
  
  public SaveVolumeAttributeModel(IVolumeAttribute attribute)
  {
    super(attribute, Renderer.VOLUME.toString());
  }
}
