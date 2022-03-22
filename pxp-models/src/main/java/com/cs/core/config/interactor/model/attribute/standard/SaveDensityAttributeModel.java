package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.DensityAttribute;
import com.cs.core.config.interactor.entity.attribute.IDensityAttribute;

public class SaveDensityAttributeModel extends AbstractSaveUnitAttributeModel
    implements IDensityAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveDensityAttributeModel()
  {
    super(new DensityAttribute(), Renderer.DENSITY.toString());
  }
  
  public SaveDensityAttributeModel(IDensityAttribute attribute)
  {
    super(attribute, Renderer.DENSITY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
