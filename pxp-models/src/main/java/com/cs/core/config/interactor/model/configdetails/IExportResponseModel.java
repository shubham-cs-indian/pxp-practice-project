package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IExportResponseModel extends IModel {
  
  public static final String exportedFilePath = "filePath";
  
  public String getFilePath();
  
  public void setFilePath(String filePath);
}
