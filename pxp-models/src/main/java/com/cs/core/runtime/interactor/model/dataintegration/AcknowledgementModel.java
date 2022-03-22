package com.cs.core.runtime.interactor.model.dataintegration;

public class AcknowledgementModel implements IAcknowledgementModel {
  
  private static final long               serialVersionUID = 1L;
  private IAcknowledgementPropertiesModel properties;
  private IAcknowledgementHeadersModel    header;
  private IStandardAckBodyModel           body;
  
  @Override
  public IAcknowledgementPropertiesModel getProperties()
  {
    return properties;
  }
  
  @Override
  public void setProperties(IAcknowledgementPropertiesModel properties)
  {
    this.properties = properties;
  }
  
  @Override
  public IAcknowledgementHeadersModel getHeader()
  {
    return header;
  }
  
  @Override
  public void setHeader(IAcknowledgementHeadersModel headers)
  {
    this.header = headers;
  }
  
  @Override
  public IStandardAckBodyModel getBody()
  {
    return body;
  }
  
  @Override
  public void setBody(IStandardAckBodyModel body)
  {
    this.body = body;
  }
}
