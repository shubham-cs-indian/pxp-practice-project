package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IPressureAttribute;
import com.cs.core.config.interactor.entity.attribute.PressureAttribute;

public class SavePressureAttributeModel extends AbstractSaveUnitAttributeModel
    implements IPressureAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SavePressureAttributeModel()
  {
    super(new PressureAttribute(), Renderer.PRESSURE.toString());
  }
  
  public SavePressureAttributeModel(IPressureAttribute attribute)
  {
    super(attribute, Renderer.PRESSURE.toString());
  }
}
