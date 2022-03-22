package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.CapacitanceAttribute;
import com.cs.core.config.interactor.entity.attribute.ICapacitanceAttribute;

public class CapacitanceAttributeModel extends AbstractUnitAttributeModel
    implements ICapacitanceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public CapacitanceAttributeModel()
  {
    super(new CapacitanceAttribute(), Renderer.CAPACITANCE.toString());
  }
  
  public CapacitanceAttributeModel(ICapacitanceAttribute attribute)
  {
    super(attribute, Renderer.CAPACITANCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
