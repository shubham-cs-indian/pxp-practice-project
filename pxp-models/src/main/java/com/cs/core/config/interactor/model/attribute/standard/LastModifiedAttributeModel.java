package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.ILastModifiedAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class LastModifiedAttributeModel extends AbstractMandatoryAttributeModel
    implements ILastModifiedAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public LastModifiedAttributeModel()
  {
    super(new LastModifiedAttribute(), Renderer.DATE.toString());
  }
  
  public LastModifiedAttributeModel(ILastModifiedAttribute attribute)
  {
    super(attribute, Renderer.DATE.toString());
  }
}
