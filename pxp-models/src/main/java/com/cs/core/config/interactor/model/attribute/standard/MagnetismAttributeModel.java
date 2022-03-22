package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IMagnetismAttribute;
import com.cs.core.config.interactor.entity.attribute.MagnetismAttribute;

public class MagnetismAttributeModel extends AbstractUnitAttributeModel
    implements IMagnetismAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public MagnetismAttributeModel()
  {
    super(new MagnetismAttribute(), Renderer.MAGNETISM.toString());
  }
  
  public MagnetismAttributeModel(IMagnetismAttribute attribute)
  {
    super(attribute, Renderer.MAGNETISM.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
