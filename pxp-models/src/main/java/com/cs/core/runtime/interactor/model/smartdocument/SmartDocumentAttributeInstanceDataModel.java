package com.cs.core.runtime.interactor.model.smartdocument;

public class SmartDocumentAttributeInstanceDataModel
    implements ISmartDocumentAttributeInstanceDataModel {
  
  private static final long serialVersionUID = 1L;
  protected String          attributeId;
  protected String          value;
  protected String          valueAsHtml;
  protected Double          valueAsNumber;
  protected String          attributeLabel;
  protected String          attributeType;
  protected String          defaultUnit;
  
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
  public String getAttributeLabel()
  {
    return attributeLabel;
  }
  
  @Override
  public void setAttributeLabel(String attributeLabel)
  {
    this.attributeLabel = attributeLabel;
  }
  
  @Override
  public String getAttributeType()
  {
    return attributeType;
  }
  
  @Override
  public void setAttributeType(String attributeType)
  {
    this.attributeType = attributeType;
  }
  
  @Override
  public String getDefaultUnit()
  {
    return defaultUnit;
  }

  @Override
  public void setDefaultUnit(String defaultUnit)
  {
    this.defaultUnit = defaultUnit;
  }
}
