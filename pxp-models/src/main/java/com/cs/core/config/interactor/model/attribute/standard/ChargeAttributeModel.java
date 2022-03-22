package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ChargeAttribute;
import com.cs.core.config.interactor.entity.attribute.IChargeAttribute;

public class ChargeAttributeModel extends AbstractUnitAttributeModel
    implements IChargeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public ChargeAttributeModel()
  {
    super(new ChargeAttribute(), Renderer.CHARGE.toString());
  }
  
  public ChargeAttributeModel(IChargeAttribute attribute)
  {
    super(attribute, Renderer.CHARGE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
