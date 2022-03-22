package com.cs.dam.config.interactor.model.downloadtracker;


public class DownloadTrackerConfigurationResponseModel
    implements IDownloadTrackerConfigurationResponseModel {
  
  private static final long serialVersionUID         = 1L;
  protected String          klassId;
  protected String          klassCode;
  protected Boolean         isDownloadTrackerEnabled = false;

  @Override
  public String getKlassId()
  {
    return klassId;
  }

  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public String getKlassCode()
  {
    return klassCode;
  }

  @Override
  public void setKlassCode(String klassCode)
  {
    this.klassCode = klassCode;
  }
  
  @Override
  public Boolean getIsDownloadTrackerEnabled()
  {
    return isDownloadTrackerEnabled;
  }

  @Override
  public void setIsDownloadTrackerEnabled(Boolean isDownloadTrackerEnabled)
  {
    this.isDownloadTrackerEnabled = isDownloadTrackerEnabled;
  }

}
