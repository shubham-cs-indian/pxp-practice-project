package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWrapperModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IAutoCreateVariantInstanceTaskService
    extends IRuntimeService<ITechnicalImageVariantWrapperModel, IIdParameterModel> {
}
