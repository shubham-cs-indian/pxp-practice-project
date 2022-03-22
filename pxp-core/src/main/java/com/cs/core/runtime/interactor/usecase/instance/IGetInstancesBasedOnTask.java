package com.cs.core.runtime.interactor.usecase.instance;

import com.cs.core.runtime.interactor.model.instance.IGetInstancesBasedOnTaskModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetInstancesBasedOnTask
    extends IRuntimeInteractor<IGetInstancesBasedOnTaskModel, IGetKlassInstanceTreeModel> {
}
