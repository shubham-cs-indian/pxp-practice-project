package com.cs.core.runtime.interactor.usecase.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IInitiateRelationshipDataTransferOnRelationshipChangeTask
    extends IRuntimeInteractor<ISideInfoForRelationshipDataTransferModel, IModel> {
}
