package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IRadiationAttribute;
import com.cs.core.config.interactor.entity.attribute.RadiationAttribute;

public class SaveRadiationAttributeModel extends AbstractSaveUnitAttributeModel
    implements IRadiationAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveRadiationAttributeModel()
  {
    super(new RadiationAttribute(), Renderer.RADIATION.toString());
  }
  
  public SaveRadiationAttributeModel(IRadiationAttribute attribute)
  {
    super(attribute, Renderer.RADIATION.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
