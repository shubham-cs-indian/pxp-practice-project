package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.runtime.interactor.exception.configuration.SaveInstanceFailedException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import java.util.List;

public class AssetInstanceSaveFailedException extends SaveInstanceFailedException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetInstanceSaveFailedException()
  {
    super();
  }
  
  public AssetInstanceSaveFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
