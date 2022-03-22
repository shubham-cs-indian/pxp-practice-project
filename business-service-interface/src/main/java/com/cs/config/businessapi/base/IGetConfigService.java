package com.cs.config.businessapi.base;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigService<P extends IModel, R extends IModel>
    extends IConfigService<P, R> {
  
}