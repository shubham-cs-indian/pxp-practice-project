package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteTasksService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {

}
