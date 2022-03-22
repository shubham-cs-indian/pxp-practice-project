package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.IGetMarketInstanceForLanguageComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetMarketInstanceForLanguageComparison extends AbstractRuntimeInteractor<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel>
    implements IGetMarketInstanceForLanguageComparison {

  @Autowired
  protected IGetMarketInstanceForLanguageComparisonService getMarketInstanceForLanguageComparisonService;

  @Override
  protected ILanguageComparisonResponseModel executeInternal(
      IGetInstanceForLanguageComparisonRequestModel klassInstancesModel) throws Exception
  {
    return getMarketInstanceForLanguageComparisonService.execute(klassInstancesModel);
  }
}
