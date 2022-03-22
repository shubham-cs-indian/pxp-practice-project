package com.cs.core.config.interactor.model.processevent;

public class GetProcessEndpointEventModel extends GetProcessEventModel
    implements IGetProcessEndpointEventModel {
  
  private static final long serialVersionUID = 1L;
  protected String          endpointId;
  protected String          systemId;
  protected String          processLabel;
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public String getSystemId()
  {
    if (systemId == null)
      return "-1";
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public String getProcessLabel()
  {
    return processLabel;
  }
  
  @Override
  public void setProcessLabel(String processLabel)
  {
    this.processLabel = processLabel;
  }
}
