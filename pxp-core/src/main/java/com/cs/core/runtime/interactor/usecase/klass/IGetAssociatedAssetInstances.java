package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetAssociatedAssetInstancesModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAssociatedAssetInstances
    extends IRuntimeInteractor<IIdParameterModel, IGetAssociatedAssetInstancesModel> {
}
