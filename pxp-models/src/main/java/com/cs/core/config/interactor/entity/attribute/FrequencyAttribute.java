package com.cs.core.config.interactor.entity.attribute;

public class FrequencyAttribute extends AbstractUnitAttribute implements IFrequencyAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.FREQUENCY.name();
  }
}
