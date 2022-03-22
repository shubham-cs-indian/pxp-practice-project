package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IPlaneAngleAttribute;
import com.cs.core.config.interactor.entity.attribute.PlaneAngleAttribute;

public class SavePlaneAngleAttributeModel extends AbstractSaveUnitAttributeModel
    implements IPlaneAngleAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SavePlaneAngleAttributeModel()
  {
    super(new PlaneAngleAttribute(), Renderer.PLANE_ANGLE.toString());
  }
  
  public SavePlaneAngleAttributeModel(IPlaneAngleAttribute attribute)
  {
    super(attribute, Renderer.PLANE_ANGLE.toString());
  }
}
