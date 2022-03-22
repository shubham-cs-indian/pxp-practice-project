package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.ICreatedOnAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class CreatedOnAttributeModel extends AbstractMandatoryAttributeModel
    implements ICreatedOnAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public CreatedOnAttributeModel()
  {
    super(new CreatedOnAttribute(), Renderer.DATE.toString());
  }
  
  public CreatedOnAttributeModel(ICreatedOnAttribute attribute)
  {
    super(attribute, Renderer.DATE.toString());
  }
}
