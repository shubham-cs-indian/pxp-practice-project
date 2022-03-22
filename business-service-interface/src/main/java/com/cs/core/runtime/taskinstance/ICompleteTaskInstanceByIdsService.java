package com.cs.core.runtime.taskinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.IBulkSaveResponseModelForTaskTab;

public interface ICompleteTaskInstanceByIdsService extends IRuntimeService<IIdsListParameterModel, IBulkSaveResponseModelForTaskTab> {
}