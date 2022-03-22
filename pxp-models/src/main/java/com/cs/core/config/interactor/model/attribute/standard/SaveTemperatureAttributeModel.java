package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ITemperatureAttribute;
import com.cs.core.config.interactor.entity.attribute.TemperatureAttribute;

public class SaveTemperatureAttributeModel extends AbstractSaveUnitAttributeModel
    implements ITemperatureAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveTemperatureAttributeModel()
  {
    super(new TemperatureAttribute(), Renderer.TEMPERATURE.toString());
  }
  
  public SaveTemperatureAttributeModel(ITemperatureAttribute attribute)
  {
    super(attribute, Renderer.TEMPERATURE.toString());
  }
}
