package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.runtime.interactor.exception.configuration.SaveInstanceFailedException;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import java.util.List;

public class SupplierInstanceSaveFailedException extends SaveInstanceFailedException {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierInstanceSaveFailedException()
  {
    super();
  }
  
  public SupplierInstanceSaveFailedException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    super(exceptionDetails, devExceptionDetails);
  }
}
