package com.cs.core.config.interactor.model.assetstatus;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetUploadStatusResultModel extends IModel {
  
  public static final String STATUS = "status";
  
  public Integer getStatus();
  
  public void setStatus(Integer status);
}
