package com.cs.core.config.interactor.exception.bulksavemappings;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;

import java.util.List;

public class BulkSaveMappingsFailedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public BulkSaveMappingsFailedException()
  {
    super();
  }
  
  public BulkSaveMappingsFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
