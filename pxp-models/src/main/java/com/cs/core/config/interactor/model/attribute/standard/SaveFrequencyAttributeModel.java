package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.FrequencyAttribute;
import com.cs.core.config.interactor.entity.attribute.IFrequencyAttribute;

public class SaveFrequencyAttributeModel extends AbstractSaveUnitAttributeModel
    implements IFrequencyAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveFrequencyAttributeModel()
  {
    super(new FrequencyAttribute(), Renderer.FREQUENCY.toString());
  }
  
  public SaveFrequencyAttributeModel(IFrequencyAttribute attribute)
  {
    super(attribute, Renderer.FREQUENCY.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
