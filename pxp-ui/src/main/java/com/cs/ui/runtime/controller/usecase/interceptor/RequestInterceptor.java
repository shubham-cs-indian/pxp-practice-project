package com.cs.ui.runtime.controller.usecase.interceptor;

import com.cs.core.runtime.interactor.usecase.threadlocal.ApplicationThreadLocal;
import com.cs.core.runtime.interactor.usecase.threadlocal.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor extends HandlerInterceptorAdapter {
  
  @Autowired
  ApplicationThreadLocal threadLocal;
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception
  {
    String requestId = request.getParameter("requestId");
    String sessionId = request.getParameter("sessionId");
    Context requestContext = new Context();
    requestContext.setRequestId(requestId);
    requestContext.setSessionId(sessionId);
    threadLocal.setValue(requestContext);
    
    return true;
  }
  
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception
  {
    super.postHandle(request, response, handler, modelAndView);
    if (response instanceof HttpServletResponse) {
      ((HttpServletResponse) response).setHeader("Content-Security-Policy",
          "default-src 'self'; style-src 'self' 'unsafe-inline'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; "
          + "frame-ancestors 'self'; font-src 'self' data: ; img-src 'self' data: blob:");
      ((HttpServletResponse)response).setHeader("X-Frame-Options", "sameorigin");
      Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
      boolean firstHeader = true;
      for (String header : headers) {  
          if (firstHeader) {
              response.setHeader("Set-Cookie", String.format("%s; %s", header, "SameSite=Strict"));
              firstHeader = false;
              continue;
          }
          response.addHeader("Set-Cookie", String.format("%s; %s", header, "SameSite=Strict"));
      }
    }
  }
}
