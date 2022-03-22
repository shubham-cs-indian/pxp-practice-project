package com.cs.core.config.strategy.usecase.condition;

import com.cs.core.config.interactor.model.condition.IConditionInformationModel;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllConditionsStrategy
    extends IConfigStrategy<IConditionModel, IListModel<IConditionInformationModel>> {
  
}
