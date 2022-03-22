package com.cs.core.config.interactor.usecase.rulelist;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetRuleList extends IGetConfigInteractor<IIdParameterModel, IRuleListModel> {
  
}
