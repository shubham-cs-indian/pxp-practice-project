package com.cs.core.runtime.interactor.model.configuration;

public interface IProcessInstanceFileModel extends IModel {
  
  public static String       ID                  = "id";
  public static final String DOWNLOAD_INDIVIDUAL = "downloadIndividual";
  public static String       FILE_PATH           = "filePath";
  
  public String getId();
  
  public void setId(String id);
  
  public String getfilePath();
  
  public void setfilePath(String filePath);
  
  public void setdownloadIndividual(Boolean downloadIndividual);
  
  public boolean getIsDownloadIndividual();
  
}
