package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IUpdateIdentifierStatusForSearchableInstanceTask
    extends IRuntimeInteractor<IUpdateSearchableInstanceModel, IVoidModel> {
}
