package com.cs.core.runtime.interactor.usecase.exporttask;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IComponent<P extends IModel, R extends IModel> {
  
  public R execute(P model) throws Exception;
}
