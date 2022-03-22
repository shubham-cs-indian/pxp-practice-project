package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ConductanceAttribute;
import com.cs.core.config.interactor.entity.attribute.IConductanceAttribute;

public class SaveConductanceAttributeModel extends AbstractSaveUnitAttributeModel
    implements IConductanceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveConductanceAttributeModel()
  {
    super(new ConductanceAttribute(), Renderer.CONDUCTANCE.toString());
  }
  
  public SaveConductanceAttributeModel(IConductanceAttribute attribute)
  {
    super(attribute, Renderer.CONDUCTANCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
