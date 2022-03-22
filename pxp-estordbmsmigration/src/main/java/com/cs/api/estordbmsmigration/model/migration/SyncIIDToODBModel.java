package com.cs.api.estordbmsmigration.model.migration;

import java.util.List;

public class SyncIIDToODBModel implements ISyncIIDToODBModel {
  
  private static final long   serialVersionUID = 1L;
  private String              vertexType       = "";
  private List<IIIDCodeModel> iidCodeModel;
  
  @Override
  public void setVertexType(String vertextType)
  {
    this.vertexType = vertextType;
  }
  
  @Override
  public String getVertexType()
  {
    return vertexType;
  }
  
  @Override
  public void setList(List<IIIDCodeModel> iidCodeModel)
  {
    this.iidCodeModel = iidCodeModel;
  }
  
  @Override
  public List<IIIDCodeModel> getList()
  {
    return iidCodeModel;
  }
  
}
