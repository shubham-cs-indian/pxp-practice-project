package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.EnergyAttribute;
import com.cs.core.config.interactor.entity.attribute.IEnergyAttribute;

public class EnergyAttributeModel extends AbstractUnitAttributeModel
    implements IEnergyAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public EnergyAttributeModel()
  {
    super(new EnergyAttribute(), Renderer.ENERGY.toString());
  }
  
  public EnergyAttributeModel(IEnergyAttribute attribute)
  {
    super(attribute, Renderer.ENERGY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
