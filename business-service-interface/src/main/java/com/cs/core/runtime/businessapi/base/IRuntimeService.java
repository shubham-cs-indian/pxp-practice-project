package com.cs.core.runtime.businessapi.base;

import com.cs.core.businessapi.base.IService;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRuntimeService<P extends IModel, R extends IModel> extends IService<P, R> {
}
