package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IWeightPerAreaAttribute;
import com.cs.core.config.interactor.entity.attribute.WeightPerAreaAttribute;

public class WeightPerAreaAttributeModel extends AbstractUnitAttributeModel
    implements IWeightPerAreaAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public WeightPerAreaAttributeModel()
  {
    super(new WeightPerAreaAttribute(), Renderer.WEIGHT_PER_AREA.toString());
  }
  
  public WeightPerAreaAttributeModel(IWeightPerAreaAttribute attribute)
  {
    super(attribute, Renderer.WEIGHT_PER_AREA.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
