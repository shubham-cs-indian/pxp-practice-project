package com.cs.core.runtime.interactor.model.version;

import java.util.ArrayList;
import java.util.List;

public class MoveKlassInstanceVersionsModel implements IMoveKlassInstanceVersionsModel {
  
  private static final long serialVersionUID = 1L;
  protected List<Integer>    versionNumbers   = new ArrayList<>();
  protected String          instanceId;
  protected Boolean         isDeleteFromArchival;
  
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
  public List<Integer> getVersionNumbers()
  {
    
    return versionNumbers;
  }
  
  @Override
  public void setVersionNumbers(List<Integer> versionNumbers)
  {
    this.versionNumbers = versionNumbers;
  }
  
  @Override
  public Boolean getIsDeleteFromArchival()
  {
    if (isDeleteFromArchival == null) {
      isDeleteFromArchival = false;
    }
    return isDeleteFromArchival;
  }
  
  @Override
  public void setIsDeleteFromArchival(Boolean isDeleteFromArchival)
  {
    this.isDeleteFromArchival = isDeleteFromArchival;
  }
}
