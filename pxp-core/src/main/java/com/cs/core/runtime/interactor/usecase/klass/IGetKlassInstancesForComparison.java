package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstancesForComparisonModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstancesForComparisonRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetKlassInstancesForComparison extends
    IRuntimeInteractor<IGetKlassInstancesForComparisonRequestModel, IGetKlassInstancesForComparisonModel> {
}
