package com.cs.core.runtime.interactor.usecase.variant.articleinstance;

import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateArticleInstanceVariantForLimitedObject extends
    IRuntimeInteractor<ICreateVariantForLimitedObjectRequestModel, IGetVariantInstancesInTableViewModel> {
}
