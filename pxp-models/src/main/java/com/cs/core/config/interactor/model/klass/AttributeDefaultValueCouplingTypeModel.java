package com.cs.core.config.interactor.model.klass;

public class AttributeDefaultValueCouplingTypeModel extends DefaultValueChangeModel implements IAttributeDefaultValueCouplingTypeModel {
  
  private static final long serialVersionUID = 1L;
  protected String          value            = "";
  protected String          valueAsHtml      = "";
  protected Boolean         isDependent      = false;
  protected double          valueAsNumber    = 0;
  protected String          unitSymbol       = "";
  
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
  public Boolean getIsDependent()
  {
    return isDependent;
  }
  
  @Override
  public void setIsDependent(Boolean isDependent)
  {
    this.isDependent = isDependent;
  }
  
  @Override
  public double getValueAsNumber()
  {
    return valueAsNumber;
  }
  
  @Override
  public void setValueAsNumber(double valueAsNumber)
  {
    this.valueAsNumber = valueAsNumber;
  }
  
  @Override
  public String getUnitSymbol()
  {
    return unitSymbol;
  }
  
  @Override
  public void setUnitSymbol(String unitSymbol)
  {
    this.unitSymbol = unitSymbol;
  }
}
