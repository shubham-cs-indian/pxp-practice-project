package com.cs.core.runtime.interactor.usecase.instance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetInstanceTree
    extends IRuntimeInteractor<IGetKlassInstanceTreeStrategyModel, IGetKlassInstanceTreeModel> {
}
