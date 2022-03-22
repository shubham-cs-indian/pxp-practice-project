package com.cs.core.config.interactor.exception.task;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;

import java.util.List;

public class BulkSaveTaskFailedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public BulkSaveTaskFailedException()
  {
    super();
  }
  
  public BulkSaveTaskFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
