package com.cs.core.runtime.interactor.usecase.klassinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.instance.IGetInstanceTree;
import com.cs.core.runtime.klassinstance.IGetInstanceTreeService;

@Service
public class GetInstanceTree extends
    AbstractRuntimeInteractor<IGetKlassInstanceTreeStrategyModel, IGetKlassInstanceTreeModel>
    implements IGetInstanceTree {
  
  @Autowired
  protected IGetInstanceTreeService getInstanceTreeService;
  
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(IGetKlassInstanceTreeStrategyModel dataModel)
      throws Exception
  {
    return getInstanceTreeService.execute(dataModel);
  }
}
