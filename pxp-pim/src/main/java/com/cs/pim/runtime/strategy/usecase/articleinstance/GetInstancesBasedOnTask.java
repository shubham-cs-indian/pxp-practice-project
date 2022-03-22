package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstancesBasedOnTaskModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.instance.IGetInstancesBasedOnTask;
import com.cs.core.runtime.klassinstance.IGetInstancesBasedOnTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetInstancesBasedOnTask
    extends AbstractRuntimeInteractor<IGetInstancesBasedOnTaskModel, IGetKlassInstanceTreeModel>
    implements IGetInstancesBasedOnTask {

  @Autowired
  protected IGetInstancesBasedOnTaskService  getInstancesBasedOnTaskService;
  
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(
      IGetInstancesBasedOnTaskModel getKlassInstanceBasedOnTaskStrategyModel) throws Exception
  {
    return getInstancesBasedOnTaskService.execute(getKlassInstanceBasedOnTaskStrategyModel);
  }
  
}
