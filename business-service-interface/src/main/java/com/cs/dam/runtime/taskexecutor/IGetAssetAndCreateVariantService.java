package com.cs.dam.runtime.taskexecutor;

import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableWrapperModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAssetAndCreateVariantService extends
    IRuntimeService<ITechnicalImageVariantWithAutoCreateEnableWrapperModel, IIdParameterModel> {
}
