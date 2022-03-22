package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ChargeAttribute;
import com.cs.core.config.interactor.entity.attribute.IChargeAttribute;

public class SaveChargeAttributeModel extends AbstractSaveUnitAttributeModel
    implements IChargeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveChargeAttributeModel()
  {
    super(new ChargeAttribute(), Renderer.CHARGE.toString());
  }
  
  public SaveChargeAttributeModel(IChargeAttribute attribute)
  {
    super(attribute, Renderer.CHARGE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
