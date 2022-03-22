package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ISpeedAttribute;
import com.cs.core.config.interactor.entity.attribute.SpeedAttribute;

public class SpeedAttributeModel extends AbstractUnitAttributeModel
    implements ISpeedAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SpeedAttributeModel()
  {
    super(new SpeedAttribute(), Renderer.SPEED.toString());
  }
  
  public SpeedAttributeModel(ISpeedAttribute attribute)
  {
    super(attribute, Renderer.SPEED.toString());
  }
}
