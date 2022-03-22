package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.interactor.model.rulelist.IRuleListStrategySaveModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveRuleListStrategy
    extends IConfigStrategy<IRuleListModel, IRuleListStrategySaveModel> {
  
}
