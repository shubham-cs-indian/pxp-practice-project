package com.cs.core.runtime.interactor.model.variants;

public class PropertyVariantRowIdParameterModel extends RowIdParameterModel
    implements IPropertyVariantRowIdParameterModel {
  
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
