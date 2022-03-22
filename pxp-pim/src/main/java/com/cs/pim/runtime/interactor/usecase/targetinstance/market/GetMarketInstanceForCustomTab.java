package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.IGetMarketInstanceForCustomTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetMarketInstanceForCustomTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetMarketInstanceForCustomTab {
  
  @Autowired
  protected IGetMarketInstanceForCustomTabService getMarketInstanceForCustomTabService;
  
  @Override
  protected IGetKlassInstanceCustomTabModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getMarketInstanceForCustomTabService.execute(getKlassInstanceTreeStrategyModel);
  }
}
