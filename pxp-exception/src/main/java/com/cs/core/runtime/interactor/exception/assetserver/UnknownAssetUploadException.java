package com.cs.core.runtime.interactor.exception.assetserver;

import java.util.List;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public class UnknownAssetUploadException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public UnknownAssetUploadException()
  {
    super();
  }
  
  public UnknownAssetUploadException(String message)
  {
    super(message);
  }
  
  public UnknownAssetUploadException(IExceptionModel exceptionModel)
  {
    super(exceptionModel);
  }
  
  public UnknownAssetUploadException(Exception e)
  {
    super(e);
  }
  
  public UnknownAssetUploadException(Throwable e)
  {
    super(e);
  }
  
  public UnknownAssetUploadException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
