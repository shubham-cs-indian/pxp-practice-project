package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantRequestNewStrategyModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateInstanceVariantTask extends
    IRuntimeInteractor<ICreateVariantRequestNewStrategyModel, ISaveStrategyInstanceResponseModel> {
}
