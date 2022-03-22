package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public interface IWarningsModel extends IModel {
  
  public static final String WARNINGS = "warnings";
  
  public IExceptionModel getWarnings();
  
  public void setWarnings(IExceptionModel warnings);
}
