package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForDataTransferModel;

public interface IGetIdentifierAttributesForTypesStrategy
    extends IConfigStrategy<IMulticlassificationRequestModel, IConfigDetailsForDataTransferModel> {
}
