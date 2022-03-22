package com.cs.dam.runtime.strategy.usecase.assetinstance;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForTasksTabModel;


public interface IGetConfigDetailsForAssetInstanceTasksTabStrategy
    extends IConfigStrategy<IMulticlassificationRequestModel, IGetConfigDetailsForTasksTabModel> {
  
}
