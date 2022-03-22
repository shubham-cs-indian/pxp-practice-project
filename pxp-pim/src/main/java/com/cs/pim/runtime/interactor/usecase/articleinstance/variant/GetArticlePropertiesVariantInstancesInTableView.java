package com.cs.pim.runtime.interactor.usecase.articleinstance.variant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArticlePropertiesVariantInstancesInTableViewService;

@Service
public class GetArticlePropertiesVariantInstancesInTableView
    extends AbstractRuntimeInteractor<IGetPropertiesVariantInstanceInTableViewRequestModel, IGetPropertiesVariantInstancesInTableViewModel>
    implements IGetArticlePropertiesVariantInstancesInTableView {
  
  @Autowired
  protected IGetArticlePropertiesVariantInstancesInTableViewService getArticlePropertiesVariantInstancesInTableViewService;
  
  @Override
  protected IGetPropertiesVariantInstancesInTableViewModel executeInternal(
      IGetPropertiesVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    return getArticlePropertiesVariantInstancesInTableViewService.execute(getKlassInstanceTreeStrategyModel);
  }
}
