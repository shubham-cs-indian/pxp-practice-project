package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetInstancesDetailForComparisonModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstancesListForComparisonModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetKlassInstancesDetailsForComparison
    extends IRuntimeInteractor<IGetInstancesDetailForComparisonModel, IKlassInstancesListForComparisonModel> {
  
}
