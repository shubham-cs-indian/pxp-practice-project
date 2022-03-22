package com.cs.core.config.interactor.usecase.systemstatictranslation;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.hidden.IHiddenResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IExportSystemStaticTranslation
    extends IGetConfigInteractor<IModel, IHiddenResponseModel> {
  
}
