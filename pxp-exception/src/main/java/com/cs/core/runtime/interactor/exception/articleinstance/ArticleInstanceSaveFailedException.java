package com.cs.core.runtime.interactor.exception.articleinstance;

import com.cs.core.runtime.interactor.exception.configuration.SaveInstanceFailedException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import java.util.List;

public class ArticleInstanceSaveFailedException extends SaveInstanceFailedException {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleInstanceSaveFailedException()
  {
    super();
  }
  
  public ArticleInstanceSaveFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
