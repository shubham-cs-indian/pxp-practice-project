package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.AccelerationAttribute;
import com.cs.core.config.interactor.entity.attribute.IAccelerationAttribute;

public class AccelerationAttributeModel extends AbstractUnitAttributeModel
    implements IAccelerationAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public AccelerationAttributeModel()
  {
    super(new AccelerationAttribute(), Renderer.ACCELERATION.toString());
  }
  
  public AccelerationAttributeModel(IAccelerationAttribute attribute)
  {
    super(attribute, Renderer.ACCELERATION.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
