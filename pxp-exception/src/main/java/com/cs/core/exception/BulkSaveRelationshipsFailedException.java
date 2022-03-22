package com.cs.core.exception;

import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import java.util.List;

public class BulkSaveRelationshipsFailedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public BulkSaveRelationshipsFailedException()
  {
    super();
  }
  
  public BulkSaveRelationshipsFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
