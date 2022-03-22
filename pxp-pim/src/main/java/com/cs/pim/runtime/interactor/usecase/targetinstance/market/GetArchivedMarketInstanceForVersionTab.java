package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.IGetArchivedMarketInstanceForVersionTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArchivedMarketInstanceForVersionTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel>
    implements IGetArchivedMarketInstanceForVersionTab{
  
  @Autowired
  protected IGetArchivedMarketInstanceForVersionTabService getArchivedMarketInstanceForVersionTabService;
  
  @Override
  public IGetKlassInstanceVersionsForTimeLineModel executeInternal(
      IGetInstanceRequestModel dataModel) throws Exception
  {
    return getArchivedMarketInstanceForVersionTabService.execute(dataModel);
  }
}
