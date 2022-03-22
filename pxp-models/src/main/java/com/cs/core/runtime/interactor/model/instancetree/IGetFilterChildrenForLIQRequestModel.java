package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

public interface IGetFilterChildrenForLIQRequestModel extends IGetFilterChildrenRequestModel {
  
  public static final String INSTANCE_ID    = "instanceId";
  public static final String BASE_TYPE      = "baseType";
  public static final String IDS_TO_INCLUDE = "idsToInclude";
  
  public String getInstanceId();
  public void setInstanceId(String instanceId);
  
  public String getBaseType();
  public void setBaseType(String baseType);
  
  public List<String> getIdsToInclude();
  public void setIdsToInclude(List<String> idsToInclude);
}
