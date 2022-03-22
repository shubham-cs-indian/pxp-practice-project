package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IPowerAttribute;
import com.cs.core.config.interactor.entity.attribute.PowerAttribute;

public class SavePowerAttributeModel extends AbstractSaveUnitAttributeModel
    implements IPowerAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SavePowerAttributeModel()
  {
    super(new PowerAttribute(), Renderer.POWER.toString());
  }
  
  public SavePowerAttributeModel(IPowerAttribute attribute)
  {
    super(attribute, Renderer.POWER.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
