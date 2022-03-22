package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ILuminosityAttribute;
import com.cs.core.config.interactor.entity.attribute.LuminosityAttribute;

public class LuminosityAttributeModel extends AbstractUnitAttributeModel
    implements ILuminosityAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public LuminosityAttributeModel()
  {
    super(new LuminosityAttribute(), Renderer.LUMINOSITY.toString());
  }
  
  public LuminosityAttributeModel(ILuminosityAttribute attribute)
  {
    super(attribute, Renderer.LUMINOSITY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
