package com.cs.core.runtime.interactor.usecase.variant.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetPropertiesVariantInstancesInTableViewService;

@Service
public class GetTextAssetPropertiesVariantInstancesInTableView
    extends AbstractRuntimeInteractor<IGetPropertiesVariantInstanceInTableViewRequestModel, IGetPropertiesVariantInstancesInTableViewModel>
    implements IGetTextAssetPropertiesVariantInstancesInTableView {
  
  @Autowired
  protected IGetTextAssetPropertiesVariantInstancesInTableViewService getTextAssetPropertiesVariantInstancesInTableView;
  
  @Override
  protected IGetPropertiesVariantInstancesInTableViewModel executeInternal(
      IGetPropertiesVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    return getTextAssetPropertiesVariantInstancesInTableView.execute(getKlassInstanceTreeStrategyModel);
  }
}
