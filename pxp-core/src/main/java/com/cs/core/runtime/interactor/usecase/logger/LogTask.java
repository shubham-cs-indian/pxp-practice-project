package com.cs.core.runtime.interactor.usecase.logger;
/*package com.cs.base.interactor.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.base.interactor.logger.Model.ILoggerMode;
import com.cs.base.interactor.logger.Model.LogModel;

public class LogTask implements Runnable {

  private final Logger slf4jErrorLogger = LoggerFactory.getLogger("com.SL4JErrorLogger.log");

  private final Logger slf4jCSLogger    = LoggerFactory.getLogger("com.SL4JCSLogger.log");

  private LogModel     logModel;

  private EchoSocket   socket;

  public LogTask(LogModel logMessage)
  {
    this.logModel = logMessage;
  }

  public LogTask(LogModel logMessage, EchoSocket socket)
  {
    this.logModel = logMessage;
    this.socket = socket;
  }

  @Override
  public void run()
  {
    try {

      String loggerMode = logModel.getMode();

      switch (loggerMode) {
        case ILoggerMode.SERVER:
          try {
            socket.logMessage(logModel.toString());
            break;
          }
          catch (Exception e) {
            String error = CSLogUtil.getStackTrace(e);
            slf4jErrorLogger.error(error);
          }

        case ILoggerMode.FILE:
          try {
            slf4jCSLogger.info(logModel.toString().replaceAll("\\\\\"", "\""));
          }
          catch (Exception e) {
            String error = CSLogUtil.getStackTrace(e);
            slf4jErrorLogger.error(error);

          }

          break;

        case ILoggerMode.CONSOLE:
          RDBMSLogger.instance().info(logModel);
          break;
      }
    }
    catch (Exception e) {
      String error = CSLogUtil.getStackTrace(e);
      slf4jErrorLogger.error(error);
    }
  }

}
*/
