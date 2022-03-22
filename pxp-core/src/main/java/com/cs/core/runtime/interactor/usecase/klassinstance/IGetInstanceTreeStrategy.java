package com.cs.core.runtime.interactor.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetInstanceTreeStrategy
    extends IRuntimeStrategy<IGetKlassInstanceTreeStrategyModel, IGetKlassInstanceTreeModel> {
}
