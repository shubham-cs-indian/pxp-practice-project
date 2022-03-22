package com.cs.core.runtime.interactor.usecase.context;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.context.ICheckDuplicateLinkedVariantRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICheckDuplicateLinkedVariant
    extends IRuntimeInteractor<ICheckDuplicateLinkedVariantRequestModel, IVoidModel> {
  
}
