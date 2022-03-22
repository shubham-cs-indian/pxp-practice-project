package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IPressureAttribute;
import com.cs.core.config.interactor.entity.attribute.PressureAttribute;

public class PressureAttributeModel extends AbstractUnitAttributeModel
    implements IPressureAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public PressureAttributeModel()
  {
    super(new PressureAttribute(), Renderer.PRESSURE.toString());
  }
  
  public PressureAttributeModel(IPressureAttribute attribute)
  {
    super(attribute, Renderer.PRESSURE.toString());
  }
}
