package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITechnicalImageVariantWrapperModel extends IModel {
  
  public static final String VARIANT_MODEL     = "variantModel";
  public static final String KLASS_INSTANCE_ID = "klassInstanceId";
  public static final String PARENT_ID         = "parentId";
  public static final String INSTANCE_NAME     = "instanceName";
  public static final String BASE_TYPE         = "baseType";
  
  public ITechnicalImageVariantWithAutoCreateEnableModel getVariantModel();
  
  public void setVariantModel(ITechnicalImageVariantWithAutoCreateEnableModel variantModel);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getInstanceName();
  
  public void setInstanceName(String instanceName);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
