package com.cs.ui.runtime.controller.usecase.interceptor;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.configuration.RequestPendingException;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.logger.InteractorThreadData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.model.response.RESTBulkResponseModel;
import com.cs.ui.runtime.controller.model.response.RESTFailedObjectResponseModel;
import com.cs.ui.runtime.controller.model.response.RESTSuccessResponseModel;

@Component
public class BaseController {
  
  @Autowired
  TransactionThreadData logThread;
  
  @Autowired
  InteractorThreadData  interactorThreadData;
  
  protected IRESTModel createResponse(IModel reponseObject)
  {
    logging("success", null);
    RESTSuccessResponseModel response = new RESTSuccessResponseModel();
    response.setSuccess(reponseObject);
    return response;
  }
  
  protected IRESTModel createResponse(IBulkResponseModel reponseObject)
  {
    logging("success", null);
    RESTBulkResponseModel response = new RESTBulkResponseModel();
    response.setSuccess(reponseObject.getSuccess());
    response.setFailure(reponseObject.getFailure());
    return response;
  }
  
  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void handle(HttpMessageNotReadableException e)
  {
    RDBMSLogger.instance().exception(e);
  }
  
  @ExceptionHandler(Throwable.class)
  public @ResponseBody RESTFailedObjectResponseModel handleException(Throwable exception,
      HttpServletResponse httpRes)
  {
    exception.printStackTrace();
    RDBMSLogger.instance().exception(exception);
    logging("failure", exception);
    httpRes.setStatus(500);
    RESTFailedObjectResponseModel response = new RESTFailedObjectResponseModel(exception);
    return response;
  }
  
  @ExceptionHandler(PluginException.class)
  public @ResponseBody RESTFailedObjectResponseModel handlePluginException(
      PluginException exception, HttpServletResponse httpRes) throws Exception
  {
    logging("failure", exception);
    RDBMSLogger.instance().exception(exception);
    httpRes.setStatus(500);
    RESTFailedObjectResponseModel response = new RESTFailedObjectResponseModel(exception);
    return response;
  }
  
  @ExceptionHandler(RequestPendingException.class)
  public @ResponseBody RESTFailedObjectResponseModel handlePluginException(
      RequestPendingException exception, HttpServletResponse httpRes) throws Exception
  {
    logging("pending", exception);
    RDBMSLogger.instance().exception(exception);
    httpRes.setStatus(202);
    RESTFailedObjectResponseModel response = new RESTFailedObjectResponseModel(exception);
    return response;
  }
  
  /*@ExceptionHandler(AssetUploadInProgressException.class)
  public @ResponseBody RESTFailedObjectResponseModel handleInProgressStatus(
      AssetUploadInProgressException exception, HttpServletResponse httpRes) throws Exception
  {
    logging("IN PROGRESS", exception);
    httpRes.setStatus(202);
    RESTFailedObjectResponseModel response = new RESTFailedObjectResponseModel(exception);
    return response;
  }*/
  
  public void logging(String responseStatus, Throwable exception)
  {
    TransactionData controller = this.logThread.getTransactionData();
    controller.setEndTime(System.currentTimeMillis());
    controller.setInteractorData(interactorThreadData.getInteractorData());
    controller.setExecutionStatus(responseStatus);
    controller.setException(exception);
    logThread.removeTransactionData();
  }
}
