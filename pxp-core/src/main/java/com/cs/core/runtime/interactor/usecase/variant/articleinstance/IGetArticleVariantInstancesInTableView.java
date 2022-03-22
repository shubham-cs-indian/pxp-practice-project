package com.cs.core.runtime.interactor.usecase.variant.articleinstance;

import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetArticleVariantInstancesInTableView extends
    IRuntimeInteractor<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel> {
}
