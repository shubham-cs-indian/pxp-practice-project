package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ISpeedAttribute;
import com.cs.core.config.interactor.entity.attribute.SpeedAttribute;

public class SaveSpeedAttributeModel extends AbstractSaveUnitAttributeModel
    implements ISpeedAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveSpeedAttributeModel()
  {
    super(new SpeedAttribute(), Renderer.SPEED.toString());
  }
  
  public SaveSpeedAttributeModel(ISpeedAttribute attribute)
  {
    super(attribute, Renderer.SPEED.toString());
  }
}
