package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.INameAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.NameAttribute;

public class SaveNameAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements INameAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveNameAttributeModel()
  {
    super(new NameAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveNameAttributeModel(INameAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
