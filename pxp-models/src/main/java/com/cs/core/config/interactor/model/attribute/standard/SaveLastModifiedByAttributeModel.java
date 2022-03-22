package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.ILastModifiedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedByAttribute;

public class SaveLastModifiedByAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements ILastModifiedByAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveLastModifiedByAttributeModel()
  {
    super(new LastModifiedByAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveLastModifiedByAttributeModel(ILastModifiedByAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
