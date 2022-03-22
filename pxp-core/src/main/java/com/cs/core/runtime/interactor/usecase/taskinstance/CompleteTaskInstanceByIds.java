package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.IBulkSaveResponseModelForTaskTab;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.taskinstance.ICompleteTaskInstanceByIdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompleteTaskInstanceByIds extends AbstractRuntimeInteractor<IIdsListParameterModel, IBulkSaveResponseModelForTaskTab>
    implements ICompleteTaskInstanceByIds {
  
  @Autowired
  protected ICompleteTaskInstanceByIdsService completeTaskInstanceByIdsService;

  @Override
  protected IBulkSaveResponseModelForTaskTab executeInternal(IIdsListParameterModel model) throws Exception
  {
  return completeTaskInstanceByIdsService.execute(model);
  }

}
