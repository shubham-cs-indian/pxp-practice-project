package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.variants.IVariantLinkedInstancesQuickListModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetVariantLinkedInstancesQuickList
    extends IRuntimeInteractor<IVariantLinkedInstancesQuickListModel, IGetKlassInstanceTreeModel> {
  
}
