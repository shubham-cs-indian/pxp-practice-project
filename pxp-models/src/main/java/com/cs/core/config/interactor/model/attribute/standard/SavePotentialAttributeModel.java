package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IPotentialAttribute;
import com.cs.core.config.interactor.entity.attribute.PotentialAttribute;

public class SavePotentialAttributeModel extends AbstractSaveUnitAttributeModel
    implements IPotentialAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SavePotentialAttributeModel()
  {
    super(new PotentialAttribute(), Renderer.POTENTIAL.toString());
  }
  
  public SavePotentialAttributeModel(IPotentialAttribute attribute)
  {
    super(attribute, Renderer.POTENTIAL.toString());
  }
}
