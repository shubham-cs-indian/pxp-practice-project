package com.cs.core.runtime.interactor.usecase.scheduler;

import org.springframework.stereotype.Component;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Component
public class DailyTaskScheduler implements IDailyTaskScheduler {
  
  /*@Autowired
  protected IDailyTaskSchedulerStrategy dailyTaskSchedulerStrategy;*/
  
  public void initialize()
  {
    try {
      execute(null);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  @Override
  public IModel execute(IModel model) throws Exception
  {
    return null /* dailyTaskSchedulerStrategy.execute(model)*/;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
