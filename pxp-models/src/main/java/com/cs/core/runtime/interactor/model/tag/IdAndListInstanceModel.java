package com.cs.core.runtime.interactor.model.tag;

import java.util.HashSet;
import java.util.Set;

public class IdAndListInstanceModel implements IIdAndListInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected Set<String>     tagInstanceIds;
  protected String          klassinstanceId;
  
  @Override
  public Set<String> getTagInstanceIds()
  {
    return tagInstanceIds;
  }
  
  @Override
  public void setTagInstanceIds(Set<String> tagInstanceIds)
  {
    if (tagInstanceIds == null) {
      tagInstanceIds = new HashSet<String>();
    }
    this.tagInstanceIds = tagInstanceIds;
  }
  
  @Override
  public String getKlassinstanceId()
  {
    return klassinstanceId;
  }
  
  @Override
  public void setKlassinstanceId(String klassinstanceId)
  {
    this.klassinstanceId = klassinstanceId;
  }
}
