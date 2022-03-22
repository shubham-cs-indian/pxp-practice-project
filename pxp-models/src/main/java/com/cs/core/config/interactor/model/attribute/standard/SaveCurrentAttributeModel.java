package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.CurrentAttribute;
import com.cs.core.config.interactor.entity.attribute.ICurrentAttribute;

public class SaveCurrentAttributeModel extends AbstractSaveUnitAttributeModel
    implements ICurrentAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveCurrentAttributeModel()
  {
    super(new CurrentAttribute(), Renderer.CURRENT.toString());
  }
  
  public SaveCurrentAttributeModel(ICurrentAttribute attribute)
  {
    super(attribute, Renderer.CURRENT.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
