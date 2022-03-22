package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public class IdsListWithVersionIdModel implements IIdsListWithVersionIdModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    ids;
  protected Long            versionId;
  
  @Override
  public List<String> getIds()
  {
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
}
