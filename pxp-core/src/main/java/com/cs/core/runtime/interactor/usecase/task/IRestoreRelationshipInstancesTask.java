package com.cs.core.runtime.interactor.usecase.task;

import com.cs.core.config.interactor.model.relationship.IRestoreRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IRestoreRelationshipInstancesTask
    extends IRuntimeInteractor<IRestoreRelationshipInstancesRequestModel, IModel> {
}
