package com.cs.core.runtime.interactor.model.context;

import com.cs.core.config.interactor.model.context.IContextualPropertiesToInheritAttributeModel;

public class ContextualPropertiesToInheritAttributeModel
    extends AbstractContextualPropertiesToInheritPropertyModel
    implements IContextualPropertiesToInheritAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          attributeId;
  protected String          value;
  protected Double          valueAsNumber;
  protected String          valueAsHtml;
  
  @Override
  public String getValue()
  {
    return value;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
  }
  
  @Override
  public Double getValueAsNumber()
  {
    return valueAsNumber;
  }
  
  @Override
  public void setValueAsNumber(Double valueAsNumber)
  {
    this.valueAsNumber = valueAsNumber;
  }
  
  @Override
  public String getValueAsHtml()
  {
    return valueAsHtml;
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    this.valueAsHtml = valueAsHtml;
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
}
