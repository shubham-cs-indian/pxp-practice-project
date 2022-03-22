package com.cs.core.config.interactor.entity.attribute;

public class ConcatenatedAttributeOperator extends AbstractConcatenatedOperator
    implements IConcatenatedAttributeOperator {
  
  private static final long serialVersionUID = 1L;
  
  protected String          attributeId;
  
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
