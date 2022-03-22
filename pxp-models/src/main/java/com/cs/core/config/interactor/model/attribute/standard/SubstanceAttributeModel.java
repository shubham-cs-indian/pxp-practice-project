package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ISubstanceAttribute;
import com.cs.core.config.interactor.entity.attribute.SubstanceAttribute;

public class SubstanceAttributeModel extends AbstractUnitAttributeModel
    implements ISubstanceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SubstanceAttributeModel()
  {
    super(new SubstanceAttribute(), Renderer.SUBSTANCE.toString());
  }
  
  public SubstanceAttributeModel(ISubstanceAttribute attribute)
  {
    super(attribute, Renderer.SUBSTANCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
