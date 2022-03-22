package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ITimeAttribute;
import com.cs.core.config.interactor.entity.attribute.TimeAttribute;

public class SaveTimeAttributeModel extends AbstractSaveUnitAttributeModel
    implements ITimeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveTimeAttributeModel()
  {
    super(new TimeAttribute(), Renderer.TIME.toString());
  }
  
  public SaveTimeAttributeModel(ITimeAttribute attribute)
  {
    super(attribute, Renderer.TIME.toString());
  }
}
