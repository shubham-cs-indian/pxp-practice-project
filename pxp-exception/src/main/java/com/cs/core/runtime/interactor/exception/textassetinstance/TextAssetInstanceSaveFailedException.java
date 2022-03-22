package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.runtime.interactor.exception.configuration.SaveInstanceFailedException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import java.util.List;

public class TextAssetInstanceSaveFailedException extends SaveInstanceFailedException {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetInstanceSaveFailedException()
  {
    super();
  }
  
  public TextAssetInstanceSaveFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
