package com.cs.base.interactor.interceptor;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomExceptionHandler extends ExceptionMappingAuthenticationFailureHandler{
  
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException
  {

    HashMap<String, String> failureUrlMap = new HashMap<String, String>();
    failureUrlMap.put(BadCredentialsException.class.getName(),
        "/login?authfailure=true");
    failureUrlMap.put(AuthenticationServiceException.class.getName(),
        "/login?userwithoutrole=true");
    failureUrlMap.put(InternalAuthenticationServiceException.class.getName(),
        "/login?incorrectdatasize=true");
    String url = failureUrlMap.get(exception.getClass().getName());

    if (url != null) {
      getRedirectStrategy().sendRedirect(request, response, url);
    }
    else {
      super.onAuthenticationFailure(request, response, exception);
    }
    
  }
}
