package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.variants.IVariantLinkedInstancesQuickListModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.klassinstance.IGetVariantLinkedInstancesQuickListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetVariantLinkedInstancesQuickList extends
    AbstractRuntimeInteractor<IVariantLinkedInstancesQuickListModel, IGetKlassInstanceTreeModel>
    implements IGetVariantLinkedInstancesQuickList {

  @Autowired
  protected IGetVariantLinkedInstancesQuickListService getVariantLinkedInstancesQuickListService;
  
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(IVariantLinkedInstancesQuickListModel model) throws Exception
  {
    return getVariantLinkedInstancesQuickListService.execute(model);
  }
}
