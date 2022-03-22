package com.cs.di.runtime.interactor.usecase.dataIntegration;

import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetImportedFileFromServer
    extends IRuntimeInteractor<IdParameterModel, IResponseModelForProcessInstance> {
  
}
