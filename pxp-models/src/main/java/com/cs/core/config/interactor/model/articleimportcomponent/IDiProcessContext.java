package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDiProcessContext extends IProcessContext {
  
  public final String REQUEST_MODEL = "requestModel";
  public final String TALEND_DATA   = "talendData";
  public final String JMS_DATA      = "jsmData";
  
  public IModel getRequestModel();
  
  public void setRequestModel(IModel requestModel);
  
  public String getDataTalendData();
  
  public void setDataTalendData(String talendData);
  
  public String getDataJmsData();
  
  public void setDataJmsData(String jmsData);
}
