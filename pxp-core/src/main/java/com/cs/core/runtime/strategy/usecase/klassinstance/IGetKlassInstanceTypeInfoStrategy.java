package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdAndBaseTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeInfoModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetKlassInstanceTypeInfoStrategy
    extends IRuntimeStrategy<IIdAndBaseTypeModel, IKlassInstanceTypeInfoModel> {
  
}
