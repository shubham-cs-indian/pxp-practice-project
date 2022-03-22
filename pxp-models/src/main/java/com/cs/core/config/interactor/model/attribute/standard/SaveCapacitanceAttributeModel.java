package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.CapacitanceAttribute;
import com.cs.core.config.interactor.entity.attribute.ICapacitanceAttribute;

public class SaveCapacitanceAttributeModel extends AbstractSaveUnitAttributeModel
    implements ICapacitanceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveCapacitanceAttributeModel()
  {
    super(new CapacitanceAttribute(), Renderer.CAPACITANCE.toString());
  }
  
  public SaveCapacitanceAttributeModel(ICapacitanceAttribute attribute)
  {
    super(attribute, Renderer.CAPACITANCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
