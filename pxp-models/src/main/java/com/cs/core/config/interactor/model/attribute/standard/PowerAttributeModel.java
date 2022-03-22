package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IPowerAttribute;
import com.cs.core.config.interactor.entity.attribute.PowerAttribute;

public class PowerAttributeModel extends AbstractUnitAttributeModel
    implements IPowerAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public PowerAttributeModel()
  {
    super(new PowerAttribute(), Renderer.POWER.toString());
  }
  
  public PowerAttributeModel(IPowerAttribute attribute)
  {
    super(attribute, Renderer.POWER.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
