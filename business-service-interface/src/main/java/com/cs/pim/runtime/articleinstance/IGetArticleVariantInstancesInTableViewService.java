package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;

public interface IGetArticleVariantInstancesInTableViewService extends
    IRuntimeService<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel> {
}
