package com.cs.core.exception;

import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import java.util.List;

public class SaveInstanceFailedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public SaveInstanceFailedException()
  {
    super();
  }
  
  public SaveInstanceFailedException(IExceptionModel exceptionModel)
  {
    super(exceptionModel);
  }
  
  public SaveInstanceFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
