package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.DensityAttribute;
import com.cs.core.config.interactor.entity.attribute.IDensityAttribute;

public class DensityAttributeModel extends AbstractUnitAttributeModel
    implements IDensityAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public DensityAttributeModel()
  {
    super(new DensityAttribute(), Renderer.DENSITY.toString());
  }
  
  public DensityAttributeModel(IDensityAttribute attribute)
  {
    super(attribute, Renderer.DENSITY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
