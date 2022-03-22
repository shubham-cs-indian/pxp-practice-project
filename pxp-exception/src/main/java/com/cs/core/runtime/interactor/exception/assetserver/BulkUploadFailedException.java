package com.cs.core.runtime.interactor.exception.assetserver;

import java.util.List;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;

public class BulkUploadFailedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public BulkUploadFailedException()
  {
    super();
  }
  
  public BulkUploadFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
