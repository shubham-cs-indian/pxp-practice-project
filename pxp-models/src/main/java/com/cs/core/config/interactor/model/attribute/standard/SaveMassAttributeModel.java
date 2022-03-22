package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IMassAttribute;
import com.cs.core.config.interactor.entity.attribute.MassAttribute;

public class SaveMassAttributeModel extends AbstractSaveUnitAttributeModel
    implements IMassAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveMassAttributeModel()
  {
    super(new MassAttribute(), Renderer.MASS.toString());
  }
  
  public SaveMassAttributeModel(IMassAttribute attribute)
  {
    super(attribute, Renderer.MASS.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
