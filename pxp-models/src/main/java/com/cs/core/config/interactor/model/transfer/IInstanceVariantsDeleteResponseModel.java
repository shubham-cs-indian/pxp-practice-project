package com.cs.core.config.interactor.model.transfer;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IInstanceVariantsDeleteResponseModel extends IModel {
  
  public static final String INSTANCE_ID           = "instanceId";
  public static final String BASETYPE              = "baseType";
  public static final String VARIANT_IDS_TO_DELETE = "variantIdsToDelete";
  
  public String getInstanceId();
  
  public void setInstanceId(String instanceId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getVariantIdsToDelete();
  
  public void setVariantIdsToDelete(List<String> variantIdsToDelete);
}
