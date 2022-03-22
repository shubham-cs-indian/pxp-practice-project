package com.cs.core.runtime.klassinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstancesForComparisonModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstancesForComparisonRequestModel;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGetInstancesForComparisonService<P extends IGetKlassInstancesForComparisonRequestModel, R extends IGetKlassInstancesForComparisonModel>
    extends AbstractRuntimeService<P, R> {
  
  protected abstract R executeGetInstancesForComparison(
      P getKlassInstancesForComparisonRequestModel) throws Exception;
  
  @Override
  protected R executeInternal(P getKlassInstancesForComparisonRequestModel) throws Exception
  {
    return executeGetInstancesForComparison(getKlassInstancesForComparisonRequestModel);
  }
}
