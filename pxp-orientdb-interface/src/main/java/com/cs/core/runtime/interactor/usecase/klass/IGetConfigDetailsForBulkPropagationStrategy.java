package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

public interface IGetConfigDetailsForBulkPropagationStrategy extends
    IConfigStrategy<IMulticlassificationRequestModel, IConfigDetailsForBulkPropagationResponseModel> {
}
