package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.targetinstance.IMarketInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.ISaveMarketInstanceForTabsService;

@Service
public class SaveMarketInstanceForTabs
    extends AbstractRuntimeInteractor<IMarketInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveMarketInstanceForTabs {
  
  @Autowired
  protected ISaveMarketInstanceForTabsService saveMarketInstanceForTabsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IMarketInstanceSaveModel klassInstancesModel)
      throws Exception
  {
    
    return saveMarketInstanceForTabsService.execute(klassInstancesModel);
  }
}
