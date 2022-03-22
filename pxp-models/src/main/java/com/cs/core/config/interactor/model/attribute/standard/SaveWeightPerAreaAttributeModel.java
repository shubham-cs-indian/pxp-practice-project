package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IWeightPerAreaAttribute;
import com.cs.core.config.interactor.entity.attribute.WeightPerAreaAttribute;

public class SaveWeightPerAreaAttributeModel extends AbstractSaveUnitAttributeModel
    implements IWeightPerAreaAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveWeightPerAreaAttributeModel()
  {
    super(new WeightPerAreaAttribute(), Renderer.WEIGHT_PER_AREA.toString());
  }
  
  public SaveWeightPerAreaAttributeModel(IWeightPerAreaAttribute attribute)
  {
    super(attribute, Renderer.WEIGHT_PER_AREA.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
