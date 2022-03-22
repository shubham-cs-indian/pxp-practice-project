package com.cs.core.config.interactor.model.component;

import com.cs.core.config.interactor.model.transfer.IInstanceVariantsDeleteResponseModel;

import java.util.ArrayList;
import java.util.List;

public class InstanceVariantsDeleteResponseModel implements IInstanceVariantsDeleteResponseModel {
  
  private static final long serialVersionUID   = 1L;
  protected String          instanceId;
  protected String          baseType;
  protected List<String>    variantIdsToDelete = new ArrayList<>();
  
  @Override
  public String getInstanceId()
  {
    return instanceId;
  }
  
  @Override
  public void setInstanceId(String instanceId)
  {
    this.instanceId = instanceId;
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
  
  @Override
  public List<String> getVariantIdsToDelete()
  {
    return variantIdsToDelete;
  }
  
  @Override
  public void setVariantIdsToDelete(List<String> variantIdsToDelete)
  {
    this.variantIdsToDelete = variantIdsToDelete;
  }
}
