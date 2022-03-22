package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public class WarningsModel implements IWarningsModel {
  
  private static final long serialVersionUID = 1L;
  protected IExceptionModel warnings         = new ExceptionModel();
  
  @Override
  public IExceptionModel getWarnings()
  {
    return warnings;
  }
  
  @Override
  public void setWarnings(IExceptionModel warnings)
  {
    this.warnings = warnings;
  }
}
