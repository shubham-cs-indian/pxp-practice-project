package com.cs.core.runtime.interactor.exception.taskinstance;

import com.cs.core.runtime.interactor.exception.configuration.DeleteInstanceFailedException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import java.util.List;

public class TaskInstanceDeleteFailedException extends DeleteInstanceFailedException {
  
  private static final long serialVersionUID = 1L;
  
  public TaskInstanceDeleteFailedException()
  {
    super();
  }
  
  public TaskInstanceDeleteFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
