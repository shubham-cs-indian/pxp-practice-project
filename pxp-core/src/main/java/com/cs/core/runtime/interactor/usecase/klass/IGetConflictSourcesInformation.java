package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.runtime.interactor.model.klassinstance.IConflictSourcesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetConflictSourcesInformationModel;

public interface IGetConflictSourcesInformation
    extends IGetConfigInteractor<IConflictSourcesRequestModel, IGetConflictSourcesInformationModel> {
}
