package com.cs.base.saml.configuration.custom;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.saml.SAMLEntryPoint;

import com.cs.constants.CommonConstants;

public class CustomSamlEntryPoint extends SAMLEntryPoint {
  
  static String username;
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException
  {
    synchronized (this) {
      username = request.getParameter(CommonConstants.USER_NAME_PROPERTY);
      super.doFilter(request, response, chain);
      username = null;
    }
    
  }
  
  public static String getUserName()
  {
    return username;
  }
  
}