package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.ILastModifiedAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute;

public class SaveLastModifiedAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements ILastModifiedAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveLastModifiedAttributeModel()
  {
    super(new LastModifiedAttribute(), Renderer.DATE.toString());
  }
  
  public SaveLastModifiedAttributeModel(ILastModifiedAttribute attribute)
  {
    super(attribute, Renderer.DATE.toString());
  }
}
