package com.cs.api.estordbmsmigration.model.migration;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISyncIIDToODBModel extends IModel {
  
  public void setVertexType(String vertextType);
  
  public String getVertexType();
  
  public void setList(List<IIIDCodeModel> iidCodeModel);
  
  public List<IIIDCodeModel> getList();
  
}
