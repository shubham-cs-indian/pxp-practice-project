package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForQuickListModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;

public interface IGetConfigDetailsForQuicklistStrategy
    extends IConfigStrategy<IGetTargetKlassesModel, IConfigDetailsForQuickListModel> {
  
}
