package com.cs.core.runtime.interactor.usecase.base;



import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.logger.InteractorData;
import com.cs.core.runtime.interactor.model.logger.InteractorThreadData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class AbstractInteractor<P extends IModel, R extends IModel>
implements IInteractor<P, R> {
  
  @Autowired
  InteractorThreadData                       interactorThread;
  
  @Autowired
  protected TransactionThreadData            transactionThread;
  
  @Autowired
  protected ISessionContext                  context;

  @Autowired
  protected WorkflowUtils workflowUtils;
  
  protected abstract R executeInternal(P model) throws Exception;
  
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.UNDEFINED;
  }

  public String getEntity(P model)
  {
    return null;
  }
  
  @Override
  public R execute(P dataModel) throws Exception
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    Boolean isDataIntegrationUsecase = (transactionData.getId() == null) ? true : false;
    if (isDataIntegrationUsecase) {
      fillTransactionData(transactionData);
    }
    
    try {
      InteractorData interactorData = interactorThread.getInteractorData();
      interactorData.setKlassName(this.getClass()
          .getSimpleName());
      interactorData.setStartTime(System.currentTimeMillis());
      transactionData.setInteractorData(interactorData);
      R response = executeInternal(dataModel);
      // initiateExportDiData.execute(dataModel, metaData);
      //workflowUtils.executeBusinessProcessEvent(getUsecase().actionType, dataModel, response);

      // dataUtil.initiateExportDiData(dataModel, metaData);
      interactorData.setEndTime(System.currentTimeMillis());
      interactorData
          .setTurnAroundTime((interactorData.getEndTime() - interactorData.getStartTime()));
      if (isDataIntegrationUsecase) {
        logging("success", null);
      }
      return response;
    }
    catch (Exception exception) {
      if (isDataIntegrationUsecase) {
        logging("failure", exception);
      }
      throw exception;
    }
  }
  
  private void fillTransactionData(TransactionData transactionData)
  {
    transactionData.setRequestId(UUID.randomUUID()
        .toString());
    transactionData.setId(UUID.randomUUID()
        .toString());
    transactionData.setUserId(context.getUserId());
    transactionData.setUserName(context.getUserName());
    transactionData.setUseCase("DataIntegration");
    transactionData.setStartTime(System.currentTimeMillis());
    transactionData.setExecutionStatus("pending");
  }
  
  public void logging(String responseStatus, Throwable exception)
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    transactionData.setEndTime(System.currentTimeMillis());
    transactionData.setInteractorData(interactorThread.getInteractorData());
    transactionData.setExecutionStatus(responseStatus);
    transactionData.setException(exception);
    // transactionThread.removeTransactionData();
  }
}
