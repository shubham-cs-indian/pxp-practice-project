package com.cs.core.runtime.interactor.usecase.translations;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IGetStaticLabelTranslationsResponseModel;
import com.cs.core.runtime.interactor.model.translations.IGetStaticTranslationsForRuntimeRequestModel;

public interface IGetStaticTranslationsForRuntime extends
    IGetConfigInteractor<IGetStaticTranslationsForRuntimeRequestModel, IGetStaticLabelTranslationsResponseModel> {
}
