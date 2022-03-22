package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.klassinstance.IGetKlassInstancesDetailsForComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IGetInstancesDetailForComparisonModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstancesListForComparisonModel;

@Service
public class GetKlassInstancesDetailForComparison  extends AbstractRuntimeInteractor<IGetInstancesDetailForComparisonModel, IKlassInstancesListForComparisonModel>
  implements IGetKlassInstancesDetailsForComparison {

  @Autowired
  protected IGetKlassInstancesDetailsForComparisonService getKlassInstancesDetailsForComparisonService;

  protected IKlassInstancesListForComparisonModel executeInternal(
      IGetInstancesDetailForComparisonModel  requestModel) throws Exception
  {
    return getKlassInstancesDetailsForComparisonService.execute(requestModel);
  }

}
