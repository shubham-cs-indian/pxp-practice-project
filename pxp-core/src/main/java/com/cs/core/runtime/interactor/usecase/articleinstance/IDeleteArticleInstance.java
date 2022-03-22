package com.cs.core.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteArticleInstance
    extends IRuntimeInteractor<IIdsListParameterModel, IDeleteKlassInstanceResponseModel> {
}
