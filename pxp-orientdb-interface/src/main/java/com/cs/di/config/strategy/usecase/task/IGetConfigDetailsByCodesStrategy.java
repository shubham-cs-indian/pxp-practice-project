package com.cs.di.config.strategy.usecase.task;

import com.cs.core.config.interactor.model.marketingbundle.IKlassesAndTaxonomiesModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.di.config.interactor.model.task.IGetConfigDetailsByCodesResponseModel;

public interface IGetConfigDetailsByCodesStrategy
    extends IConfigStrategy<IKlassesAndTaxonomiesModel, IGetConfigDetailsByCodesResponseModel> {
  
}
