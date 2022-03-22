package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllTargetsStrategy
    extends IConfigStrategy<ITargetModel, IListModel<IKlassInformationModel>> {
  
}
