package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.datarule.ICategoryTreeInformationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetKlassesTreeStrategy
    extends IConfigStrategy<IIdsListParameterModel, ICategoryTreeInformationModel> {
  
}
