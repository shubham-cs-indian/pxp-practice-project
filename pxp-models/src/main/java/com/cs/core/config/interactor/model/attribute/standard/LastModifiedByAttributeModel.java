package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.ILastModifiedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedByAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class LastModifiedByAttributeModel extends AbstractMandatoryAttributeModel
    implements ILastModifiedByAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public LastModifiedByAttributeModel()
  {
    super(new LastModifiedByAttribute(), Renderer.TEXT.toString());
  }
  
  public LastModifiedByAttributeModel(ILastModifiedByAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
