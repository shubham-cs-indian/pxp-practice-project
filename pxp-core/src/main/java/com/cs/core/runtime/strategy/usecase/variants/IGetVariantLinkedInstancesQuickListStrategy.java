package com.cs.core.runtime.strategy.usecase.variants;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.variants.IVariantLinkedInstancesQuickListModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetVariantLinkedInstancesQuickListStrategy extends
    IRuntimeStrategy<IVariantLinkedInstancesQuickListModel, IGetKlassInstanceTreeModel> {
  
}
