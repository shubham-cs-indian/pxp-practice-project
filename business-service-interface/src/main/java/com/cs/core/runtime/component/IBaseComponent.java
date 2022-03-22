package com.cs.core.runtime.component;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IBaseComponent<P extends IModel, R extends IModel> {
  
  public R execute(P dataModel) throws Exception;
}
