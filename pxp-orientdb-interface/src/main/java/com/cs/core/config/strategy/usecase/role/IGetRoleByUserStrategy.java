package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetRoleByUserStrategy extends IConfigStrategy<IIdParameterModel,IIdsListParameterModel> {
  
}
