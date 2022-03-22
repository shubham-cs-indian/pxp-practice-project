package com.cs.core.config.interactor.entity.attribute;

public class ThermalInsulationAttribute extends AbstractUnitAttribute
    implements IThermalInsulationAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.THERMAL_INSULATION.name();
  }
}
