package com.cs.core.bgprocess.rest;

import com.cs.core.rdbms.driver.RDBMSLogger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Generic way to manage correctly exceptions that can happen in REST service
 * execution
 *
 * @author vallee
 */
public class ServiceException extends Throwable implements ExceptionMapper<Throwable> {
  
  @Override
  public Response toResponse(Throwable e)
  {
    RDBMSLogger.instance()
        .exception(e);
    StringBuilder message = new StringBuilder(e.getMessage());
    message.append("\n\n")
        .append(RDBMSLogger.instance()
            .getExceptionStack(e));
    return Response.status(500)
        .entity(message.toString())
        .type("text/plain")
        .build();
  }
}
