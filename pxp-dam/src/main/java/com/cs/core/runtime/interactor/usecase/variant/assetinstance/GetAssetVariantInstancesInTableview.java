package com.cs.core.runtime.interactor.usecase.variant.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetVariantInstancesInTableViewService;

@Service
public class GetAssetVariantInstancesInTableview
    extends AbstractRuntimeInteractor<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel>
    implements IGetAssetVariantInstancesInTableView {
  
  @Autowired
  protected IGetAssetVariantInstancesInTableViewService getAssetVariantInstancesInTableViewService;
  
  @Override
  protected IGetVariantInstancesInTableViewModel executeInternal(
      IGetVariantInstanceInTableViewRequestModel getAssetInstanceTreeStrategyModel) throws Exception
  {
    return getAssetVariantInstancesInTableViewService.execute(getAssetInstanceTreeStrategyModel);
  }
}
