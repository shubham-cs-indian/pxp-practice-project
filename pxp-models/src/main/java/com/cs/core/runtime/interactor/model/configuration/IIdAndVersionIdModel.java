package com.cs.core.runtime.interactor.model.configuration;

public interface IIdAndVersionIdModel extends IModel {
  
  public static String ID                        = "id";
  public static String VERSION_ID                = "versionId";
  public static String SHOULD_FETCH_FROM_ARCHIVE = "shouldFetchFromArchive";
  
  public String getId();
  
  public void setId(String id);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
  
  public Boolean getShouldFetchFromArchive();
  
  public void setShouldFetchFromArchive(Boolean shouldFetchFromArchive);
}
