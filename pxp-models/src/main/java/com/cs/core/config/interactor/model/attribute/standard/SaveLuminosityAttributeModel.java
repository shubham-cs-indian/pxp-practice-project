package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ILuminosityAttribute;
import com.cs.core.config.interactor.entity.attribute.LuminosityAttribute;

public class SaveLuminosityAttributeModel extends AbstractSaveUnitAttributeModel
    implements ILuminosityAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveLuminosityAttributeModel()
  {
    super(new LuminosityAttribute(), Renderer.LUMINOSITY.toString());
  }
  
  public SaveLuminosityAttributeModel(ILuminosityAttribute attribute)
  {
    super(attribute, Renderer.LUMINOSITY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
