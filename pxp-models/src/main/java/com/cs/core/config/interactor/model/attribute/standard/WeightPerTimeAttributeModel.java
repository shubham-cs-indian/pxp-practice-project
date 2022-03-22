package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IWeightPerTimeAttribute;
import com.cs.core.config.interactor.entity.attribute.WeightPerTimeAttribute;

public class WeightPerTimeAttributeModel extends AbstractUnitAttributeModel
    implements IWeightPerTimeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public WeightPerTimeAttributeModel()
  {
    super(new WeightPerTimeAttribute(), Renderer.WEIGHT_PER_TIME.toString());
  }
  
  public WeightPerTimeAttributeModel(IWeightPerTimeAttribute attribute)
  {
    super(attribute, Renderer.WEIGHT_PER_TIME.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
