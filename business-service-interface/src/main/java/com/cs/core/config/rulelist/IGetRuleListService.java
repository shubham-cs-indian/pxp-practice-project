package com.cs.core.config.rulelist;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetRuleListService extends IGetConfigService<IIdParameterModel, IRuleListModel> {
  
}
