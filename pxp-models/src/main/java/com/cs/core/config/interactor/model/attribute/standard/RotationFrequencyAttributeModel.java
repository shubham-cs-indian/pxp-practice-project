package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IRotationFrequencyAttribute;
import com.cs.core.config.interactor.entity.attribute.RotationFrequencyAttribute;

public class RotationFrequencyAttributeModel extends AbstractUnitAttributeModel
    implements IRotationFrequencyAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public RotationFrequencyAttributeModel()
  {
    super(new RotationFrequencyAttribute(), Renderer.ROTATION_FREQUENCY.toString());
  }
  
  public RotationFrequencyAttributeModel(IRotationFrequencyAttribute attribute)
  {
    super(attribute, Renderer.ROTATION_FREQUENCY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
