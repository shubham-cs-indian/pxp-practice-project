package com.cs.core.config.interactor.entity.attribute;

public class WeightPerAreaAttribute extends AbstractUnitAttribute
    implements IWeightPerAreaAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.WEIGHT_PER_AREA.name();
  }
}
