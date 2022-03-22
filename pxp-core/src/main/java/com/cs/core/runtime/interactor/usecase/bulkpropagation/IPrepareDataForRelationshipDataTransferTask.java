package com.cs.core.runtime.interactor.usecase.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInputModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IPrepareDataForRelationshipDataTransferTask
    extends IRuntimeInteractor<IRelationshipDataTransferInputModel, IModel> {
}
