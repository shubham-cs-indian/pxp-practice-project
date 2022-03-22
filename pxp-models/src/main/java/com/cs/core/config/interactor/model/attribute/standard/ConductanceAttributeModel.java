package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ConductanceAttribute;
import com.cs.core.config.interactor.entity.attribute.IConductanceAttribute;

public class ConductanceAttributeModel extends AbstractUnitAttributeModel
    implements IConductanceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public ConductanceAttributeModel()
  {
    super(new ConductanceAttribute(), Renderer.CONDUCTANCE.toString());
  }
  
  public ConductanceAttributeModel(IConductanceAttribute attribute)
  {
    super(attribute, Renderer.CONDUCTANCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
