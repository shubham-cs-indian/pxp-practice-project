package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IThermalInsulationAttribute;
import com.cs.core.config.interactor.entity.attribute.ThermalInsulationAttribute;

public class ThermalInsulationAttributeModel extends AbstractUnitAttributeModel
    implements IThermalInsulationAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public ThermalInsulationAttributeModel()
  {
    super(new ThermalInsulationAttribute(), Renderer.THERMAL_INSULATION.toString());
  }
  
  public ThermalInsulationAttributeModel(IThermalInsulationAttribute attribute)
  {
    super(attribute, Renderer.THERMAL_INSULATION.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
