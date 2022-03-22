package com.cs.core.config.interactor.entity.datarule;

public class AttributeOperator implements IAttributeOperator {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected String          attributeId;
  protected String          value;
  protected String          operator;
  protected String          type;
  protected Integer         order;
  protected String          valueAsHtml;
  protected String          code;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  public String getOperator()
  {
    return operator;
  }
  
  @Override
  public void setOperator(String operator)
  {
    this.operator = operator;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Integer getOrder()
  {
    return order;
  }
  
  @Override
  public void setOrder(Integer order)
  {
    this.order = order;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
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
}
