package com.cs.core.runtime.interactor.model.dataintegration;

public class ReceiveModel implements IReceiveModel {
  
  private String data;
  
  @Override
  public String getData()
  {
    return data;
  }
  
  @Override
  public void setData(String data)
  {
    this.data = data;
  }
}
