package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.AreaAttribute;
import com.cs.core.config.interactor.entity.attribute.IAreaAttribute;

public class AreaAttributeModel extends AbstractUnitAttributeModel implements IAreaAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public AreaAttributeModel()
  {
    super(new AreaAttribute(), Renderer.AREA.toString());
  }
  
  public AreaAttributeModel(IAreaAttribute attribute)
  {
    super(attribute, Renderer.AREA.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
