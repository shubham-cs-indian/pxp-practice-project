package com.cs.core.runtime.interactor.model.variants.detail;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IVariantDetailBaseRequestModel extends IModel {
  
  public static final String INSTANCE_ID = "instanceId";
  public static final String VARIANT_IDS = "variantIds";
  
  public String getInstanceId();
  
  public void setInstanceId(String id);
  
  public List<String> getVariantIds();
  
  public void setVariantIds(List<String> variants);
}
