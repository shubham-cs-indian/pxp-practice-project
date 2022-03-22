package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.task.IDeleteTasksStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeleteTasksService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> implements IDeleteTasksService {
  
  @Autowired
  IDeleteTasksStrategy deleteTasksStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy      getTaskEntityConfigurationStrategy;

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    List<String> taskIdsToBeDeleted = dataModel.getIds();
    IGetEntityConfigurationResponseModel getEntityResponse = getTaskEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(taskIdsToBeDeleted));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();

    if (!referenceData.keySet().isEmpty()) {
      throw new EntityConfigurationDependencyException();
    }

    return deleteTasksStrategy.execute(dataModel);
  }
}
