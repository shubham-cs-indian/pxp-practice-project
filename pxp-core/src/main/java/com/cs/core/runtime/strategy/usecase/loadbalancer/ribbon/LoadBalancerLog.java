package com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.netflix.loadbalancer.BaseLoadBalancer;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.*;

public class LoadBalancerLog {
  
  private static final String orient           = "orient-loadbalancer";
  private static final String elastic          = "elastic-loadbalancer";
  private static final String simpleDateFormat = "E yyyy.MM.dd 'at' kk:mm:ss a zzz";
  private static final String separator        = "---------------------------------------------------------------------------------------------------------------------------------\n";
  protected static Logger     elasticLog;
  protected static Logger     orientLog;
  String                      elasticLoadBalancerLog, orientLoadBalancerLog;
  String                      className;
  private Handler             elasticFileHandler, orientFileHandler;
  
  public LoadBalancerLog(String loadBalLogFilePath)
  {
    if (loadBalLogFilePath.contains("elastic")) {
      this.elasticLoadBalancerLog = loadBalLogFilePath;
      
    }
    else {
      this.orientLoadBalancerLog = loadBalLogFilePath;
    }
  }
  
  public static String getClassName(BaseLoadBalancer loadbal)
  {
    String clientName = loadbal.getName();
    switch (clientName) {
      case elastic:
        return elastic;
      case orient:
        return orient;
    }
    return null;
  }
  
  private static String getDate()
  {
    Date now = new Date();
    SimpleDateFormat dateformat = new SimpleDateFormat(simpleDateFormat);
    dateformat.setTimeZone(TimeZone.getTimeZone("IST"));
    String date = dateformat.format(now);
    
    return date;
  }
  
  public String getElasticLoadBalancerLog()
  {
    return elasticLoadBalancerLog;
  }
  
  public void setElasticLoadBalancerLog(String elasticLoadBalancerLog)
  {
    this.elasticLoadBalancerLog = elasticLoadBalancerLog;
  }
  
  public String getOrientLoadBalancerLog()
  {
    return orientLoadBalancerLog;
  }
  
  public void setOrientLoadBalancerLog(String orientLoadBalancerLog)
  {
    this.orientLoadBalancerLog = orientLoadBalancerLog;
  }
  
  public void logStats(URI uri, BaseLoadBalancer loadbal)
  {
    
    className = getClassName(loadbal);
    if (className.equals(elastic)) {
      elasticLog(uri, loadbal.getLoadBalancerStats()
          .toString());
    }
    else {
      orientLog(uri, loadbal.getLoadBalancerStats()
          .toString());
    }
  }
  
  private void elasticLog(URI uri, String stats)
  {
    elasticLog = Logger.getLogger(elastic);
    elasticLog.setLevel(Level.FINE);
    try {
      if (elasticFileHandler == null) {
        elasticFileHandler = new FileHandler(elasticLoadBalancerLog);
      }
      elasticFileHandler.setFormatter(new SimpleFormatter());
      elasticLog.setUseParentHandlers(false);
      
      elasticLog.addHandler(elasticFileHandler);
      String logMess = getLogMess(uri, stats);
      elasticLog.log(Level.INFO, logMess);
      
    }
    catch (SecurityException | IOException e) {
      // TODO Auto-generated catch block
      RDBMSLogger.instance().exception(e);
    }
  }
  
  private String getLogMess(URI uri, String stats)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(getDate() + "\n");
    sb.append(uri + "\n");
    sb.append(stats + "\n");
    sb.append(separator);
    return sb.toString();
  }
  
  private void orientLog(URI uri, String stats)
  {
    orientLog = Logger.getLogger(orient);
    orientLog.setLevel(Level.FINE);
    try {
      if (orientFileHandler == null) {
        orientFileHandler = new FileHandler(orientLoadBalancerLog);
      }
      orientFileHandler.setFormatter(new SimpleFormatter());
      orientLog.setUseParentHandlers(false);
      
      orientLog.addHandler(orientFileHandler);
      String logMess = getLogMess(uri, stats);
      orientLog.log(Level.INFO, logMess);
      
    }
    catch (SecurityException | IOException e) {
      // TODO Auto-generated catch block
      RDBMSLogger.instance().exception(e);
    }
  }
}
