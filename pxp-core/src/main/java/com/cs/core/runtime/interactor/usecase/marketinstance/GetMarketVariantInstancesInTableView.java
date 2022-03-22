package com.cs.core.runtime.interactor.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.variant.marketinstance.IGetMarketVariantInstancesInTableView;
import com.cs.pim.runtime.targetinstance.market.IGetMarketVariantInstancesInTableViewService;

@Service
public class GetMarketVariantInstancesInTableView
    extends AbstractRuntimeInteractor<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel>
    implements IGetMarketVariantInstancesInTableView {
  
  @Autowired
  protected IGetMarketVariantInstancesInTableViewService getMarketVariantInstancesInTableViewService;
  
  @Override
  protected IGetVariantInstancesInTableViewModel executeInternal(
      IGetVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    return getMarketVariantInstancesInTableViewService.execute(getKlassInstanceTreeStrategyModel);
  }
}
