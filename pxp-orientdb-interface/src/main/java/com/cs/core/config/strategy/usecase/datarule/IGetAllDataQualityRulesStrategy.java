package com.cs.core.config.strategy.usecase.datarule;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAllDataQualityRulesStrategy
    extends IConfigStrategy<IIdsListParameterModel, IListModel<IDataRuleModel>> {
  
}
