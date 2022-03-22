package com.cs.core.runtime.strategy.usecase.translations;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IGetStaticLabelTranslationsResponseModel;
import com.cs.core.runtime.interactor.model.translations.IGetStaticTranslationsForRuntimeRequestModel;

public interface IGetStaticTranslationsForRuntimeStrategy extends
    IConfigStrategy<IGetStaticTranslationsForRuntimeRequestModel, IGetStaticLabelTranslationsResponseModel> {
  
}
