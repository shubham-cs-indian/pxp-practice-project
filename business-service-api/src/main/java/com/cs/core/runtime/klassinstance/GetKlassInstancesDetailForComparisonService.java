package com.cs.core.runtime.klassinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetInstancesDetailForComparisonModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstancesListForComparisonModel;
import org.springframework.stereotype.Service;

@Service
public class GetKlassInstancesDetailForComparisonService extends AbstractGetInstancesDetailForComparisonService<IGetInstancesDetailForComparisonModel, IKlassInstancesListForComparisonModel>
  implements IGetKlassInstancesDetailsForComparisonService {
  
  protected IKlassInstancesListForComparisonModel executeInternal(
      IGetInstancesDetailForComparisonModel  requestModel) throws Exception
  {
    return super.executeInternal(requestModel);
  }

}
