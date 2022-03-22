package com.cs.core.runtime.interactor.usecase.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.languageinstance.IBulkPropogationForDeletedTranslationsRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IInitiateBulkPropogationOnDeletedTranslationsTask
    extends IRuntimeInteractor<IBulkPropogationForDeletedTranslationsRequestModel, IModel> {
}
