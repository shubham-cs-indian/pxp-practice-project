package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IProportionAttribute;
import com.cs.core.config.interactor.entity.attribute.ProportionAttribute;

public class SaveProportionAttributeModel extends AbstractSaveUnitAttributeModel
    implements IProportionAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveProportionAttributeModel()
  {
    super(new ProportionAttribute(), Renderer.PROPORTION.toString());
  }
  
  public SaveProportionAttributeModel(IProportionAttribute attribute)
  {
    super(attribute, Renderer.PROPORTION.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
