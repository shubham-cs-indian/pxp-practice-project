package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IIlluminanceAttribute;
import com.cs.core.config.interactor.entity.attribute.IlluminanceAttribute;

public class SaveIlluminanceAttributeModel extends AbstractSaveUnitAttributeModel
    implements IIlluminanceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveIlluminanceAttributeModel()
  {
    super(new IlluminanceAttribute(), Renderer.ILLUMINANCE.toString());
  }
  
  public SaveIlluminanceAttributeModel(IIlluminanceAttribute attribute)
  {
    super(attribute, Renderer.ILLUMINANCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
