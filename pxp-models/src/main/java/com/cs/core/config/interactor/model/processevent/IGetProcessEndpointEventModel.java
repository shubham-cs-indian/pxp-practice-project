package com.cs.core.config.interactor.model.processevent;

public interface IGetProcessEndpointEventModel extends IGetProcessEventModel {
  
  public static final String ENDPOINTID    = "endpointId";
  public static final String SYSTEMID      = "systemId";
  public static final String PROCESS_LABEL = "processLabel";
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getProcessLabel();
  
  public void setProcessLabel(String processLabel);
}
