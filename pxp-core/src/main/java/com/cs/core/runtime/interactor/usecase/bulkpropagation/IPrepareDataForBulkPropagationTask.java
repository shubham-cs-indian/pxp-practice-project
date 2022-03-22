package com.cs.core.runtime.interactor.usecase.bulkpropagation;

import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IPrepareDataForBulkPropagationTask
    extends IRuntimeInteractor<IContentDiffModelToPrepareDataForBulkPropagation, IModel> {
}
