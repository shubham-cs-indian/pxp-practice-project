package com.cs.core.config.interactor.model.duplicatecode;

import com.cs.core.config.interactor.entity.datarule.IAttributeConflictingValuesModel;

public class AttributeConflictingValuesModel extends AbstractElementConflictingValuesModel
    implements IAttributeConflictingValuesModel {
  
  private static final long serialVersionUID = 1L;
  protected String          value;
  protected String          valueAsHtml;
  protected String          couplingType;
  
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
  public String getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }
}
