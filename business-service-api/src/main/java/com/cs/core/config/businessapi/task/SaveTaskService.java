package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.*;
import com.cs.core.config.strategy.usecase.task.ISaveTaskStrategy;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveTaskService extends AbstractSaveConfigService<IListModel<ITaskModel>, IBulkSaveTasksResponseModel> implements ISaveTaskService {
  
  @Autowired
  protected ISaveTaskStrategy      saveTaskStrategy;
  
  /*@Autowired
  protected IGetTaskInstanceIdsByTypeStrategy getTaskInstanceIdsByTypeStrategy;*/
  
  @Autowired
  protected ThreadPoolTaskExecutor taskExecutor;
  
  @Autowired
  protected TransactionThreadData  controllerThread;
  
  @Autowired
  protected ApplicationContext     applicationContext;
  
  @Override
  public IBulkSaveTasksResponseModel executeInternal(IListModel<ITaskModel> model) throws Exception
  {
    IExceptionModel failure = new ExceptionModel();
    List<ITaskModel> validTasks = new ArrayList<>();
    for (ITaskModel task : model.getList()) {
      try {
        TaskValidations.validateTask(task, false);
        validTasks.add(task);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    model.setList(validTasks);

    IBulkSaveTasksResponseModel returnModel = saveTaskStrategy.execute(model);

    returnModel.getFailure().getDevExceptionDetails().addAll(failure.getDevExceptionDetails());
    returnModel.getFailure().getExceptionDetails().addAll(failure.getExceptionDetails());

    IGetGridTasksResponseModel responseSuccess = (IGetGridTasksResponseModel) returnModel.getSuccess();
    List<ISaveTaskResponseModel> responseList = responseSuccess.getTasksList();
    for (ISaveTaskResponseModel response : responseList) {
      if (response.getIsTypeSwitched()) {
        String id = response.getId();
        IIdParameterModel idModel = new IdParameterModel(id);
        // TODO: need to create bulk get task instance ids strategy
        IIdsListParameterModel idsList = new IdsListParameterModel(); /*getTaskInstanceIdsByTypeStrategy.execute(idModel);*/
        List<String> ids = idsList.getIds();
        String newType = response.getType();
        Map<String, IRole> referencedRACIVSRoles = response.getReferencedRoles();
        List<IPropagateTaskChangesModel> listModel = new ArrayList<>();
        for (String instanceId : ids) {
          IPropagateTaskChangesModel propagateModel = new PropagateTaskChangesModel();
          propagateModel.setId(instanceId);
          propagateModel.setNewType(newType);
          propagateModel.setReferencedRoles(referencedRACIVSRoles);
          listModel.add(propagateModel);
        }
        // TODO cannot call this method in loop
        propagateChangesToTaskInstances(listModel);
      }
      response.setReferencedRoles(new HashMap<>());
    }
    return returnModel;
  }
  
  private void propagateChangesToTaskInstances(List<IPropagateTaskChangesModel> listModel)
  {
    String transactionId = controllerThread.getTransactionData()
        .getId();
    for (IPropagateTaskChangesModel propagateTaskChangesModel : listModel) {
      PropagateTaskChangesToTaskInstancesTask propagateTaskChangesToTaskInstanceTask = applicationContext
          .getBean(PropagateTaskChangesToTaskInstancesTask.class);
      propagateTaskChangesToTaskInstanceTask.setData(propagateTaskChangesModel, transactionId);
      taskExecutor.submit(propagateTaskChangesToTaskInstanceTask);
    }
  }
}
