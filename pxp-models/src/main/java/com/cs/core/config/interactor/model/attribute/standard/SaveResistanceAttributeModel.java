package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IResistanceAttribute;
import com.cs.core.config.interactor.entity.attribute.ResistanceAttribute;

public class SaveResistanceAttributeModel extends AbstractSaveUnitAttributeModel
    implements IResistanceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveResistanceAttributeModel()
  {
    super(new ResistanceAttribute(), Renderer.RESISTANCE.toString());
  }
  
  public SaveResistanceAttributeModel(IResistanceAttribute attribute)
  {
    super(attribute, Renderer.RESISTANCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
