package com.cs.core.runtime.interactor.model.assetinstance;

import java.io.InputStream;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetInstanceExportResponseModel extends IModel {
  
  public static final String FILE_NAME     = "fileName";
  public static final String ERROR_MESSAGE = "errorMessage";
  public static final String INPUT_STREAM  = "inputStream";
  
  public String getFileName();
  public void setFileName(String fileName);
  
  public InputStream getInputStream();
  public void setInputStream(InputStream inputStream);
  
  public String getErrorMessage();
  public void setErrorMessage(String errorMessage);
}
