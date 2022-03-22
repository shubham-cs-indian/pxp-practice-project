package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IInductanceAttribute;
import com.cs.core.config.interactor.entity.attribute.InductanceAttribute;

public class InductanceAttributeModel extends AbstractUnitAttributeModel
    implements IInductanceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public InductanceAttributeModel()
  {
    super(new InductanceAttribute(), Renderer.INDUCTANCE.toString());
  }
  
  public InductanceAttributeModel(IInductanceAttribute attribute)
  {
    super(attribute, Renderer.INDUCTANCE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
