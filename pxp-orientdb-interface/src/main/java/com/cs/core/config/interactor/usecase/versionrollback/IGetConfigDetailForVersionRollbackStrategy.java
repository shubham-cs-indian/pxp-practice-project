package com.cs.core.config.interactor.usecase.versionrollback;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;

public interface IGetConfigDetailForVersionRollbackStrategy extends
    IConfigStrategy<IMulticlassificationRequestModel, IGetConfigDetailsForVersionRollbackModel> {
  
}
