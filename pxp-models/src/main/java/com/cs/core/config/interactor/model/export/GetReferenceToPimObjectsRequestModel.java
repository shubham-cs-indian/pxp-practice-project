package com.cs.core.config.interactor.model.export;

import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;

public class GetReferenceToPimObjectsRequestModel extends IdParameterModel
    implements IGetReferenceToPimObjectsRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          objectTypeAttributeId;
  protected String          skuAttributeId;
  
  @Override
  public String getObjectTypeAttributeId()
  {
    return objectTypeAttributeId;
  }
  
  @Override
  public void setObjectTypeAttributeId(String objectTypeAttributeId)
  {
    this.objectTypeAttributeId = objectTypeAttributeId;
  }
  
  @Override
  public String getSkuAttributeId()
  {
    return skuAttributeId;
  }
  
  @Override
  public void setSkuAttributeId(String skuAttributeId)
  {
    this.skuAttributeId = skuAttributeId;
  }
}
