package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.INameAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.NameAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class NameAttributeModel extends AbstractMandatoryAttributeModel
    implements INameAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public NameAttributeModel()
  {
    super(new NameAttribute(), Renderer.TEXT.toString());
  }
  
  public NameAttributeModel(INameAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
