package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.IBulkApplyValueRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IBulkApplyValues
    extends IRuntimeInteractor<IBulkApplyValueRequestModel, IIdsListParameterModel> {
  
}
