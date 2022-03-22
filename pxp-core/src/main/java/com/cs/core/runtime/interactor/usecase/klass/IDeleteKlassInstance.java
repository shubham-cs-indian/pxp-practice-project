package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteKlassInstance extends
    IRuntimeInteractor<IDeleteKlassInstanceRequestModel, IDeleteKlassInstanceResponseModel> {
}
