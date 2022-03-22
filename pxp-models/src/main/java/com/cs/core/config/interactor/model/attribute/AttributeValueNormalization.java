package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.entity.datarule.IAttributeValueNormalization;
import com.cs.core.config.interactor.model.datarule.Normalization;

public class AttributeValueNormalization extends Normalization
    implements IAttributeValueNormalization {
  
  private static final long serialVersionUID = 1L;
  
  protected String          valueAttributeId;
  
  @Override
  public String getValueAttributeId()
  {
    return valueAttributeId;
  }
  
  @Override
  public void setValueAttributeId(String valueAttributeId)
  {
    this.valueAttributeId = valueAttributeId;
  }
}
