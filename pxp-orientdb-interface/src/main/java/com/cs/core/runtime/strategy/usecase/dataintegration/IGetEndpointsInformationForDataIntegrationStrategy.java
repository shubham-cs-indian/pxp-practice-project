package com.cs.core.runtime.strategy.usecase.dataintegration;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetEndpointsInformationForDataIntegrationStrategy
extends IRuntimeStrategy<IGetEndointsInfoModel, IListModel<IDataIntegrationInfoModel>> {

}
