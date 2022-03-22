package com.cs.core.runtime.interactor.model.variants;

import java.util.ArrayList;
import java.util.List;

public class GetVariantInfoForDataTransferRequestModel
    implements IGetVariantInfoForDataTransferRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          klassInstanceId;
  protected String          parentVariantInstanceId;
  protected String          baseType;
  protected List<String>    klassIds;
  
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
  public String getParentVariantInstanceId()
  {
    return parentVariantInstanceId;
  }
  
  @Override
  public void setParentVariantInstanceId(String parentVariantInstanceId)
  {
    this.parentVariantInstanceId = parentVariantInstanceId;
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
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
}
