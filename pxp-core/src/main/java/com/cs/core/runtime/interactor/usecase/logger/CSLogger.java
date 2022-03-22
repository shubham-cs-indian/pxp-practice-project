package com.cs.core.runtime.interactor.usecase.logger;
/*package com.cs.base.interactor.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.cs.base.interactor.logger.Model.ILoggerMode;
import com.cs.base.interactor.logger.Model.LogModel;
import com.cs.base.interactor.thread.ApplicationThreadLocal;

@Component
public class CSLogger {

  private final Logger                           slf4jErrorLogger = LoggerFactory
                                                                      .getLogger("com.SL4JErrorLogger.log");

  @Autowired
  ThreadPoolTaskExecutor taskExecutor;

  @Autowired
  ApplicationThreadLocal threadLocal;

  @Autowired
  EchoSocket             echoSocket;

  public void debug(String className)
  {
    debug(className, "");
  }

  public void debug(String className, String functionName)
  {
    debug(className, functionName, "");
  }

  public void debug(String className, String functionName, String message)
  {
    debug(className, functionName, message, "");
  }

  public void debug(String className, String functionName, String message, Object data)
  {
    log(className, functionName, message, data, "DEBUG");
  }

  public void error(String className, String functionName, String message, Object data)
  {
    log(className, functionName, message, data, "ERROR");
  }

  public void info(String className, String useCase, String message, Object data)
  {
    log(className, useCase, message, data, "INFO");
  }

  public void log(final String className, final String userScenario, final String description,
      final Object data, final String logLevel)
  {
    try {

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
      Calendar cal = Calendar.getInstance();

      LogModel logModel = new LogModel();
      logModel.setCaptureTimeStamp(sdf.format(new Date(cal.getTimeInMillis())));
      logModel.setDescription(description);
      logModel.setAppData(data);
      logModel.setUserScenario(userScenario);
      logModel.setClassName(className);
      logModel.setLogType(logLevel);
      logModel.setMode(ILoggerMode.SERVER);
      if (threadLocal.getValue() != null) {
        logModel.setRequestId(threadLocal.getValue().getRequestId());
        logModel.setSessionId(threadLocal.getValue().getSessionId());
      }
      if (description.equalsIgnoreCase("request")) {
        Long startTime = System.currentTimeMillis();
        threadLocal.getValue().setStartTime(startTime);

      } else if (description.equalsIgnoreCase("response")) {
        Long endTime = System.currentTimeMillis();
        threadLocal.getValue().setEndTime(endTime);
        logModel.setTurnArroundTime(threadLocal.getValue().getEndTime() - threadLocal.getValue().getStartTime());
      }

      if (taskExecutor != null) {
        taskExecutor.execute(new LogTask(logModel, echoSocket));
      }
      else {
        throw new Exception("logMessageBlockingQueue is null");
      }

    }
    catch (Exception e) {
      String error = CSLogUtil.getStackTrace(e);
      slf4jErrorLogger.error(error);
    }
  }

}*/
