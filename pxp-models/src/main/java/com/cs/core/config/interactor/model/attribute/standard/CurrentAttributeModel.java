package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.CurrentAttribute;
import com.cs.core.config.interactor.entity.attribute.ICurrentAttribute;

public class CurrentAttributeModel extends AbstractUnitAttributeModel
    implements ICurrentAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public CurrentAttributeModel()
  {
    super(new CurrentAttribute(), Renderer.CURRENT.toString());
  }
  
  public CurrentAttributeModel(ICurrentAttribute attribute)
  {
    super(attribute, Renderer.CURRENT.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
