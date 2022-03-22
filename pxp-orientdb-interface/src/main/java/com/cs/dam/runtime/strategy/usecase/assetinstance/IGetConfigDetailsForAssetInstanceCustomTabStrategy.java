package com.cs.dam.runtime.strategy.usecase.assetinstance;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

public interface IGetConfigDetailsForAssetInstanceCustomTabStrategy
    extends IConfigStrategy<IMulticlassificationRequestModel, IGetConfigDetailsForCustomTabModel> {
}
