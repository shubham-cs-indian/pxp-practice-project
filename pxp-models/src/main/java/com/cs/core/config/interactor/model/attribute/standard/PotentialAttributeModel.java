package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IPotentialAttribute;
import com.cs.core.config.interactor.entity.attribute.PotentialAttribute;

public class PotentialAttributeModel extends AbstractUnitAttributeModel
    implements IPotentialAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public PotentialAttributeModel()
  {
    super(new PotentialAttribute(), Renderer.POTENTIAL.toString());
  }
  
  public PotentialAttributeModel(IPotentialAttribute attribute)
  {
    super(attribute, Renderer.POTENTIAL.toString());
  }
}
