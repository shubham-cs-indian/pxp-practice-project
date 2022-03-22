package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IViscocityAttribute;
import com.cs.core.config.interactor.entity.attribute.ViscocityAttribute;

public class SaveViscocityAttributeModel extends AbstractSaveUnitAttributeModel
    implements IViscocityAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveViscocityAttributeModel()
  {
    super(new ViscocityAttribute(), Renderer.VISCOCITY.toString());
  }
  
  public SaveViscocityAttributeModel(IViscocityAttribute attribute)
  {
    super(attribute, Renderer.VISCOCITY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
