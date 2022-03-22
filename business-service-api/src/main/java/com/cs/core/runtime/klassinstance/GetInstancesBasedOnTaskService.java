package com.cs.core.runtime.klassinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstancesBasedOnTaskModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import org.springframework.stereotype.Service;

@Service
public class GetInstancesBasedOnTaskService
    extends AbstractGetInstancesBasedOnTaskService<IGetInstancesBasedOnTaskModel, IGetKlassInstanceTreeModel>
    implements IGetInstancesBasedOnTaskService {
 
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(
      IGetInstancesBasedOnTaskModel getKlassInstanceBasedOnTaskStrategyModel) throws Exception
  {
    return super.executeInternal(getKlassInstanceBasedOnTaskStrategyModel);
  }
  
}
