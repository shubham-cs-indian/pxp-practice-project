package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.CreatedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.ICreatedByAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class CreatedByAttributeModel extends AbstractMandatoryAttributeModel
    implements ICreatedByAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public CreatedByAttributeModel()
  {
    super(new CreatedByAttribute(), Renderer.TEXT.toString());
  }
  
  public CreatedByAttributeModel(ICreatedByAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
