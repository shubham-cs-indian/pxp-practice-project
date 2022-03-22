package com.cs.core.runtime.interactor.usecase.cammunda;

import com.cs.core.config.interactor.model.articleimportcomponent.IKlassInstanceExportAllRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetInstanceIdsForExport
    extends IRuntimeInteractor<IKlassInstanceExportAllRequestModel, IIdsListParameterModel> {
}
