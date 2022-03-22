package com.cs.core.runtime.interactor.exception.assetserver;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public class AssetFileTypeNotSupportedRollbackException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetFileTypeNotSupportedRollbackException(IExceptionModel failure)
  {
    super(failure);
  }
  
  public AssetFileTypeNotSupportedRollbackException()
  {
    super();
  }
}
