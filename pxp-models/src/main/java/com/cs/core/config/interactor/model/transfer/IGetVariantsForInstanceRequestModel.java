package com.cs.core.config.interactor.model.transfer;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetVariantsForInstanceRequestModel extends IModel {
  
  public static final String ID                  = "id";
  public static final String PARENT_ID           = "parentId";
  public static final String KLASS_INSTANCE_ID   = "klassInstanceId";
  public static final String VARIANT_INSTANCE_ID = "variantInstanceId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String klassInstanceId);
}
