package com.cs.core.runtime.interactor.usecase.variant.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArticleVariantInstancesInTableViewService;

@Service
public class GetArticleVariantInstancesInTableView
    extends AbstractRuntimeInteractor<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel>
    implements IGetArticleVariantInstancesInTableView {
  
  @Autowired
  protected IGetArticleVariantInstancesInTableViewService getArticleVariantInstancesInTableViewService;
  
  @Override
  protected IGetVariantInstancesInTableViewModel executeInternal(
      IGetVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    return getArticleVariantInstancesInTableViewService.execute(getKlassInstanceTreeStrategyModel);
  }
}
