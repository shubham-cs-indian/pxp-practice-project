package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionsComparisonModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.IGetMarketInstanceVersionsForComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetMarketInstanceVersionsForComparison extends AbstractRuntimeInteractor<IKlassInstanceVersionsComparisonModel, IGetKlassInstanceVersionsForComparisonModel>
    implements IGetMarketInstanceVersionsForComparison {
  
  @Autowired
  protected IGetMarketInstanceVersionsForComparisonService getMarketInstanceVersionsForComparisonService;
  
  @Override
  public IGetKlassInstanceVersionsForComparisonModel executeInternal(
      IKlassInstanceVersionsComparisonModel dataModel) throws Exception
  {
    return getMarketInstanceVersionsForComparisonService.execute(dataModel);
  }

}
