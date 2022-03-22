package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IIlluminanceAttribute;
import com.cs.core.config.interactor.entity.attribute.IlluminanceAttribute;

public class IlluminanceAttributeModel extends AbstractUnitAttributeModel
    implements IIlluminanceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public IlluminanceAttributeModel()
  {
    super(new IlluminanceAttribute(), Renderer.ILLUMINANCE.toString());
  }
  
  public IlluminanceAttributeModel(IIlluminanceAttribute attribute)
  {
    super(attribute, Renderer.ILLUMINANCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
