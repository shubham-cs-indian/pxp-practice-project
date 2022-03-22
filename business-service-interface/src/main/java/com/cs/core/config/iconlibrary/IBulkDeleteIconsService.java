package com.cs.core.config.iconlibrary;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IBulkDeleteIconsService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
