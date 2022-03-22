package com.cs.core.config.interactor.usecase.datarule;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetDataRule extends IGetConfigInteractor<IIdParameterModel, IDataRuleModel> {
  
}
