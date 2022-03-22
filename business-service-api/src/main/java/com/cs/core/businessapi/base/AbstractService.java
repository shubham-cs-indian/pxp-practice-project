package com.cs.core.businessapi.base;



import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.logger.InteractorData;
import com.cs.core.runtime.interactor.model.logger.InteractorThreadData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;

public abstract class AbstractService<P extends IModel, R extends IModel>
implements IService<P, R> {
  
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
  
  /**
   * The method runs in transaction boundary (Annotation Driven transaction) 
   * All calls from this method run in the transaction If an exception is thrown
   * while executing, it rollbacks all the SQL dataBase operation in a given transaction
   * process Nested transaction work as a single transaction
   * Starting point of call services
   */
  @Override
  @Transactional(transactionManager = "postgresTransactionManager")
  public R execute(P dataModel) throws Exception
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    Boolean isDataIntegrationUsecase = (transactionData.getId() == null) ? true : false;
    if (isDataIntegrationUsecase) {
      fillTransactionData(transactionData);
    }
    
    try {
      InteractorData interactorData = interactorThread.getInteractorData();
      interactorData.setKlassName(this.getClass().getSimpleName());
      interactorData.setStartTime(System.currentTimeMillis());
      transactionData.setInteractorData(interactorData);
      R response = executeInternal(dataModel);
      TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter()
      {
        public void afterCommit()
        {
          try {
            workflowUtils.executeBusinessProcessEvent(getUsecase().actionType, dataModel, response);
          }
          catch (Exception e) {
            RDBMSLogger.instance().exception(e);
          }
        }
      });
      interactorData.setEndTime(System.currentTimeMillis());
      interactorData.setTurnAroundTime((interactorData.getEndTime() - interactorData.getStartTime()));
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
  
  /*@SuppressWarnings("unchecked")
  @Override
  public R execute(P dataModel) throws Exception
  {
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver)RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.setName(UUID.randomUUID().toString() + getClass() );
    return (R) transactionTemplate.execute( new TransactionCallback<Object>()
    {
      public Object doInTransaction(TransactionStatus status)
      {
        try {
          return execute_old(dataModel);
        }
        catch (Exception e) {
          status.setRollbackOnly();
          e.printStackTrace();
        }
        return null;
        }
      });
     }*/
}
