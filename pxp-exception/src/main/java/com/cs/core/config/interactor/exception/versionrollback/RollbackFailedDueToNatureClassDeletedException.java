package com.cs.core.config.interactor.exception.versionrollback;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;

import java.util.List;

public class RollbackFailedDueToNatureClassDeletedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public RollbackFailedDueToNatureClassDeletedException()
  {
    super();
  }
  
  public RollbackFailedDueToNatureClassDeletedException(
      List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
