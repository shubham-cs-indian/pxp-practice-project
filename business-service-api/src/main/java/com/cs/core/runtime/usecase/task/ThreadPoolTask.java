package com.cs.core.runtime.usecase.task;

import com.cs.core.businessapi.base.IService;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.zookeeper.InstanceLockException;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.kafka.IThreadPoolTaskModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;

@Service
@Scope("prototype")
public class ThreadPoolTask implements Runnable {
  
  protected IThreadPoolTaskModel   messageData;
  
  @Autowired
  protected ApplicationContext     appContext;
  
  @Autowired
  protected TransactionThreadData  transactionThreadData;
  
  @Autowired
  protected Boolean                isKafkaLoggingEnabled;
  
  @Autowired
  protected ThreadPoolTaskExecutor taskExecutor;
  
  public void setData(IThreadPoolTaskModel messageData)
  {
    this.messageData = messageData;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void run()
  {
    try {
      transactionThreadData.setTransactionData((TransactionData) messageData.getTransactionData());
      String taskName = messageData.getTaskName();
      IModel contentInfo = messageData.getContentInfo();
      Class<?> cls = Class.forName(taskName);
      IService<IModel, IModel> usecase = (IService<IModel, IModel>) appContext.getBean(cls);
      usecase.execute(contentInfo);
    }
    catch (InstanceLockException | SocketTimeoutException e) {
//      KafkaUtils.log("TaskExecutor Retry Catch", e, consumerFailureLogPath, null);
      Integer retryCount = messageData.getRetryCount();
      if (retryCount >= 2) {
        messageData.setRetryCount(--retryCount);
        ThreadPoolTask kafkaDisabledHandlerTask = appContext
            .getBean(ThreadPoolTask.class);
        kafkaDisabledHandlerTask.setData(messageData);
        taskExecutor.execute(kafkaDisabledHandlerTask);
      }
      else {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
//        KafkaUtils.log("TaskExecutor Timeout Catch", e, consumerFailureLogPath, null);
      }
    }
    catch (Throwable ex) {
      ex.printStackTrace();
      RDBMSLogger.instance().exception(ex);
      if (isKafkaLoggingEnabled) {
//        KafkaUtils.log("KafkaDisabledHandlerTask ", ex, bulkPropagationLogPath, null);
      }
    }
  }

}
