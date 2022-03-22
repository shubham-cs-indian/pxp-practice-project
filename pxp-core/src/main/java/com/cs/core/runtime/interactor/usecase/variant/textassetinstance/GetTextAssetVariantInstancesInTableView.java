package com.cs.core.runtime.interactor.usecase.variant.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetVariantInstancesInTableViewService;

@Service
public class GetTextAssetVariantInstancesInTableView
    extends AbstractRuntimeInteractor<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel>
    implements IGetTextAssetVariantInstancesInTableView {
  
  @Autowired
  protected IGetTextAssetVariantInstancesInTableViewService getTextAssetVariantInstancesInTableViewService;
  
  @Override
  protected IGetVariantInstancesInTableViewModel executeInternal(
      IGetVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    return getTextAssetVariantInstancesInTableViewService.execute(getKlassInstanceTreeStrategyModel);
  }
}
