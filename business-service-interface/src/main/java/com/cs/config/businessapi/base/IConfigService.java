package com.cs.config.businessapi.base;

import com.cs.core.businessapi.base.IService;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigService<P extends IModel, R extends IModel> extends IService<P, R> {
  
}
