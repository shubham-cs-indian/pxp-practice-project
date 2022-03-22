package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.ICreatedOnAttribute;

public class SaveCreatedOnAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements ICreatedOnAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveCreatedOnAttributeModel()
  {
    super(new CreatedOnAttribute(), Renderer.DATE.toString());
  }
  
  public SaveCreatedOnAttributeModel(ICreatedOnAttribute attribute)
  {
    super(attribute, Renderer.DATE.toString());
  }
}
