package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IPlaneAngleAttribute;
import com.cs.core.config.interactor.entity.attribute.PlaneAngleAttribute;

public class PlaneAngleAttributeModel extends AbstractUnitAttributeModel
    implements IPlaneAngleAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public PlaneAngleAttributeModel()
  {
    super(new PlaneAngleAttribute(), Renderer.PLANE_ANGLE.toString());
  }
  
  public PlaneAngleAttributeModel(IPlaneAngleAttribute attribute)
  {
    super(attribute, Renderer.PLANE_ANGLE.toString());
  }
}
