package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetFileNameModel extends IModel {
  
  public static final String FILE_NAME = "fileName";
  
  public String getFileName();
  
  public void setFileName(String fileName);
}
