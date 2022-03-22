package com.cs.core.config.strategy.usecase.collection;

import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetSectionInfoForCollectionStrategy
    extends IConfigStrategy<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel> {
  
}
