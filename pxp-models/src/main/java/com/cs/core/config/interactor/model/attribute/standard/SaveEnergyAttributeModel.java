package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.EnergyAttribute;
import com.cs.core.config.interactor.entity.attribute.IEnergyAttribute;

public class SaveEnergyAttributeModel extends AbstractSaveUnitAttributeModel
    implements IEnergyAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveEnergyAttributeModel()
  {
    super(new EnergyAttribute(), Renderer.ENERGY.toString());
  }
  
  public SaveEnergyAttributeModel(IEnergyAttribute attribute)
  {
    super(attribute, Renderer.ENERGY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
