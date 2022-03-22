package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionInfoModel;
import com.cs.core.runtime.interactor.model.collections.ISaveStaticCollectionDetailsModel;

public interface ISaveStaticCollectionDetailsStrategy
    extends IConfigStrategy<ISaveStaticCollectionDetailsModel, IGetStaticCollectionInfoModel> {
  
}
