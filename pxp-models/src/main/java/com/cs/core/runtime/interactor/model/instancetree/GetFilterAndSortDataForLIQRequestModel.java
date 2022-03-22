package com.cs.core.runtime.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.List;

public class GetFilterAndSortDataForLIQRequestModel extends GetNewFilterAndSortDataRequestModel implements IGetFilterAndSortDataForLIQRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          instanceId;
  protected String          baseType;
  protected List<String>    idsToInclude;
  
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
  public List<String> getIdsToInclude()
  {
    if(idsToInclude == null) {
      idsToInclude = new ArrayList<>();
    }
    return idsToInclude;
  }

  @Override
  public void setIdsToInclude(List<String> idsToInclude)
  {
    this.idsToInclude = idsToInclude;
  }
}
