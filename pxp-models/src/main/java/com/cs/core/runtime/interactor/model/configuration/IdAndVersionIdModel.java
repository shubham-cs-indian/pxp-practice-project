package com.cs.core.runtime.interactor.model.configuration;

public class IdAndVersionIdModel implements IIdAndVersionIdModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Long            versionId;
  protected Boolean         shouldFetchFromArchive;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
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
  
  @Override
  public Boolean getShouldFetchFromArchive()
  {
    return shouldFetchFromArchive;
  }
  
  @Override
  public void setShouldFetchFromArchive(Boolean shouldFetchFromArchive)
  {
    this.shouldFetchFromArchive = shouldFetchFromArchive;
  }
}
