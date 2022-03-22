package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ITemperatureAttribute;
import com.cs.core.config.interactor.entity.attribute.TemperatureAttribute;

public class TemperatureAttributeModel extends AbstractUnitAttributeModel
    implements ITemperatureAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public TemperatureAttributeModel()
  {
    super(new TemperatureAttribute(), Renderer.TEMPERATURE.toString());
  }
  
  public TemperatureAttributeModel(ITemperatureAttribute attribute)
  {
    super(attribute, Renderer.TEMPERATURE.toString());
  }
}
