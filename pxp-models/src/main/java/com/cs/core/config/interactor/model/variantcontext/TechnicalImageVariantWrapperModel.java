package com.cs.core.config.interactor.model.variantcontext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TechnicalImageVariantWrapperModel implements ITechnicalImageVariantWrapperModel {
  
  private static final long                                 serialVersionUID = 1L;
  protected ITechnicalImageVariantWithAutoCreateEnableModel variantModel;
  protected String                                          klassInstanceId;
  protected String                                          parentId;
  protected String                                          instanceName;
  protected String                                          baseType;
  
  @Override
  public ITechnicalImageVariantWithAutoCreateEnableModel getVariantModel()
  {
    return variantModel;
  }
  
  @JsonDeserialize(as = TechnicalImageVariantWithAutoCreateEnableModel.class)
  @Override
  public void setVariantModel(ITechnicalImageVariantWithAutoCreateEnableModel variantModel)
  {
    this.variantModel = variantModel;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public String getInstanceName()
  {
    return instanceName;
  }
  
  @Override
  public void setInstanceName(String instanceName)
  {
    this.instanceName = instanceName;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
