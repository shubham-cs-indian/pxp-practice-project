package com.cs.di.base;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.IInteractor;

public interface IDiInteractor<P extends IModel, R extends IModel> extends IInteractor<P, R> {
}
