package com.cs.core.runtime.interactor.usecase.logger;
/*package com.cs.base.interactor.logger.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cs.base.interactor.logger.CSLogUtil;
import com.cs.base.interactor.logger.CSLogger;
import com.cs.base.interactor.logger.exception.CSLoggerException;
import com.cs.base.interactor.utility.EntityUtil;

@Configuration
@Aspect
public class NotificationStrategyAspect {

  @Autowired
  CSLogger             logger;

  @Autowired
  String               mode;

  private final Logger slf4jErrorLogger = LoggerFactory.getLogger("com.SL4JErrorLogger.log");

  @Before("execution(public * com.cs.runtime.store.strategy.base.IEmailNotificationStrategy.execute(..))")
  public void beforeContentInteractors(JoinPoint joinPoint) throws CSLoggerException
  {
    try {
      String className = joinPoint.getTarget().getClass().getSimpleName();

      Object[] args = joinPoint.getArgs();
      if (args != null && args.length > 0 && args[0] != null) {
        logger.info("Strategy_" + className, className, "request", args[0].toString());
      }
      else {
        logger.info("Strategy_" + className, className, "request", null);
      }
    }
    catch (Exception e) {
      if (mode.toUpperCase().equals("DEVELOPMENT")) {
        throw new CSLoggerException(e);
      }
      else {
        String errorString = CSLogUtil.getStackTrace(e);
        slf4jErrorLogger.error(errorString);
      }
    }

  }

  @AfterThrowing(
      pointcut = "execution(public * com.cs.runtime.store.strategy.base.IEmailNotificationStrategy.execute(..))",
      throwing = "error")
  public void afterThrowingContentInteractors(JoinPoint joinPoint, Throwable error)
      throws CSLoggerException
  {
    try {
      String className = joinPoint.getTarget().getClass().getSimpleName();

      logger.error("Strategy_" + className, className, error.getMessage(), EntityUtil.quoteIt(CSLogUtil.getStackTrace(error)));
    }
    catch (Exception e) {
      if (mode.toUpperCase().equals("DEVELOPMENT")) {
        throw new CSLoggerException(e);
      }
      else {
        String errorString = CSLogUtil.getStackTrace(e);
        slf4jErrorLogger.error(errorString);
      }
    }

  }

}
*/
