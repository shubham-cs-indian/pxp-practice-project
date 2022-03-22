package com.cs.core.runtime.interactor.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IUpdateSearchableDocumentForInstance
    extends IRuntimeInteractor<IUpdateSearchableInstanceRequestModel, IModel> {
}
