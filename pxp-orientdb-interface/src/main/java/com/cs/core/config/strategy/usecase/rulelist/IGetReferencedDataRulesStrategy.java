package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetReferencedDataRulesStrategy
    extends IRuntimeStrategy<IIdParameterModel, IListModel<IGetKlassModel>> {
  
}
