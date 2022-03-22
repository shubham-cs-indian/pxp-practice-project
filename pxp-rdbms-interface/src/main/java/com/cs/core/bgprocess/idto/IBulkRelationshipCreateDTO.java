package com.cs.core.bgprocess.idto;

import java.util.List;

public interface IBulkRelationshipCreateDTO extends IInitializeBGProcessDTO {
  
  
  public List<Long> getSuccessInstanceIIds();
  public void setSuccessInstanceIIds(List<Long> successInstanceIIds);
  
  public String getSide1InstanceId();
  public void setSide1InstanceId(String side1InstanceId);
  
  public String getSide1BaseType();
  public void setSide1BaseType(String baseType);
  
  public String getTabType();
  public void setTabType(String tabType);
  
  public String getTabId();
  public void setTabId(String tabId);
  
  String getRelationshipId();
  void setRelationshipId(String relationshipId);
  
  String getRelationshipEntityId();
  void setRelationshipEntityId(String relationshipEntityId);
 
  String getSideId();
  void setSideId(String sideId);
  
}
