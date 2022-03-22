package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IMagnetismAttribute;
import com.cs.core.config.interactor.entity.attribute.MagnetismAttribute;

public class SaveMagnetismAttributeModel extends AbstractSaveUnitAttributeModel
    implements IMagnetismAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveMagnetismAttributeModel()
  {
    super(new MagnetismAttribute(), Renderer.MAGNETISM.toString());
  }
  
  public SaveMagnetismAttributeModel(IMagnetismAttribute attribute)
  {
    super(attribute, Renderer.MAGNETISM.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
