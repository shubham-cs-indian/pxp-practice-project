package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.runtime.interactor.exception.configuration.SaveInstanceFailedException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import java.util.List;

public class MarketInstanceSaveFailedException extends SaveInstanceFailedException {
  
  private static final long serialVersionUID = 1L;
  
  public MarketInstanceSaveFailedException()
  {
    super();
  }
  
  public MarketInstanceSaveFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
