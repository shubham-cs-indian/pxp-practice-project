package com.cs.api.estordbmsmigration.model.migration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISyncCongifToRDBMSRequestModel extends IModel {
  
  public void setVertexType(String vertexType);
  
  public String getVertexType();
  
  public void setFrom(int from);
  
  public int getFrom();
  
  public void setSize(int size);
  
  public int getSize();
  
}
