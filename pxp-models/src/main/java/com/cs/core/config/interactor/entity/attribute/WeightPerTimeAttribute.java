package com.cs.core.config.interactor.entity.attribute;

public class WeightPerTimeAttribute extends AbstractUnitAttribute
    implements IWeightPerTimeAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.WEIGHT_PER_TIME.name();
  }
}
