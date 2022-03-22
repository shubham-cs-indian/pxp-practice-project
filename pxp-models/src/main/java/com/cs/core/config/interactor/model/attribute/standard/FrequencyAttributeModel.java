package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.FrequencyAttribute;
import com.cs.core.config.interactor.entity.attribute.IFrequencyAttribute;

public class FrequencyAttributeModel extends AbstractUnitAttributeModel
    implements IFrequencyAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public FrequencyAttributeModel()
  {
    super(new FrequencyAttribute(), Renderer.FREQUENCY.toString());
  }
  
  public FrequencyAttributeModel(IFrequencyAttribute attribute)
  {
    super(attribute, Renderer.FREQUENCY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
