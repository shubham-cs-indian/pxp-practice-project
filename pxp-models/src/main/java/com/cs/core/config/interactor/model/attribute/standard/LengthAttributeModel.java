package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ILengthAttribute;
import com.cs.core.config.interactor.entity.attribute.LengthAttribute;

public class LengthAttributeModel extends AbstractUnitAttributeModel
    implements ILengthAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public LengthAttributeModel()
  {
    super(new LengthAttribute(), Renderer.LENGTH.toString());
  }
  
  public LengthAttributeModel(ILengthAttribute attribute)
  {
    super(attribute, Renderer.LENGTH.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
