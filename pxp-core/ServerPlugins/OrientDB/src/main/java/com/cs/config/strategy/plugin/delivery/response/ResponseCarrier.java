package com.cs.config.strategy.plugin.delivery.response;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.DevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.PluginExceptionModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.OHttpUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResponseCarrier {
  
  public static void successResponse(OHttpResponse iResponse, Object object) throws IOException
  {
    iResponse.send(OHttpUtils.STATUS_OK_CODE, OHttpUtils.STATUS_OK_DESCRIPTION,
        OHttpUtils.CONTENT_JSON, ObjectMapperUtil.writeValueAsString(object), null);
  }
  
  public static void failedResponse(OHttpResponse iResponse, PluginException e) throws IOException
  {
    
    PluginExceptionModel response = new PluginExceptionModel();
    response.setExceptionDetails(e.getExceptionDetails());
    response.setDevExceptionDetails(e.getDevExceptionDetails());
    response.setExceptionClass(e.getClass()
        .getName());
    
    iResponse.send(OHttpUtils.STATUS_INTERNALERROR_CODE,
        OHttpUtils.STATUS_INTERNALERROR_DESCRIPTION, OHttpUtils.CONTENT_JSON,
        ObjectMapperUtil.writeValueAsString(response), null);
  }
  
  public static void failedResponse(OHttpResponse iResponse, Throwable e) throws IOException
  {
    
    IExceptionDetailModel exceptionDetail = new ExceptionDetailModel();
    exceptionDetail.setKey(e.getClass()
        .getSimpleName());
    List<IExceptionDetailModel> exceptionDetails = new ArrayList<>();
    exceptionDetails.add(exceptionDetail);
    
    IDevExceptionDetailModel devExceptionDetail = new DevExceptionDetailModel();
    devExceptionDetail.setDetailMessage(e.getMessage());
    devExceptionDetail.setKey(e.getClass()
        .getSimpleName());
    devExceptionDetail.setStack(e.getStackTrace());
    devExceptionDetail.setExceptionClass(e.getClass()
        .getName());
    List<IDevExceptionDetailModel> devExceptionDetails = new ArrayList<>();
    devExceptionDetails.add(devExceptionDetail);
    
    PluginExceptionModel response = new PluginExceptionModel();
    response.setExceptionDetails(exceptionDetails);
    response.setDevExceptionDetails(devExceptionDetails);
    response.setExceptionClass(e.getClass()
        .getName());
    
    iResponse.send(OHttpUtils.STATUS_INTERNALERROR_CODE,
        OHttpUtils.STATUS_INTERNALERROR_DESCRIPTION, OHttpUtils.CONTENT_JSON,
        ObjectMapperUtil.writeValueAsString(response), null);
  }
  
  public static String stackTraceToString(Throwable e)
  {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
      sb.append(element.toString());
      sb.append("\n");
    }
    return sb.toString();
  }
}
