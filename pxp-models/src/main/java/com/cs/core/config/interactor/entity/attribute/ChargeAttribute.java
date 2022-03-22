package com.cs.core.config.interactor.entity.attribute;

public class ChargeAttribute extends AbstractUnitAttribute implements IChargeAttribute {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getRendererType()
  {
    return Renderer.CHARGE.name();
  }
}
