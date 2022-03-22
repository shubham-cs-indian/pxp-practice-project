package com.cs.core.runtime.strategy.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ApplicationUtils {
  
  @Autowired
  private ApplicationContext appContext;
  private String             localIpAddress;
  private String             applicationName;
  private Integer            portNumber = 8092;
  private String             protocol   = "http://";
  
  public String getLocalHostIpAddress() throws UnknownHostException
  {
    return InetAddress.getLocalHost()
        .getHostAddress();
  }
  
  public void setLocalHostIpAddress(String ipAddress)
  {
    this.localIpAddress = ipAddress;
  }
  
  public String getApplicationName() throws UnknownHostException
  {
    return appContext.getApplicationName();
  }
  
  public void setApplicationName(String applicationName)
  {
    this.applicationName = applicationName;
  }
  
  public String getProtocol() throws UnknownHostException
  {
    return protocol;
  }
  
  public void setProtocol(String protocol)
  {
    this.protocol = protocol;
  }
  
  public Integer getPortNumber() throws UnknownHostException
  {
    return portNumber;
  }
  
  public void setPortNumber(Integer portNumber)
  {
    this.portNumber = portNumber;
  }
  
  public String getCompleteAddress()
  {
    return this.protocol + this.localIpAddress + this.portNumber + this.applicationName;
  }
}
