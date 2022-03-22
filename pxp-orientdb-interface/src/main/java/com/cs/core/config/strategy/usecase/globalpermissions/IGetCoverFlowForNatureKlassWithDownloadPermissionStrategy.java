package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;
import com.cs.core.runtime.interactor.model.assetinstance.IUserIDAndTypesAndCoverFlowModel;

public interface IGetCoverFlowForNatureKlassWithDownloadPermissionStrategy
    extends IConfigStrategy<IUserIDAndTypesAndCoverFlowModel, IListModel<ICoverFlowModel>> {
  
}
