package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import org.springframework.stereotype.Component;

@Component
public class UpdateIdentifierStatusForSearchableInstanceTask
    extends AbstractRuntimeInteractor<IUpdateSearchableInstanceModel, IVoidModel>
    implements IUpdateIdentifierStatusForSearchableInstanceTask {
  
  /*@Autowired
  protected IUpdateIdentifierStatusForSearchableInstanceStrategy updateIdentifierStatusForSearchableInstanceStrategy;*/
  
  @Override
  protected IVoidModel executeInternal(IUpdateSearchableInstanceModel updateSearchableInstanceModel)
      throws Exception
  {
    /*
    if (updateSearchableInstanceModel == null) {
      return new VoidModel();
    }
    
    List<String> searchableInstanceIds = updateSearchableInstanceModel.getSearchableInstanceIds();
    for (String searchableInstanceId : searchableInstanceIds) {
      IUpdateSearchableInstanceRequestModel updateSearchableInstanceRequestModel = new UpdateSearchableInstanceRequestModel();
      updateSearchableInstanceRequestModel.setSearchablePropertyInstancesInformation(
          updateSearchableInstanceModel.getSearchablePropertyInstancesInformation());
      updateSearchableInstanceRequestModel.setSearchableInstanceId(searchableInstanceId);
    
      updateIdentifierStatusForSearchableInstanceStrategy
          .execute(updateSearchableInstanceRequestModel);
    }*/
    return new VoidModel();
  }
}
