package com.cs.core.runtime.interactor.model.configuration;

public class InstanceTypeGetRequestModel implements IInstanceTypeGetRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         shouldFetchFromArchive;
  protected String          id;
  protected Long            versionId;
  protected String          baseType;
  
  @Override
  public Boolean getShouldFetchFromArchive()
  {
    if (shouldFetchFromArchive == null) {
      shouldFetchFromArchive = false;
    }
    return shouldFetchFromArchive;
  }
  
  @Override
  public void setShouldFetchFromArchive(Boolean shouldFetchFromArchive)
  {
    this.shouldFetchFromArchive = shouldFetchFromArchive;
  }
  
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
