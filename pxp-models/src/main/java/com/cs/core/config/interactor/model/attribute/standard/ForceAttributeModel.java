package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ForceAttribute;
import com.cs.core.config.interactor.entity.attribute.IForceAttribute;

public class ForceAttributeModel extends AbstractUnitAttributeModel
    implements IForceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public ForceAttributeModel()
  {
    super(new ForceAttribute(), Renderer.FORCE.toString());
  }
  
  public ForceAttributeModel(IForceAttribute attribute)
  {
    super(attribute, Renderer.FORCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
