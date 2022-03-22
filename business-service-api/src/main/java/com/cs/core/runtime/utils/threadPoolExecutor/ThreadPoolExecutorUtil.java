package com.cs.core.runtime.utils.threadPoolExecutor;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.kafka.IThreadPoolTaskModel;
import com.cs.core.runtime.interactor.model.kafka.ThreadPoolTaskModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.usecase.task.ThreadPoolTask;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolExecutorUtil {
  
  @Autowired
  protected TransactionThreadData  transactionThreadData;
  
  @Autowired
  protected ApplicationContext     appContext;
  
  @Autowired
  protected ThreadPoolTaskExecutor taskExecutor;
  
  public void prepareRequestModel(IModel contentInfo, String taskName) throws Exception
  {
    
    TransactionData transactionData = transactionThreadData.getTransactionData();
    TransactionData newTransactionData = new TransactionData();
    BeanUtils.copyProperties(newTransactionData, transactionData);
    newTransactionData.setLevel(newTransactionData.getLevel() + 1);
    IThreadPoolTaskModel messageData = new ThreadPoolTaskModel();
    messageData.setTransactionData(newTransactionData);
    messageData.setContentInfo(contentInfo);
    messageData.setTaskName(taskName);
    
    executeTask(messageData);
  }
  
  public void executeTask(IThreadPoolTaskModel model)
  {
    ThreadPoolTask kafkaDisabledHandlerTask = appContext.getBean(ThreadPoolTask.class);
    kafkaDisabledHandlerTask.setData(model);
    taskExecutor.execute(kafkaDisabledHandlerTask);
  }
}
