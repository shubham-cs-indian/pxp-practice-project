package com.cs.core.config.businessapi.tabs;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteTabsService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
}
