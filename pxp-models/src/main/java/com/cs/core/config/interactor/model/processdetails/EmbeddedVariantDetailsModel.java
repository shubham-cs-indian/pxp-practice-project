package com.cs.core.config.interactor.model.processdetails;

public class EmbeddedVariantDetailsModel extends ProcessModel
    implements IEmbeddedVariantDetailsModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          klassInstanceId;
  protected String          parentId;
  protected String          batchId;
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  public String getParentId()
  {
    return parentId;
  }
  
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  public String getBatchId()
  {
    return batchId;
  }
  
  public void setBatchId(String batchId)
  {
    this.batchId = batchId;
  }
}
