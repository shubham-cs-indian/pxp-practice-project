package com.cs.core.config.interactor.model.export;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetReferenceToPimObjectsRequestModel extends IIdParameterModel, IModel {
  
  public static String OBJECT_TYPE_ATTRIBUTE_ID = "objectTypeAttributeId";
  public static String SKU_ATTRIBUTE_ID         = "skuAttributeId";
  
  public String getObjectTypeAttributeId();
  
  public void setObjectTypeAttributeId(String objectTypeAttributeId);
  
  public String getSkuAttributeId();
  
  public void setSkuAttributeId(String skuAttributeId);
}
