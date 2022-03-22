package com.cs.core.config.strategy.usecase.concatenatedAttribute;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForSaveConcatenatedAttributeModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

public interface IGetConfigDetailsForSaveConcatenatedAttributeStrategy
    extends IConfigStrategy<IMulticlassificationRequestModel, IConfigDetailsForSaveConcatenatedAttributeModel> {
  
}
