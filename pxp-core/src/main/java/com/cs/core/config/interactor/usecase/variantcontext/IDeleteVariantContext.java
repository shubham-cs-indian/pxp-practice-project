package com.cs.core.config.interactor.usecase.variantcontext;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteVariantContext
    extends IConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
