package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.runtime.interactor.exception.configuration.DeleteInstanceFailedException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import java.util.List;

public class AssetInstanceDeleteFailedException extends DeleteInstanceFailedException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetInstanceDeleteFailedException()
  {
    super();
  }
  
  public AssetInstanceDeleteFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
