package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IMassAttribute;
import com.cs.core.config.interactor.entity.attribute.MassAttribute;

public class MassAttributeModel extends AbstractUnitAttributeModel implements IMassAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public MassAttributeModel()
  {
    super(new MassAttribute(), Renderer.MASS.toString());
  }
  
  public MassAttributeModel(IMassAttribute attribute)
  {
    super(attribute, Renderer.MASS.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
