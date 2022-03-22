package com.cs.core.config.interactor.entity.attribute;

public class ResistanceAttribute extends AbstractUnitAttribute implements IResistanceAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.RESISTANCE.name();
  }
}
