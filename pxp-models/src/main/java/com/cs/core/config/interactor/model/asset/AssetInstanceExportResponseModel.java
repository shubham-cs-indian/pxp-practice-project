package com.cs.core.config.interactor.model.asset;

import java.io.InputStream;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceExportResponseModel;

public class AssetInstanceExportResponseModel implements IAssetInstanceExportResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected String          fileName;
  protected String          errorMessage;
  protected InputStream     inputStream;

  @Override
  public String getFileName()
  {
    return fileName;
  }

  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  @Override
  public String getErrorMessage()
  {
    return errorMessage;
  }

  @Override
  public void setErrorMessage(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }
  
  @Override
  public InputStream getInputStream()
  {
    return inputStream;
  }

  @Override
  public void setInputStream(InputStream inputStream)
  {
    this.inputStream = inputStream;
  }
  
  
  
}
