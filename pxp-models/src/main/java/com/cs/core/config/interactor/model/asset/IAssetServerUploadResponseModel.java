package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetServerUploadResponseModel extends IModel {
  
  public static final String RESPONSE_CODE = "responseCode";
  
  public Integer getResponseCode();
  
  public void setResponseCode(Integer responseCode);
}
