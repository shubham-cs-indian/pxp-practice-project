package com.cs.core.runtime.interactor.model.configuration;

public interface IInstanceTypeGetRequestModel extends IModel {
  
  String ID                        = "id";
  String VERSION_ID                = "versionId";
  String SHOULD_FETCH_FROM_ARCHIVE = "shouldFetchFromArchive";
  String BASE_TYPE                 = "baseType";
  
  public Boolean getShouldFetchFromArchive();
  
  public void setShouldFetchFromArchive(Boolean shouldFetchFromArchive);
  
  public String getId();
  
  public void setId(String id);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
