package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

public interface IGetMultiClassificationKlassDetailsStrategy extends
    IConfigStrategy<IMulticlassificationRequestModel, IGetMultiClassificationKlassDetailsModel> {
}
