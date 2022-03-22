package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ITimeAttribute;
import com.cs.core.config.interactor.entity.attribute.TimeAttribute;

public class TimeAttributeModel extends AbstractUnitAttributeModel implements ITimeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public TimeAttributeModel()
  {
    super(new TimeAttribute(), Renderer.TIME.toString());
  }
  
  public TimeAttributeModel(ITimeAttribute attribute)
  {
    super(attribute, Renderer.TIME.toString());
  }
}
