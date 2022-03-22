package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.CreatedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.ICreatedByAttribute;

public class SaveCreatedByAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements ICreatedByAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveCreatedByAttributeModel()
  {
    super(new CreatedByAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveCreatedByAttributeModel(ICreatedByAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
