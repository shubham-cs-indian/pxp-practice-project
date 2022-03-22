package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import java.util.List;

public class DeleteInstanceFailedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public DeleteInstanceFailedException()
  {
    super();
  }
  
  public DeleteInstanceFailedException(IExceptionModel exceptionModel)
  {
    super(exceptionModel);
  }
  
  public DeleteInstanceFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
