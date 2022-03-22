package com.cs.core.config.interactor.model.configdetails;

public class ExportResponseModel implements IExportResponseModel {
  
  private String filePath;
  
  @Override
  public String getFilePath()
  {
    return filePath;
  }
  
  @Override
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }
}
