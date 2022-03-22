package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.targetinstance.IMarketInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.ISaveMarketInstanceForOverviewTabService;

@Service
public class SaveMarketInstanceForOverviewTab extends AbstractRuntimeInteractor<IMarketInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveMarketInstanceForOverviewTab {
  
  @Autowired
  protected ISaveMarketInstanceForOverviewTabService saveMarketInstanceForOverviewTabService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IMarketInstanceSaveModel klassInstancesModel) throws Exception
  {
    
    return saveMarketInstanceForOverviewTabService.execute(klassInstancesModel);
  }
}
