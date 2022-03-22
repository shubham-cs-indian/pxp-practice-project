package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.IBulkSaveResponseModelForTaskTab;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICompleteTaskInstanceByIds extends IRuntimeInteractor<IIdsListParameterModel, IBulkSaveResponseModelForTaskTab> {
}