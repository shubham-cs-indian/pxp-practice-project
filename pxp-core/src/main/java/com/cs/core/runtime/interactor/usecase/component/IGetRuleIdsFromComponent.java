package com.cs.core.runtime.interactor.usecase.component;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetRuleIdsFromComponent
    extends IConfigStrategy<IIdParameterModel, IIdsListParameterModel> {
}
