package com.cs.core.runtime.interactor.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.variant.marketinstance.IGetMarketPropertiesVariantInstancesInTableView;
import com.cs.pim.runtime.targetinstance.market.IGetMarketPropertiesVariantInstancesInTableViewService;

@Service
public class GetMarketPropertiesVariantInstancesInTableView
    extends AbstractRuntimeInteractor<IGetPropertiesVariantInstanceInTableViewRequestModel, IGetPropertiesVariantInstancesInTableViewModel>
    implements IGetMarketPropertiesVariantInstancesInTableView {
  
  @Autowired
  protected IGetMarketPropertiesVariantInstancesInTableViewService getMarketPropertiesVariantInstancesInTableViewService;
  
  @Override
  protected IGetPropertiesVariantInstancesInTableViewModel executeInternal(
      IGetPropertiesVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    return getMarketPropertiesVariantInstancesInTableViewService.execute(getKlassInstanceTreeStrategyModel);
  }
}
