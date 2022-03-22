package com.cs.core.runtime.strategy.dashboard;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetConfigDetailsForDashboardTileInformationStrategy
    extends IConfigStrategy<IIdParameterModel, IConfigDetailsForInstanceTreeGetModel> {
}
