package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.runtime.interactor.exception.configuration.DeleteInstanceFailedException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import java.util.List;

public class TextAssetInstanceDeleteFailedException extends DeleteInstanceFailedException {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetInstanceDeleteFailedException()
  {
    super();
  }
  
  public TextAssetInstanceDeleteFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
