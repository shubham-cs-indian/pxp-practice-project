package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IRotationFrequencyAttribute;
import com.cs.core.config.interactor.entity.attribute.RotationFrequencyAttribute;

public class SaveRotationFrequencyAttributeModel extends AbstractSaveUnitAttributeModel
    implements IRotationFrequencyAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveRotationFrequencyAttributeModel()
  {
    super(new RotationFrequencyAttribute(), Renderer.ROTATION_FREQUENCY.toString());
  }
  
  public SaveRotationFrequencyAttributeModel(IRotationFrequencyAttribute attribute)
  {
    super(attribute, Renderer.ROTATION_FREQUENCY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
