package com.cs.api.estordbmsmigration.model.migration;

public class SyncCongifToRDBMSRequestModel implements ISyncCongifToRDBMSRequestModel {
  
  private static final long serialVersionUID = 1L;
  private String            vertexTpe        = "";
  private int               from;
  private int               size;
  
  @Override
  public void setVertexType(String vertexType)
  {
    this.vertexTpe = vertexType;
  }
  
  @Override
  public String getVertexType()
  {
    return vertexTpe;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
  @Override
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setSize(int size)
  {
    this.size = size;
  }
  
  @Override
  public int getSize()
  {
    return size;
  }
  
}
