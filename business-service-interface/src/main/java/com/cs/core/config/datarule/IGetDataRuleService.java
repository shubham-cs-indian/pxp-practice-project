package com.cs.core.config.datarule;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetDataRuleService extends IGetConfigService<IIdParameterModel, IDataRuleModel> {
  
}
