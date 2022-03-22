package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAcknowledgementModel extends IModel {
  
  public static final String PROPERTIES = "properties";
  public static final String HEADERS    = "headers";
  public static final String BODY       = "body";
  
  public IAcknowledgementPropertiesModel getProperties();
  
  public void setProperties(IAcknowledgementPropertiesModel properties);
  
  public IAcknowledgementHeadersModel getHeader();
  
  public void setHeader(IAcknowledgementHeadersModel headers);
  
  public IStandardAckBodyModel getBody();
  
  public void setBody(IStandardAckBodyModel body);
}
