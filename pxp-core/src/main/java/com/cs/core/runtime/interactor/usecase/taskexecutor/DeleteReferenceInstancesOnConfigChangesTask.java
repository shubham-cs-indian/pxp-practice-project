package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.config.interactor.model.klass.IBulkDeleteSuccessKlassResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import org.springframework.stereotype.Component;

@Component
public class DeleteReferenceInstancesOnConfigChangesTask
    extends AbstractRuntimeInteractor<IBulkDeleteSuccessKlassResponseModel, IIdParameterModel>
    implements IDeleteReferenceInstancesOnConfigChangesTask {
  
  /*@Autowired
  protected IDeleteReferenceInstancesStrategy                             deleteReferenceInstancesStrategy;*/
  
  protected IIdParameterModel executeInternal(IBulkDeleteSuccessKlassResponseModel model)
      throws Exception
  {
    /*return deleteReferenceInstancesStrategy.execute(model);*/
    return null;
  }
}
