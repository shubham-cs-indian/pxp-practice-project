package com.cs.core.runtime.interactor.usecase.variant.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetPropertiesVariantInstancesInTableViewService;

@Service
public class GetAssetPropertiesVariantInstancesInTableView
    extends AbstractRuntimeInteractor<IGetPropertiesVariantInstanceInTableViewRequestModel, IGetPropertiesVariantInstancesInTableViewModel>
    implements IGetAssetPropertiesVariantInstancesInTableView {
  
  @Autowired
  protected IGetAssetPropertiesVariantInstancesInTableViewService getAssetPropertiesVariantInstancesInTableViewService;
  
  @Override
  protected IGetPropertiesVariantInstancesInTableViewModel executeInternal(
      IGetPropertiesVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel)
      throws Exception
  {
    return getAssetPropertiesVariantInstancesInTableViewService.execute(getKlassInstanceTreeStrategyModel);
  }
}
