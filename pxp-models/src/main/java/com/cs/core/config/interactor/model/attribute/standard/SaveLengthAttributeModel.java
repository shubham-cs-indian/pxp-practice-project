package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ILengthAttribute;
import com.cs.core.config.interactor.entity.attribute.LengthAttribute;

public class SaveLengthAttributeModel extends AbstractSaveUnitAttributeModel
    implements ILengthAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveLengthAttributeModel()
  {
    super(new LengthAttribute(), Renderer.LENGTH.toString());
  }
  
  public SaveLengthAttributeModel(ILengthAttribute attribute)
  {
    super(attribute, Renderer.LENGTH.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
